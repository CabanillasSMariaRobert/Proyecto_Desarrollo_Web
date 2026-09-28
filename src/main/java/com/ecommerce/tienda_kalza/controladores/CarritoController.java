package com.ecommerce.tienda_kalza.controladores;

import com.ecommerce.tienda_kalza.servicios.CarritoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Carrito de compras. No hay sesion de usuario todavia, asi que el carrito no
 * se persiste: cada carga devuelve los mismos items de prueba.
 */
@Controller
public class CarritoController {

    private final CarritoService carrito;

    public CarritoController(CarritoService carrito) {
        this.carrito = carrito;
    }

    @GetMapping("/carrito")
    public String carrito(Model model) {
        model.addAttribute("title", "KALZA | Carrito de Compras");
        model.addAttribute("currentPage", "carrito");
        model.addAttribute("items", carrito.obtenerItems());
        model.addAttribute("resumen", carrito.obtenerResumen());
        return "vistas/carrito";
    }
}
