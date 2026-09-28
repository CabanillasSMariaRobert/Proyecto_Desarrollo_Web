package com.ecommerce.tienda_kalza.dto.publico;

/** Fila de la tabla de horarios. horario es texto porque "Cerrado" no es una hora. */
public record HorarioAtencion(String dia, String horario) {
}
