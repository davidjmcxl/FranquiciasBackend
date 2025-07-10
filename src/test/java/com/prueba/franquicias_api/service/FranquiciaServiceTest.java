package com.prueba.franquicias_api.service;

import com.prueba.franquicias_api.exception.FranquiciaNotFoundException;
import com.prueba.franquicias_api.model.Franquicia;
import com.prueba.franquicias_api.model.Sucursal;
import com.prueba.franquicias_api.repository.FranquiciaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FranquiciaServiceTest {

    @Mock
    private FranquiciaRepository franquiciaRepository;

    @InjectMocks
    private FranquiciaService franquiciaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void crearFranquicia_conNombreValido_devuelveFranquiciaGuardada() {
        Franquicia franquicia = new Franquicia();
        franquicia.setNombre("Mi Franquicia");

        when(franquiciaRepository.save(any())).thenReturn(franquicia);

        Franquicia result = franquiciaService.crearFranquicia(franquicia);

        assertEquals("Mi Franquicia", result.getNombre());
        verify(franquiciaRepository).save(franquicia);
    }

    @Test
    void crearFranquicia_conNombreVacio_lanzaExcepcion() {
        Franquicia franquicia = new Franquicia();
        franquicia.setNombre("");

        assertThrows(IllegalArgumentException.class, () -> {
            franquiciaService.crearFranquicia(franquicia);
        });
    }

    @Test
    void listarFranquicias_retornaListaDesdeRepositorio() {
        List<Franquicia> lista = Arrays.asList(new Franquicia(), new Franquicia());
        when(franquiciaRepository.findAll()).thenReturn(lista);

        List<Franquicia> resultado = franquiciaService.listarFranquicias();

        assertEquals(2, resultado.size());
    }

    @Test
    void agregarSucursal_conFranquiciaExistente_agregaCorrectamente() {
        String franquiciaId = "123";
        Franquicia franquicia = new Franquicia();
        franquicia.setId(franquiciaId);
        franquicia.setSucursales(new ArrayList<>());

        Sucursal sucursal = new Sucursal();
        sucursal.setNombre("Sucursal Nueva");

        when(franquiciaRepository.findById(franquiciaId)).thenReturn(Optional.of(franquicia));
        when(franquiciaRepository.save(any())).thenReturn(franquicia);

        Franquicia result = franquiciaService.agregarSucursal(franquiciaId, sucursal);

        assertEquals(1, result.getSucursales().size());
        assertEquals("Sucursal Nueva", result.getSucursales().get(0).getNombre());
    }

    @Test
    void agregarSucursal_conNombreVacio_lanzaExcepcion() {
        Sucursal sucursal = new Sucursal();
        sucursal.setNombre("");

        assertThrows(IllegalArgumentException.class, () ->
                franquiciaService.agregarSucursal("1", sucursal));
    }

    @Test
    void agregarSucursal_franquiciaNoEncontrada_lanzaExcepcion() {
        when(franquiciaRepository.findById("X")).thenReturn(Optional.empty());

        Sucursal sucursal = new Sucursal();
        sucursal.setNombre("Sucursal");

        assertThrows(FranquiciaNotFoundException.class, () ->
                franquiciaService.agregarSucursal("X", sucursal));
    }

    @Test
    void actualizarNombre_conIdExistente_actualizaNombre() {
        Franquicia f = new Franquicia();
        f.setId("1");
        f.setNombre("Antiguo");

        when(franquiciaRepository.findById("1")).thenReturn(Optional.of(f));
        when(franquiciaRepository.save(any())).thenReturn(f);

        var response = franquiciaService.actualizarNombre("1", "Nuevo");

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Nuevo", ((Franquicia) response.getBody()).getNombre());
    }

    @Test
    void actualizarNombre_conIdInexistente_retorna404() {
        when(franquiciaRepository.findById("X")).thenReturn(Optional.empty());

        var response = franquiciaService.actualizarNombre("X", "Nuevo");

        assertEquals(404, response.getStatusCode().value());
        assertEquals("Franquicia no encontrada", response.getBody());
    }
}
