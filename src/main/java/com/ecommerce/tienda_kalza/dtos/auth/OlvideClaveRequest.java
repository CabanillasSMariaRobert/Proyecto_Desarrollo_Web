package com.ecommerce.tienda_kalza.dtos.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class OlvideClaveRequest {

    @NotBlank @Email @Size(max = 255) private String correo;

    public OlvideClaveRequest() {}

    public OlvideClaveRequest(String correo) {
        this.correo = correo;
    }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
}