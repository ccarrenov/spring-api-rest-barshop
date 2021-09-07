package com.barshop.app;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.barshop.app.models.dto.Country;
import com.barshop.app.models.entity.impl.mysql.CountryMySQL;
import com.barshop.app.models.entity.impl.oracle.CountryOracle;
import com.barshop.app.models.mapper.MapperConvert;

@SpringBootTest
@TestPropertySource(locations = "classpath:/application.properties")
class MapperConvertTests {

    private static final Logger LOGGER = Logger.getLogger(MapperConvertTests.class.getName());

    @Test
    void simpleConvertDtoToEntityMySQL() {
        Country dto = new Country();
        dto.setId(1);
        dto.setCountryCode(new Short("56"));
        dto.setName("Chile");
        dto.setTwoDigitIso("CL");
        dto.setThreeDigitIso("CHL");
        dto.setCountryCallingCode("+56");
        LOGGER.debug("DTO: " + dto);
        MapperConvert<Country, CountryMySQL> convert = new MapperConvert<>();
        CountryMySQL entity = convert.convertObject(dto, CountryMySQL.class);
        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getName(), entity.getName());
        assertEquals(dto.getCountryCode(), entity.getCountryCode());
        assertEquals(dto.getTwoDigitIso(), entity.getTwoDigitIso());
        assertEquals(dto.getThreeDigitIso(), entity.getThreeDigitIso());
        assertEquals(dto.getCountryCallingCode(), entity.getCountryCallingCode());
    }

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
        MapperConvert<Country, CountryOracle> convert = new MapperConvert<>();
        CountryOracle entity = convert.convertObject(dto, CountryOracle.class);
        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getName(), entity.getName());
        assertEquals(dto.getCountryCode(), entity.getCountryCode());
        assertEquals(dto.getTwoDigitIso(), entity.getTwoDigitIso());
        assertEquals(dto.getThreeDigitIso(), entity.getThreeDigitIso());
        assertEquals(dto.getCountryCallingCode(), entity.getCountryCallingCode());
    }

    @Test
    void simpleConvertEntityToDTO() {
        CountryOracle entity = new CountryOracle();
        entity.setId(1);
        entity.setCountryCode(new Short("56"));
        entity.setName("Chile");
        entity.setTwoDigitIso("CL");
        entity.setThreeDigitIso("CHL");
        entity.setCountryCallingCode("+56");
        LOGGER.debug("ENTITY: " + entity);
        MapperConvert<CountryOracle, Country> convert = new MapperConvert<>();
        Country dto = convert.convertObject(entity, Country.class);
        assertNotNull(dto);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getName(), entity.getName());
        assertEquals(dto.getCountryCode(), entity.getCountryCode());
        assertEquals(dto.getTwoDigitIso(), entity.getTwoDigitIso());
        assertEquals(dto.getThreeDigitIso(), entity.getThreeDigitIso());
        assertEquals(dto.getCountryCallingCode(), entity.getCountryCallingCode());
    }

    @Test
    void simpleConvertListEntityToListDTO() {
        List<CountryOracle> entities = new ArrayList<>();
        CountryOracle entity = new CountryOracle();
        entity.setId(1);
        entity.setCountryCode(new Short("56"));
        entity.setName("Chile");
        entity.setTwoDigitIso("CL");
        entity.setThreeDigitIso("CHL");
        entity.setCountryCallingCode("+56");
        LOGGER.debug("ENTITY: " + entity);
        CountryOracle entity2 = new CountryOracle();
        entity2.setId(1);
        entity2.setCountryCode(new Short("1"));
        entity2.setName("EEUU");
        entity2.setTwoDigitIso("US");
        entity2.setThreeDigitIso("USA");
        entity2.setCountryCallingCode("+1");
        LOGGER.debug("ENTITY: " + entity2);
        entities.add(entity);
        entities.add(entity2);
        MapperConvert<CountryOracle, Country> convert = new MapperConvert<>();
        List<Country> dtos = convert.convertListObjects(entities, Country.class);
        assertNotNull(dtos);
        assertFalse(dtos.isEmpty());
        int i = 0;
        for (CountryOracle ent : entities) {
            Country dto = dtos.get(i++);
            assertEquals(dto.getId(), ent.getId());
            assertEquals(dto.getName(), ent.getName());
            assertEquals(dto.getCountryCode(), ent.getCountryCode());
            assertEquals(dto.getTwoDigitIso(), ent.getTwoDigitIso());
            assertEquals(dto.getThreeDigitIso(), ent.getThreeDigitIso());
            assertEquals(dto.getCountryCallingCode(), ent.getCountryCallingCode());
        }
    }
}
