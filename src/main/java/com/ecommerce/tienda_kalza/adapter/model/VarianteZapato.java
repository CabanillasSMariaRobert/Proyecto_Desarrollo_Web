package com.ecommerce.tienda_kalza.adapter.model;

import java.sql.Timestamp;

public class VarianteZapato {
    private int idVariante;
    private int idZapato;
    private int talla;
    private String color;
    private int stock;
    private Timestamp creadoEl;
    private Timestamp actualizadoEl;

    public VarianteZapato(int idVariante, int idZapato, int talla, String color, int stock, Timestamp creadoEl, Timestamp actualizadoEl) {
        this.idVariante = idVariante;
        this.idZapato = idZapato;
        this.talla = talla;
        this.color = color;
        this.stock = stock;
        this.creadoEl = creadoEl;
        this.actualizadoEl = actualizadoEl;
    }

    public VarianteZapato(int idZapato, int talla, String color, int stock, Timestamp creadoEl, Timestamp actualizadoEl) {
        this.idZapato = idZapato;
        this.talla = talla;
        this.color = color;
        this.stock = stock;
        this.creadoEl = creadoEl;
        this.actualizadoEl = actualizadoEl;
    }

    public VarianteZapato() {
    }

    public int getIdVariante() { return idVariante; }
    public void setIdVariante(int idVariante) { this.idVariante = idVariante; }

    public int getIdZapato() { return idZapato; }
    public void setIdZapato(int idZapato) { this.idZapato = idZapato; }

    public int getTalla() { return talla; }
    public void setTalla(int talla) { this.talla = talla; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public Timestamp getCreadoEl() { return creadoEl; }
    public void setCreadoEl(Timestamp creado_el) { this.creadoEl = creado_el; }

    public Timestamp getActualizadoEl() { return actualizadoEl; }
    public void setActualizadoEl(Timestamp actualizado_el) { this.actualizadoEl = actualizado_el; }
}
