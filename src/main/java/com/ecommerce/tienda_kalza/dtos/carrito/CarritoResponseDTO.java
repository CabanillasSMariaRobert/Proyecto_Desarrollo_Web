package com.ecommerce.tienda_kalza.dtos.carrito;

import java.math.BigDecimal;
import java.util.List;

public class CarritoResponseDTO {
    private Integer idCompra;
    private List<CarritoItemDTO> items;
    private BigDecimal totalOriginal;
    private BigDecimal totalDescuento;
    private BigDecimal totalConDescuento;
    private Integer totalItems;
    private Boolean validoParaCheckout;
}