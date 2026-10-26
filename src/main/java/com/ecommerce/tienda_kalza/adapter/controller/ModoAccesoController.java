package com.ecommerce.tienda_kalza.adapter.controller;

import com.ecommerce.tienda_kalza.adapter.enums.RolUsuario;
import com.ecommerce.tienda_kalza.adapter.model.Usuario;
import com.ecommerce.tienda_kalza.adapter.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/** Selección de navegación. No modifica el rol de la cuenta ni sustituye Spring Security. */
@Controller
@RequiredArgsConstructor
public class ModoAccesoController {
    private final UsuarioService usuarios;

    public enum Modo { CLIENTE, ADMINISTRADOR }

    @GetMapping("/seleccionar-modo")
    public String mostrar(HttpSession session, Model model) {
        Usuario usuario = usuarioActual(session);
        if (usuario == null) return "redirect:/login";
        if (usuario.getRol() != RolUsuario.ADMINISTRADOR) return "redirect:/catalogo";
        model.addAttribute("title", "Elige cómo ingresar | KALZA");
        model.addAttribute("currentPage", "modo");
        model.addAttribute("nombreUsuario", usuario.getNombres());
        return "vistas/seleccionar-modo";
    }

    @PostMapping("/seleccionar-modo")
    public String seleccionar(@RequestParam Modo modo, HttpSession session) {
        Usuario usuario = usuarioActual(session);
        if (usuario == null) return "redirect:/login";
        if (usuario.getRol() != RolUsuario.ADMINISTRADOR) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Solo los administradores pueden elegir modo.");
        }
        session.setAttribute("modoAcceso", modo.name());
        return modo == Modo.ADMINISTRADOR ? "redirect:/admin" : "redirect:/catalogo";
    }

    private Usuario usuarioActual(HttpSession session) {
        Object id = session.getAttribute("idUsuario");
        if (!(id instanceof Integer idUsuario) || session.getAttribute("usuarioLogueado") == null) return null;
        try {
            Usuario usuario = usuarios.obtener(idUsuario);
            if (!Boolean.TRUE.equals(usuario.getEstaActivo()) || Boolean.TRUE.equals(usuario.getEstaBaneado())) {
                session.invalidate();
                return null;
            }
            return usuario;
        } catch (IllegalArgumentException e) {
            session.invalidate();
            return null;
        }
    }
}
