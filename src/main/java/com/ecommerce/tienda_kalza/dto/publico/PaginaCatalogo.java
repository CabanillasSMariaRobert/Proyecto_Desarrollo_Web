package com.ecommerce.tienda_kalza.dto.publico;

/**
 * Item de la paginacion del catalogo.
 *
 * Un solo record cubre los tres casos porque los tres se dibujan en el mismo
 * <li>: el separador "...", las flechas deshabilitadas y los numeros.
 */
public record PaginaCatalogo(
        String etiqueta,
        int numero,
        boolean activa,
        boolean habilitada,
        boolean esSeparador
) {
    /** Numero de pagina navegable. */
    public static PaginaCatalogo pagina(int numero, boolean activa, boolean habilitada) {
        return new PaginaCatalogo(String.valueOf(numero), numero, activa, habilitada, false);
    }

    /** Separador "..." sin destino. */
    public static PaginaCatalogo separador() {
        return new PaginaCatalogo("...", 0, false, false, true);
    }
}
