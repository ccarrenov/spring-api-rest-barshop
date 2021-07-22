package com.barshop.app.models.entity.impl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.barshop.app.models.entity.CountryEntity;
import com.barshop.app.models.mapper.MapperAnnotation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "country")
@ToString
@Getter
@Setter
public class CountryMySQL implements CountryEntity {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	@MapperAnnotation
	private long id;

	@Column(name = "name", nullable = false)
	@MapperAnnotation
	private String name;
	
	@Column(name = "country_code", nullable = false)
	@MapperAnnotation
	private short countryCode;	
	
	@Column(name = "two_digit_iso", nullable = false)
	@MapperAnnotation
	private String twoDigitIso;		
	
	@Column(name = "three_digit_iso", nullable = false)
	@MapperAnnotation
	private String threeDigitIso;		
	
	@Column(name = "country_calling_code", nullable = false)
	@MapperAnnotation
	private String countryCallingCode;	
	
}
