package pe.com.cesel.prueba_cesel.domain.gasto.gastoDetalle;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
public record DatosDetalleGastoActualizar(
        @NotNull
        Long detalleId,
        String c_costo,
        String c_gasto,
        String c_contable,
        BigDecimal importe,
        BigDecimal p_importe) {}
