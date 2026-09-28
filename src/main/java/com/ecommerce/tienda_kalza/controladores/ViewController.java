package com.ecommerce.tienda_kalza.controladores;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ViewController {

    @GetMapping("/")
    public String index(Model model) {
        return "vistas/index";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("loginRequest", new com.ecommerce.tienda_kalza.dtos.auth.LoginRequest());
        return "vistas/login";
    }

    @GetMapping("/registro")
    public String registro(Model model) {
        model.addAttribute("registerRequest", new com.ecommerce.tienda_kalza.dtos.auth.RegisterRequest());
        return "vistas/registro";
    }

    @GetMapping("/catalogo")
    public String catalogo(Model model) {
        return "vistas/catalogo";
    }

    @GetMapping("/producto/{id}")
    public String producto(@org.springframework.web.bind.annotation.PathVariable Integer id, Model model) {
        return "vistas/producto";
    }
}