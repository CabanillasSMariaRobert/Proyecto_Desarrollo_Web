package com.ecommerce.tienda_kalza.servicios.publico;
import org.springframework.stereotype.Service;
import com.ecommerce.tienda_kalza.dto.publico.*;
import java.util.List;
@Service
public class PublicidadService {
    public List<BannerPromo> obtenerBanners() {
        return List.of(new BannerPromo(BannerPromo.Tipo.IMAGEN, "/img/banners/KALZA banner 3.jpg", "Promo", null, "Nuevo", "Promo", "Descripción", "/catalogo", "Ver más", "col-md-6"));
    }
    public List<SeccionLegal> obtenerLegales() {
        return List.of(new SeccionLegal("Términos", "Contenido legal..."));
    }
}
