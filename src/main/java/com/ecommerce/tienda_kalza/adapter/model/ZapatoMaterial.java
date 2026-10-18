package com.ecommerce.tienda_kalza.adapter.model;

public class ZapatoMaterial {
    private int idZapato;
    private int idMaterial;

    public ZapatoMaterial(int idZapato, int idMaterial) {
        this.idZapato = idZapato;
        this.idMaterial = idMaterial;
    }

    public ZapatoMaterial() {
    }

    public int getIdZapato() { return idZapato; }
    public void setIdZapato(int idZapato) { this.idZapato = idZapato; }

    public int getIdMaterial() { return idMaterial; }
    public void setIdMaterial(int idMaterial) { this.idMaterial = idMaterial; }
}
