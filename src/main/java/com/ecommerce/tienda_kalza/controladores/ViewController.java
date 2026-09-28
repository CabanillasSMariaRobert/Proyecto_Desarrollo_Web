package com.ecommerce.tienda_kalza.controladores;

import com.ecommerce.tienda_kalza.dtos.auth.LoginRequest;
import com.ecommerce.tienda_kalza.dtos.auth.RegisterRequest;
import com.ecommerce.tienda_kalza.servicios.CatalogoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ViewController {

    private final CatalogoService catalogoService;

    public ViewController(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("title", "KALZA | Iniciar Sesión");
        model.addAttribute("currentPage", "login");
        model.addAttribute("loginRequest", new LoginRequest());
        return "vistas/login";
    }

    @GetMapping("/registro")
    public String registro(Model model) {
        model.addAttribute("title", "KALZA | Crear Cuenta");
        model.addAttribute("currentPage", "registro");
        model.addAttribute("registerRequest", new RegisterRequest());
        return "vistas/registro";
    }

    @GetMapping("/catalogo")
    public String catalogo(@RequestParam(defaultValue = "1") int pagina, Model model) {
        model.addAttribute("title", "KALZA | Catálogo de Zapatillas");
        model.addAttribute("currentPage", "catalogo");
        
        model.addAttribute("productos", catalogoService.obtenerProductos(pagina));
        model.addAttribute("categorias", catalogoService.obtenerCategorias());
        model.addAttribute("tallas", catalogoService.obtenerTallas());
        model.addAttribute("ordenamientos", catalogoService.obtenerOrdenamientos());
        model.addAttribute("rangoPrecio", catalogoService.obtenerRangoPrecio());
        model.addAttribute("paginacion", catalogoService.obtenerPaginacion(pagina));
        
        return "vistas/catalogo";
    }
}
