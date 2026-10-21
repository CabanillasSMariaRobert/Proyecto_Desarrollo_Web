package com.ecommerce.tienda_kalza.adapter.service;

import com.ecommerce.tienda_kalza.adapter.enums.EstadoEntrega;
import com.ecommerce.tienda_kalza.adapter.model.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntregasService {
    public List<Entrega> listar(){
        // ES SERVICIO NO ESTA USANDO EL REPOSITORIO DE ENTREGAS PORQUE
        // NECESITA TENER LAS INTERFACES DE COMPRAS Y PUNTO DE ENTREGA COMPLETAS
        // PARA TENER DATOS LOS CUALES MOSTRAR POR PANTALLA
        Compra compra = new Compra();
        Usuario cliente = new Usuario();
        cliente.setNombres("Alfonso");
        cliente.setNombres("Otarola");
        compra.setCliente(cliente);
        PuntoEntrega puntoDeEntrega = new PuntoEntrega("Central", "Av. Izaguirre", "MegaPlaza", 10.255, 20.555);
        return List.of(
                new Entrega(compra, puntoDeEntrega, EstadoEntrega.EN_CAMINO),
                new Entrega(compra, puntoDeEntrega, EstadoEntrega.PENDIENTE)
        );
    }

    public List<Entrega> listarPorUsuario(int idUsuario){
        // ES SERVICIO NO ESTA USANDO EL REPOSITORIO DE ENTREGAS PORQUE
        // NECESITA TENER LAS INTERFACES DE COMPRAS Y PUNTO DE ENTREGA COMPLETAS
        // PARA TENER DATOS LOS CUALES MOSTRAR POR PANTALLA
        Compra compra = new Compra();
        PuntoEntrega puntoDeEntrega = new PuntoEntrega("Central", "Av. Izaguirre", "MegaPlaza", 10.255, 20.555);
        return List.of(
                new Entrega(compra, puntoDeEntrega, EstadoEntrega.EN_CAMINO),
                new Entrega(compra, puntoDeEntrega, EstadoEntrega.PENDIENTE)
        );
    }
}
