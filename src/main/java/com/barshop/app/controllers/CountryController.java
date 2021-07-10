package com.barshop.app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.barshop.app.models.dto.Country;
import com.barshop.app.models.entity.CountryEntity;
import com.barshop.app.models.services.GenericService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

//@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api/v1/country")
@Api(value = "Country", description = "Everything about countries ", tags = { "Country" })
public class CountryController implements BaseRestController<Country, Integer> {

	@Autowired
	private GenericService<Country, CountryEntity, Integer> generic;

	private final static String RESOURCE = "Country";

	@ApiOperation(value = "Finds all countries", tags = { "Country" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = Country[].class),
			@ApiResponse(code = 500, message = "Failure") })
	@GetMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE })
	@Override
	public ResponseEntity<Object> findAll() {
		return generic.findAll(Country.class, CountryEntity.class, RESOURCE, "countries");
	}

//	@ApiOperation(value = "Finds all countries filter pages", tags = { "Country" })
//	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = Country[].class),
//			@ApiResponse(code = 500, message = "Failure") })
//	@GetMapping(value = "page/", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE })
//	public ResponseEntity<Object> findAll(@RequestParam("page") int page, @RequestParam("size") int size) {
//		return generic.findAll(Country.class, PageRequest.of(page, size), RESOURCE, "los countryes");
//	}

	@ApiOperation(value = "Método para crear un country o actualizarlo", tags = { "Country" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = Country.class),
			@ApiResponse(code = 500, message = "Failure") })
	@PostMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE })
	@Override
	public ResponseEntity<Object> createOrUpdate(Country object) {
		return generic.createOrUpdate(Country.class, CountryEntity.class, Integer.class, RESOURCE, object,
				"country ID: " + object.getId());
	}
	
	@ApiOperation(value = "Método para crear un country o actualizarlo", tags = { "Country" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = Country[].class),
			@ApiResponse(code = 500, message = "Failure") })
	@PostMapping(value = "create-countries", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE })
	@Override
	public ResponseEntity<Object> createAll(List<Country> objects) {
		return generic.createAll(Country.class, CountryEntity.class, Integer.class, RESOURCE, objects,
				"countries : " + objects.size());
	}	

	@ApiOperation(value = "Método para obtener un country por id", tags = { "Country" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = Country.class),
			@ApiResponse(code = 500, message = "Failure") })
	@GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE })
	@Override
	public ResponseEntity<Object> findById(Integer id) {
		return generic.findById(Country.class, CountryEntity.class, RESOURCE, id);
	}

	@ApiOperation(value = "Método para borrar un country por id", tags = { "Country" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = Country.class),
			@ApiResponse(code = 500, message = "Failure") })
	@DeleteMapping(value = "/{id}", produces = { MediaType.TEXT_PLAIN_VALUE })
	@Override
	public ResponseEntity<Object> deleteById(Integer id) {
		return generic.deleteById(CountryEntity.class, RESOURCE, id);
	}	
}
