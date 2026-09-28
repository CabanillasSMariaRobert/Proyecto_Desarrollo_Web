package com.ecommerce.tienda_kalza.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Compras",
    indexes = @Index(name = "idx_compras_cliente", columnList = "id_cliente"))
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_compra")
    private Integer idCompra;

    @NotBlank
    @Size(max = 30)
    @Column(name = "estado", nullable = false, length = 30)
    private String estado = "pendiente";

    @NotNull
    @DecimalMin(value = "0.0")
    @Column(name = "total_con_descuento", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalConDescuento;

    @NotNull
    @DecimalMin(value = "0.0")
    @Column(name = "total_sin_descuento", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalSinDescuento;

    @CreationTimestamp
    @Column(name = "fecha_de_compra", nullable = false, updatable = false)
    private LocalDateTime fechaDeCompra;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_cliente", nullable = false,
        foreignKey = @ForeignKey(name = "fk_compras_cliente"))
    private Usuario cliente;

    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<DetalleDeCompra> detalles = new ArrayList<>();

    @OneToOne(mappedBy = "compra", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Boleta boleta;

    @OneToOne(mappedBy = "compra", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Entrega entrega;

    public Compra() {}

    public Integer getIdCompra() { return idCompra; }
    public void setIdCompra(Integer idCompra) { this.idCompra = idCompra; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public BigDecimal getTotalConDescuento() { return totalConDescuento; }
    public void setTotalConDescuento(BigDecimal totalConDescuento) { this.totalConDescuento = totalConDescuento; }

    public BigDecimal getTotalSinDescuento() { return totalSinDescuento; }
    public void setTotalSinDescuento(BigDecimal totalSinDescuento) { this.totalSinDescuento = totalSinDescuento; }

    public LocalDateTime getFechaDeCompra() { return fechaDeCompra; }
    public void setFechaDeCompra(LocalDateTime fechaDeCompra) { this.fechaDeCompra = fechaDeCompra; }

    public Usuario getCliente() { return cliente; }
    public void setCliente(Usuario cliente) { this.cliente = cliente; }

    public List<DetalleDeCompra> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleDeCompra> detalles) { this.detalles = detalles; }

    public Boleta getBoleta() { return boleta; }
    public void setBoleta(Boleta boleta) { this.boleta = boleta; }

    public Entrega getEntrega() { return entrega; }
    public void setEntrega(Entrega entrega) { this.entrega = entrega; }
}