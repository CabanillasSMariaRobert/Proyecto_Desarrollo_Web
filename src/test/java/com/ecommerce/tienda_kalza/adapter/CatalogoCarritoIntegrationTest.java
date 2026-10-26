package com.ecommerce.tienda_kalza.adapter;

import com.ecommerce.tienda_kalza.adapter.entity.*;
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
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {"spring.datasource.url=jdbc:h2:mem:catalogo_carrito;DB_CLOSE_DELAY=-1",
        "spring.jpa.hibernate.ddl-auto=create-drop", "spring.jpa.open-in-view=false", "spring.jpa.show-sql=false"})
@AutoConfigureMockMvc @Transactional
class CatalogoCarritoIntegrationTest {
    @Autowired MockMvc mvc;
    @Autowired ProductoService productos;
    @Autowired CarritoService carrito;
    @Autowired ProductoRepository productoRepository;
    @Autowired MarcaRepository marcas;
    @Autowired CategoriaRepository categorias;
    @Autowired ZapatoRepository zapatos;
    @Autowired VarianteZapatoRepository variantes;
    @Autowired EntityManager em;
    int idProducto, idMarca, idCategoria;

    @BeforeEach void registrarDesdeAdministracion() throws Exception {
        idMarca = marcas.saveAndFlush(MarcaEntity.builder().nombre("Marca catálogo").build()).getIdMarca();
        idCategoria = categorias.saveAndFlush(CategoriaEntity.builder().nombre("Categoría catálogo").build()).getIdCategoria();
        mvc.perform(post("/admin/productos/crear").param("nombre", "Producto recién registrado")
                .param("idMarca", "" + idMarca).param("idsCategorias", "" + idCategoria)
                .param("precioOriginal", "120.00").param("descuento", "0.20"))
                .andExpect(status().is3xxRedirection()).andExpect(flash().attributeExists("mensaje"));
        idProducto = productoRepository.findAll().get(0).getIdProducto();
        em.clear();
    }

    int completarCalzado(int producto) throws Exception {
        mvc.perform(get("/admin/zapatos")).andExpect(status().isOk());
        mvc.perform(post("/admin/zapatos/crear").param("idProducto", "" + producto))
                .andExpect(status().is3xxRedirection()).andExpect(flash().attributeExists("mensaje"));
        em.clear();
        return zapatos.findByProducto_IdProducto(producto).orElseThrow().getIdZapato();
    }

    int completarVariante(int calzado, int stock) throws Exception {
        mvc.perform(post("/admin/zapatos/{id}/variantes/crear", calzado).param("idZapato", "" + calzado)
                .param("talla", "39").param("color", "AZUL").param("stock", "" + stock))
                .andExpect(status().is3xxRedirection()).andExpect(flash().attributeExists("mensaje"));
        em.clear();
        return variantes.findAll().stream().filter(v -> v.getZapato().getIdZapato() == calzado)
                .findFirst().orElseThrow().getIdVariante();
    }

    void agregar(MockHttpSession session, int variante, boolean exito) throws Exception {
        mvc.perform(post("/carrito/agregar").session(session).param("idVariante", "" + variante).param("cantidad", "1"))
                .andExpect(status().is3xxRedirection()).andExpect(flash().attributeExists(exito ? "mensaje" : "error"));
    }

    @Test void altaConservaDatosRealesSinInventarInventario() {
        var p = productos.obtener(idProducto);
        assertThat(p.isEstaActivo()).isTrue();
        assertThat(p.getIdMarca()).isEqualTo(idMarca);
        assertThat(p.getIdsCategorias()).containsExactly(idCategoria);
        assertThat(p.getPrecioOriginal()).isEqualByComparingTo("120.00");
        assertThat(p.getDescuento()).isEqualByComparingTo("0.20");
        assertThat(p.getPrecioConDescuento()).isEqualByComparingTo("96.00");
        assertThat(p.getIdZapato()).isNull();
        assertThat(p.isDisponibleParaCarrito()).isFalse();
        assertThat(zapatos.count()).isZero();
        assertThat(variantes.count()).isZero();
        assertThat(productoRepository.count()).isEqualTo(1);
    }

