package com.ecommerce.tienda_kalza.dtos.pedido;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PagarPedidoRequestDTO {
    @NotNull private Integer idMetodoPago;
    @NotBlank private String pasarela;
    @NotBlank private String tokenPago;
}