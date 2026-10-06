package com.ecommerce.tienda_kalza.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * Todo lo que muestra la vista admin/metricas en una sola llamada.
 */
@Getter
@Setter
public class ResumenMetricas {

    private BigDecimal ventasHoy;
    private String variacionVentas;
    private int pedidosPendientes;
    private int usuariosActivos;
    private String variacionUsuarios;
    private int alertasStock;
    private List<PuntoIngreso> ingresos;
    private List<TopProducto> topProductos;
    private List<PedidoAdmin> pedidosRecientes;

    /**
     * Barra del grafico de ingresos. porcentaje es la altura relativa en el
     * rango 0-100, destacado marca la barra que se pinta de color primario.
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PuntoIngreso {

        private String etiqueta;
        private BigDecimal monto;
        private int porcentaje;
        private boolean destacado;
    }

    /**
     * Producto del ranking de mas vendidos. imagen es solo el nombre del
     * archivo; el path completo lo arma la vista.
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TopProducto {

        private String nombre;
        private String imagen;
        private int unidades;
        private BigDecimal total;
    }
}
