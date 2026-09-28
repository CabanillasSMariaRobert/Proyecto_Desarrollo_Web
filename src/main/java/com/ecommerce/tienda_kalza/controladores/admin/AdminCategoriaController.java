package com.ecommerce.tienda_kalza.controladores.admin;

import com.ecommerce.tienda_kalza.dto.CategoriaAdmin;
import com.ecommerce.tienda_kalza.servicios.CategoriaAdminService;
import com.ecommerce.tienda_kalza.servicios.Paginacion;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AdminCategoriaController {

    private static final int POR_PAGINA = 4;

    private final CategoriaAdminService categoriaAdminService;

    public AdminCategoriaController(CategoriaAdminService categoriaAdminService) {
        this.categoriaAdminService = categoriaAdminService;
    }

    @GetMapping("/admin/categorias")
    public String listarCategorias(
            @RequestParam(name = "pagina", defaultValue = "0") int pagina,
            Model model) {

        List<CategoriaAdmin> todas = categoriaAdminService.obtenerTodos();
        int totalPaginas = Paginacion.totalPaginas(todas.size(), POR_PAGINA);
        int paginaActual = Math.min(Math.max(pagina, 0), totalPaginas - 1);

        model.addAttribute("title", "Categorías");
        model.addAttribute("currentPage", "categorias");
        model.addAttribute("categorias", Paginacion.paginar(todas, paginaActual, POR_PAGINA));
        model.addAttribute("paginaActual", paginaActual);
        model.addAttribute("totalPaginas", totalPaginas);
        model.addAttribute("totalCategorias", todas.size());
        model.addAttribute("desde", Math.min(paginaActual * POR_PAGINA + 1, todas.size()));
        model.addAttribute("hasta", Math.min((paginaActual + 1) * POR_PAGINA, todas.size()));
        return "admin/categorias";
    }
}
