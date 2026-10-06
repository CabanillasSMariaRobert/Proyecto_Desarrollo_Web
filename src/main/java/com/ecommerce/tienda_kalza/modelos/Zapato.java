package com.ecommerce.tienda_kalza.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Zapatos")
@Getter
@Setter
@NoArgsConstructor
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
}