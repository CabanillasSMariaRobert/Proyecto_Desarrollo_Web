package com.ecommerce.tienda_kalza.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Zapatos")
public class Zapato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_zapato")
    private Integer idZapato;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_producto", nullable = false, unique = true,
        foreignKey = @ForeignKey(name = "fk_zapatos_producto"))
    private Producto producto;

    @OneToMany(mappedBy = "zapato", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<VarianteDeZapato> variantes = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "ZapatoMateriales",
        joinColumns = @JoinColumn(name = "id_zapato", foreignKey = @ForeignKey(name = "fk_zapmat_zapato")),
        inverseJoinColumns = @JoinColumn(name = "id_material", foreignKey = @ForeignKey(name = "fk_zapmat_material"))
    )
    private Set<Material> materiales = new HashSet<>();

    @CreationTimestamp
    @Column(name = "creado_el", nullable = false, updatable = false)
    private LocalDateTime creadoEl;

    @UpdateTimestamp
    @Column(name = "actualizado_el", nullable = false)
    private LocalDateTime actualizadoEl;

    public Zapato() {}

    public Integer getIdZapato() { return idZapato; }
    public void setIdZapato(Integer idZapato) { this.idZapato = idZapato; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    public Set<VarianteDeZapato> getVariantes() { return variantes; }
    public void setVariantes(Set<VarianteDeZapato> variantes) { this.variantes = variantes; }

    public Set<Material> getMateriales() { return materiales; }
    public void setMateriales(Set<Material> materiales) { this.materiales = materiales; }

    public LocalDateTime getCreadoEl() { return creadoEl; }
    public void setCreadoEl(LocalDateTime creadoEl) { this.creadoEl = creadoEl; }

    public LocalDateTime getActualizadoEl() { return actualizadoEl; }
    public void setActualizadoEl(LocalDateTime actualizadoEl) { this.actualizadoEl = actualizadoEl; }
}