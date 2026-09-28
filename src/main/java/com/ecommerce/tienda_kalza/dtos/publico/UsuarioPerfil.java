package com.ecommerce.tienda_kalza.dtos.publico;

/** Datos personales del perfil. Los inputs los rellena la vista con th:value. */
public record UsuarioPerfil(String nombre, String apellido, String correo, String telefono) {
}
