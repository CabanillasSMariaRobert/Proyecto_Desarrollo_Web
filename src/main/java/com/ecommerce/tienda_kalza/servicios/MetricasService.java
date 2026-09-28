package com.ecommerce.tienda_kalza.servicios;

import com.ecommerce.tienda_kalza.dto.EstadoPedido;
import com.ecommerce.tienda_kalza.dto.ResumenMetricas;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Datos de prueba para el panel de metricas.
 *
 * Los KPIs, el ranking y la serie de ingresos hoy son fijos. Cuando exista el
 * repositorio, van a salir de consultas agregadas sobre Pedido y Producto.
 */
@Service
public class MetricasService {

    private final PedidoAdminService pedidoAdminService;

    public MetricasService(PedidoAdminService pedidoAdminService) {
        this.pedidoAdminService = pedidoAdminService;
    }

    public ResumenMetricas obtenerResumen() {
        ResumenMetricas resumen = new ResumenMetricas();

        resumen.setVentasHoy(new BigDecimal("12450.00"));
        resumen.setVariacionVentas("+14.5% vs ayer");
        resumen.setPedidosPendientes(
                pedidoAdminService.contarPorEstado(EstadoPedido.PENDIENTE)
                        + pedidoAdminService.contarPorEstado(EstadoPedido.PROCESANDO));
        resumen.setUsuariosActivos(1204);
        resumen.setVariacionUsuarios("+5.2% esta semana");
        resumen.setAlertasStock(12);
        resumen.setIngresos(serieIngresos());
        resumen.setTopProductos(List.of(
                new ResumenMetricas.TopProducto(
                        "AeroGlide Pro Runner", "AeroGlide Pro Runner.webp", 450, new BigDecimal("45000.00")),
                new ResumenMetricas.TopProducto(
                        "Court Classic Low", "Court Classic Low.webp", 320, new BigDecimal("31680.00")),
                new ResumenMetricas.TopProducto(
                        "Velocity Pro X", "Velocity Pro X.webp", 280, new BigDecimal("36400.00"))
        ));
        resumen.setPedidosRecientes(pedidoAdminService.obtenerTodos().stream().limit(3).toList());

        return resumen;
    }

    /**
     * Siete dias de ingresos. La altura de cada barra es el porcentaje
     * relativo al maximo de la semana, que es como se ve en el diseno.
     */
    private List<ResumenMetricas.PuntoIngreso> serieIngresos() {
        List<ResumenMetricas.PuntoIngreso> serie = List.of(
                punto("Lun", "8200.00"),
                punto("Mar", "10450.00"),
                punto("Mie", "7600.00"),
                punto("Jue", "11800.00"),
                punto("Vie", "9600.00"),
                punto("Sab", "17400.00"),
                punto("Dom", "13100.00")
        );

        BigDecimal maximo = serie.stream()
                .map(ResumenMetricas.PuntoIngreso::getMonto)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ONE);

        BigDecimal mayorDelDia = BigDecimal.ZERO;
        String etiquetaMayor = null;
        for (ResumenMetricas.PuntoIngreso punto : serie) {
            int porcentaje = punto.getMonto()
                    .multiply(new BigDecimal("100"))
                    .divide(maximo, 0, RoundingMode.HALF_UP)
                    .intValue();
            punto.setPorcentaje(porcentaje);
            if (punto.getMonto().compareTo(mayorDelDia) > 0) {
                mayorDelDia = punto.getMonto();
                etiquetaMayor = punto.getEtiqueta();
            }
        }
        String destacada = etiquetaMayor;
        for (ResumenMetricas.PuntoIngreso punto : serie) {
            punto.setDestacado(punto.getEtiqueta().equals(destacada));
        }
        return serie;
    }

    private ResumenMetricas.PuntoIngreso punto(String etiqueta, String monto) {
        return new ResumenMetricas.PuntoIngreso(etiqueta, new BigDecimal(monto), 0, false);
    }
}
