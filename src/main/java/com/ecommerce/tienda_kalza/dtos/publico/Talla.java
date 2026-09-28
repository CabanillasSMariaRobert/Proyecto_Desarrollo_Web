package com.ecommerce.tienda_kalza.dtos.publico;

/** Boton de talla del filtro o del selector. Sin tallas disponibles, habilitada=false. */
public record Talla(String etiqueta, boolean seleccionada, boolean habilitada) {
}
