package com.ecommerce.tienda_kalza.adapter.repository;

// Autor: Samuel

import com.ecommerce.tienda_kalza.adapter.entity.CompraEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.QueryHint;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompraRepository extends JpaRepository<CompraEntity, Integer> {
    List<CompraEntity> findByEstado(String estado);

    // Lee el historial persistido antes de evaluar nuevas unidades, sin volcar cambios del formulario.
    @Query("select c.estado as estado, v.idVariante as idVariante, d.cantidad as cantidad "
            + "from CompraEntity c left join c.detalles d left join d.varianteZapato v where c.idCompra = :id")
    @QueryHints(@QueryHint(name = "org.hibernate.flushMode", value = "COMMIT"))
    List<DetalleRn01> consultarDetallesRn01(@Param("id") int id);

    interface DetalleRn01 {
        String getEstado();
        Integer getIdVariante();
        Integer getCantidad();
    }
}
