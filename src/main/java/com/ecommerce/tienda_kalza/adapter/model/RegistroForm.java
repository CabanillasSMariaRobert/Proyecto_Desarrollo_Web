package com.ecommerce.tienda_kalza.adapter.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistroForm {
    private String nombreCompleto;
    private String correo;
    private String clave;
    private String confirmarClave;
}