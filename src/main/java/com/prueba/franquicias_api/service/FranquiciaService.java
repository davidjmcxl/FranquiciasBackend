package com.prueba.franquicias_api.service;

import com.prueba.franquicias_api.dto.ProductoMaxStockDTO;
import com.prueba.franquicias_api.exception.FranquiciaNotFoundException;
import com.prueba.franquicias_api.exception.SucursalNotFoundException;
import com.prueba.franquicias_api.model.Franquicia;
import com.prueba.franquicias_api.model.Producto;
import com.prueba.franquicias_api.model.Sucursal;
import com.prueba.franquicias_api.repository.FranquiciaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class FranquiciaService {

    private final FranquiciaRepository franquiciaRepository;

    public FranquiciaService(FranquiciaRepository franquiciaRepository) {
        this.franquiciaRepository = franquiciaRepository;
    }

    /**
     * Crea y persiste una nueva franquicia.
     *
     * @param franquicia Objeto Franquicia a guardar; debe tener nombre no vacío.
     * @return La franquicia recién guardada, con ID asignado.
     * @throws IllegalArgumentException si el nombre es nulo o vacío.
     */
    public Franquicia crearFranquicia(Franquicia franquicia) {
        if (franquicia == null || !StringUtils.hasText(franquicia.getNombre())) {
            throw new IllegalArgumentException("El nombre de la franquicia no puede estar vacío");
        }
        return franquiciaRepository.save(franquicia);
    }

    /**
     * Recupera todas las franquicias existentes.
     *
     * @return Lista de franquicias (vacía si no hay ninguna).
     */
    public List<Franquicia> listarFranquicias() {
        return franquiciaRepository.findAll();
    }

    /**
     * Añade una nueva sucursal a la franquicia indicada.
     *
     * @param franquiciaId   ID de la franquicia donde se agrega la sucursal.
     * @param nuevaSucursal  Objeto Sucursal a agregar; debe tener nombre no vacío.
     * @return La franquicia actualizada con la nueva sucursal incluida.
     * @throws FranquiciaNotFoundException si no existe la franquicia.
     * @throws IllegalArgumentException   si el nombre de la sucursal es nulo o vacío.
     */
    public Franquicia agregarSucursal(String franquiciaId, Sucursal nuevaSucursal) {
        if (nuevaSucursal == null || !StringUtils.hasText(nuevaSucursal.getNombre())) {
            throw new IllegalArgumentException("El nombre de la sucursal no puede estar vacío");
        }
        Franquicia franquicia = franquiciaRepository.findById(franquiciaId)
                .orElseThrow(() -> new FranquiciaNotFoundException(franquiciaId));

        franquicia.getSucursales().add(nuevaSucursal);
        return franquiciaRepository.save(franquicia);
    }

    /**
     * Añade un nuevo producto a la sucursal de una franquicia.
     *
     * @param franquiciaId ID de la franquicia.
     * @param sucursalId   ID de la sucursal dentro de esa franquicia.
     * @param nuevoProducto Objeto Producto a agregar; debe tener nombre no vacío y stock ≥ 0.
     * @return La franquicia actualizada con el producto añadido.
     * @throws FranquiciaNotFoundException si no existe la franquicia.
     * @throws SucursalNotFoundException   si no existe la sucursal dentro de la franquicia.
     * @throws IllegalArgumentException    si el nombre del producto es nulo/vacío o stock < 0.
     */
    public Franquicia agregarProducto(String franquiciaId, String sucursalId, Producto nuevoProducto) {
        if (nuevoProducto == null
                || !StringUtils.hasText(nuevoProducto.getNombre())
                || nuevoProducto.getStock() < 0) {
            throw new IllegalArgumentException("Producto inválido: nombre no vacío y stock ≥ 0");
        }
        Franquicia franquicia = franquiciaRepository.findById(franquiciaId)
                .orElseThrow(() -> new FranquiciaNotFoundException(franquiciaId));

        Sucursal sucursal = franquicia.getSucursales().stream()
                .filter(s -> s.getId().equals(sucursalId))
                .findFirst()
                .orElseThrow(() -> new SucursalNotFoundException(sucursalId));

        sucursal.getProductos().add(nuevoProducto);
        return franquiciaRepository.save(franquicia);
    }

    /**
     * Elimina un producto de una sucursal dentro de una franquicia.
     *
     * @param franquiciaId ID de la franquicia.
     * @param sucursalId   ID de la sucursal.
     * @param productoId   ID del producto a eliminar.
     * @return La franquicia actualizada sin el producto.
     * @throws FranquiciaNotFoundException si no existe la franquicia.
     * @throws SucursalNotFoundException   si no existe la sucursal.
     * @throws IllegalArgumentException    si el producto no existe en la sucursal.
     */
    public Franquicia eliminarProducto(String franquiciaId,
                                       String sucursalId,
                                       String productoId) {
        Franquicia franquicia = franquiciaRepository.findById(franquiciaId)
                .orElseThrow(() -> new FranquiciaNotFoundException(franquiciaId));

        Sucursal sucursal = franquicia.getSucursales().stream()
                .filter(s -> s.getId().equals(sucursalId))
                .findFirst()
                .orElseThrow(() -> new SucursalNotFoundException(sucursalId));

        boolean removed = sucursal.getProductos().removeIf(p -> p.getId().equals(productoId));
        if (!removed) {
            throw new IllegalArgumentException("Producto no encontrado con id: " + productoId);
        }

        return franquiciaRepository.save(franquicia);
    }
    /**
     * Actualiza el stock de un producto en una sucursal de una franquicia.
     *
     * @param franquiciaId ID de la franquicia.
     * @param sucursalId   ID de la sucursal.
     * @param productoId   ID del producto a actualizar.
     * @param nuevoStock   Nuevo valor de stock (debe ser ≥ 0).
     * @return La franquicia actualizada con el stock modificado.
     * @throws FranquiciaNotFoundException si no existe la franquicia.
     * @throws SucursalNotFoundException   si no existe la sucursal.
     * @throws IllegalArgumentException    si el producto no existe o el stock es negativo.
     */
    public Franquicia actualizarStock(String franquiciaId,
                                      String sucursalId,
                                      String productoId,
                                      int nuevoStock) {
        if (nuevoStock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }

        Franquicia franquicia = franquiciaRepository.findById(franquiciaId)
                .orElseThrow(() -> new FranquiciaNotFoundException(franquiciaId));

        Sucursal sucursal = franquicia.getSucursales().stream()
                .filter(s -> s.getId().equals(sucursalId))
                .findFirst()
                .orElseThrow(() -> new SucursalNotFoundException(sucursalId));

        Producto producto = sucursal.getProductos().stream()
                .filter(p -> p.getId().equals(productoId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con id: " + productoId));

        producto.setStock(nuevoStock);
        return franquiciaRepository.save(franquicia);
    }
    /**
     * Para cada sucursal de la franquicia indicada, encuentra el producto con más stock.
     *
     * @param franquiciaId ID de la franquicia.
     * @return Lista de DTOs con sucursal y su producto de mayor stock.
     * @throws FranquiciaNotFoundException si no existe la franquicia.
     */
    public List<ProductoMaxStockDTO> listarProductoMaxStockPorSucursal(String franquiciaId) {
        Franquicia franquicia = franquiciaRepository.findById(franquiciaId)
                .orElseThrow(() -> new FranquiciaNotFoundException(franquiciaId));

        return franquicia.getSucursales().stream()
                .map(sucursal -> {
                    return sucursal.getProductos().stream()
                            .max(Comparator.comparingInt(p -> p.getStock()))
                            .map(p -> new ProductoMaxStockDTO(
                                    sucursal.getId(),
                                    sucursal.getNombre(),
                                    p.getId(),
                                    p.getNombre(),
                                    p.getStock()))
                            // Si no hay productos, lo omitimos
                            .orElse(null);
                })
                .filter(Objects::nonNull)
                .toList();
    }
/**
 * Actualiza el nombre de una franquicia existente.
 *
 * @param id ID de la franquicia a actualizar.
 * @param nuevoNombre Nuevo nombre que se asignará a la franquicia.
 * @return ResponseEntity con la franquicia actualizada si existe,
 *         o un mensaje de error con estado 404 si no se encuentra la franquicia.
 */
    public ResponseEntity<?> actualizarNombre(String id, String nuevoNombre) {
        Optional<Franquicia> optional = franquiciaRepository.findById(id);
        if (optional.isPresent()) {
            Franquicia franquicia = optional.get();
            franquicia.setNombre(nuevoNombre);
            franquiciaRepository.save(franquicia);
            return ResponseEntity.ok(franquicia);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Franquicia no encontrada");
        }
    }
    /**
     * Actualiza el nombre de una sucursal perteneciente a una franquicia específica.
     *
     * @param franquiciaId ID de la franquicia a la cual pertenece la sucursal.
     * @param nombreAntiguo Nombre actual de la sucursal que se desea actualizar.
     * @param nuevoNombre Nuevo nombre que se asignará a la sucursal.
     * @return ResponseEntity con la franquicia actualizada si la sucursal fue encontrada,
     *         o un mensaje de error con estado 404 si no se encuentra la franquicia o la sucursal
     */
    public ResponseEntity<?> actualizarNombreSucursal(String franquiciaId, String nombreAntiguo, String nuevoNombre) {
        Optional<Franquicia> optional = franquiciaRepository.findById(franquiciaId);
        if (optional.isPresent()) {
            Franquicia franquicia = optional.get();
            List<Sucursal> sucursales = franquicia.getSucursales();
            for (Sucursal sucursal : sucursales) {
                if (sucursal.getNombre().equalsIgnoreCase(nombreAntiguo)) {
                    sucursal.setNombre(nuevoNombre);
                    franquiciaRepository.save(franquicia);
                    return ResponseEntity.ok(franquicia);
                }
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sucursal no encontrada");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Franquicia no encontrada");
    }
    /**
     * Actualiza el nombre de un producto dentro de una sucursal específica de una franquicia.
     *
     * @param franquiciaId ID de la franquicia donde se encuentra la sucursal.
     * @param sucursalNombre Nombre de la sucursal que contiene el producto.
     * @param productoAntiguo Nombre actual del producto que se desea actualizar.
     * @param nuevoNombre Nuevo nombre que se asignará al producto.
     * @return ResponseEntity con la franquicia actualizada si el producto fue encontrado y actualizado,
     *         o un mensaje de error con estado 404 si no se encuentra la franquicia, la sucursal o el producto.
     */
    public ResponseEntity<?> actualizarNombreProducto(String franquiciaId, String sucursalNombre, String productoAntiguo, String nuevoNombre) {
        Optional<Franquicia> optional = franquiciaRepository.findById(franquiciaId);
        if (optional.isPresent()) {
            Franquicia franquicia = optional.get();
            for (Sucursal sucursal : franquicia.getSucursales()) {
                if (sucursal.getNombre().equalsIgnoreCase(sucursalNombre)) {
                    for (Producto producto : sucursal.getProductos()) {
                        if (producto.getNombre().equalsIgnoreCase(productoAntiguo)) {
                            producto.setNombre(nuevoNombre);
                            franquiciaRepository.save(franquicia);
                            return ResponseEntity.ok(franquicia);
                        }
                    }
                }
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto no encontrado");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Franquicia no encontrada");
    }

}
