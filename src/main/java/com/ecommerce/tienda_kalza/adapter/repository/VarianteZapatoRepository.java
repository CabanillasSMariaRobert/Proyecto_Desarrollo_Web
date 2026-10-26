package com.ecommerce.tienda_kalza.adapter.repository;

import com.ecommerce.tienda_kalza.adapter.entity.VarianteZapatoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VarianteZapatoRepository extends JpaRepository<VarianteZapatoEntity, Integer> {
    List<VarianteZapatoEntity> findByZapato_IdZapato(int idZapato);

    boolean existsByZapato_IdZapatoAndTallaAndColorIgnoreCase(int idZapato, int talla, String color);

    boolean existsByZapato_IdZapatoAndTallaAndColorIgnoreCaseAndIdVarianteNot(
            int idZapato, int talla, String color, int idVariante
    );
}
