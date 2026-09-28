package com.ecommerce.tienda_kalza.controladores;

import org.springframework.stereotype.Controller;
import com.ecommerce.tienda_kalza.servicios.publico.ContactoService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.Map;

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
        
        Map<String, String> datosMap = Map.of(
            "direccion", "Av. Principal 123",
            "telefono", "999888777",
            "correo", "contacto@kalza.com"
        );
        model.addAttribute("datos", datosMap);
        
        model.addAttribute("horarios", contactoService.obtenerHorarios());
        model.addAttribute("faqs", contactoService.obtenerFaqs());
        return "vistas/contacto";
    }
}
