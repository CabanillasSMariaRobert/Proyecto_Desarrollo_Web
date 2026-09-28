package com.ecommerce.tienda_kalza.servicios;

import com.ecommerce.tienda_kalza.dto.publico.FilaGuiaTallas;
import com.ecommerce.tienda_kalza.dto.publico.ImagenProducto;
import com.ecommerce.tienda_kalza.dto.publico.ProductoDetalle;
import com.ecommerce.tienda_kalza.dto.publico.ProductoPublico;
import com.ecommerce.tienda_kalza.dto.publico.Talla;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * Detalle de producto.
 *
 * Los relacionados salen del CatalogoService, asi que muestran el precio
 * vigente y no el que tenia el HTML original. Cuando el catalogo venga de la
 * base, esto deja de ser moot y el precio queda garantizado.
 */
@Service
public class ProductoService {

    private static final Long PRODUCTO_POR_DEFECTO = 7L;
    private static final List<Long> RELACIONADOS = List.of(11L, 13L, 9L, 10L);

    private static final String DESCRIPCION = """
            Las Caven 2.0 le dan un sutil giro a una silueta clásica de basketball de los años 80, \
            ofreciendo un look atemporal para la cancha o fuera de ella. Combinando la comodidad de \
            SOFTFOAM+ con una estética retro, estas zapatillas unisex son la esencia del estilo deportivo.""";

    private final CatalogoService catalogo;

    public ProductoService(CatalogoService catalogo) {
        this.catalogo = catalogo;
    }

    /**
     * Detalle del producto pedido. Si el id no existe o es null cae en el
     * producto por defecto: las tarjetas del catalogo mock todavia apuntan a
     * /producto sin id.
     */
    public ProductoDetalle obtenerDetalle(Long id) {
        ProductoPublico producto = catalogo.buscarPorId(id);
        if (producto == null) {
            producto = catalogo.buscarPorId(PRODUCTO_POR_DEFECTO);
        }
        return new ProductoDetalle(
                producto.id(),
                producto.nombre(),
                "Zapatilla lifestyle unisex",
                producto.precio(),
                DESCRIPCION,
                producto.nuevo(),
                producto.enStock() ? "En stock - se envía hoy" : "Sin stock por el momento",
                imagenes(producto),
                tallas(),
                relacionados()
        );
    }

    public List<FilaGuiaTallas> obtenerGuiaTallas() {
        return List.of(
                new FilaGuiaTallas("6", "7.5", "39", "24.5"),
                new FilaGuiaTallas("6.5", "8", "39.5", "25.0"),
                new FilaGuiaTallas("7", "8.5", "40", "25.5"),
                new FilaGuiaTallas("7.5", "9", "41", "26.0"),
                new FilaGuiaTallas("8", "9.5", "42", "26.5"),
                new FilaGuiaTallas("8.5", "10", "42.5", "27.0"),
                new FilaGuiaTallas("9", "10.5", "43", "27.5"),
                new FilaGuiaTallas("9.5", "11", "44", "28.0"),
                new FilaGuiaTallas("10", "11.5", "44.5", "28.5"),
                new FilaGuiaTallas("10.5", "12", "45", "29.0")
        );
    }

    /** La galeria repite la imagen principal: el mockup no tiene fotos extra. */
    private List<ImagenProducto> imagenes(ProductoPublico producto) {
        return List.of(
                new ImagenProducto(producto.imagen(), producto.nombre()),
                new ImagenProducto(producto.imagen(), "Vista de la suela"),
                new ImagenProducto(producto.imagen(), "Vista lateral")
        );
    }

    private List<Talla> tallas() {
        return List.of(
                new Talla("39", false, true),
                new Talla("40", false, true),
                new Talla("41", true, true),
                new Talla("42", false, true),
                new Talla("43", false, true),
                new Talla("44", false, true),
                new Talla("45", false, false)
        );
    }

    private List<ProductoPublico> relacionados() {
        return RELACIONADOS.stream()
                .map(catalogo::buscarPorId)
                .filter(Objects::nonNull)
                .toList();
    }
}
