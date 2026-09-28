package com.ecommerce.tienda_kalza.repositorios;

import com.ecommerce.tienda_kalza.modelos.Marca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarcaRepository extends JpaRepository<Marca, Integer> {
    boolean existsByNombre(String nombre);
}