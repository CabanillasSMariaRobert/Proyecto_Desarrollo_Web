package com.ecommerce.tienda_kalza.adapter.repository;

import com.ecommerce.tienda_kalza.adapter.entity.MarcaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarcaRepository extends JpaRepository<MarcaEntity, Integer> {
    boolean existsByNombreIgnoreCaseAndIdMarcaNot(String nombre, int idMarca);
}
