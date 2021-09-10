package com.barshop.app.controllers;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.barshop.app.models.dto.Country;
import com.barshop.app.models.entity.Entity;
import com.barshop.app.models.entity.impl.oracle.CountryOracle;
import com.barshop.app.models.services.GenericService;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:/application.properties")
class CountryControllerTests {

    private static final Logger LOGGER = Logger.getLogger(CountryControllerTests.class.getName());

    private MockMvc mvc;

    @Mock
    private GenericService<Country, Entity, Long> genericService;

    @Mock
    private Environment env;

    // @Autowired
    private CountryController countryController;

    @BeforeEach
    void init() {
        LOGGER.debug("init");
        countryController = new CountryController(genericService, env);
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
        assertNotNull(result);
        MockHttpServletResponse resp = result.getResponse();
        assertNotNull(resp);
        assertEquals(respDefault.getStatusCodeValue(), resp.getStatus());
        LOGGER.debug("resp.getContentAsString() -> " + resp.getContentAsString());

    }
}
