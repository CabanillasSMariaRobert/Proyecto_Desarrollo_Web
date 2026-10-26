package com.ecommerce.tienda_kalza.adapter.entity;

import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "\"Variantes_de_zapato\"",
uniqueConstraints = {
        @UniqueConstraint(
                name = "UK_VARIANTE_ZAPATO_TALLA_COLOR",
                columnNames = {"id_zapato","talla","color"}
        )
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VarianteZapatoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_variante_de_zapato")
    private int idVariante;

    // Relacion con Zapato: Columna
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="id_zapato", nullable = false)
    private ZapatoEntity zapato;

    @Column(name = "talla", nullable = false)
    @Min(value = 1, message = "La talla debe ser al menos 1")
    private int talla;

    @NotBlank(message = "El color es obligatorio")
    @Size(max = 100, message = "El color no puede superar los 100 caracteres")
    @Column(name = "color", nullable = false, length = 100)
    private String color;

    @Column(name = "stock", nullable = false)
    @Min(value = 0, message = "El stock no puede ser negativo")
    private int stock;

    @CreationTimestamp
    @Column(name = "creado_el", nullable = false, updatable = false)
    private Timestamp creadoEl;

    @UpdateTimestamp
    @Column(name = "actualizado_el", nullable = false)
    private Timestamp actualizadoEl;
}
