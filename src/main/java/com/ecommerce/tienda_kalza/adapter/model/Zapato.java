package com.ecommerce.tienda_kalza.adapter.model;

import java.time.LocalDate;
import jakarta.validation.constraints.Min;

public class Zapato {
    private int idZapato;
    private LocalDate creadoEl;
    private LocalDate actualizadoEl;
    @Min(value = 1, message = "Debes seleccionar un producto")
    private int idProducto;
    private boolean estaActivo;

    public Zapato(int idZapato, LocalDate creadoEl, LocalDate actualizadoEl, int idProducto){
        this.idZapato = idZapato;
        this.creadoEl = creadoEl;
        this.actualizadoEl = actualizadoEl;
        this.idProducto = idProducto;
        this.estaActivo = true;
    }

    public Zapato(LocalDate creadoEl, LocalDate actualizadoEl, int idProducto){
        this.creadoEl = creadoEl;
        this.actualizadoEl = actualizadoEl;
        this.idProducto = idProducto;
        this.estaActivo = true;
    }

    public Zapato(){
    }

    public int getIdZapato() { return idZapato; }
    public void setIdZapato(int idZapato) { this.idZapato = idZapato; }

    public LocalDate getCreadoEl() { return creadoEl; }
    public void setCreadoEl(LocalDate creadoEl) { this.creadoEl = creadoEl; }

    public LocalDate getActualizadoEl() { return actualizadoEl; }
    public void setActualizadoEl(LocalDate actualizadoEl) { this.actualizadoEl = actualizadoEl; }

    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }

    public boolean isEstaActivo() { return estaActivo; }
    public void setEstaActivo(boolean estaActivo) { this.estaActivo = estaActivo; }
}