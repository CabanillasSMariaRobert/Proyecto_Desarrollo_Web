package com.ecommerce.tienda_kalza.servicios;

import com.ecommerce.tienda_kalza.dto.publico.AccesoRapido;
import com.ecommerce.tienda_kalza.dto.publico.PedidoCliente;
import com.ecommerce.tienda_kalza.dto.publico.ResumenUsuario;
import com.ecommerce.tienda_kalza.dto.publico.UsuarioPerfil;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Cuenta del cliente: perfil y resumenes.
 *
 * Sirve a las dos paginas de cuenta (perfil y usuario) porque comparten los
 * pedidos recientes y los mismos datos personales.
 */
@Service
public class CuentaService {

    private final PedidoClienteService pedidos;

    private final UsuarioPerfil perfil = new UsuarioPerfil(
            "John", "Doe", "john.doe@example.com", "+51 987 654 321");

    private final List<ResumenUsuario> resumenes = List.of(
            new ResumenUsuario("bi-bag", "Pedidos", "Tienes 3 pedidos realizados", "/pedidos", "Ver historial"),
            new ResumenUsuario("bi-geo-alt", "Direcciones", "1 dirección guardada", "/perfil", "Administrar"),
            new ResumenUsuario("bi-heart", "Favoritos", "4 productos favoritos", "/catalogo", "Explorar")
    );

    private final List<AccesoRapido> accesos = List.of(
            new AccesoRapido("bi-person", "Mi perfil", "/perfil"),
            new AccesoRapido("bi-box-seam", "Mis pedidos", "/pedidos"),
            new AccesoRapido("bi-cart3", "Mi carrito", "/carrito"),
            new AccesoRapido("bi-bag", "Comprar", "/catalogo")
    );

    public CuentaService(PedidoClienteService pedidos) {
        this.pedidos = pedidos;
    }

    public UsuarioPerfil obtenerPerfil() {
        return perfil;
    }

    public List<PedidoCliente> obtenerPedidosRecientes() {
        return pedidos.obtenerRecientes();
    }

    public List<ResumenUsuario> obtenerResumenes() {
        return resumenes;
    }

    public List<AccesoRapido> obtenerAccesos() {
        return accesos;
    }

    public String obtenerSaludo() {
        return perfil.nombre() + " " + perfil.apellido();
    }
}
