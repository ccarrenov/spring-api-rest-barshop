package com.barshop.app.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WSEnvironmentEnums {

    ENGINE_DB("ENGINE_DB"),
    CONNECTOR_DB("CONNECTOR_DB"),
    DB_DOMAIN("DB_DOMAIN"),
    DB_PORT("DB_PORT"),
    DB_INS_NAM("DB_INS_NAM"),
    DB_USER("DB_USER"),
    DB_DRIVER("DB_DRIVER"),
    DB_PLATAFORM("DB_PLATAFORM"),
    HB_LOG_LVL("HB_LOG_LVL"),
    ENGINE_DB_MSG("ENGINE_DB ->{}"),
    CONNECTOR_DB_MSG("CONNECTOR_DB ->{}"),
    DB_DOMAIN_MSG("DB_DOMAIN ->{}"),
    DB_PORT_MSG("DB_PORT ->{}"),
    DB_INS_NAM_MSG("DB_INS_NAM ->{}"),
    DB_USER_MSG("DB_USER ->{}"),
    DB_DRIVER_MSG("DB_DRIVER ->{}"),
    DB_PLATAFORM_MSG("DB_PLATAFORM ->{}"),
    HB_LOG_LVL_MSG("HB_LOG_LVL ->{}");    

    private String value;
}
