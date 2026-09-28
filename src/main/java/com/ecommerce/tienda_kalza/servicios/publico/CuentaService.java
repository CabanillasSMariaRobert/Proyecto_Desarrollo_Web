package com.ecommerce.tienda_kalza.servicios.publico;
import org.springframework.stereotype.Service;
import com.ecommerce.tienda_kalza.dto.publico.*;
import java.util.List;
import java.math.BigDecimal;
@Service
public class CuentaService {
    public UsuarioPerfil obtenerPerfil() {
        return new UsuarioPerfil("Juan", "Perez", "juan@test.com", "999888777");
    }
    public ResumenUsuario obtenerResumen() {
        return new ResumenUsuario("bi-person", "Perfil", "Detalles", "/perfil", "Editar");
    }
    public List<PedidoCliente> obtenerPedidosRecientes() {
        return List.of(new PedidoCliente("ORD-001", "2024-01-01", "/img/p1.jpg", "Nike", "1 item", "Entregado", "bg-success", new BigDecimal("350.00"), "/pedidos/1", "Ver"));
    }
}
