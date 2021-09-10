package com.barshop.app.models.services;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.LongStream;

import javax.persistence.PersistenceException;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import com.barshop.app.enums.WSMessageEnums;
import com.barshop.app.exception.NumberPageException;
import com.barshop.app.models.dao.GenericDAO;
import com.barshop.app.models.dao.GenericPage;
import com.barshop.app.models.dto.Country;
import com.barshop.app.models.entity.impl.oracle.CountryOracle;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:/application.properties")
class GenericServiceTests {

    private static final Logger LOGGER = Logger.getLogger(GenericServiceTests.class.getName());

    @Mock
    private GenericDAO<Country, CountryOracle, Long> dao;
    
    @Autowired
    private GenericService<Country, CountryOracle, Long> genericService;
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @BeforeEach
    void initUseCase() {
        genericService = new GenericService();
        genericService.setDao(dao);
    }

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
        Mockito.doAnswer(i -> {
            return null;
        }).when(dao).deleteById(any(CountryOracle.class), anyLong());
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

    @SuppressWarnings({ "serial", "unchecked" })
    @Test()
    void createAll() {
        LOGGER.debug("test createAll -> GenericService.createAll");
        List<Country> countries = dtos();
        LOGGER.debug("country -> " + countries);
        when(dao.createAll(any(), any(CountryOracle.class), any(), anyList(), anyInt())).thenReturn(countries);
        ResponseEntity<Object> respDefault = new ResponseEntity<>(countries, HttpStatus.OK);
        ResponseEntity<Object> response = genericService.createAll(Country.class, new CountryOracle(), Long.class, "oracle", new ArrayList<Country>() {
        }, 2, "");
        LOGGER.debug("respDefault -> " + respDefault);
        LOGGER.debug("response -> " + response);
        assertNotNull(response);
        assertEquals(respDefault.getStatusCode(), response.getStatusCode());
        List<Country> countriesDefault = (List<Country>) respDefault.getBody();
        assertNotNull(countriesDefault);
        assertFalse(countriesDefault.isEmpty());

    }

    @SuppressWarnings("serial")
    @Test()
    void createAllDuplicateKeyException() {
        LOGGER.debug("test createAllDuplicateKeyException -> GenericService.createAll");
        when(dao.createAll(any(), any(CountryOracle.class), any(), anyList(), anyInt())).thenThrow(DuplicateKeyException.class);
        ResponseEntity<Object> respDefault = new ResponseEntity<>(WSMessageEnums.ERROR_DUPLICATE_REGISTER.getValue(), HttpStatus.INTERNAL_SERVER_ERROR);
        ResponseEntity<Object> response = genericService.createAll(Country.class, new CountryOracle(), Long.class, "oracle", new ArrayList<Country>() {
        }, 2, "");
        LOGGER.debug("respDefault -> " + respDefault);
        LOGGER.debug("response -> " + response);
        assertNotNull(response);
        assertEquals(respDefault.getStatusCode(), response.getStatusCode());
    }

    @SuppressWarnings("serial")
    @Test()
    void createAllDataIntegrityViolationException() {
        LOGGER.debug("test createAllDataIntegrityViolationException -> GenericService.createAll");
        when(dao.createAll(any(), any(CountryOracle.class), any(), anyList(), anyInt())).thenThrow(DataIntegrityViolationException.class);
        ResponseEntity<Object> respDefault = new ResponseEntity<>(WSMessageEnums.ERROR_INTEGRITY_REGISTER.getValue(), HttpStatus.INTERNAL_SERVER_ERROR);
        ResponseEntity<Object> response = genericService.createAll(Country.class, new CountryOracle(), Long.class, "oracle", new ArrayList<Country>() {
        }, 2, "");
        LOGGER.debug("respDefault -> " + respDefault);
        LOGGER.debug("response -> " + response);
        assertNotNull(response);
        assertEquals(respDefault.getStatusCode(), response.getStatusCode());
    }

    @SuppressWarnings("serial")
    @Test()
    void createAllException() {
        LOGGER.debug("test createAllException -> GenericService.createAll");
        when(dao.createAll(any(), any(CountryOracle.class), any(), anyList(), anyInt())).thenThrow(PersistenceException.class);
        String messageError = "E";
        ResponseEntity<Object> respDefault = new ResponseEntity<>(WSMessageEnums.ERROR_SAVE_OR_UPDATE.getValue().replace("\\$1", messageError), HttpStatus.INTERNAL_SERVER_ERROR);
        ResponseEntity<Object> response = genericService.createAll(Country.class, new CountryOracle(), Long.class, "oracle", new ArrayList<Country>() {
        }, 2, messageError);
        LOGGER.debug("respDefault -> " + respDefault);
        LOGGER.debug("response -> " + response);
        assertNotNull(response);
        assertEquals(respDefault.getStatusCode(), response.getStatusCode());
    }

