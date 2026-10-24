package com.ecommerce.tienda_kalza.adapter.service;

import com.ecommerce.tienda_kalza.adapter.entity.MarcaEntity;
import com.ecommerce.tienda_kalza.adapter.model.Marca;
import com.ecommerce.tienda_kalza.adapter.repository.MarcaRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import java.util.List;

@Service @Validated @RequiredArgsConstructor
public class MarcaService {
    private final MarcaRepository repository;

    @Transactional(readOnly = true)
    public Page<MarcaEntity> listar(int pagina) {
        return repository.findAll(PageRequest.of(Math.max(0, pagina), 10, Sort.by("nombre").and(Sort.by("idMarca"))));
    }

    @Transactional(readOnly = true)
    public List<MarcaEntity> opciones() { return repository.findAll(Sort.by("nombre")); }

    @Transactional(readOnly = true)
    public Marca obtener(int id) {
        MarcaEntity entity = buscar(id);
        Marca model = new Marca();
        model.setIdMarca(id);
        model.setNombre(entity.getNombre());
        model.setDescripcion(entity.getDescripcion());
        return model;
    }

    @Transactional
    public void guardar(Integer id, @NotNull @Valid Marca model) {
        MarcaEntity entity = id == null ? new MarcaEntity() : buscar(id);
        String nombre = model.getNombre().trim();
        if (repository.existsByNombreIgnoreCaseAndIdMarcaNot(nombre, entity.getIdMarca())) {
            throw new IllegalStateException("Ya existe una marca con ese nombre.");
        }
        entity.setNombre(nombre);
        entity.setDescripcion(model.getDescripcion() == null ? null : model.getDescripcion().trim());
        repository.saveAndFlush(entity);
    }

    private MarcaEntity buscar(int id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("La marca no existe."));
    }
}
