package com.ecommerce.tienda_kalza.repositorios;

import com.ecommerce.tienda_kalza.modelos.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    @Query("SELECT p FROM Producto p JOIN FETCH p.marca LEFT JOIN FETCH p.zapato WHERE p.idProducto = :id")
    Optional<Producto> findByIdWithRelations(@Param("id") Integer id);

    @Query("SELECT p FROM Producto p JOIN FETCH p.marca LEFT JOIN FETCH p.zapato WHERE p.marca.idMarca = :marcaId")
    List<Producto> findByMarcaId(@Param("marcaId") Integer marcaId);

    @Query("SELECT p FROM Producto p JOIN FETCH p.marca LEFT JOIN FETCH p.zapato WHERE :categoriaId MEMBER OF p.categorias")
    List<Producto> findByCategoriaId(@Param("categoriaId") Integer categoriaId);

    @Query("SELECT p FROM Producto p JOIN FETCH p.marca LEFT JOIN FETCH p.zapato WHERE p.nombre ILIKE %:term%")
    List<Producto> buscarPorNombre(@Param("term") String term);

    @Query("SELECT p FROM Producto p JOIN FETCH p.marca LEFT JOIN FETCH p.zapato WHERE p.precioOriginal BETWEEN :min AND :max")
    List<Producto> findByPrecioRange(@Param("min") BigDecimal min, @Param("max") BigDecimal max);

    Page<Producto> findAll(Pageable pageable);

    boolean existsByNombre(String nombre);
}