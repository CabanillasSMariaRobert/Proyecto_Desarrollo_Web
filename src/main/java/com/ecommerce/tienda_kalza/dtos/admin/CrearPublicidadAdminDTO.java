package com.ecommerce.tienda_kalza.dtos.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CrearPublicidadAdminDTO {
    @NotBlank private String urlBanner;
    @NotBlank @Size(max = 250) private String titulo;
    private String descripcion;
    private String enlace;
    private Boolean estaActivo = true;
}