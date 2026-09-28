package com.ecommerce.tienda_kalza.repositorios;

import com.ecommerce.tienda_kalza.modelos.Zapato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ZapatoRepository extends JpaRepository<Zapato, Integer> {
    Optional<Zapato> findByProductoIdProducto(Integer idProducto);
    boolean existsByProductoIdProducto(Integer idProducto);
}