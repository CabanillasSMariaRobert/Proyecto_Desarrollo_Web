package com.ecommerce.tienda_kalza.dtos.catalogo;

import java.math.BigDecimal;

public class ProductoResumenDTO {
    private Integer idProducto;
    private String nombre;
    private BigDecimal precioOriginal;
    private BigDecimal descuento;
    private BigDecimal precioConDescuento;
    private String urlImagen;
    private String marcaNombre;
    private Boolean hayStock;
    private Integer variantesDisponibles;
}