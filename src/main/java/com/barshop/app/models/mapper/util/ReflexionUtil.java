package com.barshop.app.models.mapper.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.barshop.app.models.mapper.MapperFieldAnnotation;

public final class ReflexionUtil {

	private static final Logger LOGGER = Logger.getLogger(ReflexionUtil.class.getName());

    private ReflexionUtil() {
        throw new IllegalStateException("EntityConstant class");
    }   

	public static String builderGetName(String attribute) {
		return "get" + attribute.substring(0, 1).toUpperCase() + attribute.substring(1, attribute.length());
	}

	public static String builderSetName(String attribute) {
		return "set" + attribute.substring(0, 1).toUpperCase() + attribute.substring(1, attribute.length());
	}

	public static <T> T setAttribute(T clazz, String attribute, Class<?> type, Object value) {
		MapperFieldAnnotation map = new MapperFieldAnnotation(attribute, type, value);
		return setAttribute(clazz, map);
	}

	public static <T> T setAttribute(T clazz, MapperFieldAnnotation field) {
		LOGGER.debug("setAttribute: " + field.getAttribute());
		try {
			Method method = clazz.getClass().getMethod(builderSetName(field.getAttribute()), field.getType());
			method.invoke(clazz, field.getValue());
		} catch (Exception e) {
			LOGGER.debug("Error Load Atributte Reflexion Class " + e.getMessage());
			e.printStackTrace();
		}
		return clazz;
	}

	public static <T> Object getAttribute(T clazz, String attribute) {
		LOGGER.debug("getAttribute: " + attribute);
		try {
			Method getMethod = clazz.getClass().getMethod(builderGetName(attribute));
			Object[] obj = new Object[] {};
			return getMethod.invoke(clazz, obj);
		} catch (Exception e) {
			LOGGER.debug("Error Load Atributte Reflexion Class " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	public static <T> Object getAttributeByAnnotation(T obj, Class<? extends Annotation> annotation) {
		LOGGER.debug("getAttribute: " + annotation);
		MapperFieldAnnotation field = fieldByAnnotation(obj, annotation);
		if (field != null)
			return getAttribute(obj, field.getAttribute());
		return null;
	}

	public static <T> MapperFieldAnnotation fieldByAnnotation(T obj, Class<? extends Annotation> annotation) {
		final String className = obj.getClass().getName();
		LOGGER.debug("origin clazzName: " + obj.getClass());
		LOGGER.debug("origin object: " + obj);
		try {
			final Field[] fieldsC = Class.forName(className).getDeclaredFields();
			for (final Field field : fieldsC) {

				if (field.isAnnotationPresent(annotation)) {
					Object value = getAttribute(obj, field.getName());
					return new MapperFieldAnnotation(field.getName(), field.getType(), value);
				}
			}
		} catch (final Exception e) {
			LOGGER.debug("Error Reflexion Class" + e.getMessage());
		}
		return null;
	}

	public static Object createNewInstance(String clazzName) {

		Class<?> c;
		try {
			c = Class.forName(clazzName);
			Constructor<?> builderWithoutParams = c .getConstructor();
			return builderWithoutParams.newInstance();			
		} catch (Exception e) {
			LOGGER.debug("Error New Instance Reflexion Class" + e.getMessage());
		}
		return null;
	}

	public static <T> T createNewInstance(Class<T> clazzName) {

		T newInstance = null;
		try {
			Constructor<T> builderWithoutParams = clazzName.getConstructor();
			newInstance = builderWithoutParams.newInstance();
		} catch (Exception e) {
			LOGGER.debug("Error New Instance Reflexion Class" + e.getMessage());
		}
		return newInstance;
	}

	public static <T> MapperFieldAnnotation fieldByAnnotation(Class<T> clazzName,
			Class<? extends Annotation> annotation) {
		T newInstance = createNewInstance(clazzName);
		if(newInstance==null)
		    return null;
	    return fieldByAnnotation(newInstance, annotation);
	}

	public static <T> Set<MapperFieldAnnotation> fieldsByAnnotation(T obj, Class<? extends Annotation> annotation) {
		Set<MapperFieldAnnotation> fields = new LinkedHashSet<>();
		final String className = obj.getClass().getName();
		LOGGER.debug("origin clazzName: " + obj.getClass());
		LOGGER.debug("origin object: " + obj);
		try {
			final Field[] fieldsC = Class.forName(className).getDeclaredFields();
			for (final Field field : fieldsC) {

				if (field.isAnnotationPresent(annotation)) {
					Object value = getAttribute(obj, field.getName());
					MapperFieldAnnotation fieldM = new MapperFieldAnnotation(field.getName(), field.getType(), value);
					fields.add(fieldM);
				}
				LOGGER.debug("fields: " + fields);
			}
		} catch (final Exception e) {
			LOGGER.debug("Error Reflexion Class" + e.getMessage());
		}
		return fields;
	}
}
