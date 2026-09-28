package com.ecommerce.tienda_kalza.dtos.catalogo;

import java.math.BigDecimal;
import java.util.List;

public class ProductoDetalleDTO {
    private Integer idProducto;
    private String nombre;
    private String descripcion;
    private String urlImagen;
    private BigDecimal precioOriginal;
    private BigDecimal descuento;
    private BigDecimal precioConDescuento;
    private String marcaNombre;
    private Integer marcaId;
    private List<String> categorias;
    private List<VarianteDTO> variantes;
    private List<MaterialDTO> materiales;
}