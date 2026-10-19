package com.ecommerce.tienda_kalza.adapter.model;

public class Categoria {
    private Integer idCategoria;
    private String nombre;
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