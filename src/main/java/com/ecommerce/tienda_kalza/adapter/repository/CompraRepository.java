package com.ecommerce.tienda_kalza.adapter.repository;

// Autor: Samuel

import com.ecommerce.tienda_kalza.adapter.entity.CompraEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompraRepository extends JpaRepository<CompraEntity, Integer> {
    List<CompraEntity> findByEstado(String estado);
}
