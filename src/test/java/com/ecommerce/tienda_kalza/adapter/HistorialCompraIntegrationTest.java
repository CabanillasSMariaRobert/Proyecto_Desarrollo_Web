package com.ecommerce.tienda_kalza.adapter;

import com.ecommerce.tienda_kalza.adapter.entity.*;
import com.ecommerce.tienda_kalza.adapter.model.Usuario;
import com.ecommerce.tienda_kalza.adapter.service.UsuarioService;
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
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {"spring.datasource.url=jdbc:h2:mem:historial;DB_CLOSE_DELAY=-1",
        "spring.jpa.hibernate.ddl-auto=create-drop", "spring.jpa.open-in-view=false", "spring.jpa.show-sql=false"})
@AutoConfigureMockMvc
class HistorialCompraIntegrationTest {
    @Autowired EntityManager em;
    @Autowired PlatformTransactionManager manager;
    @Autowired MockMvc mvc;
    @Autowired UsuarioService usuarios;
    int clienteA, clienteB;

    @BeforeEach void preparar() {
        new TransactionTemplate(manager).executeWithoutResult(s -> {
            var marca = MarcaEntity.builder().nombre("Historial " + UUID.randomUUID()).build();
            em.persist(marca);
            var producto = ProductoEntity.builder().marca(marca).nombre("Nombre actual del catálogo")
                    .precioOriginal(BigDecimal.TEN).build();
            em.persist(producto);
            var zapato = new ZapatoEntity();
            zapato.setProducto(producto);
            em.persist(zapato);
            var variante = VarianteZapatoEntity.builder().zapato(zapato).talla(40).color("NEGRO").stock(10).build();
            em.persist(variante);
            var a = cliente("Cliente A");
            var b = cliente("Cliente B");
            clienteA = a.getIdUsuario();
            clienteB = b.getIdUsuario();
            for (int i = 0; i < 7; i++) compra(a, variante, "Compra privada A " + i);
            var conEntrega = compra(b, variante, "Compra privada B");
            var punto = new PuntoEntregaEntity();
            punto.setNombre("Punto de prueba");
            punto.setDireccion("Dirección de prueba");
            punto.setReferencia("Referencia");
            em.persist(punto);
            var entrega = new EntregaEntity();
            entrega.setCompra(conEntrega);
            entrega.setPuntoDeEntrega(punto);
            em.persist(entrega);
        });
    }

    UsuarioEntity cliente(String nombre) {
        var cliente = UsuarioEntity.builder().nombres(nombre).apellidos("Prueba")
                .correo(UUID.randomUUID() + "@test.com").clave("prueba").build();
        em.persist(cliente);
        return cliente;
    }

    CompraEntity compra(UsuarioEntity cliente, VarianteZapatoEntity variante, String nombreHistorico) {
        var compra = CompraEntity.builder().cliente(cliente).totalSinDescuento(BigDecimal.TEN)
                .totalConDescuento(BigDecimal.TEN).build();
        compra.getDetalles().add(DetalleCompraEntity.builder().compra(compra).varianteZapato(variante)
                .nombreDelProducto(nombreHistorico).cantidad(1).precioOriginalXUnidad(BigDecimal.TEN).build());
        em.persist(compra);
        return compra;
    }

    MockHttpSession sesion(int id) {
        Usuario usuario = usuarios.obtener(id);
        var session = new MockHttpSession();
        session.setAttribute("idUsuario", id);
        session.setAttribute("usuarioLogueado", usuario);
        session.setAttribute("rolUsuario", usuario.getRol().name());
        return session;
    }

    @Test void anonimoNoPuedeConsultarHistorial() throws Exception {
        mvc.perform(get("/pedidos")).andExpect(redirectedUrl("/login"));
    }

    @Test void consultaSoloComprasPropiasIncluidasLasQueNoTienenEntrega() throws Exception {
        mvc.perform(get("/pedidos").session(sesion(clienteA)).param("idUsuario", String.valueOf(clienteB)))
                .andExpect(status().isOk()).andExpect(model().attribute("totalPedidos", 7L))
                .andExpect(model().attribute("pedidos", hasSize(5)))
                .andExpect(content().string(containsString("Compra privada A")))
                .andExpect(content().string(not(containsString("Compra privada B"))))
                .andExpect(content().string(containsString("Entrega aún no asignada")))
                .andExpect(content().string(not(containsString("Nombre actual del catálogo"))));
    }

    @Test void compraConEntregaConservaDetalleYPuntoDeEntrega() throws Exception {
        mvc.perform(get("/pedidos").session(sesion(clienteB)))
                .andExpect(status().isOk()).andExpect(model().attribute("totalPedidos", 1L))
                .andExpect(content().string(containsString("Compra privada B")))
                .andExpect(content().string(containsString("Punto de prueba")))
                .andExpect(content().string(not(containsString("Compra privada A"))));
    }

    @Test void paginacionYUsuarioSinCompras() throws Exception {
        mvc.perform(get("/pedidos").session(sesion(clienteA)).param("pagina", "1"))
                .andExpect(status().isOk()).andExpect(model().attribute("pedidos", hasSize(2)))
                .andExpect(model().attribute("desde", 6L)).andExpect(model().attribute("hasta", 7L));
        mvc.perform(get("/pedidos").session(sesion(clienteA)).param("pagina", "-1"))
                .andExpect(status().isOk()).andExpect(model().attribute("paginaActual", 0));
        int sinCompras = new TransactionTemplate(manager).execute(s -> cliente("Sin compras").getIdUsuario());
        mvc.perform(get("/pedidos").session(sesion(sinCompras)))
                .andExpect(status().isOk()).andExpect(model().attribute("totalPedidos", 0L))
                .andExpect(model().attribute("pedidos", empty()));
    }
}
