package com.ecommerce.tienda_kalza.adapter.controller;

import com.ecommerce.tienda_kalza.adapter.enums.RolUsuario;
import com.ecommerce.tienda_kalza.adapter.model.LoginForm;
import com.ecommerce.tienda_kalza.adapter.model.Usuario;
import com.ecommerce.tienda_kalza.adapter.service.UsuarioService;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final UsuarioService usuarioService;

    @GetMapping("/login")
    public String mostrarLogin(Model model, HttpSession session) {
        // Si el usuario ya está autenticado en la sesión, lo redirigimos a su sección correspondiente
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuarioLogueado != null) {
            return redirigirSegunRol(usuarioLogueado);
        }

        if (!model.containsAttribute("loginForm")) {
            model.addAttribute("loginForm", new LoginForm());
        }
        model.addAttribute("title", "Iniciar Sesión | KALZA");
        model.addAttribute("currentPage", "login");

        return "vistas/login";
    }

    @PostMapping("/login")
    public String procesarLogin(
            @ModelAttribute("loginForm") LoginForm form,
            HttpSession session,
            Model model) {

        try {
            Usuario usuario = usuarioService.autenticar(form.getCorreo(), form.getClave());

            session.removeAttribute("modoAcceso");

            // Guardar atributos clave en la sesión HTTP
            session.setAttribute("usuarioLogueado", usuario);
            session.setAttribute("idUsuario", usuario.getIdUsuario());
            session.setAttribute("nombreCompleto", usuario.getNombres() + " " + usuario.getApellidos());
            session.setAttribute("rolUsuario", usuario.getRol().name());

            // Redirigir según el rol
            return redirigirSegunRol(usuario);

        } catch (IllegalArgumentException | IllegalStateException e) {
            // Este atributo "errorGlobal" es el que ya lee login.html
            model.addAttribute("errorGlobal", e.getMessage());
            model.addAttribute("title", "Iniciar Sesión | KALZA");
            model.addAttribute("currentPage", "login");
            return "vistas/login";
        }
    }

    @GetMapping("/logout")
    public String cerrarSesion(HttpServletRequest request, HttpServletResponse response, RedirectAttributes flash) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // Destruye completamente la sesión
        }

        // Forzamos cabeceras anti-caché en la respuesta de logout
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        flash.addFlashAttribute("mensaje", "Has cerrado sesión correctamente.");
        return "redirect:/login";
    }

    private String redirigirSegunRol(Usuario usuario) {
        if (usuario.getRol() == RolUsuario.ADMINISTRADOR) {
            return "redirect:/seleccionar-modo";
        }
        return "redirect:/catalogo";
    }
}
