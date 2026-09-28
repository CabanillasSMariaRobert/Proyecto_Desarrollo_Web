package com.ecommerce.tienda_kalza.dtos.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CambiarClaveRequest {

    @NotBlank @Size(min = 6, max = 100) private String claveActual;
    @NotBlank @Size(min = 6, max = 100) private String claveNueva;

    public CambiarClaveRequest() {}

    public CambiarClaveRequest(String claveActual, String claveNueva) {
        this.claveActual = claveActual;
        this.claveNueva = claveNueva;
    }

    public String getClaveActual() { return claveActual; }
    public void setClaveActual(String claveActual) { this.claveActual = claveActual; }

    public String getClaveNueva() { return claveNueva; }
    public void setClaveNueva(String claveNueva) { this.claveNueva = claveNueva; }
}