package com.barshop.app.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WSMessageEnums {

	INIT("INIT "),
	FINISH("FINISH "),
	ERROR("ERROR"),
	SUCCESS("SUCCESS"),
	SUCCESS_DELETE("Registro eliminado."),	
	ERROR_DUPLICATE_REGISTER("Registro duplicado."),
	ERROR_INTEGRITY_REGISTER("Error de integridad de datos."),	
	ERROR_FIND("Problema al buscar $1."),
	ERROR_NOT_FOUND("Registro no encontrado -> id $1"),
	ERROR_NOT_COUNT("No se pudo encontrar registros."),	
	ERROR_SAVE_OR_UPDATE("Problema al registra o actualizar $1 en la Base de Datos."),
	ERROR_DELETE("Problema al borrar el registro en la Base de Datos."),
	ERROR_NUMBER_PAGE("Numero de página incorrecto. Total de paginas $1."),
	ERROR_ILEGAL_PAGE("La página no puede ser menor a 1.");
    
	private String value;
	
}
