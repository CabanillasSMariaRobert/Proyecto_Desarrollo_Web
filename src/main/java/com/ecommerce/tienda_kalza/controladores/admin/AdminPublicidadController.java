package com.ecommerce.tienda_kalza.controladores.admin;

import com.ecommerce.tienda_kalza.servicios.PublicidadAdminService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminPublicidadController {

    private final PublicidadAdminService publicidadAdminService;

    public AdminPublicidadController(PublicidadAdminService publicidadAdminService) {
        this.publicidadAdminService = publicidadAdminService;
    }

    @GetMapping("/admin/publicidad")
    public String listarBanners(Model model) {
        model.addAttribute("title", "Publicidad");
        model.addAttribute("currentPage", "publicidad");
        model.addAttribute("banners", publicidadAdminService.obtenerTodos());
        return "admin/publicidad";
    }
}
