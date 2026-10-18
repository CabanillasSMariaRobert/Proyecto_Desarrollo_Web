package com.ecommerce.tienda_kalza.dtos.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Formulario de creacion de cuenta.
 *
 * No es lo mismo que {@link RegisterRequest}: ese es el contrato JSON de
 * {@code /api/auth/register} y exige nombres y apellidos por separado, mientras
 * que la pantalla pide un unico campo "Nombre completo". El corte se hace en el
 * controlador despues de validar, porque si se validara el request de API
 * faltaria el apellido y la pantalla rechazaria el formulario.
 */
@Getter
@Setter
@NoArgsConstructor
public class RegistroForm {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 301, message = "Máximo 301 caracteres")
    private String nombreCompleto;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Formato de correo inválido")
    @Size(max = 255, message = "Máximo 255 caracteres")
    private String correo;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, max = 100, message = "La contraseña debe tener entre 6 y 100 caracteres")
    private String clave;

    @NotBlank(message = "La confirmación de contraseña es obligatoria")
    private String confirmarClave;
}
