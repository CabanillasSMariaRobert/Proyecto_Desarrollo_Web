package com.ecommerce.tienda_kalza.adapter.entity;

import com.ecommerce.tienda_kalza.adapter.enums.TipoToken;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@Table(name="\"Tokens_de_un_solo_uso\"")
@Getter
@Setter
public class TokenEntity {
    @Id
    @Column(name="id_token")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tipo;
    @Enumerated (EnumType.STRING)
    @Column(nullable = false)
    private TipoToken token;

    @CreationTimestamp
    @Column(name="creado_el", nullable = false)
    private LocalDate creadoEl;
    @Column(name="expira_el", nullable = false)
    private LocalDate expiraEl;
    @Column(name="usado_el")
    private LocalDate usadoEl;

    @ManyToOne
    @JoinColumn(name="id_usuario")
    private UsuarioEntity usuario;

    public TokenEntity(){}

    // Constructor completo
    public TokenEntity(
            Long id,
            String tipo,
            TipoToken token,
            LocalDate creadoEl,
            LocalDate expiraEl,
            LocalDate usadoEl,
            UsuarioEntity usuario
    ){
        this.id = id;
        this.tipo = tipo;
        this.token = token;
        this.creadoEl = creadoEl;
        this.expiraEl = expiraEl;
        this.usadoEl = usadoEl;
        this.usuario = usuario;
    }

    // Constructor sin id
    public TokenEntity(
            String tipo,
            TipoToken token,
            LocalDate creadoEl,
            LocalDate expiraEl,
            LocalDate usadoEl,
            UsuarioEntity usuario
    ){
        this.tipo = tipo;
        this.token = token;
        this.creadoEl = creadoEl;
        this.expiraEl = expiraEl;
        this.usadoEl = usadoEl;
        this.usuario = usuario;
    }

    // Constructor sin id ni usadoEl
    public TokenEntity(
            String tipo,
            TipoToken token,
            LocalDate creadoEl,
            LocalDate expiraEl,
            UsuarioEntity usuario
    ){
        this.tipo = tipo;
        this.token = token;
        this.creadoEl = creadoEl;
        this.expiraEl = expiraEl;
        this.usadoEl = null;
        this.usuario = usuario;
    }

    // Constructor sin id, creadoEl ni usadoEl
    public TokenEntity(
            String tipo,
            TipoToken token,
            LocalDate expiraEl,
            UsuarioEntity usuario
    ){
        this.tipo = tipo;
        this.token = token;
        this.expiraEl = expiraEl;
        this.usadoEl = null;
        this.usuario = usuario;
    }
}
