package com.ecommerce.tienda_kalza.servicios;

import com.ecommerce.tienda_kalza.dtos.publico.BannerPromo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Banners de la pagina de publicidad.
 *
 * Los cuatro del mockup tienen markup distinto, asi que cada uno declara su
 * Tipo y la vista elige el layout con th:switch. Agregar un banner despues es
 * agregar un item aca y un case en la vista, no reescribir el HTML.
 */
@Service
public class PublicidadService {

    private final BannerPromo hero = new BannerPromo(
            BannerPromo.Tipo.HERO,
            "KALZA banner 1.jpg", "Oferta de temporada KALZA",
            null, "Por tiempo limitado",
            "Rebajas de temporada 30% de descuento",
            "Eleva tu rendimiento con los últimos lanzamientos KALZA. El descuento se aplica automáticamente al finalizar la compra.",
            "/catalogo", "Comprar la colección", "col-12"
    );

    private final List<BannerPromo> banners = List.of(
            new BannerPromo(BannerPromo.Tipo.IMAGEN,
                    "KALZA banner 2.jpg", "Esenciales urbanos",
                    null, null,
                    "Esenciales urbanos",
                    "Hasta 40% de descuento en modelos seleccionados.",
                    "/catalogo", "Explorar", "col-md-8"),
            new BannerPromo(BannerPromo.Tipo.TEXTO,
                    null, null,
                    null, "Nuevos descuentos",
                    "Serie Pro Footwear", null,
                    "/catalogo", "Comprar ahora", "col-md-4"),
            new BannerPromo(BannerPromo.Tipo.ICONO,
                    null, null,
                    "bi-truck", null,
                    "Envío gratis express",
                    "En todos los pedidos superiores a S/ 150. Aplicado al pagar.",
                    null, null, "col-md-4"),
            new BannerPromo(BannerPromo.Tipo.LIQUIDACION,
                    "KALZA banner 3.jpg", "Evento de liquidación",
                    null, null,
                    "Evento de liquidación",
                    "Artículos finales de la última temporada. Precios imbatibles en calzado premium.",
                    "/catalogo", "Ver liquidación", "col-md-8")
    );

    public BannerPromo obtenerHero() {
        return hero;
    }

    public List<BannerPromo> obtenerBanners() {
        return banners;
    }
}
