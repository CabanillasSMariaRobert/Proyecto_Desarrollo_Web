package com.ecommerce.tienda_kalza.servicios.publico;
import org.springframework.stereotype.Service;
import com.ecommerce.tienda_kalza.dto.publico.*;
import java.util.List;
@Service
public class ContactoService {
    public DatoContacto obtenerDatos() {
        return new DatoContacto("Av. Principal 123", "999888777", "contacto@kalza.com");
    }
    public List<HorarioAtencion> obtenerHorarios() {
        return List.of(new HorarioAtencion("Lunes - Viernes", "9:00 - 18:00"));
    }
    public List<SeccionLegal> obtenerFaqs() {
        return List.of(new SeccionLegal("¿Tienen cambios?", "Sí, dentro de los 7 días."));
    }
}
