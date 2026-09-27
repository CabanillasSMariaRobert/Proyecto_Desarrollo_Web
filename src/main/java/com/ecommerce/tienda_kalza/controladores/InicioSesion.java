package com.ecommerce.tienda_kalza.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InicioSesion {
    @GetMapping("/login")
    public String login(Model model){
        return "vistas/login";
    }
}
