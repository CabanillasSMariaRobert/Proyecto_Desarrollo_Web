package com.ecommerce.tienda_kalza.dtos.admin;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CrearVarianteAdminDTO {
    @NotNull @Min(1) private Integer talla;
    @NotBlank @Size(max = 100) private String color;
    @NotNull @Min(0) private Integer stock;
}