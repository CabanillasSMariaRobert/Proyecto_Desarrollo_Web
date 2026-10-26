package com.ecommerce.tienda_kalza.adapter.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginForm {
    private String correo;
    private String clave;
    private Boolean recordar = false;
}