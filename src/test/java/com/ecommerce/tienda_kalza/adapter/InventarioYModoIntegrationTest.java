package com.ecommerce.tienda_kalza.adapter;

import com.ecommerce.tienda_kalza.adapter.entity.*;
import com.ecommerce.tienda_kalza.adapter.enums.RolUsuario;
import com.ecommerce.tienda_kalza.adapter.model.Usuario;
import com.ecommerce.tienda_kalza.adapter.service.*;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.*;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {"spring.datasource.url=jdbc:h2:mem:inventario-modo;DB_CLOSE_DELAY=-1;LOCK_TIMEOUT=10000",
        "spring.jpa.hibernate.ddl-auto=create-drop", "spring.jpa.open-in-view=false", "spring.jpa.show-sql=false"})
@AutoConfigureMockMvc
class InventarioYModoIntegrationTest {
    @Autowired EntityManager em;
    @Autowired CompraService compras;
    @Autowired UsuarioService usuarios;
    @Autowired PlatformTransactionManager manager;
    @Autowired MockMvc mvc;
    @Autowired PasswordEncoder encoder;
    TransactionTemplate tx;
    int variante, segunda, cliente, admin;
    String correoAdmin, correoCliente;

    @BeforeEach void preparar() {
        tx = new TransactionTemplate(manager);
        tx.executeWithoutResult(s -> {
            String sufijo = UUID.randomUUID().toString();
            MarcaEntity marca = MarcaEntity.builder().nombre("Marca " + sufijo).build();
            em.persist(marca);
            ProductoEntity producto = ProductoEntity.builder().nombre("Prueba inventario " + sufijo)
                    .marca(marca).precioOriginal(BigDecimal.TEN).build();
            em.persist(producto);
            ZapatoEntity zapato = new ZapatoEntity();
            zapato.setProducto(producto);
            em.persist(zapato);
            VarianteZapatoEntity v = VarianteZapatoEntity.builder().zapato(zapato).talla(40).color("NEGRO").stock(1).build();
            em.persist(v);
            variante = v.getIdVariante();
            VarianteZapatoEntity otra = VarianteZapatoEntity.builder().zapato(zapato).talla(41).color("NEGRO").stock(0).build();
            em.persist(otra);
            segunda = otra.getIdVariante();
            correoCliente = "cliente-" + sufijo + "@test.com";
            correoAdmin = "admin-" + sufijo + "@test.com";
            cliente = crearUsuario(correoCliente, RolUsuario.COMUN);
            admin = crearUsuario(correoAdmin, RolUsuario.ADMINISTRADOR);
        });
    }

    int crearUsuario(String correo, RolUsuario rol) {
        var u = UsuarioEntity.builder().nombres("Robert").apellidos("Prueba").correo(correo)
                .clave(encoder.encode("Clave123!")).rol(rol).build();
        em.persist(u);
        return u.getIdUsuario();
    }

    int crearCompra(String estado, int... ids) {
        return tx.execute(s -> {
            var compra = CompraEntity.builder().cliente(em.find(UsuarioEntity.class, cliente))
                    .estado(estado).totalSinDescuento(BigDecimal.TEN).totalConDescuento(BigDecimal.TEN).build();
            for (int id : ids) compra.getDetalles().add(DetalleCompraEntity.builder().compra(compra)
                    .varianteZapato(em.find(VarianteZapatoEntity.class, id)).nombreDelProducto("Prueba")
                    .cantidad(1).precioOriginalXUnidad(BigDecimal.TEN).build());
            return compras.guardar(compra).getIdCompra();
        });
    }

    int stock(int id) { return tx.execute(s -> em.find(VarianteZapatoEntity.class, id).getStock()); }
    String estado(int id) { return tx.execute(s -> compras.obtener(id).getEstado()); }

