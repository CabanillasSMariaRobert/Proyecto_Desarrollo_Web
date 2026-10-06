package com.ecommerce.tienda_kalza.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "MetodosDePago")
@Getter
@Setter
@NoArgsConstructor
public class MetodoDePago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_metodo_de_pago")
    private Integer idMetodoDePago;

    @NotBlank
    @Size(max = 150)
    @Column(name = "token", nullable = false, length = 150)
    private String token;

    @NotBlank
    @Size(max = 50)
    @Column(name = "pasarela", nullable = false, length = 50)
    private String pasarela;

    @NotBlank
    @Size(max = 15)
    @Column(name = "red_usada", nullable = false, length = 15)
    private String redUsada;

    @NotBlank
    @Size(min = 4, max = 4)
    @Column(name = "ultimos_4_digitos", nullable = false, length = 4)
    private String ultimos4Digitos;

    @NotNull
    @Min(1)
    @Max(12)
    @Column(name = "expira_el_mes", nullable = false)
    private Integer expiraElMes;

    @NotNull
    @Min(2024)
    @Column(name = "expira_el_anio", nullable = false)
    private Integer expiraElAnio;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario", nullable = false,
        foreignKey = @ForeignKey(name = "fk_metodospago_usuario"))
    private Usuario usuario;

    @CreationTimestamp
    @Column(name = "creado_el", nullable = false, updatable = false)
    private LocalDateTime creadoEl;

    public boolean estaExpirado() {
        LocalDateTime now = LocalDateTime.now();
        return expiraElAnio < now.getYear() || 
               (expiraElAnio == now.getYear() && expiraElMes < now.getMonthValue());
    }
}