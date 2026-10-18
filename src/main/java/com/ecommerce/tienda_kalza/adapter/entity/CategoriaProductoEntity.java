package com.ecommerce.tienda_kalza.adapter.entity;

import com.ecommerce.tienda_kalza.adapter.utils.CategoriaProductoEntityId;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "\"Categorias_de_productos\"")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CategoriaProductoEntity {
    @EmbeddedId
    @Builder.Default
    private CategoriaProductoEntityId id = new CategoriaProductoEntityId();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("idProducto")
    @JoinColumn(name = "id_producto")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ProductoEntity producto;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("idCategoria")
    @JoinColumn(name = "id_categoria")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CategoriaEntity categoria;
}