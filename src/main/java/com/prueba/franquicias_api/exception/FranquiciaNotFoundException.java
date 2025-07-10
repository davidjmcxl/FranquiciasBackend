package com.prueba.franquicias_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción lanzada cuando no se encuentra una franquicia con el ID indicado.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class FranquiciaNotFoundException extends RuntimeException {
    public FranquiciaNotFoundException(String id) {
        super("Franquicia no encontrada con id: " + id);
    }
}
