package pe.com.cesel.prueba_cesel.infra.security;

import java.util.List;

public record DatosAutenticacion(
        Long id,
        String email,
        String fullName,
        Boolean isActive,
        List<String> roles,
        String token) {
}
