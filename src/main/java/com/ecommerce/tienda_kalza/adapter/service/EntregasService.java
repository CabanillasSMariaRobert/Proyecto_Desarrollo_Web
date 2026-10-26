package com.ecommerce.tienda_kalza.adapter.service;

import com.ecommerce.tienda_kalza.adapter.entity.CompraEntity;
import com.ecommerce.tienda_kalza.adapter.entity.EntregaEntity;
import com.ecommerce.tienda_kalza.adapter.entity.PuntoEntregaEntity;
import com.ecommerce.tienda_kalza.adapter.model.*;
import com.ecommerce.tienda_kalza.adapter.repository.EntregasRepository;
import com.ecommerce.tienda_kalza.adapter.enums.EstadoEntrega;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class EntregasService {
    private final EntregasRepository repository;
    private final CompraService compraService;

    public EntregasService(EntregasRepository repository,CompraService compraService) {
        this.repository = repository;
        this.compraService=compraService;
    }

    @Transactional
    public void cambiarEstado(int id, EstadoEntrega estado) {
        if (estado == null) {
            throw new ReglaNegocioException("Selecciona un estado de entrega.");
        }
        EntregaEntity entrega = repository.findById(id)
                .orElseThrow(() -> new ReglaNegocioException("No se encontró el pedido solicitado."));
        entrega.setEstado(estado);
        repository.save(entrega);


        if (entrega.getCompra() != null) {
            int idCompra = entrega.getCompra().getIdCompra();
            switch (estado) {
                case EN_CAMINO -> compraService.cambiarEstado(idCompra, "enviado");
                case ENTREGADO -> compraService.cambiarEstado(idCompra, "entregado");
                case CANCELADO, FALLADO -> compraService.cambiarEstado(idCompra, "cancelado");
                default -> {
                    // PENDIENTE, ASIGNADO, RECOGIDO no cambian el estado comercial por ahora
                }
            }
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
