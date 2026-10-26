package com.ecommerce.tienda_kalza.adapter.repository;

import com.ecommerce.tienda_kalza.adapter.entity.MetodoPagoEntity;
import com.ecommerce.tienda_kalza.adapter.enums.RedUsada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MetodoPagoRepository extends JpaRepository<MetodoPagoEntity, Integer> {

    // Listar las tarjetas guardadas de un usuario
    List<MetodoPagoEntity> findByUsuario_IdUsuario(Integer idUsuario);

    // Buscar una tarjeta específica garantizando que pertenezca al usuario
    Optional<MetodoPagoEntity> findByIdMetodoDePagoAndUsuario_IdUsuario(Integer idMetodoDePago, Integer idUsuario);

    // Evitar registrar dos veces la misma tarjeta para un mismo usuario
    boolean existsByUsuario_IdUsuarioAndUltimos4DigitosAndRedUsada(Integer idUsuario, String ultimos4Digitos, RedUsada redUsada);
}