package com.ecommerce.tienda_kalza.adapter.model;

import java.sql.Timestamp;
import jakarta.validation.constraints.*;

public class VarianteZapato {
    private int idVariante;
    @Min(value = 1, message = "Debes seleccionar un calzado")
    private int idZapato;
    @Min(value = 1, message = "La talla debe ser al menos 1")
    private int talla;
    @NotBlank(message = "El color es obligatorio")
    @Size(max = 100, message = "El color no puede superar los 100 caracteres")
    private String color;
    @Min(value = 0, message = "El stock no puede ser negativo")
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
