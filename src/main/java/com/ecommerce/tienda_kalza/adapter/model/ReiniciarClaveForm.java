package com.ecommerce.tienda_kalza.adapter.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ReiniciarClaveForm {
    @NotBlank(message = "Necesitas el token enviado a tu correo para reiniciar tu contraseña.")
    @Size(max = 128, message = "El token no es válido.")
    private String token;

    @NotBlank(message = "Ingresa tu nueva contraseña.")
    @Size(min = 6, max = 72, message = "La contraseña debe tener entre 6 y 72 caracteres.")
    private String clave;

    @NotBlank(message = "Confirma tu nueva contraseña.")
    @Size(max = 72, message = "La confirmación no debe superar los 72 caracteres.")
    private String confirmarClave;
}
