package com.barshop.app.models.mapper.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barshop.app.enums.WSEnvironmentEnums;
import com.barshop.app.models.entity.Entity;

public class EntityReflexionUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntityReflexionUtil.class);

    public static final String PACKAGE_ENTITY = "com.barshop.app.models.entity.impl.";

    private static final String ENV_VALUE = "% : %";

    private EntityReflexionUtil() {
        throw new IllegalStateException("EntityConstant class");
    }

    public static void environment() {
        LOGGER.debug(ENV_VALUE, WSEnvironmentEnums.CONNECTOR_DB_MSG.getValue(), System.getenv(WSEnvironmentEnums.CONNECTOR_DB.getValue()));
        LOGGER.debug(ENV_VALUE, WSEnvironmentEnums.DB_DOMAIN_MSG.getValue(), System.getenv(WSEnvironmentEnums.DB_DOMAIN.getValue()));
        LOGGER.debug(ENV_VALUE, WSEnvironmentEnums.DB_PORT_MSG.getValue(), System.getenv(WSEnvironmentEnums.DB_PORT.getValue()));
        LOGGER.debug(ENV_VALUE, WSEnvironmentEnums.DB_INS_NAM_MSG.getValue(), System.getenv(WSEnvironmentEnums.DB_INS_NAM.getValue()));
        LOGGER.debug(ENV_VALUE, WSEnvironmentEnums.DB_USER_MSG.getValue(), System.getenv(WSEnvironmentEnums.DB_USER.getValue()));
        LOGGER.debug(ENV_VALUE, WSEnvironmentEnums.DB_DRIVER_MSG.getValue(), System.getenv(WSEnvironmentEnums.DB_DRIVER.getValue()));
        LOGGER.debug(ENV_VALUE, WSEnvironmentEnums.DB_PLATAFORM_MSG.getValue(), System.getenv(WSEnvironmentEnums.DB_PLATAFORM.getValue()));
        LOGGER.debug(ENV_VALUE, WSEnvironmentEnums.HB_LOG_LVL_MSG.getValue(), System.getenv(WSEnvironmentEnums.HB_LOG_LVL.getValue()));
    }

    public static Entity newInstance( String entity ) {

        environment();
        String engine = System.getenv(WSEnvironmentEnums.ENGINE_DB.getValue());
        LOGGER.info("engine: {}", engine);
        LOGGER.info("packageEntity: " + PACKAGE_ENTITY);

        if ("mysql".equalsIgnoreCase(engine)) {
            String entityImpl = PACKAGE_ENTITY + System.getenv(WSEnvironmentEnums.ENGINE_DB.getValue()) + "." + entity + "MySQL";
            LOGGER.info("entityImpl: {}", entityImpl);
            return (Entity) ReflexionUtil.createNewInstance(entityImpl);
        } else if ("oracle".equalsIgnoreCase(engine)) {
            String entityImpl = PACKAGE_ENTITY + System.getenv(WSEnvironmentEnums.ENGINE_DB.getValue()) + "." + entity + "Oracle";
            LOGGER.info("entityImpl: {}", entityImpl);
            return (Entity) ReflexionUtil.createNewInstance(entityImpl);
        }

        return null;
    }
}
