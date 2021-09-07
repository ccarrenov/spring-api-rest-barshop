package com.barshop.app.exception;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:/application.properties")
class NumberPageExceptionTests {

    private static final Logger LOGGER = Logger.getLogger(NumberPageExceptionTests.class.getName());

    @Test
    void createException() throws NumberPageException {
        String message = "TEST NUMBER PAGE EXCEPTION";
        NumberPageException numEx = new NumberPageException(message);
        LOGGER.debug(numEx);
        assertNotNull(numEx);
        assertEquals(numEx.getMessage(), message);
    }

    @Test
    void createExceptionEncapsuled() throws NumberPageException {
        String message = "TEST NUMBER PAGE EXCEPTION";
        Exception ex = new Exception("TEST");
        NumberPageException numEx = new NumberPageException(message, ex);
        LOGGER.debug(numEx);
        assertNotNull(numEx);
        assertEquals(numEx.getMessage(), message);
        assertEquals(numEx.getCause(), ex);
    }
}
