package com.ecommerce.tienda_kalza.repositorios;

import com.ecommerce.tienda_kalza.modelos.DetalleDeCompra;
import com.ecommerce.tienda_kalza.modelos.DetalleDeCompraId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleDeCompraRepository extends JpaRepository<DetalleDeCompra, DetalleDeCompraId> {
    List<DetalleDeCompra> findByCompraIdCompra(Integer idCompra);
    List<DetalleDeCompra> findByVarianteDeZapatoIdVarianteDeZapato(Integer idVariante);
}