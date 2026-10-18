package com.ecommerce.tienda_kalza.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "Entregas")
@Getter
@Setter
@NoArgsConstructor
public class Entrega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_entrega")
    private Integer idEntrega;

    @NotBlank
    @Size(max = 30)
    @Column(name = "estado", nullable = false, length = 30)
    private String estado = "pendiente";

    @Column(name = "asignado_el")
    private LocalDateTime asignadoEl;

    @Column(name = "recogido_el")
    private LocalDateTime recogidoEl;

    @Column(name = "entregado_el")
    private LocalDateTime entregadoEl;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_compra", nullable = false, unique = true,
        foreignKey = @ForeignKey(name = "fk_entregas_compra"))
    private Compra compra;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_punto_de_entrega", nullable = false,
        foreignKey = @ForeignKey(name = "fk_entregas_punto"))
    private PuntoDeEntrega puntoDeEntrega;

    @CreationTimestamp
    @Column(name = "creado_el", nullable = false, updatable = false)
    private LocalDateTime creadoEl;

    @UpdateTimestamp
    @Column(name = "actualizado_el", nullable = false)
    private LocalDateTime actualizadoEl;
}