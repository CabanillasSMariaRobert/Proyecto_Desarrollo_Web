package com.ecommerce.tienda_kalza.adapter.service;

import com.ecommerce.tienda_kalza.adapter.entity.*;
import com.ecommerce.tienda_kalza.adapter.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CatalogoService {
    private final ZapatoRepository zapatos;
    private final VarianteZapatoRepository variantes;

    public record Ficha(int idProducto, int idZapato, String nombre, String descripcion,
                        String marca, String imagen, BigDecimal precio, List<String> materiales,
                        List<Opcion> variantes) {}
    public record Opcion(int idVariante, int talla, String color, int stock) {}
    public record Articulo(int idVariante, int idProducto, String nombre, String imagen,
                           int talla, String color, int stock, BigDecimal precio) {}

    @Transactional(readOnly = true)
    public Page<Ficha> listar(int pagina, String buscar) {
        return zapatos.buscarPublicos(buscar == null ? "" : buscar.trim(),
                PageRequest.of(Math.max(0, pagina), 12, Sort.by("producto.nombre").and(Sort.by("idZapato"))))
                .map(this::ficha);
    }

    @Transactional(readOnly = true)
    public Ficha detalle(int idProducto) {
        ZapatoEntity zapato = zapatos.findByProducto_IdProducto(idProducto)
                .filter(z -> z.isEstaActivo() && z.getProducto().isEstaActivo())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "El calzado no está disponible."));
        return ficha(zapato);
    }

    @Transactional(readOnly = true)
    public Articulo articulo(int idVariante) {
        VarianteZapatoEntity v = variantes.findById(idVariante)
                .orElseThrow(() -> new ReglaNegocioException("La variante no existe."));
        ZapatoEntity z = v.getZapato();
        ProductoEntity p = z.getProducto();
        if (!v.isEstaActivo() || !z.isEstaActivo() || !p.isEstaActivo()) {
            throw new ReglaNegocioException("El artículo seleccionado está desactivado.");
        }
        return new Articulo(v.getIdVariante(), p.getIdProducto(), p.getNombre(), imagen(p.getUrlImagen()),
                v.getTalla(), v.getColor(), v.getStock(), precio(p));
    }

    private Ficha ficha(ZapatoEntity z) {
        ProductoEntity p = z.getProducto();
        // Un material retirado conserva la composición de los zapatos ya asociados.
        List<String> materiales = z.getMateriales().stream().map(r -> r.getMaterial().getNombre())
                .distinct().sorted().toList();
        List<Opcion> opciones = z.getVariantes().stream().filter(VarianteZapatoEntity::isEstaActivo)
                .sorted(Comparator.comparingInt(VarianteZapatoEntity::getTalla)
                        .thenComparing(VarianteZapatoEntity::getColor))
                .map(v -> new Opcion(v.getIdVariante(), v.getTalla(), v.getColor(), v.getStock())).toList();
        return new Ficha(p.getIdProducto(), z.getIdZapato(), p.getNombre(), p.getDescripcion(),
                p.getMarca().getNombre(), imagen(p.getUrlImagen()), precio(p), materiales, opciones);
    }

    private BigDecimal precio(ProductoEntity p) {
        return p.getPrecioOriginal().multiply(BigDecimal.ONE.subtract(p.getDescuento()))
                .setScale(2, java.math.RoundingMode.HALF_UP);
    }

    private String imagen(String ruta) {
        if (ruta == null || ruta.isBlank()) return null;
        if (ruta.startsWith("/") || ruta.startsWith("https://") || ruta.startsWith("http://")) return ruta;
        return "/img/productos/" + ruta;
    }
}
