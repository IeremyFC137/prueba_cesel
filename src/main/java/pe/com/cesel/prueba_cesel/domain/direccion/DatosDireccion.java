package pe.com.cesel.prueba_cesel.domain.direccion;

import jakarta.validation.constraints.NotBlank;

public record DatosDireccion(

        @NotBlank
        String calle,
        @NotBlank
        String distrito,
        @NotBlank
        String numero,
        @NotBlank
        String ciudad,
        @NotBlank
        String complemento) {
}
