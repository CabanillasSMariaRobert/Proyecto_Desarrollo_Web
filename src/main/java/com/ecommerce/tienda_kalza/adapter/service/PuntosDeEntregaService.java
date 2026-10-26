package com.ecommerce.tienda_kalza.adapter.service;

import com.ecommerce.tienda_kalza.adapter.entity.PuntoEntregaEntity;
import com.ecommerce.tienda_kalza.adapter.repository.PuntoEntregaRepository;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PuntosDeEntregaService {
    private final PuntoEntregaRepository repository;

    public PuntosDeEntregaService(PuntoEntregaRepository repository) {
        this.repository = repository;
    }

    @Data
    public static class Formulario {
        @NotBlank(message = "Ingresa el nombre del punto de entrega.")
        @Size(max = 255, message = "El nombre no debe superar los 255 caracteres.")
        private String nombre;
        @NotBlank(message = "Ingresa la dirección del punto de entrega.")
        @Size(max = 255, message = "La dirección no debe superar los 255 caracteres.")
        private String direccion;
        @NotBlank(message = "Ingresa una referencia para encontrar el punto.")
        @Size(max = 255, message = "La referencia no debe superar los 255 caracteres.")
        private String referencia;
        @NotNull(message = "Ingresa la latitud.")
        @DecimalMin(value = "-90", message = "La latitud debe estar entre -90 y 90.")
        @DecimalMax(value = "90", message = "La latitud debe estar entre -90 y 90.")
        private Double latitud;
        @NotNull(message = "Ingresa la longitud.")
        @DecimalMin(value = "-180", message = "La longitud debe estar entre -180 y 180.")
        @DecimalMax(value = "180", message = "La longitud debe estar entre -180 y 180.")
        private Double longitud;
        private boolean estaActivo = true;
    }

    public Page<PuntoEntregaEntity> listar(int pagina) {
        var resultado = repository.findAll(PageRequest.of(Math.max(0, pagina), 10, Sort.by("nombre", "id")));
        int ultimaPagina = Math.max(0, resultado.getTotalPages() - 1);
        if (resultado.getNumber() > ultimaPagina) {
            return repository.findAll(PageRequest.of(ultimaPagina, 10, Sort.by("nombre", "id")));
        }
        return resultado;
    }

    @Transactional
    public PuntoEntregaEntity guardar(Integer id, Formulario form) {
        if (form == null) throw new ReglaNegocioException("Ingresa los datos del punto de entrega.");
        validarTexto(form.getNombre(), "nombre");
        validarTexto(form.getDireccion(), "dirección");
        validarTexto(form.getReferencia(), "referencia");
        validarCoordenada(form.getLatitud(), -90, 90, "latitud");
        validarCoordenada(form.getLongitud(), -180, 180, "longitud");
        var punto = id == null ? new PuntoEntregaEntity() : repository.findById(id)
                .orElseThrow(() -> new ReglaNegocioException("El punto de entrega ya no existe."));
        punto.setNombre(form.getNombre().trim());
        punto.setDireccion(form.getDireccion().trim());
        punto.setReferencia(form.getReferencia().trim());
        punto.setLatitud(form.getLatitud());
        punto.setLongitud(form.getLongitud());
        punto.setEstaActivo(form.isEstaActivo());
        return repository.saveAndFlush(punto);
    }

    private void validarTexto(String valor, String campo) {
        if (valor == null || valor.isBlank() || valor.length() > 255) {
            throw new ReglaNegocioException("El campo " + campo + " es obligatorio y admite hasta 255 caracteres.");
        }
    }

    private void validarCoordenada(Double valor, double minimo, double maximo, String campo) {
        if (valor == null || !Double.isFinite(valor) || valor < minimo || valor > maximo) {
            throw new ReglaNegocioException("La " + campo + " debe estar entre " + minimo + " y " + maximo + ".");
        }
    }
}
