package com.ecommerce.tienda_kalza.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "MetodosDePago")
public class MetodoDePago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_metodo_de_pago")
    private Integer idMetodoDePago;

    @NotBlank
    @Size(max = 150)
    @Column(name = "token", nullable = false, length = 150)
    private String token;

    @NotBlank
    @Size(max = 50)
    @Column(name = "pasarela", nullable = false, length = 50)
    private String pasarela;

    @NotBlank
    @Size(max = 15)
    @Column(name = "red_usada", nullable = false, length = 15)
    private String redUsada;

    @NotBlank
    @Size(min = 4, max = 4)
    @Column(name = "ultimos_4_digitos", nullable = false, length = 4)
    private String ultimos4Digitos;

    @NotNull
    @Min(1)
    @Max(12)
    @Column(name = "expira_el_mes", nullable = false)
    private Integer expiraElMes;

    @NotNull
    @Min(2024)
    @Column(name = "expira_el_anio", nullable = false)
    private Integer expiraElAnio;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario", nullable = false,
        foreignKey = @ForeignKey(name = "fk_metodospago_usuario"))
    private Usuario usuario;

    @CreationTimestamp
    @Column(name = "creado_el", nullable = false, updatable = false)
    private LocalDateTime creadoEl;

    public MetodoDePago() {}

    public Integer getIdMetodoDePago() { return idMetodoDePago; }
    public void setIdMetodoDePago(Integer idMetodoDePago) { this.idMetodoDePago = idMetodoDePago; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getPasarela() { return pasarela; }
    public void setPasarela(String pasarela) { this.pasarela = pasarela; }

    public String getRedUsada() { return redUsada; }
    public void setRedUsada(String redUsada) { this.redUsada = redUsada; }

    public String getUltimos4Digitos() { return ultimos4Digitos; }
    public void setUltimos4Digitos(String ultimos4Digitos) { this.ultimos4Digitos = ultimos4Digitos; }

    public Integer getExpiraElMes() { return expiraElMes; }
    public void setExpiraElMes(Integer expiraElMes) { this.expiraElMes = expiraElMes; }

    public Integer getExpiraElAnio() { return expiraElAnio; }
    public void setExpiraElAnio(Integer expiraElAnio) { this.expiraElAnio = expiraElAnio; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public LocalDateTime getCreadoEl() { return creadoEl; }
    public void setCreadoEl(LocalDateTime creadoEl) { this.creadoEl = creadoEl; }

    public boolean estaExpirado() {
        LocalDateTime now = LocalDateTime.now();
        return expiraElAnio < now.getYear() || 
               (expiraElAnio == now.getYear() && expiraElMes < now.getMonthValue());
    }
}