    @Test void sinCalzadoSeIndicaComoCompletarloYNoSeVende() throws Exception {
        mvc.perform(get("/admin/productos")).andExpect(status().isOk())
                .andExpect(content().string(containsString("Falta registrar el calzado")))
                .andExpect(content().string(containsString("href=\"/admin/zapatos\"")));
        mvc.perform(get("/catalogo")).andExpect(status().isOk())
                .andExpect(content().string(not(containsString("Producto recién registrado"))));
        mvc.perform(get("/catalogo/{id}", idProducto)).andExpect(status().isNotFound());
        MockHttpSession session = new MockHttpSession();
        agregar(session, Integer.MAX_VALUE, false);
        assertThat(carrito.consultar(session).items()).isEmpty();
    }

    @Test void calzadoSinVariantesNoSimulaDisponibilidad() throws Exception {
        int calzado = completarCalzado(idProducto);
        var p = productos.obtener(idProducto);
        assertThat(p.getIdZapato()).isEqualTo(calzado);
        assertThat(p.isDisponibleParaCarrito()).isFalse();
        assertThat(p.getPreparacionVenta()).isEqualTo("Sin variantes activas con stock");
        mvc.perform(get("/catalogo/{id}", idProducto)).andExpect(status().isOk())
                .andExpect(content().string(containsString("No hay variantes activas")));
        MockHttpSession session = new MockHttpSession();
        agregar(session, Integer.MAX_VALUE, false);
        assertThat(carrito.consultar(session).items()).isEmpty();
    }

    @Test void flujoAdministracionCatalogoDetalleCarritoUtilizaMismaVarianteYSinDescontarStock() throws Exception {
        int calzado = completarCalzado(idProducto);
        int variante = completarVariante(calzado, 4);
        var p = productos.obtener(idProducto);
        assertThat(p.isDisponibleParaCarrito()).isTrue();
        mvc.perform(get("/admin/productos")).andExpect(content().string(containsString("Listo para agregar al carrito")))
                .andExpect(content().string(containsString("href=\"/catalogo/" + idProducto + "\"")));
        mvc.perform(get("/catalogo")).andExpect(status().isOk())
                .andExpect(content().string(containsString("Producto recién registrado")));
        mvc.perform(get("/catalogo/{id}", idProducto)).andExpect(status().isOk())
                .andExpect(content().string(containsString("AZUL")))
                .andExpect(content().string(containsString("/carrito/agregar")));
        MockHttpSession session = new MockHttpSession();
        agregar(session, variante, true);
        mvc.perform(get("/carrito").session(session)).andExpect(status().isOk())
                .andExpect(content().string(containsString("Producto recién registrado")))
                .andExpect(content().string(containsString("AZUL")));
        var linea = carrito.consultar(session).items().get(0);
        assertThat(linea.idProducto()).isEqualTo(idProducto);
        assertThat(linea.idVariante()).isEqualTo(variante);
        assertThat(linea.talla()).isEqualTo(39);
        assertThat(linea.cantidad()).isEqualTo(1);
        assertThat(linea.precioUnitario()).isEqualByComparingTo("96.00");
        em.clear();
        assertThat(variantes.findById(variante).orElseThrow().getStock()).isEqualTo(4);
        assertThat(productoRepository.count()).isEqualTo(1);
    }

