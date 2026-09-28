package com.ecommerce.tienda_kalza.dtos.carrito;

import java.math.BigDecimal;

public class CarritoItemDTO {
    private Integer idVariante;
    private Integer idProducto;
    private String nombreProducto;
    private String urlImagen;
    private Integer talla;
    private String color;
    private BigDecimal precioOriginal;
    private BigDecimal descuento;
    private BigDecimal precioConDescuento;
    private Integer cantidad;
    private Integer stockDisponible;
    private Boolean hayStockSuficiente;
    private BigDecimal subtotal;
}