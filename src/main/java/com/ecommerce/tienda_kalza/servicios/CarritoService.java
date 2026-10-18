package com.ecommerce.tienda_kalza.servicios;

import com.ecommerce.tienda_kalza.dtos.publico.ItemCarrito;
import com.ecommerce.tienda_kalza.dtos.publico.ResumenPedido;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Carrito de prueba.
 *
 * IMPORTANTE: los precios de sus lineas no salen del CatalogoService a
 * proposito. Un carrito guarda el precio del momento en que se agrego el
 * producto, no el vigente: si el catalogo baja de precio despues, el carrito
 * debe conservar lo que el cliente agreed pagar. Por eso aqui viven cifras
 * propias y no es una inconsistencia.
 */
@Service
public class CarritoService {

    private final List<ItemCarrito> items = List.of(
            new ItemCarrito(1L, "AeroGlide Pro Runner", "AeroGlide Pro Runner.webp",
                    "10.5", "Negro Obsidiana", new BigDecimal("145.00"), 1),
            new ItemCarrito(2L, "Court Classic Low", "Court Classic Low.webp",
                    "9", "Blanco Nube", new BigDecimal("95.00"), 2)
    );

    public List<ItemCarrito> obtenerItems() {
        return items;
    }

    /** El envio todavia no se conoce: lo calcula el checkout. */
    public ResumenPedido obtenerResumen() {
        return ResumenPedido.calcular(items, BigDecimal.ZERO, false, "Calculado en el pago");
    }
}
