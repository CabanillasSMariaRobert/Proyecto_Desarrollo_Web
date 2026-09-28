package com.ecommerce.tienda_kalza.servicios.publico;
import org.springframework.stereotype.Service;
import com.ecommerce.tienda_kalza.dto.publico.*;
import java.util.List;
import java.math.BigDecimal;
@Service
public class ProductoService {
    public ProductoDetalle obtenerDetalle(Long id) {
        return new ProductoDetalle(id, "Nike", "Urbana", new BigDecimal("350.00"), "Desc", true, "Stock", 
            List.of(new ImagenProducto("/img/productos/nike Air Force 1.webp", "Imagen del producto")), 
            List.of(new Talla("40", false, true)), 
            List.of());
    }
}
