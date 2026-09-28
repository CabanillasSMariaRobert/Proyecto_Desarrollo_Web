package com.ecommerce.tienda_kalza.controladores;

import com.ecommerce.tienda_kalza.dtos.auth.LoginRequest;
import com.ecommerce.tienda_kalza.dtos.auth.RegisterRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Pantallas de autenticacion.
 *
 * Estas dos son las unicas que usan datos reales: el form se enlaza con
 * request de AuthController contra /api/auth, y el resto de vistas todavia no
 * toca la base. El resto de paginas publicas viven en sus propios controllers.
 */
@Controller
public class ViewController {

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("title", "KALZA | Iniciar Sesión");
        model.addAttribute("currentPage", "login");
        model.addAttribute("loginRequest", new LoginRequest());
        return "vistas/login";
    }

    @GetMapping("/registro")
    public String registro(Model model) {
        model.addAttribute("title", "KALZA | Crear Cuenta");
        model.addAttribute("currentPage", "registro");
        model.addAttribute("registerRequest", new RegisterRequest());
        return "vistas/registro";
    }

    @GetMapping("/catalogo")
    public String catalogo(Model model) {
        model.addAttribute("title", "KALZA | Catálogo de Zapatillas");
        model.addAttribute("currentPage", "catalogo");
        return "vistas/catalogo";
    }
}
