package pe.com.cesel.prueba_cesel.domain.gasto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record DatosActualizarGasto(
        @NotNull Long id,
        String c_costo,
        String c_gasto,
        String c_contable,
        BigDecimal importe,
        BigDecimal p_importe
) {
}
