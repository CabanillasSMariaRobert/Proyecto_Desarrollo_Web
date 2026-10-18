package com.ecommerce.tienda_kalza.controladores;

import com.ecommerce.tienda_kalza.servicios.CheckoutService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Formulario de checkout. El formulario es visual por ahora: no se procesa el
 * pago ni se guardan datos de envio. El resumen si viene del CarritoService,
 * con la diferencia de que aqui el envio ya salio gratis.
 */
@Controller
public class CheckoutController {

    private final CheckoutService checkout;

    public CheckoutController(CheckoutService checkout) {
        this.checkout = checkout;
    }

    @GetMapping("/checkout")
    public String checkout(Model model) {
        model.addAttribute("title", "KALZA | Checkout");
        model.addAttribute("currentPage", "checkout");
        model.addAttribute("resumen", checkout.obtenerResumen());
        return "vistas/checkout";
    }
}
