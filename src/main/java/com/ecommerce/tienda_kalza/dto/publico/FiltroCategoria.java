package com.ecommerce.tienda_kalza.dto.publico;

/** Checkbox de categoria del catalogo. El id alimenta el for del label. */
public record FiltroCategoria(String id, String nombre, boolean seleccionada) {
}
