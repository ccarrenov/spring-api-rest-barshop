package com.barshop.app.models.dao;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.barshop.app.enums.WSMessageEnums;
import com.barshop.app.exception.NumberPageException;
import com.barshop.app.models.dto.DataAccessObject;
import com.barshop.app.models.entity.Entity;
import com.barshop.app.models.mapper.MapperConvert;
import com.barshop.app.models.mapper.MapperFieldAnnotation;
import com.barshop.app.models.mapper.util.DecimalUtil;
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

    public void setEm( EntityManager em ) {
        this.em = em;
    }

    public List<D> findAll( Class<D> clazzNameD, E clazzE ) {

        MapperConvert<E, D> convert = new MapperConvert<>();
        MapperFieldAnnotation id = ReflexionUtil.fieldByAnnotation(clazzE, Id.class);
        LOGGER.debug("id: " + id);
        return convert.convertListObjects(findAll(clazzE, id.getAttribute()), clazzNameD);
    }

    public Page<D> findAll( E clazzE, PageRequest page, Class<D> clazzNameD ) throws NumberPageException {

        MapperFieldAnnotation id = ReflexionUtil.fieldByAnnotation(clazzE, Id.class);
        LOGGER.debug("id: " + id);
        return findAll(clazzE, id.getAttribute(), page, clazzNameD);
    }

    public D findById( Class<D> clazzNameD, E clazzE, I id ) {
        MapperConvert<E, D> convert = new MapperConvert<>();
        return convert.convertObject(findById(clazzE, id), clazzNameD);
    }

    @Transactional
    public void deleteById( E clazzE, I id ) {
        E entity = findById(clazzE, id);
        delete(entity);
    }

    private boolean isIdEmpty( I id ) {

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

    @SuppressWarnings("unchecked")
    @Transactional
    public D createOrUpdate( Class<D> clazzNameD, E clazzE, Class<I> clazzNameI, D clazzD ) {
        MapperConvert<D, E> convert = new MapperConvert<>();
        MapperConvert<E, D> convert2 = new MapperConvert<>();
        try {
            E entity = convert.convertObject(clazzD, (Class<E>) clazzE.getClass());
            I id = findId(entity);
            if (isIdEmpty(id)) {
                LOGGER.debug("save");
                entity = save(entity);
                return convert2.convertObject(entity, clazzNameD);
            } else {
                LOGGER.debug("update");
                entity = update(entity);
                return convert2.convertObject(entity, clazzNameD);
            }
        } catch (Exception ex) {
            LOGGER.debug(ex);
            throw ex;
        }
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public List<D> createAll( Class<D> clazzNameD, E clazzE, Class<I> clazzNameI, List<D> objects, int lot ) {
        MapperConvert<D, E> convert = new MapperConvert<>();
        MapperConvert<E, D> convert2 = new MapperConvert<>();
        List<D> objectsD = new ArrayList<>();
        try {
            String engine = System.getenv("ENGINE_DB");
            LOGGER.info("engine: " + engine);
            List<E> entites = convert.convertListObjects(objects, (Class<E>) clazzE.getClass());
            objectsD = convert2.convertListObjects(saveAll(entites, lot), clazzNameD);
        } catch (Exception ex) {
            LOGGER.debug(ex);
            throw ex;
        }
        return objectsD;
    }

    @SuppressWarnings("unchecked")
    private I findId( E obj ) {
        final String className = obj.getClass().getName();
        LOGGER.debug("find id entity: " + obj);
        try {
            final Field[] fieldsC = Class.forName(className).getDeclaredFields();
            for (final Field field : fieldsC) {

                if (field.isAnnotationPresent(Id.class) && field.isAnnotationPresent(GeneratedValue.class)) {
                    Method getMethod = obj.getClass().getMethod(getField(field.getName()));
                    Object[] ob = new Object[] {};
                    return (I) getMethod.invoke(obj, ob);
                }
            }
        } catch (final Exception e) {
            LOGGER.debug("Error Reflexion Class" + e.getMessage());
        }

        return null;
    }

    private String getField( String attribute ) {
        return "get" + attribute.substring(0, 1).toUpperCase() + attribute.substring(1, attribute.length());
    }

    @Transactional
    public E save( E entity ) {
        em.persist(entity);
        em.flush();
        Object id = ReflexionUtil.getAttributeByAnnotation(entity, Id.class);
        LOGGER.debug("Id: " + id);
        return entity;
    }

    public List<E> saveAll( List<E> entities, int lot ) {
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

    public List<E> mergeAll( List<E> entities, int lot ) {
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
    public E update( E entity ) {
        em.merge(entity);
        em.flush();
        return entity;
    }

    @Transactional
    public void delete( E entity ) {
        em.remove(em.contains(entity) ? entity : em.merge(entity));
    }

    @SuppressWarnings("unchecked")
    public E findById( E clazzE, I id ) {
        return em.find((Class<E>) clazzE.getClass(), id);
    }

    @SuppressWarnings("unchecked")
    public List<E> findAll( E clazzE, String attributeName ) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        // TYPE DEPLOY ZONE
        CriteriaQuery<E> query = (CriteriaQuery<E>) builder.createQuery(clazzE.getClass());
        // ADD ORIGIN TABLE
        Root<E> variableRoot = (Root<E>) query.from(clazzE.getClass());
        // CREATE QUERY SELECT COL1, COL2, ... , COLN
        query.select(variableRoot);
        // ADD ORDER BY ATTRIBUTENAME FROM ENTITY
        query.orderBy(builder.asc(variableRoot.get(attributeName)));
        // EXECUTE QUERY, RESULT LIST<E>
        return em.createQuery(query).getResultList();
    }

    @SuppressWarnings("unchecked")
    public Long count( E clazzE ) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        // TYPE DEPLOY ZONE
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        // ADD ORIGIN TABLE
        Root<E> variableRoot = (Root<E>) query.from(clazzE.getClass());
        // CREATE QUERY SELECT COUNT(*) FROM ENTITY
        query.select(builder.count(variableRoot));
        return em.createQuery(query).getSingleResult();
    }

    @SuppressWarnings("unchecked")
    public Page<D> findAll( E clazzE, String attributeName, PageRequest page, Class<D> clazzNameD ) throws NumberPageException {
        // COUNT ELEMENTS IN ENTITY
        Long count = count(clazzE);
        int pageNumber = page.getPageNumber();
        double pageSize = (double) page.getPageSize();
        long totalPage = DecimalUtil.roundDecimal(count / pageSize, 0, RoundingMode.UP).longValue();

        if (totalPage < pageNumber) {
            throw new NumberPageException(WSMessageEnums.ERROR_NUMBER_PAGE.getValue().replace("$1", String.valueOf(totalPage)));
        }

        CriteriaBuilder builder = em.getCriteriaBuilder();
        // TYPE DEPLOY ZONE
        CriteriaQuery<E> query = (CriteriaQuery<E>) builder.createQuery(clazzE.getClass());
        // ADD ORIGIN TABLE
        Root<E> variableRoot = (Root<E>) query.from(clazzE.getClass());
        // CREATE QUERY SELECT COL1, COL2, ... , COLN
        query.select(variableRoot);
        // ADD ORDER BY ATTRIBUTENAME FROM ENTITY
        query.orderBy(builder.asc(variableRoot.get(attributeName)));

        TypedQuery<E> typedQuery = em.createQuery(query);
        int firstResult = (pageNumber - 1) * page.getPageSize();
        typedQuery.setFirstResult(firstResult);
        typedQuery.setMaxResults((int) pageSize);
        List<E> elements = typedQuery.getResultList();
        MapperConvert<E, D> convert = new MapperConvert<>();
        List<D> elementsD = convert.convertListObjects(elements, clazzNameD);
        return new GenericPage<>(elementsD, pageNumber, (int) pageSize, (int) totalPage, Sort.by(attributeName), count);
    }

}
