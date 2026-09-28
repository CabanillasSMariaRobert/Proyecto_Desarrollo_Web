package com.ecommerce.tienda_kalza.controladores;

import org.springframework.stereotype.Controller;
import com.ecommerce.tienda_kalza.servicios.publico.CheckoutService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Formulario de checkout. Todo el formulario es visual por ahora: no se
 * procesa el pago ni se guardan datos de envio.
 */
@Controller
public class CheckoutController {
    private final CheckoutService checkoutService;

    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }



    @GetMapping("/checkout")
    public String checkout(Model model) {
        model.addAttribute("title", "KALZA | Checkout");
        model.addAttribute("currentPage", "checkout");
        model.addAttribute("resumen", checkoutService.obtenerResumen());
        return "vistas/checkout";
    }
}
