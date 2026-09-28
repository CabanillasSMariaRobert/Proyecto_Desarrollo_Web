package com.ecommerce.tienda_kalza.servicios;

import com.ecommerce.tienda_kalza.dto.BannerAdmin;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Datos de prueba para el panel de publicidad.
 *
 * Son los cuatro banners del diseno, sin paginacion. Para conectar la base de
 * datos hay que reemplazar obtenerTodos() por bannerRepository.findAll() y
 * dejar la firma igual.
 */
@Service
public class PublicidadAdminService {

    private final List<BannerAdmin> banners;

    public PublicidadAdminService() {
        this.banners = List.of(
                new BannerAdmin(1L, "Colección Hombre 2026",
                        "Banner principal de la portada. Muestra la nueva colección masculina deportiva.",
                        true, "KALZA banner 1.jpg"),
                new BannerAdmin(2L, "Mujer Casual",
                        "Banner secundario de la portada para la linea de calzado femenino casual.",
                        true, "KALZA banner 2.jpg"),
                new BannerAdmin(3L, "Ofertas Especiales",
                        "Banner promocional de descuentos por tiempo limitado.",
                        true, "KALZA banner 3.jpg"),
                new BannerAdmin(4L, "Colección Otoño",
                        "Banner pendiente de publicar para la próxima temporada.",
                        false, null)
        );
    }

    public List<BannerAdmin> obtenerTodos() {
        return banners;
    }
}
