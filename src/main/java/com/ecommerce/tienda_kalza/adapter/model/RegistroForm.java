package com.ecommerce.tienda_kalza.adapter.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistroForm {

    @NotBlank(message = "El nombre completo es obligatorio.")
    @Size(min = 3, max = 150, message = "El nombre debe tener entre 3 y 150 caracteres.")
    private String nombreCompleto;

    @NotBlank(message = "El correo electrónico es obligatorio.")
    @Email(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "Ingresa un correo electrónico con formato válido (ej. usuario@dominio.com).")
    @Size(max = 255, message = "El correo no puede exceder los 255 caracteres.")
    private String correo;

    @NotBlank(message = "La contraseña es obligatoria.")
    @Size(min = 6, max = 50, message = "La contraseña debe tener entre 6 y 50 caracteres.")
    private String clave;

    @NotBlank(message = "Debes confirmar tu contraseña.")
    @Size(min = 6, max = 50, message = "La confirmación de contraseña debe tener entre 6 y 50 caracteres.")
    private String confirmarClave;
}