package com.ecommerce.tienda_kalza.adapter.controller;

import com.ecommerce.tienda_kalza.adapter.model.Usuario;
import com.ecommerce.tienda_kalza.adapter.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/usuarios")
@RequiredArgsConstructor
public class UsuarioAdminController {

    private static final String LISTA = "redirect:/admin/usuarios";
    private static final Integer TAMANO_PAGINA = 10;

    private final UsuarioService service;

    // READ (LISTAR Y BUSCAR)
    @GetMapping
    public String listar(
            @RequestParam(name = "buscar", required = false) String buscar,
            @RequestParam(defaultValue = "0") Integer pagina,
            Model model) {

        Page<Usuario> page = service.listar(buscar, pagina, TAMANO_PAGINA);
        long total = page.getTotalElements();
        int inicio = page.getNumber() * TAMANO_PAGINA;

        model.addAttribute("usuarios", page.getContent());
        model.addAttribute("buscar", buscar != null ? buscar : "");
        model.addAttribute("currentPage", "usuarios");
        model.addAttribute("title", "Usuarios");
        model.addAttribute("paginaActual", page.getNumber());
        model.addAttribute("totalPaginas", page.getTotalPages());
        model.addAttribute("totalUsuarios", total);
        model.addAttribute("desde", total == 0 ? 0 : inicio + 1);
        model.addAttribute("hasta", inicio + page.getNumberOfElements());

        return "admin/usuarios";
    }

    // CREATE
    @PostMapping("/crear")
    public String crear(@ModelAttribute("usuario") Usuario usuario, RedirectAttributes flash) {
        try {
            service.crear(usuario);
            flash.addFlashAttribute("mensaje", "Usuario registrado correctamente.");
        } catch (Exception e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return LISTA;
    }

    // UPDATE
    @PostMapping("/{id}/actualizar")
    public String actualizar(@PathVariable Integer id, @ModelAttribute("usuario") Usuario usuario, RedirectAttributes flash) {
        try {
            service.actualizar(id, usuario);
            flash.addFlashAttribute("mensaje", "Usuario actualizado correctamente.");
        } catch (Exception e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return LISTA;
    }

    // RN05: ACTIVAR / DESACTIVAR (MANEJA LA REGLA DIRECTAMENTE)
    @PostMapping("/{id}/estado")
    public String cambiarEstado(@PathVariable Integer id, RedirectAttributes flash) {
        try {
            String mensaje = service.cambiarEstado(id);
            flash.addFlashAttribute("mensaje", mensaje);
        } catch (Exception e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return LISTA;
    }
}