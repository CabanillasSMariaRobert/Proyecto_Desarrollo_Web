package com.ecommerce.tienda_kalza.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Carrito de compras. Los items son datos de prueba: no hay sesion de usuario
 * todavia, asi que el carrito no se persiste.
 */
@Controller
public class CarritoController {

    @GetMapping("/carrito")
    public String carrito(Model model) {
        model.addAttribute("title", "KALZA | Carrito de Compras");
        model.addAttribute("currentPage", "carrito");
        return "vistas/carrito";
    }
}
