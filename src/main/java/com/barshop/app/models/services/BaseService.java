package com.barshop.app.models.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.barshop.app.enums.WSMessageEnums;

public class BaseService {

    public ResponseEntity<Object> msg( String attribute, String msg, HttpStatus status ) {
        Map<String, Object> map = new HashMap<>();
        map.put(attribute, msg);
        return new ResponseEntity<>(map, status);
    }

    public ResponseEntity<Object> msg( String msg, HttpStatus status ) {
        return new ResponseEntity<>(msg, status);
    }

    public ResponseEntity<Object> response( Object obj, HttpStatus status ) {
        return new ResponseEntity<>(obj, status);
    }

    public ResponseEntity<Object> responseEmpty( Object obj, HttpStatus status ) {
        if (obj == null)
            return new ResponseEntity<>(WSMessageEnums.ERROR_NOT_FOUND.getValue(), HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<>(obj, status);
    }
}
