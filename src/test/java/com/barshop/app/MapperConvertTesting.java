package com.barshop.app;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.barshop.app.models.dto.Country;
import com.barshop.app.models.entity.CountryEntity;
import com.barshop.app.models.mapper.MapperConvert;

@SpringBootTest
class MapperConvertTesting {

	private static final Logger LOGGER = Logger.getLogger(MapperConvertTesting.class.getName());
	
	@Test
	void simpleConvertDtoToEntity() {
		Country dto = new Country();
		dto.setId(1);
		dto.setCountryCode(new Short("56"));
		dto.setName("Chile");
		dto.setTwoDigitIso("CL");
		dto.setThreeDigitIso("CHL");
		dto.setCountryCallingCode("+56");
		LOGGER.debug("DTO: " + dto);
		MapperConvert<Country, CountryEntity> convert = new MapperConvert<>();
		CountryEntity entity = convert.convertObject(dto, CountryEntity.class);
		assertEquals(dto.getId(), entity.getId());
		assertEquals(dto.getName(), entity.getName());
		assertEquals(dto.getCountryCode(), entity.getCountryCode());
		assertEquals(dto.getTwoDigitIso(), entity.getTwoDigitIso());
		assertEquals(dto.getThreeDigitIso(), entity.getThreeDigitIso());
		assertEquals(dto.getCountryCallingCode(), entity.getCountryCallingCode());
	}
	
	@Test
	void simpleConvertEntityToDTO() {
		CountryEntity entity = new CountryEntity();
		entity.setId(1);
		entity.setCountryCode(new Short("56"));
		entity.setName("Chile");
		entity.setTwoDigitIso("CL");
		entity.setThreeDigitIso("CHL");
		entity.setCountryCallingCode("+56");
		LOGGER.debug("ENTITY: " + entity);
		MapperConvert<CountryEntity, Country> convert = new MapperConvert<>();
		Country dto = convert.convertObject(entity, Country.class);
		assertEquals(dto.getId(), entity.getId());
		assertEquals(dto.getName(), entity.getName());
		assertEquals(dto.getCountryCode(), entity.getCountryCode());
		assertEquals(dto.getTwoDigitIso(), entity.getTwoDigitIso());
		assertEquals(dto.getThreeDigitIso(), entity.getThreeDigitIso());
		assertEquals(dto.getCountryCallingCode(), entity.getCountryCallingCode());
	}	
}
