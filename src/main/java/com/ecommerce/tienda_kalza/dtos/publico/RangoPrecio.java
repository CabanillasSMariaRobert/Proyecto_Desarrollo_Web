package com.ecommerce.tienda_kalza.dtos.publico;

import java.math.BigDecimal;

/** Extremos del slider de precio del catalogo. */
public record RangoPrecio(BigDecimal minimo, BigDecimal maximo) {
}
