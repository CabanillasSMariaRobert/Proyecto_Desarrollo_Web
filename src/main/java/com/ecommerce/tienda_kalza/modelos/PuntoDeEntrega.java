package com.ecommerce.tienda_kalza.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "PuntosDeEntregas")
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

    public PuntoDeEntrega() {}

    public Integer getIdPuntoDeEntrega() { return idPuntoDeEntrega; }
    public void setIdPuntoDeEntrega(Integer idPuntoDeEntrega) { this.idPuntoDeEntrega = idPuntoDeEntrega; }

    public Integer getIdDistrito() { return idDistrito; }
    public void setIdDistrito(Integer idDistrito) { this.idDistrito = idDistrito; }

    public BigDecimal getLatitud() { return latitud; }
    public void setLatitud(BigDecimal latitud) { this.latitud = latitud; }

    public BigDecimal getLongitud() { return longitud; }
    public void setLongitud(BigDecimal longitud) { this.longitud = longitud; }

    public LocalDateTime getCreadaEl() { return creadaEl; }
    public void setCreadaEl(LocalDateTime creadaEl) { this.creadaEl = creadaEl; }

    public LocalDateTime getActualizadaEl() { return actualizadaEl; }
    public void setActualizadaEl(LocalDateTime actualizadaEl) { this.actualizadaEl = actualizadaEl; }
}