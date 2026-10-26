package com.ecommerce.tienda_kalza.adapter;

import com.ecommerce.tienda_kalza.adapter.entity.*;
import com.ecommerce.tienda_kalza.adapter.enums.RedUsada;
import com.ecommerce.tienda_kalza.adapter.enums.EstadoEntrega;
import com.ecommerce.tienda_kalza.adapter.enums.RolUsuario;
import com.ecommerce.tienda_kalza.adapter.service.*;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import java.math.BigDecimal;
import java.util.UUID;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {"spring.datasource.url=jdbc:h2:mem:checkout;DB_CLOSE_DELAY=-1",
        "spring.jpa.hibernate.ddl-auto=create-drop", "spring.jpa.open-in-view=false", "spring.jpa.show-sql=false"})
@AutoConfigureMockMvc
class CheckoutIntegrationTest {
    @Autowired EntityManager em;
    @Autowired PlatformTransactionManager manager;
    @Autowired MockMvc mvc;
    @Autowired CarritoService carrito;
    @Autowired UsuarioService usuarios;
    @Autowired CheckoutService checkout;
    @Autowired EntregasService seguimiento;
    TransactionTemplate tx;
    int cliente, otroCliente, variante, punto, metodo, metodoAjeno;
    long comprasAntes, boletasAntes, entregasAntes;

    @BeforeEach void preparar() {
        tx = new TransactionTemplate(manager);
        tx.executeWithoutResult(s -> {
            var usuario = crearCliente();
            var otro = crearCliente();
            cliente = usuario.getIdUsuario();
            otroCliente = otro.getIdUsuario();
            metodo = crearMetodo(usuario, "1234");
            metodoAjeno = crearMetodo(otro, "9876");
            var marca = MarcaEntity.builder().nombre("Checkout " + UUID.randomUUID()).build();
            em.persist(marca);
            var producto = ProductoEntity.builder().nombre("Calzado checkout").marca(marca)
                    .precioOriginal(new BigDecimal("100.00")).descuento(new BigDecimal("0.20")).build();
            em.persist(producto);
            var zapato = new ZapatoEntity();
            zapato.setProducto(producto);
            em.persist(zapato);
            var v = VarianteZapatoEntity.builder().zapato(zapato).talla(40).color("NEGRO").stock(3).build();
            em.persist(v);
            variante = v.getIdVariante();
            var p = new PuntoEntregaEntity();
            p.setNombre("Recojo checkout");
            p.setDireccion("Dirección de prueba");
            p.setReferencia("Referencia");
            p.setEstaActivo(true);
            em.persist(p);
            punto = p.getId();
        });
        comprasAntes = contar("CompraEntity");
        boletasAntes = contar("BoletaEntity");
        entregasAntes = contar("EntregaEntity");
    }

    UsuarioEntity crearCliente() {
        var u = UsuarioEntity.builder().nombres("Cliente").apellidos("Checkout")
                .correo(UUID.randomUUID() + "@test.com").clave("prueba").build();
        em.persist(u);
        return u;
    }

    int crearMetodo(UsuarioEntity usuario, String ultimos) {
        var m = MetodoPagoEntity.builder().usuario(usuario).pasarela("Simulada").redUsada(RedUsada.VISA)
                .token(UUID.randomUUID().toString()).ultimos4Digitos(ultimos).expira_el_mes(12).expira_el_anio(2099).build();
        em.persist(m);
        return m.getIdMetodoDePago();
    }

    MockHttpSession sesion(int id) {
        var u = usuarios.obtener(id);
        var session = new MockHttpSession();
        session.setAttribute("idUsuario", id);
        session.setAttribute("usuarioLogueado", u);
        session.setAttribute("rolUsuario", u.getRol().name());
        return session;
    }

    long contar(String entity) { return tx.execute(s -> em.createQuery("select count(e) from " + entity + " e", Long.class).getSingleResult()); }
    int stock() { return tx.execute(s -> em.find(VarianteZapatoEntity.class, variante).getStock()); }

    int entregaPagada() {
        var session = sesion(cliente);
        carrito.agregar(session, variante, 1);
        int idCompra = checkout.confirmar(cliente, carrito.consultar(session).items(), metodo, punto, true);
        return tx.execute(s -> em.find(CompraEntity.class, idCompra).getEntrega().getId());
    }

    int entregaSinPago(String estadoCompra) {
        return tx.execute(s -> {
            var compra = CompraEntity.builder().cliente(em.find(UsuarioEntity.class, cliente)).estado(estadoCompra)
                    .totalSinDescuento(BigDecimal.TEN).totalConDescuento(BigDecimal.TEN).build();
            em.persist(compra);
            var entrega = new EntregaEntity();
            entrega.setCompra(compra);
            entrega.setPuntoDeEntrega(em.find(PuntoEntregaEntity.class, punto));
            em.persist(entrega);
            return entrega.getId();
        });
    }

