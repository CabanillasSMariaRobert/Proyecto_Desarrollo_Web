package com.ecommerce.tienda_kalza.adapter.service;

import com.ecommerce.tienda_kalza.adapter.entity.UsuarioEntity;
import com.ecommerce.tienda_kalza.adapter.enums.RolUsuario;
import com.ecommerce.tienda_kalza.adapter.enums.TipoDocumento;
import com.ecommerce.tienda_kalza.adapter.model.Usuario;
import com.ecommerce.tienda_kalza.adapter.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    // AUTENTICACIÓN / LOGIN
    @Transactional
    public Usuario autenticar(String correo, String clave) {
        String correoLimpio = limpiar(correo);
        String claveLimpia = (clave != null) ? clave.trim() : "";

        if (correoLimpio == null || claveLimpia.isEmpty()) {
            throw new IllegalArgumentException("Debe ingresar su correo electrónico y contraseña.");
        }

        UsuarioEntity entity = repository.findByCorreoIgnoreCase(correoLimpio)
                .orElseThrow(() -> new IllegalArgumentException("El correo o la contraseña son incorrectos."));

        // Verificación con BCrypt
        boolean coincide = false;
        try {
            coincide = passwordEncoder.matches(claveLimpia, entity.getClave());
        } catch (Exception e) {
            coincide = false;
        }

        // Respaldo por si quedó algún registro con contraseña sin hashear en la BD de pruebas
        if (!coincide && claveLimpia.equals(entity.getClave())) {
            coincide = true;
            entity.setClave(passwordEncoder.encode(claveLimpia));
            repository.save(entity);
        }

        if (!coincide) {
            throw new IllegalArgumentException("El correo o la contraseña son incorrectos.");
        }

        // Validación de usuario bloqueado
        if (Boolean.TRUE.equals(entity.getEstaBaneado())) {
            String razon = (entity.getRazonDeBaneo() != null && !entity.getRazonDeBaneo().isBlank())
                    ? ": " + entity.getRazonDeBaneo()
                    : ".";
            throw new IllegalStateException("Tu cuenta ha sido bloqueada" + razon);
        }

        // Validación de usuario inactivo
        if (!Boolean.TRUE.equals(entity.getEstaActivo())) {
            throw new IllegalStateException("Tu cuenta se encuentra inactiva. Comunícate con soporte.");
        }

        return aModel(entity);
    }

    // CREATE
    @Transactional
    public void crear(Usuario model) {
        String correo = limpiar(model.getCorreo());
        if (correo == null || correo.isBlank()) {
            throw new IllegalArgumentException("El correo electrónico es obligatorio.");
        }

        if (repository.existsByCorreoIgnoreCase(correo)) {
            throw new IllegalStateException("Ya existe un usuario con el correo \"" + correo + "\".");
        }

        UsuarioEntity entity = new UsuarioEntity();

        // Manejo de nombre y apellido obligatorio
        String nombres = limpiar(model.getNombres());
        String apellidos = limpiar(model.getApellidos());

        if ((apellidos == null || apellidos.isBlank()) && nombres != null && nombres.contains(" ")) {
            int pos = nombres.lastIndexOf(' ');
            apellidos = nombres.substring(pos + 1).trim();
            nombres = nombres.substring(0, pos).trim();
        }

        entity.setNombres(nombres != null ? nombres : "Sin nombre");
        entity.setApellidos(apellidos != null && !apellidos.isBlank() ? apellidos : "-");
        entity.setCorreo(correo);
        entity.setTelefono(limpiar(model.getTelefono()));
        entity.setNumeroDeDocumento(limpiar(model.getNumeroDeDocumento()));
        entity.setRol(model.getRol() != null ? model.getRol() : RolUsuario.COMUN);
        entity.setTipoDeDocumento(model.getTipo_de_documento() != null ? model.getTipo_de_documento() : TipoDocumento.DNI);

        String clave = (model.getClave() != null && !model.getClave().isBlank())
                ? model.getClave().trim()
                : "12345678";
        entity.setClave(passwordEncoder.encode(clave));

        entity.setEstaActivo(true);
        entity.setEstaBaneado(false);
        entity.setEstaVerificado(false);

        repository.save(entity);
    }

    // READ (uno)
    @Transactional(readOnly = true)
    public Usuario obtener(Integer id) {
        return aModel(buscar(id));
    }

    // READ (listar con búsqueda y paginación)
    @Transactional(readOnly = true)
    public Page<Usuario> listar(String buscar, Integer pagina, Integer tamano) {
        Pageable pag = PageRequest.of(Math.max(pagina != null ? pagina : 0, 0), tamano, Sort.by(Sort.Direction.DESC, "creadoEl"));
        Page<UsuarioEntity> pageEntities;

        if (buscar != null && !buscar.trim().isEmpty()) {
            pageEntities = repository.buscarPorFiltro(buscar.trim(), pag);
        } else {
            pageEntities = repository.findAll(pag);
        }

        return pageEntities.map(this::aModel);
    }

    // UPDATE
    @Transactional
    public void actualizar(Integer id, Usuario model) {
        UsuarioEntity entity = buscar(id);

        String correo = limpiar(model.getCorreo());
        if (correo != null && repository.existsByCorreoIgnoreCaseAndIdUsuarioNot(correo, id)) {
            throw new IllegalStateException("El correo \"" + correo + "\" ya está en uso por otro usuario.");
        }

        String nombres = limpiar(model.getNombres());
        String apellidos = limpiar(model.getApellidos());
        if ((apellidos == null || apellidos.isBlank()) && nombres != null && nombres.contains(" ")) {
            int pos = nombres.lastIndexOf(' ');
            apellidos = nombres.substring(pos + 1).trim();
            nombres = nombres.substring(0, pos).trim();
        }

        if (nombres != null) entity.setNombres(nombres);
        if (apellidos != null && !apellidos.isBlank()) entity.setApellidos(apellidos);
        if (correo != null) entity.setCorreo(correo);
        if (model.getTelefono() != null) entity.setTelefono(limpiar(model.getTelefono()));
        if (model.getRol() != null) entity.setRol(model.getRol());

        if (model.getClave() != null && !model.getClave().isBlank()) {
            entity.setClave(passwordEncoder.encode(model.getClave().trim()));
        }

        repository.save(entity);
    }

    // TOGGLE ESTADO (BLOQUEAR / DESBLOQUEAR)
    @Transactional
    public boolean cambiarEstado(Integer id) {
        UsuarioEntity entity = buscar(id);
        boolean estaBaneadoActual = Boolean.TRUE.equals(entity.getEstaBaneado());

        entity.setEstaBaneado(!estaBaneadoActual);
        repository.save(entity);

        return !Boolean.TRUE.equals(entity.getEstaBaneado());
    }

    private UsuarioEntity buscar(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El usuario con ID " + id + " no existe."));
    }

    private String limpiar(String texto) {
        return (texto == null || texto.isBlank()) ? null : texto.trim();
    }

    private Usuario aModel(UsuarioEntity entity) {
        return Usuario.builder()
                .idUsuario(entity.getIdUsuario())
                .nombres(entity.getNombres())
                .apellidos(entity.getApellidos())
                .correo(entity.getCorreo())
                .rol(entity.getRol())
                .telefono(entity.getTelefono())
                .tipo_de_documento(entity.getTipoDeDocumento())
                .numeroDeDocumento(entity.getNumeroDeDocumento())
                .estaActivo(Boolean.TRUE.equals(entity.getEstaActivo()))
                .estaVerificado(entity.getEstaVerificado())
                .estaBaneado(entity.getEstaBaneado())
                .razonDeBaneo(entity.getRazonDeBaneo())
                .creadoEl(entity.getCreadoEl())
                .build();
    }
}