package com.ecommerce.tienda_kalza.adapter.repository;

import com.ecommerce.tienda_kalza.adapter.entity.ProductoEntity;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ProductoRepository extends JpaRepository<ProductoEntity, Integer> {
    Page<ProductoEntity> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);

    Page<ProductoEntity> findByEstaActivoTrueAndNombreContainingIgnoreCase(String nombre, Pageable pageable);

    Optional<ProductoEntity> findByIdProductoAndEstaActivoTrue(int idProducto);

    // Proyección escalar: consulta el estado actual, incluso si el carrito conserva entidades antiguas.
    @Query("select v.idVariante as idVariante, p.nombre as nombreProducto, "
            + "case when p.estaActivo = true and z.estaActivo = true and v.estaActivo = true "
            + "then true else false end as estaActivo "
            + "from VarianteZapatoEntity v join v.zapato z join z.producto p where v.idVariante in :ids")
    List<DisponibilidadVariante> consultarDisponibilidad(@Param("ids") Collection<Integer> ids);

    interface DisponibilidadVariante {
        Integer getIdVariante();
        String getNombreProducto();
        boolean getEstaActivo();
    }
}
