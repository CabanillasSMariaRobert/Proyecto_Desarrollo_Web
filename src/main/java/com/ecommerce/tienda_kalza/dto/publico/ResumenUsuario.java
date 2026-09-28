package com.ecommerce.tienda_kalza.dto.publico;

/** Tarjeta de resumen de la cuenta (pedidos, direcciones, favoritos). */
public record ResumenUsuario(String icono, String titulo, String detalle, String enlace, String textoEnlace) {
}
