package com.ecommerce.tienda_kalza.servicios;

import java.util.List;

/**
 * Recorte de paginas para los paneles de administracion.
 *
 * pagina es base 0, igual que el parametro que llega por query string.
 */
public final class Paginacion {

    private Paginacion() {
    }

    public static <T> List<T> paginar(List<T> origen, int pagina, int porPagina) {
        if (porPagina <= 0) {
            return List.of();
        }
        int desde = Math.min(pagina * porPagina, origen.size());
        int hasta = Math.min(desde + porPagina, origen.size());
        return List.copyOf(origen.subList(desde, hasta));
    }

    public static int totalPaginas(int cantidadElementos, int porPagina) {
        if (porPagina <= 0) {
            return 1;
        }
        return Math.max(1, (int) Math.ceil((double) cantidadElementos / porPagina));
    }
}
