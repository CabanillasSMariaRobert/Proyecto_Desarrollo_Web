package com.ecommerce.tienda_kalza.controladores;

import com.ecommerce.tienda_kalza.dtos.auth.AuthResponse;
import com.ecommerce.tienda_kalza.dtos.auth.LoginForm;
import com.ecommerce.tienda_kalza.dtos.auth.LoginRequest;
import com.ecommerce.tienda_kalza.dtos.auth.RegisterRequest;
import com.ecommerce.tienda_kalza.dtos.auth.RegistroForm;
import com.ecommerce.tienda_kalza.servicios.auth.AuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Envio de los formularios de login y registro.
 *
 * Vive separado de {@link ViewController} porque ese solo renderiza: aca entra
 * la peticion, se valida, se arma el request del API y se decide que hacer con
 * la sesion.
 *
 * Los formularios se re-renderizan en vez de redirigir cuando hay error, porque
 * {@link BindingResult} no sobrevive un redirect. El precio es que la URL
 * queda en POST y un F5 reenvia, algo aceptable en una pantalla de formulario.
 */
@Controller
public class AuthViewController {

    public static final String ATRIBUTO_TOKEN = "token";
    public static final String ATRIBUTO_USUARIO = "usuario";
    public static final String ATRIBUTO_RECORDAR = "recordar";

    private static final String VISTA_LOGIN = "vistas/login";
    private static final String VISTA_REGISTRO = "vistas/registro";

    private final AuthService authService;

    public AuthViewController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/registro")
    public String registrar(@Valid @ModelAttribute("registroForm") RegistroForm form,
                            BindingResult binding,
                            Model model,
                            HttpSession session) {

        String[] nombre = dividirNombre(form.getNombreCompleto());
        if (nombre[1].isEmpty()) {
            binding.rejectValue("nombreCompleto", "nombre.incompleto", "Ingresa tu nombre y tu apellido");
            return reintentar(model, binding, VISTA_REGISTRO, "KALZA | Crear Cuenta", "registro");
        }

        if (binding.hasErrors()) {
            return reintentar(model, binding, VISTA_REGISTRO, "KALZA | Crear Cuenta", "registro");
        }

        RegisterRequest request = new RegisterRequest(
                nombre[0], nombre[1], form.getCorreo(), form.getClave(), form.getConfirmarClave());

        AuthResponse response;
        try {
            response = authService.register(request);
        } catch (IllegalArgumentException | DataIntegrityViolationException e) {
            binding.reject("global", e.getMessage());
            return reintentar(model, binding, VISTA_REGISTRO, "KALZA | Crear Cuenta", "registro");
        }

        guardarSesion(session, response, false);
        return "redirect:/catalogo";
    }

    @PostMapping("/login")
    public String iniciarSesion(@Valid @ModelAttribute("loginForm") LoginForm form,
                                BindingResult binding,
                                Model model,
                                HttpSession session) {

        if (binding.hasErrors()) {
            return reintentar(model, binding, VISTA_LOGIN, "KALZA | Iniciar Sesión", "login");
        }

        AuthResponse response;
        try {
            response = authService.login(new LoginRequest(form.getCorreo(), form.getClave()));
        } catch (DisabledException e) {
            binding.reject("global", "Tu cuenta está desactivada");
            return reintentar(model, binding, VISTA_LOGIN, "KALZA | Iniciar Sesión", "login");
        } catch (BadCredentialsException e) {
            binding.reject("global", "Correo o contraseña incorrectos");
            return reintentar(model, binding, VISTA_LOGIN, "KALZA | Iniciar Sesión", "login");
        } catch (IllegalStateException e) {
            binding.reject("global", e.getMessage());
            return reintentar(model, binding, VISTA_LOGIN, "KALZA | Iniciar Sesión", "login");
        }

        guardarSesion(session, response, form.isRecordar());
        return "redirect:/catalogo";
    }

    /**
     * "John Doe" -&gt; ["John", "Doe"]. Si no hay apellido devuelve la segunda
     * posicion vacia y el llamador lo rechaza, porque la columna de apellidos no
     * acepta nulos y sin esto el alta revienta con un 500.
     */
    private String[] dividirNombre(String nombreCompleto) {
        String limpio = nombreCompleto == null ? "" : nombreCompleto.trim().replaceAll("\\s+", " ");
        int corte = limpio.indexOf(' ');
        if (corte < 0) {
            return new String[]{limpio, ""};
        }
        return new String[]{limpio.substring(0, corte), limpio.substring(corte + 1).trim()};
    }

    /**
     * El token queda en sesion para que el frontend lo lea. Que se use para
     * autenticar de verdad es parte del trabajo de seguridad que todavia no esta
     * hecho: el JwtAuthenticationFilter no esta registrado en la cadena de
     * filtros y todas las rutas siguen permitidas.
     */
    private void guardarSesion(HttpSession session, AuthResponse response, boolean recordar) {
        session.setAttribute(ATRIBUTO_TOKEN, response.getToken());
        session.setAttribute(ATRIBUTO_USUARIO, response.getUsuario());
        session.setAttribute(ATRIBUTO_RECORDAR, recordar);
    }

    private String reintentar(Model model, BindingResult binding, String vista, String titulo, String pagina) {
        model.addAttribute("title", titulo);
        model.addAttribute("currentPage", pagina);
        model.addAttribute("errorGlobal", binding.hasGlobalErrors() ? primerErrorGlobal(binding) : null);
        return vista;
    }

    private String primerErrorGlobal(BindingResult binding) {
        return binding.getGlobalErrors().isEmpty() ? null : binding.getGlobalErrors().get(0).getDefaultMessage();
    }
}
