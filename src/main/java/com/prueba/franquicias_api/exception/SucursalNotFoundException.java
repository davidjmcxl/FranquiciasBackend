package com.prueba.franquicias_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción lanzada cuando no se encuentra una sucursal dentro de una franquicia.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class SucursalNotFoundException extends RuntimeException {
    public SucursalNotFoundException(String id) {
        super("Sucursal no encontrada con id: " + id);
    }
}