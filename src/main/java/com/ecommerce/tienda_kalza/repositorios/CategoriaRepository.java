package com.ecommerce.tienda_kalza.repositorios;

import com.ecommerce.tienda_kalza.modelos.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    boolean existsByNombre(String nombre);
}