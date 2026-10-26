package com.ecommerce.tienda_kalza.adapter.service;

import com.ecommerce.tienda_kalza.adapter.entity.MaterialEntity;
import com.ecommerce.tienda_kalza.adapter.entity.ZapatoEntity;
import com.ecommerce.tienda_kalza.adapter.entity.ZapatoMaterialEntity;
import com.ecommerce.tienda_kalza.adapter.model.ZapatoMaterial;
import com.ecommerce.tienda_kalza.adapter.repository.MaterialRepository;
import com.ecommerce.tienda_kalza.adapter.repository.ZapatoMaterialRepository;
import com.ecommerce.tienda_kalza.adapter.repository.ZapatoRepository;
import com.ecommerce.tienda_kalza.adapter.utils.ZapatoMaterialEntityId;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
public class ZapatoMaterialService {
    private final ZapatoMaterialRepository repository;
    private final ZapatoRepository zapatos;
    private final MaterialRepository materiales;

    // CREATE
    @Transactional
    public void asignar(@NotNull @Valid ZapatoMaterial model){
        ZapatoEntity zapato = buscarZapato(model.getIdZapato());

        MaterialEntity material = materiales.findById(model.getIdMaterial()).orElseThrow(() -> new IllegalArgumentException("El material seleccionado no existe."));

        if(!zapato.isEstaActivo()) throw new IllegalStateException("No se pueden asignar materiales a un calzado desactivado.");

        if(!material.isEstaActivo()) throw  new IllegalStateException("No se puede asignar un material desactivado.");

        ZapatoMaterialEntityId id = new ZapatoMaterialEntityId(zapato.getIdZapato(), material.getIdMaterial());

        if(repository.existsById(id)) throw new IllegalStateException("El material ya está asociado a este calzado");

        ZapatoMaterialEntity asociacion = new ZapatoMaterialEntity();

        asociacion.setId(id);
        asociacion.setZapato(zapato);
        asociacion.setMaterial(material);
        ZapatoMaterialEntity guardada = repository.save(asociacion);
        // Mantiene ambos lados sincronizados cuando se consultan en la misma transacción.
        if (zapato.getMateriales().stream().noneMatch(a -> a.getId().equals(id))) {
            zapato.getMateriales().add(guardada);
        }
        if (material.getMateriales().stream().noneMatch(a -> a.getId().equals(id))) {
            material.getMateriales().add(guardada);
        }
        repository.flush();
    }

    // READ (lista)
    @Transactional(readOnly = true)
    public List<ZapatoMaterial> listarPorZapato(int idZapato) {
        buscarZapato(idZapato);

        return repository.findByZapato_IdZapato(idZapato).stream().map(this::aModel).toList();
    }

    // RETIRAR ASOCIACIÓN
    @Transactional
    public void retirar(int idZapato, int idMaterial) {
        ZapatoMaterialEntityId id = new ZapatoMaterialEntityId(idZapato, idMaterial);

        ZapatoMaterialEntity asociacion = repository.findById(id).orElseThrow(() ->
                        new IllegalArgumentException("La asociación entre el calzado y el material no existe."));

        // Evita que el cascade del padre vuelva a persistir una asociación retirada.
        asociacion.getZapato().getMateriales().removeIf(a -> a.getId().equals(id));
        asociacion.getMaterial().getMateriales().removeIf(a -> a.getId().equals(id));
        repository.delete(asociacion);
        repository.flush();
    }

    private ZapatoEntity buscarZapato(int id){
        return zapatos.findById(id).orElseThrow(() -> new IllegalArgumentException("El calzado seleccionado no existe."));
    }

    private ZapatoMaterial aModel(ZapatoMaterialEntity entity){
        return new ZapatoMaterial(
                entity.getZapato().getIdZapato(),
                entity.getMaterial().getIdMaterial()
        );
    }
}
