package com.ecommerce.tienda_kalza.adapter.controller;

import com.ecommerce.tienda_kalza.adapter.enums.EstadoEntrega;
import com.ecommerce.tienda_kalza.adapter.service.EntregasService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/pedidos")
public class EntregasController {
    private final EntregasService entregasServicio;

    /*
    *  Maneja todas las rutas de pedidos
    * */
    public EntregasController(EntregasService entregasServicio){
        this.entregasServicio = entregasServicio;
    }

    public record Pedido(int codigo,
            LocalDate fecha,
            List<String> imagenes,
            String resumenProductos,
            String claseEstado,
            String estado,
            double total,
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
    public String obtenerPedidosDelUsuarioActual(Model modelo){
        List<Pedido> pedidos = this.entregasServicio
                .listarPorUsuario(1)
                .stream()
                .map(entrega -> new Pedido(
                        entrega.getId(),
                        entrega.getCreadoEl().toLocalDate(),
                        List.of("adidas Forum Low.jpg"),
                        String.join(", ", entrega.getCompra().getDetalles().stream().map(detalle -> detalle.getNombreDelProducto()).toList()),
                        Pedido.obtenerClaseEstado(entrega.getEstado()),
                        entrega.getEstado().name(),
                        81.57,
                        "pedidos/" + entrega.getId()
                ))
                .toList();
        modelo.addAttribute("pedidos", pedidos);
        return "vistas/pedidos";
    }
}
