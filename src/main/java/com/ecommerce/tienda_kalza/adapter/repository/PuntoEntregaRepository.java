package com.ecommerce.tienda_kalza.adapter.repository;

import com.ecommerce.tienda_kalza.adapter.entity.PuntoEntregaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PuntoEntregaRepository extends JpaRepository<PuntoEntregaEntity, Integer> {
    List<PuntoEntregaEntity> findByEstaActivoTrueOrderByNombreAsc();
}
