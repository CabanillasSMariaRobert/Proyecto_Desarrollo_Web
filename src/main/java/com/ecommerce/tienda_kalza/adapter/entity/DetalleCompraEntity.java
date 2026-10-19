package com.ecommerce.tienda_kalza.adapter.entity;

// Autor: Samuel

import com.ecommerce.tienda_kalza.adapter.utils.DetalleCompraEntityId;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "\"Detalle_de_compra\"",
        indexes = @Index(name = "IDX_DETALLE_COMPRA_VARIANTE", columnList = "id_variante_de_zapato"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder @ToString(exclude = {"compra", "varianteZapato"})
public class DetalleCompraEntity {
    @EmbeddedId
    @Builder.Default
    private DetalleCompraEntityId id = new DetalleCompraEntityId();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("idCompra")
    @JoinColumn(
            name = "id_compra",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_DETALLE_COMPRA")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CompraEntity compra;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("idVariante")
    @JoinColumn(
            name = "id_variante_de_zapato",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_DETALLE_VARIANTE")
    )
    private VarianteZapatoEntity varianteZapato;

    @Column(name = "nombre_del_producto", length = 150, nullable = false)
    private String nombreDelProducto;

    @DecimalMin("0.00")
    @Column(name = "precio_original_x_unidad", precision = 10, scale = 2, nullable = false)
    private BigDecimal precioOriginalXUnidad;

    @DecimalMin("0.00")
    @Column(name = "descuento_x_unidad", precision = 10, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal descuentoXUnidad = BigDecimal.ZERO;

    @Min(1)
    @Column(name = "cantidad", nullable = false)
    private int cantidad;

    @Column(name = "precio_sin_descuento", insertable = false, updatable = false,
            columnDefinition = "NUMERIC(10,2) GENERATED ALWAYS AS (ROUND(precio_original_x_unidad * cantidad, 2))")
    private BigDecimal precioSinDescuento;

    @Column(name = "precio_con_descuento", insertable = false, updatable = false,
            columnDefinition = "NUMERIC(10,2) GENERATED ALWAYS AS (ROUND(precio_original_x_unidad * cantidad - descuento_x_unidad * cantidad, 2))")
    private BigDecimal precioConDescuento;

    @CreationTimestamp
    @Column(name = "creado_el", nullable = false, updatable = false)
    private LocalDateTime creadoEl;
}
