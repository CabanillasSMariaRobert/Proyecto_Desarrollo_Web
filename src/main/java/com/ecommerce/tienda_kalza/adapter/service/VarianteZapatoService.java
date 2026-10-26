package com.ecommerce.tienda_kalza.adapter.service;

import com.ecommerce.tienda_kalza.adapter.entity.VarianteZapatoEntity;
import com.ecommerce.tienda_kalza.adapter.entity.ZapatoEntity;
import com.ecommerce.tienda_kalza.adapter.model.VarianteZapato;
import com.ecommerce.tienda_kalza.adapter.repository.VarianteZapatoRepository;
import com.ecommerce.tienda_kalza.adapter.repository.ZapatoRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Service
@Validated
@RequiredArgsConstructor
public class VarianteZapatoService {
    private final VarianteZapatoRepository repository;
    private final ZapatoRepository zapatos;

    @Transactional
    public void guardar(Integer id, @NotNull @Valid VarianteZapato model){
        VarianteZapatoEntity entity = id == null ? new VarianteZapatoEntity() : buscarParaActualizar(id);

        ZapatoEntity zapato = buscarZapato(model.getIdZapato());

        if (!zapato.isEstaActivo()) throw new IllegalStateException("No se pueden registrar o modificar variantes de un calzado desactivado.");

        if (id != null) {
            if (entity.getZapato().getIdZapato() != zapato.getIdZapato()){
                throw new IllegalStateException("No se puede trasladar una variante a otro calzado.");
            }
            if (entity.getTalla() != model.getTalla() || !normalizarColor(entity.getColor()).equals(normalizarColor(model.getColor()))){
                throw new IllegalStateException("No se puede cambiar la talla o el color de una variante registrada. Desactívala y crea otra."
                );
            }
        }

        String color = normalizarColor(model.getColor());

        boolean duplicado = repository.existsByZapato_IdZapatoAndTallaAndColorIgnoreCaseAndIdVarianteNot(
                zapato.getIdZapato(),
                model.getTalla(),
                color,
                entity.getIdVariante()
        );

        if(duplicado) throw new IllegalStateException("Ya existe una variante con esa talla y color, incluso si está desactivada.");

        entity.setZapato(zapato);
        entity.setTalla(model.getTalla());
        entity.setColor(color);
        entity.setStock(model.getStock());

        repository.saveAndFlush(entity);
    }

    // READ (uno)
    @Transactional(readOnly = true)
    public VarianteZapato obtener(int id) {
        return aModel(buscar(id));
    }

    // READ (lista)
    @Transactional(readOnly = true)
    public List<VarianteZapato> listarPorZapato(int idZapato) {
        buscarZapato(idZapato);

        return repository.findByZapato_IdZapato(idZapato).stream().sorted(Comparator.comparingInt(VarianteZapatoEntity::getTalla)
                        .thenComparing(VarianteZapatoEntity::getColor).thenComparingInt(VarianteZapatoEntity::getIdVariante))
                .map(this::aModel).toList();
    }

    // DELETE
    @Transactional
    public void desactivar(int id) {
        VarianteZapatoEntity variante = buscarParaActualizar(id);

        if (!variante.isEstaActivo()) throw new IllegalStateException("La variante ya está desactivada.");

        variante.setEstaActivo(false);
        repository.saveAndFlush(variante);
    }

    @Transactional
    public void activar(int id) {
        VarianteZapatoEntity variante = buscarParaActualizar(id);

        if (variante.isEstaActivo()) throw new IllegalStateException("La variante ya está activa.");

        if (!variante.getZapato().isEstaActivo()) throw new IllegalStateException("No se puede activar una variante de un calzado desactivado.");

        variante.setEstaActivo(true);
        repository.saveAndFlush(variante);
    }

    private VarianteZapatoEntity buscar(int id){
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("La variante no existe."));
    }

    private VarianteZapatoEntity buscarParaActualizar(int id) {
        return repository.buscarParaActualizar(id).orElseThrow(() ->
                new IllegalArgumentException("La variante no existe."));
    }

    private ZapatoEntity buscarZapato(int id){
        return zapatos.findById(id).orElseThrow(() -> new IllegalArgumentException("El calzado seleccionado no existe."));
    }

    private String normalizarColor(String color) {
        return color.strip().replaceAll("\\s+", " ").toUpperCase(Locale.ROOT);
    }

    private VarianteZapato aModel(VarianteZapatoEntity entity) {
        VarianteZapato model = new VarianteZapato(
                entity.getIdVariante(),
                entity.getZapato().getIdZapato(),
                entity.getTalla(),
                entity.getColor(),
                entity.getStock(),
                entity.getCreadoEl(),
                entity.getActualizadoEl()
        );

        model.setEstaActivo(entity.isEstaActivo());
        return model;
    }
}
