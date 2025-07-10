package com.prueba.franquicias_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Contiene la información de la sucursal y su producto de mayor stock.
 */
@Data
@AllArgsConstructor
public class ProductoMaxStockDTO {
    private String sucursalId;
    private String sucursalNombre;
    private String productoId;
    private String productoNombre;
    private int stock;
}