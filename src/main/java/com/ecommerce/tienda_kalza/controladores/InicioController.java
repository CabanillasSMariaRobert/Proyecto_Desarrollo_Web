package com.ecommerce.tienda_kalza.controladores;

import com.ecommerce.tienda_kalza.servicios.InicioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/** Portada publica. Los datos salen del InicioService, que a su vez toma los precios del catalogo. */
@Controller
public class InicioController {

    private final InicioService inicio;

    public InicioController(InicioService inicio) {
        this.inicio = inicio;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("title", "KALZA | Zapatillas, zapatos y moda urbana");
        model.addAttribute("currentPage", "inicio");
        model.addAttribute("hero", inicio.obtenerHero());
        model.addAttribute("categorias", inicio.obtenerCategorias());
        model.addAttribute("destacados", inicio.obtenerDestacados());
        model.addAttribute("oferta", inicio.obtenerOferta());
        return "vistas/index";
    }
}
