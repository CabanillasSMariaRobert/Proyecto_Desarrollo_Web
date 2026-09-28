package com.ecommerce.tienda_kalza.controladores;

import com.ecommerce.tienda_kalza.servicios.ContactoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Pagina de contacto. El formulario no se envia a ningun lado todavia; los
 * anchors de privacidad y terminos apuntan a secciones de esta misma vista.
 */
@Controller
public class ContactoController {

    private final ContactoService contacto;

    public ContactoController(ContactoService contacto) {
        this.contacto = contacto;
    }

    @GetMapping("/contacto")
    public String contacto(Model model) {
        model.addAttribute("title", "KALZA | Contacto");
        model.addAttribute("currentPage", "contacto");
        model.addAttribute("canales", contacto.obtenerCanales());
        model.addAttribute("horarios", contacto.obtenerHorarios());
        model.addAttribute("privacidad", contacto.obtenerPrivacidad());
        model.addAttribute("terminos", contacto.obtenerTerminos());
        return "vistas/contacto";
    }
}
