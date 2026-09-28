package com.ecommerce.tienda_kalza.dtos.publico;

import java.math.BigDecimal;

/**
 * Tarjeta de producto destacado de la portada.
 *
 * No reutiliza ProductoPublico a proposito: la portada separa la marca del
 * nombre ("Nike" sobre "Dunk Low") mientras que el catalogo y los relacionados
 * muestran el nombre completo. Son dos presentaciones del mismo producto, no
 * dos productos distintos.
 */
public record DestacadoHome(
        Long id,
        String imagen,
        String alt,
        String marca,
        String nombre,
        BigDecimal precio,
        boolean nuevo
) {
}
