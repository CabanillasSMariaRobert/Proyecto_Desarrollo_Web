package com.ecommerce.tienda_kalza.servicios;

import com.ecommerce.tienda_kalza.dtos.publico.FiltroCategoria;
import com.ecommerce.tienda_kalza.dtos.publico.Ordenamiento;
import com.ecommerce.tienda_kalza.dtos.publico.PaginaCatalogo;
import com.ecommerce.tienda_kalza.dtos.publico.ProductoPublico;
import com.ecommerce.tienda_kalza.dtos.publico.RangoPrecio;
import com.ecommerce.tienda_kalza.dtos.publico.Talla;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Catalogo de prueba: la fuente unica de productos y precios de la tienda.
 *
 * Existe para que un mismo SKU no aparezca con dos precios distintos. Antes de
 * esto, la portada, el listado y los relacionados traian sus propios numeros
 * desde el HTML y el mismo modelo costaba 459 en un lado y 299.90 en otro.
 * Ahora portada, listado y detalle leen de esta lista, asi que el precio es
 * uno solo por producto.
 *
 * Ojo con la distincion: el carrito NO lee de aca. Sus lineas guardan el
 * precio del momento en que se agrego el producto, que es un snapshot y puede
 * diferir del precio vigente. Por eso CarritoService tiene sus propias cifras.
 *
 * Para conectar la base de datos, obtenerProductos() debe devolver el mapeo de
 * productoRepository.findAll() conservando la misma firma; el resto de metodos
 * se derivan de esa lista y no necesitan cambios.
 */
@Service
public class CatalogoService {

    private static final int TOTAL_PRODUCTOS = 124;
    private static final int PRODUCTOS_POR_PAGINA = 6;
    private static final int PAGINAS_VISIBLES = 3;

    private final List<ProductoPublico> productos;
    private final List<FiltroCategoria> categorias;
    private final List<Talla> tallas;
    private final List<Ordenamiento> ordenamientos;
    private final RangoPrecio rangoPrecio;

    public CatalogoService() {
        this.productos = List.copyOf(generarProductos());
        this.categorias = List.of(
                new FiltroCategoria("catRunning", "Running", false),
                new FiltroCategoria("catTraining", "Training", true),
                new FiltroCategoria("catBasket", "Basketball", false),
                new FiltroCategoria("catCasual", "Casual", false)
        );
        this.tallas = List.of(
                new Talla("8", false, true),
                new Talla("8.5", false, true),
                new Talla("9", true, true),
                new Talla("9.5", false, true),
                new Talla("10", false, true),
                new Talla("10.5", false, false)
        );
        this.ordenamientos = List.of(
                new Ordenamiento("Novedades", true),
                new Ordenamiento("Precio: menor a mayor", false),
                new Ordenamiento("Precio: mayor a menor", false),
                new Ordenamiento("Más vendidos", false)
        );
        this.rangoPrecio = new RangoPrecio(BigDecimal.ZERO, new BigDecimal("300"));
    }

    public List<ProductoPublico> obtenerProductos() {
        return productos;
    }

    /** Productos de una pagina, 1-indexada. Una pagina fuera de rango se recorta. */
    public List<ProductoPublico> obtenerProductos(int pagina) {
        int desde = Math.max(0, (pagina - 1) * PRODUCTOS_POR_PAGINA);
        if (desde >= productos.size()) {
            return List.of();
        }
        return productos.subList(desde, Math.min(desde + PRODUCTOS_POR_PAGINA, productos.size()));
    }

    public int obtenerTotalPaginas() {
        return (int) Math.ceil((double) productos.size() / PRODUCTOS_POR_PAGINA);
    }

    public List<FiltroCategoria> obtenerCategorias() {
        return categorias;
    }

    public List<Talla> obtenerTallas() {
        return tallas;
    }

    public List<Ordenamiento> obtenerOrdenamientos() {
        return ordenamientos;
    }

    public RangoPrecio obtenerRangoPrecio() {
        return rangoPrecio;
    }

    /**
     * Barra de paginacion: flecha anterior, hasta tres numeros, separador y
     * flecha siguiente. La primera y la ultima flecha se deshabilitan segun la
     * pagina actual, igual que en el mockup.
     */
    public List<PaginaCatalogo> obtenerPaginacion(int paginaActual) {
        int total = obtenerTotalPaginas();
        int desde = Math.max(1, Math.min(paginaActual, total) - 1);
        int hasta = Math.min(desde + PAGINAS_VISIBLES - 1, total);

        List<PaginaCatalogo> items = new ArrayList<>();
        items.add(new PaginaCatalogo("", 0, false, paginaActual > 1, false));

        for (int numero = desde; numero <= hasta; numero++) {
            items.add(PaginaCatalogo.pagina(numero, numero == paginaActual, true));
        }

        if (hasta < total) {
            items.add(PaginaCatalogo.separador());
        }

        items.add(new PaginaCatalogo("", 0, false, paginaActual < total, false));
        return List.copyOf(items);
    }

    /** Busca un producto por id. Devuelve null si no existe, como el repositorio. */
    public ProductoPublico buscarPorId(Long id) {
        if (id == null) {
            return null;
        }
        return productos.stream()
                .filter(producto -> producto.id().equals(id))
                .findFirst()
                .orElse(null);
    }

