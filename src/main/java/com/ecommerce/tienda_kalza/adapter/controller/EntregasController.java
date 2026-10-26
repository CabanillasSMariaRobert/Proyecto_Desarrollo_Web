package com.ecommerce.tienda_kalza.adapter.controller;

import com.ecommerce.tienda_kalza.adapter.entity.CompraEntity;
import com.ecommerce.tienda_kalza.adapter.entity.EntregaEntity;
import com.ecommerce.tienda_kalza.adapter.service.CompraService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Controller
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class EntregasController {
    private final CompraService compras;

    public record Pedido(int codigo, LocalDate fecha, String resumenProductos, String claseEstado,
                         String estado, BigDecimal total, int cantidadItems, CompraEntity compra, EntregaEntity entrega) {}

    @GetMapping
    public String obtenerPedidosDelUsuarioActual(Model modelo, HttpSession session,
            @RequestParam(name = "pagina", defaultValue = "0") int pagina) {
        // El propietario se obtiene de la sesión, nunca de parámetros del navegador.
        if (!(session.getAttribute("idUsuario") instanceof Integer idUsuario)
                || session.getAttribute("usuarioLogueado") == null) return "redirect:/login";
        var historial = compras.listarPorCliente(idUsuario, pagina);
        modelo.addAttribute("pedidos", historial.map(compra -> new Pedido(
                compra.getIdCompra(), compra.getFechaDeCompra().toLocalDate(),
                String.join(", ", compra.getDetalles().stream().map(d -> d.getNombreDelProducto()).toList()),
                claseEstado(compra.getEstado()), compra.getEstado(), compra.getTotalConDescuento(),
                compra.getDetalles().stream().mapToInt(d -> d.getCantidad()).sum(), compra, compra.getEntrega()
        )).getContent());
        modelo.addAttribute("desde", historial.isEmpty() ? 0 : historial.getPageable().getOffset() + 1);
        modelo.addAttribute("hasta", historial.isEmpty() ? 0 : historial.getPageable().getOffset() + historial.getNumberOfElements());
        modelo.addAttribute("paginaActual", historial.getNumber());
        modelo.addAttribute("totalPedidos", historial.getTotalElements());
        modelo.addAttribute("totalPaginas", Math.max(1, historial.getTotalPages()));
        modelo.addAttribute("title", "Mis compras | KALZA");
        modelo.addAttribute("currentPage", "pedidos");
        return "vistas/pedidos";
    }

    private String claseEstado(String estado) {
        return switch (estado) {
            case "pagado", "entregado" -> "text-bg-success";
            case "enviado" -> "text-bg-info";
            case "cancelado" -> "text-bg-danger";
            default -> "text-bg-warning";
        };
    }
}
