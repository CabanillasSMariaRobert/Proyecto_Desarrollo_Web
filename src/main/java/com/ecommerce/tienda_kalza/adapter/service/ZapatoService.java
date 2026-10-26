package com.ecommerce.tienda_kalza.adapter.service;

import com.ecommerce.tienda_kalza.adapter.entity.ZapatoEntity;
import com.ecommerce.tienda_kalza.adapter.entity.ProductoEntity;
import com.ecommerce.tienda_kalza.adapter.model.Zapato;
import com.ecommerce.tienda_kalza.adapter.repository.ProductoRepository;
import com.ecommerce.tienda_kalza.adapter.repository.ZapatoRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@RequiredArgsConstructor
public class ZapatoService {
    private final ZapatoRepository repository;
    private final ProductoRepository productos;

    // CREATE-UPDATE
    @Transactional
    public void guardar(Integer id, @NotNull @Valid Zapato model){
        ZapatoEntity entity = id == null ? new ZapatoEntity() : buscar(id);

        ProductoEntity producto = productos.findById(model.getIdProducto()).orElseThrow(() -> new IllegalArgumentException("El producto seleccionado no existe."));

        boolean duplicado = repository.existsByProducto_IdProductoAndIdZapatoNot(producto.getIdProducto(), entity.getIdZapato());

        if (duplicado) throw new IllegalStateException("Ya existe un zapato vinculado a ese producto.");

        if (id != null && entity.getProducto().getIdProducto() != producto.getIdProducto())
            throw new IllegalStateException("No se puede cambiar el producto de un calzado ya registrado.");

        entity.setProducto(producto);
        repository.saveAndFlush(entity);
    }

    // READ (uno)
    @Transactional(readOnly = true)
    public Zapato obtener(int id) { return aModel(buscar(id)); }

    // READ (listar)
    @Transactional(readOnly = true)
    public Page<Zapato> listar(int pagina){
        PageRequest paginacion = PageRequest.of(Math.max(0, pagina), 10, Sort.by("idZapato").descending());
        return repository.findAll(paginacion).map(this::aModel);
    }

    // DELETE
    @Transactional
    public void desactivar(int id){
        ZapatoEntity zapato = buscar(id);
        if(!zapato.isEstaActivo()) throw new IllegalStateException("El zapato ya esta desactivado.");

        zapato.setEstaActivo(false);
        repository.saveAndFlush(zapato);
    }

    private ZapatoEntity buscar(int id){
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("El zapato no existe."));
    }

    private Zapato aModel(ZapatoEntity entity){
        Zapato model = new Zapato(
                entity.getIdZapato(),
                entity.getCreadoEl(),
                entity.getActualizadoEl(),
                entity.getProducto().getIdProducto()
        );
        model.setEstaActivo(entity.isEstaActivo());
        return model;
    }
}
