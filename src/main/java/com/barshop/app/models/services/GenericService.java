package com.barshop.app.models.services;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.barshop.app.enums.WSMessageEnums;
import com.barshop.app.models.dao.GenericDAO;
import com.barshop.app.models.dto.DataAccessObject;
import com.barshop.app.models.entity.Entity;

import lombok.Getter;

@Service
@Getter
public class GenericService<D extends DataAccessObject, E extends Entity, I> extends BaseService {

	private static final Logger LOGGER = Logger.getLogger(GenericService.class.getName());
	private static final String FIND_ALL = ".findAll";
	private static final String CREATE_OR_UPDATE = ".createOrUpdate";
	private static final String FIND_BY_ID = ".findById";
	private static final String DELETE_BY_ID = ".deleteById";

	@Autowired
	private GenericDAO<D, E, I> dao;

	public ResponseEntity<Object> findAll(Class<D> classD, Class<E> classE, String resource, String messageError) {
		LOGGER.info(WSMessageEnums.INIT.getValue() + resource + FIND_ALL);
		try {
			return response(dao.findAll(classD, classE), HttpStatus.OK);
		} catch (Exception ex) {
			LOGGER.error(WSMessageEnums.ERROR.getValue(), ex);
			return msg(WSMessageEnums.ERROR.getValue(),
					WSMessageEnums.ERROR_FIND.getValue().replace("$1", messageError), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			LOGGER.info(WSMessageEnums.FINISH.getValue() + resource + FIND_ALL);
		}
	}

//	public ResponseEntity<Object> findAll(Class<D> className, Pageable pageable, String resource, String messageError) {
//		LOGGER.info(WSMessageEnums.INIT.getValue() + resource + FIND_ALL);
//		try {
//			return response(findAllImplementation(className, pageable), HttpStatus.OK);
//		} catch (Exception ex) {
//			LOGGER.error(WSMessageEnums.ERROR.getValue(), ex);
//			return msg(WSMessageEnums.ERROR.getValue(),
//					WSMessageEnums.ERROR_FIND.getValue().replace("$1", messageError), HttpStatus.INTERNAL_SERVER_ERROR);
//		} finally {
//			LOGGER.info(WSMessageEnums.FINISH.getValue() + resource + FIND_ALL);
//		}
//	}

	public ResponseEntity<Object> createOrUpdate(Class<D> classD, Class<E> classE, Class<I> classI, String resource,
			D object, String messageError) {
		LOGGER.info(WSMessageEnums.INIT.getValue() + resource + CREATE_OR_UPDATE);
		try {
			return response(dao.createOrUpdate(classD, classE, classI, object), HttpStatus.OK);
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
			return msg(WSMessageEnums.ERROR.getValue(),
					WSMessageEnums.ERROR_SAVE_OR_UPDATE.getValue().replace("\\$1", messageError),
					HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			LOGGER.info(WSMessageEnums.FINISH.getValue() + resource + CREATE_OR_UPDATE);
		}
	}
	
	public ResponseEntity<Object> createAll(Class<D> classD, Class<E> classE, Class<I> classI, String resource,
			List<D> object, String messageError) {
		LOGGER.info(WSMessageEnums.INIT.getValue() + resource + CREATE_OR_UPDATE);
		try {
			return response(dao.createAll(classD, classE, classI, object, 3500), HttpStatus.OK);
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
			return msg(WSMessageEnums.ERROR.getValue(),
					WSMessageEnums.ERROR_SAVE_OR_UPDATE.getValue().replace("\\$1", messageError),
					HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			LOGGER.info(WSMessageEnums.FINISH.getValue() + resource + CREATE_OR_UPDATE);
		}
	}	

	public ResponseEntity<Object> findById(Class<D> classD, Class<E> classE, String resource, I id) {
		LOGGER.info(WSMessageEnums.INIT.getValue() + resource + FIND_BY_ID);
		try {
			return responseEmpty(dao.findById(classD, classE, id), HttpStatus.OK);
		} catch (NullPointerException ex) {
			StringBuilder error = new StringBuilder(
					WSMessageEnums.ERROR_NOT_FOND.getValue().replace("$1", id.toString()));
			LOGGER.info(error.toString());
			return msg(error.toString(), HttpStatus.NOT_FOUND);
		} catch (Exception ex) {
			StringBuilder error = new StringBuilder(
					WSMessageEnums.ERROR_FIND.getValue().replace("$1", "el producto ID: " + id));
			LOGGER.debug(error.toString(), ex);
			LOGGER.error(ex);
			return msg(error.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			LOGGER.info(WSMessageEnums.FINISH.getValue() + resource + FIND_BY_ID);
		}
	}

	public ResponseEntity<Object> deleteById(Class<E> classE, String resource, I id) {
		LOGGER.info(WSMessageEnums.INIT.getValue() + resource + DELETE_BY_ID);
		try {
			dao.deleteById(classE, id);
			return msg(WSMessageEnums.SUCCESS_DELETE.getValue(), HttpStatus.OK);
		} catch (EmptyResultDataAccessException ex) {
			LOGGER.debug(WSMessageEnums.ERROR.getValue(), ex);
			LOGGER.warn(ex);
			return msg(WSMessageEnums.ERROR_NOT_FOND.getValue(), HttpStatus.NOT_FOUND);
		} catch (Exception ex) {
			LOGGER.debug(WSMessageEnums.ERROR.getValue(), ex);
			LOGGER.error(ex);
			return msg(WSMessageEnums.ERROR_DELETE.getValue(), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			LOGGER.info(WSMessageEnums.FINISH.getValue() + resource + DELETE_BY_ID);
		}
	}
}
