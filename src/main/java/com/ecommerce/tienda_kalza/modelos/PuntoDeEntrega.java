package com.ecommerce.tienda_kalza.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "PuntosDeEntregas")
@Getter
@Setter
@NoArgsConstructor
public class PuntoDeEntrega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_punto_de_entrega")
    private Integer idPuntoDeEntrega;

    @NotNull
    @Column(name = "id_distrito", nullable = false)
    private Integer idDistrito;

    @NotNull
    @Column(name = "latitud", nullable = false, precision = 15, scale = 10)
    private BigDecimal latitud;

    @NotNull
    @Column(name = "longitud", nullable = false, precision = 15, scale = 10)
    private BigDecimal longitud;

    @CreationTimestamp
    @Column(name = "creada_el", nullable = false, updatable = false)
    private LocalDateTime creadaEl;

    @UpdateTimestamp
    @Column(name = "actualizada_el", nullable = false)
    private LocalDateTime actualizadaEl;
}