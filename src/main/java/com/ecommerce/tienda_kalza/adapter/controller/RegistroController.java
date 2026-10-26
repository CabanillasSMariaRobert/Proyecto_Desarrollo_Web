package com.ecommerce.tienda_kalza.adapter.controller;

import com.ecommerce.tienda_kalza.adapter.model.RegistroForm;
import com.ecommerce.tienda_kalza.adapter.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class RegistroController {

    private final UsuarioService usuarioService;

    @GetMapping("/registro")
    public String mostrarRegistro(Model model, HttpSession session) {
        // Si ya hay sesión activa, redirige a la página principal
        if (session.getAttribute("usuarioLogueado") != null) {
            return "redirect:/";
        }

        if (!model.containsAttribute("registroForm")) {
            model.addAttribute("registroForm", new RegistroForm());
        }
        model.addAttribute("title", "Crear Cuenta | KALZA");
        model.addAttribute("currentPage", "registro");

        return "vistas/registro";
    }

    @PostMapping("/registro")
    public String procesarRegistro(
            @ModelAttribute("registroForm") RegistroForm form,
            Model model,
            RedirectAttributes flash) {

        try {
            usuarioService.registrar(form);

            // Mensaje de éxito que se mostrará en /login
            flash.addFlashAttribute("mensaje", "¡Tu cuenta ha sido creada exitosamente! Ya puedes iniciar sesión.");
            return "redirect:/login";

        } catch (IllegalArgumentException | IllegalStateException e) {
            // Muestra el mensaje de error en la alerta roja de registro.html
            model.addAttribute("errorGlobal", e.getMessage());
            model.addAttribute("title", "Crear Cuenta | KALZA");
            model.addAttribute("currentPage", "registro");

            return "vistas/registro";
        }
    }
}