package com.ecommerce.tienda_kalza.adapter.entity;

import com.ecommerce.tienda_kalza.adapter.enums.TipoToken;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name="\"Tokens_de_un_solo_uso\"")
@Getter
@Setter
public class TokenEntity {
    @Id
    @Column(name="id_token")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoToken tipo;
    @Column(nullable = false, length = 64, unique = true)
    private String token;

    @CreationTimestamp
    @Column(name="creado_el", nullable = false)
    private LocalDateTime creadoEl;
    @Column(name="expira_el", nullable = false)
    private LocalDateTime expiraEl;
    @Column(name="usado_el")
    private LocalDateTime usadoEl;

    // Relacion con Usuario: Columna
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_usuario")
    private UsuarioEntity usuario;

    public TokenEntity(){}

    // Constructor completo
    public TokenEntity(
            int id,
            TipoToken tipo,
            String token,
            LocalDateTime creadoEl,
            LocalDateTime expiraEl,
            LocalDateTime usadoEl,
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
            TipoToken tipo,
            String token,
            LocalDateTime creadoEl,
            LocalDateTime expiraEl,
            LocalDateTime usadoEl,
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
            TipoToken tipo,
            String token,
            LocalDateTime creadoEl,
            LocalDateTime expiraEl,
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
            TipoToken tipo,
            String token,
            LocalDateTime expiraEl,
            UsuarioEntity usuario
    ){
        this.tipo = tipo;
        this.token = token;
        this.expiraEl = expiraEl;
        this.usadoEl = null;
        this.usuario = usuario;
    }
}
