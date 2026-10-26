package com.ecommerce.tienda_kalza.adapter.repository;

import com.ecommerce.tienda_kalza.adapter.entity.ZapatoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ZapatoRepository extends JpaRepository<ZapatoEntity, Integer> {
    java.util.Optional<ZapatoEntity> findByProducto_IdProducto(int idProducto);

    @org.springframework.data.jpa.repository.Query("select z from ZapatoEntity z join z.producto p "
            + "where z.estaActivo = true and p.estaActivo = true "
            + "and lower(p.nombre) like lower(concat('%', :buscar, '%'))")
    org.springframework.data.domain.Page<ZapatoEntity> buscarPublicos(
            @org.springframework.data.repository.query.Param("buscar") String buscar,
            org.springframework.data.domain.Pageable pageable);
    boolean existsByProducto_IdProducto(int idProducto);
    boolean existsByProducto_IdProductoAndIdZapatoNot(int idProducto, int idZapato);
}
