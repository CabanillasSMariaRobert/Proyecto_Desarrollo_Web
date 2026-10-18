package com.ecommerce.tienda_kalza.controladores;

import com.ecommerce.tienda_kalza.servicios.PublicidadService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Pagina de ofertas. En el header esta entrada se llama "Ofertas", asi que
 * currentPage va con ese nombre para que el link quede marcado.
 */
@Controller
public class PublicidadController {

    private final PublicidadService publicidad;

    public PublicidadController(PublicidadService publicidad) {
        this.publicidad = publicidad;
    }

    @GetMapping("/publicidad")
    public String publicidad(Model model) {
        model.addAttribute("title", "KALZA | Ofertas y promociones");
        model.addAttribute("currentPage", "ofertas");
        model.addAttribute("hero", publicidad.obtenerHero());
        model.addAttribute("banners", publicidad.obtenerBanners());
        return "vistas/publicidad";
    }
}
