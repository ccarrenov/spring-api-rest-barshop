package com.barshop.app.controllers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.LongStream;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.barshop.app.enums.WSMessageEnums;
import com.barshop.app.models.dao.GenericPage;
import com.barshop.app.models.dto.Country;
import com.barshop.app.models.entity.Entity;
import com.barshop.app.models.entity.impl.oracle.CountryOracle;
import com.barshop.app.services.GenericService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:/application.properties")
class CountryControllerTests {

    private static final Logger LOGGER = Logger.getLogger(CountryControllerTests.class.getName());

    private MockMvc mvc;

    @Mock
    private GenericService<Country, Entity, Long> genericService;

    @Mock
    private Environment env;

    private CountryController countryController;

    @BeforeEach
    void init() {
        LOGGER.debug("init");
        countryController = new CountryController();
        countryController.setEnv(env);
        countryController.setGeneric(genericService);
        this.mvc = MockMvcBuilders.standaloneSetup(countryController).build();
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
    void findAll() throws Exception {
        LOGGER.debug("test findAll -> CountryController.findAll");
        List<Country> countries = dtos();
        ResponseEntity<Object> respDefault = new ResponseEntity<>(countries, HttpStatus.OK);
        when(genericService.findAll(any(), any(CountryOracle.class), any(), any())).thenReturn(respDefault);
        when(env.getProperty(any())).thenReturn("oracle");
        MvcResult result = mvc.perform(get("/api/v1/country").accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertNotNull("MvcResult is null", result);
        MockHttpServletResponse resp = result.getResponse();
        assertNotNull("MockHttpServletResponse is null", resp);
        assertEquals(respDefault.getStatusCodeValue(), resp.getStatus(), "status code != " + respDefault.getStatusCodeValue());
        LOGGER.debug("resp.getContentAsString() -> " + resp.getContentAsString());
        assertNotEquals("", resp.getContentAsString(), "ContentAsString (Response Body) is null");
        Gson gson = new Gson();
        List<Country> respBody = gson.fromJson(resp.getContentAsString(), new TypeToken<List<Country>>() {
        }.getType());
        assertNotNull("response body is empty or error convert json object", respBody);
        assertFalse("response body is empty", respBody.isEmpty());
        assertEquals(countries.size(), respBody.size(), "Arrays size -> " + countries.size() + " != " + respBody.size());
        int i = 0;
        for (Country country : countries) {
            assertEquals(country.getId(), respBody.get(i).getId(), "id: " + country.getId() + " != " + respBody.get(i).getId());
            assertEquals(country.getName(), respBody.get(i).getName(), "id: " + country.getName() + " != " + respBody.get(i).getName());
            assertEquals(country.getCountryCallingCode(), respBody.get(i).getCountryCallingCode(), "id: " + country.getCountryCallingCode() + " != " + respBody.get(i).getCountryCallingCode());
            assertEquals(country.getCountryCode(), respBody.get(i).getCountryCode(), "id: " + country.getCountryCode() + " != " + respBody.get(i).getCountryCode());
            assertEquals(country.getThreeDigitIso(), respBody.get(i).getThreeDigitIso(), "id: " + country.getThreeDigitIso() + " != " + respBody.get(i).getThreeDigitIso());
            assertEquals(country.getTwoDigitIso(), respBody.get(i).getTwoDigitIso(), "id: " + country.getTwoDigitIso() + " != " + respBody.get(i).getTwoDigitIso());
            i++;
        }
    }

    @Test
    void findPage() throws Exception {
        LOGGER.debug("test findAll -> CountryController.findAll");
        List<Country> countries = dtos();
        Page<Country> page = new GenericPage<>(countries, 1, 1, 1, Sort.by("id"), (long) countries.size());
        ResponseEntity<Object> respDefault = new ResponseEntity<>(page, HttpStatus.OK);
        when(genericService.findAll(any(), any(CountryOracle.class), anyInt(), anyInt(), any(), any())).thenReturn(respDefault);
        when(env.getProperty(any())).thenReturn("oracle");
        MvcResult result = mvc.perform(get("/api/v1/country/page/?page={page}&size={size}", 1, 1).accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)/* .param("page", "1").param("size", "1") */).andReturn();
        assertNotNull("MvcResult is null", result);
        MockHttpServletResponse resp = result.getResponse();
        assertNotNull("MockHttpServletResponse is null", resp);
        assertEquals(respDefault.getStatusCodeValue(), resp.getStatus(), "status code != " + respDefault.getStatusCodeValue());
        LOGGER.debug("resp.getContentAsString() -> " + resp.getContentAsString());
        assertNotEquals("", resp.getContentAsString(), "ContentAsString (Response Body) is null");
        Gson gson = new Gson();
        GenericPage<Country> respBody = gson.fromJson(resp.getContentAsString(), new TypeToken<GenericPage<Country>>() {
        }.getType());
        assertNotNull("response body is empty or error convert json object", respBody);
        assertFalse("response body is empty", respBody.getContent().isEmpty());
        assertEquals(countries.size(), respBody.getContent().size(), "Arrays size -> " + countries.size() + " != " + respBody.getSize());
        int i = 0;
        for (Country country : countries) {
            assertEquals(country.getId(), respBody.getContent().get(i).getId(), "id: " + country.getId() + " != " + respBody.getContent().get(i).getId());
            assertEquals(country.getName(), respBody.getContent().get(i).getName(), "id: " + country.getName() + " != " + respBody.getContent().get(i).getName());
            assertEquals(country.getCountryCallingCode(), respBody.getContent().get(i).getCountryCallingCode(),
                    "id: " + country.getCountryCallingCode() + " != " + respBody.getContent().get(i).getCountryCallingCode());
            assertEquals(country.getCountryCode(), respBody.getContent().get(i).getCountryCode(), "id: " + country.getCountryCode() + " != " + respBody.getContent().get(i).getCountryCode());
            assertEquals(country.getThreeDigitIso(), respBody.getContent().get(i).getThreeDigitIso(), "id: " + country.getThreeDigitIso() + " != " + respBody.getContent().get(i).getThreeDigitIso());
            assertEquals(country.getTwoDigitIso(), respBody.getContent().get(i).getTwoDigitIso(), "id: " + country.getTwoDigitIso() + " != " + respBody.getContent().get(i).getTwoDigitIso());
            LOGGER.debug(country);
            LOGGER.debug(respBody.getContent().get(i));
            i++;

        }
    }

    @Test
    void findById() throws Exception {
        LOGGER.debug("test findById -> CountryController.findAll");
        Country country = dto();
        ResponseEntity<Object> respDefault = new ResponseEntity<>(country, HttpStatus.OK);
        when(genericService.findById(any(), any(CountryOracle.class), any(), any())).thenReturn(respDefault);
        when(env.getProperty(any())).thenReturn("oracle");
        MvcResult result = mvc.perform(get("/api/v1/country/{id}", country.getId()).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertNotNull("MvcResult is null", result);
        MockHttpServletResponse resp = result.getResponse();
        assertNotNull("MockHttpServletResponse is null", resp);
        assertEquals(respDefault.getStatusCodeValue(), resp.getStatus(), "status code != " + respDefault.getStatusCodeValue());
        LOGGER.debug("resp.getContentAsString() -> " + resp.getContentAsString());
        assertNotEquals("", resp.getContentAsString(), "ContentAsString (Response Body) is null");
        Gson gson = new Gson();
        Country respBody = gson.fromJson(resp.getContentAsString(), Country.class);
        LOGGER.debug("validate content body");
        assertEquals(country.getId(), respBody.getId(), "id: " + country.getId() + " != " + respBody.getId());
        assertEquals(country.getName(), respBody.getName(), "id: " + country.getName() + " != " + respBody.getName());
        assertEquals(country.getCountryCallingCode(), respBody.getCountryCallingCode(), "id: " + country.getCountryCallingCode() + " != " + respBody.getCountryCallingCode());
        assertEquals(country.getCountryCode(), respBody.getCountryCode(), "id: " + country.getCountryCode() + " != " + respBody.getCountryCode());
        assertEquals(country.getThreeDigitIso(), respBody.getThreeDigitIso(), "id: " + country.getThreeDigitIso() + " != " + respBody.getThreeDigitIso());
        assertEquals(country.getTwoDigitIso(), respBody.getTwoDigitIso(), "id: " + country.getTwoDigitIso() + " != " + respBody.getTwoDigitIso());
    }

    @Test
    void count() throws Exception {
        LOGGER.debug("test count -> CountryController.count");
        Long size = 10L;
        ResponseEntity<Object> respDefault = new ResponseEntity<>(size, HttpStatus.OK);
        when(genericService.count(any(), any())).thenReturn(respDefault);
        when(env.getProperty(any())).thenReturn("oracle");
        MvcResult result = mvc.perform(get("/api/v1/country/count").accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertNotNull("MvcResult is null", result);
        MockHttpServletResponse resp = result.getResponse();
        assertNotNull("MockHttpServletResponse is null", resp);
        assertEquals(respDefault.getStatusCodeValue(), resp.getStatus(), "status code != " + respDefault.getStatusCodeValue());
        LOGGER.debug("resp.getContentAsString() -> " + resp.getContentAsString());
        assertNotEquals("", resp.getContentAsString(), "ContentAsString (Response Body) is null");
        Gson gson = new Gson();
        Long respBody = gson.fromJson(resp.getContentAsString(), Long.class);
        assertEquals(size, respBody, "Error count");
    }

    @Test
    void deleteById() throws Exception {
        LOGGER.debug("test findById -> CountryController.deleteById");
        Country country = dto();
        ResponseEntity<Object> respDefault = new ResponseEntity<>(WSMessageEnums.SUCCESS_DELETE.getValue(), HttpStatus.OK);
        when(genericService.deleteById(any(), any(), any())).thenReturn(respDefault);
        when(env.getProperty(any())).thenReturn("oracle");
        MvcResult result = mvc.perform(delete("/api/v1/country/{id}", country.getId()).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertNotNull("MvcResult is null", result);
        MockHttpServletResponse resp = result.getResponse();
        assertNotNull("MockHttpServletResponse is null", resp);
        assertEquals(respDefault.getStatusCodeValue(), resp.getStatus(), "status code != " + respDefault.getStatusCodeValue());
        LOGGER.debug("resp.getContentAsString() -> " + resp.getContentAsString());
        assertNotEquals("", resp.getContentAsString(), "ContentAsString (Response Body) is null");
    }
}
