package com.barshop.app.models.mapper.util;

import org.apache.log4j.Logger;

import com.barshop.app.models.entity.Entity;

public class EntityReflexionUtil {

	private static final Logger LOGGER = Logger.getLogger(EntityReflexionUtil.class.getName());

	private static final String PACKAGE_ENTITY = "com.barshop.app.models.entity.impl";
	
    private EntityReflexionUtil() {
        throw new IllegalStateException("EntityConstant class");
    }	

	public static Entity newInstance(String entity) {

		String engine = System.getenv("ENGINE_DB");
		LOGGER.debug("engine: " + engine);
		LOGGER.debug("packageEntity: " + PACKAGE_ENTITY);

		if ("mysql".equalsIgnoreCase(engine)) {
			String entityImpl = PACKAGE_ENTITY + "." + entity + "MySQL";
			return (Entity) ReflexionUtil.createNewInstance(entityImpl);
		} else if ("oracle".equalsIgnoreCase(engine)) {
			String entityImpl = PACKAGE_ENTITY + "." + entity + "Oracle";
			return (Entity) ReflexionUtil.createNewInstance(entityImpl);
		}

		return null;
	}
}
