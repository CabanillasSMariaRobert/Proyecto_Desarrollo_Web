package com.ecommerce.tienda_kalza.dtos.publico;

import java.math.BigDecimal;

/**
 * Linea del carrito.
 *
 * precioUnitario es el precio de una unidad; el subtotal de la linea sale de
 * multiplicarlo por cantidad en la vista.
 */
public record ItemCarrito(
        Long id,
        String nombre,
        String imagen,
        String talla,
        String color,
        BigDecimal precioUnitario,
        int cantidad
) {
}
