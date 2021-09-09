package com.barshop.app.models.mapper.entity;

import com.barshop.app.models.mapper.MapperAnnotation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Entity {

    @MapperAnnotation
    private long id;

    @MapperAnnotation
    private String name;
}
