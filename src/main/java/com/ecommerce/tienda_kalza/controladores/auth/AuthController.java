package com.ecommerce.tienda_kalza.controladores.auth;

import com.ecommerce.tienda_kalza.dtos.auth.*;
import com.ecommerce.tienda_kalza.servicios.auth.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * POST /api/auth/login
     * 
     * Body:
     * {
     *   "correo": "usuario@ejemplo.com",
     *   "clave": "password123"
     * }
     * 
     * Response 200:
     * {
     *   "token": "eyJhbGciOiJIUzI1NiJ9...",
     *   "tipoToken": "Bearer",
     *   "expiraEn": 86400000,
     *   "usuario": {
     *     "idUsuario": 1,
     *     "nombres": "Juan",
     *     "apellidos": "Pérez",
     *     "correo": "usuario@ejemplo.com",
     *     "rol": "cliente",
     *     "estaVerificado": true
     *   }
     * }
     * 
     * Response 401: BadCredentialsException -> "Credenciales inválidas"
     * Response 400: Validation errors
     * Response 403: Cuenta no verificada/desactivada
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/auth/register
     * 
     * Body:
     * {
     *   "nombres": "Juan",
     *   "apellidos": "Pérez",
     *   "correo": "usuario@ejemplo.com",
     *   "clave": "password123",
     *   "confirmarClave": "password123"
     * }
     * 
     * Response 201: AuthResponse (igual que login - auto-login tras registro)
     * Response 400: Validaciones (correo existe, claves no coinciden, etc.)
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(201).body(response);
    }

    /**
     * POST /api/auth/refresh
     * Header: Authorization: Bearer <token>
     * 
     * Response 200: AuthResponse con nuevo token
     * Response 401: Token inválido/expirado
     */
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        AuthResponse response = authService.refreshToken(token);
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/auth/cambiar-clave
     * Header: Authorization: Bearer <token>
     * Body:
     * {
     *   "claveActual": "password123",
     *   "claveNueva": "nuevaPassword456"
     * }
     * 
     * Response 204: Sin contenido
     * Response 400: Clave actual incorrecta
     * Response 401: Token inválido
     */
    @PostMapping("/cambiar-clave")
    public ResponseEntity<Void> cambiarClave(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody CambiarClaveRequest request) {
        
        String token = authHeader.replace("Bearer ", "");
        // Obtener usuario del token (el filtro ya setea SecurityContext, pero podemos validar)
        authService.cambiarClave(
                authService.validarTokenYObtenerUsuario(token).getIdUsuario(),
                request.getClaveActual(),
                request.getClaveNueva()
        );
        return ResponseEntity.noContent().build();
    }

    /**
     * POST /api/auth/olvide-clave
     * Body: { "correo": "usuario@ejemplo.com" }
     * 
     * Response 204: Siempre 204 (no revelar si existe el correo)
     * Envía email con token de reset
     */
    @PostMapping("/olvide-clave")
    public ResponseEntity<Void> olvideClave(@Valid @RequestBody OlvideClaveRequest request) {
        authService.solicitarResetClave(request.getCorreo());
        return ResponseEntity.noContent().build();
    }

    /**
     * POST /api/auth/resetear-clave
     * Body:
     * {
     *   "token": "token-del-email",
     *   "claveNueva": "nuevaPassword456"
     * }
     * 
     * Response 204: Contraseña actualizada
     * Response 400: Token inválido/expirado/ya usado
     */
    @PostMapping("/resetear-clave")
    public ResponseEntity<Void> resetearClave(@Valid @RequestBody ResetearClaveRequest request) {
        authService.resetearClave(request.getToken(), request.getClaveNueva());
        return ResponseEntity.noContent().build();
    }
}