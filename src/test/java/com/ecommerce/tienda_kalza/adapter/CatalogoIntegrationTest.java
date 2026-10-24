package com.ecommerce.tienda_kalza.adapter;

import com.ecommerce.tienda_kalza.adapter.entity.*;
import com.ecommerce.tienda_kalza.adapter.model.*;
import com.ecommerce.tienda_kalza.adapter.repository.*;
import com.ecommerce.tienda_kalza.adapter.service.*;
import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:catalogo;DB_CLOSE_DELAY=-1",
        "spring.jpa.hibernate.ddl-auto=create-drop", "spring.jpa.open-in-view=false"})
@AutoConfigureMockMvc
@Transactional
class CatalogoIntegrationTest {
    @Autowired ProductoService productos;
    @Autowired MarcaService marcas;
    @Autowired CategoriaService categorias;
    @Autowired ProductoRepository productoRepository;
    @Autowired MarcaRepository marcaRepository;
    @Autowired CategoriaRepository categoriaRepository;
    @Autowired EntityManager em;
    @Autowired MockMvc mvc;
    int marcaId;
    int categoriaId;

    @BeforeEach void preparar() {
        marcaId = marcaRepository.saveAndFlush(MarcaEntity.builder().nombre("Kalza").build()).getIdMarca();
        categoriaId = categoriaRepository.saveAndFlush(CategoriaEntity.builder().nombre("Casual").build()).getIdCategoria();
    }

    Producto formulario() {
        Producto model = new Producto();
        model.setNombre("Producto real");
        model.setPrecioOriginal(new BigDecimal("100.00"));
        model.setDescuento(new BigDecimal("0.20"));
        model.setIdMarca(marcaId);
        model.setIdsCategorias(List.of(categoriaId));
        return model;
    }

    int registrar() {
        productos.guardar(null, formulario());
        return productoRepository.findAll().get(0).getIdProducto();
    }

    @Test void registraRelacionesYPrecioGenerado() {
        int id = registrar();
        em.clear();
        Producto obtenido = productos.obtener(id);
        assertThat(obtenido.getNombreMarca()).isEqualTo("Kalza");
        assertThat(obtenido.getIdsCategorias()).containsExactly(categoriaId);
        assertThat(obtenido.getPrecioConDescuento()).isEqualByComparingTo("80.00");
        assertThat(categoriaRepository.contarProductos(categoriaId)).isEqualTo(1);
        assertThat(categoriaRepository.findById(categoriaId).orElseThrow().getEstaActivo()).isTrue();
    }

    @Test void actualizaCategoriasSinBorrarCategoriasNiDuplicarRelaciones() {
        int id = registrar();
        int otra = categoriaRepository.saveAndFlush(CategoriaEntity.builder().nombre("Deportivo").build()).getIdCategoria();
        Producto model = formulario();
        model.setIdsCategorias(List.of(categoriaId, otra));
        productos.guardar(id, model);
        productos.guardar(id, model);
        em.clear();
        assertThat(productos.obtener(id).getIdsCategorias()).containsExactlyInAnyOrder(categoriaId, otra);
        model.setIdsCategorias(List.of(otra));
        productos.guardar(id, model);
        em.clear();
        assertThat(productos.obtener(id).getIdsCategorias()).containsExactly(otra);
        assertThat(categoriaRepository.count()).isEqualTo(2);
        assertThat(categoriaRepository.contarProductos(categoriaId)).isZero();
        model.setIdsCategorias(List.of());
        productos.guardar(id, model);
        em.clear();
        assertThat(productos.obtener(id).getIdsCategorias()).isEmpty();
        assertThat(categoriaRepository.count()).isEqualTo(2);
    }

    @Test void rechazaAsociacionesDuplicadas() {
        Producto model = formulario();
        model.setIdsCategorias(List.of(categoriaId, categoriaId));
        assertThatThrownBy(() -> productos.guardar(null, model)).isInstanceOf(IllegalArgumentException.class);
        assertThat(productoRepository.count()).isZero();
    }

    @Test void rechazaMarcaInexistente() {
        Producto model = formulario();
        model.setIdMarca(Integer.MAX_VALUE);
        assertThatThrownBy(() -> productos.guardar(null, model)).hasMessageContaining("marca");
        assertThat(productoRepository.count()).isZero();
    }

    @Test void rechazaCategoriaInexistente() {
        Producto model = formulario();
        model.setIdsCategorias(List.of(Integer.MAX_VALUE));
        assertThatThrownBy(() -> productos.guardar(null, model)).hasMessageContaining("categoría");
        assertThat(productoRepository.count()).isZero();
    }

    @Test void rechazaPrecioNegativo() {
        Producto model = formulario();
        model.setPrecioOriginal(new BigDecimal("-0.01"));
        assertThatThrownBy(() -> productos.guardar(null, model)).isInstanceOf(ConstraintViolationException.class);
    }

    @Test void validaDescuentosFueraDeRango() {
        for (String valor : List.of("-0.01", "1.01")) {
            Producto model = formulario();
            model.setDescuento(new BigDecimal(valor));
            assertThatThrownBy(() -> productos.guardar(null, model)).isInstanceOf(ConstraintViolationException.class);
        }
    }

