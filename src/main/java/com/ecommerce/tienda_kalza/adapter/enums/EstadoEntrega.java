package com.ecommerce.tienda_kalza.adapter.enums;

public enum EstadoEntrega {
    PENDIENTE, // 0
    ASIGNADO, // 1
    RECOGIDO, // 2
    EN_CAMINO, // 3
    ENTREGADO, // 4
    FALLADO, // 5
    CANCELADO; // 6

    public boolean permiteTransicionA(EstadoEntrega siguiente) {
        if (siguiente == null) {
            return false;
        }
        return switch (this) {
            case PENDIENTE -> siguiente == ASIGNADO || siguiente == CANCELADO;
            case ASIGNADO -> siguiente == RECOGIDO || siguiente == CANCELADO;
            case RECOGIDO -> siguiente == EN_CAMINO || siguiente == CANCELADO;
            case EN_CAMINO -> siguiente == ENTREGADO || siguiente == FALLADO;
            case ENTREGADO, FALLADO, CANCELADO -> false;
        };
    }
}
