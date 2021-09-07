package com.barshop.app.models.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.PersistenceException;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.barshop.app.enums.WSMessageEnums;
import com.barshop.app.models.dao.GenericDAO;
import com.barshop.app.models.dto.Country;
import com.barshop.app.models.entity.impl.oracle.CountryOracle;

@SpringBootTest
@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:/application.properties")
class GenericServiceTests {

    private static final Logger LOGGER = Logger.getLogger(GenericServiceTests.class.getName());

    @MockBean(classes = GenericDAO.class)
    private GenericDAO<Country, CountryOracle, Long> dao;

    @Autowired
    private GenericService<Country, CountryOracle, Long> genericService;

    @Test
    void count() {
        long size = 20L;
        LOGGER.debug("count");
        CountryOracle country = new CountryOracle();
        when(dao.count(any(CountryOracle.class))).thenReturn(size);
        ResponseEntity<Object> response = genericService.count(country, "oracle");
        LOGGER.debug(response);
        assertNotNull(response);
        assertEquals(size, response.getBody());
    }

    @Test
    void countNullPointerException() {
        LOGGER.debug("countNullPointerException");
        ResponseEntity<Object> respDefault = new ResponseEntity<>(WSMessageEnums.ERROR_NOT_COUNT.getValue(), HttpStatus.NOT_FOUND);
        CountryOracle country = new CountryOracle();
        when(dao.count(any(CountryOracle.class))).thenThrow(NullPointerException.class);
        ResponseEntity<Object> response = genericService.count(country, "oracle");
        LOGGER.debug(response);
        assertNotNull(response);
        assertEquals(respDefault.getStatusCode(), response.getStatusCode());
        assertEquals(respDefault.getBody(), response.getBody());
    }

    @Test
    void countException() {
        LOGGER.debug("countException");
        ResponseEntity<Object> respDefault = new ResponseEntity<>(WSMessageEnums.ERROR_NOT_COUNT.getValue(), HttpStatus.INTERNAL_SERVER_ERROR);
        CountryOracle country = new CountryOracle();
        when(dao.count(any(CountryOracle.class))).thenThrow(PersistenceException.class);
        ResponseEntity<Object> response = genericService.count(country, "oracle");
        LOGGER.debug(response);
        assertNotNull(response);
        assertEquals(respDefault.getStatusCode(), response.getStatusCode());
        assertEquals(respDefault.getBody(), response.getBody());
    }

    @SuppressWarnings("unchecked")
    @Test()
    void findAll() {
        Country dto = new Country();
        dto.setCountryCode(new Short("56"));
        dto.setName("Chile");
        dto.setTwoDigitIso("CL");
        dto.setThreeDigitIso("CHL");
        dto.setCountryCallingCode("+56");
        List<Country> dtos = new ArrayList<>();
        dtos.add(dto);
        LOGGER.debug("findAll");
        CountryOracle country = new CountryOracle();
        when(dao.findAll(Country.class, country)).thenReturn(dtos);
        ResponseEntity<Object> response = genericService.findAll(Country.class, country, "oracle", "Error DB");
        LOGGER.debug(response);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertDoesNotThrow(() -> {
            List<Country> list = (List<Country>) response.getBody();
            LOGGER.debug(list.size());
        });
        List<Country> resp = (List<Country>) response.getBody();
        assertEquals(1, resp.size());
    }

    @Test()
    void findAllException() {
        LOGGER.debug("findAllException");
        String error = "code 1";
        ResponseEntity<Object> respDefault = new ResponseEntity<>(WSMessageEnums.ERROR_FIND.getValue().replaceAll("\\$1", error), HttpStatus.INTERNAL_SERVER_ERROR);
        CountryOracle country = new CountryOracle();
        when(dao.findAll(Country.class, country)).thenThrow(PersistenceException.class);
        ResponseEntity<Object> response = genericService.findAll(Country.class, country, "oracle", error);
        LOGGER.debug(response);
        assertNotNull(response);
        assertEquals(respDefault.getStatusCode(), response.getStatusCode());
    }

    @Test()
    void createOrUpdate() {
        LOGGER.debug("createOrUpdate");
        Country dto = new Country();
        dto.setCountryCode(new Short("56"));
        dto.setName("Chile");
        dto.setTwoDigitIso("CL");
        dto.setThreeDigitIso("CHL");
        dto.setCountryCallingCode("+56");
        Country newDTO = new Country();
        newDTO.setCountryCode(new Short("56"));
        newDTO.setId(1L);
        newDTO.setName("Chile");
        newDTO.setTwoDigitIso("CL");
        newDTO.setThreeDigitIso("CHL");
        newDTO.setCountryCallingCode("+56");
        CountryOracle country = new CountryOracle();
        when(dao.createOrUpdate(Country.class, country, Long.class, dto)).thenReturn(newDTO);
        ResponseEntity<Object> respDefault = new ResponseEntity<>(newDTO, HttpStatus.OK);
        ResponseEntity<Object> response = genericService.createOrUpdate(Country.class, country, Long.class, "oracle", dto, "");
        LOGGER.debug(response);
        assertNotNull(response);
        assertEquals(respDefault.getStatusCode(), response.getStatusCode());
        assertDoesNotThrow(() -> {
            Country cast = (Country) respDefault.getBody();
            LOGGER.debug(cast);
        });

        Country resDTO = (Country) respDefault.getBody();
        assertNotNull(resDTO);
        assertEquals(1L, resDTO.getId());
        assertEquals(dto.getName(), resDTO.getName());
        assertEquals(dto.getCountryCode(), resDTO.getCountryCode());
        assertEquals(dto.getTwoDigitIso(), resDTO.getTwoDigitIso());
        assertEquals(dto.getThreeDigitIso(), resDTO.getThreeDigitIso());
        assertEquals(dto.getCountryCallingCode(), resDTO.getCountryCallingCode());
    }

