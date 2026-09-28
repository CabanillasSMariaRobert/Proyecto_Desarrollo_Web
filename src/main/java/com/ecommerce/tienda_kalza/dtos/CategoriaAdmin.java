package com.ecommerce.tienda_kalza.dtos;

/**
 * Categoria tal como la muestra el panel de administracion.
 *
 * imagen puede venir null: en la vista, una categoria sin imagen se reemplaza
 * por un placeholder de Bootstrap Icons.
 */
public class CategoriaAdmin {

    private Long id;
    private String nombre;
    private Integer cantidadProductos;
    private boolean activo;
    private String imagen;

    public CategoriaAdmin() {
    }

    public CategoriaAdmin(Long id, String nombre, Integer cantidadProductos, boolean activo, String imagen) {
        this.id = id;
        this.nombre = nombre;
        this.cantidadProductos = cantidadProductos;
        this.activo = activo;
        this.imagen = imagen;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getCantidadProductos() {
        return cantidadProductos;
    }

    public void setCantidadProductos(Integer cantidadProductos) {
        this.cantidadProductos = cantidadProductos;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
