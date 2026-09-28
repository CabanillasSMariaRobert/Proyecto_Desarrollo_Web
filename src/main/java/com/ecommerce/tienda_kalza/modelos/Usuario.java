package com.ecommerce.tienda_kalza.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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

    public Usuario() {}

    public Usuario(Integer idUsuario, String nombres, String apellidos, String correo, String clave, String rol, Boolean estaVerificado, Boolean estaActivo, Boolean estaBaneado, String razonDeBaneo, LocalDateTime creadoEl, List<TokenDeUnSoloUso> tokens, List<Compra> compras, List<MetodoDePago> metodosDePago) {
        this.idUsuario = idUsuario;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.correo = correo;
        this.clave = clave;
        this.rol = rol;
        this.estaVerificado = estaVerificado;
        this.estaActivo = estaActivo;
        this.estaBaneado = estaBaneado;
        this.razonDeBaneo = razonDeBaneo;
        this.creadoEl = creadoEl;
        this.tokens = tokens;
        this.compras = compras;
        this.metodosDePago = metodosDePago;
    }

    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getClave() { return clave; }
    public void setClave(String clave) { this.clave = clave; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public Boolean getEstaVerificado() { return estaVerificado; }
    public void setEstaVerificado(Boolean estaVerificado) { this.estaVerificado = estaVerificado; }

    public Boolean getEstaActivo() { return estaActivo; }
    public void setEstaActivo(Boolean estaActivo) { this.estaActivo = estaActivo; }

    public Boolean getEstaBaneado() { return estaBaneado; }
    public void setEstaBaneado(Boolean estaBaneado) { this.estaBaneado = estaBaneado; }

    public String getRazonDeBaneo() { return razonDeBaneo; }
    public void setRazonDeBaneo(String razonDeBaneo) { this.razonDeBaneo = razonDeBaneo; }

    public LocalDateTime getCreadoEl() { return creadoEl; }
    public void setCreadoEl(LocalDateTime creadoEl) { this.creadoEl = creadoEl; }

    public List<TokenDeUnSoloUso> getTokens() { return tokens; }
    public void setTokens(List<TokenDeUnSoloUso> tokens) { this.tokens = tokens; }

    public List<Compra> getCompras() { return compras; }
    public void setCompras(List<Compra> compras) { this.compras = compras; }

    public List<MetodoDePago> getMetodosDePago() { return metodosDePago; }
    public void setMetodosDePago(List<MetodoDePago> metodosDePago) { this.metodosDePago = metodosDePago; }

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
        return estaActivo && estaVerificado;
    }

    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }
}