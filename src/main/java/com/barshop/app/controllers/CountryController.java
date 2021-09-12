package com.barshop.app.controllers;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.barshop.app.App;
import com.barshop.app.models.dto.Country;
import com.barshop.app.models.entity.Entity;
import com.barshop.app.models.entity.EntityConstant;
import com.barshop.app.models.mapper.util.EntityReflexionUtil;
import com.barshop.app.services.GenericService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.Setter;

//@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api/v1/country")
@Api(value = "Country", tags = { App.COUNTRY_TAG })
@Setter
public class CountryController implements BaseRestController<Country, Long> {

    private static final Logger LOGGER = Logger.getLogger(CountryController.class.getName());

    private final static String RESOURCE = "Country";

    @Autowired
    private GenericService<Country, Entity, Long> generic;

    @Autowired
    private Environment env;

    private final static int NUMBER_COMMIT_SIZE = 3500;

    @ApiOperation(value = "Finds all countries", tags = { App.COUNTRY_TAG })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = Country[].class), @ApiResponse(code = 500, message = "Failure") })
    @GetMapping(value = "", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE })
    @Override
    public ResponseEntity<Object> findAll() {
        Entity country = EntityReflexionUtil.newInstance(EntityConstant.COUNTRY_ENTITY, env);
        ResponseEntity<Object> resp = generic.findAll(Country.class, country, RESOURCE, "countries");
        LOGGER.info("resp -> " + resp);
        return resp;
    }

    @ApiOperation(value = "Finds all countries filter pages", tags = { App.COUNTRY_TAG })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = Country[].class), @ApiResponse(code = 500, message = "Failure") })
    @GetMapping(value = "page/", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE })
    public ResponseEntity<Object> findAll( @RequestParam("page") int page, @RequestParam("size") int size ) {
        Entity country = EntityReflexionUtil.newInstance(EntityConstant.COUNTRY_ENTITY, env);
        return generic.findAll(Country.class, country, page, size, RESOURCE, "los countries");
    }

    @ApiOperation(value = "Create or update a country", tags = { App.COUNTRY_TAG })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = Country.class), @ApiResponse(code = 500, message = "Failure") })
    @PostMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE })
    @Override
    public ResponseEntity<Object> createOrUpdate( Country object ) {
        Entity country = EntityReflexionUtil.newInstance(EntityConstant.COUNTRY_ENTITY, env);
        return generic.createOrUpdate(Country.class, country, Long.class, RESOURCE, object, "country ID: " + object.getId());
    }

    @ApiOperation(value = "create countries list", tags = { App.COUNTRY_TAG })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = Country[].class), @ApiResponse(code = 500, message = "Failure") })
    @PostMapping(value = "create-countries", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE })
    @Override
    public ResponseEntity<Object> createAll( List<Country> objects ) {
        Entity country = EntityReflexionUtil.newInstance(EntityConstant.COUNTRY_ENTITY, env);
        return generic.createAll(Country.class, country, Long.class, RESOURCE, objects, NUMBER_COMMIT_SIZE, "countries : " + objects.size());
    }

    @ApiOperation(value = "count countries", tags = { App.COUNTRY_TAG })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = Integer.class), @ApiResponse(code = 500, message = "Failure") })
    @GetMapping(value = "/count", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE })
    @Override
    public ResponseEntity<Object> count() {
        Entity country = EntityReflexionUtil.newInstance(EntityConstant.COUNTRY_ENTITY, env);
        return generic.count(country, RESOURCE);
    }

    @ApiOperation(value = "find a country by id", tags = { App.COUNTRY_TAG })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = Country.class), @ApiResponse(code = 500, message = "Failure") })
    @GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE })
    @Override
    public ResponseEntity<Object> findById( Long id ) {
        Entity country = EntityReflexionUtil.newInstance(EntityConstant.COUNTRY_ENTITY, env);
        return generic.findById(Country.class, country, RESOURCE, id);
    }

    @ApiOperation(value = "delete a country by id", tags = { App.COUNTRY_TAG })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = Country.class), @ApiResponse(code = 500, message = "Failure") })
    @DeleteMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE })
    @Override
    public ResponseEntity<Object> deleteById( Long id ) {
        Entity country = EntityReflexionUtil.newInstance(EntityConstant.COUNTRY_ENTITY, env);
        return generic.deleteById(country, RESOURCE, id);
    }
}
