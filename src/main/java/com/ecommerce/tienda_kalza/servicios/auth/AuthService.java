package com.ecommerce.tienda_kalza.servicios.auth;

import com.ecommerce.tienda_kalza.dtos.auth.AuthResponse;
import com.ecommerce.tienda_kalza.dtos.auth.LoginRequest;
import com.ecommerce.tienda_kalza.dtos.auth.RegisterRequest;
import com.ecommerce.tienda_kalza.modelos.Usuario;
import com.ecommerce.tienda_kalza.repositorios.UsuarioRepository;
import com.ecommerce.tienda_kalza.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getCorreo(), request.getClave())
        );

        Usuario usuario = (Usuario) authentication.getPrincipal();

        if (!usuario.getEstaActivo() || !usuario.getEstaVerificado()) {
            throw new IllegalStateException("Cuenta no verificada o desactivada");
        }

        String token = jwtUtil.generateToken(usuario);

        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setExpiraEn(jwtUtil.extractExpiration(token).getTime() - System.currentTimeMillis());
        response.setUsuario(AuthResponse.UsuarioDTO.fromEntity(usuario));
        return response;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (!request.getClave().equals(request.getConfirmarClave())) {
            throw new IllegalArgumentException("Las contraseñas no coinciden");
        }

        if (usuarioRepository.existsByCorreo(request.getCorreo())) {
            throw new IllegalArgumentException("El correo ya está registrado");
        }

        Usuario usuario = new Usuario();
        usuario.setNombres(request.getNombres());
        usuario.setApellidos(request.getApellidos());
        usuario.setCorreo(request.getCorreo());
        usuario.setClave(passwordEncoder.encode(request.getClave()));
        usuario.setRol("cliente");
        usuario.setEstaVerificado(false);
        usuario.setEstaActivo(true);
        usuario.setEstaBaneado(false);

        usuario = usuarioRepository.save(usuario);

        String token = jwtUtil.generateToken(usuario);

        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setExpiraEn(jwtUtil.extractExpiration(token).getTime() - System.currentTimeMillis());
        response.setUsuario(AuthResponse.UsuarioDTO.fromEntity(usuario));
        return response;
    }

    @Transactional(readOnly = true)
    public Usuario validarTokenYObtenerUsuario(String token) {
        String correo = jwtUtil.extractUsername(token);
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new BadCredentialsException("Usuario no encontrado"));

        if (!jwtUtil.validateToken(token, usuario)) {
            throw new BadCredentialsException("Token inválido o expirado");
        }

        return usuario;
    }

    public AuthResponse refreshToken(String token) {
        Usuario usuario = validarTokenYObtenerUsuario(token);
        String nuevoToken = jwtUtil.generateToken(usuario);

        AuthResponse response = new AuthResponse();
        response.setToken(nuevoToken);
        response.setExpiraEn(jwtUtil.extractExpiration(nuevoToken).getTime() - System.currentTimeMillis());
        response.setUsuario(AuthResponse.UsuarioDTO.fromEntity(usuario));
        return response;
    }

    @Transactional
    public void cambiarClave(Integer usuarioId, String claveActual, String claveNueva) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        if (!passwordEncoder.matches(claveActual, usuario.getClave())) {
            throw new BadCredentialsException("Contraseña actual incorrecta");
        }

        usuario.setClave(passwordEncoder.encode(claveNueva));
        usuarioRepository.save(usuario);
    }

    @Transactional
    public void solicitarResetClave(String correo) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // TODO: Generar token de reset (TokenDeUnSoloUso tipo "RESET_PASSWORD")
        // TODO: Enviar email con enlace /reset-password?token=xxx
    }

    @Transactional
    public void resetearClave(String token, String claveNueva) {
        // TODO: Validar token (TokenDeUnSoloUso tipo "RESET_PASSWORD", no usado, no expirado)
        // TODO: Obtener usuario del token
        // TODO: Actualizar clave
        // TODO: Marcar token como usado
    }
}