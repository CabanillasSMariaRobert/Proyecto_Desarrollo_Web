package com.ecommerce.tienda_kalza.adapter.repository;

import com.ecommerce.tienda_kalza.adapter.entity.ZapatoMaterialEntity;
import com.ecommerce.tienda_kalza.adapter.utils.ZapatoMaterialEntityId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ZapatoMaterialRepository extends JpaRepository<ZapatoMaterialEntity, ZapatoMaterialEntityId> {
    List<ZapatoMaterialEntity> findByZapato_IdZapato(int idZapato);

    boolean existsByMaterial_IdMaterial(int idMaterial);
}