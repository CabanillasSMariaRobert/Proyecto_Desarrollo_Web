package com.ecommerce.tienda_kalza.servicios;

import com.ecommerce.tienda_kalza.dto.publico.ResumenPedido;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Datos del checkout.
 *
 * Reutiliza el carrito y su aritmetica; lo unico que cambia es que aqui el
 * envio ya se resolvio. Delegar en ResumenPedido.calcular evita que las dos
 * pantallas-Calculen el IGV por su cuenta y terminen con totales distintos.
 */
@Service
public class CheckoutService {

    private final CarritoService carrito;

    public CheckoutService(CarritoService carrito) {
        this.carrito = carrito;
    }

    public ResumenPedido obtenerResumen() {
        return ResumenPedido.calcular(carrito.obtenerItems(), BigDecimal.ZERO, true, "Gratis");
    }
}
