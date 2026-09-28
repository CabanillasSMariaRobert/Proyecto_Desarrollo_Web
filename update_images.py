import os

services = {
    "InicioService.java": '''package com.ecommerce.tienda_kalza.servicios.publico;
import org.springframework.stereotype.Service;
import com.ecommerce.tienda_kalza.dto.publico.*;
import java.util.List;
@Service
public class InicioService {
    public PortadaHero obtenerHero() {
        return new PortadaHero("NUEVA COLECCIÓN", "Zapatillas 2024", "Exclusivas", "Ver más", "/catalogo", "/img/banners/KALZA banner 1.jpg");
    }
    public List<CategoriaHome> obtenerCategorias() {
        return List.of(new CategoriaHome(1L, "Zapatillas", "/img/productos/adidas Forum Low.jpg", "/catalogo"));
    }
    public List<DestacadoHome> obtenerDestacados() {
        return List.of(new DestacadoHome(1L, "Nike Air Force 1", "S/ 350.00", "S/ 450.00", "-25%", "/img/productos/nike Air Force 1.webp", true, "/producto/1"));
    }
    public PortadaOferta obtenerOferta() {
        return new PortadaOferta("OFERTA", "50% dscto", "En calzado", "Comprar", "/catalogo", "/img/banners/KALZA banner 2.jpg");
    }
}''',
    "ProductoService.java": '''package com.ecommerce.tienda_kalza.servicios.publico;
import org.springframework.stereotype.Service;
import com.ecommerce.tienda_kalza.dto.publico.*;
import java.util.List;
import java.math.BigDecimal;
@Service
public class ProductoService {
    public ProductoDetalle obtenerDetalle(Long id) {
        return new ProductoDetalle(id, "Nike Air Force 1", "Urbana", new BigDecimal("350.00"), "Zapatillas urbanas clásicas y cómodas.", true, "Stock disponible", 
            List.of(new ImagenProducto(1L, "/img/productos/nike Air Force 1.webp", true)), 
            List.of(new Talla(1L, "40", true)), 
            List.of());
    }
}''',
    "CarritoService.java": '''package com.ecommerce.tienda_kalza.servicios.publico;
import org.springframework.stereotype.Service;
import com.ecommerce.tienda_kalza.dto.publico.*;
import java.util.List;
import java.math.BigDecimal;
@Service
public class CarritoService {
    public ResumenPedido obtenerResumen() {
        return ResumenPedido.calcular(List.of(new ItemCarrito(1L, "Nike Air Force 1", "Urbana", "40", new BigDecimal("350.00"), 1, "/img/productos/nike Air Force 1.webp", "/producto/1")), new BigDecimal("0"), true, "Gratis");
    }
}''',
    "CheckoutService.java": '''package com.ecommerce.tienda_kalza.servicios.publico;
import org.springframework.stereotype.Service;
import com.ecommerce.tienda_kalza.dto.publico.*;
import java.util.List;
import java.math.BigDecimal;
@Service
public class CheckoutService {
    public ResumenPedido obtenerResumen() {
        return ResumenPedido.calcular(List.of(new ItemCarrito(1L, "Nike Air Force 1", "Urbana", "40", new BigDecimal("350.00"), 1, "/img/productos/nike Air Force 1.webp", "/producto/1")), new BigDecimal("15.00"), false, "S/ 15.00");
    }
}''',
    "PedidoService.java": '''package com.ecommerce.tienda_kalza.servicios.publico;
import org.springframework.stereotype.Service;
import com.ecommerce.tienda_kalza.dto.publico.*;
import java.util.List;
import java.math.BigDecimal;
@Service
public class PedidoService {
    public List<PedidoCliente> obtenerPedidos() {
        return List.of(new PedidoCliente("ORD-001", "2024-01-01", "/img/productos/nike Air Force 1.webp", "Nike Air Force 1", "1 item", "Entregado", "bg-success", new BigDecimal("350.00"), "/pedidos", "Ver"));
    }
}''',
    "CuentaService.java": '''package com.ecommerce.tienda_kalza.servicios.publico;
import org.springframework.stereotype.Service;
import com.ecommerce.tienda_kalza.dto.publico.*;
import java.util.List;
import java.math.BigDecimal;
@Service
public class CuentaService {
    public UsuarioPerfil obtenerPerfil() {
        return new UsuarioPerfil("Juan", "Perez", "juan@test.com", "999888777");
    }
    public ResumenUsuario obtenerResumen() {
        return new ResumenUsuario("bi-person", "Perfil", "Detalles", "/perfil", "Editar");
    }
    public List<PedidoCliente> obtenerPedidosRecientes() {
        return List.of(new PedidoCliente("ORD-001", "2024-01-01", "/img/productos/nike Air Force 1.webp", "Nike Air Force 1", "1 item", "Entregado", "bg-success", new BigDecimal("350.00"), "/pedidos", "Ver"));
    }
}''',
    "ContactoService.java": '''package com.ecommerce.tienda_kalza.servicios.publico;
import org.springframework.stereotype.Service;
import com.ecommerce.tienda_kalza.dto.publico.*;
import java.util.List;
@Service
public class ContactoService {
    public DatoContacto obtenerDatos() {
        return new DatoContacto("Av. Principal 123", "999888777", "contacto@kalza.com");
    }
    public List<HorarioAtencion> obtenerHorarios() {
        return List.of(new HorarioAtencion("Lunes - Viernes", "9:00 - 18:00"));
    }
    public List<SeccionLegal> obtenerFaqs() {
        return List.of(new SeccionLegal("¿Tienen cambios?", "Sí, dentro de los 7 días."));
    }
}''',
    "PublicidadService.java": '''package com.ecommerce.tienda_kalza.servicios.publico;
import org.springframework.stereotype.Service;
import com.ecommerce.tienda_kalza.dto.publico.*;
import java.util.List;
@Service
public class PublicidadService {
    public List<BannerPromo> obtenerBanners() {
        return List.of(new BannerPromo(1L, "Promo Exclusiva", "/img/banners/KALZA banner 3.jpg", "/catalogo", "Ver más"));
    }
    public List<SeccionLegal> obtenerLegales() {
        return List.of(new SeccionLegal("Términos y condiciones", "Las promociones están sujetas a stock."));
    }
}'''
}

for name, content in services.items():
    path = os.path.join("src/main/java/com/ecommerce/tienda_kalza/servicios/publico", name)
    with open(path, "w", encoding="utf-8") as f:
        f.write(content)
