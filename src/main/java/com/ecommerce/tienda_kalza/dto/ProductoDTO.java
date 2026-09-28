package com.ecommerce.tienda_kalza.dto;

public class ProductoDTO {
    private Long id;
    private String nombre;
    private String marca;
    private String categoria;
    private String imagen;
    private double precio;
    private double precioOriginal;
    private int stock;
    private boolean agotado;
    private String descripcion;
    private String talla;

    public ProductoDTO() {}

    public ProductoDTO(Long id, String nombre, String marca, String categoria,
                       String imagen, double precio, double precioOriginal,
                       int stock, boolean agotado, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.marca = marca;
        this.categoria = categoria;
        this.imagen = imagen;
        this.precio = precio;
        this.precioOriginal = precioOriginal;
        this.stock = stock;
        this.agotado = agotado;
        this.descripcion = descripcion;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
    public double getPrecioOriginal() { return precioOriginal; }
    public void setPrecioOriginal(double precioOriginal) { this.precioOriginal = precioOriginal; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public boolean isAgotado() { return agotado; }
    public void setAgotado(boolean agotado) { this.agotado = agotado; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getTalla() { return talla; }
    public void setTalla(String talla) { this.talla = talla; }
}
