package com.ecommerce.tienda_kalza.dtos.admin;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class UsuarioAdminDTO {
    private Integer idUsuario;
    private String nombres;
    private String apellidos;
    private String correo;
    private String rol;
    private Boolean estaVerificado;
    private Boolean estaActivo;
    private Boolean estaBaneado;
    private String razonBaneo;
    private LocalDateTime creadoEl;
    private Integer totalCompras;
    private BigDecimal totalGastado;
}