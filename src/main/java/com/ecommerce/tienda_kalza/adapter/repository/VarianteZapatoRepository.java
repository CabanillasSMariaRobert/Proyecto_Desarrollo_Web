package com.ecommerce.tienda_kalza.adapter.repository;

import com.ecommerce.tienda_kalza.adapter.entity.VarianteZapatoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VarianteZapatoRepository extends JpaRepository<VarianteZapatoEntity, Integer> {
    @Lock(jakarta.persistence.LockModeType.PESSIMISTIC_WRITE)
    @Query("select v from VarianteZapatoEntity v where v.idVariante = :id")
    java.util.Optional<VarianteZapatoEntity> buscarParaActualizar(
            @org.springframework.data.repository.query.Param("id") int id);

    List<VarianteZapatoEntity> findByZapato_IdZapato(int idZapato);

    boolean existsByZapato_IdZapatoAndTallaAndColorIgnoreCase(int idZapato, int talla, String color);

    boolean existsByZapato_IdZapatoAndTallaAndColorIgnoreCaseAndIdVarianteNot(
            int idZapato, int talla, String color, int idVariante
    );
}
