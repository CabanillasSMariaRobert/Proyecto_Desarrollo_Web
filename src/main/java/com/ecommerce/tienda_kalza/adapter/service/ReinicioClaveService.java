package com.ecommerce.tienda_kalza.adapter.service;

import com.ecommerce.tienda_kalza.adapter.entity.TokenEntity;
import com.ecommerce.tienda_kalza.adapter.enums.TipoToken;
import com.ecommerce.tienda_kalza.adapter.repository.TokenRepository;
import com.ecommerce.tienda_kalza.adapter.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.HexFormat;

@Service
@Transactional(readOnly = true)
public class ReinicioClaveService {
    private static final TipoToken TIPO = TipoToken.RESTABLECIMIENTO_CLAVE;
    private static final ZoneId ZONA = ZoneId.of("America/Lima");
    private static final String TOKEN_INVALIDO = "El enlace no es válido, ya fue utilizado o ha vencido. Solicita uno nuevo.";
    private final TokenRepository tokenRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder codificador;
    private final EmailService emailService;
    private final String urlBase;
    private final int minutosExpiracion;
    private final SecureRandom aleatorio = new SecureRandom();

    public ReinicioClaveService(TokenRepository tokens, UsuarioRepository usuarios, PasswordEncoder codificador,
                               EmailService emailService,
                               @Value("${app.reinicio-clave.url-base:http://localhost:8080}") String urlBase,
                               @Value("${app.reinicio-clave.minutos-expiracion:60}") int minutosExpiracion) {
        URI base = URI.create(urlBase);
        if (!("https".equals(base.getScheme()) || "http".equals(base.getScheme()))
                || base.getHost() == null || minutosExpiracion < 1) {
            throw new IllegalArgumentException("Configura una URL base válida y una duración positiva para el reinicio de clave.");
        }
        this.tokenRepository = tokens;
        this.usuarioRepository = usuarios;
        this.codificador = codificador;
        this.emailService = emailService;
        this.urlBase = urlBase.replaceAll("/+$", "");
        this.minutosExpiracion = minutosExpiracion;
    }

    @Transactional
    public void solicitar(String correo) {
        if (correo == null || correo.isBlank()) {
            throw new IllegalArgumentException("Ingresa tu correo electrónico.");
        }
        var usuario = tokenRepository.buscarUsuarioPorCorreo(correo.trim())
                .orElseThrow(() -> new IllegalArgumentException("No existe un usuario con dicho correo electrónico."));
        var horaActual = LocalDateTime.now(ZONA);

        byte[] bytes = new byte[32];
        aleatorio.nextBytes(bytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        tokenRepository.invalidarAnteriores(usuario.getIdUsuario(), TIPO, horaActual);

        // Se persiste el hash; el token original solo se incluye en el correo.
        tokenRepository.save(new TokenEntity(TIPO, hash(token), horaActual, horaActual.plusMinutes(minutosExpiracion), usuario));
        String enlace = UriComponentsBuilder.fromUriString(urlBase).path("/reiniciar-clave")
                .queryParam("token", token).build().toUriString();
        String cuerpo = """
                <html lang="es"><body style="font-family:Arial,sans-serif;color:#1a1c1c;line-height:1.6">
                  <h1 style="font-size:24px">Reinicia tu contraseña de KALZA</h1>
                  <p>Hola, %s:</p>
                  <p>Recibimos una solicitud para reiniciar la contraseña de tu cuenta.</p>
                  <p><a href="%s" style="background:#0035c6;color:#fff;padding:12px 20px;border-radius:8px;display:inline-block;text-decoration:none">Reiniciar contraseña</a></p>
                  <p>El enlace vence en %d minutos y solo se puede utilizar una vez.</p>
                  <p>Si el botón no funciona, copia este enlace en tu navegador:<br><a href="%s">%s</a></p>
                  <p>Si no solicitaste este cambio, puedes ignorar el correo. Tu contraseña seguirá siendo la misma.</p>
                </body></html>
                """.formatted(HtmlUtils.htmlEscape(usuario.getNombres()), HtmlUtils.htmlEscape(enlace), minutosExpiracion,
                HtmlUtils.htmlEscape(enlace), HtmlUtils.htmlEscape(enlace));
        emailService.enviar(usuario.getCorreo(), "Reinicia tu contraseña | KALZA", cuerpo);
    }

    public boolean tokenValido(String token) {
        if (!formatoValido(token)) {
            return false;
        }
        return tokenRepository.findByTokenAndTipo(hash(token), TIPO).filter(this::vigente).isPresent();
    }

    @Transactional
    public void reiniciar(String token, String clave, String confirmarClave) {
        if (!formatoValido(token)) {
            throw new IllegalArgumentException(TOKEN_INVALIDO);
        }
        validarClave(clave, confirmarClave);
        String resumen = hash(token);
        int id = tokenRepository.buscarIdUsuario(resumen, TIPO)
                .orElseThrow(() -> new IllegalArgumentException(TOKEN_INVALIDO));
        var usuario = tokenRepository.buscarUsuarioPorId(id)
                .orElseThrow(() -> new IllegalArgumentException(TOKEN_INVALIDO));
        var tokenEnUso = tokenRepository.findByTokenAndTipo(resumen, TIPO).filter(this::vigente)
                .orElseThrow(() -> new IllegalArgumentException(TOKEN_INVALIDO));

        usuario.setClave(codificador.encode(clave.trim()));
        usuarioRepository.save(usuario);
        tokenEnUso.setUsadoEl(LocalDateTime.now(ZONA));
        tokenRepository.invalidarAnteriores(id, TIPO, tokenEnUso.getUsadoEl());
    }

    private void validarClave(String clave, String confirmarClave) {
        if (clave == null || clave.isBlank() || clave.trim().length() < 6) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres.");
        }
        if (clave.trim().getBytes(StandardCharsets.UTF_8).length > 72) {
            throw new IllegalArgumentException("La contraseña no debe superar los 72 bytes UTF-8.");
        }
        if (!clave.equals(confirmarClave)) {
            throw new IllegalArgumentException("Las contraseñas no coinciden. Por favor verifícalas.");
        }
    }

    private boolean vigente(TokenEntity token) {
        return token.getUsuario() != null && token.getUsadoEl() == null
                && token.getExpiraEl().isAfter(LocalDateTime.now(ZONA));
    }

    private boolean formatoValido(String token) {
        return token != null && token.matches("[A-Za-z0-9_-]{43}");
    }

    private String hash(String token) {
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256")
                    .digest(token.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 no está disponible.", e);
        }
    }
}
