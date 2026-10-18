package com.ecommerce.tienda_kalza.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Categoria tal como la muestra el panel de administracion.
 *
 * imagen puede venir null: en la vista, una categoria sin imagen se reemplaza
 * por un placeholder de Bootstrap Icons.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaAdmin {

    private Long id;
    private String nombre;
    private Integer cantidadProductos;
    private boolean activo;
    private String imagen;
}
