package com.ecommerce.tienda_kalza.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Portada publica. Los datos de productos y banners siguen hardcodeados en la
 * vista; cuando se conecte el catalogo real, el modelo se arma aqui.
 */
@Controller
public class InicioController {

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("title", "KALZA | Zapatillas, zapatos y moda urbana");
        model.addAttribute("currentPage", "inicio");
        return "vistas/index";
    }
}
