package com.ecommerce.tienda_kalza.adapter.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "\"Productos\"",
        indexes = @Index(name = "IDX_PRODUCTOS_MARCA", columnList = "id_marca"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProductoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private int idProducto;

    @Column(name = "nombre", length = 150, nullable = false)
    private String nombre;

    @Column(name = "esta_activo", nullable = false)
    @ColumnDefault("true")
    @Builder.Default
    private boolean estaActivo = true;

    @Column(name = "descripcion", length = 2000)
    private String descripcion;

    @DecimalMin("0.00")
    @Column(name = "precio_original", precision = 10, scale = 2, nullable = false)
    private BigDecimal precioOriginal;

    // descuento
    @DecimalMin("0.00") @DecimalMax("1.00")
    @Column(name = "descuento", precision = 3, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal descuento = BigDecimal.ZERO;

    @Column(name = "precio_con_descuento", insertable = false, updatable = false,
            columnDefinition = "NUMERIC(10,2) GENERATED ALWAYS AS (ROUND(precio_original * (1 - descuento), 2))")
    private BigDecimal precioConDescuento;

    @Column(name = "url_imagen", length = 255)
    private String urlImagen;

    // Relacion con Marca: Columna
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_marca", nullable = false)
    private MarcaEntity marca;

    @CreationTimestamp
    @Column(name = "creado_el", nullable = false, updatable = false)
    private LocalDateTime creadoEl;

    @UpdateTimestamp
    @Column(name = "actualizado_el", nullable = false)
    private LocalDateTime actualizadoEl;

    // Relacion con CategoriaProducto
    @OneToMany(mappedBy = "producto", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CategoriaProductoEntity> categorias = new ArrayList<>();
}
