package com.ecommerce.tienda_kalza.adapter.controller;

import com.ecommerce.tienda_kalza.adapter.service.CatalogoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class CatalogoController {
    private final CatalogoService catalogo;

    @GetMapping("/catalogo")
    public String listar(@RequestParam(defaultValue = "0") int pagina,
                         @RequestParam(defaultValue = "") String buscar, Model model) {
        model.addAttribute("page", catalogo.listar(pagina, buscar));
        model.addAttribute("buscar", buscar);
        model.addAttribute("title", "Catálogo | KALZA");
        model.addAttribute("currentPage", "catalogo");
        return "vistas/catalogo";
    }

    @GetMapping({"/catalogo/{id}", "/producto/{id}"})
    public String detalle(@PathVariable int id, Model model) {
        var producto = catalogo.detalle(id);
        model.addAttribute("producto", producto);
        model.addAttribute("title", producto.nombre() + " | KALZA");
        model.addAttribute("currentPage", "catalogo");
        return "vistas/producto";
    }
}
