package com.ecommerce.tienda_kalza.adapter.service;
import com.ecommerce.tienda_kalza.adapter.model.Categoria;
import com.ecommerce.tienda_kalza.adapter.entity.CategoriaEntity;
import com.ecommerce.tienda_kalza.adapter.repository.CategoriaRepository;

import org.springframework.data.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoriaService {
    private final CategoriaRepository repository;

    // CREATE
    @Transactional
    public void crear(Categoria model){
        String nombre = model.getNombre().trim();
        boolean duplicado = repository.existsByNombreIgnoreCase(nombre);
        if (duplicado) throw new IllegalStateException(mensajeDuplicado(nombre));

        CategoriaEntity entity = new CategoriaEntity();
        entity.setNombre(nombre);
        entity.setDescripcion(limpiar(model.getDescripcion()));
        repository.save(entity);
    }

    // READ (uno)
    @Transactional(readOnly = true)
    public Categoria obtener(int id){
        return aModel(buscar(id));
    }

    // READ (listar)
    @Transactional(readOnly = true)
    public Page<Categoria> listar(int pagina, int tamano){
        Pageable pag = PageRequest.of(Math.max(pagina, 0), tamano, Sort.by("nombre"));
        return repository.findAll(pag).map(this::aModel);
    }

    // UPDATE
    @Transactional
    public void actualizar(Integer id, Categoria model){
        CategoriaEntity entity = buscar(id);

        String nombre = model.getNombre().trim();
        boolean duplicado = repository.existsByNombreIgnoreCaseAndIdCategoriaNot(nombre, id);
        if (duplicado) throw new IllegalStateException(mensajeDuplicado(nombre));

        entity.setNombre(nombre);
        entity.setDescripcion(limpiar(model.getDescripcion()));
        repository.save(entity);
    }

    // DELETE
    @Transactional
    public boolean eliminar(int id){
        CategoriaEntity categoria = buscar(id);

        if (categoria.getEstaActivo()) {
            Integer productos = repository.contarProductos(id);
            if (productos > 0) {
                throw new IllegalStateException("No se puede desactivar la categoría \""
                        + categoria.getNombre() + "\": tiene " + productos + " producto(s) asociado(s).");
            }
            categoria.setEstaActivo(false);
        } else {
            categoria.setEstaActivo(true);
        }
        repository.save(categoria);
        return categoria.getEstaActivo();
    }

    private CategoriaEntity buscar(int id){
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("La categoria no existe."));
    }

    private String mensajeDuplicado(String nombre){
        String mensaje = "Ya existe una categoría con el nombre \"" + nombre + "\".";
        return mensaje;
    }

    private String limpiar(String texto){
        String resultado = texto == null || texto.isBlank() ? null : texto.trim();
        return resultado;
    }

    private Categoria aModel(CategoriaEntity entity){
        Categoria model = new Categoria();
        model.setIdCategoria(entity.getIdCategoria());
        model.setNombre(entity.getNombre());
        model.setDescripcion(entity.getDescripcion());
        model.setCantidadProductos(repository.contarProductos(entity.getIdCategoria()));
        return model;
    }
}
