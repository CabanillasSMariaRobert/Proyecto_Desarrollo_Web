package com.ecommerce.tienda_kalza.dtos.admin;

import java.math.BigDecimal;
import java.util.List;

public class DashboardMetricasDTO {
    private BigDecimal ventasHoy;
    private BigDecimal ventasMes;
    private Long pedidosHoy;
    private Long pedidosMes;
    private Long usuariosNuevosHoy;
    private Long usuariosNuevosMes;
    private Long productosConStockBajo;
    private BigDecimal ticketPromedio;
    private List<MetricaTemporalDTO> ventasUltimos7Dias;
    private List<MetricaCategoriaDTO> ventasPorCategoria;
}