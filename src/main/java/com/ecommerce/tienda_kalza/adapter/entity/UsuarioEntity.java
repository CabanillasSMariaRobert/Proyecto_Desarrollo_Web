package com.ecommerce.tienda_kalza.adapter.entity;

import com.ecommerce.tienda_kalza.adapter.enums.RolUsuario;
import com.ecommerce.tienda_kalza.adapter.enums.TipoDocumento;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "`Usuarios`",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_USUARIOS_CORREO", columnNames = {"correo"}),
                @UniqueConstraint(name = "UQ_USUARIOS_DOCUMENTO", columnNames = {"tipo_de_documento", "numero_de_documento"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "id_usuario", nullable = false, updatable = false)
    private Integer idUsuario;

    @Column (name = "nombres", length = 150, nullable = false)
    private String nombres;

    @Column (name = "apellidos", length = 150, nullable = false)
    private String apellidos;

    @Column (name = "correo", length = 255, nullable = false, unique = true)
    private String correo;

    @Column (name = "clave", length = 255, nullable = false)
    private String clave;

    @Enumerated (EnumType.STRING)
    @Column (name = "rol", nullable = false)
    @Builder.Default
    private RolUsuario rol = RolUsuario.COMUN;

    @Column (name = "telefono", length = 15)
    private String telefono;

    @Enumerated(EnumType.STRING)
    @Column (name = "tipo_de_documento",nullable = false)
    @Builder.Default
    private TipoDocumento tipoDeDocumento = TipoDocumento.DNI;

    @Column (name = "numero_de_documento", length = 20)
    private String numeroDeDocumento;

    @Column (name = "esta_activo", nullable = false)
    @Builder.Default
    private Boolean estaActivo = true;

    @Column (name = "esta_verificado", nullable = false)
    @Builder.Default
    private Boolean estaVerificado = false;

    @Column (name = "esta_baneado", nullable = false)
    @Builder.Default
    private  Boolean estaBaneado = false;

    @Column (name = "razon_de_baneo", length = 360)
    private String razonDeBaneo;

    @CreationTimestamp
    @Column (name = "creado_el", nullable = false, updatable = false)
    private LocalDateTime creadoEl;
}
