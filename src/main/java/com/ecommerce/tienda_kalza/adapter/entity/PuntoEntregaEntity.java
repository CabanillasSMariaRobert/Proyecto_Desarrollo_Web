package com.ecommerce.tienda_kalza.adapter.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;

@Entity
@Table(name="\"Puntos_de_entregas\"")
@Getter
@Setter
public class PuntoEntregaEntity {

    @Id
    @Column(name="id_punto_de_entrega")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private String direccion;
    @Column(nullable = false)
    private String referencia;

    @Column(nullable = false)
    private double latitud;
    @Column(nullable = false)
    private double longitud;

    @Column(name="esta_activo", nullable = false)
    private boolean estaActivo;

    @CreationTimestamp
    @Column(name="creado_el", nullable = false, updatable = false)
    private LocalDate creadoEl;
    @UpdateTimestamp
    @Column(name="actualizado_el", nullable = false)
    private LocalDate actualizadoEl;

    public PuntoEntregaEntity(){}

    // Constructor completo
    public PuntoEntregaEntity(
            Long id,
            String nombre,
            String direccion,
            String referencia,
            double latitud,
            double longitud,
            boolean estaActivo,
            LocalDate creadoEl,
            LocalDate actualizadoEl
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
    public PuntoEntregaEntity(
            String nombre,
            String direccion,
            String referencia,
            double latitud,
            double longitud,
            boolean estaActivo,
            LocalDate creadoEl,
            LocalDate actualizadoEl
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
    public PuntoEntregaEntity(
            String nombre,
            String direccion,
            String referencia,
            double latitud,
            double longitud,
            LocalDate creadoEl,
            LocalDate actualizadoEl
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
    public PuntoEntregaEntity(
            String nombre,
            String direccion,
            String referencia,
            double latitud,
            double longitud,
            LocalDate creadoEl
    ){
        this.nombre = nombre;
        this.direccion = direccion;
        this.referencia = referencia;
        this.latitud = latitud;
        this.longitud = longitud;
        this.estaActivo = true;
        this.creadoEl = creadoEl;
    }

    // Constructor sin id, estaActivo, creado ni actualizado
    public PuntoEntregaEntity(
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
    }

}
