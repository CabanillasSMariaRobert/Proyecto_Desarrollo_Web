package com.ecommerce.tienda_kalza.adapter.repository;

import com.ecommerce.tienda_kalza.adapter.entity.EntregaEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EntregasRepository extends JpaRepository<EntregaEntity, Integer> {
    @Override
    @EntityGraph(attributePaths = {"compra.cliente", "compra.detalles", "puntoDeEntrega"})
    List<EntregaEntity> findAll();

    @EntityGraph(attributePaths = {"compra.cliente", "compra.detalles", "puntoDeEntrega"})
    List<EntregaEntity> findByCompra_Cliente_IdUsuario(int idUsuario);
}
