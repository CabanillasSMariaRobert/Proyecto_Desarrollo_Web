package com.ecommerce.tienda_kalza.dtos.carrito;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CarritoItemRequestDTO {
    @NotNull(message = "ID de variante es obligatorio")
    private Integer idVariante;

    @NotNull @Min(1)
    private Integer cantidad;
}