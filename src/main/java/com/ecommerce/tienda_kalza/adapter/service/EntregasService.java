package com.ecommerce.tienda_kalza.adapter.service;

import com.ecommerce.tienda_kalza.adapter.entity.CompraEntity;
import com.ecommerce.tienda_kalza.adapter.entity.EntregaEntity;
import com.ecommerce.tienda_kalza.adapter.entity.PuntoEntregaEntity;
import com.ecommerce.tienda_kalza.adapter.model.*;
import com.ecommerce.tienda_kalza.adapter.repository.EntregasRepository;
import com.ecommerce.tienda_kalza.adapter.repository.CompraRepository;
import com.ecommerce.tienda_kalza.adapter.enums.EstadoEntrega;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@Transactional(readOnly = true)
public class EntregasService {
    private final EntregasRepository repository;
    private final CompraService compraService;
    private final CompraRepository compras;

    public EntregasService(EntregasRepository repository, CompraService compraService, CompraRepository compras) {
        this.repository = repository;
        this.compraService=compraService;
        this.compras = compras;
    }

    @Transactional
    public void cambiarEstado(int id, EstadoEntrega estado) {
        if (estado == null) {
            throw new ReglaNegocioException("Selecciona un estado de entrega.");
        }
        EntregaEntity entrega = repository.buscarParaActualizar(id)
                .orElseThrow(() -> new ReglaNegocioException("No se encontró el pedido solicitado."));
        EstadoEntrega anterior = entrega.getEstado();
        if (anterior == estado) {
            return; // Un reenvío del formulario no modifica las fechas ya registradas.
        }
        if (anterior == null || !anterior.permiteTransicionA(estado)) {
            throw new ReglaNegocioException("No se permite cambiar la entrega de " + anterior + " a " + estado + ".");
        }

        if (entrega.getCompra() == null) throw new ReglaNegocioException("La entrega no tiene una compra asociada.");
        // Bloquea también la compra para no atenderla mientras otra operación la cancela.
        CompraEntity compra = compras.buscarParaActualizar(entrega.getCompra().getIdCompra())
                .orElseThrow(() -> new ReglaNegocioException("La compra no existe."));
        if (estado == EstadoEntrega.CANCELADO) {
            if (!"pendiente".equals(compra.getEstado()))
                throw new ReglaNegocioException("Solo se puede cancelar la entrega de una compra pendiente de pago.");
        } else if (!List.of("pagado", "enviado").contains(compra.getEstado())) {
            throw new ReglaNegocioException("Solo se pueden atender entregas de compras pagadas o enviadas.");
        }

        LocalDateTime ahora = LocalDateTime.now(ZoneId.of("America/Lima"));
        LocalDateTime asignado = entrega.getAsignadoEl();
        LocalDateTime recogido = entrega.getRecogidoEl();
        LocalDateTime entregado = entrega.getEntregadoEl();
        if (estado == EstadoEntrega.ASIGNADO && asignado == null) asignado = ahora;
        if (estado == EstadoEntrega.RECOGIDO && recogido == null) recogido = ahora;
        if (estado == EstadoEntrega.ENTREGADO && entregado == null) entregado = ahora;
        if (estado == EstadoEntrega.ASIGNADO || estado == EstadoEntrega.RECOGIDO
                || estado == EstadoEntrega.EN_CAMINO || estado == EstadoEntrega.ENTREGADO) {
            validarFecha(asignado, null, ahora, "asignación");
        }
        if (estado == EstadoEntrega.RECOGIDO || estado == EstadoEntrega.EN_CAMINO || estado == EstadoEntrega.ENTREGADO) {
            validarFecha(recogido, asignado, ahora, "recojo");
        }
        if (estado == EstadoEntrega.ENTREGADO) {
            validarFecha(entregado, recogido, ahora, "entrega");
        }

        // CompraService conserva sus propias reglas. Si rechaza el cambio, no se actualiza el seguimiento.
        int idCompra = compra.getIdCompra();
        switch (estado) {
            case EN_CAMINO -> compraService.cambiarEstado(idCompra, "enviado");
            case ENTREGADO -> compraService.cambiarEstado(idCompra, "entregado");
            case CANCELADO -> compraService.cambiarEstado(idCompra, "cancelado");
            default -> {
                // Un reparto FALLADO no equivale a cancelar o reembolsar una compra ya enviada.
            }
        }
        entrega.setEstado(estado);
        entrega.setAsignadoEl(asignado);
        entrega.setRecogidoEl(recogido);
        entrega.setEntregadoEl(entregado);
        repository.save(entrega);
    }

