package com.ecommerce.tienda_kalza.servicios.publico;
import org.springframework.stereotype.Service;
import com.ecommerce.tienda_kalza.dto.publico.*;
import java.util.List;
import java.math.BigDecimal;
@Service
public class InicioService {
    public PortadaHero obtenerHero() {
        return new PortadaHero("NUEVA COLECCIÓN", "Zapatillas 2024", "/img/banners/KALZA banner 1.jpg");
    }
    public List<CategoriaHome> obtenerCategorias() {
        return List.of(new CategoriaHome("Deportivas", "/img/productos/adidas Forum Low.jpg", "Categoría Deportivas", "/catalogo"));
    }
    public List<DestacadoHome> obtenerDestacados() {
        return List.of(new DestacadoHome(1L, "/img/productos/nike Air Force 1.webp", "Zapatillas Nike", "Nike", "Zapatillas 2024", new BigDecimal("300.00"), true));
    }
    public PortadaOferta obtenerOferta() {
        return new PortadaOferta("OFERTA", "50% dscto", "En calzado", "/img/banners/KALZA banner 2.jpg");
    }
}
