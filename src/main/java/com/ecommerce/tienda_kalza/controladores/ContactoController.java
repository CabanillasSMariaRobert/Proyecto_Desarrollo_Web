package com.ecommerce.tienda_kalza.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Pagina de contacto. El formulario no se envia a ningun lado todavia; los
 * anchors de privacidad y terminos apuntan a secciones de esta misma vista.
 */
@Controller
public class ContactoController {

    @GetMapping("/contacto")
    public String contacto(Model model) {
        model.addAttribute("title", "KALZA | Contacto");
        model.addAttribute("currentPage", "contacto");
        return "vistas/contacto";
    }
}
