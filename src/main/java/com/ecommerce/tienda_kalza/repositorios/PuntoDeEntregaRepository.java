package com.ecommerce.tienda_kalza.repositorios;

import com.ecommerce.tienda_kalza.modelos.PuntoDeEntrega;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PuntoDeEntregaRepository extends JpaRepository<PuntoDeEntrega, Integer> {
    List<PuntoDeEntrega> findByIdDistrito(Integer idDistrito);
}