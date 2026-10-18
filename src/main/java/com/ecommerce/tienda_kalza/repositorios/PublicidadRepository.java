package com.ecommerce.tienda_kalza.repositorios;

import com.ecommerce.tienda_kalza.modelos.Publicidad;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublicidadRepository extends JpaRepository<Publicidad, Integer> {
    List<Publicidad> findByEstaActivoTrueOrderByCreadoElDesc();
    Page<Publicidad> findByEstaActivoTrue(Pageable pageable);
    List<Publicidad> findByEstaActivoFalse();
}