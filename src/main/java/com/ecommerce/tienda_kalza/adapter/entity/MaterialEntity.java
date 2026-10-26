package com.ecommerce.tienda_kalza.adapter.entity;

import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.*;

import java.util.List;
import java.util.ArrayList;

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

    @NotBlank(message = "El nombre del material es obligatorio")
    @Size(max = 150, message = "El nombre no puede superar los 150 caracteres")
    @Column(name = "nombre", nullable = false, unique = true, length = 150)
    private String nombre;

    // Relación con ZapatoMaterial: Referencial
    @OneToMany(mappedBy = "material", fetch = FetchType.LAZY)
    private List<ZapatoMaterialEntity> materiales = new ArrayList<>();
}
