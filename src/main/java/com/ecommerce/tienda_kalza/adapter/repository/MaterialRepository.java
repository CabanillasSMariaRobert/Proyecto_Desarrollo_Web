package com.ecommerce.tienda_kalza.adapter.repository;

import com.ecommerce.tienda_kalza.adapter.entity.MaterialEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialRepository extends JpaRepository<MaterialEntity, Integer> {
    boolean existsByNombreIgnoreCase(String name);
    boolean existsByNombreIgnoreCaseAndIdMaterialNot(String nombre, int idMaterial);
}