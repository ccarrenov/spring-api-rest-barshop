package com.barshop.app.models.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import com.barshop.app.enums.WSMessageEnums;

@SpringBootTest
@TestPropertySource(locations = "classpath:/application.properties")
class BaseServiceTests {

    private static final Logger LOGGER = Logger.getLogger(BaseServiceTests.class.getName());

    @Autowired
    private BaseService baseService;

    @Test
    void responseEmpty() {

        ResponseEntity<Object> respDefault = new ResponseEntity<>(WSMessageEnums.ERROR_NOT_FOUND.getValue(), HttpStatus.NOT_FOUND);
        ResponseEntity<Object> resp = baseService.responseEmpty(null, null);
        LOGGER.debug(respDefault);
        LOGGER.debug(resp);
        assertNotNull(resp);
        assertEquals(respDefault.getStatusCode(), resp.getStatusCode());
        assertEquals(respDefault.getBody(), resp.getBody());
    }

    @Test
    void responseNotEmpty() {
        ResponseEntity<Long> respDefault = new ResponseEntity<>(1L, HttpStatus.OK);
        ResponseEntity<Object> resp = baseService.responseEmpty(1L, HttpStatus.OK);
        LOGGER.debug(respDefault);
        LOGGER.debug(resp);
        assertNotNull(resp);
        assertEquals(respDefault.getStatusCode(), resp.getStatusCode());
        assertEquals(respDefault.getBody(), resp.getBody());
    }

    @Test
    void response() {
        ResponseEntity<Long> respDefault = new ResponseEntity<>(1L, HttpStatus.OK);
        ResponseEntity<Object> resp = baseService.response(1L, HttpStatus.OK);
        LOGGER.debug(respDefault);
        LOGGER.debug(resp);
        assertNotNull(resp);
        assertEquals(respDefault.getStatusCode(), resp.getStatusCode());
        assertEquals(respDefault.getBody(), resp.getBody());
    }

    @Test
    void msg() {
        ResponseEntity<Object> respDefault = new ResponseEntity<>("Test", HttpStatus.OK);
        ResponseEntity<Object> resp = baseService.msg("Test", HttpStatus.OK);
        LOGGER.debug(respDefault);
        LOGGER.debug(resp);
        assertNotNull(resp);
        assertEquals(respDefault.getStatusCode(), resp.getStatusCode());
        assertEquals(respDefault.getBody(), resp.getBody());
    }
    
    @Test
    void msgMap() {        
        String key = "name";
        String value = "John Eagly"; 
        ResponseEntity<Object> resp = baseService.msg(key, value, HttpStatus.OK);
        LOGGER.debug(resp);
        assertNotNull(resp);
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertTrue((resp.getBody() instanceof HashMap));
    }    
}
