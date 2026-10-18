package com.ecommerce.tienda_kalza.adapter.model;

import com.ecommerce.tienda_kalza.adapter.enums.TipoToken;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Token {
    private Long id;

    private String tipo;
    private TipoToken token;

    private LocalDate creadoEl;
    private LocalDate expiraEl;
    private LocalDate usadoEl;

    private Usuario usuario;

    public Token(){}

    // Constructor completo
    public Token(
            Long id,
            String tipo,
            TipoToken token,
            LocalDate creadoEl,
            LocalDate expiraEl,
            LocalDate usadoEl,
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
            String tipo,
            TipoToken token,
            LocalDate creadoEl,
            LocalDate expiraEl,
            LocalDate usadoEl,
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
            String tipo,
            TipoToken token,
            LocalDate creadoEl,
            LocalDate expiraEl,
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
            String tipo,
            TipoToken token,
            LocalDate expiraEl,
            Usuario usuario
    ){
        this.tipo = tipo;
        this.token = token;
        this.expiraEl = expiraEl;
        this.creadoEl = LocalDate.now();
        this.usadoEl = null;
        this.usuario = usuario;
    }
}
