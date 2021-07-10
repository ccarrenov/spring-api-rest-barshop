package com.barshop.app.models.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.barshop.app.models.mapper.MapperAnnotation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "country")
@ToString
@Getter
@Setter
public class CountryEntity extends com.barshop.app.models.entity.Entity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE)	
	@SequenceGenerator(name="country_seq", sequenceName="country_seq")
	@Column(name = "id")
	@MapperAnnotation
	private int id;

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
