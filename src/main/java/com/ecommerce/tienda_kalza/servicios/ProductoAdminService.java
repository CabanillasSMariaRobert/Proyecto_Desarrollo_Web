package com.ecommerce.tienda_kalza.servicios;

import com.ecommerce.tienda_kalza.dtos.ProductoAdmin;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Datos de prueba para el panel de productos.
 *
 * Las primeras filas son las del diseno; el resto se genera para que la
 * paginacion tenga contenido real. Para conectar la base de datos hay que
 * reemplazar obtenerTodos() por productoRepository.findAll() y dejar la
 * firma igual.
 */
@Service
public class ProductoAdminService {

    private static final int CANTIDAD_PRODUCTOS = 45;

    private static final String[] NOMBRES = {
            "Velocity Pro X", "Apex Trainer 2", "Terra Grip Pro",
            "Quantum Court", "Core Minimal", "AeroGlide Pro Runner",
            "Court Classic Low", "adidas Samba OG", "nike Air Force 1",
            "adidas Forum Low", "Aero Float", "nike Dunk Low"
    };

    private static final String[] CATEGORIAS = {
            "Deportivo", "Casual", "Deportivo", "Tenis", "Urbano", "Running",
            "Casual", "Casual", "Deportivo", "Skate", "Casual", "Deportivo"
    };

    private static final String[] IMAGENES = {
            "Velocity Pro X.webp", "Apex Trainer 2.jpg", "Terra Grip Pro.jpg",
            "Quantum Court.jpg", "Core Minimal.jpg", "AeroGlide Pro Runner.webp",
            "Court Classic Low.webp", "adidas Samba OG.webp", "nike Air Force 1.webp",
            "adidas Forum Low.jpg", "Aero Float.jpg", "nike Dunk Low.jpg"
    };

    /** Tallas del diseno para las tres primeras filas. */
    private static final String[] TALLAS_INICIALES = {"42, 43, 44", "39, 40, 41, 42", "-"};

    private final List<ProductoAdmin> productos;

    public ProductoAdminService() {
        this.productos = generarProductos();
    }

    public List<ProductoAdmin> obtenerTodos() {
        return List.copyOf(productos);
    }

    public List<ProductoAdmin> buscar(String texto) {
        if (texto == null || texto.isBlank()) {
            return obtenerTodos();
        }
        String filtro = texto.trim().toLowerCase();
        return productos.stream()
                .filter(p -> p.getNombre().toLowerCase().contains(filtro)
                        || p.getCategoria().toLowerCase().contains(filtro))
                .toList();
    }

    private List<ProductoAdmin> generarProductos() {
        // Semilla fija: los datos tienen que ser los mismos en cada recarga.
        Random aleatorio = new Random(20260201L);
        List<ProductoAdmin> lista = new ArrayList<>(CANTIDAD_PRODUCTOS);

        for (int i = 0; i < CANTIDAD_PRODUCTOS; i++) {
            ProductoAdmin producto = new ProductoAdmin();
            producto.setId(1L + i);
            int indice = i % NOMBRES.length;
            producto.setNombre(NOMBRES[indice]);
            producto.setCategoria(CATEGORIAS[indice]);
            producto.setImagen(IMAGENES[indice]);

            if (i < TALLAS_INICIALES.length) {
                // Primera fila deshabilitada: coincide con el diseno.
                boolean deshabilitado = i == 2;
                producto.setActivo(!deshabilitado);
                producto.setStock(deshabilitado ? 0 : (i == 0 ? 45 : 12));
                producto.setPrecio(new BigDecimal(i == 0 ? "120.00" : i == 1 ? "95.00" : "110.00"));
                producto.setTallas(TALLAS_INICIALES[i]);
            } else {
                producto.setActivo(true);
                producto.setStock(1 + aleatorio.nextInt(80));
                producto.setPrecio(BigDecimal.valueOf(50 + aleatorio.nextInt(400))
                        .setScale(2, java.math.RoundingMode.HALF_UP));
                producto.setTallas(generarTallas(aleatorio));
            }
            lista.add(producto);
        }
        return List.copyOf(lista);
    }

    private String generarTallas(Random aleatorio) {
        int cantidad = 2 + aleatorio.nextInt(3);
        int desde = 36 + aleatorio.nextInt(6);
        List<String> tallas = new ArrayList<>(cantidad);
        for (int i = 0; i < cantidad; i++) {
            tallas.add(String.valueOf(desde + i));
        }
        return String.join(", ", tallas);
    }
}
