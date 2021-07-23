package com.barshop.app.models.mapper.util;

import org.apache.log4j.Logger;

import com.barshop.app.enums.WSEnvironmentEnums;
import com.barshop.app.models.entity.Entity;

public class EntityReflexionUtil {

    private static final Logger LOGGER = Logger.getLogger(EntityReflexionUtil.class.getName());

    public static final String PACKAGE_ENTITY = "com.barshop.app.models.entity.impl.";

    private static final String ENV_VALUE = "% : %";

    private static final char REG_PARAM = '%';

    private EntityReflexionUtil() {
        throw new IllegalStateException("EntityConstant class");
    }

    public static void environment() {
        LOGGER.debug(StringUtils.replaceValues(ENV_VALUE, REG_PARAM, WSEnvironmentEnums.CONNECTOR_DB, System.getenv(WSEnvironmentEnums.CONNECTOR_DB.getValue())));
        LOGGER.debug(StringUtils.replaceValues(ENV_VALUE, REG_PARAM, WSEnvironmentEnums.DB_DOMAIN, System.getenv(WSEnvironmentEnums.DB_DOMAIN.getValue())));
        LOGGER.debug(StringUtils.replaceValues(ENV_VALUE, REG_PARAM, WSEnvironmentEnums.DB_PORT, System.getenv(WSEnvironmentEnums.DB_PORT.getValue())));
        LOGGER.debug(StringUtils.replaceValues(ENV_VALUE, REG_PARAM, WSEnvironmentEnums.DB_INS_NAM, System.getenv(WSEnvironmentEnums.DB_INS_NAM.getValue())));
        LOGGER.debug(StringUtils.replaceValues(ENV_VALUE, REG_PARAM, WSEnvironmentEnums.DB_USER, System.getenv(WSEnvironmentEnums.DB_USER.getValue())));
        LOGGER.debug(StringUtils.replaceValues(ENV_VALUE, REG_PARAM, WSEnvironmentEnums.DB_DRIVER, System.getenv(WSEnvironmentEnums.DB_DRIVER.getValue())));
        LOGGER.debug(StringUtils.replaceValues(ENV_VALUE, REG_PARAM, WSEnvironmentEnums.DB_PLATAFORM, System.getenv(WSEnvironmentEnums.DB_PLATAFORM.getValue())));
        LOGGER.debug(StringUtils.replaceValues(ENV_VALUE, REG_PARAM, WSEnvironmentEnums.HB_LOG_LVL, System.getenv(WSEnvironmentEnums.HB_LOG_LVL.getValue())));
    }

    public static Entity newInstance( String entity ) {

        environment();
        String engine = System.getenv(WSEnvironmentEnums.ENGINE_DB.getValue());
        LOGGER.info("engine: " + engine);
        LOGGER.info("packageEntity: " + PACKAGE_ENTITY);

        if ("mysql".equalsIgnoreCase(engine)) {
            String entityImpl = PACKAGE_ENTITY + "." + entity + "MySQL";
            LOGGER.info("entityImpl: " + entityImpl);
            return (Entity) ReflexionUtil.createNewInstance(entityImpl);
        } else if ("oracle".equalsIgnoreCase(engine)) {
            String entityImpl = PACKAGE_ENTITY + "." + entity + "Oracle";
            LOGGER.info("entityImpl: " + entityImpl);
            return (Entity) ReflexionUtil.createNewInstance(entityImpl);
        }

        return null;
    }
}
