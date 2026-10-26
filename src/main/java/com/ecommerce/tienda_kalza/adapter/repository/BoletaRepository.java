package com.ecommerce.tienda_kalza.adapter.repository;

import com.ecommerce.tienda_kalza.adapter.entity.BoletaEntity;
import org.springframework.data.jpa.repository.*;
import java.util.Optional;

public interface BoletaRepository extends JpaRepository<BoletaEntity, Integer> {
    @EntityGraph(attributePaths = {"compra", "compra.detalles"})
    Optional<BoletaEntity> findByCompra_IdCompraAndCompra_Cliente_IdUsuario(int idCompra, int idUsuario);
}
