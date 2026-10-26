package com.ecommerce.tienda_kalza.adapter.controller;

import com.ecommerce.tienda_kalza.adapter.model.*;
import com.ecommerce.tienda_kalza.adapter.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/zapatos")
@RequiredArgsConstructor
public class ZapatoAdminController {
    private final ZapatoService zapatos;
    private final VarianteZapatoService variantes;
    private final ZapatoMaterialService asociaciones;
    private final AdministracionCalzadoService consultas;

    @GetMapping
    public String listar(@RequestParam(defaultValue = "0") int pagina, Model model) {
        model.addAttribute("page", consultas.listar(pagina));
        model.addAttribute("productos", consultas.productosDisponibles());
        if (!model.containsAttribute("zapato")) model.addAttribute("zapato", new Zapato());
        model.addAttribute("title", "Calzados");
        model.addAttribute("currentPage", "zapatos");
        return "admin/zapatos";
    }

    @PostMapping("/crear")
    public String crear(@Valid @ModelAttribute("zapato") Zapato zapato, BindingResult result,
                        Model model, RedirectAttributes flash) {
        if (!result.hasErrors()) {
            try {
                zapatos.guardar(null, zapato);
                flash.addFlashAttribute("mensaje", "Calzado registrado. Ya puedes administrar sus variantes y materiales.");
                return "redirect:/admin/zapatos";
            } catch (IllegalArgumentException | IllegalStateException e) {
                result.reject("zapato", e.getMessage());
            } catch (DataIntegrityViolationException e) {
                result.reject("zapato", "El producto ya está vinculado o no es válido.");
            }
        }
        return listar(0, model);
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable int id, Model model) {
        var zapato = zapatos.obtener(id);
        model.addAttribute("zapato", zapato);
        model.addAttribute("nombre", consultas.nombre(id));
        model.addAttribute("variantes", variantes.listarPorZapato(id));
        model.addAttribute("composicion", consultas.composicion(id));
        model.addAttribute("materiales", consultas.materialesDisponibles(id));
        if (!model.containsAttribute("variante")) {
            VarianteZapato variante = new VarianteZapato();
            variante.setIdZapato(id);
            model.addAttribute("variante", variante);
        }
        model.addAttribute("title", "Variantes y materiales");
        model.addAttribute("currentPage", "zapatos");
        return "admin/zapato-detalle";
    }

    @GetMapping("/{id}/variantes/{idVariante}/editar")
    public String editarVariante(@PathVariable int id, @PathVariable int idVariante, Model model,
                                 RedirectAttributes flash) {
        try {
            comprobarPertenencia(id, idVariante);
            model.addAttribute("variante", variantes.obtener(idVariante));
            return detalle(id, model);
        } catch (IllegalArgumentException | IllegalStateException e) {
            flash.addFlashAttribute("error", e.getMessage());
            return volver(id);
        }
    }

    @PostMapping({"/{id}/variantes/crear", "/{id}/variantes/{idVariante}/actualizar"})
    public String guardarVariante(@PathVariable int id, @PathVariable(required = false) Integer idVariante,
                                  @Valid @ModelAttribute("variante") VarianteZapato variante, BindingResult result,
                                  Model model, RedirectAttributes flash) {
        variante.setIdVariante(idVariante == null ? 0 : idVariante);
        if (variante.getIdZapato() != id) result.reject("variante", "El calzado del formulario no coincide con la ruta.");
        if (!result.hasErrors()) {
            try {
                if (idVariante != null) comprobarPertenencia(id, idVariante);
                variantes.guardar(idVariante, variante);
                flash.addFlashAttribute("mensaje", "Variante guardada correctamente.");
                return volver(id);
            } catch (IllegalArgumentException | IllegalStateException e) {
                result.reject("variante", e.getMessage());
            } catch (DataIntegrityViolationException e) {
                result.reject("variante", "La combinación de talla y color ya existe o los datos no son válidos.");
            }
        }
        return detalle(id, model);
    }

    @PostMapping("/{id}/variantes/{idVariante}/estado")
    public String estadoVariante(@PathVariable int id, @PathVariable int idVariante,
                                 @RequestParam boolean activo, RedirectAttributes flash) {
        return ejecutar(id, flash, () -> {
            comprobarPertenencia(id, idVariante);
            if (activo) variantes.activar(idVariante); else variantes.desactivar(idVariante);
        }, "Estado de variante actualizado.");
    }

    @PostMapping("/{id}/materiales/asignar")
    public String asignar(@PathVariable int id, @RequestParam int idMaterial, RedirectAttributes flash) {
        return ejecutar(id, flash, () -> asociaciones.asignar(new ZapatoMaterial(id, idMaterial)),
                "Material asignado correctamente.");
    }

    @PostMapping("/{id}/materiales/{idMaterial}/retirar")
    public String retirar(@PathVariable int id, @PathVariable int idMaterial, RedirectAttributes flash) {
        return ejecutar(id, flash, () -> asociaciones.retirar(id, idMaterial), "Asociación retirada.");
    }

    @PostMapping("/{id}/desactivar")
    public String desactivar(@PathVariable int id, RedirectAttributes flash) {
        ejecutar(id, flash, () -> zapatos.desactivar(id), "Calzado desactivado. Se conservó su información.");
        return "redirect:/admin/zapatos";
    }

    private void comprobarPertenencia(int id, int idVariante) {
        if (variantes.obtener(idVariante).getIdZapato() != id)
            throw new IllegalArgumentException("La variante no pertenece a este calzado.");
    }

    private String ejecutar(int id, RedirectAttributes flash, Runnable operacion, String mensaje) {
        try {
            operacion.run();
            flash.addFlashAttribute("mensaje", mensaje);
        } catch (IllegalArgumentException | IllegalStateException e) {
            flash.addFlashAttribute("error", e.getMessage());
        } catch (jakarta.validation.ConstraintViolationException e) {
            flash.addFlashAttribute("error", "Selecciona identificadores válidos.");
        } catch (DataIntegrityViolationException e) {
            flash.addFlashAttribute("error", "No se pudo completar la operación: comprueba las asociaciones.");
        }
        return volver(id);
    }

    private String volver(int id) { return "redirect:/admin/zapatos/" + id; }

    @ExceptionHandler(IllegalArgumentException.class)
    public String registroInexistente(IllegalArgumentException e, RedirectAttributes flash) {
        flash.addFlashAttribute("error", e.getMessage());
        return "redirect:/admin/zapatos";
    }
}
