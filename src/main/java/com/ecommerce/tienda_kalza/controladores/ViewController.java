package com.ecommerce.tienda_kalza.controladores;

import com.ecommerce.tienda_kalza.dtos.auth.LoginRequest;
import com.ecommerce.tienda_kalza.dtos.auth.RegisterRequest;
import com.ecommerce.tienda_kalza.servicios.CatalogoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Pantallas de autenticacion y listado de catalogo.
 *
 * Login y registro son las unicas que usan datos reales: el form se enlaza con
 * el request de AuthController contra /api/auth. El catalogo es de prueba y
 * sale del CatalogoService.
 */
@Controller
public class ViewController {

    private final CatalogoService catalogo;

    public ViewController(CatalogoService catalogo) {
        this.catalogo = catalogo;
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

    /**
     * Catalogo paginado. La pagina se valida contra el total y se recorta en
     * vez de tirar excepcion: un ?pagina=99 en la barra de direcciones tiene
     * que mostrar la ultima pagina, no un error 500.
     */
    @GetMapping("/catalogo")
    public String catalogo(@RequestParam(defaultValue = "1") int pagina, Model model) {
        int totalPaginas = catalogo.obtenerTotalPaginas();
        int actual = Math.min(Math.max(pagina, 1), totalPaginas);

        model.addAttribute("title", "KALZA | Catálogo de Zapatillas");
        model.addAttribute("currentPage", "catalogo");
        model.addAttribute("productos", catalogo.obtenerProductos(actual));
        model.addAttribute("categoriasFiltro", catalogo.obtenerCategorias());
        model.addAttribute("tallas", catalogo.obtenerTallas());
        model.addAttribute("ordenamientos", catalogo.obtenerOrdenamientos());
        model.addAttribute("rangoPrecio", catalogo.obtenerRangoPrecio());
        model.addAttribute("paginacion", catalogo.obtenerPaginacion(actual));
        model.addAttribute("totalProductos", catalogo.obtenerProductos().size());
        model.addAttribute("paginaActual", actual);
        return "vistas/catalogo";
    }
}
