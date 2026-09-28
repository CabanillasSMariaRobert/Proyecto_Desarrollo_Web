package com.ecommerce.tienda_kalza.controladores;

import org.springframework.stereotype.Controller;
import com.ecommerce.tienda_kalza.servicios.publico.ContactoService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Pagina de contacto. El formulario no se envia a ningun lado todavia; los
 * anchors de privacidad y terminos apuntan a secciones de esta misma vista.
 */
@Controller
public class ContactoController {
    private final ContactoService contactoService;

    public ContactoController(ContactoService contactoService) {
        this.contactoService = contactoService;
    }



    @GetMapping("/contacto")
    public String contacto(Model model) {
        model.addAttribute("title", "KALZA | Contacto");
        model.addAttribute("currentPage", "contacto");
        model.addAttribute("datos", contactoService.obtenerDatos());
        model.addAttribute("horarios", contactoService.obtenerHorarios());
        model.addAttribute("faqs", contactoService.obtenerFaqs());
        return "vistas/contacto";
    }
}
