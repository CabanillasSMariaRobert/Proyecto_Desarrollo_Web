package com.ecommerce.tienda_kalza.adapter.model;

public class Material {
    private int idMaterial;
    private String nombre;

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
}