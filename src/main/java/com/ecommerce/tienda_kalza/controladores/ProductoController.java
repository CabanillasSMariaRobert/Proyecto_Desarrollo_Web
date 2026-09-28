package com.ecommerce.tienda_kalza.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Detalle de producto.
 *
 * Hay dos rutas a proposito: las tarjetas del catalogo apuntan a /producto sin
 * id porque todavia son datos de prueba, y /producto/{id} queda lista para
 * cuando el producto venga de la base. Las dos muestran la misma vista.
 */
@Controller
public class ProductoController {

    @GetMapping("/producto")
    public String productoPorDefecto(Model model) {
        return cargarProducto(model, null);
    }

    @GetMapping("/producto/{id}")
    public String producto(@PathVariable Integer id, Model model) {
        return cargarProducto(model, id);
    }

    private String cargarProducto(Model model, Integer id) {
        model.addAttribute("title", "KALZA | Detalle de Producto");
        model.addAttribute("currentPage", "catalogo");
        model.addAttribute("productoId", id);
        return "vistas/producto";
    }
}
