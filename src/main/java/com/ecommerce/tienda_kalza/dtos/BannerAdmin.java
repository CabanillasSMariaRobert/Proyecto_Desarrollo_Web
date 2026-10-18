package com.ecommerce.tienda_kalza.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Banner tal como lo muestra el panel de publicidad.
 *
 * imagen puede venir null: un banner todavia no publicado se dibuja con un
 * placeholder en lugar de una foto.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BannerAdmin {

    private Long id;
    private String titulo;
    private String descripcion;
    private boolean activo;
    private String imagen;
}
