package com.ecommerce.tienda_kalza.dtos.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ResetearClaveRequest {

    @NotBlank @Size(max = 64) private String token;
    @NotBlank @Size(min = 6, max = 100) private String claveNueva;

    public ResetearClaveRequest() {}

    public ResetearClaveRequest(String token, String claveNueva) {
        this.token = token;
        this.claveNueva = claveNueva;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getClaveNueva() { return claveNueva; }
    public void setClaveNueva(String claveNueva) { this.claveNueva = claveNueva; }
}