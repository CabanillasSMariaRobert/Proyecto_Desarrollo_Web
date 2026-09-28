package com.ecommerce.tienda_kalza.dtos.catalogo;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;

public class FiltroCatalogoDTO {
    @Min(0) private Integer page = 0;
    @Min(1) @Max(50) private Integer size = 12;
    private Integer categoriaId;
    private Integer marcaId;
    @DecimalMin("0") private BigDecimal precioMin;
    @DecimalMin("0") private BigDecimal precioMax;
    private String orden = "nuevo";
    private String tallas;
    private String colores;
    private Boolean soloConStock = true;
}