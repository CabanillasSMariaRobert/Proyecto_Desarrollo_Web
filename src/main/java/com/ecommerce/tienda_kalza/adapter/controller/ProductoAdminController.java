package com.ecommerce.tienda_kalza.adapter.controller;

import com.ecommerce.tienda_kalza.adapter.model.Producto;
import com.ecommerce.tienda_kalza.adapter.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller @RequestMapping("/admin/productos") @RequiredArgsConstructor
public class ProductoAdminController {
    private final ProductoService service;

    @PostMapping("/{id}/estado")
    public String cambiarEstado(@PathVariable int id, @RequestParam boolean activo, RedirectAttributes flash) {
        try {
            service.cambiarEstado(id, activo);
            flash.addFlashAttribute("mensaje", activo ? "Producto activado correctamente." : "Producto desactivado correctamente.");
        } catch (IllegalArgumentException e) {
            flash.addFlashAttribute("error", e.getMessage());
        } catch (DataIntegrityViolationException e) {
            flash.addFlashAttribute("error", "No se pudo cambiar el estado del producto.");
        }
        return "redirect:/admin/productos";
    }

    @GetMapping
    public String listar(@RequestParam(defaultValue = "0") int pagina,
                         @RequestParam(defaultValue = "") String buscar, Model model) {
        model.addAttribute("page", service.listar(pagina, buscar));
        model.addAttribute("buscar", buscar);
        model.addAttribute("title", "Productos");
        model.addAttribute("currentPage", "productos");
        Producto formulario = model.getAttribute("producto") instanceof Producto p ? p : null;
        model.addAttribute("marcas", service.opcionesMarcas(formulario == null ? null : formulario.getIdProducto()));
        model.addAttribute("categorias", service.opcionesCategorias());
        if (model.getAttribute("producto") instanceof Producto producto && producto.getIdProducto() > 0) {
            model.addAttribute("categoriasConservadas", service.categoriasInactivasAsociadas(producto.getIdProducto()));
        } else {
            model.addAttribute("categoriasConservadas", java.util.List.of());
        }
        return "admin/productos";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("producto", new Producto());
        return listar(0, "", model);
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable int id, Model model, RedirectAttributes flash) {
        try {
            model.addAttribute("producto", service.obtener(id));
            return listar(0, "", model);
        } catch (IllegalArgumentException e) {
            flash.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/productos";
        }
    }

    @PostMapping({"/crear", "/{id}/actualizar"})
    public String guardar(@PathVariable(required = false) Integer id,
                          @Valid @ModelAttribute("producto") Producto producto, BindingResult result,
                          Model model, RedirectAttributes flash) {
        producto.setIdProducto(id == null ? 0 : id);
        if (!result.hasErrors()) {
            try {
                service.guardar(id, producto);
                flash.addFlashAttribute("mensaje", "Producto guardado correctamente.");
                return "redirect:/admin/productos";
            } catch (IllegalArgumentException | IllegalStateException e) {
                result.reject("producto", e.getMessage());
            } catch (DataIntegrityViolationException e) {
                result.reject("producto", "No se pudo guardar. Comprueba los datos y las relaciones seleccionadas.");
            }
        }
        return listar(0, "", model);
    }
}
