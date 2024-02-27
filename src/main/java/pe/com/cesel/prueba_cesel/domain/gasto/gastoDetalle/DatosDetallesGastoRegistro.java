package pe.com.cesel.prueba_cesel.domain.gasto.gastoDetalle;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public record DatosDetallesGastoRegistro(
        @NotNull
        Long gastoId,
        @NotNull
        List<DatosDetalleGastoRegistro> detalles
) {
}
