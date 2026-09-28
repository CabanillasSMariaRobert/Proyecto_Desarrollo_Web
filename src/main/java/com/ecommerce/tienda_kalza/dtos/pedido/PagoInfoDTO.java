package com.ecommerce.tienda_kalza.dtos.pedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PagoInfoDTO {
    private String metodoPago;
    private String ultimos4Digitos;
    private String pasarela;
    private BigDecimal monto;
    private LocalDateTime fechaPago;
    private String estado;
    private String transaccionId;
}