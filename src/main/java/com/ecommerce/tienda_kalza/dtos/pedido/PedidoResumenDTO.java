package com.ecommerce.tienda_kalza.dtos.pedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PedidoResumenDTO {
    private Integer idCompra;
    private String estado;
    private LocalDateTime fechaCompra;
    private BigDecimal total;
    private Integer totalItems;
    private String estadoEntrega;
    private LocalDateTime fechaEntregaEstimada;
}