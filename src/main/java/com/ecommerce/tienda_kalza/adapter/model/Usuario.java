package com.ecommerce.tienda_kalza.adapter.model;

import com.ecommerce.tienda_kalza.adapter.enums.RolUsuario;
import com.ecommerce.tienda_kalza.adapter.enums.TipoDocumento;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {
    private int idUsuario;
    private String nombres;
    private String apellidos;
    private String correo;
    private String clave;

    @Builder.Default
    private RolUsuario rol = RolUsuario.COMUN;

    private String telefono;
    private TipoDocumento tipo_de_documento;
    private String numeroDeDocumento;

    @Builder.Default
    private Boolean estaActivo = true;

    @Builder.Default
    private Boolean estaVerificado = false;

    @Builder.Default
    private Boolean estaBaneado = false;

    private String razonDeBaneo;

    @CreationTimestamp
    private LocalDateTime creadoEl = LocalDateTime.now();
}
