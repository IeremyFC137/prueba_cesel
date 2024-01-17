package pe.com.cesel.prueba_cesel.domain.usuario;

import pe.com.cesel.prueba_cesel.domain.direccion.DatosDireccion;

public record DatosRespuestaUsuario(
        Long id,
        String nombre,
        String email,
        String telefono,
        String especialidad,
        DatosDireccion direccion
) {
}
