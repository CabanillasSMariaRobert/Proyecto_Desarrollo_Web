package com.ecommerce.tienda_kalza.adapter.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Desactivar el caché del navegador
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
        response.setHeader("Pragma", "no-cache");                                  // HTTP 1.0
        response.setDateHeader("Expires", 0);                                       // Proxies

        // Verificar si hay sesión activa
        HttpSession session = request.getSession(false);
        Object usuario = (session != null) ? session.getAttribute("usuarioLogueado") : null;

        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }

        // Control de rol
        String uri = request.getRequestURI();
        String rol = (String) session.getAttribute("rolUsuario");
        if (uri.startsWith(request.getContextPath() + "/admin") && !"ADMINISTRADOR".equalsIgnoreCase(rol)) {
            response.sendRedirect(request.getContextPath() + "/");
            return false;
        }

        return true;
    }
}