package com.ecommerce.tienda_kalza.adapter.model;

import com.ecommerce.tienda_kalza.adapter.enums.EstadoEntrega;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class Entrega {
    private int id;

    private Compra compra;
    private PuntoEntrega puntoDeEntrega;
    private EstadoEntrega estado = EstadoEntrega.PENDIENTE;


    //-------- COLUMNAS DE FECHAS DE TIEMPO --------
    private LocalDateTime asignadoEl;
    private LocalDateTime recogidoEl;
    private LocalDateTime entregadoEl;

    // -------- FECHAS DE CREACION Y ACTUALIZACIÓN --------
    private LocalDateTime creadoEl;
    private LocalDateTime actualizadoEl;

    public Entrega(){}

    // Constructor completo
    public Entrega(
            int id,
            Compra compra,
            PuntoEntrega puntoDeEntrega,
            EstadoEntrega estado,
            LocalDateTime asignadoEl,
            LocalDateTime recogidoEl,
            LocalDateTime entregadoEl,
            LocalDateTime creadoEl,
            LocalDateTime actualizadoEl
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
            LocalDateTime asignadoEl,
            LocalDateTime recogidoEl,
            LocalDateTime entregadoEl,
            LocalDateTime creadoEl,
            LocalDateTime actualizadoEl
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
            LocalDateTime creadoEl,
            LocalDateTime actualizadoEl
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
            LocalDateTime creadoEl
    ) {
        this.compra = compra;
        this.puntoDeEntrega = puntoDeEntrega;
        this.estado = estado;
        this.asignadoEl = null;
        this.recogidoEl = null;
        this.entregadoEl = null;
        this.creadoEl = creadoEl;
        this.actualizadoEl = LocalDateTime.now();
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
        this.creadoEl = LocalDateTime.now();
        this.actualizadoEl = LocalDateTime.now();
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
        this.creadoEl = LocalDateTime.now();
        this.actualizadoEl = LocalDateTime.now();
    }
}
