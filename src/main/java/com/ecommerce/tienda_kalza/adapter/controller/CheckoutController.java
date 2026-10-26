package com.ecommerce.tienda_kalza.adapter.controller;

import com.ecommerce.tienda_kalza.adapter.repository.PuntoEntregaRepository;
import com.ecommerce.tienda_kalza.adapter.service.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class CheckoutController {
    private final CarritoService carrito;
    private final CheckoutService checkout;
    private final MetodoPagoService metodos;
    private final PuntoEntregaRepository puntos;

    @GetMapping("/checkout")
    public String mostrar(HttpSession session, Model model) {
        Integer idUsuario = (Integer) session.getAttribute("idUsuario");
        if (idUsuario == null) return "redirect:/login";
        model.addAttribute("resumen", carrito.consultar(session));
        model.addAttribute("metodos", metodos.listarPorUsuario(idUsuario));
        model.addAttribute("puntos", puntos.findByEstaActivoTrueOrderByNombreAsc());
        model.addAttribute("title", "Confirmar compra | KALZA");
        model.addAttribute("currentPage", "carrito");
        return "vistas/checkout";
    }

    @PostMapping("/checkout/confirmar")
    public String confirmar(@RequestParam int idMetodoPago, @RequestParam int idPuntoEntrega,
                            @RequestParam boolean pagoAprobado, HttpSession session, RedirectAttributes flash) {
        Integer idUsuario = (Integer) session.getAttribute("idUsuario");
        if (idUsuario == null) return "redirect:/login";
        // Dos clics de la misma sesión no pueden confirmar el mismo carrito simultáneamente.
        synchronized (session) {
            try {
                var resumen = carrito.consultar(session);
                if (!resumen.disponible()) throw new ReglaNegocioException("Revisa tu carrito: está vacío o tiene artículos no disponibles.");
                int idCompra = checkout.confirmar(idUsuario, resumen.items(), idMetodoPago, idPuntoEntrega, pagoAprobado);
                // Se limpia únicamente después de que la transacción terminó correctamente.
                carrito.vaciar(session);
                flash.addFlashAttribute("mensaje", "Pago simulado aprobado. Tu compra fue registrada.");
                return "redirect:/compras/" + idCompra + "/boleta";
            } catch (ReglaNegocioException e) {
                flash.addFlashAttribute("error", e.getMessage());
                return "redirect:/checkout";
            } catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.addFlashAttribute("error", "No se pudo registrar la compra. Revisa tus datos e inténtalo nuevamente.");
                return "redirect:/checkout";
            }
        }
    }

    @GetMapping("/compras/{idCompra}/boleta")
    public String boleta(@PathVariable int idCompra, HttpSession session, Model model, RedirectAttributes flash) {
        Integer idUsuario = (Integer) session.getAttribute("idUsuario");
        if (idUsuario == null) return "redirect:/login";
        try {
            model.addAttribute("boleta", checkout.obtenerBoleta(idCompra, idUsuario));
            model.addAttribute("title", "Boleta de compra | KALZA");
            model.addAttribute("currentPage", "pedidos");
            return "vistas/boleta";
        } catch (ReglaNegocioException e) {
            flash.addFlashAttribute("error", e.getMessage());
            return "redirect:/pedidos";
        }
    }
}
