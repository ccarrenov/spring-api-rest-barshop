package com.barshop.app.models.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.barshop.app.models.dto.DataAccessObject;
import com.barshop.app.models.entity.Entity;
import com.barshop.app.models.mapper.MapperConvert;

@Repository
public class IGenericDAO<D extends DataAccessObject, E extends Entity, I> {

	@PersistenceContext
	private EntityManager em;

	public IGenericDAO() {
	}

	public IGenericDAO(EntityManager em) {

		this.em = em;
	}

	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}
	
	public List<D> findAll(Class<D> classD, Class<E> classNameE) {
		MapperConvert<E, D> convert = new MapperConvert<>();
		return convert.convertListObjects(findAll(classNameE), classD);
	}	

	/**
	 * Método encargado crear un objeto
	 * 
	 * @param entity
	 */
	@Transactional
	public void save(E entity) {
		em.persist(entity);
	}

	/**
	 * Método encargado de borrar un objeto
	 * 
	 * @param entity
	 */
	@Transactional
	public void delete(E entity) {
		em.remove(em.contains(entity) ? entity : em.merge(entity));
	}

	/**
	 * Método encargado de actualizar un objeto
	 * 
	 * @param entity
	 */
	@Transactional
	public void update(E entity) {
		em.merge(entity);
	}

	/**
	 * Método encargado de buscar un registro dentro la tabla según su id
	 * 
	 * @param className
	 *            class que hace referencia a la tabla
	 * @param id
	 *            del elemento a buscar
	 * @return
	 */
	public E findById(Class<E> className, I id) {
		return em.find(className, id);
	}

	/**
	 * Método encargado de buscar una lista de una tabla especificada
	 * 
	 * @param className
	 *            class que hace referencia a la tabla
	 * @return lista con información de la tabla
	 */
	public List<E> findAll(Class<E> className) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		//Tipo de objeto obtenido al ejecutar query
		CriteriaQuery<E> query = builder.createQuery(className);
		// Agregando la tabla
		Root<E> variableRoot = query.from(className);
		// Creando zona de despliegue del SELECT COL1, COL2, ... , COLN 
		query.select(variableRoot);
		// Creando zona condición de ORDEN ORDER BY 2
		query.orderBy(builder.asc(variableRoot.get(variableRoot.getModel().getAttributes().iterator().next().getName())));
		// Ejecutando consulta y listando todos los resultados
		// SELECT COL1, COL2, ... , COLN FROM TABLE
		return em.createQuery(query).getResultList();
	}

}
