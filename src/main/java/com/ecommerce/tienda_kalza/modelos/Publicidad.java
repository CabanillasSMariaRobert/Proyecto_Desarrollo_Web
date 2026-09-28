package com.ecommerce.tienda_kalza.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "Publicidades")
public class Publicidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_publicidad")
    private Integer idPublicidad;

    @NotBlank
    @Column(name = "url_del_banner", nullable = false, columnDefinition = "TEXT")
    private String urlDelBanner;

    @NotBlank
    @Size(max = 250)
    @Column(name = "titulo", nullable = false, length = 250)
    private String titulo;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "enlace", columnDefinition = "TEXT")
    private String enlace;

    @Column(name = "esta_activo", nullable = false)
    private Boolean estaActivo = true;

    @CreationTimestamp
    @Column(name = "creado_el", nullable = false, updatable = false)
    private LocalDateTime creadoEl;

    public Publicidad() {}

    public Integer getIdPublicidad() { return idPublicidad; }
    public void setIdPublicidad(Integer idPublicidad) { this.idPublicidad = idPublicidad; }

    public String getUrlDelBanner() { return urlDelBanner; }
    public void setUrlDelBanner(String urlDelBanner) { this.urlDelBanner = urlDelBanner; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getEnlace() { return enlace; }
    public void setEnlace(String enlace) { this.enlace = enlace; }

    public Boolean getEstaActivo() { return estaActivo; }
    public void setEstaActivo(Boolean estaActivo) { this.estaActivo = estaActivo; }

    public LocalDateTime getCreadoEl() { return creadoEl; }
    public void setCreadoEl(LocalDateTime creadoEl) { this.creadoEl = creadoEl; }
}