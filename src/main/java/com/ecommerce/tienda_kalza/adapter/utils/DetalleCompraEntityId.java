package com.ecommerce.tienda_kalza.adapter.utils;

// Autor: Samuel

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

@Embeddable
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode
public class DetalleCompraEntityId implements Serializable {
    @Column(name = "id_compra")
    private Integer idCompra;

    @Column(name = "id_variante_de_zapato")
    private Integer idVariante;
}
