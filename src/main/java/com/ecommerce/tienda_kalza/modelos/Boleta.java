package com.ecommerce.tienda_kalza.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Boletas")
public class Boleta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_boleta")
    private Integer idBoleta;

    @NotBlank
    @Size(max = 150)
    @Column(name = "cliente", nullable = false, length = 150)
    private String cliente;

    @NotBlank
    @Size(min = 11, max = 11)
    @Column(name = "ruc_de_la_tienda", nullable = false, length = 11)
    private String rucDeLaTienda;

    @NotBlank
    @Size(max = 150)
    @Column(name = "pasarela_de_pago", nullable = false, length = 150)
    private String pasarelaDePago;

    @NotBlank
    @Size(max = 150)
    @Column(name = "red_de_tarjeta", nullable = false, length = 150)
    private String redDeTarjeta;

    @NotBlank
    @Size(min = 4, max = 4)
    @Column(name = "ultimos_4_digitos", nullable = false, length = 4)
    private String ultimos4Digitos;

    @NotNull
    @DecimalMin(value = "0.0")
    @Column(name = "total", nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @CreationTimestamp
    @Column(name = "creado_el", nullable = false, updatable = false)
    private LocalDateTime creadoEl;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_compra", nullable = false, unique = true,
        foreignKey = @ForeignKey(name = "fk_boletas_compra"))
    private Compra compra;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_metodo_de_pago", nullable = false,
        foreignKey = @ForeignKey(name = "fk_boletas_metodopago"))
    private MetodoDePago metodoDePago;

    public Boleta() {}

    public Integer getIdBoleta() { return idBoleta; }
    public void setIdBoleta(Integer idBoleta) { this.idBoleta = idBoleta; }

    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }

    public String getRucDeLaTienda() { return rucDeLaTienda; }
    public void setRucDeLaTienda(String rucDeLaTienda) { this.rucDeLaTienda = rucDeLaTienda; }

    public String getPasarelaDePago() { return pasarelaDePago; }
    public void setPasarelaDePago(String pasarelaDePago) { this.pasarelaDePago = pasarelaDePago; }

    public String getRedDeTarjeta() { return redDeTarjeta; }
    public void setRedDeTarjeta(String redDeTarjeta) { this.redDeTarjeta = redDeTarjeta; }

    public String getUltimos4Digitos() { return ultimos4Digitos; }
    public void setUltimos4Digitos(String ultimos4Digitos) { this.ultimos4Digitos = ultimos4Digitos; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public LocalDateTime getCreadoEl() { return creadoEl; }
    public void setCreadoEl(LocalDateTime creadoEl) { this.creadoEl = creadoEl; }

    public Compra getCompra() { return compra; }
    public void setCompra(Compra compra) { this.compra = compra; }

    public MetodoDePago getMetodoDePago() { return metodoDePago; }
    public void setMetodoDePago(MetodoDePago metodoDePago) { this.metodoDePago = metodoDePago; }
}