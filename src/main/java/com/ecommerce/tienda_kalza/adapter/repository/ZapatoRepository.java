package com.ecommerce.tienda_kalza.adapter.repository;

import com.ecommerce.tienda_kalza.adapter.entity.ZapatoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ZapatoRepository extends JpaRepository<ZapatoEntity, Integer> {
    boolean existsByProducto_IdProducto(int idProducto);
    boolean existsByProducto_IdProductoAndIdZapatoNot(int idProducto, int idZapato);
}
