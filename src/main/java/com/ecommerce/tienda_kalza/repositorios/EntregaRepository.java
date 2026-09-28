package com.ecommerce.tienda_kalza.repositorios;

import com.ecommerce.tienda_kalza.modelos.Entrega;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EntregaRepository extends JpaRepository<Entrega, Integer> {
    Optional<Entrega> findByCompraIdCompra(Integer idCompra);
    List<Entrega> findByEstado(String estado);
    List<Entrega> findByPuntoDeEntregaIdPuntoDeEntrega(Integer idPuntoDeEntrega);
    boolean existsByCompraIdCompra(Integer idCompra);
}