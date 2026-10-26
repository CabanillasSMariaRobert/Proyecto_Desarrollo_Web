package com.ecommerce.tienda_kalza.adapter.service;

import com.ecommerce.tienda_kalza.adapter.entity.DetalleCompraEntity;
import com.ecommerce.tienda_kalza.adapter.repository.VarianteZapatoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/** La salida de stock debe confirmarse o revertirse junto con la compra. */
@Service
@RequiredArgsConstructor
public class InventarioService {
    private final VarianteZapatoRepository variantes;
    private final EntityManager entityManager;

    @Transactional(propagation = Propagation.MANDATORY)
    public void descontar(List<DetalleCompraEntity> detalles) {
        if (detalles == null || detalles.isEmpty()) {
            throw new ReglaNegocioException("La compra debe contener al menos una variante.");
        }
        // El orden estable de los bloqueos reduce los conflictos entre compras concurrentes.
        Map<Integer, Integer> cantidades = new TreeMap<>();
        for (var detalle : detalles) {
            if (detalle == null || detalle.getVarianteZapato() == null || detalle.getCantidad() < 1) {
                throw new ReglaNegocioException("Las variantes y cantidades de la compra no son válidas.");
            }
            int id = detalle.getVarianteZapato().getIdVariante();
            long total = (long) cantidades.getOrDefault(id, 0) + detalle.getCantidad();
            if (id < 1 || total > Integer.MAX_VALUE) {
                throw new ReglaNegocioException("La cantidad solicitada no es válida.");
            }
            cantidades.put(id, (int) total);
        }
        for (var item : cantidades.entrySet()) {
            var variante = variantes.buscarParaActualizar(item.getKey()).orElseThrow(() ->
                    new ReglaNegocioException("Una variante de la compra ya no existe."));
            // Recupera el stock actual incluso si la variante ya estaba en el contexto JPA.
            entityManager.refresh(variante, LockModeType.PESSIMISTIC_WRITE);
            var zapato = variante.getZapato();
            var producto = zapato.getProducto();
            if (!variante.isEstaActivo() || !zapato.isEstaActivo() || !producto.isEstaActivo()) {
                throw new ReglaNegocioException("El producto \"" + producto.getNombre()
                        + "\" o su variante no está disponible para nuevas compras.");
            }
            if (variante.getStock() < item.getValue()) {
                throw new ReglaNegocioException("Existencias insuficientes para " + producto.getNombre()
                        + " · talla " + variante.getTalla() + " · " + variante.getColor() + ".");
            }
            variante.setStock(variante.getStock() - item.getValue());
        }
    }
}
