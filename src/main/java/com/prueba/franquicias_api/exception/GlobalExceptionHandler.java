package com.prueba.franquicias_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.validation.BindException;

import java.util.Map;

/**
 * Maneja de forma centralizada las excepciones lanzadas por los controladores y servicios,
 * devolviendo respuestas JSON con el código HTTP apropiado y un mensaje claro.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FranquiciaNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleFranquiciaNotFound(FranquiciaNotFoundException ex) {
        return Map.of("error", ex.getMessage());
    }

    @ExceptionHandler(SucursalNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleSucursalNotFound(SucursalNotFoundException ex) {
        return Map.of("error", ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleBadRequest(IllegalArgumentException ex) {
        return Map.of("error", ex.getMessage());
    }

    @ExceptionHandler({ BindException.class, MethodArgumentTypeMismatchException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationErrors(Exception ex) {
        return Map.of("error", "Datos de entrada inválidos",
                "detalle", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleAllUncaught(Exception ex) {
        return Map.of("error", "Error interno del servidor",
                "detalle", ex.getMessage());
    }
}