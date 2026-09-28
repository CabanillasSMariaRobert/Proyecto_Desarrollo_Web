package com.ecommerce.tienda_kalza.servicios;

import com.ecommerce.tienda_kalza.dto.publico.CategoriaHome;
import com.ecommerce.tienda_kalza.dto.publico.DestacadoHome;
import com.ecommerce.tienda_kalza.dto.publico.PortadaHero;
import com.ecommerce.tienda_kalza.dto.publico.PortadaOferta;
import com.ecommerce.tienda_kalza.dto.publico.ProductoPublico;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Datos de la portada.
 *
 * Los destacados no inventan precios: toman el producto del CatalogoService y
 * solo le cambian la presentacion, separando la marca del nombre. Esa es la
 * razon de existir de DestacadoHome.
 */
@Service
public class InicioService {

    private static final String[] MARCAS = {"adidas", "nike"};

    private final CatalogoService catalogo;

    private final List<CategoriaHome> categorias = List.of(
            new CategoriaHome("Hombre", "nike Dunk Low.jpg", "Zapatillas para hombre", "/catalogo"),
            new CategoriaHome("Mujer", "Aero Float.jpg", "Zapatillas para mujer", "/catalogo"),
            new CategoriaHome("Unisex", "adidas Samba OG.webp", "Zapatillas unisex", "/catalogo"),
            new CategoriaHome("Running", "Velocity Pro X.webp", "Zapatillas deportivas", "/catalogo")
    );

    private final PortadaHero hero = new PortadaHero(
            "Encuentra tu próximo par favorito",
            "Descubre lo último en tendencias y encuentra el que más se adecue a tu estilo.",
            "zapatillas footwear loui.jpg"
    );

    private final PortadaOferta oferta = new PortadaOferta(
            "Oferta especial",
            "Hasta 30% de descuento",
            "Aprovecha nuestras promociones especiales en modelos seleccionados.",
            "zapatillas footwear loui.jpg"
    );

    public InicioService(CatalogoService catalogo) {
        this.catalogo = catalogo;
    }

    public PortadaHero obtenerHero() {
        return hero;
    }

    public List<CategoriaHome> obtenerCategorias() {
        return categorias;
    }

    public PortadaOferta obtenerOferta() {
        return oferta;
    }

    /** Los cuatro destacados del diseno, en ese orden. */
    public List<DestacadoHome> obtenerDestacados() {
        return List.of(destacado(8L), destacado(9L), destacado(10L), destacado(11L));
    }

    private DestacadoHome destacado(Long id) {
        ProductoPublico producto = catalogo.buscarPorId(id);
        String[] partes = producto.nombre().split(" ", 2);
        String marca = partes.length > 1 ? partes[0] : MARCAS[0];
        String nombre = partes.length > 1 ? partes[1] : producto.nombre();

        return new DestacadoHome(producto.id(), producto.imagen(), producto.nombre(),
                marca, nombre, producto.precio(), producto.nuevo());
    }
}
