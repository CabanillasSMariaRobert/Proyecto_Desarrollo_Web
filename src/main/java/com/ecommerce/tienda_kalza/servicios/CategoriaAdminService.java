package com.ecommerce.tienda_kalza.servicios;

import com.ecommerce.tienda_kalza.dto.CategoriaAdmin;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Datos de prueba para el panel de categorias.
 *
 * Las primeras filas son las del diseno; el resto se genera para que la
 * paginacion tenga contenido real. Para conectar la base de datos hay que
 * reemplazar obtenerTodos() por categoriaRepository.findAll() y dejar la
 * firma igual.
 */
@Service
public class CategoriaAdminService {

    private static final int CANTIDAD_CATEGORIAS = 12;

    private static final String[] NOMBRES = {
            "Hombre Deportivo", "Mujer Casual", "Niños", "Ofertas Especiales",
            "Running", "Training", "Skate", "Urbano", "Fútbol", "Tenis",
            "Botas", "Accesorios"
    };

    /** De la tercera en adelante la imagen es null: la vista pone placeholder. */
    private static final String[] IMAGENES = {
            "nike Dunk Low.jpg", "Aero Float.jpg", null, "zapatillas footwear loui.jpg",
            "Velocity Pro X.webp", "Quantum Court.jpg", "Core Minimal.jpg", "AeroGlide Pro Runner.webp",
            "adidas Samba OG.webp", "Terra Grip Pro.jpg", "adidas Forum Low.jpg", "Court Classic Low.webp"
    };

    /** Conteos fijos de las cuatro primeras filas, tal como aparecen en el diseno. */
    private static final int[] CONTEO_FIJO = {1245, 892, 430, 2156};

    private final List<CategoriaAdmin> categorias;

    public CategoriaAdminService() {
        this.categorias = generarCategorias();
    }

    public List<CategoriaAdmin> obtenerTodos() {
        return List.copyOf(categorias);
    }

    private List<CategoriaAdmin> generarCategorias() {
        // Semilla fija: los datos tienen que ser los mismos en cada recarga.
        Random aleatorio = new Random(20260115L);
        List<CategoriaAdmin> lista = new ArrayList<>(CANTIDAD_CATEGORIAS);

        for (int i = 0; i < CANTIDAD_CATEGORIAS; i++) {
            CategoriaAdmin categoria = new CategoriaAdmin();
            categoria.setId(1L + i);
            categoria.setNombre(NOMBRES[i]);
            categoria.setImagen(IMAGENES[i]);
            // El diseno marca la tercera como inactiva; el resto se alterna.
            categoria.setActivo(i != 2);
            if (i < CONTEO_FIJO.length) {
                categoria.setCantidadProductos(CONTEO_FIJO[i]);
            } else {
                categoria.setCantidadProductos(categoria.isActivo()
                        ? 1 + aleatorio.nextInt(1200)
                        : aleatorio.nextInt(500));
            }
            lista.add(categoria);
        }
        return List.copyOf(lista);
    }
}
