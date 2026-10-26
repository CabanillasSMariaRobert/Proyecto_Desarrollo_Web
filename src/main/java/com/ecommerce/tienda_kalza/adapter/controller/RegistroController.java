package com.ecommerce.tienda_kalza.adapter.controller;

import com.ecommerce.tienda_kalza.adapter.model.RegistroForm;
import com.ecommerce.tienda_kalza.adapter.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
            @Valid @ModelAttribute("registroForm") RegistroForm form,
            BindingResult result,
            Model model,
            RedirectAttributes flash) {

        // Si fallan las validaciones de longitud o formato de correo
        if (result.hasErrors()) {
            model.addAttribute("title", "Crear Cuenta | KALZA");
            model.addAttribute("currentPage", "registro");
            return "vistas/registro";
        }

        try {
            usuarioService.registrar(form);

            flash.addFlashAttribute("mensaje", "¡Tu cuenta ha sido creada exitosamente! Ya puedes iniciar sesión.");
            return "redirect:/login";

        } catch (IllegalArgumentException | IllegalStateException e) {
            model.addAttribute("errorGlobal", e.getMessage());
            model.addAttribute("title", "Crear Cuenta | KALZA");
            model.addAttribute("currentPage", "registro");
            return "vistas/registro";
        }
    }
}