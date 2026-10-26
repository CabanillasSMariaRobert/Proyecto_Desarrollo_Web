package com.ecommerce.tienda_kalza.adapter;

import com.ecommerce.tienda_kalza.adapter.entity.*;
import com.ecommerce.tienda_kalza.adapter.enums.RedUsada;
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
import java.util.*;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {"spring.datasource.url=jdbc:h2:mem:rn01;DB_CLOSE_DELAY=-1",
        "spring.jpa.hibernate.ddl-auto=create-drop", "spring.jpa.open-in-view=false", "spring.jpa.show-sql=false"})
@AutoConfigureMockMvc
@Transactional
class Rn01IntegrationTest {
    @Autowired EntityManager em;
    @Autowired ProductoService productos;
    @Autowired DisponibilidadProductoService disponibilidad;
    @Autowired CompraService compras;
    @Autowired ProductoRepository productoRepository;
    @Autowired CompraRepository compraRepository;
    @Autowired MockMvc mvc;
    ProductoEntity producto;
    VarianteZapatoEntity variante;
    UsuarioEntity cliente;

    @BeforeEach void preparar() {
        MarcaEntity marca = MarcaEntity.builder().nombre("Marca RN01").build();
        em.persist(marca);
        producto = ProductoEntity.builder().nombre("Calzado RN01").marca(marca)
                .precioOriginal(new BigDecimal("80.00")).build();
        em.persist(producto);
        variante = crearVariante(producto);
        cliente = UsuarioEntity.builder().nombres("Cliente").apellidos("Prueba")
                .correo("rn01@example.test").clave("prueba").build();
        em.persist(cliente);
        em.flush();
    }

    VarianteZapatoEntity crearVariante(ProductoEntity p) {
        ZapatoEntity zapato = new ZapatoEntity();
        zapato.setProducto(p);
        em.persist(zapato);
        VarianteZapatoEntity v = VarianteZapatoEntity.builder().zapato(zapato).talla(40)
                .color("Negro").stock(10).build();
        em.persist(v);
        return v;
    }

    CompraEntity nuevaCompra(VarianteZapatoEntity... variantes) {
        CompraEntity compra = CompraEntity.builder().cliente(cliente).totalSinDescuento(new BigDecimal("80.00"))
                .totalConDescuento(new BigDecimal("80.00")).build();
        for (VarianteZapatoEntity v : variantes) {
            compra.getDetalles().add(DetalleCompraEntity.builder().compra(compra).varianteZapato(v)
                    .nombreDelProducto(v.getZapato().getProducto().getNombre()).cantidad(1)
                    .precioOriginalXUnidad(new BigDecimal("80.00")).build());
        }
        return compra;
    }

    long boletas() { return em.createQuery("select count(b) from BoletaEntity b", Long.class).getSingleResult(); }
    long detalles() { return em.createQuery("select count(d) from DetalleCompraEntity d", Long.class).getSingleResult(); }

    @Test void productoNuevoActivoYNoAdmiteNullEnLaBase() {
        assertThat(new ProductoEntity().isEstaActivo()).isTrue();
        assertThat(producto.isEstaActivo()).isTrue();
        em.clear();
        assertThat(productoRepository.findById(producto.getIdProducto()).orElseThrow().isEstaActivo()).isTrue();
        assertThat(em.createNativeQuery("select is_nullable from information_schema.columns "
                + "where table_name = 'productos' and column_name = 'ESTA_ACTIVO'")
                .getSingleResult()).isEqualTo("NO");
    }

    @Test void administraEstadoPersistidoMediantePostYEsIdempotente() throws Exception {
        int id = producto.getIdProducto();
        for (boolean activo : List.of(false, false, true)) {
            mvc.perform(post("/admin/productos/{id}/estado", id).param("activo", String.valueOf(activo)))
                    .andExpect(status().is3xxRedirection()).andExpect(flash().attributeExists("mensaje"));
            em.clear();
            assertThat(productoRepository.findById(id).orElseThrow().isEstaActivo()).isEqualTo(activo);
        }
    }

    @Test void inexistenteProduceErrorControlado() throws Exception {
        mvc.perform(post("/admin/productos/{id}/estado", Integer.MAX_VALUE).param("activo", "false"))
                .andExpect(status().is3xxRedirection()).andExpect(flash().attribute("error", "El producto no existe."));
        assertThat(productoRepository.count()).isEqualTo(1);
    }

    @Test void editarNoReactivaNiPermiteInyectarEstado() throws Exception {
        int id = producto.getIdProducto();
        productos.cambiarEstado(id, false);
        mvc.perform(post("/admin/productos/{id}/actualizar", id).param("nombre", "Editado inactivo")
                .param("idMarca", String.valueOf(producto.getMarca().getIdMarca()))
                .param("precioOriginal", "80").param("descuento", "0").param("estaActivo", "true"))
                .andExpect(status().is3xxRedirection()).andExpect(flash().attributeExists("mensaje"));
        em.clear();
        assertThat(productos.obtener(id).isEstaActivo()).isFalse();
        assertThat(productos.obtener(id).getNombre()).isEqualTo("Editado inactivo");
    }

