package com.ecommerce.tienda_kalza.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "TokensDeUnSoloUso",
    uniqueConstraints = @UniqueConstraint(name = "uk_tokens_token", columnNames = "token"),
    indexes = @Index(name = "idx_tokens_usuario", columnList = "id_usuario"))
@Getter
@Setter
@NoArgsConstructor
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