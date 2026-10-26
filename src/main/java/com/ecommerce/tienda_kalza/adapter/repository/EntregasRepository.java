package com.ecommerce.tienda_kalza.adapter.repository;

import com.ecommerce.tienda_kalza.adapter.entity.EntregaEntity;
import com.ecommerce.tienda_kalza.adapter.enums.EstadoEntrega;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;

import java.util.List;
import java.util.Optional;

public interface EntregasRepository extends JpaRepository<EntregaEntity, Integer> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT e FROM EntregaEntity e WHERE e.id = :id")
    Optional<EntregaEntity> buscarParaActualizar(@Param("id") int id);

    Page<EntregaEntity> findByEstado(EstadoEntrega estado, Pageable pageable);

    Page<EntregaEntity> findByCompra_Cliente_IdUsuario(int idUsuario, Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"compra.cliente", "compra.detalles", "puntoDeEntrega"})
    List<EntregaEntity> findAll();

    @EntityGraph(attributePaths = {"compra.cliente", "compra.detalles", "puntoDeEntrega"})
    List<EntregaEntity> findByCompra_Cliente_IdUsuario(int idUsuario);
}
