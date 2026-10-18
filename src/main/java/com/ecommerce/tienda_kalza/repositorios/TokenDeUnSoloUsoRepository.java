package com.ecommerce.tienda_kalza.repositorios;

import com.ecommerce.tienda_kalza.modelos.TokenDeUnSoloUso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TokenDeUnSoloUsoRepository extends JpaRepository<TokenDeUnSoloUso, Integer> {

    Optional<TokenDeUnSoloUso> findByToken(String token);

    @Query("SELECT t FROM TokenDeUnSoloUso t WHERE t.usuario.idUsuario = :usuarioId AND t.tipo = :tipo")
    List<TokenDeUnSoloUso> findByUsuarioIdAndTipo(@Param("usuarioId") Integer usuarioId, @Param("tipo") String tipo);

    @Query("SELECT t FROM TokenDeUnSoloUso t WHERE t.token = :token AND t.tipo = :tipo")
    Optional<TokenDeUnSoloUso> findByTokenAndTipo(@Param("token") String token, @Param("tipo") String tipo);

    @Query("DELETE FROM TokenDeUnSoloUso t WHERE t.expiraEl < :now")
    void deleteExpiredTokens(@Param("now") LocalDateTime now);

    @Query("DELETE FROM TokenDeUnSoloUso t WHERE t.usuario.idUsuario = :usuarioId AND t.tipo = :tipo")
    void deleteByUsuarioIdAndTipo(@Param("usuarioId") Integer usuarioId, @Param("tipo") String tipo);
}