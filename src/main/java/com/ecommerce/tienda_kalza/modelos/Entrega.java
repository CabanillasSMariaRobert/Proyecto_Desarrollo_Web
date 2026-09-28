package com.ecommerce.tienda_kalza.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "Entregas")
public class Entrega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_entrega")
    private Integer idEntrega;

    @NotBlank
    @Size(max = 30)
    @Column(name = "estado", nullable = false, length = 30)
    private String estado = "pendiente";

    @Column(name = "asignado_el")
    private LocalDateTime asignadoEl;

    @Column(name = "recogido_el")
    private LocalDateTime recogidoEl;

    @Column(name = "entregado_el")
    private LocalDateTime entregadoEl;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_compra", nullable = false, unique = true,
        foreignKey = @ForeignKey(name = "fk_entregas_compra"))
    private Compra compra;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_punto_de_entrega", nullable = false,
        foreignKey = @ForeignKey(name = "fk_entregas_punto"))
    private PuntoDeEntrega puntoDeEntrega;

    @CreationTimestamp
    @Column(name = "creado_el", nullable = false, updatable = false)
    private LocalDateTime creadoEl;

    @UpdateTimestamp
    @Column(name = "actualizado_el", nullable = false)
    private LocalDateTime actualizadoEl;

    public Entrega() {}

    public Integer getIdEntrega() { return idEntrega; }
    public void setIdEntrega(Integer idEntrega) { this.idEntrega = idEntrega; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public LocalDateTime getAsignadoEl() { return asignadoEl; }
    public void setAsignadoEl(LocalDateTime asignadoEl) { this.asignadoEl = asignadoEl; }

    public LocalDateTime getRecogidoEl() { return recogidoEl; }
    public void setRecogidoEl(LocalDateTime recogidoEl) { this.recogidoEl = recogidoEl; }

    public LocalDateTime getEntregadoEl() { return entregadoEl; }
    public void setEntregadoEl(LocalDateTime entregadoEl) { this.entregadoEl = entregadoEl; }

    public Compra getCompra() { return compra; }
    public void setCompra(Compra compra) { this.compra = compra; }

    public PuntoDeEntrega getPuntoDeEntrega() { return puntoDeEntrega; }
    public void setPuntoDeEntrega(PuntoDeEntrega puntoDeEntrega) { this.puntoDeEntrega = puntoDeEntrega; }

    public LocalDateTime getCreadoEl() { return creadoEl; }
    public void setCreadoEl(LocalDateTime creadoEl) { this.creadoEl = creadoEl; }

    public LocalDateTime getActualizadoEl() { return actualizadoEl; }
    public void setActualizadoEl(LocalDateTime actualizadoEl) { this.actualizadoEl = actualizadoEl; }
}