package com.ecommerce.tienda_kalza.dtos.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

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

    public RegistroForm() {}

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getClave() { return clave; }
    public void setClave(String clave) { this.clave = clave; }

    public String getConfirmarClave() { return confirmarClave; }
    public void setConfirmarClave(String confirmarClave) { this.confirmarClave = confirmarClave; }
}