    private void validarFecha(LocalDateTime fecha, LocalDateTime anterior, LocalDateTime ahora, String evento) {
        if (fecha == null) {
            throw new ReglaNegocioException("Falta la fecha de " + evento + " de la entrega.");
        }
        if (fecha.isAfter(ahora) || (anterior != null && fecha.isBefore(anterior))) {
            throw new ReglaNegocioException("La fecha de " + evento + " no respeta el orden del seguimiento de la entrega.");
        }
    }

    public List<Entrega> listar() {
        return repository.findAll().stream().map(this::aModel).toList();
    }

    public record Filtro(EstadoEntrega estado, int pagina, int tamanio) {
        public Filtro {
            pagina = Math.max(0, pagina);
            tamanio = Math.max(1, Math.min(100, tamanio));
        }
    }

    public Page<Entrega> listar(Filtro filtro) {
        var paginado = PageRequest.of(filtro.pagina(), filtro.tamanio(), Sort.by("id").descending());
        return (filtro.estado() == null
                ? repository.findAll(paginado)
                : repository.findByEstado(filtro.estado(), paginado))
                .map(this::aModel);
    }

    public List<Entrega> listarPorUsuario(int idUsuario) {
        return repository.findByCompra_Cliente_IdUsuario(idUsuario)
                .stream().map(this::aModel).toList();
    }

    public Page<Entrega> listarPorUsuario(int idUsuario, int pagina, int tamanio) {
        var filtro = new Filtro(null, pagina, tamanio);
        var paginado = PageRequest.of(filtro.pagina(), filtro.tamanio(), Sort.by("id").descending());
        return repository.findByCompra_Cliente_IdUsuario(idUsuario, paginado).map(this::aModel);
    }

    private Entrega aModel(EntregaEntity entity) {
        return new Entrega(
                entity.getId(), aCompra(entity.getCompra()), aPuntoEntrega(entity.getPuntoDeEntrega()),
                entity.getEstado(), entity.getAsignadoEl(), entity.getRecogidoEl(),
                entity.getEntregadoEl(), entity.getCreadoEl(), entity.getActualizadoEl()
        );
    }

    // Copia los datos usados por Entregas sin recorrer relaciones circulares.
    private Compra aCompra(CompraEntity entity) {
        if (entity == null) {
            return null;
        }
        Compra compra = new Compra();
        compra.setIdCompra(entity.getIdCompra());
        compra.setEstado(entity.getEstado());
        compra.setTotalConDescuento(entity.getTotalConDescuento());
        compra.setTotalSinDescuento(entity.getTotalSinDescuento());
        compra.setFechaDeCompra(entity.getFechaDeCompra());
        if (entity.getCliente() != null) {
            Usuario cliente = new Usuario();
            cliente.setIdUsuario(entity.getCliente().getIdUsuario());
            cliente.setNombres(entity.getCliente().getNombres());
            cliente.setApellidos(entity.getCliente().getApellidos());
            cliente.setCorreo(entity.getCliente().getCorreo());
            cliente.setTelefono(entity.getCliente().getTelefono());
            compra.setCliente(cliente);
        }
        compra.setDetalles(entity.getDetalles().stream().map(detalle -> {
            DetalleCompra model = new DetalleCompra();
            model.setNombreDelProducto(detalle.getNombreDelProducto());
            model.setPrecioOriginalXUnidad(detalle.getPrecioOriginalXUnidad());
            model.setDescuentoXUnidad(detalle.getDescuentoXUnidad());
            model.setCantidad(detalle.getCantidad());
            model.setPrecioSinDescuento(detalle.getPrecioSinDescuento());
            model.setPrecioConDescuento(detalle.getPrecioConDescuento());
            model.setCreadoEl(detalle.getCreadoEl());
            return model;
        }).toList());
        return compra;
    }

    private PuntoEntrega aPuntoEntrega(PuntoEntregaEntity entity) {
        if (entity == null) {
            return null;
        }
        return new PuntoEntrega(
                entity.getId(), entity.getNombre(), entity.getDireccion(), entity.getReferencia(),
                entity.getLatitud(), entity.getLongitud(), entity.isEstaActivo(),
                entity.getCreadoEl(), entity.getActualizadoEl()
        );
    }
}