    @Test void administracionIncluyeInactivosYConsultaPublicaLosExcluye() throws Exception {
        productos.cambiarEstado(producto.getIdProducto(), false);
        assertThat(productos.listar(0, "").getTotalElements()).isEqualTo(1);
        assertThat(productos.listarDisponibles(0, "").getTotalElements()).isZero();
        assertThatThrownBy(() -> productos.obtenerDisponible(producto.getIdProducto()))
                .isInstanceOf(ReglaNegocioException.class).hasMessageContaining("no está disponible");
        mvc.perform(get("/admin/productos")).andExpect(status().isOk())
                .andExpect(content().string(containsString("Inactivo")))
                .andExpect(content().string(containsString("Activar")));
    }

    @Test void reactivarRestauraDisponibilidad() {
        productos.cambiarEstado(producto.getIdProducto(), false);
        productos.cambiarEstado(producto.getIdProducto(), true);
        assertThat(productos.obtenerDisponible(producto.getIdProducto()).isEstaActivo()).isTrue();
        assertThat(productos.listarDisponibles(0, "CALZADO").getTotalElements()).isEqualTo(1);
        disponibilidad.validarVariantes(List.of(variante.getIdVariante()));
    }

    @Test void validadorDeVarianteRechazaInactivoYVarianteInexistente() {
        productos.cambiarEstado(producto.getIdProducto(), false);
        assertThatThrownBy(() -> disponibilidad.validarVariantes(List.of(variante.getIdVariante())))
                .isInstanceOf(ReglaNegocioException.class).hasMessageContaining("Calzado RN01");
        assertThatThrownBy(() -> disponibilidad.validarVariantes(List.of(Integer.MAX_VALUE)))
                .isInstanceOf(ReglaNegocioException.class);
    }

    @Test void compraActivaPersisteConSusReglasActuales() {
        CompraEntity compra = compras.guardar(nuevaCompra(variante));
        em.flush();
        em.clear();
        CompraEntity guardada = compras.obtener(compra.getIdCompra());
        assertThat(guardada.getDetalles()).hasSize(1);
        assertThat(guardada.getTotalConDescuento()).isEqualByComparingTo("80.00");
    }

    @Test void productoDesactivadoDespuesDeSeleccionarNoSeCompraAunqueLaEntidadEsteObsoleta() {
        CompraEntity seleccion = nuevaCompra(variante);
        em.detach(producto);
        productos.cambiarEstado(producto.getIdProducto(), false);
        assertThat(producto.isEstaActivo()).isTrue(); // snapshot anterior del cliente
        assertThatThrownBy(() -> compras.guardar(seleccion)).isInstanceOf(ReglaNegocioException.class)
                .hasMessageContaining("Calzado RN01");
        assertThat(compraRepository.count()).isZero();
        assertThat(detalles()).isZero();
        assertThat(boletas()).isZero();
        em.refresh(variante);
        assertThat(variante.getStock()).isEqualTo(10);
    }

    @Test void compraMixtaSeRechazaCompletaSinEfectosParciales() {
        ProductoEntity otro = ProductoEntity.builder().nombre("Segundo producto").marca(producto.getMarca())
                .precioOriginal(new BigDecimal("80.00")).build();
        em.persist(otro);
        VarianteZapatoEntity segunda = crearVariante(otro);
        em.flush();
        productos.cambiarEstado(otro.getIdProducto(), false);
        assertThatThrownBy(() -> compras.guardar(nuevaCompra(variante, segunda)))
                .isInstanceOf(ReglaNegocioException.class).hasMessageContaining("Segundo producto");
        assertThat(compraRepository.count()).isZero();
        assertThat(detalles()).isZero();
        assertThat(boletas()).isZero();
        assertThat(variante.getStock()).isEqualTo(10);
        assertThat(segunda.getStock()).isEqualTo(10);
    }

    @Test void pendienteNoPuedePagarseNiSaltarConfirmacionConProductoInactivo() {
        CompraEntity compra = compras.guardar(nuevaCompra(variante));
        em.flush();
        productos.cambiarEstado(producto.getIdProducto(), false);
        for (String estado : List.of("pagado", "enviado", "entregado")) {
            assertThatThrownBy(() -> compras.cambiarEstado(compra.getIdCompra(), estado))
                    .isInstanceOf(ReglaNegocioException.class).hasMessageContaining("Calzado RN01");
        }
        assertThat(compras.obtener(compra.getIdCompra()).getEstado()).isEqualTo("pendiente");
        assertThat(boletas()).isZero();
    }

