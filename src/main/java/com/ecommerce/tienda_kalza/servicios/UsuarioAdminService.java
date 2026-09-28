package com.ecommerce.tienda_kalza.servicios;

import com.ecommerce.tienda_kalza.dto.EstadoUsuario;
import com.ecommerce.tienda_kalza.dto.UsuarioAdmin;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Datos de prueba para el panel de usuarios.
 *
 * Las primeras cinco filas son las del diseno; el resto se genera para que la
 * paginacion tenga contenido real. Para conectar la base de datos hay que
 * reemplazar obtenerTodos() por usuarioRepository.findAll() y dejar la firma
 * igual.
 */
@Service
public class UsuarioAdminService {

    private static final int CANTIDAD_USUARIOS = 1284;

    private static final String[] NOMBRES = {
            "John Doe", "María García", "Carlos Pérez", "Ana Torres", "Luis Ramírez"
    };

    private static final String[] CORREOS = {
            "johndoe@email.com", "maria.garcia@email.com", "carlos.perez@email.com",
            "ana.torres@email.com", "luis.ramirez@email.com"
    };

    private static final LocalDate[] FECHAS = {
            LocalDate.of(2026, 1, 2),
            LocalDate.of(2026, 1, 15),
            LocalDate.of(2026, 1, 28),
            LocalDate.of(2026, 2, 3),
            LocalDate.of(2026, 2, 22)
    };

    private static final Integer[] PEDIDOS = {12, 8, 5, 2, 0};

    private static final EstadoUsuario[] ESTADOS = {
            EstadoUsuario.ACTIVO, EstadoUsuario.ACTIVO, EstadoUsuario.ACTIVO,
            EstadoUsuario.ACTIVO, EstadoUsuario.BLOQUEADO
    };

    /** Cada color trae su propio color de texto: el avatar no define ninguno. */
    private static final String[] COLORES = {
            "bg-primary text-white", "bg-success text-white", "bg-warning text-dark",
            "bg-danger text-white", "bg-secondary text-white"
    };

    private static final String[] NOMBRES_GENERADOS = {
            "Sarah Jenkins", "Michael Chang", "Emma Watson", "Diego Fernandez",
            "Lucia Vargas", "Pedro Sanchez", "Carmen Rojas", "Jorge Castillo",
            "Rosa Mendoza", "Andres Silva", "Paola Ramirez", "Diego Alonso"
    };

    private final List<UsuarioAdmin> usuarios;

    public UsuarioAdminService() {
        this.usuarios = generarUsuarios();
    }

    public List<UsuarioAdmin> obtenerTodos() {
        return List.copyOf(usuarios);
    }

    public List<UsuarioAdmin> buscar(String texto) {
        if (texto == null || texto.isBlank()) {
            return obtenerTodos();
        }
        String filtro = texto.trim().toLowerCase();
        return usuarios.stream()
                .filter(u -> u.getNombre().toLowerCase().contains(filtro)
                        || u.getCorreo().toLowerCase().contains(filtro))
                .toList();
    }

    private List<UsuarioAdmin> generarUsuarios() {
        // Semilla fija: los datos tienen que ser los mismos en cada recarga.
        Random aleatorio = new Random(20260220L);
        List<UsuarioAdmin> lista = new ArrayList<>(CANTIDAD_USUARIOS);

        for (int i = 0; i < CANTIDAD_USUARIOS; i++) {
            UsuarioAdmin usuario = new UsuarioAdmin();
            usuario.setId(1L + i);

            if (i < NOMBRES.length) {
                usuario.setNombre(NOMBRES[i]);
                usuario.setCorreo(CORREOS[i]);
                usuario.setFechaRegistro(FECHAS[i]);
                usuario.setPedidos(PEDIDOS[i]);
                usuario.setEstado(ESTADOS[i]);
                usuario.setColorClase(COLORES[i]);
            } else {
                String nombre = NOMBRES_GENERADOS[aleatorio.nextInt(NOMBRES_GENERADOS.length)];
                usuario.setNombre(nombre);
                usuario.setCorreo(usuario.getIniciales().toLowerCase() + "@email.com");
                usuario.setFechaRegistro(LocalDate.of(2026, 1, 1).plusDays(aleatorio.nextInt(250)));
                usuario.setPedidos(aleatorio.nextInt(30));
                // Mayoria activos, algunos bloqueados, para que la vista tenga de todo.
                usuario.setEstado(aleatorio.nextInt(10) == 0
                        ? EstadoUsuario.BLOQUEADO
                        : EstadoUsuario.ACTIVO);
                usuario.setColorClase(COLORES[aleatorio.nextInt(COLORES.length)]);
            }
            lista.add(usuario);
        }
        return List.copyOf(lista);
    }
}
