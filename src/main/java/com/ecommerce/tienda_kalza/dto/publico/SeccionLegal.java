package com.ecommerce.tienda_kalza.dto.publico;

/**
 * Parrafo de las secciones de privacidad y terminos.
 *
 * Las dos secciones comparten estructura exacta, asi que el mismo record las
 * cubre y el texto sale del service.
 */
public record SeccionLegal(String titulo, String descripcion) {
}
