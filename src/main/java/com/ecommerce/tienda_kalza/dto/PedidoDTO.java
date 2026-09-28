package com.ecommerce.tienda_kalza.dto;

public class PedidoDTO {
    private Long id;
    private String numero;
    private String cliente;
    private String fecha;
    private int cantidadProductos;
    private double total;
    private String estado;
    private String badgeClass;

    public PedidoDTO() {}

    public PedidoDTO(Long id, String numero, String cliente, String fecha,
                     int cantidadProductos, double total, String estado, String badgeClass) {
        this.id = id;
        this.numero = numero;
        this.cliente = cliente;
        this.fecha = fecha;
        this.cantidadProductos = cantidadProductos;
        this.total = total;
        this.estado = estado;
        this.badgeClass = badgeClass;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public int getCantidadProductos() { return cantidadProductos; }
    public void setCantidadProductos(int cantidadProductos) { this.cantidadProductos = cantidadProductos; }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getBadgeClass() { return badgeClass; }
    public void setBadgeClass(String badgeClass) { this.badgeClass = badgeClass; }
}
