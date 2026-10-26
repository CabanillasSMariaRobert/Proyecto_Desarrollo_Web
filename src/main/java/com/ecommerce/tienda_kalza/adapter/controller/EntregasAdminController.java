package com.ecommerce.tienda_kalza.adapter.controller;

import com.ecommerce.tienda_kalza.adapter.enums.EstadoEntrega;
import com.ecommerce.tienda_kalza.adapter.model.Entrega;
import com.ecommerce.tienda_kalza.adapter.service.EntregasService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/admin/pedidos")
public class EntregasAdminController {
    private final EntregasService entregasServicio;

    /*
    *  Maneja todas las rutas de pedidos
    * */
    public EntregasAdminController(EntregasService entregasServicio){
        this.entregasServicio = entregasServicio;
    }

    public record Pedido(
            int id,
            String cliente,
            LocalDate fecha,
            List<String> imagenes,
            String resumenProductos,
            String claseEstado,
            String estado,
            BigDecimal total,
            int cantidadItems,
            String linkHaciaDetalle,
            Entrega entrega
    ){
        public static String obtenerClaseEstado(EstadoEntrega estado){
            return switch (estado) {
                case EstadoEntrega.CANCELADO, EstadoEntrega.FALLADO -> "text-bg-danger";
                case EstadoEntrega.ASIGNADO -> "text-bg-primary";
                case EstadoEntrega.EN_CAMINO -> "text-bg-info";
                case EstadoEntrega.ENTREGADO -> "text-bg-success";
                case EstadoEntrega.PENDIENTE -> "text-bg-warning";
                case EstadoEntrega.RECOGIDO -> "text-dark";
            };
        }
    }

    @GetMapping
    public String obtenerPedidosDelUsuarioActual(
            Model modelo,
            @RequestParam(name="estado", required = false)
            EstadoEntrega estadoActivo,
            @RequestParam(name="pagina", defaultValue = "0") int pagina
    ){
        var entregas = this.entregasServicio.listar(new EntregasService.Filtro(estadoActivo, pagina, 5));
        List<Pedido> pedidos = entregas
                .stream()
                .map(entrega -> new Pedido(
                        entrega.getId(),
                        entrega.getCompra().getCliente().getNombres() + " " + entrega.getCompra().getCliente().getApellidos(),
                        entrega.getCreadoEl().toLocalDate(),
                        List.of("adidas Forum Low.jpg"),
                        String.join(", ", entrega.getCompra().getDetalles().stream().map(detalle -> detalle.getNombreDelProducto()).toList()),
                        Pedido.obtenerClaseEstado(entrega.getEstado()),
                        entrega.getEstado().name(),
                        entrega.getCompra().getTotalConDescuento(),
                        entrega.getCompra().getDetalles().stream()
                                .map(detalle -> detalle.getCantidad())
                                .reduce(0, (acc, valor) -> acc + valor),
                        "admin/pedidos/" + entrega.getId(),
                        entrega
                ))
                .toList();
        modelo.addAttribute("desde", entregas.isEmpty() ? 0 : entregas.getPageable().getOffset() + 1);
        modelo.addAttribute("hasta", entregas.isEmpty() ? 0 : entregas.getPageable().getOffset() + entregas.getNumberOfElements());
        modelo.addAttribute("paginaActual", entregas.getNumber());
        modelo.addAttribute("totalPedidos", entregas.getTotalElements());
        modelo.addAttribute("totalPaginas", Math.max(1, entregas.getTotalPages()));
        modelo.addAttribute("estadoActivo", estadoActivo);
        modelo.addAttribute("pedidos", pedidos);
        return "admin/pedidos";
    }
}
