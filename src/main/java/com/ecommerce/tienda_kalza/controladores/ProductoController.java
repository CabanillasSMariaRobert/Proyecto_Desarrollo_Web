package com.ecommerce.tienda_kalza.controladores;

import com.ecommerce.tienda_kalza.servicios.ProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Detalle de producto.
 *
 * Hay dos rutas a proposito: las tarjetas del catalogo apuntan a /producto sin
 * id porque sus datos todavia son de prueba, y /producto/{id} ya recibe el id
 * que usara el catalogo real. Las dos muestran la misma vista.
 */
@Controller
public class ProductoController {

    private final ProductoService productos;

    public ProductoController(ProductoService productos) {
        this.productos = productos;
    }

    @GetMapping("/producto")
    public String productoPorDefecto(Model model) {
        return cargarProducto(model, null);
    }

    @GetMapping("/producto/{id}")
    public String producto(@PathVariable Long id, Model model) {
        return cargarProducto(model, id);
    }

    private String cargarProducto(Model model, Long id) {
        model.addAttribute("title", "KALZA | Detalle de Producto");
        model.addAttribute("currentPage", "catalogo");
        model.addAttribute("producto", productos.obtenerDetalle(id));
        model.addAttribute("guiaTallas", productos.obtenerGuiaTallas());
        return "vistas/producto";
    }
}
