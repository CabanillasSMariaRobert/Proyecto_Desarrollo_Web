package com.ecommerce.tienda_kalza.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Pagina de ofertas. En el header esta entrada se llama "Ofertas", asi que
 * currentPage va con ese nombre para que el link quede marcado.
 */
@Controller
public class PublicidadController {

    @GetMapping("/publicidad")
    public String publicidad(Model model) {
        model.addAttribute("title", "KALZA | Ofertas y promociones");
        model.addAttribute("currentPage", "ofertas");
        return "vistas/publicidad";
    }
}
