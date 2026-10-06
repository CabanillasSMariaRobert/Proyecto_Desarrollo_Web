package com.ecommerce.tienda_kalza.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Compras",
    indexes = @Index(name = "idx_compras_cliente", columnList = "id_cliente"))
@Getter
@Setter
@NoArgsConstructor
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_compra")
    private Integer idCompra;

    @NotBlank
    @Size(max = 30)
    @Column(name = "estado", nullable = false, length = 30)
    private String estado = "pendiente";

    @NotNull
    @DecimalMin(value = "0.0")
    @Column(name = "total_con_descuento", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalConDescuento;

    @NotNull
    @DecimalMin(value = "0.0")
    @Column(name = "total_sin_descuento", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalSinDescuento;

    @CreationTimestamp
    @Column(name = "fecha_de_compra", nullable = false, updatable = false)
    private LocalDateTime fechaDeCompra;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_cliente", nullable = false,
        foreignKey = @ForeignKey(name = "fk_compras_cliente"))
    private Usuario cliente;

    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<DetalleDeCompra> detalles = new ArrayList<>();

    @OneToOne(mappedBy = "compra", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Boleta boleta;

    @OneToOne(mappedBy = "compra", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Entrega entrega;
}