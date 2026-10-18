package com.ecommerce.tienda_kalza.adapter.entity;
import com.ecommerce.tienda_kalza.adapter.utils.ZapatoMaterialEntityId;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table (name = "\"Zapato_materiales\"")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ZapatoMaterialEntity {
    @EmbeddedId
    @Builder.Default
    private ZapatoMaterialEntityId id = new ZapatoMaterialEntityId();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("idZapato")
    @JoinColumn(name = "id_zapato")
    private ZapatoEntity zapato;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("idMaterial")
    @JoinColumn(name = "id_material")
    private MaterialEntity material;
}
