package com.ecommerce.tienda_kalza.dtos;

import java.math.BigDecimal;

/**
 * Producto tal como lo muestra el panel de administracion.
 *
 * tallas es texto ya unido porque la vista solo lo imprime; el formato
 * "42, 43, 44" es decision de presentacion, no del dato.
 */
public class ProductoAdmin {

    private Long id;
    private String nombre;
    private String imagen;
    private String categoria;
    private String tallas;
    private BigDecimal precio;
    private Integer stock;
    private boolean activo;

    public ProductoAdmin() {
    }

    public ProductoAdmin(Long id, String nombre, String imagen, String categoria,
                         String tallas, BigDecimal precio, Integer stock, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.imagen = imagen;
        this.categoria = categoria;
        this.tallas = tallas;
        this.precio = precio;
        this.stock = stock;
        this.activo = activo;
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

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getTallas() {
        return tallas;
    }

    public void setTallas(String tallas) {
        this.tallas = tallas;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
