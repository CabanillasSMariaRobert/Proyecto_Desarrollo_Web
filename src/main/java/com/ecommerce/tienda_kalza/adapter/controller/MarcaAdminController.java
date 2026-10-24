package com.ecommerce.tienda_kalza.adapter.controller;

import com.ecommerce.tienda_kalza.adapter.model.Marca;
import com.ecommerce.tienda_kalza.adapter.service.MarcaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller @RequestMapping("/admin/marcas") @RequiredArgsConstructor
public class MarcaAdminController {
    private final MarcaService service;

    @GetMapping
    public String listar(@RequestParam(defaultValue = "0") int pagina, Model model) {
        model.addAttribute("page", service.listar(pagina));
        return vista(model);
    }

    @GetMapping("/nueva")
    public String nueva(Model model) {
        model.addAttribute("marca", new Marca());
        return listar(0, model);
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable int id, Model model, RedirectAttributes flash) {
        try {
            model.addAttribute("marca", service.obtener(id));
            return listar(0, model);
        } catch (IllegalArgumentException e) {
            flash.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/marcas";
        }
    }

    @PostMapping({"/crear", "/{id}/actualizar"})
    public String guardar(@PathVariable(required = false) Integer id,
                          @Valid @ModelAttribute("marca") Marca marca, BindingResult result,
                          Model model, RedirectAttributes flash) {
        marca.setIdMarca(id == null ? 0 : id);
        if (!result.hasErrors()) {
            try {
                service.guardar(id, marca);
                flash.addFlashAttribute("mensaje", "Marca guardada correctamente.");
                return "redirect:/admin/marcas";
            } catch (IllegalArgumentException | IllegalStateException e) {
                result.reject("marca", e.getMessage());
            } catch (DataIntegrityViolationException e) {
                result.reject("marca", "No se pudo guardar: comprueba que el nombre no esté registrado.");
            }
        }
        return listar(0, model);
    }

    private String vista(Model model) {
        model.addAttribute("title", "Marcas");
        model.addAttribute("currentPage", "productos");
        return "admin/marcas";
    }
}
