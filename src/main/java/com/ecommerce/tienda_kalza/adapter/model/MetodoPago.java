package com.ecommerce.tienda_kalza.adapter.model;

import com.ecommerce.tienda_kalza.adapter.enums.RedUsada;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString(exclude = "token")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MetodoPago {
    private int idMetodoDePago;
    private Usuario usuario;
    private String pasarela;
    private RedUsada redUsada;
    private String token;
    private String ultimos4Digitos;
    private int expiraElMes;
    private int expiraElAnio;

    @Builder.Default
    private LocalDateTime creadoEl = LocalDateTime.now();
}
