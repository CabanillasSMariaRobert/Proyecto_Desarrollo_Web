package com.ecommerce.tienda_kalza.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Historial de pedidos del cliente. Los pedidos son datos de prueba, ajenos a
 * los que genera PedidoAdminService para el panel de administracion.
 */
@Controller
public class PedidoController {

    @GetMapping("/pedidos")
    public String pedidos(Model model) {
        model.addAttribute("title", "KALZA | Historial de Pedidos");
        model.addAttribute("currentPage", "pedidos");
        return "vistas/pedidos";
    }
}
