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

        if (!usuario.getEstaActivo()) {
            throw new IllegalStateException("Cuenta desactivada");
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
}