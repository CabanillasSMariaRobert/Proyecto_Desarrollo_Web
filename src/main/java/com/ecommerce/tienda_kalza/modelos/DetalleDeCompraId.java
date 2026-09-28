package com.ecommerce.tienda_kalza.modelos;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class DetalleDeCompraId implements Serializable {

    private Integer compra;
    private Integer varianteDeZapato;

    public DetalleDeCompraId() {}

    public DetalleDeCompraId(Integer compra, Integer varianteDeZapato) {
        this.compra = compra;
        this.varianteDeZapato = varianteDeZapato;
    }

    public Integer getCompra() { return compra; }
    public void setCompra(Integer compra) { this.compra = compra; }

    public Integer getVarianteDeZapato() { return varianteDeZapato; }
    public void setVarianteDeZapato(Integer varianteDeZapato) { this.varianteDeZapato = varianteDeZapato; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DetalleDeCompraId that = (DetalleDeCompraId) o;
        return Objects.equals(compra, that.compra) && Objects.equals(varianteDeZapato, that.varianteDeZapato);
    }

    @Override
    public int hashCode() {
        return Objects.hash(compra, varianteDeZapato);
    }
}