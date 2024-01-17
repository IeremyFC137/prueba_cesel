package pe.com.cesel.prueba_cesel.domain.usuario;


import jakarta.validation.constraints.NotNull;
import pe.com.cesel.prueba_cesel.domain.direccion.DatosDireccion;

public record DatosActualizarUsuario(
        @NotNull Long id,
        String nombre,
        String documento,
        DatosDireccion direccion) {
}
