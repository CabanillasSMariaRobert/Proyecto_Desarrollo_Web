package com.ecommerce.tienda_kalza.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "DetalleDeCompra",
    indexes = @Index(name = "idx_detalle_compra_variante", columnList = "id_variante_de_zapato"))
@IdClass(DetalleDeCompraId.class)
@Getter
@Setter
@NoArgsConstructor
public class DetalleDeCompra {

    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_compra", nullable = false,
        foreignKey = @ForeignKey(name = "fk_detalle_compra"))
    private Compra compra;

    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_variante_de_zapato", nullable = false,
        foreignKey = @ForeignKey(name = "fk_detalle_variante"))
    private VarianteDeZapato varianteDeZapato;

    @NotBlank
    @Size(max = 150)
    @Column(name = "nombre_del_producto", nullable = false, length = 150)
    private String nombreDelProducto;

    @NotNull
    @DecimalMin(value = "0.0")
    @Column(name = "precio_original_x_unidad", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioOriginalXUnidad;

    @NotNull
    @Min(1)
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @DecimalMin(value = "0.0")
    @Column(name = "descuento_x_unidad", precision = 10, scale = 2)
    private BigDecimal descuentoXUnidad = BigDecimal.ZERO;

    @NotNull
    @DecimalMin(value = "0.0")
    @Column(name = "precio_con_descuento", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioConDescuento;

    @NotNull
    @DecimalMin(value = "0.0")
    @Column(name = "precio_sin_descuento", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioSinDescuento;

    @CreationTimestamp
    @Column(name = "creado_el", nullable = false, updatable = false)
    private LocalDateTime creadoEl;

    @PrePersist
    @PreUpdate
    protected void calcularTotales() {
        if (precioOriginalXUnidad != null && cantidad != null) {
            this.precioSinDescuento = precioOriginalXUnidad.multiply(BigDecimal.valueOf(cantidad));
            if (descuentoXUnidad != null) {
                this.precioConDescuento = precioSinDescuento.subtract(descuentoXUnidad.multiply(BigDecimal.valueOf(cantidad)));
            } else {
                this.precioConDescuento = precioSinDescuento;
            }
        }
    }
}