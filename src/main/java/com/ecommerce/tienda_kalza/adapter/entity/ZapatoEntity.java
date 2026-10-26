package com.ecommerce.tienda_kalza.adapter.entity;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "\"Zapatos\"")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ZapatoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_zapato")
    private int idZapato;

    @CreationTimestamp
    @Column(name = "creado_el", nullable = false, updatable = false)
    private LocalDate creadoEl;

    @Column(name = "esta_activo", nullable = false)
    @Builder.Default
    private boolean estaActivo = true;

    @UpdateTimestamp
    @Column(name = "actualizado_el", nullable = false)
    private LocalDate actualizadoEl;

    // Relacion con Producto: Columna
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_producto", nullable = false, unique = true)
    private ProductoEntity producto;

    // Relación con VarianteZapato: Referencial
    @OneToMany(mappedBy = "zapato", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE })
    @Builder.Default
    private List<VarianteZapatoEntity> variantes = new ArrayList<>();

    // Relación con ZapatoMaterial: Referencial
    @OneToMany(mappedBy = "zapato", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ZapatoMaterialEntity> materiales = new ArrayList<>();
}
