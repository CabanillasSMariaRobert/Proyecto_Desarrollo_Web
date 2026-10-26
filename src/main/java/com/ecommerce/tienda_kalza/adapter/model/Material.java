package com.ecommerce.tienda_kalza.adapter.model;

import jakarta.validation.constraints.*;

public class Material {
    private int idMaterial;
    @NotBlank(message = "El nombre del material es obligatorio")
    @Size(max = 150, message = "El nombre no puede superar los 150 caracteres")
    private String nombre;
    private boolean estaActivo = true;

    public Material(int idMaterial, String nombre) {
        this.idMaterial = idMaterial;
        this.nombre = nombre;
    }

    public Material(String nombre) {
        this.nombre = nombre;
    }

    public Material() {
    }

    public int getIdMaterial() { return idMaterial; }
    public void setIdMaterial(int idMaterial) { this.idMaterial = idMaterial; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public boolean isEstaActivo() { return estaActivo; }
    public void setEstaActivo(boolean estaActivo) { this.estaActivo = estaActivo; }
}