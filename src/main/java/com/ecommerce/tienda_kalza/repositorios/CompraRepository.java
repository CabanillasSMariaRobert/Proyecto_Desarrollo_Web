package com.ecommerce.tienda_kalza.repositorios;

import com.ecommerce.tienda_kalza.modelos.Compra;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Integer> {

    @Query("SELECT c FROM Compra c JOIN FETCH c.cliente WHERE c.idCompra = :id")
    Optional<Compra> findByIdWithCliente(@Param("id") Integer id);

    @Query("SELECT c FROM Compra c JOIN FETCH c.cliente WHERE c.cliente.idUsuario = :clienteId ORDER BY c.fechaDeCompra DESC")
    List<Compra> findByClienteIdUsuarioOrderByFechaDeCompraDesc(@Param("clienteId") Integer clienteId);

    Page<Compra> findByClienteIdUsuario(Integer clienteId, Pageable pageable);

    @Query("SELECT c FROM Compra c JOIN FETCH c.cliente WHERE c.estado = :estado ORDER BY c.fechaDeCompra DESC")
    List<Compra> findByEstado(@Param("estado") String estado);

    @Query("SELECT c FROM Compra c JOIN FETCH c.cliente WHERE c.fechaDeCompra BETWEEN :inicio AND :fin ORDER BY c.fechaDeCompra DESC")
    List<Compra> findByFechaDeCompraBetween(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);

    long countByEstado(String estado);
}