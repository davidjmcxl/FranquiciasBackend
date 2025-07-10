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
    /**
     * Endpoint para crear una nueva franquicia.
     *
     * @param franquicia Objeto {@link Franquicia} recibido en el cuerpo de la petición,
     *                   que contiene el nombre de la franquicia y opcionalmente una lista de sucursales.
     * @return ResponseEntity con la franquicia creada, incluyendo su ID generado.
     */
    @PostMapping
    public ResponseEntity<Franquicia> crear(@RequestBody Franquicia franquicia) {
        Franquicia creada = franquiciaService.crearFranquicia(franquicia);
        return ResponseEntity.ok(creada);
    }
    /**
     * Endpoint para listar todas las franquicias registradas en el sistema.
     *
     * @return ResponseEntity que contiene una lista de objetos {@link Franquicia}.
      */
    @GetMapping
    public ResponseEntity<List<Franquicia>> listar() {
        return ResponseEntity.ok(franquiciaService.listarFranquicias());
    }
    /**
     * Endpoint para agregar una nueva sucursal a una franquicia existente.
     *
     * @param franquiciaId ID de la franquicia a la cual se desea agregar la sucursal.
     * @param sucursal Objeto Sucursal recibido en el cuerpo de la petición, que contiene el nombre y lista de productos (opcional).
     * @return ResponseEntity con la franquicia actualizada que ahora incluye la nueva sucursal
     */
    @PostMapping("/{franquiciaId}/sucursales")
    public ResponseEntity<Franquicia> agregarSucursal(
            @PathVariable String franquiciaId,
            @RequestBody Sucursal sucursal) {
        Franquicia actualizada = franquiciaService.agregarSucursal(franquiciaId, sucursal);
        return ResponseEntity.ok(actualizada);
    }
    /**
     * Endpoint para agregar un nuevo producto a una sucursal específica dentro de una franquicia.
     *
     * @param franquiciaId ID de la franquicia que contiene la sucursal.
     * @param sucursalId ID de la sucursal donde se agregará el nuevo producto.
     * @param producto Objeto Producto que se desea agregar, recibido en el cuerpo de la petición.
     * @return ResponseEntity con la franquicia actualizada que contiene el nuevo producto en la sucursal.
     */
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
    /**
     * Endpoint para actualizar el nombre de una franquicia específica.
     *
     * @param id ID de la franquicia cuyo nombre se desea actualizar.
     * @param body Cuerpo de la petición que debe contener el nuevo nombre bajo la clave "nombre".
     * @return ResponseEntity con la franquicia actualizada si el ID existe, o un mensaje de error si no se encuentra.
     */
    @PutMapping("/franquicias/{id}/nombre")
    public ResponseEntity<?> actualizarNombreFranquicia(@PathVariable String id, @RequestBody Map<String, String> body) {
        String nuevoNombre = body.get("nombre");
        return franquiciaService.actualizarNombre(id, nuevoNombre);
    }
    /**
     * Endpoint para actualizar el nombre de una sucursal específica dentro de una franquicia.
     *
     * @param franquiciaId ID de la franquicia que contiene la sucursal a renombrar.
     * @param sucursalNombreAntiguo Nombre actual de la sucursal que se desea actualizar.
     * @param body Cuerpo de la petición que debe contener el nuevo nombre de la sucursal bajo la clave "nombre".
     * @return ResponseEntity con la franquicia actualizada si se encuentra la sucursal, o un mensaje de error si no.
     */
    @PutMapping("/franquicias/{franquiciaId}/sucursales/{sucursalNombreAntiguo}/nombre")
    public ResponseEntity<?> actualizarNombreSucursal(
            @PathVariable String franquiciaId,
            @PathVariable String sucursalNombreAntiguo,
            @RequestBody Map<String, String> body) {
        String nuevoNombre = body.get("nombre");
        return franquiciaService.actualizarNombreSucursal(franquiciaId, sucursalNombreAntiguo, nuevoNombre);
    }
    /**
     * Endpoint para actualizar el nombre de un producto dentro de una sucursal específica de una franquicia.
     *
     * @param franquiciaId ID de la franquicia donde se encuentra la sucursal.
     * @param sucursalNombre Nombre actual de la sucursal que contiene el producto.
     * @param productoNombreAntiguo Nombre actual del producto que se desea actualizar.
     * @param body Cuerpo de la petición que debe contener el nuevo nombre del producto bajo la clave "nombre".
     * @return ResponseEntity con la franquicia actualizada si se encuentra el producto, o un mensaje de error si no.
     */
    @PutMapping("/franquicias/{franquiciaId}/sucursales/{sucursalNombre}/productos/{productoNombreAntiguo}/nombre")
    public ResponseEntity<?> actualizarNombreProducto(
            @PathVariable String franquiciaId,
            @PathVariable String sucursalNombre,
            @PathVariable String productoNombreAntiguo,
            @RequestBody Map<String, String> body) {
        String nuevoNombre = body.get("nombre");
        return franquiciaService.actualizarNombreProducto(franquiciaId, sucursalNombre, productoNombreAntiguo, nuevoNombre);
    }
}