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
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:calzado_web;DB_CLOSE_DELAY=-1",
        "spring.jpa.hibernate.ddl-auto=create-drop", "spring.jpa.open-in-view=false",
        "spring.jpa.show-sql=false"})
@AutoConfigureMockMvc
@Transactional
class CalzadoWebIntegrationTest {
    @Autowired MockMvc mvc;
    @Autowired MarcaRepository marcas;
    @Autowired ProductoRepository productos;
    @Autowired ZapatoRepository zapatos;
    @Autowired VarianteZapatoRepository variantes;
    @Autowired MaterialRepository materiales;
    @Autowired ZapatoMaterialRepository asociaciones;
    @Autowired DisponibilidadProductoService disponibilidad;
    @Autowired EntityManager em;
    int idProducto, idZapato, idVariante, idMaterial;

    @BeforeEach void datos() {
        MarcaEntity marca = marcas.saveAndFlush(MarcaEntity.builder().nombre("Marca prueba").build());
        ProductoEntity producto = new ProductoEntity();
        producto.setMarca(marca);
        producto.setNombre("Calzado de prueba");
        producto.setPrecioOriginal(new BigDecimal("100.00"));
        producto.setDescuento(new BigDecimal("0.20"));
        idProducto = productos.saveAndFlush(producto).getIdProducto();
        ZapatoEntity zapato = new ZapatoEntity();
        zapato.setProducto(producto);
        idZapato = zapatos.saveAndFlush(zapato).getIdZapato();
        VarianteZapatoEntity variante = new VarianteZapatoEntity();
        variante.setZapato(zapato);
        variante.setTalla(38);
        variante.setColor("NEGRO");
        variante.setStock(3);
        idVariante = variantes.saveAndFlush(variante).getIdVariante();
        MaterialEntity material = new MaterialEntity();
        material.setNombre("Cuero");
        idMaterial = materiales.saveAndFlush(material).getIdMaterial();
        em.clear();
    }

