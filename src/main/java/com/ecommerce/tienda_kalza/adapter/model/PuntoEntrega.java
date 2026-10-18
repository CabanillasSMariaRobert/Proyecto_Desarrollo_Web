package com.ecommerce.tienda_kalza.adapter.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PuntoEntrega {

    private int id;

    private String nombre;
    private String direccion;
    private String referencia;

    private double latitud;
    private double longitud;

    private boolean estaActivo;

    private LocalDateTime creadoEl;
    private LocalDateTime actualizadoEl;

    public PuntoEntrega(){}

    // Constructor completo
    public PuntoEntrega(
            int id,
            String nombre,
            String direccion,
            String referencia,
            double latitud,
            double longitud,
            boolean estaActivo,
            LocalDateTime creadoEl,
            LocalDateTime actualizadoEl
    ){
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.referencia = referencia;
        this.latitud = latitud;
        this.longitud = longitud;
        this.estaActivo = estaActivo;
        this.creadoEl = creadoEl;
        this.actualizadoEl = actualizadoEl;
    }

    // Constructor sin id
    public PuntoEntrega(
            String nombre,
            String direccion,
            String referencia,
            double latitud,
            double longitud,
            boolean estaActivo,
            LocalDateTime creadoEl,
            LocalDateTime actualizadoEl
    ){
        this.nombre = nombre;
        this.direccion = direccion;
        this.referencia = referencia;
        this.latitud = latitud;
        this.longitud = longitud;
        this.estaActivo = estaActivo;
        this.creadoEl = creadoEl;
        this.actualizadoEl = actualizadoEl;
    }

    // Constructor sin estadoActivo
    public PuntoEntrega(
            String nombre,
            String direccion,
            String referencia,
            double latitud,
            double longitud,
            LocalDateTime creadoEl,
            LocalDateTime actualizadoEl
    ){
        this.nombre = nombre;
        this.direccion = direccion;
        this.referencia = referencia;
        this.latitud = latitud;
        this.longitud = longitud;
        this.estaActivo = true;
        this.creadoEl = creadoEl;
        this.actualizadoEl = actualizadoEl;
    }

    // Constructor sin id, estaActivo y actualizado
    public PuntoEntrega(
            String nombre,
            String direccion,
            String referencia,
            double latitud,
            double longitud,
            LocalDateTime creadoEl
    ){
        this.nombre = nombre;
        this.direccion = direccion;
        this.referencia = referencia;
        this.latitud = latitud;
        this.longitud = longitud;
        this.estaActivo = true;
        this.creadoEl = creadoEl;
        this.actualizadoEl = LocalDateTime.now();
    }

    // Constructor sin id, estaActivo, creado ni actualizado
    public PuntoEntrega(
            String nombre,
            String direccion,
            String referencia,
            double latitud,
            double longitud
    ){
        this.nombre = nombre;
        this.direccion = direccion;
        this.referencia = referencia;
        this.latitud = latitud;
        this.longitud = longitud;
        this.estaActivo = true;
        this.creadoEl = LocalDateTime.now();
        this.actualizadoEl = LocalDateTime.now();
    }

}
