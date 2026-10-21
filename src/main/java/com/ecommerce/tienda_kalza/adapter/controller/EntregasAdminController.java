package com.ecommerce.tienda_kalza.adapter.controller;

import com.ecommerce.tienda_kalza.adapter.enums.EstadoEntrega;
import com.ecommerce.tienda_kalza.adapter.service.EntregasService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
            double total,
            int cantidadItems,
            String linkHaciaDetalle
    ){
        public static String obtenerClaseEstado(EstadoEntrega estado){
            return switch (estado) {
                case EstadoEntrega.CANCELADO, EstadoEntrega.FALLADO -> "text-bg-danger";
                case EstadoEntrega.ASIGNADO -> "text-bg-primary";
                case EstadoEntrega.EN_CAMINO -> "text-bg-info";
                case EstadoEntrega.ENTREGADO -> "text-bg-success";
                case EstadoEntrega.PENDIENTE -> "text-bg-warning";
                case EstadoEntrega.RECOGIDO -> "text-bg-accent";
            };
        }
    }

    @GetMapping
    public String obtenerPedidosDelUsuarioActual(
            Model modelo,
            @RequestParam(name="estado", required = false)
            EstadoEntrega estadoActivo
    ){
        List<Pedido> pedidos = this.entregasServicio
                .listar()
                .stream()
                .map(entrega -> new Pedido(
                        entrega.getId(),
                        entrega.getCompra().getCliente().getNombres() + entrega.getCompra().getCliente().getApellidos(),
                        entrega.getCreadoEl().toLocalDate(),
                        List.of("adidas Forum Low.jpg"),
                        String.join(", ", entrega.getCompra().getDetalles().stream().map(detalle -> detalle.getNombreDelProducto()).toList()),
                        Pedido.obtenerClaseEstado(entrega.getEstado()),
                        entrega.getEstado().name(),
                        81.57,
                        entrega.getCompra().getDetalles().stream()
                                .map(detalle -> detalle.getCantidad())
                                .reduce(0, (acc, valor) -> acc + valor),
                        "admin/pedidos/" + entrega.getId()
                ))
                .toList();
        modelo.addAttribute("desde", 1);
        modelo.addAttribute("hasta", 5);
        modelo.addAttribute("paginaActual", 1);
        modelo.addAttribute("totalPedidos", 10);
        modelo.addAttribute("estadoActivo", estadoActivo);
        modelo.addAttribute("pedidos", pedidos);
        return "admin/pedidos";
    }
}
