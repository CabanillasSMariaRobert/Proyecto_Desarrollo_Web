$base_dir = "src/main/java/com/ecommerce/tienda_kalza/servicios/publico"
New-Item -ItemType Directory -Force -Path $base_dir | Out-Null

$services = @{
    "InicioService.java" = @'
package com.ecommerce.tienda_kalza.servicios.publico;
import org.springframework.stereotype.Service;
import com.ecommerce.tienda_kalza.dto.publico.*;
import java.util.List;
@Service
public class InicioService {
    public PortadaHero obtenerHero() {
        return new PortadaHero("NUEVA COLECCIÓN", "Zapatillas 2024", "Exclusivas", "Ver más", "/catalogo", "/img/banner.jpg");
    }
    public List<CategoriaHome> obtenerCategorias() {
        return List.of(new CategoriaHome(1L, "Deportivas", "/img/cat.jpg", "/catalogo"));
    }
    public List<DestacadoHome> obtenerDestacados() {
        return List.of(new DestacadoHome(1L, "Nike", "S/ 300.00", "S/ 400.00", "-25%", "/img/p1.jpg", true, "/producto/1"));
    }
    public PortadaOferta obtenerOferta() {
        return new PortadaOferta("OFERTA", "50% dscto", "En calzado", "Comprar", "/catalogo", "/img/of.jpg");
    }
}
'@
    "ProductoService.java" = @'
package com.ecommerce.tienda_kalza.servicios.publico;
import org.springframework.stereotype.Service;
import com.ecommerce.tienda_kalza.dto.publico.*;
import java.util.List;
import java.math.BigDecimal;
@Service
public class ProductoService {
    public ProductoDetalle obtenerDetalle(Long id) {
        return new ProductoDetalle(id, "Nike", "Urbana", new BigDecimal("350.00"), "Desc", true, "Stock", 
            List.of(new ImagenProducto(1L, "/img/p1.jpg", true)), 
            List.of(new Talla(1L, "40", true)), 
            List.of());
    }
}
'@
    "CarritoService.java" = @'
package com.ecommerce.tienda_kalza.servicios.publico;
import org.springframework.stereotype.Service;
import com.ecommerce.tienda_kalza.dto.publico.*;
import java.util.List;
import java.math.BigDecimal;
@Service
public class CarritoService {
    public ResumenPedido obtenerResumen() {
        return ResumenPedido.calcular(List.of(new ItemCarrito(1L, "Nike", "Urbana", "40", new BigDecimal("350.00"), 1, "/img/p1.jpg", "/producto/1")), new BigDecimal("0"), true, "Gratis");
    }
}
'@
    "CheckoutService.java" = @'
package com.ecommerce.tienda_kalza.servicios.publico;
import org.springframework.stereotype.Service;
import com.ecommerce.tienda_kalza.dto.publico.*;
import java.util.List;
import java.math.BigDecimal;
@Service
public class CheckoutService {
    public ResumenPedido obtenerResumen() {
        return ResumenPedido.calcular(List.of(new ItemCarrito(1L, "Nike", "Urbana", "40", new BigDecimal("350.00"), 1, "/img/p1.jpg", "/producto/1")), new BigDecimal("15.00"), false, "S/ 15.00");
    }
}
'@
    "PedidoService.java" = @'
package com.ecommerce.tienda_kalza.servicios.publico;
import org.springframework.stereotype.Service;
import com.ecommerce.tienda_kalza.dto.publico.*;
import java.util.List;
import java.math.BigDecimal;
@Service
public class PedidoService {
    public List<PedidoCliente> obtenerPedidos() {
        return List.of(new PedidoCliente("ORD-001", "2024-01-01", "/img/p1.jpg", "Nike", "1 item", "Entregado", "bg-success", new BigDecimal("350.00"), "/pedidos/1", "Ver"));
    }
}
'@
    "CuentaService.java" = @'
package com.ecommerce.tienda_kalza.servicios.publico;
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
        return List.of(new PedidoCliente("ORD-001", "2024-01-01", "/img/p1.jpg", "Nike", "1 item", "Entregado", "bg-success", new BigDecimal("350.00"), "/pedidos/1", "Ver"));
    }
}
'@
    "ContactoService.java" = @'
package com.ecommerce.tienda_kalza.servicios.publico;
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
}
'@
    "PublicidadService.java" = @'
package com.ecommerce.tienda_kalza.servicios.publico;
import org.springframework.stereotype.Service;
import com.ecommerce.tienda_kalza.dto.publico.*;
import java.util.List;
@Service
public class PublicidadService {
    public List<BannerPromo> obtenerBanners() {
        return List.of(new BannerPromo(1L, "Promo", "/img/promo.jpg", "/catalogo", "Ver más"));
    }
    public List<SeccionLegal> obtenerLegales() {
        return List.of(new SeccionLegal("Términos", "Contenido legal..."));
    }
}
'@
}

$services.GetEnumerator() | ForEach-Object {
    Set-Content -Path (Join-Path $base_dir $_.Key) -Value $_.Value -Encoding UTF8
}
Write-Host "Services created successfully."
