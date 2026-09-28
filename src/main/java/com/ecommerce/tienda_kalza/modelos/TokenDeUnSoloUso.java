package com.ecommerce.tienda_kalza.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "TokensDeUnSoloUso",
    uniqueConstraints = @UniqueConstraint(name = "uk_tokens_token", columnNames = "token"),
    indexes = @Index(name = "idx_tokens_usuario", columnList = "id_usuario"))
public class TokenDeUnSoloUso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_token")
    private Integer idToken;

    @NotBlank
    @Size(max = 20)
    @Column(name = "tipo", nullable = false, length = 20)
    private String tipo;

    @NotBlank
    @Size(max = 64)
    @Column(name = "token", nullable = false, unique = true, length = 64)
    private String token;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario", nullable = false,
        foreignKey = @ForeignKey(name = "fk_tokens_usuario"))
    private Usuario usuario;

    @NotNull
    @Column(name = "expira_el", nullable = false)
    private LocalDateTime expiraEl;

    @Column(name = "usado_el")
    private LocalDateTime usadoEl;

    @CreationTimestamp
    @Column(name = "creado_el", nullable = false, updatable = false)
    private LocalDateTime creadoEl;

    public TokenDeUnSoloUso() {}

    public Integer getIdToken() { return idToken; }
    public void setIdToken(Integer idToken) { this.idToken = idToken; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public LocalDateTime getExpiraEl() { return expiraEl; }
    public void setExpiraEl(LocalDateTime expiraEl) { this.expiraEl = expiraEl; }

    public LocalDateTime getUsadoEl() { return usadoEl; }
    public void setUsadoEl(LocalDateTime usadoEl) { this.usadoEl = usadoEl; }

    public LocalDateTime getCreadoEl() { return creadoEl; }
    public void setCreadoEl(LocalDateTime creadoEl) { this.creadoEl = creadoEl; }

    public boolean estaUsado() {
        return usadoEl != null;
    }

    public boolean estaExpirado() {
        return LocalDateTime.now().isAfter(expiraEl);
    }

    public boolean esValido() {
        return !estaUsado() && !estaExpirado();
    }
}