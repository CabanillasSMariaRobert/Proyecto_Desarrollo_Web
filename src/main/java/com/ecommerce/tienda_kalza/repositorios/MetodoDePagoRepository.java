package com.ecommerce.tienda_kalza.repositorios;

import com.ecommerce.tienda_kalza.modelos.MetodoDePago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MetodoDePagoRepository extends JpaRepository<MetodoDePago, Integer> {
    List<MetodoDePago> findByUsuarioIdUsuario(Integer idUsuario);
    boolean existsByToken(String token);
}