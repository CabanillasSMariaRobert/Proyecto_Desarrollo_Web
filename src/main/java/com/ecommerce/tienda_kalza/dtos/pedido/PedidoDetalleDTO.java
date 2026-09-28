package com.ecommerce.tienda_kalza.dtos.pedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class PedidoDetalleDTO {
    private Integer idCompra;
    private String estado;
    private LocalDateTime fechaCompra;
    private BigDecimal totalOriginal;
    private BigDecimal totalDescuento;
    private BigDecimal totalConDescuento;
    private List<PedidoItemDTO> items;
    private PagoInfoDTO pago;
    private EntregaInfoDTO entrega;
    private BoletaInfoDTO boleta;
}