package com.ecommerce.tienda_kalza.adapter.repository;

import com.ecommerce.tienda_kalza.adapter.entity.CategoriaEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface CategoriaRepository extends JpaRepository<CategoriaEntity, Integer> {
    boolean existsByNombreIgnoreCase(String nombre);
    boolean existsByNombreIgnoreCaseAndIdCategoriaNot(String nombre, int idCategoria);

    @Query(value = "SELECT COUNT(*) FROM \"Categorias_de_productos\" WHERE id_categoria = :id", nativeQuery = true)
    Integer contarProductos(@Param("id") Integer id);
}