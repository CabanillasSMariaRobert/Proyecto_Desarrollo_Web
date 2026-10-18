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
@Table(name = "Boletas")
@Getter
@Setter
@NoArgsConstructor
public class Boleta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_boleta")
    private Integer idBoleta;

    @NotBlank
    @Size(max = 150)
    @Column(name = "cliente", nullable = false, length = 150)
    private String cliente;

    @NotBlank
    @Size(min = 11, max = 11)
    @Column(name = "ruc_de_la_tienda", nullable = false, length = 11)
    private String rucDeLaTienda;

    @NotBlank
    @Size(max = 150)
    @Column(name = "pasarela_de_pago", nullable = false, length = 150)
    private String pasarelaDePago;

    @NotBlank
    @Size(max = 150)
    @Column(name = "red_de_tarjeta", nullable = false, length = 150)
    private String redDeTarjeta;

    @NotBlank
    @Size(min = 4, max = 4)
    @Column(name = "ultimos_4_digitos", nullable = false, length = 4)
    private String ultimos4Digitos;

    @NotNull
    @DecimalMin(value = "0.0")
    @Column(name = "total", nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @CreationTimestamp
    @Column(name = "creado_el", nullable = false, updatable = false)
    private LocalDateTime creadoEl;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_compra", nullable = false, unique = true,
        foreignKey = @ForeignKey(name = "fk_boletas_compra"))
    private Compra compra;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_metodo_de_pago", nullable = false,
        foreignKey = @ForeignKey(name = "fk_boletas_metodopago"))
    private MetodoDePago metodoDePago;
}