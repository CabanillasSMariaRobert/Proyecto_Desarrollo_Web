package com.ecommerce.tienda_kalza.dtos;

import java.math.BigDecimal;

public class ItemPedido {

    private String nombre;
    private int cantidad;
    private BigDecimal subtotal;

    public ItemPedido() {
    }

    public ItemPedido(String nombre, int cantidad, BigDecimal subtotal) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
}
