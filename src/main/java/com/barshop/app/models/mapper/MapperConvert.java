package com.barshop.app.models.mapper;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barshop.app.models.mapper.util.ReflexionUtil;

import lombok.Getter;

@Getter
public class MapperConvert<D, E> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapperConvert.class);

    Set<MapperFieldAnnotation> fields;

    public MapperConvert() {
        fields = new LinkedHashSet<>();
    }

    private E createEntity( Class<E> clazzName ) {
        LOGGER.debug("destiny clazzName: {}", clazzName);
        E newInstance = null;
        try {
            newInstance = ReflexionUtil.createNewInstance(clazzName);
            LOGGER.debug("new object: {}", newInstance);
            for (MapperFieldAnnotation fieldM : fields) {
                newInstance = ReflexionUtil.setAttribute(newInstance, fieldM);
            }
        } catch (Exception e) {
            LOGGER.debug("Error New Instance Reflexion Class {}", e.getMessage());
        }
        LOGGER.debug("destiny object: {}", newInstance);
        return newInstance;
    }

    public E convertObject( D obj1, Class<E> obj2 ) {
        fields = ReflexionUtil.fieldsByAnnotation(obj1, MapperAnnotation.class);
        return createEntity(obj2);
    }

    public List<E> convertListObjects( List<D> obj1, Class<E> obj2 ) {
        List<E> newList = new ArrayList<>();
        for (D obj : obj1) {
            newList.add(convertObject(obj, obj2));
        }
        return newList;
    }
}
