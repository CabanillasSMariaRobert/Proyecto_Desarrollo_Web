package com.ecommerce.tienda_kalza.servicios;

import com.ecommerce.tienda_kalza.dto.publico.PedidoCliente;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Pedidos del cliente.
 *
 * claseEstado lleva la clase del badge porque cada estado se pinza distinto:
 * entregado en gris claro, enviado en secundario, confirmado con borde. Es
 * presentacion, y por eso viaja en el dato en vez de deducirse en la vista.
 */
@Service
public class PedidoClienteService {

    private final List<PedidoCliente> pedidos = List.of(
            new PedidoCliente("#ORD-99382", "24/10/2024", "Velocity Pro X.webp", "Velocity Pro X",
                    "Velocity Pro X + 1 más", "Entregado", "bg-light text-dark",
                    new BigDecimal("640.00"), "/pedidos", "Detalles"),
            new PedidoCliente("#ORD-99451", "02/11/2024", "Aero Float.jpg", "Aero Float",
                    "Aero Float", "Enviado", "text-bg-secondary",
                    new BigDecimal("89.00"), "/pedidos", "Detalles"),
            new PedidoCliente("#ORD-99502", "15/11/2024", "Terra Grip Pro.jpg", "Terra Grip Pro",
                    "Terra Grip Pro", "Confirmado", "border border-dark text-dark",
                    new BigDecimal("520.00"), "/pedidos", "Detalles")
    );

    private final List<PedidoCliente> recientes = List.of(
            new PedidoCliente("#KF-9921", "", "AeroGlide Pro Runner.webp", "AeroGlide Pro Runner",
                    "", "Entregado", "bg-light text-dark",
                    new BigDecimal("640.00"), "/pedidos", "Ver detalles"),
            new PedidoCliente("#KF-9804", "", "Quantum Court.jpg", "Quantum Court",
                    "", "Enviado", "bg-light text-dark",
                    new BigDecimal("740.00"), "/pedidos", "Rastrear pedido")
    );

    public List<PedidoCliente> obtenerPedidos() {
        return pedidos;
    }

    public List<PedidoCliente> obtenerRecientes() {
        return recientes;
    }
}
