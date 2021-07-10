package com.barshop.app.models.dao;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.barshop.app.models.dto.DataAccessObject;
import com.barshop.app.models.entity.Entity;
import com.barshop.app.models.mapper.MapperConvert;
import com.barshop.app.models.mapper.MapperFieldAnnotation;
import com.barshop.app.models.mapper.util.ReflexionUtil;

@Repository
public class GenericDAO<D extends DataAccessObject, E extends Entity, I> {

	private static final Logger LOGGER = Logger.getLogger(GenericDAO.class.getName());

	@PersistenceContext
	private EntityManager em;

	public GenericDAO() {
	}

	public GenericDAO(EntityManager em) {
		this.em = em;
	}

	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}

	public List<D> findAll(Class<D> classNameD, Class<E> classNameE) {
		MapperConvert<E, D> convert = new MapperConvert<>();
		MapperFieldAnnotation id = ReflexionUtil.fieldByAnnotation(classNameE, Id.class);
		LOGGER.debug("id: " + id);
		return convert.convertListObjects(findAll(classNameE, id.getAttribute()), classNameD);
	}

	public D findById(Class<D> classNameD, Class<E> classNameE, I id) {
		MapperConvert<E, D> convert = new MapperConvert<>();
		return convert.convertObject(findById(classNameE, id), classNameD);
	}

	@Transactional
	public void deleteById(Class<E> className, I id) {
		E entity = findById(className, id);
		delete(entity);
	}

	private boolean isIdEmpty(I id) {

		if (id == null)
			return true;
		try {
			if (Long.valueOf(id.toString()) == 0)
				return true;
		} catch (Exception ex) {
			LOGGER.debug("isIdEmpty: " + ex);
		}
		return false;
	}

	@Transactional
	public D createOrUpdate(Class<D> classNameD, Class<E> classNameE, Class<I> classNameI, D object) {
		MapperConvert<D, E> convert = new MapperConvert<>();
		MapperConvert<E, D> convert2 = new MapperConvert<>();
		try {
			E entity = convert.convertObject(object, classNameE);
			I id = findId(entity);
			if (isIdEmpty(id)) {
				LOGGER.debug("save");
				entity = save(entity);
				return convert2.convertObject(entity, classNameD);
			} else {
				LOGGER.debug("update");
				entity = update(entity);
				return convert2.convertObject(entity, classNameD);
			}
		} catch (Exception ex) {
			LOGGER.debug(ex);
			throw ex;
		}
	}

	@Transactional
	public List<D> createAll(Class<D> classNameD, Class<E> classNameE, Class<I> classNameI, List<D> objects, int lot) {
		MapperConvert<D, E> convert = new MapperConvert<>();
		MapperConvert<E, D> convert2 = new MapperConvert<>();
		List<D> objectsD = new ArrayList<>();
		try {
			List<E> entites = convert.convertListObjects(objects, classNameE);
			objectsD = convert2.convertListObjects(saveAll(entites, lot), classNameD);
		} catch (Exception ex) {
			LOGGER.debug(ex);
			throw ex;
		}
		return objectsD;
	}

	@SuppressWarnings("unchecked")
	private I findId(E obj) {
		final String className = obj.getClass().getName();
		LOGGER.debug("find id entity: " + obj);
		try {
			final Field[] fieldsC = Class.forName(className).getDeclaredFields();
			for (final Field field : fieldsC) {

				if (field.isAnnotationPresent(Id.class)) {
					Method getMethod = obj.getClass().getMethod(getField(field.getName()));
					return (I) getMethod.invoke(obj, new Object[] {});
				}
			}
		} catch (final Exception e) {
			LOGGER.debug("Error Reflexion Class" + e.getMessage());
		}

		return null;
	}

	private String getField(String attribute) {
		return "get" + attribute.substring(0, 1).toUpperCase() + attribute.substring(1, attribute.length());
	}

	@Transactional
	public E save(E entity) {
		em.persist(entity);
		em.flush();
		Object id = ReflexionUtil.getAttributeByAnnotation(entity, Id.class);
		LOGGER.debug("Id: " + id);
		return entity;
	}

	public List<E> saveAll(List<E> entities, int lot) {
		int i = 1;

		for (E entity : entities) {
			em.persist(entity);
			i++;
			if (i == lot) {
				em.persist(entity);
				em.flush();
				i = 1;
			}
		}

		if (i > 1)
			em.flush();

		for (E entity : entities) {
			Object id = ReflexionUtil.getAttributeByAnnotation(entity, Id.class);
			LOGGER.debug("Id: " + id);
		}
		return entities;
	}
	
	public List<E> mergeAll(List<E> entities, int lot) {
		int i = 1;

		for (E entity : entities) {
			em.persist(entity);
			i++;
			if (i == lot) {
				em.merge(entity);
				em.flush();
				i = 1;
			}
		}

		if (i > 1)
			em.flush();

		for (E entity : entities) {
			Object id = ReflexionUtil.getAttributeByAnnotation(entity, Id.class);
			LOGGER.debug("Id: " + id);
		}
		return entities;
	}	

	@Transactional
	public E update(E entity) {
		em.merge(entity);
		em.flush();
		return entity;
	}

	@Transactional
	public void delete(E entity) {
		em.remove(em.contains(entity) ? entity : em.merge(entity));
	}

	public E findById(Class<E> className, I id) {
		return em.find(className, id);
	}

	public List<E> findAll(Class<E> className, String attributeName) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		// Tipo de objeto obtenido al ejecutar query
		CriteriaQuery<E> query = builder.createQuery(className);
		// Agregando la tabla
		Root<E> variableRoot = query.from(className);
		// Creando zona de despliegue del SELECT COL1, COL2, ... , COLN
		query.select(variableRoot);
		// Creando zona condición de ORDEN ORDER BY 2
		query.orderBy(builder.asc(variableRoot.get(attributeName)));
		// Ejecutando consulta y listando todos los resultados
		// SELECT COL1, COL2, ... , COLN FROM TABLE
		return em.createQuery(query).getResultList();
	}

//	public Page<E> findAll(Class<E> className, PageRequest page) {
//		List <E> elements = new ArrayList<>();
//	    Query query = em.createQuery("from " + className.getCanonicalName());
//	    int pageNumber =page.getPageNumber();
//	    int pageSize = page.getPageSize();
//	    query.setFirstResult((pageNumber) * pageSize);
//	    query.setMaxResults(pageSize);
//	    try {
//		    elements = query.getResultList();
//		    Query queryTotal = em.createQuery
//		            ("select count(1) from " + className.getCanonicalName());
//		    long countResult = (long)queryTotal.getSingleResult();
//		    int i=(int)countResult;
//		    return new Page<>(elements, page.getPageNumber(),i);		    
//	    }catch(Exception ex) {
//	    	LOGGER.debug(ex);
//	    	throw ex;
//	    }
//	}

}
