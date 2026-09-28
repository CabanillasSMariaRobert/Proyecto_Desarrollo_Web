package com.ecommerce.tienda_kalza.dtos.pedido;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CambiarEstadoPedidoDTO {
    @NotNull @NotBlank private String nuevoEstado;
    private String observacion;
}