    @Test void renderizaCatalogoYAdministracion() throws Exception {
        mvc.perform(get("/catalogo")).andExpect(status().isOk())
                .andExpect(content().string(containsString("Calzado de prueba")));
        mvc.perform(get("/catalogo/{id}", idProducto)).andExpect(status().isOk())
                .andExpect(content().string(containsString("NEGRO")));
        mvc.perform(get("/admin/materiales")).andExpect(status().isOk());
        mvc.perform(get("/admin/materiales/{id}/editar", idMaterial)).andExpect(status().isOk());
        mvc.perform(get("/admin/zapatos")).andExpect(status().isOk());
        mvc.perform(get("/admin/zapatos/{id}", idZapato)).andExpect(status().isOk());
        mvc.perform(get("/admin/zapatos/{id}/variantes/{v}/editar", idZapato, idVariante))
                .andExpect(status().isOk());
        mvc.perform(get("/catalogo/{id}", Integer.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test void carritoUsaPreciosRealesYValidaExistenciasYSesion() throws Exception {
        MockHttpSession session = new MockHttpSession();
        mvc.perform(post("/carrito/agregar").session(session).param("idVariante", "" + idVariante)
                .param("cantidad", "2").param("precio", "0.01"))
                .andExpect(status().is3xxRedirection()).andExpect(flash().attributeExists("mensaje"));
        mvc.perform(get("/carrito").session(session)).andExpect(status().isOk())
                .andExpect(content().string(containsString("160.00")));
        mvc.perform(post("/carrito/agregar").session(session).param("idVariante", "" + idVariante)
                .param("cantidad", "2")).andExpect(flash().attributeExists("error"));
        mvc.perform(get("/carrito").session(new MockHttpSession())).andExpect(status().isOk())
                .andExpect(content().string(containsString("Tu carrito está vacío")));
        mvc.perform(post("/carrito/{id}/actualizar", idVariante).session(session).param("cantidad", "1"))
                .andExpect(flash().attributeExists("mensaje"));
        mvc.perform(get("/carrito").session(session)).andExpect(content().string(containsString("80.00")));
        mvc.perform(post("/carrito/{id}/retirar", idVariante).session(session)).andExpect(status().is3xxRedirection());
        mvc.perform(get("/carrito").session(session)).andExpect(content().string(containsString("Tu carrito está vacío")));
        assertThat(variantes.findById(idVariante).orElseThrow().getStock()).isEqualTo(3);
    }

    @Test void desactivarBloqueaNuevasAdicionesYMarcaCarritoAnterior() throws Exception {
        MockHttpSession session = new MockHttpSession();
        mvc.perform(post("/carrito/agregar").session(session).param("idVariante", "" + idVariante)
                .param("cantidad", "1")).andExpect(flash().attributeExists("mensaje"));
        mvc.perform(post("/admin/zapatos/{id}/variantes/{v}/estado", idZapato, idVariante)
                .param("activo", "false")).andExpect(flash().attributeExists("mensaje"));
        mvc.perform(post("/carrito/agregar").session(session).param("idVariante", "" + idVariante)
                .param("cantidad", "1")).andExpect(flash().attributeExists("error"));
        mvc.perform(get("/carrito").session(session)).andExpect(status().isOk())
                .andExpect(content().string(containsString("Artículo no disponible")));
        assertThatThrownBy(() -> disponibilidad.validarVariantes(java.util.List.of(idVariante)))
                .isInstanceOf(ReglaNegocioException.class);
    }

    @Test void formulariosValidanYActualizanSinPermitirTrasladarVariante() throws Exception {
        mvc.perform(post("/admin/materiales/crear").param("nombre", " ")).andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("material", "nombre"));
        mvc.perform(post("/admin/materiales/{id}/actualizar", idMaterial).param("nombre", "Cuero natural"))
                .andExpect(flash().attributeExists("mensaje"));
        mvc.perform(post("/admin/zapatos/{id}/variantes/crear", idZapato).param("idZapato", "" + idZapato)
                .param("talla", "38").param("color", " negro ").param("stock", "2"))
                .andExpect(status().isOk()).andExpect(model().hasErrors());
        mvc.perform(post("/admin/zapatos/{id}/variantes/{v}/actualizar", idZapato, idVariante)
                .param("idZapato", "" + idZapato).param("talla", "38").param("color", "NEGRO").param("stock", "7"))
                .andExpect(flash().attributeExists("mensaje"));
        assertThat(variantes.findById(idVariante).orElseThrow().getStock()).isEqualTo(7);
        mvc.perform(post("/admin/zapatos/{id}/variantes/{v}/estado", idZapato + 999, idVariante)
                .param("activo", "false")).andExpect(flash().attributeExists("error"));
        assertThat(variantes.findById(idVariante).orElseThrow().isEstaActivo()).isTrue();
    }

    @Test void materialesInactivosConservanComposicionPeroNoNuevasAsignaciones() throws Exception {
        mvc.perform(post("/admin/zapatos/{id}/materiales/asignar", idZapato).param("idMaterial", "" + idMaterial))
                .andExpect(flash().attributeExists("mensaje"));
        mvc.perform(post("/admin/materiales/{id}/desactivar", idMaterial)).andExpect(flash().attributeExists("mensaje"));
        em.clear();
        mvc.perform(get("/catalogo/{id}", idProducto)).andExpect(status().isOk())
                .andExpect(content().string(containsString("Cuero")));
        mvc.perform(post("/admin/zapatos/{id}/materiales/{m}/retirar", idZapato, idMaterial))
                .andExpect(flash().attributeExists("mensaje"));
        mvc.perform(post("/admin/zapatos/{id}/materiales/asignar", idZapato).param("idMaterial", "" + idMaterial))
                .andExpect(flash().attributeExists("error"));
        assertThat(asociaciones.count()).isZero();
        assertThat(materiales.existsById(idMaterial)).isTrue();
    }

    @Test void calzadoInactivoDesapareceDelCatalogoYRechazaVariantes() throws Exception {
        mvc.perform(post("/admin/zapatos/{id}/desactivar", idZapato)).andExpect(flash().attributeExists("mensaje"));
        em.clear();
        mvc.perform(get("/catalogo/{id}", idProducto)).andExpect(status().isNotFound());
        mvc.perform(get("/catalogo")).andExpect(status().isOk())
                .andExpect(content().string(containsString("No hay calzados disponibles")));
        mvc.perform(post("/carrito/agregar").param("idVariante", "" + idVariante).param("cantidad", "1"))
                .andExpect(flash().attributeExists("error"));
    }
}
