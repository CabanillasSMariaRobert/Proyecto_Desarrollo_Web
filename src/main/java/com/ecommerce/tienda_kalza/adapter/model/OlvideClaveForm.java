package com.ecommerce.tienda_kalza.adapter.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class OlvideClaveForm {
    @NotBlank(message = "Ingresa tu correo electrónico.")
    @Email(message = "Ingresa un correo electrónico válido.")
    @Size(max = 255, message = "El correo no debe superar los 255 caracteres.")
    private String correo;
}
