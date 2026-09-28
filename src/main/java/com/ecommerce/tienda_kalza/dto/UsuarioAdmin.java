package com.ecommerce.tienda_kalza.dto;

import java.time.LocalDate;

/**
 * Usuario tal como lo muestra el panel de administracion.
 *
 * El texto de la fecha ("02 ene, 2026") se arma aca y no en la vista para que
 * el mock no dependa del locale con el que corra la aplicacion.
 */
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

    public UsuarioAdmin() {
    }

    public UsuarioAdmin(Long id, String nombre, String correo, LocalDate fechaRegistro,
                        Integer pedidos, EstadoUsuario estado, String colorClase) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.fechaRegistro = fechaRegistro;
        this.pedidos = pedidos;
        this.estado = estado;
        this.colorClase = colorClase;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Integer getPedidos() {
        return pedidos;
    }

    public void setPedidos(Integer pedidos) {
        this.pedidos = pedidos;
    }

    public EstadoUsuario getEstado() {
        return estado;
    }

    public void setEstado(EstadoUsuario estado) {
        this.estado = estado;
    }

    public String getColorClase() {
        return colorClase;
    }

    public void setColorClase(String colorClase) {
        this.colorClase = colorClase;
    }

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