    @Test void permiteExtremosDelDescuento() {
        for (String valor : List.of("0.00", "1.00")) {
            Producto model = formulario();
            model.setDescuento(new BigDecimal(valor));
            productos.guardar(null, model);
        }
        assertThat(productoRepository.count()).isEqualTo(2);
    }

    @Test void marcasValidaDuplicadosYActualizacion() {
        Marca marca = new Marca();
        marca.setNombre(" kalza ");
        assertThatThrownBy(() -> marcas.guardar(null, marca)).isInstanceOf(IllegalStateException.class);
        marca.setNombre("Otra marca");
        marcas.guardar(null, marca);
        marca.setNombre("Kalza renovada");
        marcas.guardar(marcaId, marca);
        assertThat(marcas.obtener(marcaId).getNombre()).isEqualTo("Kalza renovada");
    }

    @Test void renderizaVistasConDatosReales() throws Exception {
        int id = registrar();
        mvc.perform(get("/admin/productos")).andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Producto real")));
        mvc.perform(get("/admin/productos/{id}/editar", id)).andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("selected=\"selected\"")));
        mvc.perform(get("/admin/productos/nuevo")).andExpect(status().isOk());
        mvc.perform(get("/admin/marcas")).andExpect(status().isOk());
        mvc.perform(get("/admin/marcas/nueva")).andExpect(status().isOk());
        mvc.perform(get("/admin/marcas/{id}/editar", marcaId)).andExpect(status().isOk());
        mvc.perform(get("/admin/categorias")).andExpect(status().isOk());
    }

    @Test void formulariosInvalidosMuestranErroresSinGuardar() throws Exception {
        mvc.perform(post("/admin/productos/crear").param("nombre", " ")
                .param("precioOriginal", "-1").param("descuento", "2").param("idMarca", "0"))
                .andExpect(status().isOk()).andExpect(model().attributeHasFieldErrors("producto", "nombre", "precioOriginal", "descuento", "idMarca"));
        mvc.perform(post("/admin/marcas/crear").param("nombre", " "))
                .andExpect(status().isOk()).andExpect(model().attributeHasFieldErrors("marca", "nombre"));
        mvc.perform(post("/admin/categorias/crear").param("nombre", " "))
                .andExpect(status().is3xxRedirection()).andExpect(flash().attributeExists("error"));
        assertThat(productoRepository.count()).isZero();
    }

    @Test void creaYEditaMedianteFormularios() throws Exception {
        mvc.perform(post("/admin/productos/crear").param("nombre", "Desde formulario")
                .param("precioOriginal", "90.00").param("descuento", "0.10")
                .param("idMarca", String.valueOf(marcaId)).param("idsCategorias", String.valueOf(categoriaId)))
                .andExpect(status().is3xxRedirection()).andExpect(flash().attributeExists("mensaje"));
        int id = productoRepository.findAll().get(0).getIdProducto();
        mvc.perform(post("/admin/productos/{id}/actualizar", id).param("nombre", "Editado")
                .param("precioOriginal", "80.00").param("descuento", "0")
                .param("idMarca", String.valueOf(marcaId)).param("_idsCategorias", "on"))
                .andExpect(status().is3xxRedirection()).andExpect(flash().attributeExists("mensaje"));
        em.clear();
        assertThat(productos.obtener(id).getNombre()).isEqualTo("Editado");
        assertThat(productos.obtener(id).getIdsCategorias()).isEmpty();
    }

    @Test void categoriasConservaReglasDeEstadoYDetectaDuplicados() {
        Categoria model = new Categoria();
        model.setNombre(" casual ");
        assertThatThrownBy(() -> categorias.crear(model)).isInstanceOf(IllegalStateException.class);
        model.setNombre("Casual editada");
        categorias.actualizar(categoriaId, model);
        assertThat(categorias.obtener(categoriaId).getNombre()).isEqualTo("Casual editada");
        assertThat(categorias.eliminar(categoriaId)).isFalse();
        assertThat(categorias.eliminar(categoriaId)).isTrue();
        registrar();
        assertThatThrownBy(() -> categorias.eliminar(categoriaId)).isInstanceOf(IllegalStateException.class);
        assertThat(categorias.obtener(categoriaId).isEstaActivo()).isTrue();
    }

    @Test void registraCategoriasYValidaCamposDesdeServicio() {
        Categoria model = new Categoria();
        model.setNombre(" Nueva categoría ");
        categorias.crear(model);
        assertThat(categorias.listar(0, 10).getContent()).extracting(Categoria::getNombre)
                .contains("Nueva categoría");
        model.setNombre(" ");
        assertThatThrownBy(() -> categorias.crear(model)).isInstanceOf(ConstraintViolationException.class);
        Marca marca = new Marca();
        marca.setNombre(" ");
        assertThatThrownBy(() -> marcas.guardar(null, marca)).isInstanceOf(ConstraintViolationException.class);
    }

    @Test void paginaYBuscaProductos() {
        for (int i = 0; i < 12; i++) {
            Producto model = formulario();
            model.setNombre("Producto " + i);
            productos.guardar(null, model);
        }
        assertThat(productos.listar(0, "producto").getNumberOfElements()).isEqualTo(10);
        assertThat(productos.listar(1, "producto").getNumberOfElements()).isEqualTo(2);
        assertThat(productos.listar(0, "PRODUCTO 11").getTotalElements()).isEqualTo(1);
        assertThat(productos.listar(0, "inexistente").getTotalElements()).isZero();
    }
}
