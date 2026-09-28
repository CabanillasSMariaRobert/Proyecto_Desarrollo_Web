package com.ecommerce.tienda_kalza.dto.publico;

/** Boton de talla del filtro o del selector. Sin tallas disponibles, habilitada=false. */
public record Talla(String etiqueta, boolean seleccionada, boolean habilitada) {
}
