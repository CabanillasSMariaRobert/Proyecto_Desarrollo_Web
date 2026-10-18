package com.ecommerce.tienda_kalza.dtos.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Formulario de inicio de sesion.
 *
 * La casilla "Recordarme" no existe en {@link LoginRequest} porque el endpoint
 * JSON no la necesita: ahi el cliente decide que hacer con el token. En la
 * pantalla si forma parte del formulario, asi que vive aca.
 */
@Getter
@Setter
@NoArgsConstructor
public class LoginForm {

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Formato de correo inválido")
    @Size(max = 255, message = "Máximo 255 caracteres")
    private String correo;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, max = 100, message = "La contraseña debe tener entre 6 y 100 caracteres")
    private String clave;

    private boolean recordar;
}
