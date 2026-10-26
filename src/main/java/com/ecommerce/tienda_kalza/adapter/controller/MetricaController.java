package com.ecommerce.tienda_kalza.adapter.controller;

import com.ecommerce.tienda_kalza.adapter.service.MetricaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MetricaController {
    private final MetricaService servicio;

    public MetricaController(MetricaService servicio) {
        this.servicio = servicio;
    }

    @GetMapping({"/admin", "/admin/", "/admin/metricas"})
    public String mostrar(Model modelo) {
        modelo.addAttribute("metricas", servicio.obtener());
        modelo.addAttribute("title", "Métricas");
        modelo.addAttribute("currentPage", "metricas");
        return "admin/metricas";
    }
}
