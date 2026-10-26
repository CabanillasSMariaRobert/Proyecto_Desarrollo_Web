package com.ecommerce.tienda_kalza.adapter.controller;

import com.ecommerce.tienda_kalza.adapter.service.PuntosDeEntregaService;
import com.ecommerce.tienda_kalza.adapter.service.PuntosDeEntregaService.Formulario;
import com.ecommerce.tienda_kalza.adapter.service.ReglaNegocioException;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/puntos-de-entregas")
public class PuntosDeEntregaController {
    private final PuntosDeEntregaService servicio;

    public PuntosDeEntregaController(PuntosDeEntregaService servicio) {
        this.servicio = servicio;
    }

    @InitBinder("puntoForm")
    public void limitarCampos(WebDataBinder binder) {
        binder.setAllowedFields("nombre", "direccion", "referencia", "latitud", "longitud", "estaActivo");
    }

    @GetMapping
    public String listar(@RequestParam(name = "pagina", defaultValue = "0") int pagina, Model model) {
        var puntos = servicio.listar(pagina);
        model.addAttribute("puntos", puntos.getContent());
        model.addAttribute("paginaActual", puntos.getNumber());
        model.addAttribute("totalPaginas", Math.max(1, puntos.getTotalPages()));
        model.addAttribute("totalPuntos", puntos.getTotalElements());
        model.addAttribute("desde", puntos.isEmpty() ? 0 : puntos.getPageable().getOffset() + 1);
        model.addAttribute("hasta", puntos.isEmpty() ? 0 : puntos.getPageable().getOffset() + puntos.getNumberOfElements());
        if (!model.containsAttribute("puntoForm")) model.addAttribute("puntoForm", new Formulario());
        model.addAttribute("nuevoPunto", model.getAttribute("idEdicion") == null
                ? model.getAttribute("puntoForm") : new Formulario());
        model.addAttribute("title", "Puntos de entrega");
        model.addAttribute("currentPage", "puntos-de-entregas");
        return "admin/puntos-de-entregas";
    }

    @PostMapping({"/crear", "/{id}/actualizar"})
    public String guardar(@PathVariable(name = "id", required = false) Integer id,
                          @Valid @ModelAttribute("puntoForm") Formulario form, BindingResult errores,
                          @RequestParam(name = "pagina", defaultValue = "0") int pagina,
                          Model model, RedirectAttributes flash) {
        if (!errores.hasErrors()) {
            try {
                servicio.guardar(id, form);
                flash.addFlashAttribute("mensaje", id == null ? "Punto de entrega creado correctamente." : "Punto de entrega actualizado correctamente.");
                flash.addAttribute("pagina", Math.max(0, pagina));
                return "redirect:/admin/puntos-de-entregas";
            } catch (ReglaNegocioException e) {
                errores.reject("punto", e.getMessage());
            } catch (DataIntegrityViolationException e) {
                errores.reject("punto", "No se pudo guardar el punto de entrega. Revisa sus datos e inténtalo nuevamente.");
            }
        }
        model.addAttribute("idEdicion", id);
        model.addAttribute("abrirModal", id == null ? "crearPuntoModal" : "editarPunto-" + id);
        model.addAttribute("erroresFormulario", errores.getAllErrors().stream()
                .map(error -> "typeMismatch".equals(error.getCode())
                        ? "La latitud y la longitud deben ser números válidos." : error.getDefaultMessage())
                .distinct().toList());
        return listar(pagina, model);
    }
}
