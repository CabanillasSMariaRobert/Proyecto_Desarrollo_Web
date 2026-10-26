package com.ecommerce.tienda_kalza.adapter;

import com.ecommerce.tienda_kalza.adapter.entity.*;
import com.ecommerce.tienda_kalza.adapter.model.Producto;
import com.ecommerce.tienda_kalza.adapter.repository.*;
import com.ecommerce.tienda_kalza.adapter.service.*;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {"spring.datasource.url=jdbc:h2:mem:rn04;DB_CLOSE_DELAY=-1",
        "spring.jpa.hibernate.ddl-auto=create-drop", "spring.jpa.open-in-view=false", "spring.jpa.show-sql=false"})
@AutoConfigureMockMvc
@Transactional
class Rn04IntegrationTest {
    @Autowired EntityManager em;
    @Autowired MarcaService marcas;
    @Autowired ProductoService productos;
    @Autowired MarcaRepository marcaRepository;
    @Autowired CompraService compras;
    @Autowired CarritoService carrito;
    @Autowired MockMvc mvc;
    MarcaEntity marca, otra;
    ProductoEntity producto;
    VarianteZapatoEntity variante;

    @BeforeEach void preparar() {
        marca = MarcaEntity.builder().nombre("Marca original RN04").build();
        otra = MarcaEntity.builder().nombre("Otra marca RN04").estaActivo(false).build();
        em.persist(marca);
        em.persist(otra);
        producto = ProductoEntity.builder().nombre("Calzado RN04").marca(marca).precioOriginal(BigDecimal.TEN).build();
        em.persist(producto);
        var zapato = new ZapatoEntity();
        zapato.setProducto(producto);
        em.persist(zapato);
        variante = VarianteZapatoEntity.builder().zapato(zapato).talla(40).color("NEGRO").stock(2).build();
        em.persist(variante);
        em.flush();
    }

    Producto nuevo(int idMarca) {
        var model = new Producto();
        model.setNombre("Nuevo RN04");
        model.setIdMarca(idMarca);
        model.setPrecioOriginal(BigDecimal.TEN);
        return model;
    }

    @Test void nuevasMarcasActivasPorDefectoYEstadoNoSeInyectaAlEditar() {
        assertThat(marca.isEstaActivo()).isTrue();
        marcas.cambiarEstado(marca.getIdMarca(), false);
        var model = marcas.obtener(marca.getIdMarca());
        model.setEstaActivo(true);
        model.setNombre("Nombre editado RN04");
        marcas.guardar(marca.getIdMarca(), model);
        em.clear();
        assertThat(marcas.obtener(marca.getIdMarca()).isEstaActivo()).isFalse();
    }

    @Test void desactivacionDesdePantallaConservaRegistrosYPermiteReactivar() throws Exception {
        for (boolean activo : new boolean[]{false, false, true}) {
            mvc.perform(post("/admin/marcas/{id}/estado", marca.getIdMarca()).param("activo", String.valueOf(activo)))
                    .andExpect(redirectedUrl("/admin/marcas")).andExpect(flash().attributeExists("mensaje"));
            em.clear();
            assertThat(marcas.obtener(marca.getIdMarca()).isEstaActivo()).isEqualTo(activo);
            assertThat(productos.obtener(producto.getIdProducto()).getIdMarca()).isEqualTo(marca.getIdMarca());
        }
        mvc.perform(get("/admin/marcas")).andExpect(status().isOk())
                .andExpect(content().string(containsString("Desactivar")))
                .andExpect(content().string(containsString("Inactiva")));
    }

    @Test void marcaInexistenteDevuelveErrorControlado() throws Exception {
        mvc.perform(post("/admin/marcas/{id}/estado", Integer.MAX_VALUE).param("activo", "false"))
                .andExpect(redirectedUrl("/admin/marcas")).andExpect(flash().attributeExists("error"));
    }

    @Test void noAdmiteMarcaInactivaEnProductoNuevoNiComoCambioDeMarca() {
        assertThatThrownBy(() -> productos.guardar(null, nuevo(otra.getIdMarca())))
                .isInstanceOf(IllegalArgumentException.class).hasMessageContaining("inactiva");
        var model = productos.obtener(producto.getIdProducto());
        model.setIdMarca(otra.getIdMarca());
        assertThatThrownBy(() -> productos.guardar(producto.getIdProducto(), model))
                .isInstanceOf(IllegalArgumentException.class).hasMessageContaining("inactiva");
    }

    @Test void formularioSoloOfreceActivasYLaInactivaActualAlEditar() throws Exception {
        marcas.cambiarEstado(marca.getIdMarca(), false);
        String nuevo = mvc.perform(get("/admin/productos/nuevo")).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertThat(selectorMarcas(nuevo)).doesNotContain("Marca original RN04", "Otra marca RN04");
        String editar = mvc.perform(get("/admin/productos/{id}/editar", producto.getIdProducto()))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertThat(selectorMarcas(editar)).contains("Marca original RN04 (inactiva; asociación existente)")
                .doesNotContain("Otra marca RN04");
    }

    String selectorMarcas(String html) {
        var selector = java.util.regex.Pattern.compile("<select[^>]*id=\"idMarca\"[^>]*>([\\s\\S]*?)</select>").matcher(html);
        assertThat(selector.find()).isTrue();
        return selector.group(1);
    }

    @Test void permiteConservarMarcaInactivaPeroNoReasignarlaTrasCambiar() {
        marcas.cambiarEstado(marca.getIdMarca(), false);
        var model = productos.obtener(producto.getIdProducto());
        model.setNombre("Edición conserva marca RN04");
        productos.guardar(producto.getIdProducto(), model);
        assertThat(productos.obtener(producto.getIdProducto()).getIdMarca()).isEqualTo(marca.getIdMarca());
        marcas.cambiarEstado(otra.getIdMarca(), true);
        model.setIdMarca(otra.getIdMarca());
        productos.guardar(producto.getIdProducto(), model);
        model.setIdMarca(marca.getIdMarca());
        assertThatThrownBy(() -> productos.guardar(producto.getIdProducto(), model))
                .isInstanceOf(IllegalArgumentException.class);
        marcas.cambiarEstado(marca.getIdMarca(), true);
        productos.guardar(producto.getIdProducto(), model);
    }

    @Test void productoConMarcaInactivaSigueEnCatalogoYSePuedeComprar() throws Exception {
        marcas.cambiarEstado(marca.getIdMarca(), false);
        mvc.perform(get("/catalogo/{id}", producto.getIdProducto())).andExpect(status().isOk())
                .andExpect(content().string(containsString("Calzado RN04")));
        var session = new MockHttpSession();
        carrito.agregar(session, variante.getIdVariante(), 1);
        assertThat(carrito.consultar(session).totalItems()).isEqualTo(1);
        var cliente = UsuarioEntity.builder().nombres("Cliente").apellidos("RN04")
                .correo("rn04@test.com").clave("prueba").build();
        em.persist(cliente);
        var compra = CompraEntity.builder().cliente(cliente).estado("pagado")
                .totalSinDescuento(BigDecimal.TEN).totalConDescuento(BigDecimal.TEN).build();
        compra.getDetalles().add(DetalleCompraEntity.builder().compra(compra).varianteZapato(variante)
                .nombreDelProducto("Calzado RN04").cantidad(1).precioOriginalXUnidad(BigDecimal.TEN).build());
        compras.guardar(compra);
        em.flush();
        em.refresh(variante);
        assertThat(variante.getStock()).isEqualTo(1);
        assertThat(compra.getEstado()).isEqualTo("pagado");
    }
}
