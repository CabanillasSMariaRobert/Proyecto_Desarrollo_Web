package com.ecommerce.tienda_kalza.dtos.publico;

import java.math.BigDecimal;

/**
 * Pedido del cliente, en la lista de pedidos y en los recientes del perfil.
 *
 * claseEstado lleva la clase del badge porque cada estado se pinza distinto
 * (enviado en gris, entregado en oscuro, cancelado tachado).
 */
public record PedidoCliente(
        String codigo,
        String fecha,
        String imagen,
        String nombre,
        String resumenProductos,
        String estado,
        String claseEstado,
        BigDecimal total,
        String accion,
        String textoAccion
) {
}
