package com.barshop.app.models.mapper;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class MapperFieldAnnotationTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapperFieldAnnotationTests.class);

    @BeforeEach
    void init() {
        LOGGER.debug("testing init -> MapperFieldAnnotation");
    }

    @Test
    void createMapperFieldAnnotation() {
        LOGGER.debug("testing createMapperFieldAnnotation -> MapperFieldAnnotation");
        String id = "id";
        Long value = 20L;
        MapperFieldAnnotation mfa = new MapperFieldAnnotation(id, value.getClass(), value);
        LOGGER.debug("mfa -> {}",mfa);
        LOGGER.debug("validate construct");
        assertNotNull(mfa);
        LOGGER.debug("validate getter");
        assertEquals(id, mfa.getAttribute());
        assertEquals(value, mfa.getValue());
        assertEquals(value.getClass(), mfa.getType());
        String name = "NAME";
        String val = "John";
        LOGGER.debug("validate setter");
        mfa.setAttribute(name);
        mfa.setType(val.getClass());
        mfa.setValue(val);
        assertEquals(name, mfa.getAttribute());
        assertEquals(val, mfa.getValue());
        assertEquals(val.getClass(), mfa.getType());
    }
}
