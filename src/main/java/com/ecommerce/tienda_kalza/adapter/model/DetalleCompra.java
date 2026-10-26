package com.ecommerce.tienda_kalza.adapter.model;

// Autor: Samuel

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleCompra {
    private Compra compra;
    private VarianteZapato varianteZapato;
    private String nombreDelProducto;
    private BigDecimal precioOriginalXUnidad;

    @Builder.Default
    private BigDecimal descuentoXUnidad = BigDecimal.ZERO;

    private int cantidad;
    private BigDecimal precioSinDescuento;
    private BigDecimal precioConDescuento;
    private LocalDateTime creadoEl;
}
