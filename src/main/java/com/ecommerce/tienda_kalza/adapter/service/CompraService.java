package com.ecommerce.tienda_kalza.adapter.service;

import com.ecommerce.tienda_kalza.adapter.entity.CompraEntity;
import com.ecommerce.tienda_kalza.adapter.repository.CompraRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class CompraService {
    public static final List<String> ESTADOS = List.of("pendiente", "pagado", "enviado", "entregado", "cancelado");
    private static final Map<String, Integer> ORDEN = Map.of(
            "pendiente", 0,
            "pagado", 1,
            "enviado", 2,
            "entregado", 3
    );

    private final CompraRepository compras;

    public CompraService(CompraRepository compras) {
        this.compras = compras;
    }

    @Transactional(readOnly = true)
    public List<CompraEntity> listar() {
        return compras.findAll();
    }

    @Transactional(readOnly = true)
    public CompraEntity obtener(int id) {
        return compras.findById(id)
                .orElseThrow(() -> new ReglaNegocioException("La compra #" + id + " no existe."));
    }

    @Transactional
    public CompraEntity guardar(CompraEntity compra) {
        if (compra.getTotalConDescuento() == null || compra.getTotalSinDescuento() == null) {
            throw new ReglaNegocioException("Los totales de la compra son obligatorios.");
        }
        if (compra.getTotalConDescuento().compareTo(compra.getTotalSinDescuento()) > 0) {
            throw new ReglaNegocioException(
                    "El total con descuento (S/ " + compra.getTotalConDescuento()
                            + ") no puede ser mayor al total sin descuento (S/ " + compra.getTotalSinDescuento() + ").");
        }
        if (!ESTADOS.contains(compra.getEstado())) {
            throw new ReglaNegocioException("Estado no válido: " + compra.getEstado());
        }

        if (compra.getIdCompra() != 0) {
            CompraEntity guardada = obtener(compra.getIdCompra());
            validarTransicion(guardada.getEstado(), compra.getEstado());
            compra.setFechaDeCompra(guardada.getFechaDeCompra());
        }
        return compras.save(compra);
    }

    @Transactional
    public void cambiarEstado(int id, String nuevoEstado) {
        CompraEntity compra = obtener(id);
        validarTransicion(compra.getEstado(), nuevoEstado);
        compra.setEstado(nuevoEstado);
        compras.save(compra);
    }

    @Transactional
    public void eliminar(int id) {
        CompraEntity compra = obtener(id);
        if ("entregado".equals(compra.getEstado())) {
            throw new ReglaNegocioException(
                    "No se puede eliminar la compra #" + id + ": ya fue entregada y es el respaldo de la venta.");
        }
        compras.delete(compra);
    }

    private void validarTransicion(String actual, String nuevo) {
        if (actual.equals(nuevo)) {
            return; // guardar sin cambiar el estado no es una transición
        }
        if ("entregado".equals(actual)) {
            throw new ReglaNegocioException(
                    "La compra ya fue entregada: su estado no puede cambiar (es un registro de la venta).");
        }
        if ("cancelado".equals(actual)) {
            throw new ReglaNegocioException("Una compra cancelada no puede cambiar de estado.");
        }
        if ("cancelado".equals(nuevo)) {
            if (ORDEN.get(actual) >= 2) {
                throw new ReglaNegocioException(
                        "Solo se pueden cancelar compras que no hayan sido enviadas (estado actual: " + actual + ").");
            }
            return;
        }
        if (ORDEN.get(nuevo) <= ORDEN.get(actual)) {
            throw new ReglaNegocioException(
                    "El estado solo puede avanzar: de '" + actual + "' no se puede pasar a '" + nuevo + "'.");
        }
    }
}
