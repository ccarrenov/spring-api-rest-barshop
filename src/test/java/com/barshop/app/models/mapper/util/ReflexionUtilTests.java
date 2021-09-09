package com.barshop.app.models.mapper.util;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ReflexionUtilTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReflexionUtilTests.class);

    public class ReflexionT {

        public ReflexionT() throws InstantiationException {
            throw new InstantiationException("You can't create new instance of ReflexionT.");
        }
    }

    @BeforeEach
    void init() {
        LOGGER.debug("testing init -> ReflexionUtil");
    }

    @Test
    void createReflexionUtil() {
        LOGGER.debug("testing createReflexionUtil -> ReflexionUtil");
        assertDoesNotThrow(() -> {
            Constructor<ReflexionUtil> constructor = ReflexionUtil.class.getDeclaredConstructor();
            boolean isConstructPrivate = Modifier.isPrivate(constructor.getModifiers());
            LOGGER.debug("validate constructor ReflexionUtil private -> {}", isConstructPrivate);
            assertTrue(isConstructPrivate);
        });

        InvocationTargetException ex = assertThrows(InvocationTargetException.class, () -> {
            Constructor<ReflexionUtil> ct = ReflexionUtil.class.getDeclaredConstructor();
            ct.setAccessible(true);
            ct.newInstance();
        });
        boolean isThrowConstruct = (ex.getCause() instanceof InstantiationException);
        LOGGER.debug("validate constructor ReflexionUtil throw InstantiationException -> {}", isThrowConstruct);
        assertTrue(isThrowConstruct);
        assertEquals("You can't create new instance of ReflexionUtil.", ex.getCause().getMessage());
    }

    @Test
    void builderGetName() {
        LOGGER.debug("testing builderGetName -> ReflexionUtil.builderGetName");
        String getName = ReflexionUtil.builderGetName("id");
        LOGGER.debug("getName -> {}", getName);
        assertEquals("getId", getName);
    }

    @Test
    void builderSetName() {
        LOGGER.debug("testing builderSetName -> ReflexionUtil.builderSetName");
        String setName = ReflexionUtil.builderSetName("id");
        LOGGER.debug("setName -> {}", setName);
        assertEquals("setId", setName);
    }

    @Test
    void createNewInstance() {
        LOGGER.debug("testing createNewInstance -> ReflexionUtil.createNewInstance");
        String newString = ReflexionUtil.createNewInstance(String.class);
        LOGGER.debug("newString -> {}", newString);
        assertEquals("", newString);
    }

    @Test
    void createNewInstanceException() throws NoSuchMethodException, SecurityException {
        LOGGER.debug("testing createNewInstanceException -> ReflexionUtil.createNewInstance");
        ReflexionT newString = ReflexionUtil.createNewInstance(ReflexionT.class);
        LOGGER.debug("newString -> {}", newString);
        assertNull(newString);
    }

}
