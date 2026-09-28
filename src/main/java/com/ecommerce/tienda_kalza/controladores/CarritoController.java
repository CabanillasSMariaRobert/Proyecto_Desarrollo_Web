package com.ecommerce.tienda_kalza.controladores;

import org.springframework.stereotype.Controller;
import com.ecommerce.tienda_kalza.servicios.publico.CarritoService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Carrito de compras. Los items son datos de prueba: no hay sesion de usuario
 * todavia, asi que el carrito no se persiste.
 */
@Controller
public class CarritoController {
    private final CarritoService carritoService;

    public CarritoController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }



    @GetMapping("/carrito")
    public String carrito(Model model) {
        model.addAttribute("title", "KALZA | Carrito de Compras");
        model.addAttribute("currentPage", "carrito");
        model.addAttribute("resumen", carritoService.obtenerResumen());
        return "vistas/carrito";
    }
}
