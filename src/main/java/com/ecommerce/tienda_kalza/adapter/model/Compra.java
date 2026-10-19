package com.ecommerce.tienda_kalza.adapter.model;

// Autor: Samuel

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Compra {
    private int idCompra;

    @Builder.Default
    private String estado = "pendiente";

    private BigDecimal totalConDescuento;
    private BigDecimal totalSinDescuento;
    private LocalDateTime fechaDeCompra;

    private Usuario cliente;

    @Builder.Default
    private List<DetalleCompra> detalles = new ArrayList<>();

    private Boleta boleta;
    private Entrega entrega;
}
