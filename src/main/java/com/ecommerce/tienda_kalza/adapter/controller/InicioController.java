package com.ecommerce.tienda_kalza.adapter.controller;

import com.ecommerce.tienda_kalza.adapter.repository.CategoriaRepository;
import com.ecommerce.tienda_kalza.adapter.service.CatalogoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class InicioController {
    private final CatalogoService catalogo;
    private final CategoriaRepository categorias;

    @GetMapping({"/", "/index"})
    public String inicio(Model model) {
        model.addAttribute("title", "Inicio | KALZA");
        model.addAttribute("currentPage", "inicio");
        model.addAttribute("categorias", categorias.findByEstaActivoTrueOrderByNombreAsc());
        model.addAttribute("destacados", catalogo.listar(0, "").getContent().stream().limit(4).toList());
        return "vistas/index";
    }
}
