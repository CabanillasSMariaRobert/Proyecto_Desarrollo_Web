package com.ecommerce.tienda_kalza.adapter.service;

import com.ecommerce.tienda_kalza.adapter.entity.*;
import com.ecommerce.tienda_kalza.adapter.model.Producto;
import com.ecommerce.tienda_kalza.adapter.repository.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import java.util.*;

@Service @Validated @RequiredArgsConstructor
public class ProductoService {
    private final ProductoRepository repository;
    private final MarcaRepository marcas;
    private final CategoriaRepository categorias;

    @Transactional(readOnly = true)
    public Page<Producto> listar(int pagina, String buscar) {
        return repository.findByNombreContainingIgnoreCase(buscar == null ? "" : buscar.trim(),
                PageRequest.of(Math.max(0, pagina), 10, Sort.by("nombre").and(Sort.by("idProducto"))))
                .map(this::aModel);
    }

    @Transactional(readOnly = true)
    public Producto obtener(int id) { return aModel(buscar(id)); }

    @Transactional
    public void cambiarEstado(int id, boolean activo) {
        ProductoEntity entity = buscar(id);
        entity.setEstaActivo(activo);
        repository.saveAndFlush(entity);
    }

    @Transactional(readOnly = true)
    public Page<Producto> listarDisponibles(int pagina, String buscar) {
        return repository.findByEstaActivoTrueAndNombreContainingIgnoreCase(buscar == null ? "" : buscar.trim(),
                PageRequest.of(Math.max(0, pagina), 10, Sort.by("nombre").and(Sort.by("idProducto"))))
                .map(this::aModel);
    }

    @Transactional(readOnly = true)
    public Producto obtenerDisponible(int id) {
        return repository.findByIdProductoAndEstaActivoTrue(id).map(this::aModel)
                .orElseThrow(() -> new ReglaNegocioException("El producto no está disponible para nuevas compras."));
    }

    @Transactional(readOnly = true)
    public List<CategoriaEntity> opcionesCategorias() { return categorias.findAll(Sort.by("nombre")); }

    @Transactional
    public void guardar(Integer id, @NotNull @Valid Producto model) {
        ProductoEntity entity = id == null ? new ProductoEntity() : buscar(id);
        MarcaEntity marca = marcas.findById(model.getIdMarca())
                .orElseThrow(() -> new IllegalArgumentException("La marca seleccionada no existe."));
        Set<Integer> ids = new LinkedHashSet<>(model.getIdsCategorias());
        if (ids.size() != model.getIdsCategorias().size()) {
            throw new IllegalArgumentException("No se puede asociar la misma categoría más de una vez.");
        }
        List<CategoriaEntity> seleccionadas = categorias.findAllById(ids);
        if (seleccionadas.size() != ids.size()) {
            throw new IllegalArgumentException("Alguna categoría seleccionada no existe.");
        }
        entity.setNombre(model.getNombre().trim());
        entity.setDescripcion(model.getDescripcion() == null ? null : model.getDescripcion().trim());
        entity.setPrecioOriginal(model.getPrecioOriginal());
        entity.setDescuento(model.getDescuento());
        entity.setUrlImagen(model.getUrlImagen());
        entity.setMarca(marca);
        // Conserva las asociaciones existentes para no reinsertar claves compuestas.
        entity.getCategorias().removeIf(rel -> !ids.contains(rel.getCategoria().getIdCategoria()));
        Set<Integer> actuales = new HashSet<>();
        entity.getCategorias().forEach(rel -> actuales.add(rel.getCategoria().getIdCategoria()));
        for (CategoriaEntity categoria : seleccionadas) {
            if (!actuales.contains(categoria.getIdCategoria())) {
                entity.getCategorias().add(CategoriaProductoEntity.builder().producto(entity).categoria(categoria).build());
            }
        }
        repository.saveAndFlush(entity);
    }

    private ProductoEntity buscar(int id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("El producto no existe."));
    }

    private Producto aModel(ProductoEntity entity) {
        Producto model = new Producto();
        model.setIdProducto(entity.getIdProducto());
        model.setEstaActivo(entity.isEstaActivo());
        model.setNombre(entity.getNombre());
        model.setDescripcion(entity.getDescripcion());
        model.setPrecioOriginal(entity.getPrecioOriginal());
        model.setDescuento(entity.getDescuento());
        model.setPrecioConDescuento(entity.getPrecioConDescuento());
        model.setUrlImagen(entity.getUrlImagen());
        model.setIdMarca(entity.getMarca().getIdMarca());
        model.setNombreMarca(entity.getMarca().getNombre());
        model.setIdsCategorias(entity.getCategorias().stream().map(rel -> rel.getCategoria().getIdCategoria()).toList());
        model.setNombresCategorias(entity.getCategorias().stream().map(rel -> rel.getCategoria().getNombre()).sorted().toList());
        model.setCreadoEl(entity.getCreadoEl());
        model.setActualizadoEl(entity.getActualizadoEl());
        return model;
    }
}
