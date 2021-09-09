package com.barshop.app.exception;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:/application.properties")
class NumberPageExceptionTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(NumberPageExceptionTests.class);

    @BeforeEach
    void init() {
        LOGGER.debug("testing init -> NumberPageException");
    }

    @Test
    void createException() throws NumberPageException {
        LOGGER.debug("testing createException -> create NumberPageException");
        String message = "TEST NUMBER PAGE EXCEPTION";
        NumberPageException numEx = new NumberPageException(message);
        LOGGER.debug(numEx.getMessage());
        assertNotNull(numEx);
        assertEquals(numEx.getMessage(), message);
    }

    @Test
    void createExceptionEncapsuled() throws NumberPageException {
        LOGGER.debug("testing createExceptionEncapsuled -> create NumberPageException");
        String message = "TEST NUMBER PAGE EXCEPTION";
        Exception ex = new Exception("TEST");
        NumberPageException numEx = new NumberPageException(message, ex);
        LOGGER.debug(numEx.getMessage());
        assertNotNull(numEx);
        assertEquals(numEx.getMessage(), message);
        assertEquals(numEx.getCause(), ex);
    }
}
