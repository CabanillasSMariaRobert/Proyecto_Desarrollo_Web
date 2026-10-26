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

    @Query("select z.idZapato as idZapato, z.estaActivo as activo, "
            + "sum(case when v.estaActivo = true and v.stock > 0 then 1 else 0 end) as variantesDisponibles "
            + "from ZapatoEntity z left join z.variantes v where z.producto.idProducto = :id "
            + "group by z.idZapato, z.estaActivo")
    Optional<PreparacionVenta> consultarPreparacionVenta(@Param("id") int idProducto);

    interface PreparacionVenta {
        int getIdZapato();
        boolean getActivo();
        long getVariantesDisponibles();
    }

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
