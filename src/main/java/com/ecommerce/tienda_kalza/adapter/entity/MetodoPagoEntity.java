package com.ecommerce.tienda_kalza.adapter.entity;

import com.ecommerce.tienda_kalza.adapter.enums.RedUsada;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table (
        name = "'Metodos_de_pago'",
        uniqueConstraints = {
                @UniqueConstraint(name = "UQ_METODOSPAGO_TOKEN", columnNames = {"pasarela", "token"})
        },
        indexes = {
                @Index(name = "IDX_METODOSPAGO_USUARIO", columnList = "id_usuario")
        }
)
@Getter
@Setter
@ToString (exclude = {"token","usuario"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MetodoPagoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "id_metodo_de_pago",nullable = false,updatable = false)
    private int idMetodoDePago;

    // Relacion con (Entidad a la que llama): Columna
    @ManyToOne (fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "id_usuario",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_METODOSPAGO_USUARIO")
    )
    private UsuarioEntity usuario;

    @Column (name = "pasarela", length = 50, nullable = false)
    private String pasarela;

    @Enumerated (EnumType.STRING)
    @Column (name = "red_usada", length = 15, nullable = false)
    private RedUsada redUsada;

    @Column (name = "token", length = 150, nullable = false)
    private String token;

    @Column (name = "ultimos_4_digitos", length = 4, nullable = false)
    private String ultimos4Digitos;

    @Column(name = "expira_el_mes", nullable = false)
    private int expira_el_mes;

    @Column(name = "expira_el_anio", nullable = false)
    private int expira_el_anio;

    @CreationTimestamp
    @Column(name = "creado_el", nullable = false, updatable = false)
    private LocalDateTime creado_el;
}
