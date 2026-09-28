package com.ecommerce.tienda_kalza.dtos.publico;

import java.math.BigDecimal;
import java.util.List;

/**
 * Detalle de producto.
 *
 * imagenes y tallas son colecciones porque la vista las itera; los relacionados
 * son ProductoPublico para que el precio sea el mismo que en el catalogo.
 */
public record ProductoDetalle(
        Long id,
        String nombre,
        String subtitulo,
        BigDecimal precio,
        String descripcion,
        boolean nuevo,
        String mensajeStock,
        List<ImagenProducto> imagenes,
        List<Talla> tallas,
        List<ProductoPublico> relacionados
) {
}
