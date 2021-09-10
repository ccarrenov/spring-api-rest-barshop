package com.barshop.app.models.mapper;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.LongStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.TestPropertySource;

import com.barshop.app.models.mapper.dto.DTO;
import com.barshop.app.models.mapper.entity.Entity;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:/application.properties")
class MapperConvertTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapperConvertTests.class);

    private Long id() {
        Random random = new Random();
        LongStream longStream = random.longs(1L, 1L, 8000L);
        Iterator<Long> iterator = longStream.iterator();
        return iterator.next();
    }

    private DTO dto() {
        LOGGER.debug("create dto");
        DTO dto = new DTO();
        dto.setId(id());
        dto.setName("John");
        LOGGER.debug("dto -> {}", dto);
        return dto;
    }

    private Entity entity() {
        LOGGER.debug("create entity");
        Entity entity = new Entity();
        entity.setId(id());
        entity.setName("John");
        LOGGER.debug("entity -> {}", entity);
        return entity;
    }

    private List<Entity> entities() {
        LOGGER.debug("create list CountryOracle");
        List<Entity> entities = new ArrayList<>();
        entities.add(entity());
        LOGGER.debug("countriesOracle -> {}", entities);
        return entities;
    }

    @BeforeEach
    void init() {
        LOGGER.debug("testing init -> MapperConvert");
    }

    @Test
    void simpleConvertDtoToEntity() {
        LOGGER.debug("testing simpleConvertDtoToEntity -> MapperConvert.convertObject");
        DTO dto = dto();
        MapperConvert<DTO, Entity> convert = new MapperConvert<>();
        Entity newEntity = convert.convertObject(dto, Entity.class);
        LOGGER.debug("newEntity -> {}", newEntity);
        assertNotNull(newEntity);
        assertEquals(dto.getId(), newEntity.getId());
        assertEquals(dto.getName(), newEntity.getName());
    }

     @Test
    void simpleConvertEntityToDTO() {
        LOGGER.debug("testing simpleConvertEntityToDTO -> MapperConvert.convertObject");
        Entity entity = entity();
        MapperConvert<Entity, DTO> convert = new MapperConvert<>();
        DTO newDTO = convert.convertObject(entity, DTO.class);
        LOGGER.debug("newDTO -> {}", newDTO);
        assertNotNull(newDTO);
        assertEquals(entity.getId(), newDTO.getId());
        assertEquals(entity.getName(), newDTO.getName());
    }

     @Test
    void simpleConvertListEntityToListDTO() {
        LOGGER.debug("testing simpleConvertListEntityToListDTO -> MapperConvert.convertListObjects");
        List<Entity> entities = entities();
        MapperConvert<Entity, DTO> convert = new MapperConvert<>();
        List<DTO> newDTOS = convert.convertListObjects(entities, DTO.class);
        LOGGER.debug("newDTOS -> %", newDTOS);
        assertNotNull(newDTOS);
        assertFalse(newDTOS.isEmpty());
        int i = 0;
        for (Entity ent : entities) {
            DTO dto = newDTOS.get(i++);
            assertEquals(dto.getId(), ent.getId());
            assertEquals(dto.getName(), ent.getName());
        }
    }
}
