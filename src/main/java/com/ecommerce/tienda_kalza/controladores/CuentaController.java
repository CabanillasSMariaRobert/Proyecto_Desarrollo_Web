package com.ecommerce.tienda_kalza.controladores;

import com.ecommerce.tienda_kalza.servicios.CuentaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Area del cliente. Sirve las dos vistas de cuenta: /perfil es la que enlaza el
 * header y /usuario la version extendida con resumenes y accesos rapidos.
 * Los datos son de prueba: no salen del usuario autenticado.
 */
@Controller
public class CuentaController {

    private final CuentaService cuenta;

    public CuentaController(CuentaService cuenta) {
        this.cuenta = cuenta;
    }

    @GetMapping("/perfil")
    public String perfil(Model model) {
        model.addAttribute("title", "KALZA | Mi Cuenta");
        model.addAttribute("currentPage", "perfil");
        model.addAttribute("perfil", cuenta.obtenerPerfil());
        model.addAttribute("pedidosRecientes", cuenta.obtenerPedidosRecientes());
        return "vistas/perfil";
    }

    @GetMapping("/usuario")
    public String usuario(Model model) {
        model.addAttribute("title", "KALZA | Panel de Usuario");
        model.addAttribute("currentPage", "usuario");
        model.addAttribute("saludo", cuenta.obtenerSaludo());
        model.addAttribute("resumenes", cuenta.obtenerResumenes());
        model.addAttribute("accesos", cuenta.obtenerAccesos());
        return "vistas/usuario";
    }
}
