package com.ecommerce.tienda_kalza.adapter.repository;

import com.ecommerce.tienda_kalza.adapter.entity.CompraEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface MetricaRepository extends JpaRepository<CompraEntity, Integer> {
    interface ValorPeriodo {
        Integer getAnio();
        Integer getMes();
        Integer getDia();
        BigDecimal getValor();

        default LocalDate getPeriodo() {
            return LocalDate.of(getAnio(), getMes(), getDia());
        }
    }

    interface VarianteStock {
        String getProducto();
        int getTalla();
        String getColor();
        int getStock();
    }

    @Query("""
            SELECT YEAR(c.fechaDeCompra) AS anio, MONTH(c.fechaDeCompra) AS mes,
                   1 AS dia, SUM(c.totalConDescuento) AS valor
            FROM CompraEntity c
            WHERE c.estado IN ('pagado', 'enviado', 'entregado')
              AND c.fechaDeCompra >= :inicio AND c.fechaDeCompra < :fin
            GROUP BY YEAR(c.fechaDeCompra), MONTH(c.fechaDeCompra)
            ORDER BY YEAR(c.fechaDeCompra), MONTH(c.fechaDeCompra)
            """)
    List<ValorPeriodo> ventasPorMes(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);

    @Query("""
            SELECT YEAR(c.fechaDeCompra) AS anio, MONTH(c.fechaDeCompra) AS mes,
                   DAY(c.fechaDeCompra) AS dia, COUNT(c) AS valor
            FROM CompraEntity c
            WHERE c.fechaDeCompra >= :inicio AND c.fechaDeCompra < :fin
            GROUP BY YEAR(c.fechaDeCompra), MONTH(c.fechaDeCompra), DAY(c.fechaDeCompra)
            ORDER BY YEAR(c.fechaDeCompra), MONTH(c.fechaDeCompra), DAY(c.fechaDeCompra)
            """)
    List<ValorPeriodo> comprasPorDia(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);

    @Query("""
            SELECT YEAR(u.creadoEl) AS anio, MONTH(u.creadoEl) AS mes, 1 AS dia, COUNT(u) AS valor
            FROM UsuarioEntity u
            WHERE u.creadoEl >= :inicio AND u.creadoEl < :fin
            GROUP BY YEAR(u.creadoEl), MONTH(u.creadoEl)
            ORDER BY YEAR(u.creadoEl), MONTH(u.creadoEl)
            """)
    List<ValorPeriodo> usuariosPorMes(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);

    @Query("""
            SELECT YEAR(c.fechaDeCompra) AS anio, MONTH(c.fechaDeCompra) AS mes,
                   1 AS dia, SUM(d.cantidad) AS valor
            FROM CompraEntity c JOIN c.detalles d
            WHERE c.estado IN ('pagado', 'enviado', 'entregado')
              AND c.fechaDeCompra >= :inicio AND c.fechaDeCompra < :fin
            GROUP BY YEAR(c.fechaDeCompra), MONTH(c.fechaDeCompra)
            ORDER BY YEAR(c.fechaDeCompra), MONTH(c.fechaDeCompra)
            """)
    List<ValorPeriodo> paresVendidosPorMes(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);

    @Query("""
            SELECT YEAR(e.entregadoEl) AS anio, MONTH(e.entregadoEl) AS mes, 1 AS dia, COUNT(e) AS valor
            FROM EntregaEntity e
            WHERE e.estado = com.ecommerce.tienda_kalza.adapter.enums.EstadoEntrega.ENTREGADO
              AND e.entregadoEl IS NOT NULL
              AND e.entregadoEl >= :inicio AND e.entregadoEl < :fin
            GROUP BY YEAR(e.entregadoEl), MONTH(e.entregadoEl)
            ORDER BY YEAR(e.entregadoEl), MONTH(e.entregadoEl)
            """)
    List<ValorPeriodo> entregasCompletadasPorMes(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);

    @Query("""
            SELECT COALESCE(SUM(c.totalConDescuento), 0)
            FROM CompraEntity c
            WHERE c.estado IN ('pagado', 'enviado', 'entregado')
              AND c.fechaDeCompra >= :inicio AND c.fechaDeCompra < :fin
            """)
    BigDecimal ventasDelDia(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);

    @Query("SELECT COUNT(c) FROM CompraEntity c WHERE c.estado = 'pendiente'")
    long comprasPendientes();

    @Query("SELECT COUNT(u) FROM UsuarioEntity u WHERE u.estaActivo = TRUE AND u.estaBaneado = FALSE")
    long usuariosActivos();

    @Query("""
            SELECT p.nombre AS producto, v.talla AS talla, v.color AS color, v.stock AS stock
            FROM VarianteZapatoEntity v JOIN v.zapato z JOIN z.producto p
            WHERE v.stock BETWEEN 0 AND 4
            ORDER BY v.stock, p.nombre, v.talla, v.color, v.idVariante
            """)
    List<VarianteStock> variantesConStockBajo();

    @Query("""
            SELECT COUNT(e) FROM EntregaEntity e
            WHERE e.estado IN (
                com.ecommerce.tienda_kalza.adapter.enums.EstadoEntrega.PENDIENTE,
                com.ecommerce.tienda_kalza.adapter.enums.EstadoEntrega.ASIGNADO,
                com.ecommerce.tienda_kalza.adapter.enums.EstadoEntrega.RECOGIDO,
                com.ecommerce.tienda_kalza.adapter.enums.EstadoEntrega.EN_CAMINO)
            """)
    long entregasPendientes();
}