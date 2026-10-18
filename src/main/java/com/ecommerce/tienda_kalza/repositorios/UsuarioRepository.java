package com.ecommerce.tienda_kalza.repositorios;

import com.ecommerce.tienda_kalza.modelos.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByCorreo(String correo);

    boolean existsByCorreo(String correo);

    @Query("SELECT u FROM Usuario u WHERE u.rol = :rol")
    List<Usuario> findByRol(@Param("rol") String rol);

    @Query("SELECT u FROM Usuario u WHERE u.estaActivo = true AND u.estaVerificado = true")
    List<Usuario> findActiveVerifiedUsers();

    @Query("SELECT u FROM Usuario u WHERE u.estaBaneado = true")
    List<Usuario> findBannedUsers();
}