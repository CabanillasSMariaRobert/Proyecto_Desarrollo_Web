package com.ecommerce.tienda_kalza.controladores;

import org.springframework.stereotype.Controller;
import com.ecommerce.tienda_kalza.servicios.publico.InicioService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Portada publica. Los datos de productos y banners siguen hardcodeados en la
 * vista; cuando se conecte el catalogo real, el modelo se arma aqui.
 */
@Controller
public class InicioController {
    private final InicioService inicioService;

    public InicioController(InicioService inicioService) {
        this.inicioService = inicioService;
    }



    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("title", "KALZA | Zapatillas, zapatos y moda urbana");
        model.addAttribute("currentPage", "inicio");
        model.addAttribute("hero", inicioService.obtenerHero());
        model.addAttribute("categorias", inicioService.obtenerCategorias());
        model.addAttribute("destacados", inicioService.obtenerDestacados());
        model.addAttribute("oferta", inicioService.obtenerOferta());
        return "vistas/index";
    }
}
