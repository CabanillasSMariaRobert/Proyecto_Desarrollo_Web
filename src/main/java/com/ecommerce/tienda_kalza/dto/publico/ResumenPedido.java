package com.ecommerce.tienda_kalza.dto.publico;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Totales del pedido. Lo usan carrito y checkout, que muestran las mismas
 * cifras y por eso los comparten en vez de recalcularlas.
 *
 * envioTexto viaja como dato y no como logica porque cada pantalla redacta
 * distinto: el carrito aun no lo conoce ("Calculado en el pago") y el checkout
 * ya lo resolvio ("Gratis"). El texto es presentacion, no negocio.
 */
public record ResumenPedido(
        List<ItemCarrito> items,
        int totalItems,
        BigDecimal subtotal,
        BigDecimal envio,
        boolean envioGratis,
        String envioTexto,
        BigDecimal igv,
        BigDecimal total
) {

    private static final BigDecimal IGV = new BigDecimal("0.18");

    /**
     * Arma el resumen aplicando IGV sobre el subtotal.
     *
     * El envio entra ya resuelto: cada pantalla sabe si lo tiene gratis, asi
     * que la aritmetica vive aca una sola vez y el servicio solo decide el
     * envio. Sin esto, carrito y checkoutterminan mostrando totales distintos.
     */
    public static ResumenPedido calcular(List<ItemCarrito> items, BigDecimal envio,
                                         boolean envioGratis, String envioTexto) {
        BigDecimal subtotal = items.stream()
                .map(item -> item.precioUnitario().multiply(BigDecimal.valueOf(item.cantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int totalItems = items.stream().mapToInt(ItemCarrito::cantidad).sum();
        BigDecimal igv = subtotal.multiply(IGV).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = subtotal.add(igv).add(envio).setScale(2, RoundingMode.HALF_UP);

        return new ResumenPedido(items, totalItems, subtotal.setScale(2, RoundingMode.HALF_UP),
                envio, envioGratis, envioTexto, igv, total);
    }
}
