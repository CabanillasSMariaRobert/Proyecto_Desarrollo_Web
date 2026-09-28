package com.ecommerce.tienda_kalza.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * Todo lo que muestra la vista admin/metricas en una sola llamada.
 */
public class ResumenMetricas {

    private BigDecimal ventasHoy;
    private String variacionVentas;
    private int pedidosPendientes;
    private int usuariosActivos;
    private String variacionUsuarios;
    private int alertasStock;
    private List<PuntoIngreso> ingresos;
    private List<TopProducto> topProductos;
    private List<PedidoAdmin> pedidosRecientes;

    public BigDecimal getVentasHoy() {
        return ventasHoy;
    }

    public void setVentasHoy(BigDecimal ventasHoy) {
        this.ventasHoy = ventasHoy;
    }

    public String getVariacionVentas() {
        return variacionVentas;
    }

    public void setVariacionVentas(String variacionVentas) {
        this.variacionVentas = variacionVentas;
    }

    public int getPedidosPendientes() {
        return pedidosPendientes;
    }

    public void setPedidosPendientes(int pedidosPendientes) {
        this.pedidosPendientes = pedidosPendientes;
    }

    public int getUsuariosActivos() {
        return usuariosActivos;
    }

    public void setUsuariosActivos(int usuariosActivos) {
        this.usuariosActivos = usuariosActivos;
    }

    public String getVariacionUsuarios() {
        return variacionUsuarios;
    }

    public void setVariacionUsuarios(String variacionUsuarios) {
        this.variacionUsuarios = variacionUsuarios;
    }

    public int getAlertasStock() {
        return alertasStock;
    }

    public void setAlertasStock(int alertasStock) {
        this.alertasStock = alertasStock;
    }

    public List<PuntoIngreso> getIngresos() {
        return ingresos;
    }

    public void setIngresos(List<PuntoIngreso> ingresos) {
        this.ingresos = ingresos;
    }

    public List<TopProducto> getTopProductos() {
        return topProductos;
    }

    public void setTopProductos(List<TopProducto> topProductos) {
        this.topProductos = topProductos;
    }

    public List<PedidoAdmin> getPedidosRecientes() {
        return pedidosRecientes;
    }

    public void setPedidosRecientes(List<PedidoAdmin> pedidosRecientes) {
        this.pedidosRecientes = pedidosRecientes;
    }

    /**
     * Barra del grafico de ingresos. porcentaje es la altura relativa en el
     * rango 0-100, destacado marca la barra que se pinta de color primario.
     */
    public static class PuntoIngreso {

        private String etiqueta;
        private BigDecimal monto;
        private int porcentaje;
        private boolean destacado;

        public PuntoIngreso() {
        }

        public PuntoIngreso(String etiqueta, BigDecimal monto, int porcentaje, boolean destacado) {
            this.etiqueta = etiqueta;
            this.monto = monto;
            this.porcentaje = porcentaje;
            this.destacado = destacado;
        }

        public String getEtiqueta() {
            return etiqueta;
        }

        public void setEtiqueta(String etiqueta) {
            this.etiqueta = etiqueta;
        }

        public BigDecimal getMonto() {
            return monto;
        }

        public void setMonto(BigDecimal monto) {
            this.monto = monto;
        }

        public int getPorcentaje() {
            return porcentaje;
        }

        public void setPorcentaje(int porcentaje) {
            this.porcentaje = porcentaje;
        }

        public boolean isDestacado() {
            return destacado;
        }

        public void setDestacado(boolean destacado) {
            this.destacado = destacado;
        }
    }

    /**
     * Producto del ranking de mas vendidos. imagen es solo el nombre del
     * archivo; el path completo lo arma la vista.
     */
    public static class TopProducto {

        private String nombre;
        private String imagen;
        private int unidades;
        private BigDecimal total;

        public TopProducto() {
        }

        public TopProducto(String nombre, String imagen, int unidades, BigDecimal total) {
            this.nombre = nombre;
            this.imagen = imagen;
            this.unidades = unidades;
            this.total = total;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getImagen() {
            return imagen;
        }

        public void setImagen(String imagen) {
            this.imagen = imagen;
        }

        public int getUnidades() {
            return unidades;
        }

        public void setUnidades(int unidades) {
            this.unidades = unidades;
        }

        public BigDecimal getTotal() {
            return total;
        }

        public void setTotal(BigDecimal total) {
            this.total = total;
        }
    }
}
