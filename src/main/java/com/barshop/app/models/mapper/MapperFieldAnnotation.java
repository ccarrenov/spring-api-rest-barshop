package com.barshop.app.models.mapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class MapperFieldAnnotation {

	private String attribute;
	private Class<?> type;
	private Object value;
}
