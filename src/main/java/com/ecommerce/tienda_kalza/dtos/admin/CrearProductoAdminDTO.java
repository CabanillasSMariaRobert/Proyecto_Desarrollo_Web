package com.ecommerce.tienda_kalza.dtos.admin;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;

public class CrearProductoAdminDTO {
    @NotBlank @Size(max = 150) private String nombre;
    private String descripcion;
    private String urlImagen;
    @NotNull @DecimalMin("0.01") private BigDecimal precioOriginal;
    @DecimalMin("0") @DecimalMax("1") private BigDecimal descuento = BigDecimal.ZERO;
    @NotNull private Integer marcaId;
    private List<Integer> categoriasIds;
    private List<CrearVarianteAdminDTO> variantes;
    private List<Integer> materialesIds;
}