package com.ecommerce.tienda_kalza.dtos.catalogo;

import java.util.List;

public class PaginatedResponseDTO<T> {
    private List<T> content;
    private Integer page;
    private Integer size;
    private Long totalElements;
    private Integer totalPages;
    private Boolean first;
    private Boolean last;
}