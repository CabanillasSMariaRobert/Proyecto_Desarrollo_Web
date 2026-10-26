package com.ecommerce.tienda_kalza.adapter.service;

import com.ecommerce.tienda_kalza.adapter.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/** Datos de consulta para las vistas; las mutaciones permanecen en sus servicios. */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdministracionCalzadoService {
    private final ZapatoRepository zapatos;
    private final ProductoRepository productos;
    private final MaterialRepository materiales;
    private final ZapatoMaterialRepository asociaciones;

    public record Opcion(int id, String nombre) {}
    public record Fila(int idZapato, String nombre, boolean activo) {}
    public record Composicion(int idMaterial, String nombre, boolean activo) {}

    public Page<Fila> listar(int pagina) {
        return zapatos.findAll(PageRequest.of(Math.max(0, pagina), 10, Sort.by("idZapato").descending()))
                .map(z -> new Fila(z.getIdZapato(), z.getProducto().getNombre(), z.isEstaActivo()));
    }

    public List<Opcion> productosDisponibles() {
        return productos.findAll(Sort.by("nombre")).stream()
                .filter(p -> p.isEstaActivo() && !zapatos.existsByProducto_IdProducto(p.getIdProducto()))
                .map(p -> new Opcion(p.getIdProducto(), p.getNombre())).toList();
    }

    public List<Opcion> materialesDisponibles(int idZapato) {
        var asignados = asociaciones.findByZapato_IdZapato(idZapato).stream()
                .map(a -> a.getMaterial().getIdMaterial()).collect(java.util.stream.Collectors.toSet());
        return materiales.findAll(Sort.by("nombre")).stream()
                .filter(m -> m.isEstaActivo() && !asignados.contains(m.getIdMaterial()))
                .map(m -> new Opcion(m.getIdMaterial(), m.getNombre())).toList();
    }

    public List<Composicion> composicion(int idZapato) {
        return asociaciones.findByZapato_IdZapato(idZapato).stream()
                .map(a -> new Composicion(a.getMaterial().getIdMaterial(), a.getMaterial().getNombre(),
                        a.getMaterial().isEstaActivo())).toList();
    }

    public String nombre(int idZapato) {
        return zapatos.findById(idZapato).orElseThrow(() -> new IllegalArgumentException("El calzado no existe."))
                .getProducto().getNombre();
    }
}
