package com.ecommerce.tienda_kalza.controladores;

import org.springframework.stereotype.Controller;
import com.ecommerce.tienda_kalza.servicios.publico.CuentaService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Area del cliente. Sirve las dos vistas de cuenta: /perfil es la que enlaza el
 * header, /usuario es la version extendida con datos de envio y tarjetas.
 * Los datos son de prueba: no salen del usuario autenticado.
 */
@Controller
public class CuentaController {
    private final CuentaService cuentaService;

    public CuentaController(CuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }



    @GetMapping("/perfil")
    public String perfil(Model model) {
        model.addAttribute("title", "KALZA | Mi Cuenta");
        model.addAttribute("currentPage", "perfil");
        model.addAttribute("usuario", cuentaService.obtenerPerfil());
        model.addAttribute("pedidos", cuentaService.obtenerPedidosRecientes());
        return "vistas/perfil";
    }

    @GetMapping("/usuario")
    public String usuario(Model model) {
        model.addAttribute("title", "KALZA | Panel de Usuario");
        model.addAttribute("currentPage", "usuario");
        model.addAttribute("usuario", cuentaService.obtenerPerfil());
        return "vistas/usuario";
    }
}
