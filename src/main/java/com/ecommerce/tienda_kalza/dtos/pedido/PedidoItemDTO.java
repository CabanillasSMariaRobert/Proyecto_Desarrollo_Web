package com.ecommerce.tienda_kalza.dtos.pedido;

import java.math.BigDecimal;

public class PedidoItemDTO {
    private Integer idVariante;
    private String nombreProducto;
    private String urlImagen;
    private Integer talla;
    private String color;
    private BigDecimal precioOriginal;
    private BigDecimal descuento;
    private BigDecimal precioConDescuento;
    private Integer cantidad;
    private BigDecimal subtotal;
}