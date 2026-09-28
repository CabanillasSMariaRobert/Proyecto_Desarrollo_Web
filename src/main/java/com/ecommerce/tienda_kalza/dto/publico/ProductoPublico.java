package com.ecommerce.tienda_kalza.dto.publico;

import java.math.BigDecimal;

/**
 * Producto como lo ve el cliente en la tienda.
 *
 * Es la fuente unica del catalogo: portada, listado, relacionados y carrito
 * leen de aqui, asi el mismo SKU nunca muestra precios distintos en dos vistas.
 *
 * precioAnterior es null cuando no hay descuento; tallas es texto ya unido
 * porque la vista solo lo imprime.
 */
public record ProductoPublico(
        Long id,
        String nombre,
        String imagen,
        String descripcion,
        BigDecimal precio,
        BigDecimal precioAnterior,
        boolean enStock,
        String tallas,
        boolean nuevo
) {
}
