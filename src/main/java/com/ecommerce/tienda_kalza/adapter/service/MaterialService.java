package com.ecommerce.tienda_kalza.adapter.service;

import com.ecommerce.tienda_kalza.adapter.entity.MaterialEntity;
import com.ecommerce.tienda_kalza.adapter.model.Material;
import com.ecommerce.tienda_kalza.adapter.repository.MaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Service
@Validated
@RequiredArgsConstructor
public class MaterialService {
    private final MaterialRepository repository;

    // CREATE-UPDATE
    @Transactional
    public void guardar(Integer id, @NotNull @Valid Material model){
        MaterialEntity entity = id == null ? new MaterialEntity() : buscar(id);

        String nombre = model.getNombre().trim();

        boolean duplicado = repository.existsByNombreIgnoreCaseAndIdMaterialNot(nombre, entity.getIdMaterial());

        if (duplicado) throw new IllegalStateException("Ya existe un material con ese nombre.");

        entity.setNombre(nombre);
        repository.saveAndFlush(entity);
    }

    // READ (uno)
    @Transactional(readOnly = true)
    public Material obtener(int id){
        return aModel(buscar(id));
    }

    // READ (listar)
    @Transactional(readOnly = true)
    public Page<Material> listar(int pagina){
        PageRequest paginacion = PageRequest.of(Math.max(0, pagina), 10, Sort.by("nombre").and(Sort.by("idMaterial")));
        return repository.findAll(paginacion).map(this::aModel);
    }

    // DELETE
    @Transactional
    public void desactivar(int id){
        MaterialEntity material = buscar(id);

        if(!material.isEstaActivo()) throw new IllegalStateException("El material ya está desactivado.");

        material.setEstaActivo(false);
        repository.saveAndFlush(material);
    }

    private MaterialEntity buscar(int id){
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("El material no existe."));
    }

    private Material aModel(MaterialEntity entity) {
        Material model = new Material(
                entity.getIdMaterial(),
                entity.getNombre()
        );
        model.setEstaActivo(entity.isEstaActivo());
        return model;
    }
}
