package com.ecommerce.tienda_kalza.adapter.model;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Marca {
    private int idMarca;
    @NotBlank(message = "El nombre es obligatorio.")
    @Size(max = 150, message = "El nombre admite hasta 150 caracteres.")
    private String nombre;
    @Size(max = 500, message = "La descripción admite hasta 500 caracteres.")
    private String descripcion;
}
