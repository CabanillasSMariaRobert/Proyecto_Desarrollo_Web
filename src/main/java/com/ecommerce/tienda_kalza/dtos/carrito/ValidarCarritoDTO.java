package com.ecommerce.tienda_kalza.dtos.carrito;

import java.util.List;

public class ValidarCarritoDTO {
    private Boolean valido;
    private List<String> errores;
    private List<CarritoItemDTO> itemsConProblema;
}