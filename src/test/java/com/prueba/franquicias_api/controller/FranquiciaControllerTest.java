package com.prueba.franquicias_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prueba.franquicias_api.model.Franquicia;
import com.prueba.franquicias_api.model.Sucursal;
import com.prueba.franquicias_api.model.Producto;
import com.prueba.franquicias_api.service.FranquiciaService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FranquiciaController.class)
class FranquiciaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FranquiciaService franquiciaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void crearFranquicia_devuelveFranquiciaCreada() throws Exception {
        Franquicia franquicia = new Franquicia();
        franquicia.setId("1");
        franquicia.setNombre("Franquicia Test");

        when(franquiciaService.crearFranquicia(Mockito.any())).thenReturn(franquicia);

        mockMvc.perform(post("/api/franquicias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(franquicia)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.nombre").value("Franquicia Test"));
    }

    @Test
    void listarFranquicias_retornaLista() throws Exception {
        Franquicia f1 = new Franquicia();
        f1.setId("1");
        f1.setNombre("Franquicia 1");

        Franquicia f2 = new Franquicia();
        f2.setId("2");
        f2.setNombre("Franquicia 2");

        when(franquiciaService.listarFranquicias()).thenReturn(Arrays.asList(f1, f2));

        mockMvc.perform(get("/api/franquicias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].nombre").value("Franquicia 1"))
                .andExpect(jsonPath("$[1].nombre").value("Franquicia 2"));
    }

    @Test
    void agregarSucursal_devuelveFranquiciaActualizada() throws Exception {
        Sucursal sucursal = new Sucursal();
        sucursal.setNombre("Sucursal Nueva");

        Franquicia franquicia = new Franquicia();
        franquicia.setId("1");
        franquicia.setNombre("Franquicia");
        franquicia.setSucursales(List.of(sucursal));

        when(franquiciaService.agregarSucursal(Mockito.eq("1"), Mockito.any()))
                .thenReturn(franquicia);

        mockMvc.perform(post("/api/franquicias/1/sucursales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sucursal)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sucursales[0].nombre").value("Sucursal Nueva"));
    }

    @Test
    void actualizarStockProducto() throws Exception {
        Franquicia franquicia = new Franquicia();
        franquicia.setId("1");

        Map<String, Integer> stockBody = new HashMap<>();
        stockBody.put("stock", 20);

        when(franquiciaService.actualizarStock("1", "2", "3", 20)).thenReturn(franquicia);

        mockMvc.perform(patch("/api/franquicias/1/sucursales/2/productos/3/stock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(stockBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"));
    }

    @Test
    void eliminarProducto_deberiaRetornarFranquiciaActualizada() throws Exception {
        Franquicia franquicia = new Franquicia();
        franquicia.setId("1");

        when(franquiciaService.eliminarProducto("1", "2", "3")).thenReturn(franquicia);

        mockMvc.perform(delete("/api/franquicias/1/sucursales/2/productos/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"));
    }
}
