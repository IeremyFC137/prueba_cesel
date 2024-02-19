package pe.com.cesel.prueba_cesel.domain.sunat;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record DatosValidarComprobante(
        @NotBlank
        String numRuc,
        @NotBlank
        String codComp,
        @NotBlank
        String numeroSerie,
        @NotBlank
        String numero,
        @NotBlank
        String fechaEmision,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        BigDecimal monto
) {
}
