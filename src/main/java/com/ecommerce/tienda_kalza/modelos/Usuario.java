package com.ecommerce.tienda_kalza.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "Usuarios",
    uniqueConstraints = @UniqueConstraint(name = "uk_usuarios_correo", columnNames = "correo"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @NotBlank
    @Size(max = 150)
    @Column(name = "nombres", nullable = false, length = 150)
    private String nombres;

    @NotBlank
    @Size(max = 150)
    @Column(name = "apellidos", nullable = false, length = 150)
    private String apellidos;

    @NotBlank
    @Email
    @Size(max = 255)
    @Column(name = "correo", nullable = false, unique = true, length = 255)
    private String correo;

    @NotBlank
    @Size(max = 255)
    @Column(name = "clave", nullable = false, length = 255)
    private String clave;

    @NotBlank
    @Size(max = 30)
    @Column(name = "rol", nullable = false, length = 30)
    private String rol = "cliente";

    @Column(name = "esta_verificado", nullable = false)
    private Boolean estaVerificado = false;

    @Column(name = "esta_activo", nullable = false)
    private Boolean estaActivo = true;

    @Column(name = "esta_baneado", nullable = false)
    private Boolean estaBaneado = false;

    @Size(max = 360)
    @Column(name = "razon_de_baneo", length = 360)
    private String razonDeBaneo;

    @CreationTimestamp
    @Column(name = "creado_el", nullable = false, updatable = false)
    private LocalDateTime creadoEl;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<TokenDeUnSoloUso> tokens = List.of();

    @OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY)
    private List<Compra> compras = List.of();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<MetodoDePago> metodosDePago = List.of();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + rol.toUpperCase()));
    }

    @Override
    public String getPassword() {
        return clave;
    }

    @Override
    public String getUsername() {
        return correo;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !estaBaneado;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return estaActivo;
    }

    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }
}