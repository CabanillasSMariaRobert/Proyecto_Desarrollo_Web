package com.ecommerce.tienda_kalza.adapter.service;

import com.ecommerce.tienda_kalza.adapter.entity.CompraEntity;
import com.ecommerce.tienda_kalza.adapter.entity.DetalleCompraEntity;
import com.ecommerce.tienda_kalza.adapter.repository.CompraRepository;
import com.ecommerce.tienda_kalza.adapter.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

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
    private final DisponibilidadProductoService disponibilidad;
    private final InventarioService inventario;
    private final UsuarioRepository usuarios;

    public CompraService(CompraRepository compras, DisponibilidadProductoService disponibilidad,
                         InventarioService inventario, UsuarioRepository usuarios) {
        this.compras = compras;
        this.disponibilidad = disponibilidad;
        this.inventario = inventario;
        this.usuarios = usuarios;
    }

    @Transactional(readOnly = true)
    public List<CompraEntity> listar() {
        return compras.findAll();
    }

    @Transactional(readOnly = true)
    public Page<CompraEntity> listarPorCliente(int idUsuario, int pagina) {
        var paginado = PageRequest.of(Math.max(0, pagina), 5,
                Sort.by(Sort.Direction.DESC, "fechaDeCompra", "idCompra"));
        var historial = compras.findByCliente_IdUsuario(idUsuario, paginado);
        // Carga los detalles antes de cerrar la transacción para mostrarlos en la vista.
        historial.forEach(compra -> compra.getDetalles().size());
        return historial;
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
            compras.buscarParaActualizar(compra.getIdCompra())
                    .orElseThrow(() -> new ReglaNegocioException("La compra no existe."));
            var anteriores = compras.consultarDetallesRn01(compra.getIdCompra());
            CompraEntity guardada = obtener(compra.getIdCompra());
            String estadoAnterior = anteriores.get(0).getEstado();
            validarTransicion(estadoAnterior, compra.getEstado());
            compra.setFechaDeCompra(guardada.getFechaDeCompra());
            if (anteriores.stream().anyMatch(d -> "pendiente".equals(d.getEstado()))
                    && !"cancelado".equals(compra.getEstado())) {
                disponibilidad.validarVariantes(anteriores.stream().map(d -> d.getIdVariante())
                        .filter(java.util.Objects::nonNull).toList());
                validarDisponibilidad(compra.getDetalles());
            } else {
                // El historial sigue siendo editable según sus reglas; solo nuevas unidades requieren disponibilidad.
                Map<Integer, Integer> cantidades = new HashMap<>();
                for (var detalle : anteriores) {
                    if (detalle.getIdVariante() != null) cantidades.put(detalle.getIdVariante(), detalle.getCantidad());
                }
                validarDisponibilidad(compra.getDetalles().stream()
                        .filter(d -> d == null || d.getVarianteZapato() == null || d.getCantidad() > cantidades
                                .getOrDefault(d.getVarianteZapato().getIdVariante(), 0)).toList());
            }
            if (esVenta(estadoAnterior)) {
                Map<Integer, Integer> cantidadesAnteriores = new HashMap<>();
                for (var d : anteriores) {
                    if (d.getIdVariante() != null) cantidadesAnteriores.put(d.getIdVariante(), d.getCantidad());
                }
                Map<Integer, Integer> cantidadesNuevas = new HashMap<>();
                for (var d : compra.getDetalles()) {
                    if (d == null || d.getVarianteZapato() == null)
                        throw new ReglaNegocioException("El detalle de la compra no es válido.");
                    cantidadesNuevas.merge(d.getVarianteZapato().getIdVariante(), d.getCantidad(), Integer::sum);
                }
                if (!cantidadesAnteriores.equals(cantidadesNuevas)) {
                    throw new ReglaNegocioException("No se pueden cambiar las variantes o cantidades de una compra ya pagada.");
                }
            }
            if (!esVenta(estadoAnterior) && esVenta(compra.getEstado())) {
                validarClienteActivo(compra);
                inventario.descontar(compra.getDetalles());
            }
        } else {
            validarClienteActivo(compra);
            validarDisponibilidad(compra.getDetalles());
            if (esVenta(compra.getEstado())) inventario.descontar(compra.getDetalles());
        }
        return compras.save(compra);
    }

    @Transactional
    public void cambiarEstado(int id, String nuevoEstado) {
        CompraEntity compra = compras.buscarParaActualizar(id)
                .orElseThrow(() -> new ReglaNegocioException("La compra no existe."));
        // Proyección persistida: no depende de un estado modificado en memoria.
        String anterior = compras.consultarDetallesRn01(id).get(0).getEstado();
        validarTransicion(anterior, nuevoEstado);
        if (compras.consultarDetallesRn01(id).stream().anyMatch(d -> "pendiente".equals(d.getEstado()))
                && !"pendiente".equals(nuevoEstado)
                && !"cancelado".equals(nuevoEstado)) {
            validarDisponibilidad(compra.getDetalles());
        }
        if (!esVenta(anterior) && esVenta(nuevoEstado)) {
            validarClienteActivo(compra);
            inventario.descontar(compra.getDetalles());
        }
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
        if (!ESTADOS.contains(nuevo)) throw new ReglaNegocioException("Estado de compra no válido.");
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
            if (!"pendiente".equals(actual)) {
                throw new ReglaNegocioException(
                        "Solo se pueden cancelar compras pendientes de pago (estado actual: " + actual + ").");
            }
            return;
        }
        if (ORDEN.get(nuevo) <= ORDEN.get(actual)) {
            throw new ReglaNegocioException(
                    "El estado solo puede avanzar: de '" + actual + "' no se puede pasar a '" + nuevo + "'.");
        }
    }

    private boolean esVenta(String estado) {
        return List.of("pagado", "enviado", "entregado").contains(estado);
    }

    // Consulta la base de datos para no confiar en datos antiguos del cliente.
    private void validarClienteActivo(CompraEntity compra) {
        if (compra.getCliente() == null
                || !usuarios.existsByIdUsuarioAndEstaActivoTrue(compra.getCliente().getIdUsuario())) {
            throw new ReglaNegocioException("El cliente no existe o está inactivo y no puede realizar nuevas compras.");
        }
    }

    private void validarDisponibilidad(List<DetalleCompraEntity> detalles) {
        List<Integer> ids = new ArrayList<>();
        for (DetalleCompraEntity detalle : detalles) {
            if (detalle == null || detalle.getVarianteZapato() == null) {
                throw new ReglaNegocioException("No se pudo identificar la variante del producto.");
            }
            ids.add(detalle.getVarianteZapato().getIdVariante());
        }
        disponibilidad.validarVariantes(ids);
    }
}
