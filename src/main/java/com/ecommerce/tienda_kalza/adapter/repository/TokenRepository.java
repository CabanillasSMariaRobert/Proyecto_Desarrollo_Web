package com.ecommerce.tienda_kalza.adapter.repository;

import com.ecommerce.tienda_kalza.adapter.entity.TokenEntity;
import com.ecommerce.tienda_kalza.adapter.entity.UsuarioEntity;
import com.ecommerce.tienda_kalza.adapter.enums.TipoToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<TokenEntity, Integer> {
    Optional<TokenEntity> findByTokenAndTipo(String token, TipoToken tipo);

    @Query("SELECT t.usuario.idUsuario FROM TokenEntity t WHERE t.token = :token AND t.tipo = :tipo")
    Optional<Integer> buscarIdUsuario(@Param("token") String token, @Param("tipo") TipoToken tipo);

    // Serializa solicitudes y cambios de clave de la misma cuenta sin alterar UsuarioRepository.
    @Query("SELECT u FROM UsuarioEntity u WHERE LOWER(u.correo) = LOWER(:correo)")
    Optional<UsuarioEntity> buscarUsuarioPorCorreo(@Param("correo") String correo);


    @Query("SELECT u FROM UsuarioEntity u WHERE u.idUsuario = :id")
    Optional<UsuarioEntity> buscarUsuarioPorId(@Param("id") int id);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
            UPDATE TokenEntity t SET t.usadoEl = :ahora
            WHERE t.usuario.idUsuario = :id AND t.tipo = :tipo AND t.usadoEl IS NULL
            """)
    int invalidarAnteriores(@Param("id") int id, @Param("tipo") TipoToken tipo,
                           @Param("ahora") LocalDateTime ahora);
}
