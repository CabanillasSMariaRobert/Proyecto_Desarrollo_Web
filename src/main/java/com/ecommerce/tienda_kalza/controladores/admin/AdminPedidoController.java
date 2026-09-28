package com.ecommerce.tienda_kalza.controladores.admin;

import com.ecommerce.tienda_kalza.dtos.EstadoPedido;
import com.ecommerce.tienda_kalza.dtos.PedidoAdmin;
import com.ecommerce.tienda_kalza.servicios.PedidoAdminService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AdminPedidoController {

    private static final int POR_PAGINA = 5;

    private final PedidoAdminService pedidoAdminService;

    public AdminPedidoController(PedidoAdminService pedidoAdminService) {
        this.pedidoAdminService = pedidoAdminService;
    }

    @GetMapping("/admin/pedidos")
    public String listarPedidos(
            @RequestParam(name = "estado", required = false) String estado,
            @RequestParam(name = "pagina", defaultValue = "0") int pagina,
            Model model) {

        EstadoPedido estadoActivo = parsearEstado(estado);
        List<PedidoAdmin> filtrados = pedidoAdminService.filtrarPorEstado(estadoActivo);

        model.addAttribute("title", "Pedidos");
        model.addAttribute("currentPage", "pedidos");

        int totalPaginas = Math.max(1, (int) Math.ceil((double) filtrados.size() / POR_PAGINA));
        int paginaActual = Math.min(Math.max(pagina, 0), totalPaginas - 1);

        model.addAttribute("pedidos", pedidoAdminService.paginar(filtrados, paginaActual, POR_PAGINA));
        model.addAttribute("estadoActivo", estadoActivo);
        model.addAttribute("paginaActual", paginaActual);
        model.addAttribute("totalPaginas", totalPaginas);
        model.addAttribute("totalPedidos", filtrados.size());
        model.addAttribute("desde", Math.min(paginaActual * POR_PAGINA + 1, filtrados.size()));
        model.addAttribute("hasta", Math.min((paginaActual + 1) * POR_PAGINA, filtrados.size()));
        return "admin/pedidos";
    }

    private EstadoPedido parsearEstado(String estado) {
        if (estado == null || estado.isBlank()) {
            return null;
        }
        try {
            return EstadoPedido.valueOf(estado.toUpperCase());
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }
}
