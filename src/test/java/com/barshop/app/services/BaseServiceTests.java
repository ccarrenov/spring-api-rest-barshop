package com.barshop.app.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import com.barshop.app.enums.WSMessageEnums;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:/application.properties")
class BaseServiceTests {

    private static final Logger LOGGER = Logger.getLogger(BaseServiceTests.class.getName()); 

    @Autowired
    private BaseService baseService;

    @BeforeEach
    void init() {
        LOGGER.debug("testing init -> BaseService");
        baseService = new BaseService();
        
    }

    @Test
    void responseEmpty() {
        LOGGER.debug("test responseEmpty -> BaseService.responseEmpty");
        ResponseEntity<Object> respDefault = new ResponseEntity<>(WSMessageEnums.ERROR_NOT_FOUND.getValue(), HttpStatus.NOT_FOUND);
        ResponseEntity<Object> resp = baseService.responseEmpty(null, null);        
        LOGGER.debug("respDefault -> " + respDefault);
        LOGGER.debug("resp -> " + resp);
        assertNotNull(resp);
        assertEquals(respDefault.getStatusCode(), resp.getStatusCode());
        assertEquals(respDefault.getBody(), resp.getBody());
    }

    @Test
    void responseNotEmpty() {
        LOGGER.debug("test responseNotEmpty -> BaseService.responseNotEmpty");
        ResponseEntity<Long> respDefault = new ResponseEntity<>(1L, HttpStatus.OK);
        ResponseEntity<Object> resp = baseService.responseEmpty(1L, HttpStatus.OK);
        LOGGER.debug("respDefault -> " + respDefault);
        LOGGER.debug("resp -> " + resp);
        assertNotNull(resp);
        assertEquals(respDefault.getStatusCode(), resp.getStatusCode());
        assertEquals(respDefault.getBody(), resp.getBody());
    }

    @Test
    void response() {
        LOGGER.debug("test response -> BaseService.response");
        ResponseEntity<Long> respDefault = new ResponseEntity<>(1L, HttpStatus.OK);
        ResponseEntity<Object> resp = baseService.response(1L, HttpStatus.OK);
        LOGGER.debug("respDefault -> " + respDefault);
        LOGGER.debug("resp -> " + resp);
        assertNotNull(resp);
        assertEquals(respDefault.getStatusCode(), resp.getStatusCode());
        assertEquals(respDefault.getBody(), resp.getBody());
    }

    @Test
    void msg() {
        LOGGER.debug("test msg -> BaseService.msg");
        ResponseEntity<Object> respDefault = new ResponseEntity<>("Test", HttpStatus.OK);
        ResponseEntity<Object> resp = baseService.msg("Test", HttpStatus.OK);
        LOGGER.debug("respDefault -> " + respDefault);
        LOGGER.debug("resp -> " + resp);
        assertNotNull(resp);
        assertEquals(respDefault.getStatusCode(), resp.getStatusCode());
        assertEquals(respDefault.getBody(), resp.getBody());
    }

    @Test
    void msgMap() {
        LOGGER.debug("test msgMap -> BaseService.msg");
        String key = "name";
        String value = "John Eagly";
        ResponseEntity<Object> resp = baseService.msg(key, value, HttpStatus.OK);
        LOGGER.debug("resp -> " + resp);
        assertNotNull(resp);
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertTrue((resp.getBody() instanceof HashMap));
    }
}
