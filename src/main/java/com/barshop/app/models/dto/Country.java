package com.barshop.app.models.dto;

import java.io.Serializable;

import com.barshop.app.models.mapper.MapperAnnotation;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class Country extends DataAccessObject implements Serializable {

	private static final long serialVersionUID = 1L;

	@MapperAnnotation
	@ApiModelProperty(position = 0)
	@JsonProperty("id")
	private long id;

	@MapperAnnotation
	@ApiModelProperty(position = 1)
	@JsonProperty("name")

	private String name;

	@MapperAnnotation
	@ApiModelProperty(position = 2)
	@JsonProperty("countryCode")
	private short countryCode;

	@MapperAnnotation
	@ApiModelProperty(position = 3)
	@JsonProperty("twoDigitIso")
	private String twoDigitIso;

	@MapperAnnotation
	@ApiModelProperty(position = 4)
	@JsonProperty("threeDigitIso")
	private String threeDigitIso;

	@MapperAnnotation
	@ApiModelProperty(position = 5)
	@JsonProperty("countryCallingCode")
	private String countryCallingCode;
}
