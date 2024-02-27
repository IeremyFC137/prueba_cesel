package pe.com.cesel.prueba_cesel.domain.gasto.gastoDetalle;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record DatosDetalleGastoRegistro(
        @JsonInclude(JsonInclude.Include.NON_NULL)
        Long gastoId,
        @NotBlank String c_costo,
        @NotBlank String c_gasto,
        @NotBlank String c_contable,
        @NotNull BigDecimal importe,
        @NotNull BigDecimal p_importe
) {}