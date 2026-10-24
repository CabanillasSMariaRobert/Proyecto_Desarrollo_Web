package com.ecommerce.tienda_kalza.adapter.controller;

// Autor: Samuel

import com.ecommerce.tienda_kalza.adapter.entity.CompraEntity;
import com.ecommerce.tienda_kalza.adapter.repository.UsuarioRepository;
import com.ecommerce.tienda_kalza.adapter.service.CompraService;
import com.ecommerce.tienda_kalza.adapter.service.ReglaNegocioException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/compras")
@RequiredArgsConstructor
public class CompraAdminController {

    private static final String LISTA = "redirect:/admin/compras";
    private static final Integer TAMANO_PAGINA = 10;

    private final CompraService service;
    private final UsuarioRepository usuarioRepository;

    // READ (LISTAR Y PAGINAR)
    @GetMapping
    public String listar(
            @RequestParam(name = "buscar", required = false) String buscar,
            @RequestParam(defaultValue = "0") Integer pagina,
            Model model) {

        List<CompraEntity> todas = service.listar();
        int paginaActual = pagina != null && pagina >= 0 ? pagina : 0;
        int inicio = paginaActual * TAMANO_PAGINA;
        int fin = Math.min(inicio + TAMANO_PAGINA, todas.size());

        List<CompraEntity> contenido = todas.isEmpty() ? List.of() : todas.subList(inicio, fin);
        Page<CompraEntity> page = new PageImpl<>(contenido, PageRequest.of(paginaActual, TAMANO_PAGINA), todas.size());

        long total = page.getTotalElements();
        int inicioMostrar = page.getNumber() * TAMANO_PAGINA;

        model.addAttribute("compras", page.getContent());
        model.addAttribute("buscar", buscar != null ? buscar : "");
        model.addAttribute("currentPage", "compras");
        model.addAttribute("title", "Compras");
        model.addAttribute("paginaActual", page.getNumber());
        model.addAttribute("totalPaginas", page.getTotalPages());
        model.addAttribute("totalCompras", total);
        model.addAttribute("desde", total == 0 ? 0 : inicioMostrar + 1);
        model.addAttribute("hasta", inicioMostrar + page.getNumberOfElements());
        model.addAttribute("usuarios", usuarioRepository.findAll());
        model.addAttribute("estados", CompraService.ESTADOS);

        return "admin/compras";
    }

    // CREATE
    @PostMapping("/crear")
    public String crear(@ModelAttribute("compra") CompraEntity compra, RedirectAttributes flash) {
        try {
            if (compra.getEstado() == null || compra.getEstado().isBlank()) {
                compra.setEstado("pendiente");
            }
            service.guardar(compra);
            flash.addFlashAttribute("mensaje", "Compra registrada correctamente.");
        } catch (ReglaNegocioException e) {
            flash.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return LISTA;
    }

    // UPDATE
    @PostMapping("/{id}/actualizar")
    public String actualizar(@PathVariable int id, @ModelAttribute("compra") CompraEntity compra, RedirectAttributes flash) {
        try {
            compra.setIdCompra(id);
            service.guardar(compra);
            flash.addFlashAttribute("mensaje", "Compra actualizada correctamente.");
        } catch (ReglaNegocioException e) {
            flash.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return LISTA;
    }

    // CAMBIAR ESTADO (siguiendo el patrón de cambiarEstado/bloquear)
    @PostMapping("/{id}/estado")
    public String cambiarEstado(@PathVariable int id, @RequestParam("nuevoEstado") String nuevoEstado, RedirectAttributes flash) {
        try {
            service.cambiarEstado(id, nuevoEstado);
            flash.addFlashAttribute("mensaje", "Estado de la compra actualizado correctamente.");
        } catch (ReglaNegocioException e) {
            flash.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return LISTA;
    }

    // DELETE
    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable int id, RedirectAttributes flash) {
        try {
            service.eliminar(id);
            flash.addFlashAttribute("mensaje", "Compra eliminada correctamente.");
        } catch (ReglaNegocioException e) {
            flash.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return LISTA;
    }
}
