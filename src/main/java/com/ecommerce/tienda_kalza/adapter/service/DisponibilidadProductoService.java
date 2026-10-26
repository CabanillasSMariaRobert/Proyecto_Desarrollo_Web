package com.ecommerce.tienda_kalza.adapter.service;

import com.ecommerce.tienda_kalza.adapter.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collection;
import java.util.HashSet;

@Service @RequiredArgsConstructor
public class DisponibilidadProductoService {
    private final ProductoRepository productos;

    @Transactional(readOnly = true)
    public void validarVariantes(Collection<Integer> idsVariantes) {
        if (idsVariantes == null || idsVariantes.stream().anyMatch(java.util.Objects::isNull)) {
            throw new ReglaNegocioException("No se pudo identificar la variante del producto.");
        }
        if (idsVariantes.isEmpty()) return;
        var pendientes = new HashSet<>(idsVariantes);
        var estados = productos.consultarDisponibilidad(pendientes);
        for (var estado : estados) {
            if (!estado.getEstaActivo()) {
                throw new ReglaNegocioException("El producto \"" + estado.getNombreProducto()
                        + "\" no está disponible para nuevas compras.");
            }
            pendientes.remove(estado.getIdVariante());
        }
        if (!pendientes.isEmpty()) {
            throw new ReglaNegocioException("Una de las variantes seleccionadas ya no está disponible.");
        }
    }
}
