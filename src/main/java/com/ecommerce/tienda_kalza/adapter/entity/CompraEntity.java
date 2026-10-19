package com.ecommerce.tienda_kalza.adapter.entity;

// Autor: Samuel

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "\"Compras\"",
        indexes = @Index(name = "IDX_COMPRA_CLIENTE", columnList = "id_cliente"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder @ToString(exclude = {"cliente", "detalles"})
public class CompraEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_compra", nullable = false, updatable = false)
    private int idCompra;

    @Column(name = "estado", length = 30, nullable = false)
    @Builder.Default
    private String estado = "pendiente";

    @DecimalMin("0.00")
    @Column(name = "total_con_descuento", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalConDescuento;

    @DecimalMin("0.00")
    @Column(name = "total_sin_descuento", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalSinDescuento;

    @CreationTimestamp
    @Column(name = "fecha_de_compra", nullable = false, updatable = false)
    private LocalDateTime fechaDeCompra;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "id_cliente",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_COMPRA_USUARIO")
    )
    private UsuarioEntity cliente;

    @OneToMany(mappedBy = "compra", fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @Builder.Default
    private List<DetalleCompraEntity> detalles = new ArrayList<>();


    @OneToOne(mappedBy = "compra", fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private BoletaEntity boleta;


    @OneToOne(mappedBy = "compra", fetch = FetchType.LAZY)
    private EntregaEntity entrega;
}
