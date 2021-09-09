package com.barshop.app.models.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.LongStream;

import javax.persistence.PersistenceException;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
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

    private Long id() {
        Random random = new Random();
        LongStream longStream = random.longs(1L, 1L, 8000L);
        Iterator<Long> iterator = longStream.iterator();
        return iterator.next();
    }

    private Country dtoForCreate() {
        Country dto = new Country();
        dto.setCountryCode(new Short("56"));
        dto.setName("Chile" + id());
        dto.setTwoDigitIso("CL" + id());
        dto.setThreeDigitIso("CHL" + id());
        dto.setCountryCallingCode("+56" + id());
        return dto;
    }

    private Country clone( Country dto ) {
        Country newDTO = new Country();
        newDTO.setId(dto.getId());
        newDTO.setCountryCode(dto.getCountryCode());
        newDTO.setName(dto.getName());
        newDTO.setTwoDigitIso(dto.getTwoDigitIso());
        newDTO.setThreeDigitIso(dto.getThreeDigitIso());
        newDTO.setCountryCallingCode(dto.getCountryCallingCode());
        return newDTO;
    }

    private CountryOracle DTOToEntity( Country dto ) {
        CountryOracle newEntity = new CountryOracle();
        newEntity.setId(dto.getId());
        newEntity.setCountryCode(dto.getCountryCode());
        newEntity.setName(dto.getName());
        newEntity.setTwoDigitIso(dto.getTwoDigitIso());
        newEntity.setThreeDigitIso(dto.getThreeDigitIso());
        newEntity.setCountryCallingCode(dto.getCountryCallingCode());
        return newEntity;
    }

    private Country dto() {
        Country dto = dtoForCreate();
        dto.setId(id());
        return dto;
    }

    private List<Country> dtos() {
        LOGGER.debug("create list CountryOracle");
        List<Country> countries = new ArrayList<>();
        countries.add(dto());
        LOGGER.debug("countries -> " + countries);
        return countries;
    }

    @Test
    void count() {
        LOGGER.debug("test count -> GenericService.count");
        long size = 20L;
        LOGGER.debug("count");
        when(dao.count(any(CountryOracle.class))).thenReturn(size);
        ResponseEntity<Object> response = genericService.count(new CountryOracle(), "oracle");
        LOGGER.debug("response -> " + response);
        assertNotNull(response);
        assertEquals(size, response.getBody());
    }

    @Test
    void countNullPointerException() {
        LOGGER.debug("test countNullPointerException -> GenericService.count");
        LOGGER.debug("countNullPointerException");
        ResponseEntity<Object> respDefault = new ResponseEntity<>(WSMessageEnums.ERROR_NOT_COUNT.getValue(), HttpStatus.NOT_FOUND);
        when(dao.count(any(CountryOracle.class))).thenThrow(NullPointerException.class);
        ResponseEntity<Object> response = genericService.count(new CountryOracle(), "oracle");
        LOGGER.debug("response -> " + response);
        assertNotNull(response);
        assertEquals(respDefault.getStatusCode(), response.getStatusCode());
        assertEquals(respDefault.getBody(), response.getBody());
    }

    @Test
    void countException() {
        LOGGER.debug("test countException -> GenericService.count");
        ResponseEntity<Object> respDefault = new ResponseEntity<>(WSMessageEnums.ERROR_NOT_COUNT.getValue(), HttpStatus.INTERNAL_SERVER_ERROR);
        when(dao.count(any(CountryOracle.class))).thenThrow(PersistenceException.class);
        ResponseEntity<Object> response = genericService.count(new CountryOracle(), "oracle");
        LOGGER.debug("response -> " + response);
        assertNotNull(response);
        assertEquals(respDefault.getStatusCode(), response.getStatusCode());
        assertEquals(respDefault.getBody(), response.getBody());
    }

    @SuppressWarnings("unchecked")
    @Test()
    void findAll() {
        LOGGER.debug("test findAll -> GenericService.findAll");
        List<Country> dtos = dtos();
        LOGGER.debug("findAll");
        when(dao.findAll(any(), any(CountryOracle.class))).thenReturn(dtos);
        ResponseEntity<Object> response = genericService.findAll(Country.class, new CountryOracle(), "oracle", "Error DB");
        LOGGER.debug("response -> " + response);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertDoesNotThrow(() -> {
            List<Country> list = (List<Country>) response.getBody();
            LOGGER.debug("list.size() -> " + list.size());
        });
        List<Country> resp = (List<Country>) response.getBody();
        assertEquals(1, resp.size());
    }

    @Test()
    void findAllException() {
        LOGGER.debug("test findAllException -> GenericService.findAll");
        String error = "code 1";
        ResponseEntity<Object> respDefault = new ResponseEntity<>(WSMessageEnums.ERROR_FIND.getValue().replaceAll("\\$1", error), HttpStatus.INTERNAL_SERVER_ERROR);
        when(dao.findAll(any(), any(CountryOracle.class))).thenThrow(PersistenceException.class);
        ResponseEntity<Object> response = genericService.findAll(Country.class, new CountryOracle(), "oracle", error);
        LOGGER.debug("respDefault -> " + respDefault);
        LOGGER.debug("response -> " + response);
        assertNotNull(response);
        assertEquals(respDefault.getStatusCode(), response.getStatusCode());
    }

    @Test()
    void createOrUpdate() {
        LOGGER.debug("test createOrUpdate -> GenericService.createOrUpdate");
        Country dto = dtoForCreate();
        Country newDTO = clone(dto);
        newDTO.setId(id());
        when(dao.createOrUpdate(any(), any(CountryOracle.class), any(), any(Country.class))).thenReturn(newDTO);
        ResponseEntity<Object> respDefault = new ResponseEntity<>(newDTO, HttpStatus.OK);
        ResponseEntity<Object> response = genericService.createOrUpdate(Country.class, new CountryOracle(), Long.class, "oracle", dto, "");
        LOGGER.debug("response -> " + response);
        assertNotNull(response);
        assertEquals(respDefault.getStatusCode(), response.getStatusCode());
        assertDoesNotThrow(() -> {
            Country cast = (Country) respDefault.getBody();
            LOGGER.debug("cast -> " + cast);
        });
        Country resDTO = (Country) respDefault.getBody();
        assertNotNull(resDTO);
        assertNotEquals(0L, resDTO.getId());
    }

    @Test()
    void createOrUpdateDuplicateKeyException() {
        LOGGER.debug("test createOrUpdateDuplicateKeyException -> GenericService.createOrUpdate");
        when(dao.createOrUpdate(any(), any(CountryOracle.class), any(), any(Country.class))).thenThrow(DuplicateKeyException.class);
        ResponseEntity<Object> respDefault = new ResponseEntity<>(WSMessageEnums.ERROR_DUPLICATE_REGISTER.getValue(), HttpStatus.INTERNAL_SERVER_ERROR);
        ResponseEntity<Object> response = genericService.createOrUpdate(Country.class, new CountryOracle(), Long.class, "oracle", new Country(), "");
        LOGGER.debug("respDefault -> " + respDefault);
        LOGGER.debug("response -> " + response);
        assertNotNull(response);
        assertEquals(respDefault.getStatusCode(), response.getStatusCode());
    }

    @Test()
    void createOrUpdateDataIntegrityViolationException() {
        LOGGER.debug("test createOrUpdateDataIntegrityViolationException -> GenericService.createOrUpdate");
        when(dao.createOrUpdate(any(), any(CountryOracle.class), any(), any(Country.class))).thenThrow(DataIntegrityViolationException.class);
        ResponseEntity<Object> respDefault = new ResponseEntity<>(WSMessageEnums.ERROR_INTEGRITY_REGISTER.getValue(), HttpStatus.INTERNAL_SERVER_ERROR);
        ResponseEntity<Object> response = genericService.createOrUpdate(Country.class, new CountryOracle(), Long.class, "oracle", new Country(), "");
        LOGGER.debug("respDefault -> " + respDefault);
        LOGGER.debug("response -> " + response);
        assertNotNull(response);
        assertEquals(respDefault.getStatusCode(), response.getStatusCode());
    }

    @Test()
    void createOrUpdateException() {
        LOGGER.debug("test createOrUpdateException -> GenericService.createOrUpdate");
        String error = "Error country 1";
        Country dto = new Country();
        when(dao.createOrUpdate(any(), any(CountryOracle.class), any(), any(Country.class))).thenThrow(PersistenceException.class);
        ResponseEntity<Object> respDefault = new ResponseEntity<>(WSMessageEnums.ERROR_SAVE_OR_UPDATE.getValue().replaceAll("\\$1", error), HttpStatus.INTERNAL_SERVER_ERROR);
        ResponseEntity<Object> response = genericService.createOrUpdate(Country.class, new CountryOracle(), Long.class, "oracle", dto, error);
        LOGGER.debug("respDefault -> " + respDefault);
        LOGGER.debug("response -> " + response);
        assertNotNull(response);
        assertEquals(respDefault.getStatusCode(), response.getStatusCode());
    }

    @Test()
    void deleteById() {
        LOGGER.debug("test deleteById -> GenericService.deleteById");
        doThrow(new PersistenceException("Exception occured")).when(dao).deleteById(any(CountryOracle.class), any());
        Mockito.doAnswer(i -> {
            return null;
        }).when(dao).deleteById(any(CountryOracle.class), any());
        ResponseEntity<Object> respDefault = new ResponseEntity<>(WSMessageEnums.SUCCESS_DELETE.getValue(), HttpStatus.OK);
        ResponseEntity<Object> response = genericService.deleteById(new CountryOracle(), "oracle", new Long(1));
        LOGGER.debug("respDefault -> " + respDefault);
        LOGGER.debug("response -> " + response);
        assertNotNull(response);
        assertEquals(respDefault.getStatusCode(), response.getStatusCode());
    }

    @Test()
    void deleteByIdEmptyResultDataAccessException() {
        LOGGER.debug("test deleteByIdEmptyResultDataAccessException -> GenericService.deleteById");
        doThrow(new PersistenceException("Exception occured")).when(dao).deleteById(any(CountryOracle.class), any());
        Mockito.doThrow(EmptyResultDataAccessException.class).when(dao).deleteById(any(CountryOracle.class), any());
        ResponseEntity<Object> respDefault = new ResponseEntity<>(WSMessageEnums.ERROR_NOT_FOUND.getValue(), HttpStatus.NOT_FOUND);
        ResponseEntity<Object> response = genericService.deleteById(new CountryOracle(), "oracle", new Long(1));
        LOGGER.debug("respDefault -> " + respDefault);
        LOGGER.debug("response -> " + response);
        assertNotNull(response);
        assertEquals(respDefault.getStatusCode(), response.getStatusCode());
    }

    @Test()
    void deleteByIdException() {
        LOGGER.debug("test deleteByIdException -> GenericService.deleteById");
        doThrow(new PersistenceException("Exception occured")).when(dao).deleteById(any(CountryOracle.class), any());
        Mockito.doThrow(PersistenceException.class).when(dao).deleteById(any(CountryOracle.class), any());
        ResponseEntity<Object> respDefault = new ResponseEntity<>(WSMessageEnums.ERROR_DELETE.getValue(), HttpStatus.INTERNAL_SERVER_ERROR);
        ResponseEntity<Object> response = genericService.deleteById(new CountryOracle(), "oracle", new Long(1));
        LOGGER.debug("respDefault -> " + respDefault);
        LOGGER.debug("response -> " + response);
        assertNotNull(response);
        assertEquals(respDefault.getStatusCode(), response.getStatusCode());
    }

    @Test()
    void findById() {
        LOGGER.debug("test createOrUpdateException -> GenericService.findById");
        Country country = dto();
        LOGGER.debug("country -> " + country);
        when(dao.findById(any(), any(CountryOracle.class), any())).thenReturn(country);
        ResponseEntity<Object> respDefault = new ResponseEntity<>(country, HttpStatus.OK);
        ResponseEntity<Object> response = genericService.findById(Country.class, new CountryOracle(), "oracle", country.getId());
        LOGGER.debug("respDefault -> " + respDefault);
        LOGGER.debug("response -> " + response);
        assertNotNull(response);
        assertEquals(respDefault.getStatusCode(), response.getStatusCode());
        Country bodyDefault = (Country) respDefault.getBody();
        Country body = (Country) response.getBody();
        assertEquals(bodyDefault.getId(), body.getId());
    }

    @Test()
    void findByIdNull() {
        LOGGER.debug("test findByIdNull -> GenericService.findById");
        Country country = dto();
        LOGGER.debug("country -> " + country);
        when(dao.findById(any(), any(CountryOracle.class), any())).thenReturn(null);
        ResponseEntity<Object> respDefault = new ResponseEntity<>(WSMessageEnums.ERROR_NOT_FOUND.getValue(), HttpStatus.NOT_FOUND);
        ResponseEntity<Object> response = genericService.findById(Country.class, new CountryOracle(), "oracle", country.getId());
        LOGGER.debug("respDefault -> " + respDefault);
        LOGGER.debug("response -> " + response);
        assertNotNull(response);
        assertEquals(respDefault.getStatusCode(), response.getStatusCode());
    }

    @Test()
    void findByIdNullPointerException() {
        LOGGER.debug("test findByIdNullPointerException -> GenericService.findById");
        Country country = dto();
        LOGGER.debug("country -> " + country);
        when(dao.findById(any(), any(CountryOracle.class), any())).thenThrow(NullPointerException.class);
        ResponseEntity<Object> respDefault = new ResponseEntity<>(WSMessageEnums.ERROR_NOT_FOUND.getValue().replaceAll("\\$1", "" + country.getId()), HttpStatus.NOT_FOUND);
        ResponseEntity<Object> response = genericService.findById(Country.class, new CountryOracle(), "oracle", country.getId());
        LOGGER.debug("respDefault -> " + respDefault);
        LOGGER.debug("response -> " + response);
        assertNotNull(response);
        assertEquals(respDefault.getStatusCode(), response.getStatusCode());
    }

    @Test()
    void findByIdException() {
        LOGGER.debug("test findByIdException -> GenericService.findById");
        Country country = dto();
        LOGGER.debug("country -> " + country);
        when(dao.findById(any(), any(CountryOracle.class), any())).thenThrow(PersistenceException.class);
        ResponseEntity<Object> respDefault = new ResponseEntity<>(WSMessageEnums.ERROR_NOT_FOUND.getValue().replaceAll("\\$1", "" + country.getId()), HttpStatus.INTERNAL_SERVER_ERROR);
        ResponseEntity<Object> response = genericService.findById(Country.class, new CountryOracle(), "oracle", country.getId());
        LOGGER.debug("respDefault -> " + respDefault);
        LOGGER.debug("response -> " + response);
        assertNotNull(response);
        assertEquals(respDefault.getStatusCode(), response.getStatusCode());
    }
}
