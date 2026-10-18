package com.ecommerce.tienda_kalza.adapter.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Producto {
    private int idProducto;
    private String nombre;
    private String descripcion;
    private BigDecimal precioOriginal;
    private BigDecimal descuento;
    private BigDecimal precioConDescuento;
    private String urlImagen;
    private int idMarca;
    private String nombreMarca;
    private List<String> nombresCategorias;
    private LocalDateTime creadoEl;
    private LocalDateTime actualizadoEl;
}