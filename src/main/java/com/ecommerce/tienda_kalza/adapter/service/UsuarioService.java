package com.ecommerce.tienda_kalza.adapter.service;

import com.ecommerce.tienda_kalza.adapter.entity.UsuarioEntity;
import com.ecommerce.tienda_kalza.adapter.enums.RolUsuario;
import com.ecommerce.tienda_kalza.adapter.enums.TipoDocumento;
import com.ecommerce.tienda_kalza.adapter.model.RegistroForm;
import com.ecommerce.tienda_kalza.adapter.model.Usuario;
import com.ecommerce.tienda_kalza.adapter.repository.CompraRepository;
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
    private final CompraRepository compraRepository;
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

        boolean coincide = false;
        try {
            coincide = passwordEncoder.matches(claveLimpia, entity.getClave());
        } catch (Exception e) {
            coincide = false;
        }

        if (!coincide && claveLimpia.equals(entity.getClave())) {
            coincide = true;
            entity.setClave(passwordEncoder.encode(claveLimpia));
            repository.save(entity);
        }

        if (!coincide) {
            throw new IllegalArgumentException("El correo o la contraseña son incorrectos.");
        }

        if (Boolean.TRUE.equals(entity.getEstaBaneado())) {
            String razon = (entity.getRazonDeBaneo() != null && !entity.getRazonDeBaneo().isBlank())
                    ? ": " + entity.getRazonDeBaneo()
                    : ".";
            throw new IllegalStateException("Tu cuenta ha sido bloqueada" + razon);
        }

        // RN05: Impide iniciar sesión si la cuenta está desactivada
        if (!Boolean.TRUE.equals(entity.getEstaActivo())) {
            throw new IllegalStateException("Tu cuenta se encuentra inactiva. Comunícate con soporte.");
        }

        return aModel(entity);
    }

    // REGISTRO DESDE LA VISTA PÚBLICA
    @Transactional
    public void registrar(RegistroForm form) {
        if (form == null) {
            throw new IllegalArgumentException("Los datos de registro son obligatorios.");
        }

        String nombreCompleto = limpiar(form.getNombreCompleto());
        if (nombreCompleto == null || nombreCompleto.isBlank()) {
            throw new IllegalArgumentException("Por favor, ingresa tu nombre completo.");
        }
        if (nombreCompleto.length() < 3 || nombreCompleto.length() > 150) {
            throw new IllegalArgumentException("El nombre completo debe tener entre 3 y 150 caracteres.");
        }

        String correo = limpiar(form.getCorreo());
        if (correo == null || correo.isBlank()) {
            throw new IllegalArgumentException("El correo electrónico es obligatorio.");
        }
        // Validar formato de correo mediante expresión regular
        if (!correo.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) {
            throw new IllegalArgumentException("El correo electrónico no tiene un formato válido.");
        }
        if (correo.length() > 255) {
            throw new IllegalArgumentException("El correo electrónico no puede tener más de 255 caracteres.");
        }

        if (repository.existsByCorreoIgnoreCase(correo)) {
            throw new IllegalStateException("Ya existe una cuenta registrada con el correo \"" + correo + "\".");
        }

        String clave = form.getClave();
        String confirmarClave = form.getConfirmarClave();

        if (clave == null || clave.isBlank()) {
            throw new IllegalArgumentException("La contraseña es obligatoria.");
        }

        if (clave.trim().length() < 6 || clave.trim().length() > 50) {
            throw new IllegalArgumentException("La contraseña debe tener entre 6 y 50 caracteres.");
        }

        if (!clave.equals(confirmarClave)) {
            throw new IllegalArgumentException("Las contraseñas no coinciden. Por favor verifícalas.");
        }

        String nombres = nombreCompleto;
        String apellidos = "-";
        if (nombreCompleto.contains(" ")) {
            int pos = nombreCompleto.lastIndexOf(' ');
            nombres = nombreCompleto.substring(0, pos).trim();
            apellidos = nombreCompleto.substring(pos + 1).trim();
        }

        UsuarioEntity entity = UsuarioEntity.builder()
                .nombres(nombres)
                .apellidos(apellidos)
                .correo(correo)
                .clave(passwordEncoder.encode(clave.trim()))
                .rol(RolUsuario.COMUN)
                .tipoDeDocumento(TipoDocumento.DNI)
                .estaActivo(true)
                .estaBaneado(false)
                .estaVerificado(false)
                .build();

        repository.save(entity);
    }

    // CREATE (ADMIN)
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

    // RN05: ELIMINACIÓN FÍSICA CON RECHAZO SI TIENE COMPRAS
    @Transactional
    public void eliminar(Integer id) {
        UsuarioEntity entity = buscar(id);

        // Validación de RN05: Si el usuario tiene compras registradas, se rechaza la eliminación física
        if (compraRepository.existsByCliente_IdUsuario(id)) {
            throw new ReglaNegocioException(
                    "No se puede eliminar físicamente al usuario \"" + entity.getNombres() + " " + entity.getApellidos() +
                            "\": tiene compras registradas. Debe desactivarse su cuenta para conservar su historial comercial (RN05)."
            );
        }

        // Si no tiene compras, se permite la eliminación física
        repository.delete(entity);
    }

    // RN05: DESACTIVACIÓN CON VALIDACIÓN DE HISTORIAL DE COMPRAS
    @Transactional
    public String cambiarEstado(Integer id) {
        UsuarioEntity entity = buscar(id);
        boolean nuevoEstado = !Boolean.TRUE.equals(entity.getEstaActivo());
        entity.setEstaActivo(nuevoEstado);
        repository.save(entity);
        // Si se acaba de desactivar (estaActivo = false)
        if (!nuevoEstado) {
            // Verificamos si tiene compras registradas (RN05)
            boolean tieneCompras = compraRepository.existsByCliente_IdUsuario(id);
            if (tieneCompras) {
                return "Usuario desactivado. Su cuenta queda inactiva pero su historial de compras se conserva intacto conforme a la RN05.";
            }
            return "Usuario desactivado correctamente.";
        }
        return "Usuario reactivado correctamente.";
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