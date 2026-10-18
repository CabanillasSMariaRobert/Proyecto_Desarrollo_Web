package com.ecommerce.tienda_kalza.adapter.utils;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ZapatoMaterialEntityId implements Serializable {
    @Column(name = "id_zapato")
    private Integer idZapato;

    @Column(name = "id_material")
    private Integer idMaterial;
}