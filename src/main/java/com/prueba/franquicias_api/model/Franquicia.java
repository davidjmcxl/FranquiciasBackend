package com.prueba.franquicias_api.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "franquicias")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Franquicia {

    @Id
    private String id;

    private String nombre;

    private List<Sucursal> sucursales = new ArrayList<>();
}