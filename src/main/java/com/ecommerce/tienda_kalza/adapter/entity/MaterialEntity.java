package com.ecommerce.tienda_kalza.adapter.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "\"Materiales\"")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MaterialEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_material")
    private int idMaterial;

    @Column(name = "nombre", nullable = false, unique = true, length = 150)
    private String nombre;

    // Relación con ZapatoMaterial: Referencial
    @OneToMany(mappedBy = "material", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ZapatoMaterialEntity> materiales;
}
