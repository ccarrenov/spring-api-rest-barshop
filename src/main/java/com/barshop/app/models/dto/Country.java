package com.barshop.app.models.dto;

import java.io.Serializable;

import com.barshop.app.models.mapper.MapperAnnotation;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@JsonPropertyOrder({"id", "descripcion"})
public class Country extends DataAccessObject implements Serializable {

	private static final long serialVersionUID = 1L;

	@MapperAnnotation
	@JsonProperty("id")	
	private int id;

	@MapperAnnotation
	@JsonProperty("name")	
	private String name;
	
	@MapperAnnotation
	@JsonProperty("countryCode")	
	private short countryCode;	
	
	@MapperAnnotation
	@JsonProperty("twoDigitIso")	
	private String twoDigitIso;		
	
	@MapperAnnotation
	@JsonProperty("threeDigitIso")	
	private String threeDigitIso;		
	
	@MapperAnnotation
	@JsonProperty("countryCallingCode")	
	private String countryCallingCode;	
}
