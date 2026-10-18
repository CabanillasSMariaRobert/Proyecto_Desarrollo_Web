package com.ecommerce.tienda_kalza.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "Publicidades")
@Getter
@Setter
@NoArgsConstructor
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
}