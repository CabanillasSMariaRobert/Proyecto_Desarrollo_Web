package com.ecommerce.tienda_kalza.adapter.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.ecommerce.tienda_kalza.adapter.repository.MetricaRepository;
import com.ecommerce.tienda_kalza.adapter.repository.MetricaRepository.ValorPeriodo;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MetricaService {
    private static final ZoneId ZONA = ZoneId.of("America/Lima");
    private static final Locale ESPANOL = Locale.forLanguageTag("es-PE");
    private final MetricaRepository repository;
    private final Clock reloj;

    @Autowired
    public MetricaService(MetricaRepository repository) {
        this(repository, Clock.system(ZONA));
    }

    public MetricaService(MetricaRepository repository, Clock reloj) {
        this.repository = repository;
        this.reloj = reloj.withZone(ZONA);
    }

    public record Indicador(String codigo, String nombre, BigDecimal valor, String unidad,
                            String referencia, String icono) {}
    public record Variante(String producto, int talla, String color, int stock) {}
    public record Punto(String etiqueta, BigDecimal valor) {}
    public record Serie(String codigo, String nombre, String descripcion, String ejeX, String unidad,
                        boolean linea, List<Punto> puntos) {}
    public record Panel(LocalDate fecha, List<Indicador> indicadores, List<Serie> series,
                        List<Variante> variantesBajas) {}

    public Panel obtener() {
        LocalDate hoy = LocalDate.now(reloj);
        LocalDate primerMes = hoy.withDayOfMonth(1).minusMonths(5);
        LocalDate primerDia = hoy.minusDays(6);
        // Las columnas LocalDateTime representan la fecha y hora local de Lima.
        var inicioMeses = primerMes.atStartOfDay();
        var inicioDias = primerDia.atStartOfDay();
        var fin = hoy.plusDays(1).atStartOfDay();
        var formatoMes = DateTimeFormatter.ofPattern("MMM yy", ESPANOL);
        var formatoDia = DateTimeFormatter.ofPattern("dd/MM", ESPANOL);
        List<Variante> variantes = repository.variantesConStockBajo().stream()
                .map(v -> new Variante(v.getProducto(), v.getTalla(), v.getColor(), v.getStock())).toList();
        String fecha = hoy.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        List<Indicador> indicadores = List.of(
                new Indicador("IP01", "Importe de ventas del día", repository.ventasDelDia(hoy.atStartOfDay(), fin), "S/", "Hoy · " + fecha, "bi-cash-stack"),
                new Indicador("IP02", "Compras pendientes de pago", BigDecimal.valueOf(repository.comprasPendientes()), "compras", "Al " + fecha, "bi-wallet2"),
                new Indicador("IP03", "Usuarios activos", BigDecimal.valueOf(repository.usuariosActivos()), "usuarios", "Al " + fecha, "bi-people"),
                new Indicador("IP04", "Variantes con existencias bajas", BigDecimal.valueOf(variantes.size()), "variantes", "Al " + fecha + " · Stock de 0 a 4", "bi-box-seam"),
                new Indicador("IP05", "Entregas pendientes de completar", BigDecimal.valueOf(repository.entregasPendientes()), "entregas", "Al " + fecha, "bi-truck"));
        List<Serie> series = List.of(
                serie("MT01", "Importe de ventas por mes", "Compras pagadas, enviadas o entregadas. Se excluyen las pendientes y canceladas.", "Mes de compra", "S/", true,
                        completar(repository.ventasPorMes(inicioMeses, fin), primerMes, 6, formatoMes, d -> d.plusMonths(1))),
                serie("MT02", "Compras registradas por día", "Últimos siete días, incluyendo hoy. Se contabilizan todos los estados.", "Día de compra", "compras", false,
                        completar(repository.comprasPorDia(inicioDias, fin), primerDia, 7, formatoDia, d -> d.plusDays(1))),
                serie("MT03", "Usuarios registrados por mes", "Últimos seis meses, incluyendo el actual. Cuentas de todos los estados.", "Mes de registro", "usuarios", false,
                        completar(repository.usuariosPorMes(inicioMeses, fin), primerMes, 6, formatoMes, d -> d.plusMonths(1))),
                serie("MT04", "Unidades de calzado vendidas por mes", "Pares en los detalles de compras pagadas, enviadas o entregadas.", "Mes de compra", "pares", true,
                        completar(repository.paresVendidosPorMes(inicioMeses, fin), primerMes, 6, formatoMes, d -> d.plusMonths(1))),
                serie("MT05", "Entregas completadas por mes", "Solo entregas ENTREGADO con fecha de entrega informada, agrupadas por esa fecha.", "Mes de entrega", "entregas", false,
                        completar(repository.entregasCompletadasPorMes(inicioMeses, fin), primerMes, 6, formatoMes, d -> d.plusMonths(1))));
        return new Panel(hoy, indicadores, series, variantes);
    }

    private Serie serie(String codigo, String nombre, String descripcion, String ejeX, String unidad,
                        boolean linea, List<Punto> puntos) {
        return new Serie(codigo, nombre, descripcion, ejeX, unidad, linea, puntos);
    }

    private List<Punto> completar(List<ValorPeriodo> resultados, LocalDate inicio, int cantidad,
                                  DateTimeFormatter formato, Function<LocalDate, LocalDate> siguiente) {
        Map<LocalDate, BigDecimal> valores = resultados.stream()
                .collect(Collectors.toMap(ValorPeriodo::getPeriodo, ValorPeriodo::getValor));
        List<Punto> puntos = new ArrayList<>();
        LocalDate fecha = inicio;
        for (int i = 0; i < cantidad; i++, fecha = siguiente.apply(fecha)) {
            puntos.add(new Punto(fecha.format(formato), valores.getOrDefault(fecha, BigDecimal.ZERO)));
        }
        return puntos;
    }
}
