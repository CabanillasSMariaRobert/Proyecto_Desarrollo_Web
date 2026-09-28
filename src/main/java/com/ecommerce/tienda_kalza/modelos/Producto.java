package com.ecommerce.tienda_kalza.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Productos", indexes = {
    @Index(name = "idx_productos_marca", columnList = "id_marca")
})
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Integer idProducto;

    @NotBlank
    @Size(max = 150)
    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Size(max = 255)
    @Column(name = "url_imagen", length = 255)
    private String urlImagen;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Column(name = "precio_original", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioOriginal;

    @DecimalMin(value = "0.0")
    @DecimalMax(value = "1.0")
    @Column(name = "descuento", precision = 3, scale = 2)
    private BigDecimal descuento = BigDecimal.ZERO;

    @DecimalMin(value = "0.0")
    @Column(name = "precio_con_descuento", precision = 10, scale = 2)
    private BigDecimal precioConDescuento;

    @CreationTimestamp
    @Column(name = "creado_el", nullable = false, updatable = false)
    private LocalDateTime creadoEl;

    @UpdateTimestamp
    @Column(name = "actualizado_el", nullable = false)
    private LocalDateTime actualizadoEl;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_marca", nullable = false,
        foreignKey = @ForeignKey(name = "fk_productos_marca"))
    private Marca marca;

    @OneToOne(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Zapato zapato;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "CategoriasDeProductos",
        joinColumns = @JoinColumn(name = "id_producto", foreignKey = @ForeignKey(name = "fk_catprod_producto")),
        inverseJoinColumns = @JoinColumn(name = "id_categoria", foreignKey = @ForeignKey(name = "fk_catprod_categoria"))
    )
    private Set<Categoria> categorias = new HashSet<>();

    public Producto() {}

    @PrePersist
    @PreUpdate
    protected void calcularPrecioConDescuento() {
        if (precioOriginal != null && descuento != null) {
            this.precioConDescuento = precioOriginal.subtract(precioOriginal.multiply(descuento));
        }
    }

    // Getters and Setters
    public Integer getIdProducto() { return idProducto; }
    public void setIdProducto(Integer idProducto) { this.idProducto = idProducto; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getUrlImagen() { return urlImagen; }
    public void setUrlImagen(String urlImagen) { this.urlImagen = urlImagen; }

    public BigDecimal getPrecioOriginal() { return precioOriginal; }
    public void setPrecioOriginal(BigDecimal precioOriginal) { this.precioOriginal = precioOriginal; }

    public BigDecimal getDescuento() { return descuento; }
    public void setDescuento(BigDecimal descuento) { this.descuento = descuento; }

    public BigDecimal getPrecioConDescuento() { return precioConDescuento; }
    public void setPrecioConDescuento(BigDecimal precioConDescuento) { this.precioConDescuento = precioConDescuento; }

    public LocalDateTime getCreadoEl() { return creadoEl; }
    public void setCreadoEl(LocalDateTime creadoEl) { this.creadoEl = creadoEl; }

    public LocalDateTime getActualizadoEl() { return actualizadoEl; }
    public void setActualizadoEl(LocalDateTime actualizadoEl) { this.actualizadoEl = actualizadoEl; }

    public Marca getMarca() { return marca; }
    public void setMarca(Marca marca) { this.marca = marca; }

    public Zapato getZapato() { return zapato; }
    public void setZapato(Zapato zapato) { this.zapato = zapato; }

    public Set<Categoria> getCategorias() { return categorias; }
    public void setCategorias(Set<Categoria> categorias) { this.categorias = categorias; }
}