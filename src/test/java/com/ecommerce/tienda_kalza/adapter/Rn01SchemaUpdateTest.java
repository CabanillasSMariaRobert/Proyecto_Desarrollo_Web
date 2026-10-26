package com.ecommerce.tienda_kalza.adapter;

import com.ecommerce.tienda_kalza.adapter.repository.ProductoRepository;
import com.ecommerce.tienda_kalza.adapter.repository.MarcaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {"spring.datasource.url=jdbc:h2:mem:rn01Migracion;DB_CLOSE_DELAY=-1",
        "spring.jpa.hibernate.ddl-auto=update", "spring.jpa.show-sql=false",
        "spring.sql.init.mode=always", "spring.sql.init.schema-locations=classpath:rn01-productos-anteriores.sql"})
class Rn01SchemaUpdateTest {
    @Autowired ProductoRepository productos;
    @Autowired MarcaRepository marcas;

    @Test void marcaAnteriorQuedaActivaAlAgregarColumnaRn04() {
        var anterior = marcas.findById(42).orElseThrow();
        assertThat(anterior.isEstaActivo()).isTrue();
        assertThat(anterior.getNombre()).isEqualTo("Marca anterior");
    }

    @Test void agregaColumnaConDefaultSinPerderProductosExistentes() {
        var anterior = productos.findById(42).orElseThrow();
        assertThat(anterior.isEstaActivo()).isTrue();
        assertThat(anterior.getNombre()).isEqualTo("Producto anterior a RN01");
        assertThat(anterior.getPrecioOriginal()).isEqualByComparingTo("120.00");
        assertThat(anterior.getDescuento()).isEqualByComparingTo("0.20");
        assertThat(anterior.getPrecioConDescuento()).isEqualByComparingTo("96.00");
        assertThat(productos.count()).isEqualTo(1);
    }
}
