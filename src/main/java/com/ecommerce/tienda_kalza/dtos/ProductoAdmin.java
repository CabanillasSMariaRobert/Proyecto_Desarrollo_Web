package com.ecommerce.tienda_kalza.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Producto tal como lo muestra el panel de administracion.
 *
 * tallas es texto ya unido porque la vista solo lo imprime; el formato
 * "42, 43, 44" es decision de presentacion, no del dato.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductoAdmin {

    private Long id;
    private String nombre;
    private String imagen;
    private String categoria;
    private String tallas;
    private BigDecimal precio;
    private Integer stock;
    private boolean activo;
}
