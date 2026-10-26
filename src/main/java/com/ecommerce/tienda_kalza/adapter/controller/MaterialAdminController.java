package com.ecommerce.tienda_kalza.adapter.controller;

import com.ecommerce.tienda_kalza.adapter.model.Material;
import com.ecommerce.tienda_kalza.adapter.service.MaterialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/materiales")
@RequiredArgsConstructor
public class MaterialAdminController {
    private final MaterialService service;

    @GetMapping
    public String listar(@RequestParam(defaultValue = "0") int pagina, Model model) {
        model.addAttribute("page", service.listar(pagina));
        if (!model.containsAttribute("material")) model.addAttribute("material", new Material());
        model.addAttribute("title", "Materiales");
        model.addAttribute("currentPage", "materiales");
        return "admin/materiales";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable int id, Model model, RedirectAttributes flash) {
        try {
            model.addAttribute("material", service.obtener(id));
            return listar(0, model);
        } catch (IllegalArgumentException e) {
            flash.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/materiales";
        }
    }

    @PostMapping({"/crear", "/{id}/actualizar"})
    public String guardar(@PathVariable(required = false) Integer id,
                          @Valid @ModelAttribute("material") Material material, BindingResult result,
                          Model model, RedirectAttributes flash) {
        material.setIdMaterial(id == null ? 0 : id);
        if (!result.hasErrors()) {
            try {
                service.guardar(id, material);
                flash.addFlashAttribute("mensaje", "Material guardado correctamente.");
                return "redirect:/admin/materiales";
            } catch (IllegalArgumentException | IllegalStateException e) {
                result.reject("material", e.getMessage());
            } catch (DataIntegrityViolationException e) {
                result.reject("material", "Ya existe un material con ese nombre o los datos no son válidos.");
            }
        }
        return listar(0, model);
    }

    @PostMapping("/{id}/desactivar")
    public String desactivar(@PathVariable int id, RedirectAttributes flash) {
        try {
            service.desactivar(id);
            flash.addFlashAttribute("mensaje", "Material desactivado. Se conservaron sus asociaciones.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            flash.addFlashAttribute("error", e.getMessage());
        } catch (DataIntegrityViolationException e) {
            flash.addFlashAttribute("error", "No se pudo desactivar el material.");
        }
        return "redirect:/admin/materiales";
    }
}
