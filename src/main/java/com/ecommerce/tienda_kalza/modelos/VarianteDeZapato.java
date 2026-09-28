package com.ecommerce.tienda_kalza.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "VariantesDeZapato",
    uniqueConstraints = @UniqueConstraint(name = "uq_variante_unica", columnNames = {"id_zapato", "talla", "color"}),
    indexes = @Index(name = "idx_variantes_zapato", columnList = "id_zapato"))
public class VarianteDeZapato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_variante_de_zapato")
    private Integer idVarianteDeZapato;

    @NotNull
    @Min(1)
    @Column(name = "talla", nullable = false)
    private Integer talla;

    @NotBlank
    @Size(max = 100)
    @Column(name = "color", nullable = false, length = 100)
    private String color;

    @NotNull
    @Min(0)
    @Column(name = "stock", nullable = false)
    private Integer stock = 0;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_zapato", nullable = false,
        foreignKey = @ForeignKey(name = "fk_variantes_zapato"))
    private Zapato zapato;

    @CreationTimestamp
    @Column(name = "creado_el", nullable = false, updatable = false)
    private LocalDateTime creadoEl;

    @UpdateTimestamp
    @Column(name = "actualizado_el", nullable = false)
    private LocalDateTime actualizadoEl;

    public VarianteDeZapato() {}

    public Integer getIdVarianteDeZapato() { return idVarianteDeZapato; }
    public void setIdVarianteDeZapato(Integer idVarianteDeZapato) { this.idVarianteDeZapato = idVarianteDeZapato; }

    public Integer getTalla() { return talla; }
    public void setTalla(Integer talla) { this.talla = talla; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public Zapato getZapato() { return zapato; }
    public void setZapato(Zapato zapato) { this.zapato = zapato; }

    public LocalDateTime getCreadoEl() { return creadoEl; }
    public void setCreadoEl(LocalDateTime creadoEl) { this.creadoEl = creadoEl; }

    public LocalDateTime getActualizadoEl() { return actualizadoEl; }
    public void setActualizadoEl(LocalDateTime actualizadoEl) { this.actualizadoEl = actualizadoEl; }
}