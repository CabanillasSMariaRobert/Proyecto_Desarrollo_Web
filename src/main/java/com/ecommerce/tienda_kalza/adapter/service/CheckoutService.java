package com.ecommerce.tienda_kalza.adapter.service;

import com.ecommerce.tienda_kalza.adapter.entity.*;
import com.ecommerce.tienda_kalza.adapter.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CheckoutService {
    private final UsuarioRepository usuarios;
    private final VarianteZapatoRepository variantes;
    private final MetodoPagoRepository metodos;
    private final PuntoEntregaRepository puntos;
    private final CompraService compras;
    private final BoletaRepository boletas;
    private final EntregasRepository entregas;

    @Value("${tienda.ruc:00000000000}")
    private String rucTienda;

    @Transactional
    public int confirmar(int idUsuario, List<CarritoService.Linea> items,
                         int idMetodoPago, int idPuntoEntrega, boolean pagoAprobado) {
        // 1. Validar cliente, carrito, tarjeta propia y punto de entrega.
        var cliente = usuarios.findById(idUsuario)
                .orElseThrow(() -> new ReglaNegocioException("El cliente no existe."));
        if (!Boolean.TRUE.equals(cliente.getEstaActivo()) || Boolean.TRUE.equals(cliente.getEstaBaneado()))
            throw new ReglaNegocioException("La cuenta no está habilitada para comprar.");
        if (items == null || items.isEmpty()) throw new ReglaNegocioException("El carrito está vacío.");
        var metodo = metodos.findByIdMetodoDePagoAndUsuario_IdUsuario(idMetodoPago, idUsuario)
                .orElseThrow(() -> new ReglaNegocioException("Selecciona un método de pago de tu cuenta."));
        if (YearMonth.of(metodo.getExpira_el_anio(), metodo.getExpira_el_mes()).isBefore(YearMonth.now()))
            throw new ReglaNegocioException("El método de pago está vencido.");
        var punto = puntos.findById(idPuntoEntrega)
                .orElseThrow(() -> new ReglaNegocioException("El punto de entrega no existe."));
        if (!punto.isEstaActivo()) throw new ReglaNegocioException("El punto de entrega está inactivo.");
        if (!pagoAprobado) throw new ReglaNegocioException("Pago simulado rechazado. Tu carrito se conserva.");

        // 2. Crear los detalles y calcular los importes con los precios de la base de datos.
        var compra = CompraEntity.builder().cliente(cliente).estado("pagado")
                .totalSinDescuento(BigDecimal.ZERO).totalConDescuento(BigDecimal.ZERO).build();
        for (var item : items) {
            if (item.cantidad() < 1) throw new ReglaNegocioException("La cantidad debe ser al menos uno.");
            var variante = variantes.findById(item.idVariante())
                    .orElseThrow(() -> new ReglaNegocioException("La variante ya no existe."));
            var producto = variante.getZapato().getProducto();
            BigDecimal original = producto.getPrecioOriginal();
            BigDecimal precioFinal = original.multiply(BigDecimal.ONE.subtract(producto.getDescuento()))
                    .setScale(2, RoundingMode.HALF_UP);
            BigDecimal cantidad = BigDecimal.valueOf(item.cantidad());
            compra.getDetalles().add(DetalleCompraEntity.builder().compra(compra).varianteZapato(variante)
                    .nombreDelProducto(producto.getNombre()).cantidad(item.cantidad())
                    .precioOriginalXUnidad(original).descuentoXUnidad(original.subtract(precioFinal)).build());
            compra.setTotalSinDescuento(compra.getTotalSinDescuento().add(original.multiply(cantidad)));
            compra.setTotalConDescuento(compra.getTotalConDescuento().add(precioFinal.multiply(cantidad)));
        }

        // 3. Guardar la compra: CompraService valida disponibilidad y descuenta stock una sola vez.
        compras.guardar(compra);

        // 4. Emitir la boleta y crear la entrega pendiente, dentro de la misma transacción.
        var boleta = BoletaEntity.builder().compra(compra).metodoDePago(metodo)
                .cliente(cliente.getNombres() + " " + cliente.getApellidos()).rucDeLaTienda(rucTienda)
                .pasarelaDePago("Pago simulado KALZA").redDeTarjeta(metodo.getRedUsada())
                .ultimos4Digitos(metodo.getUltimos4Digitos()).total(compra.getTotalConDescuento()).build();
        boletas.save(boleta);
        var entrega = new EntregaEntity();
        entrega.setCompra(compra);
        entrega.setPuntoDeEntrega(punto);
        entregas.save(entrega);
        return compra.getIdCompra();
    }

    @Transactional(readOnly = true)
    public BoletaEntity obtenerBoleta(int idCompra, int idUsuario) {
        return boletas.findByCompra_IdCompraAndCompra_Cliente_IdUsuario(idCompra, idUsuario)
                .orElseThrow(() -> new ReglaNegocioException("La boleta no existe o no pertenece a tu cuenta."));
    }
}
