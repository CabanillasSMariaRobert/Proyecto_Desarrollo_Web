package com.ecommerce.tienda_kalza.adapter.model;

import com.ecommerce.tienda_kalza.adapter.enums.EstadoEntrega;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Entrega {
    private Long id;

    private Compra compra;
    private PuntoEntrega puntoDeEntrega;
    private EstadoEntrega estado = EstadoEntrega.PENDIENTE;


    //-------- COLUMNAS DE FECHAS DE TIEMPO --------
    private LocalDate asignadoEl;
    private LocalDate recogidoEl;
    private LocalDate entregadoEl;

    // -------- FECHAS DE CREACION Y ACTUALIZACIÓN --------
    private LocalDate creadoEl;
    private LocalDate actualizadoEl;

    public Entrega(){}

    // Constructor completo
    public Entrega(
            Long id,
            Compra compra,
            PuntoEntrega puntoDeEntrega,
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
    public Entrega(
            Compra compra,
            PuntoEntrega puntoDeEntrega,
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
    public Entrega(
            Compra compra,
            PuntoEntrega puntoDeEntrega,
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
    public Entrega(
            Compra compra,
            PuntoEntrega puntoDeEntrega,
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
        this.actualizadoEl = LocalDate.now();
    }

    // Constructor de solo entrega sin fechas de reparto(asignado, recogido y entregado) ni de creacion o actualización
    public Entrega(
            Compra compra,
            PuntoEntrega puntoDeEntrega,
            EstadoEntrega estado
    ) {
        this.compra = compra;
        this.puntoDeEntrega = puntoDeEntrega;
        this.estado = estado;
        this.asignadoEl = null;
        this.recogidoEl = null;
        this.entregadoEl = null;
        this.creadoEl = LocalDate.now();
        this.actualizadoEl = LocalDate.now();
    }

    // Constructor de solo entrega sin fechas de reparto(asignado, recogido y entregado) ni de creacion, actualización o estado
    public Entrega(
            Compra compra,
            PuntoEntrega puntoDeEntrega
    ) {
        this.compra = compra;
        this.puntoDeEntrega = puntoDeEntrega;
        this.asignadoEl = null;
        this.recogidoEl = null;
        this.entregadoEl = null;
        this.creadoEl = LocalDate.now();
        this.actualizadoEl = LocalDate.now();
    }
}
