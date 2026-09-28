package com.ecommerce.tienda_kalza.controladores;

import com.ecommerce.tienda_kalza.servicios.PedidoClienteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Historial de pedidos del cliente. Son datos de prueba y no tienen relacion
 * con los que genera PedidoAdminService para el panel de administracion.
 */
@Controller
public class PedidoController {

    private final PedidoClienteService pedidos;

    public PedidoController(PedidoClienteService pedidos) {
        this.pedidos = pedidos;
    }

    @GetMapping("/pedidos")
    public String pedidos(Model model) {
        model.addAttribute("title", "KALZA | Historial de Pedidos");
        model.addAttribute("currentPage", "pedidos");
        model.addAttribute("pedidos", pedidos.obtenerPedidos());
        return "vistas/pedidos";
    }
}
