package com.ecommerce.tienda_kalza.dto;

public class CarritoItemDTO {
    private Long id;
    private String nombre;
    private String imagen;
    private String talla;
    private String color;
    private int cantidad;
    private double precio;
    private double subtotal;

    public CarritoItemDTO() {}

    public CarritoItemDTO(Long id, String nombre, String imagen, String talla,
                          String color, int cantidad, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.imagen = imagen;
        this.talla = talla;
        this.color = color;
        this.cantidad = cantidad;
        this.precio = precio;
        this.subtotal = precio * cantidad;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }
    public String getTalla() { return talla; }
    public void setTalla(String talla) { this.talla = talla; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
}
