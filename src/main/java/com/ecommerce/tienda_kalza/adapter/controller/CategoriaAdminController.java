package com.ecommerce.tienda_kalza.adapter.controller;
import com.ecommerce.tienda_kalza.adapter.model.Categoria;
import com.ecommerce.tienda_kalza.adapter.service.CategoriaService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/categorias")
@RequiredArgsConstructor
public class CategoriaAdminController {
    private static final String LISTA = "redirect:/admin/categorias";
    private static final int TAMANO_PAGINA = 10;

    private final CategoriaService service;

    // CREATE
    @PostMapping("/crear")
    public String crear(@Valid @ModelAttribute("categoria") Categoria categoria, BindingResult result, RedirectAttributes flash){
        if(result.hasErrors()){
            flash.addFlashAttribute("error", result.getFieldErrors().get(0).getDefaultMessage());
            return LISTA;
        }
        try {
            service.crear(categoria);
            flash.addFlashAttribute("mensaje", "Categoria creada correctamente");
        } catch (IllegalStateException e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return LISTA;
    }

    // READ
    @GetMapping
    public String listar(@RequestParam(defaultValue = "0") int pagina, Model model) {
        Page<Categoria> page = service.listar(pagina, TAMANO_PAGINA);
        long total = page.getTotalElements();
        int inicio = page.getNumber() * TAMANO_PAGINA;

        model.addAttribute("categorias", page.getContent());
        model.addAttribute("paginaActual", page.getNumber());
        model.addAttribute("totalPaginas", page.getTotalPages());
        model.addAttribute("totalCategorias", total);
        model.addAttribute("desde", total == 0 ? 0 : inicio + 1);
        model.addAttribute("hasta", inicio + page.getNumberOfElements());
        return "admin/categorias";
    }

    // UPDATE
    @PostMapping("/{id}/actualizar")
    public String actualizar(@PathVariable Integer id, @Valid @ModelAttribute("categoria") Categoria categoria, BindingResult result, RedirectAttributes flash) {
        if (result.hasErrors()) {
            flash.addFlashAttribute("error", result.getFieldErrors().get(0).getDefaultMessage());
            return LISTA;
        }
        try {
            service.actualizar(id, categoria);
            flash.addFlashAttribute("mensaje", "Categoría actualizada correctamente.");
        } catch (IllegalStateException | IllegalArgumentException e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return LISTA;
    }

    // DELETE
    @PostMapping("/{id}/estado")
    public String cambiarEstado(@PathVariable Integer id, RedirectAttributes flash) {
        try {
            boolean activa = service.eliminar(id);
            flash.addFlashAttribute("mensaje",
                    activa ? "Categoría activada correctamente." : "Categoría desactivada correctamente.");
        } catch (IllegalStateException | IllegalArgumentException e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return LISTA;
    }
}
