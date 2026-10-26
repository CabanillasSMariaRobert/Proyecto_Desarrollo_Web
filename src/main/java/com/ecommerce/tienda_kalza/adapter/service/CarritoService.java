package com.ecommerce.tienda_kalza.adapter.service;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CarritoService {
    private static final String CLAVE = "kalza.carrito";
    private final CatalogoService catalogo;

    public record Linea(int idVariante, int idProducto, String nombre, String imagen,
                        int talla, String color, int cantidad, BigDecimal precioUnitario,
                        BigDecimal subtotal, boolean disponible, String aviso) {}
    public record Resumen(List<Linea> items, int totalItems, BigDecimal total, boolean disponible) {}

    @SuppressWarnings("unchecked")
    private Map<Integer, Integer> leer(HttpSession session) {
        Object valor = session.getAttribute(CLAVE);
        return valor == null ? new LinkedHashMap<>() : new LinkedHashMap<>((Map<Integer, Integer>) valor);
    }

    public void agregar(HttpSession session, int idVariante, int cantidad) {
        synchronized (session) {
            Map<Integer, Integer> items = leer(session);
            int actual = items.getOrDefault(idVariante, 0);
            long nueva = (long) actual + cantidad;
            if (cantidad < 1 || nueva > Integer.MAX_VALUE) throw new ReglaNegocioException("La cantidad no es válida.");
            validar(idVariante, (int) nueva);
            items.put(idVariante, (int) nueva);
            session.setAttribute(CLAVE, items);
        }
    }

    public void actualizar(HttpSession session, int idVariante, int cantidad) {
        synchronized (session) {
            Map<Integer, Integer> items = leer(session);
            if (!items.containsKey(idVariante)) throw new ReglaNegocioException("El artículo no está en tu carrito.");
            validar(idVariante, cantidad);
            items.put(idVariante, cantidad);
            session.setAttribute(CLAVE, items);
        }
    }

    public void retirar(HttpSession session, int idVariante) {
        synchronized (session) {
            Map<Integer, Integer> items = leer(session);
            items.remove(idVariante);
            session.setAttribute(CLAVE, items);
        }
    }

    public Resumen consultar(HttpSession session) {
        List<Linea> lineas = new ArrayList<>();
        for (var item : leer(session).entrySet()) {
            try {
                var a = catalogo.articulo(item.getKey());
                boolean disponible = a.stock() >= item.getValue();
                lineas.add(new Linea(a.idVariante(), a.idProducto(), a.nombre(), a.imagen(), a.talla(),
                        a.color(), item.getValue(), a.precio(), a.precio().multiply(BigDecimal.valueOf(item.getValue())),
                        disponible, disponible ? null : "Existencias insuficientes: actualiza la cantidad o retira el artículo."));
            } catch (ReglaNegocioException e) {
                lineas.add(new Linea(item.getKey(), 0, "Artículo no disponible", null, 0, "", item.getValue(),
                        BigDecimal.ZERO, BigDecimal.ZERO, false, e.getMessage()));
            }
        }
        return new Resumen(List.copyOf(lineas), lineas.stream().mapToInt(Linea::cantidad).sum(),
                lineas.stream().map(Linea::subtotal).reduce(BigDecimal.ZERO, BigDecimal::add),
                !lineas.isEmpty() && lineas.stream().allMatch(Linea::disponible));
    }

    public void vaciar(HttpSession session) {
        session.removeAttribute(CLAVE);
    }

    private void validar(int id, int cantidad) {
        if (cantidad < 1) throw new ReglaNegocioException("La cantidad debe ser al menos uno.");
        if (catalogo.articulo(id).stock() < cantidad) throw new ReglaNegocioException("No hay existencias suficientes.");
    }
}