    MockHttpSession sesionAdmin() {
        int id = tx.execute(s -> {
            var admin = crearCliente();
            admin.setRol(RolUsuario.ADMINISTRADOR);
            return admin.getIdUsuario();
        });
        return sesion(id);
    }

    @Test void seguimientoCompletoRegistraFechasYSeMuestraEnAmbasVistas() throws Exception {
        int id = entregaPagada();
        assertThatThrownBy(() -> seguimiento.cambiarEstado(id, EstadoEntrega.ENTREGADO))
                .isInstanceOf(ReglaNegocioException.class);
        for (var estado : new EstadoEntrega[]{EstadoEntrega.ASIGNADO, EstadoEntrega.RECOGIDO,
                EstadoEntrega.EN_CAMINO, EstadoEntrega.ENTREGADO}) seguimiento.cambiarEstado(id, estado);
        var fecha = tx.execute(s -> {
            var e = em.find(EntregaEntity.class, id);
            assertThat(e.getAsignadoEl()).isNotNull();
            assertThat(e.getRecogidoEl()).isAfterOrEqualTo(e.getAsignadoEl());
            assertThat(e.getEntregadoEl()).isAfterOrEqualTo(e.getRecogidoEl());
            assertThat(e.getCompra().getEstado()).isEqualTo("entregado");
            return e.getEntregadoEl();
        });
        seguimiento.cambiarEstado(id, EstadoEntrega.ENTREGADO);
        tx.executeWithoutResult(s -> assertThat(em.find(EntregaEntity.class, id).getEntregadoEl()).isEqualTo(fecha));
        mvc.perform(get("/pedidos").session(sesion(cliente))).andExpect(status().isOk())
                .andExpect(content().string(containsString("ENTREGADO")));
        mvc.perform(get("/admin/pedidos").session(sesionAdmin())).andExpect(status().isOk())
                .andExpect(content().string(containsString("ENTREGADO")));
        assertThat(stock()).isEqualTo(2);
    }

    @Test void seguimientoRechazaComprasCanceladasOSinPagoSinCambiarFechas() {
        for (String estado : new String[]{"pendiente", "cancelado"}) {
            int id = entregaSinPago(estado);
            assertThatThrownBy(() -> seguimiento.cambiarEstado(id, EstadoEntrega.ASIGNADO))
                    .isInstanceOf(ReglaNegocioException.class);
            tx.executeWithoutResult(s -> {
                var e = em.find(EntregaEntity.class, id);
                assertThat(e.getEstado()).isEqualTo(EstadoEntrega.PENDIENTE);
                assertThat(e.getAsignadoEl()).isNull();
                assertThat(e.getCompra().getEstado()).isEqualTo(estado);
            });
        }
        assertThat(stock()).isEqualTo(3);
    }

    @Test void repartoFalladoConservaCompraYNoReponeStock() {
        int id = entregaPagada();
        for (var estado : new EstadoEntrega[]{EstadoEntrega.ASIGNADO, EstadoEntrega.RECOGIDO,
                EstadoEntrega.EN_CAMINO, EstadoEntrega.FALLADO}) seguimiento.cambiarEstado(id, estado);
        tx.executeWithoutResult(s -> {
            var e = em.find(EntregaEntity.class, id);
            assertThat(e.getEstado()).isEqualTo(EstadoEntrega.FALLADO);
            assertThat(e.getCompra().getEstado()).isEqualTo("enviado");
            assertThat(e.getEntregadoEl()).isNull();
        });
        assertThatThrownBy(() -> seguimiento.cambiarEstado(id, EstadoEntrega.ENTREGADO))
                .isInstanceOf(ReglaNegocioException.class);
        assertThat(stock()).isEqualTo(2);
    }

    @Test void cancelaSoloEntregasDeComprasPendientesYExigeAdministrador() throws Exception {
        int pendiente = entregaSinPago("pendiente");
        seguimiento.cambiarEstado(pendiente, EstadoEntrega.CANCELADO);
        tx.executeWithoutResult(s -> {
            var e = em.find(EntregaEntity.class, pendiente);
            assertThat(e.getCompra().getEstado()).isEqualTo("cancelado");
            assertThat(e.getEstado()).isEqualTo(EstadoEntrega.CANCELADO);
        });
        int pagada = entregaPagada();
        mvc.perform(post("/admin/pedidos/{id}/estado", pagada).param("nuevoEstado", "ASIGNADO"))
                .andExpect(redirectedUrl("/login"));
        mvc.perform(post("/admin/pedidos/{id}/estado", pagada).session(sesion(cliente)).param("nuevoEstado", "ASIGNADO"))
                .andExpect(redirectedUrl("/"));
        mvc.perform(post("/admin/pedidos/{id}/estado", pagada).session(sesionAdmin()).param("nuevoEstado", "CANCELADO"))
                .andExpect(status().is3xxRedirection()).andExpect(flash().attributeExists("error"));
        tx.executeWithoutResult(s -> assertThat(em.find(EntregaEntity.class, pagada).getCompra().getEstado()).isEqualTo("pagado"));
    }
    void sinCambios() {
        assertThat(contar("CompraEntity")).isEqualTo(comprasAntes);
        assertThat(contar("BoletaEntity")).isEqualTo(boletasAntes);
        assertThat(contar("EntregaEntity")).isEqualTo(entregasAntes);
        assertThat(stock()).isEqualTo(3);
    }

