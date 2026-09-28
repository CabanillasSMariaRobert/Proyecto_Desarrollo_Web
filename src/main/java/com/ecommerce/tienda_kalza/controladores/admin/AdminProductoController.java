package com.ecommerce.tienda_kalza.controladores.admin;

import com.ecommerce.tienda_kalza.dto.ProductoAdmin;
import com.ecommerce.tienda_kalza.servicios.Paginacion;
import com.ecommerce.tienda_kalza.servicios.ProductoAdminService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AdminProductoController {

    private static final int POR_PAGINA = 3;

    private final ProductoAdminService productoAdminService;

    public AdminProductoController(ProductoAdminService productoAdminService) {
        this.productoAdminService = productoAdminService;
    }

    @GetMapping("/admin/productos")
    public String listarProductos(
            @RequestParam(name = "buscar", required = false) String buscar,
            @RequestParam(name = "pagina", defaultValue = "0") int pagina,
            Model model) {

        List<ProductoAdmin> filtrados = productoAdminService.buscar(buscar);
        int totalPaginas = Paginacion.totalPaginas(filtrados.size(), POR_PAGINA);
        int paginaActual = Math.min(Math.max(pagina, 0), totalPaginas - 1);

        model.addAttribute("title", "Productos");
        model.addAttribute("currentPage", "productos");
        model.addAttribute("productos", Paginacion.paginar(filtrados, paginaActual, POR_PAGINA));
        model.addAttribute("buscar", buscar == null ? "" : buscar);
        model.addAttribute("paginaActual", paginaActual);
        model.addAttribute("totalPaginas", totalPaginas);
        model.addAttribute("totalProductos", filtrados.size());
        model.addAttribute("desde", Math.min(paginaActual * POR_PAGINA + 1, filtrados.size()));
        model.addAttribute("hasta", Math.min((paginaActual + 1) * POR_PAGINA, filtrados.size()));
        return "admin/productos";
    }
}