    @SuppressWarnings("unchecked")
    @Test()
    void findAllTwo() throws NumberPageException {
        LOGGER.debug("test findAllTwo -> GenericService.findAll");
        List<Country> dtos = dtos();
        when(dao.findAll(any(CountryOracle.class), any(PageRequest.class), any())).thenReturn(new GenericPage<>(dtos, 1, 1, 1, Sort.by("id"), 1));
        ResponseEntity<Object> response = genericService.findAll(Country.class, new CountryOracle(), 1, 1, "oracle", "Error DB");
        LOGGER.debug("response -> " + response);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertDoesNotThrow(() -> {
            GenericPage<Country> list = (GenericPage<Country>) response.getBody();
            LOGGER.debug("list.size() -> " + list.getSize());
        });
    }

    @Test()
    void findAllTwoNullPointerException() throws NumberPageException {
        LOGGER.debug("test findAllTwoNullPointerException -> GenericService.findAll");
        ResponseEntity<Object> respDefault = new ResponseEntity<>(WSMessageEnums.ERROR_NOT_COUNT.getValue(), HttpStatus.NOT_FOUND);
        when(dao.findAll(any(CountryOracle.class), any(PageRequest.class), any())).thenThrow(NullPointerException.class);
        ResponseEntity<Object> response = genericService.findAll(Country.class, new CountryOracle(), 1, 1, "oracle", "Error DB");
        LOGGER.debug("respDefault -> " + respDefault);
        LOGGER.debug("response -> " + response);
        assertNotNull(response);
        assertEquals(respDefault.getStatusCode(), response.getStatusCode());
    }

    @Test()
    void findAllTwoIllegalArgumentException() throws NumberPageException {
        LOGGER.debug("test findAllTwoIllegalArgumentException -> GenericService.findAll");
        ResponseEntity<Object> respDefault = new ResponseEntity<>(WSMessageEnums.ERROR.getValue() + ": " + WSMessageEnums.ERROR_ILEGAL_PAGE.getValue(), HttpStatus.BAD_REQUEST);
        when(dao.findAll(any(CountryOracle.class), any(PageRequest.class), any())).thenThrow(IllegalArgumentException.class);
        ResponseEntity<Object> response = genericService.findAll(Country.class, new CountryOracle(), 1, 1, "oracle", "Error DB");
        LOGGER.debug("respDefault -> " + respDefault);
        LOGGER.debug("response -> " + response);
        assertNotNull(response);
        assertEquals(respDefault.getStatusCode(), response.getStatusCode());
    }

    @Test()
    void findAllTwoNumberPageException() throws NumberPageException {
        LOGGER.debug("test findAllTwoNumberPageException -> GenericService.findAll");
        ResponseEntity<Object> respDefault = new ResponseEntity<>(WSMessageEnums.ERROR.getValue() + ": ", HttpStatus.BAD_REQUEST);
        when(dao.findAll(any(CountryOracle.class), any(PageRequest.class), any())).thenThrow(NumberPageException.class);
        ResponseEntity<Object> response = genericService.findAll(Country.class, new CountryOracle(), 1, 1, "oracle", "Error DB");
        LOGGER.debug("respDefault -> " + respDefault);
        LOGGER.debug("response -> " + response);
        assertNotNull(response);
        assertEquals(respDefault.getStatusCode(), response.getStatusCode());
    }

    @Test()
    void findAllTwoException() throws NumberPageException {
        LOGGER.debug("test findAllTwoException -> GenericService.findAll");
        String messageError = "Error DB";
        ResponseEntity<Object> respDefault = new ResponseEntity<>(WSMessageEnums.ERROR.getValue() + ": " + WSMessageEnums.ERROR_FIND.getValue().replaceAll("$1", messageError),
                HttpStatus.INTERNAL_SERVER_ERROR);
        when(dao.findAll(any(CountryOracle.class), any(PageRequest.class), any())).thenThrow(PersistenceException.class);
        ResponseEntity<Object> response = genericService.findAll(Country.class, new CountryOracle(), 1, 1, "oracle", messageError);
        LOGGER.debug("respDefault -> " + respDefault);
        LOGGER.debug("response -> " + response);
        assertNotNull(response);
        assertEquals(respDefault.getStatusCode(), response.getStatusCode());
    }
}
