package com.ecommerce.tienda_kalza.adapter.service;

import com.ecommerce.tienda_kalza.adapter.entity.MarcaEntity;
import com.ecommerce.tienda_kalza.adapter.model.Marca;
import com.ecommerce.tienda_kalza.adapter.repository.MarcaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MarcaServiceTest {
    @Mock MarcaRepository repository;
    @InjectMocks MarcaService service;

    private Marca formulario() {
        Marca marca = new Marca();
        marca.setNombre("  Kalza  ");
        marca.setDescripcion(" Descripción ");
        return marca;
    }

    @Test void registraNombreNormalizado() {
        service.guardar(null, formulario());
        ArgumentCaptor<MarcaEntity> captor = ArgumentCaptor.forClass(MarcaEntity.class);
        verify(repository).saveAndFlush(captor.capture());
        assertThat(captor.getValue().getNombre()).isEqualTo("Kalza");
        assertThat(captor.getValue().getDescripcion()).isEqualTo("Descripción");
    }

    @Test void rechazaDuplicados() {
        when(repository.existsByNombreIgnoreCaseAndIdMarcaNot("Kalza", 0)).thenReturn(true);
        assertThatThrownBy(() -> service.guardar(null, formulario())).isInstanceOf(IllegalStateException.class);
        verify(repository, never()).saveAndFlush(any());
    }

    @Test void actualizaLaMarcaExistente() {
        MarcaEntity entity = MarcaEntity.builder().idMarca(7).nombre("Antes").build();
        when(repository.findById(7)).thenReturn(Optional.of(entity));
        service.guardar(7, formulario());
        assertThat(entity.getNombre()).isEqualTo("Kalza");
        verify(repository).existsByNombreIgnoreCaseAndIdMarcaNot("Kalza", 7);
        verify(repository).saveAndFlush(entity);
    }
}
