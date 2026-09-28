package com.ecommerce.tienda_kalza.servicios.publico;
import org.springframework.stereotype.Service;
import com.ecommerce.tienda_kalza.dto.publico.*;
import java.util.List;
import java.math.BigDecimal;
@Service
public class PedidoService {
    public List<PedidoCliente> obtenerPedidos() {
        return List.of(new PedidoCliente("ORD-001", "2024-01-01", "/img/productos/nike Air Force 1.webp", "Nike", "1 item", "Entregado", "bg-success", new BigDecimal("350.00"), "/pedidos/1", "Ver"));
    }
}
