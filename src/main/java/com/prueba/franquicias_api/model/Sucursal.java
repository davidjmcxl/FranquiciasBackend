package com.prueba.franquicias_api.model;


import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sucursal {

    private String id = UUID.randomUUID().toString();

    private String nombre;

    private List<Producto> productos = new ArrayList<>();

    public <E> Sucursal(String sucursalA, ArrayList<E> es) {
    }
}
