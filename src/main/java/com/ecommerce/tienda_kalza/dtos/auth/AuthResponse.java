package com.ecommerce.tienda_kalza.dtos.auth;

import com.ecommerce.tienda_kalza.modelos.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private String token;
    private String tipoToken = "Bearer";
    private Long expiraEn;
    private UsuarioDTO usuario;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UsuarioDTO {
        private Integer idUsuario;
        private String nombres;
        private String apellidos;
        private String correo;
        private String rol;
        private Boolean estaVerificado;

        public static UsuarioDTO fromEntity(Usuario u) {
            UsuarioDTO dto = new UsuarioDTO();
            dto.setIdUsuario(u.getIdUsuario());
            dto.setNombres(u.getNombres());
            dto.setApellidos(u.getApellidos());
            dto.setCorreo(u.getCorreo());
            dto.setRol(u.getRol());
            dto.setEstaVerificado(u.getEstaVerificado());
            return dto;
        }
    }
}