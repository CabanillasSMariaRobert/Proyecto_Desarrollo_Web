package com.ecommerce.tienda_kalza.adapter.controller;

import com.ecommerce.tienda_kalza.adapter.model.OlvideClaveForm;
import com.ecommerce.tienda_kalza.adapter.model.ReiniciarClaveForm;
import com.ecommerce.tienda_kalza.adapter.service.ReinicioClaveService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ReinicioClaveController {
    private final ReinicioClaveService servicio;

    public ReinicioClaveController(ReinicioClaveService servicio) {
        this.servicio = servicio;
    }

    @GetMapping("/olvide-mi-clave")
    public String mostrarOlvido(Model model, HttpServletResponse response) {
        protegerRespuesta(response);
        model.addAttribute("olvideClaveForm", new OlvideClaveForm());
        return vistaOlvido(model);
    }

    @PostMapping("/olvide-mi-clave")
    public String solicitar(@Valid @ModelAttribute("olvideClaveForm") OlvideClaveForm form,
                            BindingResult errores, Model model, RedirectAttributes flash,
                            HttpServletResponse response) {
        protegerRespuesta(response);
        if (!errores.hasErrors()) {
            try {
                servicio.solicitar(form.getCorreo());
                flash.addFlashAttribute("mensaje", "Te enviamos un enlace para reiniciar tu contraseña. Revisa tu correo y la carpeta de spam.");
                return "redirect:/olvide-mi-clave";
            } catch (IllegalArgumentException e) {
                model.addAttribute("errorGlobal", e.getMessage());
            } catch (MailException e) {
                model.addAttribute("errorGlobal", "No pudimos enviar el correo en este momento. Inténtalo más tarde.");
            }
        }
        return vistaOlvido(model);
    }

    @GetMapping("/reiniciar-clave")
    public String mostrarReinicio(@RequestParam(name = "token", required = false) String token,
                                  Model model, HttpServletResponse response) {
        protegerRespuesta(response);
        var form = new ReiniciarClaveForm();
        form.setToken(token);
        model.addAttribute("reiniciarClaveForm", form);
        return vistaReinicio(model, token);
    }

    @PostMapping("/reiniciar-clave")
    public String reiniciar(@Valid @ModelAttribute("reiniciarClaveForm") ReiniciarClaveForm form,
                            BindingResult errores, Model model, RedirectAttributes flash,
                            HttpServletRequest request, HttpServletResponse response) {
        protegerRespuesta(response);
        if (!errores.hasErrors()) {
            try {
                servicio.reiniciar(form.getToken(), form.getClave(), form.getConfirmarClave());
                var sesion = request.getSession(false);
                if (sesion != null) {
                    sesion.invalidate();
                }
                flash.addFlashAttribute("mensaje", "Tu contraseña fue actualizada. Ya puedes iniciar sesión con tu nueva contraseña.");
                return "redirect:/login";
            } catch (IllegalArgumentException e) {
                model.addAttribute("errorGlobal", e.getMessage());
            }
        }
        form.setClave(null);
        form.setConfirmarClave(null);
        return vistaReinicio(model, form.getToken());
    }

    private String vistaOlvido(Model model) {
        model.addAttribute("title", "Olvidé mi contraseña | KALZA");
        model.addAttribute("currentPage", "login");
        return "vistas/olvide-mi-clave";
    }

    private String vistaReinicio(Model model, String token) {
        model.addAttribute("title", "Reiniciar contraseña | KALZA");
        model.addAttribute("currentPage", "login");
        boolean valido = servicio.tokenValido(token);
        model.addAttribute("tokenValido", valido);
        if (token == null || token.isBlank()) {
            model.addAttribute("errorGlobal", "Necesitas el token enviado a tu correo para reiniciar tu contraseña. Abre el enlace que recibiste.");
        } else if (!valido) {
            model.addAttribute("errorGlobal", "El enlace no es válido, ya fue utilizado o ha vencido. Solicita uno nuevo.");
        }
        return "vistas/reiniciar-clave";
    }

    private void protegerRespuesta(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Referrer-Policy", "no-referrer");
    }
}
