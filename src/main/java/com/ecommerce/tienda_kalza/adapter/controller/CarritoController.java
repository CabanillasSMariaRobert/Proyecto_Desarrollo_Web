package com.ecommerce.tienda_kalza.adapter.controller;

import com.ecommerce.tienda_kalza.adapter.enums.RedUsada;
import com.ecommerce.tienda_kalza.adapter.model.MetodoPago;
import jakarta.servlet.http.HttpSession;
import com.ecommerce.tienda_kalza.adapter.service.MetodoPagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/carrito")
@RequiredArgsConstructor
public class CarritoController {

    private static final String REDIRECT_CARRITO = "redirect:/carrito";

    private final MetodoPagoService metodoPagoService;
    private final com.ecommerce.tienda_kalza.adapter.service.CarritoService carrito;

    @GetMapping
    public String verCarrito(Model model, jakarta.servlet.http.HttpSession session) {
        model.addAttribute("title", "Tu Carrito | KALZA");
        model.addAttribute("currentPage", "carrito");

        Integer idUsuarioActual = obtenerIdUsuarioActual(session);

        // Cargar metodos de pago guardados
        try {
            List<MetodoPago> metodosPago = idUsuarioActual == null ? List.of() : metodoPagoService.listarPorUsuario(idUsuarioActual);
            model.addAttribute("metodosPago", metodosPago != null ? metodosPago : List.of());
        } catch (Exception e) {
            model.addAttribute("metodosPago", List.of());
        }
        model.addAttribute("redes", RedUsada.values());

        var resumen = carrito.consultar(session);
        model.addAttribute("items", resumen.items());
        model.addAttribute("resumen", resumen);

        return "vistas/carrito";
    }

    // Guardar nuevo metodo de pago
    @PostMapping("/metodo-pago/guardar")
    public String guardarMetodoPago(@ModelAttribute("metodoPago") MetodoPago metodoPago,
                                    HttpSession session, RedirectAttributes flash) {
        Integer idUsuario = obtenerIdUsuarioActual(session);
        if (idUsuario == null) return "redirect:/login";
        try {
            metodoPagoService.guardar(metodoPago, idUsuario);
            flash.addFlashAttribute("mensaje", "Método de pago guardado correctamente.");
        } catch (Exception e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return REDIRECT_CARRITO;
    }

    // Eliminar metodo de pago
    @PostMapping("/metodo-pago/{id}/eliminar")
    public String eliminarMetodoPago(@PathVariable Integer id, HttpSession session, RedirectAttributes flash) {
        Integer idUsuario = obtenerIdUsuarioActual(session);
        if (idUsuario == null) return "redirect:/login";
        try {
            metodoPagoService.eliminar(id, idUsuario);
            flash.addFlashAttribute("mensaje", "Tarjeta eliminada correctamente.");
        } catch (Exception e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return REDIRECT_CARRITO;
    }

    @PostMapping("/agregar")
    public String agregar(@RequestParam int idVariante, @RequestParam int cantidad,
                          jakarta.servlet.http.HttpSession session, RedirectAttributes flash) {
        try {
            carrito.agregar(session, idVariante, cantidad);
            flash.addFlashAttribute("mensaje", "Artículo agregado al carrito.");
        } catch (com.ecommerce.tienda_kalza.adapter.service.ReglaNegocioException e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return REDIRECT_CARRITO;
    }

    @PostMapping("/{idVariante}/actualizar")
    public String actualizar(@PathVariable int idVariante, @RequestParam int cantidad,
                             jakarta.servlet.http.HttpSession session, RedirectAttributes flash) {
        try {
            carrito.actualizar(session, idVariante, cantidad);
            flash.addFlashAttribute("mensaje", "Cantidad actualizada.");
        } catch (com.ecommerce.tienda_kalza.adapter.service.ReglaNegocioException e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return REDIRECT_CARRITO;
    }

    @PostMapping("/{idVariante}/retirar")
    public String retirar(@PathVariable int idVariante, jakarta.servlet.http.HttpSession session) {
        carrito.retirar(session, idVariante);
        return REDIRECT_CARRITO;
    }

    private Integer obtenerIdUsuarioActual(HttpSession session) {
        if (session.getAttribute("usuarioLogueado") == null) return null;
        return (Integer) session.getAttribute("idUsuario");
    }
}
