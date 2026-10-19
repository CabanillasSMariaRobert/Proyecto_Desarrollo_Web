package com.ecommerce.tienda_kalza.adapter.model;

// Autor: Samuel

import com.ecommerce.tienda_kalza.adapter.enums.RedUsada;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Boleta {
    private int idBoleta;

    private String cliente;

    private String rucDeLaTienda;
    private String pasarelaDePago;
    private RedUsada redDeTarjeta;
    private String ultimos4Digitos;
    private BigDecimal total;
    private LocalDateTime creadoEl;

    private Compra compra;
    private MetodoPago metodoDePago;
}
