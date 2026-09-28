package com.ecommerce.tienda_kalza.dto.publico;

/** Datos personales del perfil. Los inputs los rellena la vista con th:value. */
public record UsuarioPerfil(String nombre, String apellido, String correo, String telefono) {
}
