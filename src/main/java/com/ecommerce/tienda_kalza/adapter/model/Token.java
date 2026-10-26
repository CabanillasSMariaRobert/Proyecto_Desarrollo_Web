package com.ecommerce.tienda_kalza.adapter.model;

import com.ecommerce.tienda_kalza.adapter.enums.TipoToken;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Token {
    private Long id;

    private TipoToken tipo;
    private String token;

    private LocalDateTime creadoEl;
    private LocalDateTime expiraEl;
    private LocalDateTime usadoEl;

    private Usuario usuario;

    public Token(){}

    // Constructor completo
    public Token(
            Long id,
            TipoToken tipo,
            String token,
            LocalDateTime creadoEl,
            LocalDateTime expiraEl,
            LocalDateTime usadoEl,
            Usuario usuario
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
    public Token(
            TipoToken tipo,
            String token,
            LocalDateTime creadoEl,
            LocalDateTime expiraEl,
            LocalDateTime usadoEl,
            Usuario usuario
    ){
        this.tipo = tipo;
        this.token = token;
        this.creadoEl = creadoEl;
        this.expiraEl = expiraEl;
        this.usadoEl = usadoEl;
        this.usuario = usuario;
    }

    // Constructor sin id ni usadoEl
    public Token(
            TipoToken tipo,
            String token,
            LocalDateTime creadoEl,
            LocalDateTime expiraEl,
            Usuario usuario
    ){
        this.tipo = tipo;
        this.token = token;
        this.creadoEl = creadoEl;
        this.expiraEl = expiraEl;
        this.usadoEl = null;
        this.usuario = usuario;
    }

    // Constructor sin id, creadoEl ni usadoEl
    public Token(
            TipoToken tipo,
            String token,
            LocalDateTime expiraEl,
            Usuario usuario
    ){
        this.tipo = tipo;
        this.token = token;
        this.expiraEl = expiraEl;
        this.creadoEl = LocalDateTime.now();
        this.usadoEl = null;
        this.usuario = usuario;
    }
}
