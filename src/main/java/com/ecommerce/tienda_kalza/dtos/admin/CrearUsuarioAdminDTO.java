package com.ecommerce.tienda_kalza.dtos.admin;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CrearUsuarioAdminDTO {
    @NotBlank @Size(max = 150) private String nombres;
    @NotBlank @Size(max = 150) private String apellidos;
    @NotBlank @Email @Size(max = 255) private String correo;
    @NotBlank @Size(min = 6, max = 100) private String clave;
    private String rol = "cliente";
}