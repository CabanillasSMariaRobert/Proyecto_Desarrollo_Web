package com.ecommerce.tienda_kalza.adapter.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EstaticosController {
    @GetMapping("/publicidad")
    public String publicidad(Model model) {
        model.addAttribute("title", "Ofertas | KALZA");
        model.addAttribute("currentPage", "ofertas");

        return "vistas/publicidad";
    }

    @GetMapping("/contacto")
    public String contacto(Model model) {
        model.addAttribute("title", "Contacto | KALZA");
        model.addAttribute("currentPage", "contacto");

        return "vistas/contacto";
    }
}
