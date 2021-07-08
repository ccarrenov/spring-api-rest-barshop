package com.barshop.app.models.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import lombok.Getter;

@Getter
public class MapperConvert <D,E>{

	private static final Logger LOGGER = Logger.getLogger(MapperConvert.class.getName());

	Set<MapperFieldAnnotation> fields;

	public MapperConvert() {
		fields = new LinkedHashSet<>();
	}

	private void loadFieldsDTO(D obj1) {
		final String className = obj1.getClass().getName();
		LOGGER.debug("origin clazzName: " + obj1.getClass());				
		LOGGER.debug("origin object: " + obj1);		
		try {
			final Field[] fieldsC = Class.forName(className).getDeclaredFields();
			for (final Field field : fieldsC) {

				if (field.isAnnotationPresent(MapperAnnotation.class)) {
					Method getMethod = obj1.getClass().getMethod(getField(field.getName()));
					Object value = getMethod.invoke(obj1, new Object[] {});
					MapperFieldAnnotation fieldM = new MapperFieldAnnotation(field.getName(), field.getType(), value);
					fields.add(fieldM);
				}
			}
		} catch (final Exception e) {
			LOGGER.debug("Error Reflexion Class" + e.getMessage());
		}
	}

	private E createEntity(Class<E> clazzName) {
		LOGGER.debug("destiny clazzName: " + clazzName);
		E newInstance = null;
		try {
			Constructor<E> constructorSinParametros = clazzName.getConstructor();
			newInstance = constructorSinParametros.newInstance();
			for (MapperFieldAnnotation fieldM : fields) {
				setAttribute(newInstance, fieldM);
			}
		} catch (Exception e) {
			LOGGER.debug("Error New Instance Reflexion Class" + e.getMessage());
		}
		LOGGER.debug("destiny object: " + newInstance);						
		return newInstance;
	}

	private <T> T setAttribute(T clazz, MapperFieldAnnotation field) {
		LOGGER.debug("setAttribute: " + field);		
		try {
			Method method = clazz.getClass().getMethod(setField(field.getAttribute()), field.getType());
			method.invoke(clazz, field.getValue());
		} catch (Exception e) {
			LOGGER.debug("Error Load Atributte Reflexion Class " + e.getMessage());
			e.printStackTrace();
		}

		return clazz;
	}

	private String getField(String attribute) {
		return "get" + attribute.substring(0, 1).toUpperCase() + attribute.substring(1, attribute.length());
	}

	private String setField(String attribute) {
		return "set" + attribute.substring(0, 1).toUpperCase() + attribute.substring(1, attribute.length());
	}
	
	public E convertObject(D obj1, Class<E> obj2) {		
		loadFieldsDTO(obj1);
		return createEntity(obj2);
	}
	
	public List<E> convertListObjects(List<D> obj1, Class<E> obj2) {		
		List<E> newList = new ArrayList<E>();
		for(D obj : obj1) {
			newList.add(convertObject(obj,obj2));
		}
		return newList;
	}
}
