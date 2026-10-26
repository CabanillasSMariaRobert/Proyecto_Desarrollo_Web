package com.ecommerce.tienda_kalza.adapter.model;

import com.ecommerce.tienda_kalza.adapter.enums.RedUsada;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString(exclude = {"token", "cvv"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MetodoPago {
    private Integer idMetodoDePago;
    private Usuario usuario;
    private String pasarela;
    private RedUsada redUsada;
    private String token;
    private String ultimos4Digitos;
    private Integer expiraElMes;
    private Integer expiraElAnio;

    // Campos auxiliares para la recepción del formulario web
    private String numeroTarjeta;
    private String fechaVencimiento;
    private String cvv;

    @Builder.Default
    private LocalDateTime creadoEl = LocalDateTime.now();
}