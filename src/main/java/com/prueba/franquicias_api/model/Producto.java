package com.prueba.franquicias_api.model;

import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    private String id = UUID.randomUUID().toString();

    private String nombre;

    private int stock;
}