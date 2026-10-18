package com.ecommerce.tienda_kalza.adapter.entity;

import com.ecommerce.tienda_kalza.adapter.enums.EstadoEntrega;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;

@Entity
@Table(name="\"Entregas\"")
@Getter
@Setter
public class EntregaEntity {

    @Id
    @Column(name="id_entrega")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_compra")
    private CompraEntity compra;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="id_punto_de_entrega", nullable = false)
    private PuntoEntregaEntity puntoDeEntrega;

    @Enumerated (EnumType.STRING)
    @Column(nullable = false)
    private EstadoEntrega estado = EstadoEntrega.PENDIENTE;


    //-------- COLUMNAS DE FECHAS DE TIEMPO --------
    @Column(name="asignado_el")
    private LocalDate asignadoEl;

    @Column(name="recogido_el")
    private LocalDate recogidoEl;

    @Column(name="entregado_el")
    private LocalDate entregadoEl;

    // -------- FECHAS DE CREACION Y ACTUALIZACIÓN --------
    @CreationTimestamp
    @Column(name="creado_el", nullable = false, updatable = false)
    private LocalDate creadoEl;

    @UpdateTimestamp
    @Column(name="actualizado_el", nullable = false)
    private LocalDate actualizadoEl;

    public EntregaEntity(){}

    // Constructor completo
    public EntregaEntity(
            Long id,
            CompraEntity compra,
            PuntoEntregaEntity puntoDeEntrega,
            EstadoEntrega estado,
            LocalDate asignadoEl,
            LocalDate recogidoEl,
            LocalDate entregadoEl,
            LocalDate creadoEl,
            LocalDate actualizadoEl
    ){
        this.id = id;
        this.compra = compra;
        this.puntoDeEntrega = puntoDeEntrega;
        this.estado = estado;
        this.asignadoEl = asignadoEl;
        this.recogidoEl = recogidoEl;
        this.entregadoEl = entregadoEl;
        this.creadoEl = creadoEl;
        this.actualizadoEl = actualizadoEl;
    }

    // Constructor sin Id
    public EntregaEntity(
            CompraEntity compra,
            PuntoEntregaEntity puntoDeEntrega,
            EstadoEntrega estado,
            LocalDate asignadoEl,
            LocalDate recogidoEl,
            LocalDate entregadoEl,
            LocalDate creadoEl,
            LocalDate actualizadoEl
    ) {
        this.compra = compra;
        this.puntoDeEntrega = puntoDeEntrega;
        this.estado = estado;
        this.asignadoEl = asignadoEl;
        this.recogidoEl = recogidoEl;
        this.entregadoEl = entregadoEl;
        this.creadoEl = creadoEl;
        this.actualizadoEl = actualizadoEl;
    }

    // Constructor de solo entrega sin fechas de reparto(asignado, recogido y entregado)
    public EntregaEntity(
            CompraEntity compra,
            PuntoEntregaEntity puntoDeEntrega,
            EstadoEntrega estado,
            LocalDate creadoEl,
            LocalDate actualizadoEl
    ) {
        this.compra = compra;
        this.puntoDeEntrega = puntoDeEntrega;
        this.estado = estado;
        this.asignadoEl = null;
        this.recogidoEl = null;
        this.entregadoEl = null;
        this.creadoEl = creadoEl;
        this.actualizadoEl = actualizadoEl;
    }

    // Constructor de solo entrega sin fechas de reparto(asignado, recogido y entregado) ni de actualización
    public EntregaEntity(
            CompraEntity compra,
            PuntoEntregaEntity puntoDeEntrega,
            EstadoEntrega estado,
            LocalDate creadoEl
    ) {
        this.compra = compra;
        this.puntoDeEntrega = puntoDeEntrega;
        this.estado = estado;
        this.asignadoEl = null;
        this.recogidoEl = null;
        this.entregadoEl = null;
        this.creadoEl = creadoEl;
    }

    // Constructor de solo entrega sin fechas de reparto(asignado, recogido y entregado) ni de creacion o actualización
    public EntregaEntity(
            CompraEntity compra,
            PuntoEntregaEntity puntoDeEntrega,
            EstadoEntrega estado
    ) {
        this.compra = compra;
        this.puntoDeEntrega = puntoDeEntrega;
        this.estado = estado;
        this.asignadoEl = null;
        this.recogidoEl = null;
        this.entregadoEl = null;
    }

    // Constructor de solo entrega sin fechas de reparto(asignado, recogido y entregado) ni de creacion, actualización o estado
    public EntregaEntity(
            CompraEntity compra,
            PuntoEntregaEntity puntoDeEntrega
    ) {
        this.compra = compra;
        this.puntoDeEntrega = puntoDeEntrega;
        this.asignadoEl = null;
        this.recogidoEl = null;
        this.entregadoEl = null;
    }
}
