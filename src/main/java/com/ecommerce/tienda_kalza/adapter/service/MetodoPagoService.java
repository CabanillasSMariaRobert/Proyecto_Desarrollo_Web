package com.ecommerce.tienda_kalza.adapter.service;

import com.ecommerce.tienda_kalza.adapter.entity.MetodoPagoEntity;
import com.ecommerce.tienda_kalza.adapter.entity.UsuarioEntity;
import com.ecommerce.tienda_kalza.adapter.enums.RedUsada;
import com.ecommerce.tienda_kalza.adapter.model.MetodoPago;
import com.ecommerce.tienda_kalza.adapter.model.Usuario;
import com.ecommerce.tienda_kalza.adapter.repository.MetodoPagoRepository;
import com.ecommerce.tienda_kalza.adapter.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MetodoPagoService {

    private final MetodoPagoRepository repository;
    private final UsuarioRepository usuarioRepository;

    // READ (Listar tarjetas del usuario)
    @Transactional(readOnly = true)
    public List<MetodoPago> listarPorUsuario(Integer idUsuario) {
        return repository.findByUsuario_IdUsuario(idUsuario)
                .stream()
                .map(this::aModel)
                .toList();
    }

    // READ (Buscar por ID)
    @Transactional(readOnly = true)
    public MetodoPago obtener(Integer idMetodoDePago) {
        return aModel(buscar(idMetodoDePago));
    }

    // CREATE (Registrar nueva tarjeta)
    @Transactional
    public MetodoPago guardar(MetodoPago model, Integer idUsuario) {
        UsuarioEntity usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("El usuario con ID " + idUsuario + " no existe."));

        // Extraer los últimos 4 dígitos
        String ultimos4 = model.getUltimos4Digitos();
        if (model.getNumeroTarjeta() != null && !model.getNumeroTarjeta().isBlank()) {
            String soloNumeros = model.getNumeroTarjeta().replaceAll("\\D", "");
            if (soloNumeros.length() >= 4) {
                ultimos4 = soloNumeros.substring(soloNumeros.length() - 4);
            } else {
                throw new IllegalArgumentException("El número de tarjeta debe tener al menos 4 dígitos.");
            }
        }

        if (ultimos4 == null || ultimos4.length() != 4) {
            throw new IllegalArgumentException("Los últimos 4 dígitos de la tarjeta son obligatorios.");
        }

        // Procesar fecha
        if (model.getFechaVencimiento() != null && !model.getFechaVencimiento().isBlank()) {
            String[] partes = model.getFechaVencimiento().trim().split("[/-]");
            if (partes.length == 2) {
                try {
                    int mes = Integer.parseInt(partes[0].trim());
                    int anio = Integer.parseInt(partes[1].trim());
                    // Si el usuario ingresó solo 2 dígitos de año (ej: 28), se convierte a 2028
                    if (anio < 100) {
                        anio += 2000;
                    }
                    model.setExpiraElMes(mes);
                    model.setExpiraElAnio(anio);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("El formato de fecha debe ser MM/AA (ej: 12/28).");
                }
            } else {
                throw new IllegalArgumentException("El formato de fecha de vencimiento debe ser MM/AA.");
            }
        }

        // Validar mes y año de expiración
        if (model.getExpiraElMes() == null || model.getExpiraElMes() < 1 || model.getExpiraElMes() > 12) {
            throw new IllegalArgumentException("El mes de expiración debe estar entre 1 y 12.");
        }
        int anioActual = LocalDate.now().getYear();
        if (model.getExpiraElAnio() == null || model.getExpiraElAnio() < anioActual) {
            throw new IllegalArgumentException("El año de expiración no puede estar en el pasado.");
        }

        // Validar CVV
        if (model.getCvv() != null && !model.getCvv().isBlank()) {
            String cvvLimpio = model.getCvv().trim();
            if (!cvvLimpio.matches("\\d{3,4}")) {
                throw new IllegalArgumentException("El código CVV debe tener 3 o 4 dígitos numéricos.");
            }
        }

        RedUsada red = model.getRedUsada() != null ? model.getRedUsada() : RedUsada.VISA;

        if (repository.existsByUsuario_IdUsuarioAndUltimos4DigitosAndRedUsada(idUsuario, ultimos4, red)) {
            throw new IllegalStateException("Ya tienes registrada una tarjeta " + red + " que termina en " + ultimos4 + ".");
        }

        String pasarela = (model.getPasarela() != null && !model.getPasarela().isBlank())
                ? model.getPasarela().trim()
                : "Pasarela Web";

        // Token único simulado de pasarela
        String token = (model.getToken() != null && !model.getToken().isBlank())
                ? model.getToken()
                : "tok_" + UUID.randomUUID().toString().replace("-", "").substring(0, 20);

        // Se persiste la tarjeta (sin almacenar el CVV por seguridad)
        MetodoPagoEntity entity = MetodoPagoEntity.builder()
                .usuario(usuario)
                .pasarela(pasarela)
                .redUsada(red)
                .token(token)
                .ultimos4Digitos(ultimos4)
                .expira_el_mes(model.getExpiraElMes())
                .expira_el_anio(model.getExpiraElAnio())
                .build();

        return aModel(repository.save(entity));
    }

    // DELETE (Eliminar tarjeta)
    @Transactional
    public void eliminar(Integer idMetodoDePago, Integer idUsuario) {
        MetodoPagoEntity entity = repository.findByIdMetodoDePagoAndUsuario_IdUsuario(idMetodoDePago, idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el método de pago para este usuario."));
        repository.delete(entity);
    }

    private MetodoPagoEntity buscar(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El método de pago #" + id + " no existe."));
    }

    private MetodoPago aModel(MetodoPagoEntity entity) {
        Usuario usuarioModel = null;
        if (entity.getUsuario() != null) {
            usuarioModel = Usuario.builder()
                    .idUsuario(entity.getUsuario().getIdUsuario())
                    .nombres(entity.getUsuario().getNombres())
                    .apellidos(entity.getUsuario().getApellidos())
                    .correo(entity.getUsuario().getCorreo())
                    .build();
        }

        return MetodoPago.builder()
                .idMetodoDePago(entity.getIdMetodoDePago())
                .usuario(usuarioModel)
                .pasarela(entity.getPasarela())
                .redUsada(entity.getRedUsada())
                .token(entity.getToken())
                .ultimos4Digitos(entity.getUltimos4Digitos())
                .expiraElMes(entity.getExpira_el_mes())
                .expiraElAnio(entity.getExpira_el_anio())
                .creadoEl(entity.getCreado_el())
                .build();
    }
}