package com.ecommerce.tienda_kalza.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Formulario de checkout. Todo el formulario es visual por ahora: no se
 * procesa el pago ni se guardan datos de envio.
 */
@Controller
public class CheckoutController {

    @GetMapping("/checkout")
    public String checkout(Model model) {
        model.addAttribute("title", "KALZA | Checkout");
        model.addAttribute("currentPage", "checkout");
        return "vistas/checkout";
    }
}
