package com.ecommerce.tienda_kalza.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Pedido tal como lo muestra el panel de administracion.
 *
 * id es el identificador real, sin formato. Que en pantalla aparezca como
 * "#ORD-" + id es decision de la vista, no del dato.
 */
@Getter
@Setter
@NoArgsConstructor
public class PedidoAdmin {

    private Long id;
    private String cliente;
    private LocalDate fecha;
    private BigDecimal total;
    private EstadoPedido estado;
    private List<ItemPedido> items = new ArrayList<>();

    public PedidoAdmin(Long id, String cliente, LocalDate fecha, BigDecimal total, EstadoPedido estado) {
        this.id = id;
        this.cliente = cliente;
        this.fecha = fecha;
        this.total = total;
        this.estado = estado;
    }

    public int getCantidadItems() {
        return items.stream().mapToInt(ItemPedido::getCantidad).sum();
    }

    /**
     * Vista previa de los items para el modal de detalle, que se arma por
     * JavaScript del lado del cliente. Separador " | ", que se parte con split().
     */
    public String resumenItems() {
        return items.stream()
                .map(item -> item.getNombre() + " x " + item.getCantidad()
                        + " (S/ " + String.format("%.2f", item.getSubtotal()) + ")")
                .reduce((a, b) -> a + " | " + b)
                .orElse("Sin items");
    }
}
