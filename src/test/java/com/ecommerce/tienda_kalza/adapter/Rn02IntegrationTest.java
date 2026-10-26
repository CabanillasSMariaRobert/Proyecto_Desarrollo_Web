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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {"spring.datasource.url=jdbc:h2:mem:rn02;DB_CLOSE_DELAY=-1",
        "spring.jpa.hibernate.ddl-auto=create-drop", "spring.jpa.open-in-view=false", "spring.jpa.show-sql=false"})
@AutoConfigureMockMvc @Transactional
class Rn02IntegrationTest {
    @Autowired ProductoService productos;
    @Autowired CategoriaService categorias;
    @Autowired ProductoRepository productoRepository;
    @Autowired CategoriaRepository categoriaRepository;
    @Autowired MarcaRepository marcaRepository;
    @Autowired EntityManager em;
    @Autowired MockMvc mvc;
    int marca, activa, inactiva;

    @BeforeEach void datos() {
        marca = marcaRepository.saveAndFlush(MarcaEntity.builder().nombre("Marca RN02").build()).getIdMarca();
        activa = categoriaRepository.saveAndFlush(CategoriaEntity.builder().nombre("Activa RN02").build()).getIdCategoria();
        inactiva = categoriaRepository.saveAndFlush(CategoriaEntity.builder().nombre("Inactiva RN02")
                .estaActivo(false).build()).getIdCategoria();
    }

    Producto formulario(int categoria) {
        Producto producto = new Producto();
        producto.setNombre("Producto RN02");
        producto.setIdMarca(marca);
        producto.setPrecioOriginal(new BigDecimal("100.00"));
        producto.setDescuento(new BigDecimal("0.20"));
        producto.setIdsCategorias(List.of(categoria));
        return producto;
    }

    int registrar() {
        productos.guardar(null, formulario(activa));
        return productoRepository.findAll().get(0).getIdProducto();
    }

    @Test void desactivaSinAsociacionesYReactivaSinEliminarRegistro() throws Exception {
        for (boolean esperado : List.of(false, true)) {
            mvc.perform(post("/admin/categorias/{id}/estado", activa))
                    .andExpect(status().is3xxRedirection()).andExpect(flash().attributeExists("mensaje"));
            em.flush();
            em.clear();
            assertThat(categoriaRepository.findById(activa).orElseThrow().getEstaActivo()).isEqualTo(esperado);
            assertThat(categoriaRepository.count()).isEqualTo(2);
        }
    }

    @Test void rechazaDesactivacionAsociadaConMensajeSinCambiarProductosNiRelaciones() throws Exception {
        int id = registrar();
        mvc.perform(post("/admin/categorias/{id}/estado", activa))
                .andExpect(status().is3xxRedirection()).andExpect(flash().attribute("error", containsString("producto(s) asociado(s)")));
        em.clear();
        assertThat(categoriaRepository.findById(activa).orElseThrow().getEstaActivo()).isTrue();
        assertThat(productos.obtener(id).getIdsCategorias()).containsExactly(activa);
        assertThat(categoriaRepository.contarProductos(activa)).isEqualTo(1);
        assertThat(productos.obtener(id).isEstaActivo()).isTrue();
    }

    @Test void rechazaAltaConCategoriaInactivaSinGuardarProducto() {
        assertThatThrownBy(() -> productos.guardar(null, formulario(inactiva)))
                .isInstanceOf(IllegalArgumentException.class).hasMessageContaining("inactiva");
        em.clear();
        assertThat(productoRepository.count()).isZero();
        assertThat(categoriaRepository.contarProductos(inactiva)).isZero();
    }

    @Test void rechazaReclasificacionSinModificarParcialmenteDatosNiAsociaciones() {
        int id = registrar();
        Producto editado = formulario(inactiva);
        editado.setNombre("NO debe guardarse");
        editado.setPrecioOriginal(new BigDecimal("999.00"));
        assertThatThrownBy(() -> productos.guardar(id, editado)).isInstanceOf(IllegalArgumentException.class);
        em.clear();
        Producto guardado = productos.obtener(id);
        assertThat(guardado.getNombre()).isEqualTo("Producto RN02");
        assertThat(guardado.getPrecioOriginal()).isEqualByComparingTo("100.00");
        assertThat(guardado.getDescuento()).isEqualByComparingTo("0.20");
        assertThat(guardado.getIdsCategorias()).containsExactly(activa);
        assertThat(categoriaRepository.contarProductos(inactiva)).isZero();
    }

    @Test void formularioNuevoSoloOfreceCategoriasActivasYRechazaEnvioManual() throws Exception {
        mvc.perform(get("/admin/productos/nuevo")).andExpect(status().isOk())
                .andExpect(content().string(not(containsString("Inactiva RN02"))));
        mvc.perform(post("/admin/productos/crear").param("nombre", "Intento manual")
                .param("idMarca", "" + marca).param("precioOriginal", "100").param("descuento", "0")
                .param("idsCategorias", "" + inactiva))
                .andExpect(status().isOk()).andExpect(model().hasErrors())
                .andExpect(content().string(containsString("no admite nuevas asignaciones")));
        assertThat(productoRepository.count()).isZero();
    }

    @Test void conservaAsociacionInactivaPreexistenteYPermiteRetirarla() throws Exception {
        int id = registrar();
        CategoriaEntity anterior = categoriaRepository.findById(activa).orElseThrow();
        anterior.setEstaActivo(false); // representa datos heredados, no una operación permitida por RN02
        categoriaRepository.saveAndFlush(anterior);
        em.clear();
        mvc.perform(get("/admin/productos/{id}/editar", id)).andExpect(status().isOk())
                .andExpect(content().string(containsString("Conservar Activa RN02 (inactiva)")))
                .andExpect(content().string(not(containsString("Inactiva RN02"))));
        mvc.perform(post("/admin/productos/{id}/actualizar", id).param("nombre", "Editado")
                .param("idMarca", "" + marca).param("precioOriginal", "100").param("descuento", "0.20")
                .param("idsCategorias", "" + activa)).andExpect(flash().attributeExists("mensaje"));
        em.clear();
        assertThat(productos.obtener(id).getIdsCategorias()).containsExactly(activa);
        Producto sinCategorias = productos.obtener(id);
        sinCategorias.setIdsCategorias(List.of());
        productos.guardar(id, sinCategorias);
        em.clear();
        assertThat(productos.obtener(id).getIdsCategorias()).isEmpty();
        assertThat(categoriaRepository.findById(activa).orElseThrow().getEstaActivo()).isFalse();
        assertThatThrownBy(() -> productos.guardar(id, formulario(activa))).isInstanceOf(IllegalArgumentException.class);
    }

    @Test void edicionManualNoPuedeIntroducirOtraCategoriaInactiva() throws Exception {
        int id = registrar();
        mvc.perform(post("/admin/productos/{id}/actualizar", id).param("nombre", "NO guardar")
                .param("idMarca", "" + marca).param("precioOriginal", "50").param("descuento", "0")
                .param("idsCategorias", "" + inactiva)).andExpect(status().isOk()).andExpect(model().hasErrors());
        em.clear();
        assertThat(productos.obtener(id).getNombre()).isEqualTo("Producto RN02");
        assertThat(productos.obtener(id).getIdsCategorias()).containsExactly(activa);
    }
}
