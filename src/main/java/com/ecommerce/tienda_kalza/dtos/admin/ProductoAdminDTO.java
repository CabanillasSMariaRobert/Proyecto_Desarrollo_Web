package com.ecommerce.tienda_kalza.dtos.admin;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ProductoAdminDTO {
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
    private Integer totalVariantes;
    private Integer stockTotal;
    private Boolean activo;
    private LocalDateTime creadoEl;
}