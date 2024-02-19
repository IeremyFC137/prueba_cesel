package pe.com.cesel.prueba_cesel.domain.sunat;

import java.util.List;

public record DatosResultadoSunat(
        Boolean success,
        String message,
        String estadoCp,
        String estadoRuc,
        String condDomiRuc,
        List observaciones
) {
}
