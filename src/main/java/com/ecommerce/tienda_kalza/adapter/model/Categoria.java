package com.ecommerce.tienda_kalza.adapter.model;

import jakarta.validation.constraints.*;

public class Categoria {
    private Integer idCategoria;
    @NotBlank(message = "El nombre de la categoría es obligatorio.")
    @Size(max = 150, message = "El nombre admite hasta 150 caracteres.")
    private String nombre;
    @Size(max = 500, message = "La descripción admite hasta 500 caracteres.")
    private String descripcion;
    private boolean estaActivo;

    public Categoria() {
    }

    public Categoria(Integer idCategoria, String nombre, String descripcion, boolean estaActivo) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.estaActivo = estaActivo;
    }

    public Categoria(String nombre, String descripcion, boolean estaActivo) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.estaActivo = estaActivo;
    }

    public Integer getIdCategoria() { return idCategoria; }
    public void setIdCategoria(Integer idCategoria) { this.idCategoria = idCategoria; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public boolean isEstaActivo() { return estaActivo; }

    public void setEstaActivo(boolean estaActivo) { this.estaActivo = estaActivo; }

    private Integer cantidadProductos;
    public void setCantidadProductos(Integer cantidadProductos) { this.cantidadProductos = cantidadProductos; }
    public Integer getCantidadProductos() { return cantidadProductos; }
}
