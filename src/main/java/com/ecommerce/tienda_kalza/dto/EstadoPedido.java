package com.ecommerce.tienda_kalza.dto;

/**
 * Estados posibles de un pedido en el panel de administracion.
 *
 * claseCss() centraliza el color del badge para que las dos vistas admin
 * (metricas y pedidos) muestren el mismo estado siempre igual.
 */
public enum EstadoPedido {

    PENDIENTE("text-bg-warning text-dark"),
    PAGADO("text-bg-info text-dark"),
    PROCESANDO("text-bg-secondary"),
    ENVIADO("text-bg-primary"),
    ENTREGADO("text-bg-success"),
    CANCELADO("text-bg-danger");

    private final String claseCss;

    EstadoPedido(String claseCss) {
        this.claseCss = claseCss;
    }

    public String claseCss() {
        return claseCss;
    }
}
