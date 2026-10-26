package com.ecommerce.tienda_kalza.adapter.entity;

// Autor: Samuel

import com.ecommerce.tienda_kalza.adapter.enums.RedUsada;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "\"Boletas\"")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder @ToString(exclude = {"compra", "metodoDePago"})
public class BoletaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_boleta", nullable = false, updatable = false)
    private int idBoleta;

    @Column(name = "cliente", length = 150, nullable = false)
    private String cliente;

    @Column(name = "ruc_de_la_tienda", length = 11, nullable = false)
    private String rucDeLaTienda;

    @Column(name = "pasarela_de_pago", length = 150, nullable = false)
    private String pasarelaDePago;

    @Enumerated(EnumType.STRING)
    @Column(name = "red_de_tarjeta", length = 15, nullable = false)
    private RedUsada redDeTarjeta;

    @Column(name = "ultimos_4_digitos", length = 4, nullable = false)
    private String ultimos4Digitos;

    @DecimalMin("0.00")
    @Column(name = "total", precision = 10, scale = 2, nullable = false)
    private BigDecimal total;

    @CreationTimestamp
    @Column(name = "creado_el", nullable = false, updatable = false)
    private LocalDateTime creadoEl;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "id_compra",
            nullable = false,
            unique = true,
            foreignKey = @ForeignKey(name = "FK_BOLETA_COMPRA")
    )
    private CompraEntity compra;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "id_metodo_de_pago",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_BOLETA_METODOPAGO")
    )
    private MetodoPagoEntity metodoDePago;
}