    @Test void anonimoNecesitaSesionYNoVeTarjetasAjenas() throws Exception {
        mvc.perform(get("/checkout")).andExpect(redirectedUrl("/login"));
        mvc.perform(post("/checkout/confirmar").param("idMetodoPago", "1").param("idPuntoEntrega", "1")
                .param("pagoAprobado", "true")).andExpect(redirectedUrl("/login"));
        mvc.perform(get("/carrito")).andExpect(status().isOk())
                .andExpect(content().string(not(containsString("9876"))));
        mvc.perform(post("/carrito/metodo-pago/guardar")).andExpect(redirectedUrl("/login"));
    }

    @Test void pagoAprobadoGeneraTodoYNoSeDuplicaAlReenviar() throws Exception {
        var session = sesion(cliente);
        carrito.agregar(session, variante, 2);
        mvc.perform(get("/checkout").session(session)).andExpect(status().isOk())
                .andExpect(content().string(containsString("Recojo checkout")))
                .andExpect(content().string(containsString("1234")))
                .andExpect(content().string(not(containsString("9876"))));
        String destino = mvc.perform(post("/checkout/confirmar").session(session)
                .param("idMetodoPago", String.valueOf(metodo)).param("idPuntoEntrega", String.valueOf(punto))
                .param("pagoAprobado", "true").param("total", "0.01").param("idUsuario", String.valueOf(otroCliente)))
                .andExpect(status().is3xxRedirection()).andReturn().getResponse().getRedirectedUrl();
        assertThat(destino).matches("/compras/\\d+/boleta");
        assertThat(stock()).isEqualTo(1);
        assertThat(contar("CompraEntity")).isEqualTo(comprasAntes + 1);
        assertThat(contar("BoletaEntity")).isEqualTo(boletasAntes + 1);
        assertThat(contar("EntregaEntity")).isEqualTo(entregasAntes + 1);
        assertThat(carrito.consultar(session).items()).isEmpty();
        tx.executeWithoutResult(s -> {
            var compra = em.createQuery("select c from CompraEntity c where c.cliente.idUsuario = :id", CompraEntity.class)
                    .setParameter("id", cliente).getSingleResult();
            assertThat(compra.getEstado()).isEqualTo("pagado");
            assertThat(compra.getTotalConDescuento()).isEqualByComparingTo("160.00");
            assertThat(compra.getTotalSinDescuento()).isEqualByComparingTo("200.00");
            assertThat(compra.getDetalles().get(0).getDescuentoXUnidad()).isEqualByComparingTo("20.00");
            assertThat(compra.getEntrega().getPuntoDeEntrega().getId()).isEqualTo(punto);
        });
        mvc.perform(get(destino).session(session)).andExpect(status().isOk())
                .andExpect(content().string(containsString("160.00")));
        mvc.perform(get(destino).session(sesion(otroCliente))).andExpect(redirectedUrl("/pedidos"));
        mvc.perform(get("/pedidos").session(session)).andExpect(status().isOk())
                .andExpect(content().string(containsString("Ver boleta")));
        mvc.perform(post("/checkout/confirmar").session(session).param("idMetodoPago", String.valueOf(metodo))
                .param("idPuntoEntrega", String.valueOf(punto)).param("pagoAprobado", "true"))
                .andExpect(redirectedUrl("/checkout")).andExpect(flash().attributeExists("error"));
        assertThat(contar("CompraEntity")).isEqualTo(comprasAntes + 1);
    }

    @Test void pagoRechazadoConservaCarritoSinRegistrarVenta() throws Exception {
        var session = sesion(cliente);
        carrito.agregar(session, variante, 1);
        mvc.perform(post("/checkout/confirmar").session(session).param("idMetodoPago", String.valueOf(metodo))
                .param("idPuntoEntrega", String.valueOf(punto)).param("pagoAprobado", "false"))
                .andExpect(redirectedUrl("/checkout")).andExpect(flash().attribute("error", containsString("rechazado")));
        sinCambios();
        assertThat(carrito.consultar(session).totalItems()).isEqualTo(1);
    }