    @Test void clienteDesactivadoNoPuedeConfirmarPendiente() {
        int id = crearCompra("pendiente", variante);
        tx.executeWithoutResult(s -> em.find(UsuarioEntity.class, cliente).setEstaActivo(false));
        assertThatThrownBy(() -> compras.cambiarEstado(id, "pagado"))
                .isInstanceOf(ReglaNegocioException.class);
        assertThat(stock(variante)).isEqualTo(1);
        assertThat(estado(id)).isEqualTo("pendiente");
        assertThatThrownBy(() -> tx.executeWithoutResult(s -> {
            var compra = compras.obtener(id);
            compra.setEstado("pagado");
            compras.guardar(compra);
        })).isInstanceOf(ReglaNegocioException.class);
        assertThat(stock(variante)).isEqualTo(1);
        assertThat(estado(id)).isEqualTo("pendiente");
    }

    @Test void compraNuevaValidaEstadoPersistidoDelCliente() {
        UsuarioEntity anterior = tx.execute(s -> em.find(UsuarioEntity.class, cliente));
        tx.executeWithoutResult(s -> em.find(UsuarioEntity.class, cliente).setEstaActivo(false));
        assertThatThrownBy(() -> tx.executeWithoutResult(s -> {
            var compra = CompraEntity.builder().cliente(anterior).estado("pagado")
                    .totalSinDescuento(BigDecimal.TEN).totalConDescuento(BigDecimal.TEN).build();
            compra.getDetalles().add(DetalleCompraEntity.builder().compra(compra)
                    .varianteZapato(em.find(VarianteZapatoEntity.class, variante)).nombreDelProducto("Prueba")
                    .cantidad(1).precioOriginalXUnidad(BigDecimal.TEN).build());
            compras.guardar(compra);
        })).isInstanceOf(ReglaNegocioException.class);
        assertThat(stock(variante)).isEqualTo(1);
    }

    @Test void desactivarClienteConservaEdicionYAtencionDeCompraPagada() {
        int id = crearCompra("pagado", variante);
        tx.executeWithoutResult(s -> em.find(UsuarioEntity.class, cliente).setEstaActivo(false));
        assertThatCode(() -> tx.executeWithoutResult(s -> compras.guardar(compras.obtener(id))))
                .doesNotThrowAnyException();
        compras.cambiarEstado(id, "enviado");
        assertThat(estado(id)).isEqualTo("enviado");
        assertThat(stock(variante)).isZero();
    }

    @Test void pendienteNoReservaYPagoDescuentaUnaSolaVez() {
        int id = crearCompra("pendiente", variante);
        assertThat(stock(variante)).isEqualTo(1);
        compras.cambiarEstado(id, "pagado");
        compras.cambiarEstado(id, "pagado");
        compras.cambiarEstado(id, "enviado");
        assertThat(stock(variante)).isZero();
        assertThat(estado(id)).isEqualTo("enviado");
    }

    @Test void guardarCompraPagadaTambienDescuentaYNoPermiteCambiarCantidad() {
        int id = crearCompra("pagado", variante);
        assertThat(stock(variante)).isZero();
        tx.executeWithoutResult(s -> compras.guardar(compras.obtener(id)));
        assertThat(stock(variante)).isZero();
        assertThatThrownBy(() -> tx.executeWithoutResult(s -> {
            var compra = compras.obtener(id);
            compra.getDetalles().get(0).setCantidad(2);
            compras.guardar(compra);
        })).isInstanceOf(ReglaNegocioException.class);
        assertThat(stock(variante)).isZero();
        int cantidad = tx.execute(s -> compras.obtener(id).getDetalles().get(0).getCantidad());
        assertThat(cantidad).isEqualTo(1);
    }

    @Test void faltaDeStockRevierteTodasLasLineasYElEstado() {
        int id = crearCompra("pendiente", variante, segunda);
        assertThatThrownBy(() -> compras.cambiarEstado(id, "pagado"))
                .isInstanceOf(ReglaNegocioException.class).hasMessageContaining("insuficientes");
        assertThat(stock(variante)).isEqualTo(1);
        assertThat(stock(segunda)).isZero();
        assertThat(estado(id)).isEqualTo("pendiente");
    }