    @Test()
    void createOrUpdateDuplicateKeyException() {
        LOGGER.debug("createOrUpdateDuplicateKeyException");
        Country dto = new Country();
        CountryOracle country = new CountryOracle();
        when(dao.createOrUpdate(Country.class, country, Long.class, dto)).thenThrow(DuplicateKeyException.class);
        ResponseEntity<Object> respDefault = new ResponseEntity<>(WSMessageEnums.ERROR_DUPLICATE_REGISTER.getValue(), HttpStatus.INTERNAL_SERVER_ERROR);
        ResponseEntity<Object> response = genericService.createOrUpdate(Country.class, country, Long.class, "oracle", dto, "");
        LOGGER.debug(response);
        assertNotNull(response);
        assertEquals(respDefault.getStatusCode(), response.getStatusCode());
    }

    @Test()
    void createOrUpdateDataIntegrityViolationException() {
        LOGGER.debug("createOrUpdateDataIntegrityViolationException");
        Country dto = new Country();
        CountryOracle country = new CountryOracle();
        when(dao.createOrUpdate(Country.class, country, Long.class, dto)).thenThrow(DataIntegrityViolationException.class);
        ResponseEntity<Object> respDefault = new ResponseEntity<>(WSMessageEnums.ERROR_INTEGRITY_REGISTER.getValue(), HttpStatus.INTERNAL_SERVER_ERROR);
        ResponseEntity<Object> response = genericService.createOrUpdate(Country.class, country, Long.class, "oracle", dto, "");
        LOGGER.debug(response);
        assertNotNull(response);
        assertEquals(respDefault.getStatusCode(), response.getStatusCode());
    }

    @Test()
    void createOrUpdateException() {
        LOGGER.debug("createOrUpdateException");
        String error = "Error country 1";
        Country dto = new Country();
        CountryOracle country = new CountryOracle();
        when(dao.createOrUpdate(Country.class, country, Long.class, dto)).thenThrow(PersistenceException.class);
        ResponseEntity<Object> respDefault = new ResponseEntity<>(WSMessageEnums.ERROR_SAVE_OR_UPDATE.getValue().replaceAll("\\$1", error), HttpStatus.INTERNAL_SERVER_ERROR);
        ResponseEntity<Object> response = genericService.createOrUpdate(Country.class, country, Long.class, "oracle", dto, error);
        LOGGER.debug(response);
        assertNotNull(response);
        assertEquals(respDefault.getStatusCode(), response.getStatusCode());
    }
    
    @Test()
    void findById() {
        LOGGER.debug("findById");
        Long id = 1L;
        Country dto = new Country();
        dto.setId(id);
        dto.setCountryCode(new Short("56"));
        dto.setName("Chile");
        dto.setTwoDigitIso("CL");
        dto.setThreeDigitIso("CHL");
        dto.setCountryCallingCode("+56");        
        Country newDTO = new Country();
        newDTO.setId(id);
        newDTO.setCountryCode(new Short("56"));
        newDTO.setName("Chile");
        newDTO.setTwoDigitIso("CL");
        newDTO.setThreeDigitIso("CHL");
        newDTO.setCountryCallingCode("+56");
        CountryOracle country = new CountryOracle();
        when(dao.findById(Country.class, new CountryOracle(), id)).thenReturn(newDTO);
        ResponseEntity<Object> respDefault = new ResponseEntity<>(dto, HttpStatus.OK);
        ResponseEntity<Object> response = genericService.findById(Country.class, country, "oracle", id);
        LOGGER.debug(response);
        assertNotNull(response);
        assertEquals(respDefault.getStatusCode(), response.getStatusCode());
        assertDoesNotThrow(() -> {
            Country cast = (Country) respDefault.getBody();
            LOGGER.debug(cast);
        });

        Country resDTO = (Country) respDefault.getBody();
        assertNotNull(resDTO);
        assertEquals(dto.getId(), resDTO.getId());
        assertEquals(dto.getName(), resDTO.getName());
        assertEquals(dto.getCountryCode(), resDTO.getCountryCode());
        assertEquals(dto.getTwoDigitIso(), resDTO.getTwoDigitIso());
        assertEquals(dto.getThreeDigitIso(), resDTO.getThreeDigitIso());
        assertEquals(dto.getCountryCallingCode(), resDTO.getCountryCallingCode());
    }    
}
