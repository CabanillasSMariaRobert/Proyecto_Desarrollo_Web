package com.ecommerce.tienda_kalza.servicios;

import com.ecommerce.tienda_kalza.dto.EstadoPedido;
import com.ecommerce.tienda_kalza.dto.ItemPedido;
import com.ecommerce.tienda_kalza.dto.PedidoAdmin;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Datos de prueba para el panel de pedidos.
 *
 * Implementa la misma logica de filtro y paginacion que usara un repositorio
 * JPA. Para conectar la base de datos hay que reemplazar el cuerpo de
 * obtenerTodos() por pedidoRepository.findAll() y dejar las firmas igual.
 */
@Service
public class PedidoAdminService {

    private static final int CANTIDAD_PEDIDOS = 342;
    private static final long ID_INICIAL = 20241L;

    private record ProductoCatalogo(String nombre, String imagen, BigDecimal precio) {
    }

    private static final List<ProductoCatalogo> CATALOGO = List.of(
            new ProductoCatalogo("AeroGlide Pro Runner", "AeroGlide Pro Runner.webp", new BigDecimal("389.00")),
            new ProductoCatalogo("Court Classic Low", "Court Classic Low.webp", new BigDecimal("259.00")),
            new ProductoCatalogo("Velocity Pro X", "Velocity Pro X.webp", new BigDecimal("130.00")),
            new ProductoCatalogo("nike Dunk Low", "nike Dunk Low.jpg", new BigDecimal("449.00")),
            new ProductoCatalogo("adidas Samba OG", "adidas Samba OG.webp", new BigDecimal("299.00")),
            new ProductoCatalogo("nike Air Force 1", "nike Air Force 1.webp", new BigDecimal("349.00")),
            new ProductoCatalogo("Apex Trainer 2", "Apex Trainer 2.jpg", new BigDecimal("219.00")),
            new ProductoCatalogo("Terra Grip Pro", "Terra Grip Pro.jpg", new BigDecimal("179.00")),
            new ProductoCatalogo("Quantum Court", "Quantum Court.jpg", new BigDecimal("239.00")),
            new ProductoCatalogo("adidas Forum Low", "adidas Forum Low.jpg", new BigDecimal("189.00")),
            new ProductoCatalogo("Aero Float", "Aero Float.jpg", new BigDecimal("149.00")),
            new ProductoCatalogo("Core Minimal", "Core Minimal.jpg", new BigDecimal("119.00"))
    );

    private static final String[] CLIENTES = {
            "John Doe", "Maria Garcia", "Carlos Perez", "Ana Torres", "Luis Ramirez",
            "Sarah Jenkins", "Michael Chang", "Emma Watson", "Diego Fernandez", "Lucia Vargas",
            "Pedro Sanchez", "Carmen Rojas", "Jorge Castillo", "Rosa Mendoza", "Andres Silva"
    };

    /** Distribucion para que ningun filtro de la vista quede vacio. */
    private static final EstadoPedido[] DISTRIBUCION = {
            EstadoPedido.ENTREGADO, EstadoPedido.ENTREGADO, EstadoPedido.ENTREGADO,
            EstadoPedido.ENVIADO, EstadoPedido.ENVIADO, EstadoPedido.ENVIADO,
            EstadoPedido.PAGADO, EstadoPedido.PAGADO,
            EstadoPedido.PROCESANDO,
            EstadoPedido.PENDIENTE, EstadoPedido.PENDIENTE,
            EstadoPedido.CANCELADO
    };

    private final List<PedidoAdmin> pedidos;

    public PedidoAdminService() {
        this.pedidos = generarPedidos();
    }

    public List<PedidoAdmin> obtenerTodos() {
        return List.copyOf(pedidos);
    }

    public List<PedidoAdmin> filtrarPorEstado(EstadoPedido estado) {
        if (estado == null) {
            return obtenerTodos();
        }
        return pedidos.stream()
                .filter(p -> p.getEstado() == estado)
                .toList();
    }

    /**
     * Recorta la pagina pedida. pagina es base 0.
     */
    public List<PedidoAdmin> paginar(List<PedidoAdmin> origen, int pagina, int porPagina) {
        int desde = Math.min(pagina * porPagina, origen.size());
        int hasta = Math.min(desde + porPagina, origen.size());
        return List.copyOf(origen.subList(desde, hasta));
    }

    public int contarPorEstado(EstadoPedido estado) {
        return (int) pedidos.stream().filter(p -> p.getEstado() == estado).count();
    }

    private List<PedidoAdmin> generarPedidos() {
        // Semilla fija: los datos tienen que ser los mismos en cada recarga.
        Random aleatorio = new Random(20251012L);
        LocalDate hoy = LocalDate.now();
        List<PedidoAdmin> lista = new ArrayList<>(CANTIDAD_PEDIDOS);

        for (int i = 0; i < CANTIDAD_PEDIDOS; i++) {
            PedidoAdmin pedido = new PedidoAdmin();
            pedido.setId(ID_INICIAL + i);
            pedido.setCliente(CLIENTES[aleatorio.nextInt(CLIENTES.length)]);
            pedido.setFecha(hoy.minusDays(aleatorio.nextInt(60)));
            pedido.setEstado(DISTRIBUCION[aleatorio.nextInt(DISTRIBUCION.length)]);

            int cantidadDeItems = 1 + aleatorio.nextInt(4);
            List<ItemPedido> items = new ArrayList<>(cantidadDeItems);
            BigDecimal total = BigDecimal.ZERO;

            // Indices unicos: un pedido no repite el mismo producto.
            Set<Integer> elegidos = new LinkedHashSet<>();
            while (elegidos.size() < cantidadDeItems) {
                elegidos.add(aleatorio.nextInt(CATALOGO.size()));
            }

            for (int indice : elegidos) {
                ProductoCatalogo producto = CATALOGO.get(indice);
                int unidades = 1 + aleatorio.nextInt(3);
                BigDecimal subtotal = producto.precio().multiply(BigDecimal.valueOf(unidades))
                        .setScale(2, RoundingMode.HALF_UP);
                items.add(new ItemPedido(producto.nombre(), unidades, subtotal));
                total = total.add(subtotal);
            }

            pedido.setItems(items);
            pedido.setTotal(total);
            lista.add(pedido);
        }
        return List.copyOf(lista);
    }
}
