package com.ecommerce.tienda_kalza.dtos;

/**
 * Banner tal como lo muestra el panel de publicidad.
 *
 * imagen puede venir null: un banner todavia no publicado se dibuja con un
 * placeholder en lugar de una foto.
 */
public class BannerAdmin {

    private Long id;
    private String titulo;
    private String descripcion;
    private boolean activo;
    private String imagen;

    public BannerAdmin() {
    }

    public BannerAdmin(Long id, String titulo, String descripcion, boolean activo, String imagen) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.activo = activo;
        this.imagen = imagen;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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
