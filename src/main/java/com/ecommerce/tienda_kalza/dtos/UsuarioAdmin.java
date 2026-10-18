package com.ecommerce.tienda_kalza.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Usuario tal como lo muestra el panel de administracion.
 *
 * El texto de la fecha ("02 ene, 2026") se arma aca y no en la vista para que
 * el mock no dependa del locale con el que corra la aplicacion.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioAdmin {

    private static final String[] MESES = {
            "ene", "feb", "mar", "abr", "may", "jun",
            "jul", "ago", "sep", "oct", "nov", "dic"
    };

    private Long id;
    private String nombre;
    private String correo;
    private LocalDate fechaRegistro;
    private Integer pedidos;
    private EstadoUsuario estado;
    private String colorClase;

    /** Iniciales para el avatar: primera letra del nombre y del apellido. */
    public String getIniciales() {
        if (nombre == null || nombre.isBlank()) {
            return "";
        }
        String[] partes = nombre.trim().split("\\s+");
        StringBuilder iniciales = new StringBuilder();
        for (String parte : partes) {
            if (iniciales.length() == 2) {
                break;
            }
            iniciales.append(Character.toUpperCase(parte.charAt(0)));
        }
        return iniciales.toString();
    }

    /** Fecha en el formato corto que usa la tabla: "02 ene, 2026". */
    public String getFechaCorta() {
        if (fechaRegistro == null) {
            return "";
        }
        return String.format("%02d %s, %d",
                fechaRegistro.getDayOfMonth(),
                MESES[fechaRegistro.getMonthValue() - 1],
                fechaRegistro.getYear());
    }
}
