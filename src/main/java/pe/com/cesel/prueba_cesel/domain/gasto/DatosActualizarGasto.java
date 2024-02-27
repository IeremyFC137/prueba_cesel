package pe.com.cesel.prueba_cesel.domain.gasto;

import jakarta.validation.constraints.NotNull;
import pe.com.cesel.prueba_cesel.domain.gasto.gastoDetalle.DatosDetalleGastoActualizar;

import java.util.List;

public record DatosActualizarGasto(
        @NotNull
        Long id,
        List<DatosDetalleGastoActualizar> detalles
) {
}
