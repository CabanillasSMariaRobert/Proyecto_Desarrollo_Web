package com.ecommerce.tienda_kalza.adapter.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Producto {
    private int idProducto;
    private boolean estaActivo = true;
    private Integer idZapato;
    private String preparacionVenta;
    private boolean disponibleParaCarrito;
    @NotBlank(message = "El nombre es obligatorio.")
    @Size(max = 150, message = "El nombre admite hasta 150 caracteres.")
    private String nombre;
    @Size(max = 2000, message = "La descripción admite hasta 2000 caracteres.")
    private String descripcion;
    @NotNull(message = "El precio es obligatorio.")
    @DecimalMin(value = "0.00", message = "El precio no puede ser negativo.")
    @Digits(integer = 8, fraction = 2, message = "El precio admite 8 enteros y 2 decimales.")
    private BigDecimal precioOriginal;
    @NotNull(message = "El descuento es obligatorio.")
    @DecimalMin(value = "0.00", message = "El descuento debe estar entre 0 y 1.")
    @DecimalMax(value = "1.00", message = "El descuento debe estar entre 0 y 1.")
    @Digits(integer = 1, fraction = 2, message = "El descuento admite hasta 2 decimales.")
    private BigDecimal descuento = BigDecimal.ZERO;
    private BigDecimal precioConDescuento;
    @Size(max = 255, message = "La URL admite hasta 255 caracteres.")
    @Pattern(regexp = "(?i)^(https?://[^\\s]+|/(?!/)[^\\s]*)?$", message = "La imagen debe ser una URL http(s) o una ruta local que comience con /.")
    private String urlImagen;
    @Positive(message = "Selecciona una marca existente.")
    private int idMarca;
    private String nombreMarca;
    private List<String> nombresCategorias;
    private LocalDateTime creadoEl;
    private LocalDateTime actualizadoEl;
    @NotNull
    private List<@NotNull @Positive Integer> idsCategorias = new ArrayList<>();
}
