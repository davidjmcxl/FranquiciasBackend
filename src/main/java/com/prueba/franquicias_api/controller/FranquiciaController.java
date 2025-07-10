package com.prueba.franquicias_api.controller;

import com.prueba.franquicias_api.dto.ProductoMaxStockDTO;
import com.prueba.franquicias_api.model.Franquicia;
import com.prueba.franquicias_api.model.Producto;
import com.prueba.franquicias_api.model.Sucursal;
import com.prueba.franquicias_api.service.FranquiciaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/franquicias")
public class FranquiciaController {

    private final FranquiciaService franquiciaService;

    public FranquiciaController(FranquiciaService franquiciaService) {
        this.franquiciaService = franquiciaService;
    }

    @PostMapping
    public ResponseEntity<Franquicia> crear(@RequestBody Franquicia franquicia) {
        Franquicia creada = franquiciaService.crearFranquicia(franquicia);
        return ResponseEntity.ok(creada);
    }

    @GetMapping
    public ResponseEntity<List<Franquicia>> listar() {
        return ResponseEntity.ok(franquiciaService.listarFranquicias());
    }

    @PostMapping("/{franquiciaId}/sucursales")
    public ResponseEntity<Franquicia> agregarSucursal(
            @PathVariable String franquiciaId,
            @RequestBody Sucursal sucursal) {
        Franquicia actualizada = franquiciaService.agregarSucursal(franquiciaId, sucursal);
        return ResponseEntity.ok(actualizada);
    }
    @PostMapping("/{franquiciaId}/sucursales/{sucursalId}/productos")
    public ResponseEntity<Franquicia> agregarProducto(
            @PathVariable String franquiciaId,
            @PathVariable String sucursalId,
            @RequestBody Producto producto) {
        Franquicia actualizada = franquiciaService.agregarProducto(franquiciaId, sucursalId, producto);
        return ResponseEntity.ok(actualizada);
    }
    /**
     * Elimina un producto de una sucursal.
     *
     * @param franquiciaId ID de la franquicia.
     * @param sucursalId   ID de la sucursal.
     * @param productoId   ID del producto a eliminar.
     * @return La franquicia actualizada.
     */
    @DeleteMapping("/{franquiciaId}/sucursales/{sucursalId}/productos/{productoId}")
    public ResponseEntity<Franquicia> eliminarProducto(
            @PathVariable String franquiciaId,
            @PathVariable String sucursalId,
            @PathVariable String productoId) {
        Franquicia actualizada = franquiciaService.eliminarProducto(franquiciaId, sucursalId, productoId);
        return ResponseEntity.ok(actualizada);
    }
    /**
     * Modifica el stock de un producto.
     *
     * @param franquiciaId ID de la franquicia.
     * @param sucursalId   ID de la sucursal.
     * @param productoId   ID del producto.
     * @param body         JSON con el nuevo stock, por ejemplo: { "stock": 50 }
     * @return La franquicia actualizada.
     */
    @PatchMapping("/{franquiciaId}/sucursales/{sucursalId}/productos/{productoId}/stock")
    public ResponseEntity<Franquicia> actualizarStock(
            @PathVariable String franquiciaId,
            @PathVariable String sucursalId,
            @PathVariable String productoId,
            @RequestBody Map<String, Integer> body) {
        Integer nuevoStock = body.get("stock");
        Franquicia actualizada = franquiciaService.actualizarStock(franquiciaId, sucursalId, productoId, nuevoStock);
        return ResponseEntity.ok(actualizada);
    }
    /**
     * Obtiene para cada sucursal de la franquicia el producto con mayor stock.
     *
     * @param franquiciaId ID de la franquicia.
     * @return Lista de sucursales con su producto de mayor stock.
     */
    @GetMapping("/{franquiciaId}/productos-max-stock")
    public ResponseEntity<List<ProductoMaxStockDTO>> getProductosMaxStock(
            @PathVariable String franquiciaId) {
        List<ProductoMaxStockDTO> lista = franquiciaService
                .listarProductoMaxStockPorSucursal(franquiciaId);
        return ResponseEntity.ok(lista);
    }
}