package com.ecommerce.tienda_kalza.dtos.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CrearMarcaAdminDTO {
    @NotBlank @Size(max = 150) private String nombre;
    @Size(max = 150) private String descripcion;
}