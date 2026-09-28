package com.ecommerce.tienda_kalza.servicios;

import com.ecommerce.tienda_kalza.dto.publico.DatoContacto;
import com.ecommerce.tienda_kalza.dto.publico.HorarioAtencion;
import com.ecommerce.tienda_kalza.dto.publico.SeccionLegal;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Datos de contacto y textos legales.
 *
 * Privacidad y terminos comparten estructura, asi que ambos usan SeccionLegal
 * y solo cambia el texto. Duplicar los records habria producido dos listas
 * identicas que la vista tiene que recorrer igual.
 */
@Service
public class ContactoService {

    private final List<DatoContacto> canales = List.of(
            new DatoContacto("bi-geo-alt-fill", "Nuestra dirección", "123 Calle Principal, Lima, Perú"),
            new DatoContacto("bi-telephone-fill", "Teléfono", "+51 (123) 456-7890"),
            new DatoContacto("bi-envelope-fill", "Correo electrónico", "info@kalza.com")
    );

    private final List<HorarioAtencion> horarios = List.of(
            new HorarioAtencion("Lunes a viernes", "9:00 AM - 6:00 PM"),
            new HorarioAtencion("Sábado", "9:00 AM - 1:00 PM"),
            new HorarioAtencion("Domingo", "Cerrado")
    );

    private final List<SeccionLegal> privacidad = List.of(
            new SeccionLegal("Información que recopilamos",
                    "Nombre, correo electrónico, dirección de envío, datos de facturación y el historial de tus compras dentro de la tienda."),
            new SeccionLegal("Uso de la información",
                    "Utilizamos tus datos para procesar pedidos, gestionar tu cuenta, mejorar la experiencia de compra y enviarte ofertas si lo autorizas."),
            new SeccionLegal("Seguridad",
                    "Tus datos se almacenan de forma segura y solo tienen acceso a ellos el personal autorizado. No compartimos tu información con terceros sin tu consentimiento."),
            new SeccionLegal("Tus derechos",
                    "Puedes solicitar el acceso, corrección o eliminación de tus datos en cualquier momento escribiéndonos a info@kalza.com.")
    );

    private final List<SeccionLegal> terminos = List.of(
            new SeccionLegal("Pedidos y precios",
                    "Los precios están expresados en soles (S/) e incluyen impuestos. Nos reservamos el derecho de rechazar o cancelar pedidos por errores de stock o de precio."),
            new SeccionLegal("Envíos y entregas",
                    "Los plazos de entrega son estimados y pueden variar según la disponibilidad y la zona de envío. El cliente es responsable de brindar una dirección válida."),
            new SeccionLegal("Devoluciones y garantía",
                    "Aceptamos devoluciones dentro de los 30 días posteriores a la compra siempre que el producto se encuentre en su estado original, sin uso y con su etiqueta."),
            new SeccionLegal("Responsabilidad",
                    "KALZA no es responsable de daños indirectos derivados del uso indebido de los productos o del uso de la plataforma de forma contraria a estas condiciones.")
    );

    public List<DatoContacto> obtenerCanales() {
        return canales;
    }

    public List<HorarioAtencion> obtenerHorarios() {
        return horarios;
    }

    public List<SeccionLegal> obtenerPrivacidad() {
        return privacidad;
    }

    public List<SeccionLegal> obtenerTerminos() {
        return terminos;
    }
}
