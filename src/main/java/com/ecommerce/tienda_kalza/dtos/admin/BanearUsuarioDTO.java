package com.ecommerce.tienda_kalza.dtos.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class BanearUsuarioDTO {
    @NotBlank @Size(max = 360) private String razon;
}