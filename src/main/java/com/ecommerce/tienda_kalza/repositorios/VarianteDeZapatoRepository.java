package com.ecommerce.tienda_kalza.repositorios;

import com.ecommerce.tienda_kalza.modelos.VarianteDeZapato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VarianteDeZapatoRepository extends JpaRepository<VarianteDeZapato, Integer> {

    @Query("SELECT v FROM VarianteDeZapato v JOIN FETCH v.zapato z JOIN FETCH z.producto WHERE v.idVarianteDeZapato = :id")
    Optional<VarianteDeZapato> findByIdWithRelations(@Param("id") Integer id);

    List<VarianteDeZapato> findByZapatoIdZapato(Integer idZapato);

    @Query("SELECT v FROM VarianteDeZapato v WHERE v.zapato.idZapato = :zapatoId AND v.stock > 0")
    List<VarianteDeZapato> findDisponiblesByZapatoId(@Param("zapatoId") Integer zapatoId);

    @Query("SELECT v FROM VarianteDeZapato v WHERE v.zapato.idZapato = :zapatoId AND v.talla = :talla AND v.color = :color")
    Optional<VarianteDeZapato> findByZapatoIdAndTallaAndColor(
            @Param("zapatoId") Integer zapatoId,
            @Param("talla") Integer talla,
            @Param("color") String color);

    boolean existsByZapatoIdZapatoAndTallaAndColor(Integer zapatoId, Integer talla, String color);
}