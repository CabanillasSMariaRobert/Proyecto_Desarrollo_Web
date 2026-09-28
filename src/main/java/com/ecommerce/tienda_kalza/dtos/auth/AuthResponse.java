package com.ecommerce.tienda_kalza.dtos.auth;

import com.ecommerce.tienda_kalza.modelos.Usuario;

public class AuthResponse {

    private String token;
    private String tipoToken = "Bearer";
    private Long expiraEn;
    private UsuarioDTO usuario;

    public AuthResponse() {}

    public AuthResponse(String token, String tipoToken, Long expiraEn, UsuarioDTO usuario) {
        this.token = token;
        this.tipoToken = tipoToken;
        this.expiraEn = expiraEn;
        this.usuario = usuario;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getTipoToken() { return tipoToken; }
    public void setTipoToken(String tipoToken) { this.tipoToken = tipoToken; }

    public Long getExpiraEn() { return expiraEn; }
    public void setExpiraEn(Long expiraEn) { this.expiraEn = expiraEn; }

    public UsuarioDTO getUsuario() { return usuario; }
    public void setUsuario(UsuarioDTO usuario) { this.usuario = usuario; }

    public static class UsuarioDTO {
        private Integer idUsuario;
        private String nombres;
        private String apellidos;
        private String correo;
        private String rol;
        private Boolean estaVerificado;

        public UsuarioDTO() {}

        public UsuarioDTO(Integer idUsuario, String nombres, String apellidos, String correo, String rol, Boolean estaVerificado) {
            this.idUsuario = idUsuario;
            this.nombres = nombres;
            this.apellidos = apellidos;
            this.correo = correo;
            this.rol = rol;
            this.estaVerificado = estaVerificado;
        }

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

        public Integer getIdUsuario() { return idUsuario; }
        public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }

        public String getNombres() { return nombres; }
        public void setNombres(String nombres) { this.nombres = nombres; }

        public String getApellidos() { return apellidos; }
        public void setApellidos(String apellidos) { this.apellidos = apellidos; }

        public String getCorreo() { return correo; }
        public void setCorreo(String correo) { this.correo = correo; }

        public String getRol() { return rol; }
        public void setRol(String rol) { this.rol = rol; }

        public Boolean getEstaVerificado() { return estaVerificado; }
        public void setEstaVerificado(Boolean estaVerificado) { this.estaVerificado = estaVerificado; }
    }
}