    @Test void compraVaciaNoPuedeConfirmarse() {
        int id = crearCompra("pendiente");
        assertThatThrownBy(() -> compras.cambiarEstado(id, "pagado")).isInstanceOf(ReglaNegocioException.class);
        assertThat(estado(id)).isEqualTo("pendiente");
    }

    @Test void dosComprasConcurrentesNoVendenLaMismaUltimaUnidad() throws Exception {
        int a = crearCompra("pendiente", variante), b = crearCompra("pendiente", variante);
        CountDownLatch salida = new CountDownLatch(1);
        ExecutorService pool = Executors.newFixedThreadPool(2);
        try {
            List<Future<Boolean>> resultados = new ArrayList<>();
            for (int id : List.of(a, b)) resultados.add(pool.submit(() -> {
                salida.await();
                try { compras.cambiarEstado(id, "pagado"); return true; }
                catch (ReglaNegocioException e) { return false; }
            }));
            salida.countDown();
            int exitos = 0;
            for (var r : resultados) if (r.get(20, TimeUnit.SECONDS)) exitos++;
            assertThat(exitos).isEqualTo(1);
            assertThat(stock(variante)).isZero();
            assertThat(List.of(estado(a), estado(b))).containsExactlyInAnyOrder("pagado", "pendiente");
        } finally { pool.shutdownNow(); }
    }

    MockHttpSession sesion(int id) {
        Usuario usuario = usuarios.obtener(id);
        var session = new MockHttpSession();
        session.setAttribute("usuarioLogueado", usuario);
        session.setAttribute("idUsuario", id);
        session.setAttribute("rolUsuario", usuario.getRol().name());
        return session;
    }

    @Test void loginRedirigeSegunRol() throws Exception {
        mvc.perform(post("/login").param("correo", correoAdmin).param("clave", "Clave123!"))
                .andExpect(redirectedUrl("/seleccionar-modo"));
        mvc.perform(post("/login").param("correo", correoCliente).param("clave", "Clave123!"))
                .andExpect(redirectedUrl("/catalogo"));
    }

    @Test void administradorPuedeElegirAmbosModosSinCambiarSuRol() throws Exception {
        var session = sesion(admin);
        mvc.perform(get("/seleccionar-modo").session(session)).andExpect(status().isOk())
                .andExpect(content().string(containsString("Comprar como cliente")));
        mvc.perform(post("/seleccionar-modo").session(session).param("modo", "CLIENTE"))
                .andExpect(redirectedUrl("/catalogo"));
        assertThat(session.getAttribute("modoAcceso")).isEqualTo("CLIENTE");
        mvc.perform(post("/seleccionar-modo").session(session).param("modo", "ADMINISTRADOR"))
                .andExpect(redirectedUrl("/admin"));
        assertThat(session.getAttribute("modoAcceso")).isEqualTo("ADMINISTRADOR");
        assertThat(usuarios.obtener(admin).getRol()).isEqualTo(RolUsuario.ADMINISTRADOR);
    }

    @Test void comunNoPuedeElegirModoAdministradorYAnonimoNecesitaLogin() throws Exception {
        mvc.perform(get("/seleccionar-modo")).andExpect(redirectedUrl("/login"));
        mvc.perform(post("/seleccionar-modo").param("modo", "ADMINISTRADOR")).andExpect(redirectedUrl("/login"));
        mvc.perform(get("/seleccionar-modo").session(sesion(cliente))).andExpect(redirectedUrl("/catalogo"));
        mvc.perform(post("/seleccionar-modo").session(sesion(cliente)).param("modo", "ADMINISTRADOR"))
                .andExpect(status().isForbidden());
    }

    @Test void cuentaDesactivadaPierdeAccesoAlSelector() throws Exception {
        var session = sesion(admin);
        tx.executeWithoutResult(s -> em.find(UsuarioEntity.class, admin).setEstaActivo(false));
        mvc.perform(get("/seleccionar-modo").session(session)).andExpect(redirectedUrl("/login"));
        assertThat(session.isInvalid()).isTrue();
    }
}