    @Test void guardarNoPermiteConfirmacionPorRutaAlternativa() {
        CompraEntity compra = compras.guardar(nuevaCompra(variante));
        em.flush();
        productos.cambiarEstado(producto.getIdProducto(), false);
        CompraEntity actualizacion = nuevaCompra(); // el formulario puede omitir detalles
        actualizacion.setIdCompra(compra.getIdCompra());
        actualizacion.setEstado("pagado");
        assertThatThrownBy(() -> compras.guardar(actualizacion)).isInstanceOf(ReglaNegocioException.class);
        assertThat(compras.obtener(compra.getIdCompra()).getEstado()).isEqualTo("pendiente");
    }

    @Test void cancelacionDePendienteSiguePermitida() {
        CompraEntity compra = compras.guardar(nuevaCompra(variante));
        em.flush();
        productos.cambiarEstado(producto.getIdProducto(), false);
        compras.cambiarEstado(compra.getIdCompra(), "cancelado");
        assertThat(compras.obtener(compra.getIdCompra()).getEstado()).isEqualTo("cancelado");
    }

    @Test void estadoModificadoEnEntidadAdministradaNoEvadeValidacion() {
        CompraEntity compra = compras.guardar(nuevaCompra(variante));
        em.flush();
        productos.cambiarEstado(producto.getIdProducto(), false);
        compra.setEstado("pagado");
        assertThatThrownBy(() -> compras.guardar(compra)).isInstanceOf(ReglaNegocioException.class);
    }

    @Test void nuevasUnidadesEnHistorialTambienExigenDisponibilidadSinBloquearEdicionHistorica() {
        CompraEntity compra = nuevaCompra(variante);
        compra.setEstado("pagado");
        compras.guardar(compra);
        em.flush();
        productos.cambiarEstado(producto.getIdProducto(), false);
        compras.guardar(compra); // sin nuevas unidades conserva el comportamiento histórico
        em.flush();
        compra.getDetalles().get(0).setCantidad(2);
        assertThatThrownBy(() -> compras.guardar(compra)).isInstanceOf(ReglaNegocioException.class);
    }

    @Test void compraPendienteActivaPuedeConfirmarse() {
        CompraEntity compra = compras.guardar(nuevaCompra(variante));
        em.flush();
        compras.cambiarEstado(compra.getIdCompra(), "pagado");
        assertThat(compras.obtener(compra.getIdCompra()).getEstado()).isEqualTo("pagado");
    }

    @Test void historialPagadoConservaDetallesBoletaPreciosYStock() {
        CompraEntity compra = nuevaCompra(variante);
        compra.setEstado("pagado");
        compras.guardar(compra);
        MetodoPagoEntity pago = MetodoPagoEntity.builder().usuario(cliente).pasarela("Prueba")
                .redUsada(RedUsada.VISA).token("test-rn01").ultimos4Digitos("1234")
                .expira_el_mes(12).expira_el_anio(2030).build();
        em.persist(pago);
        BoletaEntity boleta = BoletaEntity.builder().compra(compra).cliente("Cliente Prueba")
                .rucDeLaTienda("12345678901").pasarelaDePago("Prueba").redDeTarjeta(RedUsada.VISA)
                .ultimos4Digitos("1234").total(new BigDecimal("80.00")).metodoDePago(pago).build();
        em.persist(boleta);
        em.flush();
        int idCompra = compra.getIdCompra();
        int idBoleta = boleta.getIdBoleta();
        var fecha = compra.getFechaDeCompra();
        em.clear();
        productos.cambiarEstado(producto.getIdProducto(), false);
        em.clear();
        CompraEntity historial = compras.obtener(idCompra);
        assertThat(historial.getEstado()).isEqualTo("pagado");
        assertThat(historial.getFechaDeCompra()).isEqualTo(fecha);
        assertThat(historial.getDetalles()).hasSize(1);
        DetalleCompraEntity detalle = historial.getDetalles().get(0);
        assertThat(detalle.getNombreDelProducto()).isEqualTo("Calzado RN01");
        assertThat(detalle.getPrecioOriginalXUnidad()).isEqualByComparingTo("80.00");
        assertThat(detalle.getPrecioConDescuento()).isEqualByComparingTo("80.00");
        assertThat(detalle.getCantidad()).isEqualTo(1);
        assertThat(detalle.getVarianteZapato().getStock()).isEqualTo(10);
        assertThat(historial.getBoleta().getIdBoleta()).isEqualTo(idBoleta);
        assertThat(historial.getBoleta().getTotal()).isEqualByComparingTo("80.00");
        compras.cambiarEstado(idCompra, "enviado"); // no bloquea la logística de compras pagadas
        assertThat(compras.obtener(idCompra).getEstado()).isEqualTo("enviado");
    }
}