    private List<ProductoPublico> generarProductos() {
        // Semilla fija: recargar la pagina no puede cambiar los datos de prueba.
        Random aleatorio = new Random(20260310L);
        List<ProductoPublico> lista = new ArrayList<>(TOTAL_PRODUCTOS);

        // Los trece productos con los que cuenta el diseno. Sus precios son los
        // canonicos de la tienda: portada, listado y relacionados los muestran.
        lista.add(new ProductoPublico(1L, "Velocity Pro X", "Velocity Pro X.webp",
                "Zapatilla Running Hombre", new BigDecimal("180.00"), null, true, "8-12", true));
        lista.add(new ProductoPublico(2L, "Apex Trainer 2", "Apex Trainer 2.jpg",
                "Zapatilla Training Mujer", new BigDecimal("120.00"), new BigDecimal("150.00"), true, "6-10", false));
        lista.add(new ProductoPublico(3L, "Core Minimal", "Core Minimal.jpg",
                "Zapatilla Casual Unisex", new BigDecimal("95.00"), null, false, "-", false));
        lista.add(new ProductoPublico(4L, "Quantum Court", "Quantum Court.jpg",
                "Zapatilla Basketball Hombre", new BigDecimal("210.00"), null, true, "9-13", false));
        lista.add(new ProductoPublico(5L, "Aero Float", "Aero Float.jpg",
                "Zapatilla Lifestyle Mujer", new BigDecimal("140.00"), null, true, "6-10", false));
        lista.add(new ProductoPublico(6L, "Terra Grip Pro", "Terra Grip Pro.jpg",
                "Zapatilla Trail Unisex", new BigDecimal("165.00"), null, true, "7-11", false));
        lista.add(new ProductoPublico(7L, "Footwear Loui", "zapatillas footwear loui.jpg",
                "Zapatilla Lifestyle Unisex", new BigDecimal("300.00"), null, true, "39-45", true));
        lista.add(new ProductoPublico(8L, "adidas Samba OG", "adidas Samba OG.webp",
                "Zapatilla Urbano Unisex", new BigDecimal("499.00"), null, true, "7-12", true));
        lista.add(new ProductoPublico(9L, "nike Air Force 1", "nike Air Force 1.webp",
                "Zapatilla Clasica Hombre", new BigDecimal("429.00"), null, true, "7-12", false));
        lista.add(new ProductoPublico(10L, "adidas Forum Low", "adidas Forum Low.jpg",
                "Zapatilla Skater Unisex", new BigDecimal("398.00"), null, true, "6-11", false));
        lista.add(new ProductoPublico(11L, "nike Dunk Low", "nike Dunk Low.jpg",
                "Zapatilla Running Unisex", new BigDecimal("459.00"), null, true, "6-12", false));
        lista.add(new ProductoPublico(12L, "AeroGlide Pro Runner", "AeroGlide Pro Runner.webp",
                "Zapatilla Running Mujer", new BigDecimal("145.00"), null, true, "8-12", false));
        lista.add(new ProductoPublico(13L, "Court Classic Low", "Court Classic Low.webp",
                "Zapatilla Basquetbol Unisex", new BigDecimal("229.00"), null, true, "6-11", false));

        // El resto existe para que la paginacion tenga contenido y el contador
        // del encabezado sea cierto. Reutilizan las imagenes reales de la carpeta.
        String[] imagenes = {
                "Velocity Pro X.webp", "Apex Trainer 2.jpg", "Core Minimal.jpg",
                "Quantum Court.jpg", "Aero Float.jpg", "Terra Grip Pro.jpg",
                "zapatillas footwear loui.jpg", "adidas Samba OG.webp", "nike Air Force 1.webp",
                "adidas Forum Low.jpg", "nike Dunk Low.jpg", "AeroGlide Pro Runner.webp",
                "Court Classic Low.webp"
        };
        String[] lineas = {
                "Zapatilla Running Hombre", "Zapatilla Training Mujer", "Zapatilla Casual Unisex",
                "Zapatilla Basketball Hombre", "Zapatilla Lifestyle Mujer", "Zapatilla Trail Unisex",
                "Zapatilla Lifestyle Unisex", "Zapatilla Urbano Unisex", "Zapatilla Clasica Hombre",
                "Zapatilla Skater Unisex", "Zapatilla Running Unisex", "Zapatilla Running Mujer",
                "Zapatilla Basquetbol Unisex"
        };
        String[] tallasTexto = {"7-11", "6-10", "8-12", "9-13", "5-9", "6-11"};

        for (int i = lista.size(); i < TOTAL_PRODUCTOS; i++) {
            int base = i % imagenes.length;
            int tallasBase = i % tallasTexto.length;
            int precio = 90 + aleatorio.nextInt(420);
            boolean sinStock = i % 17 == 0;

            lista.add(new ProductoPublico(
                    (long) i + 1,
                    MODELOS[i % MODELOS.length] + " " + (i + 1),
                    imagenes[base],
                    lineas[base],
                    new BigDecimal(precio + ".00"),
                    null,
                    !sinStock,
                    sinStock ? "-" : tallasTexto[tallasBase],
                    i % 11 == 0
            ));
        }
        return lista;
    }

    private static final String[] MODELOS = {
            "Runner", "Trainer", "Sneaker", "Basket", "Trekker", "Urban",
            "Classic", "Retro", "Trail", "Slide", "Court", "Escape", "Aero"
    };
}
