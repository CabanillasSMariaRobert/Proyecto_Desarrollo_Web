package com.ecommerce.tienda_kalza.dto;

public class CategoriaDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private int cantidadProductos;
    private String estado;

    public CategoriaDTO() {}

    public CategoriaDTO(Long id, String nombre, String descripcion, int cantidadProductos, String estado) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.cantidadProductos = cantidadProductos;
        this.estado = estado;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public int getCantidadProductos() { return cantidadProductos; }
    public void setCantidadProductos(int cantidadProductos) { this.cantidadProductos = cantidadProductos; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
