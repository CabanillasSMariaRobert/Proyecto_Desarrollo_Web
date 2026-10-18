package com.ecommerce.tienda_kalza.controladores.admin;

import com.ecommerce.tienda_kalza.servicios.MetricasService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminPanelController {

    private final MetricasService metricasService;

    public AdminPanelController(MetricasService metricasService) {
        this.metricasService = metricasService;
    }

    @GetMapping({"/admin", "/admin/metricas"})
    public String obtenerPaginaDeMetricas(Model model) {
        model.addAttribute("title", "Panel General");
        model.addAttribute("currentPage", "metricas");
        model.addAttribute("metricas", metricasService.obtenerResumen());
        return "admin/metricas";
    }
}