    @Test void rechazaTarjetaAjenaYPuntoInactivo() throws Exception {
        var session = sesion(cliente);
        carrito.agregar(session, variante, 1);
        mvc.perform(post("/checkout/confirmar").session(session).param("idMetodoPago", String.valueOf(metodoAjeno))
                .param("idPuntoEntrega", String.valueOf(punto)).param("pagoAprobado", "true"))
                .andExpect(redirectedUrl("/checkout")).andExpect(flash().attributeExists("error"));
        tx.executeWithoutResult(s -> em.find(PuntoEntregaEntity.class, punto).setEstaActivo(false));
        mvc.perform(post("/checkout/confirmar").session(session).param("idMetodoPago", String.valueOf(metodo))
                .param("idPuntoEntrega", String.valueOf(punto)).param("pagoAprobado", "true"))
                .andExpect(redirectedUrl("/checkout")).andExpect(flash().attributeExists("error"));
        sinCambios();
    }

    @Test void clienteDesactivadoOTarjetaVencidaNoCompra() throws Exception {
        var session = sesion(cliente);
        carrito.agregar(session, variante, 1);
        tx.executeWithoutResult(s -> em.find(UsuarioEntity.class, cliente).setEstaActivo(false));
        mvc.perform(post("/checkout/confirmar").session(session).param("idMetodoPago", String.valueOf(metodo))
                .param("idPuntoEntrega", String.valueOf(punto)).param("pagoAprobado", "true"))
                .andExpect(redirectedUrl("/checkout")).andExpect(flash().attributeExists("error"));
        tx.executeWithoutResult(s -> {
            em.find(UsuarioEntity.class, cliente).setEstaActivo(true);
            em.find(MetodoPagoEntity.class, metodo).setExpira_el_anio(2000);
        });
        mvc.perform(post("/checkout/confirmar").session(session).param("idMetodoPago", String.valueOf(metodo))
                .param("idPuntoEntrega", String.valueOf(punto)).param("pagoAprobado", "true"))
                .andExpect(redirectedUrl("/checkout")).andExpect(flash().attributeExists("error"));
        sinCambios();
    }

    @Test void carritoVacioOSinDisponibilidadNoGeneraCompra() throws Exception {
        var session = sesion(cliente);
        mvc.perform(post("/checkout/confirmar").session(session).param("idMetodoPago", String.valueOf(metodo))
                .param("idPuntoEntrega", String.valueOf(punto)).param("pagoAprobado", "true"))
                .andExpect(redirectedUrl("/checkout")).andExpect(flash().attributeExists("error"));
        sinCambios();
        carrito.agregar(session, variante, 1);
        tx.executeWithoutResult(s -> em.find(VarianteZapatoEntity.class, variante).setStock(0));
        mvc.perform(post("/checkout/confirmar").session(session).param("idMetodoPago", String.valueOf(metodo))
                .param("idPuntoEntrega", String.valueOf(punto)).param("pagoAprobado", "true"))
                .andExpect(redirectedUrl("/checkout")).andExpect(flash().attributeExists("error"));
        assertThat(stock()).isZero();
        assertThat(contar("CompraEntity")).isEqualTo(comprasAntes);
        assertThat(carrito.consultar(session).totalItems()).isEqualTo(1);
        tx.executeWithoutResult(s -> {
            var v = em.find(VarianteZapatoEntity.class, variante);
            v.setStock(3);
            v.getZapato().getProducto().setEstaActivo(false);
        });
        mvc.perform(post("/checkout/confirmar").session(session).param("idMetodoPago", String.valueOf(metodo))
                .param("idPuntoEntrega", String.valueOf(punto)).param("pagoAprobado", "true"))
                .andExpect(redirectedUrl("/checkout")).andExpect(flash().attributeExists("error"));
        sinCambios();
    }

    @Test void errorAlEmitirBoletaRevierteCompraStockYEntrega() throws Exception {
        var session = sesion(cliente);
        carrito.agregar(session, variante, 1);
        tx.executeWithoutResult(s -> {
            var u = em.find(UsuarioEntity.class, cliente);
            u.setNombres("A".repeat(100));
            u.setApellidos("B".repeat(100));
        });
        mvc.perform(post("/checkout/confirmar").session(session).param("idMetodoPago", String.valueOf(metodo))
                .param("idPuntoEntrega", String.valueOf(punto)).param("pagoAprobado", "true"))
                .andExpect(redirectedUrl("/checkout")).andExpect(flash().attributeExists("error"));
        sinCambios();
        assertThat(carrito.consultar(session).totalItems()).isEqualTo(1);
    }
}