    @Test void rn01ImpideNuevasAdicionesYConservaArticuloPrevio() throws Exception {
        int variante = completarVariante(completarCalzado(idProducto), 4);
        MockHttpSession session = new MockHttpSession();
        agregar(session, variante, true);
        productos.cambiarEstado(idProducto, false);
        em.clear();
        mvc.perform(get("/catalogo")).andExpect(content().string(not(containsString("Producto recién registrado"))));
        mvc.perform(get("/catalogo/{id}", idProducto)).andExpect(status().isNotFound());
        mvc.perform(get("/producto/{id}", idProducto)).andExpect(status().isNotFound());
        agregar(session, variante, false);
        assertThat(carrito.consultar(session).items()).hasSize(1);
        assertThat(carrito.consultar(session).disponible()).isFalse();
        assertThat(carrito.consultar(session).items().get(0).cantidad()).isEqualTo(1);
        assertThat(variantes.findById(variante).orElseThrow().getStock()).isEqualTo(4);
        assertThat(productos.obtener(idProducto).getPreparacionVenta()).isEqualTo("Producto inactivo");
    }

    @Test void variantesSinStockOInactivasNoSePuedenAgregar() throws Exception {
        int calzado = completarCalzado(idProducto);
        int variante = completarVariante(calzado, 0);
        MockHttpSession session = new MockHttpSession();
        agregar(session, variante, false);
        assertThat(productos.obtener(idProducto).isDisponibleParaCarrito()).isFalse();
        // Datos de prueba mediante las operaciones existentes del módulo de inventario.
        mvc.perform(post("/admin/zapatos/{id}/variantes/{v}/actualizar", calzado, variante)
                .param("idZapato", "" + calzado).param("talla", "39").param("color", "AZUL").param("stock", "4"))
                .andExpect(flash().attributeExists("mensaje"));
        mvc.perform(post("/admin/zapatos/{id}/variantes/{v}/estado", calzado, variante).param("activo", "false"))
                .andExpect(flash().attributeExists("mensaje"));
        em.clear();
        agregar(session, variante, false);
        assertThat(productos.obtener(idProducto).isDisponibleParaCarrito()).isFalse();
        assertThat(carrito.consultar(session).items()).isEmpty();
        assertThat(variantes.findById(variante).orElseThrow().getStock()).isEqualTo(4);
    }

    @Test void calzadoInactivoNoSePresentaComoListoParaVenta() throws Exception {
        int calzado = completarCalzado(idProducto);
        int variante = completarVariante(calzado, 4);
        mvc.perform(post("/admin/zapatos/{id}/desactivar", calzado)).andExpect(flash().attributeExists("mensaje"));
        em.clear();
        assertThat(productos.obtener(idProducto).getPreparacionVenta()).isEqualTo("Calzado inactivo");
        assertThat(productos.obtener(idProducto).isDisponibleParaCarrito()).isFalse();
        agregar(new MockHttpSession(), variante, false);
    }

    @Test void productosAnterioresSiguenDisponiblesJuntoConElNuevo() throws Exception {
        ProductoEntity anterior = ProductoEntity.builder().marca(marcas.findById(idMarca).orElseThrow())
                .nombre("Producto anterior").precioOriginal(new BigDecimal("70.00")).build();
        int idAnterior = productoRepository.saveAndFlush(anterior).getIdProducto();
        int varianteAnterior = completarVariante(completarCalzado(idAnterior), 5);
        int varianteNueva = completarVariante(completarCalzado(idProducto), 4);
        mvc.perform(get("/catalogo")).andExpect(status().isOk())
                .andExpect(content().string(containsString("Producto anterior")))
                .andExpect(content().string(containsString("Producto recién registrado")));
        MockHttpSession session = new MockHttpSession();
        agregar(session, varianteAnterior, true);
        agregar(session, varianteNueva, true);
        assertThat(carrito.consultar(session).items()).hasSize(2);
        assertThat(carrito.consultar(session).total()).isEqualByComparingTo("166.00");
        em.clear();
        assertThat(variantes.findById(varianteAnterior).orElseThrow().getStock()).isEqualTo(5);
        assertThat(variantes.findById(varianteNueva).orElseThrow().getStock()).isEqualTo(4);
        assertThat(productoRepository.count()).isEqualTo(2);
    }
}
