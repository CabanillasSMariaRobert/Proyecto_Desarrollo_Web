package com.ecommerce.tienda_kalza.servicios.publico;
import org.springframework.stereotype.Service;
import com.ecommerce.tienda_kalza.dto.publico.*;
import java.util.List;
import java.math.BigDecimal;
@Service
public class CarritoService {
    public ResumenPedido obtenerResumen() {
        return ResumenPedido.calcular(List.of(new ItemCarrito(1L, "Nike", "/img/productos/nike Air Force 1.webp", "40", "Negro", new BigDecimal("350.00"), 1)), new BigDecimal("0"), true, "Gratis");
    }
}
