package com.ecommerce.tienda_kalza.dtos.pedido;

import java.time.LocalDateTime;

public class EntregaInfoDTO {
    private Integer idEntrega;
    private String estado;
    private String direccion;
    private Double latitud;
    private Double longitud;
    private String transportista;
    private String codigoTracking;
    private LocalDateTime fechaAsignado;
    private LocalDateTime fechaRecogido;
    private LocalDateTime fechaEntregado;
    private LocalDateTime fechaEstimada;
}