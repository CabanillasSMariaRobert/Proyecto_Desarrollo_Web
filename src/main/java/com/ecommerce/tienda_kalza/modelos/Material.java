package com.ecommerce.tienda_kalza.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Materiales")
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_material")
    private Integer idMaterial;

    @NotBlank
    @Size(max = 150)
    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    public Material() {}

    public Material(Integer idMaterial, String nombre) {
        this.idMaterial = idMaterial;
        this.nombre = nombre;
    }

    public Integer getIdMaterial() { return idMaterial; }
    public void setIdMaterial(Integer idMaterial) { this.idMaterial = idMaterial; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}