package com.ecommerce.tienda_kalza.dtos.pedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BoletaInfoDTO {
    private Integer idBoleta;
    private String numeroBoleta;
    private String cliente;
    private String rucTienda;
    private BigDecimal total;
    private LocalDateTime fechaEmision;
    private String urlPdf;
}