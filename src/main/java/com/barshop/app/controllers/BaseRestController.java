package com.barshop.app.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.barshop.app.models.dto.DataAccessObject;

public interface BaseRestController<T extends DataAccessObject, I> {

	public abstract ResponseEntity<Object> findAll();

//	public abstract ResponseEntity<Object> createOrUpdate(@RequestBody T object);
//	
//	public abstract ResponseEntity<Object> findById(@PathVariable I id);	
//		
//	public abstract ResponseEntity<Object> deleteById(@PathVariable I id);
}
