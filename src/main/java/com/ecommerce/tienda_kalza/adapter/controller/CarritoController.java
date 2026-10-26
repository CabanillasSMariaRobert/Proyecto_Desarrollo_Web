package com.ecommerce.tienda_kalza.adapter.controller;

import com.ecommerce.tienda_kalza.adapter.entity.UsuarioEntity;
import com.ecommerce.tienda_kalza.adapter.enums.RedUsada;
import com.ecommerce.tienda_kalza.adapter.model.MetodoPago;
import com.ecommerce.tienda_kalza.adapter.repository.UsuarioRepository;
import com.ecommerce.tienda_kalza.adapter.service.MetodoPagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/carrito")
@RequiredArgsConstructor
public class CarritoController {

    private static final String REDIRECT_CARRITO = "redirect:/carrito";

    private final MetodoPagoService metodoPagoService;
    private final UsuarioRepository usuarioRepository;

    @GetMapping
    public String verCarrito(Model model) {
        // Variables requeridas por fragments/general/layout.html
        model.addAttribute("title", "Tu Carrito | KALZA");
        model.addAttribute("currentPage", "carrito");

        // 1. Obtener el usuario actual de forma segura (primer usuario disponible en la BD)
        Integer idUsuarioActual = obtenerIdUsuarioActual();

        // 2. Cargar métodos de pago guardados del usuario (protegido contra fallos)
        try {
            List<MetodoPago> metodosPago = metodoPagoService.listarPorUsuario(idUsuarioActual);
            model.addAttribute("metodosPago", metodosPago != null ? metodosPago : List.of());
        } catch (Exception e) {
            model.addAttribute("metodosPago", List.of());
        }
        model.addAttribute("redes", RedUsada.values());

        // 3. Datos de productos en el carrito para la visualización
        List<Map<String, Object>> items = List.of(
                Map.of(
                        "imagen", "AeroGlide Pro Runner.webp",
                        "nombre", "AeroGlide Pro Runner",
                        "talla", "10.5",
                        "color", "Negro Obsidiana",
                        "precioUnitario", "145.00",
                        "cantidad", 1
                ),
                Map.of(
                        "imagen", "Court Classic Low.webp",
                        "nombre", "Court Classic Low",
                        "talla", "9",
                        "color", "Blanco Nube",
                        "precioUnitario", "95.00",
                        "cantidad", 2
                )
        );

        Map<String, Object> resumen = Map.of(
                "totalItems", 3,
                "subtotal", "335.00",
                "envioTexto", "Gratis",
                "igv", "60.30",
                "total", "395.30"
        );

        model.addAttribute("items", items);
        model.addAttribute("resumen", resumen);

        return "vistas/carrito";
    }

    // Guardar nuevo método de pago desde el carrito
    @PostMapping("/metodo-pago/guardar")
    public String guardarMetodoPago(@ModelAttribute("metodoPago") MetodoPago metodoPago, RedirectAttributes flash) {
        try {
            Integer idUsuario = obtenerIdUsuarioActual();
            metodoPagoService.guardar(metodoPago, idUsuario);
            flash.addFlashAttribute("mensaje", "Método de pago guardado correctamente.");
        } catch (Exception e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return REDIRECT_CARRITO;
    }

    // Eliminar método de pago desde el carrito
    @PostMapping("/metodo-pago/{id}/eliminar")
    public String eliminarMetodoPago(@PathVariable Integer id, RedirectAttributes flash) {
        try {
            Integer idUsuario = obtenerIdUsuarioActual();
            metodoPagoService.eliminar(id, idUsuario);
            flash.addFlashAttribute("mensaje", "Tarjeta eliminada correctamente.");
        } catch (Exception e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return REDIRECT_CARRITO;
    }

    private Integer obtenerIdUsuarioActual() {
        return usuarioRepository.findAll().stream()
                .findFirst()
                .map(UsuarioEntity::getIdUsuario)
                .orElse(1);
    }
}