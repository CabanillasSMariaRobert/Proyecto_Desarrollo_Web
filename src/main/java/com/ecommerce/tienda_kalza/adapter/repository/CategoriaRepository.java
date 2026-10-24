package com.ecommerce.tienda_kalza.adapter.repository;

import com.ecommerce.tienda_kalza.adapter.entity.CategoriaEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface CategoriaRepository extends JpaRepository<CategoriaEntity, Integer> {
    boolean existsByNombreIgnoreCase(String nombre);
    boolean existsByNombreIgnoreCaseAndIdCategoriaNot(String nombre, int idCategoria);

    @Query("SELECT COUNT(rel) FROM CategoriaProductoEntity rel WHERE rel.categoria.idCategoria = :id")
    Integer contarProductos(@Param("id") Integer id);
}
