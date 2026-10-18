package com.ecommerce.tienda_kalza.dtos.publico;

/**
 * Banner de la pagina de publicidad.
 *
 * Los cuatro banners del mockup no comparten markup: dos llevan imagen con
 * overlay, uno es un bloque oscuro de texto y uno es un icono centrado. En vez
 * de partir la lista en dos, el record declara el tipo y la vista elige el
 * layout con th:switch. asi agregar un banner es agregar un item, no tocar
 * el HTML.
 */
public record BannerPromo(
        Tipo tipo,
        String imagen,
        String alt,
        String icono,
        String badge,
        String titulo,
        String descripcion,
        String enlace,
        String textoEnlace,
        String claseColumna
) {

    public enum Tipo {
        /** Imagen a pantalla completa con overlay. */
        HERO,
        /** Imagen con overlay en degradado oscuro desde abajo. */
        IMAGEN,
        /** Bloque oscuro de texto, sin imagen. */
        TEXTO,
        /** Tarjeta centrada con icono, sin imagen. */
        ICONO,
        /** Imagen con overlay claro desde la izquierda, para liquidaciones. */
        LIQUIDACION
    }
}
