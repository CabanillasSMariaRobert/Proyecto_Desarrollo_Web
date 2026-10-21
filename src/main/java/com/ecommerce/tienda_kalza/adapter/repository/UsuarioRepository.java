package com.ecommerce.tienda_kalza.adapter.repository;

import com.ecommerce.tienda_kalza.adapter.entity.UsuarioEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Integer> {

    boolean existsByCorreoIgnoreCase(String correo);

    boolean existsByCorreoIgnoreCaseAndIdUsuarioNot(String correo, Integer idUsuario);

    @Query("SELECT u FROM UsuarioEntity u WHERE " +
            "LOWER(u.nombres) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
            "LOWER(u.apellidos) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
            "LOWER(u.correo) LIKE LOWER(CONCAT('%', :filtro, '%'))")
    Page<UsuarioEntity> buscarPorFiltro(@Param("filtro") String filtro, Pageable pageable);
}