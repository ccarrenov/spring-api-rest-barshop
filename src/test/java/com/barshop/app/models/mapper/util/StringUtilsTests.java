package com.barshop.app.models.mapper.util;

import static org.junit.Assert.assertNotNull;
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

class StringUtilsTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(StringUtilsTests.class);

    @BeforeEach
    void init() {
        LOGGER.debug("testing init -> StringUtils");
    }

    @Test
    void constructStringUtils() {
        LOGGER.debug("test constructStringUtils -> StringUtils");
        assertDoesNotThrow(() -> {
            Constructor<StringUtils> constructor = StringUtils.class.getDeclaredConstructor();
            boolean isConstructPrivate = Modifier.isPrivate(constructor.getModifiers());
            LOGGER.debug("validate constructor StringUtils private -> {}", isConstructPrivate);
            assertTrue(isConstructPrivate);
        });

        InvocationTargetException ex = assertThrows(InvocationTargetException.class, () -> {
            Constructor<StringUtils> ct = StringUtils.class.getDeclaredConstructor();
            ct.setAccessible(true);
            ct.newInstance();
        });
        boolean isThrowConstruct = (ex.getCause() instanceof InstantiationException);
        LOGGER.debug("validate constructor StringUtils throw InstantiationException -> {}", isThrowConstruct);
        assertTrue(isThrowConstruct);
        assertEquals("You can't create new instance of StringUtils.", ex.getCause().getMessage());
    }

    @Test
    void concat() {
        LOGGER.debug("test concat -> StringUtils.concat");
        String text = "Java Developer";
        String newText = StringUtils.concat("Java", " ", "Developer");
        assertNotNull(newText);
        assertEquals(text, newText);
    }

    @Test
    void concatSeparate() {
        LOGGER.debug("test concatSeparate -> StringUtils.concatSeparate");
        String text = "Java Developer";
        String newText = StringUtils.concatSeparate(" ", "Java", "Developer");
        assertNotNull(newText);
        assertEquals(text, newText);
        text = "Java\nDeveloper";
        newText = StringUtils.concatSeparate("\n", "Java", "Developer");
        assertNotNull(newText);
        assertEquals(text, newText);
        text = "Java Developer";
        newText = StringUtils.concatSeparate("", "Java", " ", null, "Developer");
        assertNotNull(newText);
        assertEquals(text, newText);
    }

    @Test
    void format() {
        LOGGER.debug("test format -> StringUtils.format");
        String text = "Java Developer";
        String textToFormat = "Java%%";
        String newText = StringUtils.format(textToFormat, " ", "Developer");
        LOGGER.debug("textToFormat -> {}", textToFormat);
        LOGGER.debug("newText {} ", newText);
        assertNotNull(newText);
        assertEquals(text, newText);
        text = "Java %";
        newText = StringUtils.format("Java%%", "Developer");
        LOGGER.debug("textToFormat -> {}", textToFormat);
        LOGGER.debug("newText -> {}", newText);
        assertNotNull(newText);
    }
}
