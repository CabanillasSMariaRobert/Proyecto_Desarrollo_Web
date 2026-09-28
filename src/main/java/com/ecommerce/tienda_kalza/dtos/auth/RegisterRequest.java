package com.ecommerce.tienda_kalza.dtos.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

    @NotBlank(message = "Los nombres son obligatorios")
    @Size(max = 150, message = "Máximo 150 caracteres")
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(max = 150, message = "Máximo 150 caracteres")
    private String apellidos;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Formato de correo inválido")
    @Size(max = 255)
    private String correo;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, max = 100, message = "La contraseña debe tener entre 6 y 100 caracteres")
    private String clave;

    @NotBlank(message = "La confirmación de contraseña es obligatoria")
    private String confirmarClave;

    public RegisterRequest() {}

    public RegisterRequest(String nombres, String apellidos, String correo, String clave, String confirmarClave) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.correo = correo;
        this.clave = clave;
        this.confirmarClave = confirmarClave;
    }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getClave() { return clave; }
    public void setClave(String clave) { this.clave = clave; }

    public String getConfirmarClave() { return confirmarClave; }
    public void setConfirmarClave(String confirmarClave) { this.confirmarClave = confirmarClave; }
}