package com.ecommerce.tienda_kalza.adapter.controller;

import com.ecommerce.tienda_kalza.adapter.model.Entrega;
import com.ecommerce.tienda_kalza.adapter.model.Usuario;
import com.ecommerce.tienda_kalza.adapter.service.EntregasService;
import com.ecommerce.tienda_kalza.adapter.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/perfil")
@RequiredArgsConstructor
public class PerfilController {

    private final UsuarioService usuarioService;
    private final EntregasService entregasService;

    public record PedidoReciente(
            String codigo,
            String nombre,
            String claseEstado,
            String estado,
            String imagen,
            String total,
            String accion,
            String textoAccion
    ) {}

    @GetMapping
    public String verPerfil(Model model, HttpSession session) {
        // 1. Obtener ID del usuario autenticado en la sesión
        Integer idUsuario = (session != null) ? (Integer) session.getAttribute("idUsuario") : null;
        if (idUsuario == null) {
            return "redirect:/login";
        }

        // 2. Obtener datos reales de ese usuario
        Usuario usuario;
        try {
            usuario = usuarioService.obtener(idUsuario);
        } catch (Exception e) {
            session.invalidate();
            return "redirect:/login";
        }

        model.addAttribute("perfil", usuario);
        model.addAttribute("title", "Mi Perfil | KALZA");
        model.addAttribute("currentPage", "perfil");

        // 3. Cargar compras/pedidos del usuario logueado
        List<PedidoReciente> pedidosRecientes = new ArrayList<>();
        try {
            Page<Entrega> entregas = entregasService.listarPorUsuario(idUsuario, 0, 4);
            if (entregas != null && !entregas.isEmpty()) {
                for (Entrega entrega : entregas.getContent()) {
                    String codigo = "#" + entrega.getId();
                    String nombre = (entrega.getCompra() != null && !entrega.getCompra().getDetalles().isEmpty())
                            ? entrega.getCompra().getDetalles().get(0).getNombreDelProducto()
                            : "Calzado KALZA";
                    String estado = entrega.getEstado() != null ? entrega.getEstado().name() : "PENDIENTE";
                    String claseEstado = switch (entrega.getEstado()) {
                        case ENTREGADO -> "bg-success text-white";
                        case EN_CAMINO -> "bg-info text-dark";
                        case CANCELADO, FALLADO -> "bg-danger text-white";
                        default -> "bg-warning text-dark";
                    };
                    String total = (entrega.getCompra() != null && entrega.getCompra().getTotalConDescuento() != null)
                            ? entrega.getCompra().getTotalConDescuento().toString()
                            : "0.00";

                    pedidosRecientes.add(new PedidoReciente(
                            codigo,
                            nombre,
                            claseEstado,
                            estado,
                            "AeroGlide Pro Runner.webp",
                            total,
                            "/pedidos",
                            "Ver detalles"
                    ));
                }
            }
        } catch (Exception ignored) {
        }

        model.addAttribute("pedidosRecientes", pedidosRecientes);
        return "vistas/perfil";
    }

    @PostMapping("/actualizar")
    public String actualizarPerfil(
            @RequestParam String nombres,
            @RequestParam String apellidos,
            @RequestParam String correo,
            @RequestParam(required = false) String telefono,
            HttpSession session,
            RedirectAttributes flash) {

        Integer idUsuario = (session != null) ? (Integer) session.getAttribute("idUsuario") : null;
        if (idUsuario == null) {
            return "redirect:/login";
        }

        try {
            Usuario model = usuarioService.obtener(idUsuario);
            model.setNombres(nombres);
            model.setApellidos(apellidos);
            model.setCorreo(correo);
            model.setTelefono(telefono);

            usuarioService.actualizar(idUsuario, model);

            // Refrescar los datos actualizados en la sesión
            session.setAttribute("usuarioLogueado", model);
            session.setAttribute("nombreCompleto", nombres.trim() + " " + apellidos.trim());

            flash.addFlashAttribute("mensaje", "Tus datos personales se actualizaron correctamente.");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "No se pudo actualizar el perfil: " + e.getMessage());
        }

        return "redirect:/perfil";
    }
}