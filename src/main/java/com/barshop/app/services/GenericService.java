package com.barshop.app.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.barshop.app.enums.WSMessageEnums;
import com.barshop.app.exception.NumberPageException;
import com.barshop.app.models.dao.GenericDAO;
import com.barshop.app.models.dto.DataAccessObject;
import com.barshop.app.models.entity.Entity;

import lombok.Setter;

@Setter
@Service
public class GenericService<D extends DataAccessObject, E extends Entity, I> extends BaseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenericService.class);

    private static final String FIND_ALL = ".findAll";

    private static final String CREATE_OR_UPDATE = ".createOrUpdate";

    private static final String CREATE_ALL = ".createAll";

    private static final String FIND_BY_ID = ".findById";

    private static final String DELETE_BY_ID = ".deleteById";

    @Autowired
    private GenericDAO<D, E, I> dao;

    public ResponseEntity<Object> findAll( Class<D> clazzNameD, E clazzE, String resource, String messageError ) {
        LOGGER.info(WSMessageEnums.INIT.getValue(), resource, FIND_ALL);
        try {
            return response(dao.findAll(clazzNameD, clazzE), HttpStatus.OK);
        } catch (Exception ex) {
            LOGGER.error(WSMessageEnums.ERROR.getValue(), ex);
            return msg(WSMessageEnums.ERROR.getValue(), WSMessageEnums.ERROR_FIND.getValue().replaceAll("\\$1", messageError), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            LOGGER.info(WSMessageEnums.FINISH.getValue(), resource, FIND_ALL);
        }
    }

    public ResponseEntity<Object> findAll( Class<D> clazzNameD, E clazzE, int page, int size, String resource, String messageError ) {
        LOGGER.info(WSMessageEnums.INIT.getValue(), resource, FIND_ALL);
        try {
            return response(dao.findAll(clazzE, PageRequest.of(page, size), clazzNameD), HttpStatus.OK);
        } catch (NullPointerException ex) {
            LOGGER.debug(ex.getMessage(), ex);
            return msg(WSMessageEnums.ERROR_NOT_COUNT.getValue(), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException ex) {
            LOGGER.error(WSMessageEnums.ERROR.getValue(), ex);
            return msg(WSMessageEnums.ERROR.getValue() + ": " + WSMessageEnums.ERROR_ILEGAL_PAGE.getValue(), HttpStatus.BAD_REQUEST);
        } catch (NumberPageException ex) {
            LOGGER.error(WSMessageEnums.ERROR.getValue(), ex);
            return msg(WSMessageEnums.ERROR.getValue() + ": " + ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            LOGGER.error(WSMessageEnums.ERROR.getValue(), ex);
            return msg(WSMessageEnums.ERROR.getValue() + ": " + WSMessageEnums.ERROR_FIND.getValue().replaceAll("$1", messageError), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            LOGGER.info(WSMessageEnums.FINISH.getValue(), resource, FIND_ALL);
        }
    }

    public ResponseEntity<Object> createOrUpdate( Class<D> clazzNameD, E clazzE, Class<I> clazzNameI, String resource, D clazzD, String messageError ) {
        LOGGER.info(WSMessageEnums.INIT.getValue(), resource, CREATE_OR_UPDATE);
        try {
            return response(dao.createOrUpdate(clazzNameD, clazzE, clazzNameI, clazzD), HttpStatus.OK);
        } catch (DuplicateKeyException ex) {
            LOGGER.debug(WSMessageEnums.ERROR.getValue(), ex);
            LOGGER.warn(ex.getMessage());
            return msg(WSMessageEnums.ERROR_DUPLICATE_REGISTER.getValue(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (DataIntegrityViolationException ex) {
            LOGGER.debug(WSMessageEnums.ERROR_INTEGRITY_REGISTER.getValue(), ex);
            LOGGER.warn(ex.getMessage());
            return msg(WSMessageEnums.ERROR_INTEGRITY_REGISTER.getValue(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception ex) {
            LOGGER.debug(WSMessageEnums.ERROR.getValue(), ex);
            LOGGER.error(ex.getMessage());
            return msg(WSMessageEnums.ERROR.getValue(), WSMessageEnums.ERROR_SAVE_OR_UPDATE.getValue().replaceAll("\\$1", messageError), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            LOGGER.info(WSMessageEnums.FINISH.getValue(), resource, CREATE_OR_UPDATE);
        }
    }

    public ResponseEntity<Object> createAll( Class<D> clazzNameD, E classE, Class<I> classNameI, String resource, List<D> object, int lot, String messageError ) {
        LOGGER.info(WSMessageEnums.INIT.getValue(), resource, CREATE_ALL);
        try {
            return response(dao.createAll(clazzNameD, classE, classNameI, object, lot), HttpStatus.OK);
        } catch (DuplicateKeyException ex) {
            LOGGER.debug(WSMessageEnums.ERROR.getValue(), ex);
            LOGGER.warn(ex.getMessage());
            return msg(WSMessageEnums.ERROR_DUPLICATE_REGISTER.getValue(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (DataIntegrityViolationException ex) {
            LOGGER.debug(WSMessageEnums.ERROR_INTEGRITY_REGISTER.getValue(), ex);
            LOGGER.warn(ex.getMessage());
            return msg(WSMessageEnums.ERROR_INTEGRITY_REGISTER.getValue(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception ex) {
            LOGGER.debug(WSMessageEnums.ERROR.getValue(), ex);
            LOGGER.error(ex.getMessage());
            return msg(WSMessageEnums.ERROR.getValue(), WSMessageEnums.ERROR_SAVE_OR_UPDATE.getValue().replace("\\$1", messageError), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            LOGGER.info(WSMessageEnums.FINISH.getValue(), resource, CREATE_ALL);
        }
    }

    public ResponseEntity<Object> findById( Class<D> clazzNameD, E clazzE, String resource, I id ) {
        LOGGER.info(WSMessageEnums.INIT.getValue(), resource, FIND_BY_ID);
        try {
            return responseEmpty(dao.findById(clazzNameD, clazzE, id), HttpStatus.OK);
        } catch (NullPointerException ex) {
            StringBuilder error = new StringBuilder(WSMessageEnums.ERROR_NOT_FOUND.getValue().replaceAll("\\$1", id.toString()));
            LOGGER.debug(error.toString());
            return msg(error.toString(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            StringBuilder error = new StringBuilder(WSMessageEnums.ERROR_FIND.getValue().replaceAll("\\$1", id.toString()));
            LOGGER.debug(error.toString(), ex);
            LOGGER.error(ex.getMessage(), ex);
            return msg(error.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            LOGGER.info(WSMessageEnums.FINISH.getValue(), resource, FIND_BY_ID);
        }
    }

    public ResponseEntity<Object> count( E clazzE, String resource ) {
        LOGGER.info(WSMessageEnums.INIT.getValue(), resource, FIND_BY_ID);
        try {
            return responseEmpty(dao.count(clazzE), HttpStatus.OK);
        } catch (NullPointerException ex) {
            LOGGER.debug(ex.getMessage(), ex);
            return msg(WSMessageEnums.ERROR_NOT_COUNT.getValue(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            LOGGER.debug(ex.getMessage(), ex);
            return msg(WSMessageEnums.ERROR_NOT_COUNT.getValue(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            LOGGER.info(WSMessageEnums.FINISH.getValue(), resource, FIND_BY_ID);
        }
    }

    public ResponseEntity<Object> deleteById( E clazzE, String resource, I id ) {
        LOGGER.info(WSMessageEnums.INIT.getValue(), resource, DELETE_BY_ID);
        try {
            dao.deleteById(clazzE, id);
            return msg(WSMessageEnums.SUCCESS_DELETE.getValue(), HttpStatus.OK);
        } catch (EmptyResultDataAccessException ex) {
            LOGGER.debug(WSMessageEnums.ERROR.getValue(), ex);
            LOGGER.warn(ex.getMessage(), ex);
            return msg(WSMessageEnums.ERROR_NOT_FOUND.getValue(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            LOGGER.debug(WSMessageEnums.ERROR.getValue(), ex);
            LOGGER.error(ex.getMessage(), ex);
            return msg(WSMessageEnums.ERROR_DELETE.getValue(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            LOGGER.info(WSMessageEnums.FINISH.getValue(), resource, DELETE_BY_ID);
        }
    }
}
