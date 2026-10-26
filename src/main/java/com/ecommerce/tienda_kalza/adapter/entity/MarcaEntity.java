package com.ecommerce.tienda_kalza.adapter.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "\"Marcas\"")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MarcaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_marca")
    private int idMarca;

    @Column(name = "nombre", length = 150, nullable = false, unique = true)
    private String nombre;

    @Column(name = "descripcion", length = 500)
    private String descripcion;

    @Builder.Default
    @Column(name = "esta_activo", nullable = false, columnDefinition = "boolean default true")
    private boolean estaActivo = true;
}
