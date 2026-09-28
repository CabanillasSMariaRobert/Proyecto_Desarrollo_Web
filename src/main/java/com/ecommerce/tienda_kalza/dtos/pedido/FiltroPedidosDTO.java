package com.ecommerce.tienda_kalza.dtos.pedido;

import java.time.LocalDateTime;

public class FiltroPedidosDTO {
    private String estado;
    private LocalDateTime fechaDesde;
    private LocalDateTime fechaHasta;
    private Integer page = 0;
    private Integer size = 10;
    private String orden = "fecha_desc";
}