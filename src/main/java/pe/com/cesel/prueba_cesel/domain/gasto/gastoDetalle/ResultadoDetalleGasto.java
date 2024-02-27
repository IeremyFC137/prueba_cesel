package pe.com.cesel.prueba_cesel.domain.gasto.gastoDetalle;

import java.math.BigDecimal;

public record ResultadoDetalleGasto(
        Long id,
        String c_costo,
        String c_gasto,
        String c_contable,
        BigDecimal importe,
        BigDecimal p_importe
) {
    public ResultadoDetalleGasto(GastoDetalle gastoDetalle){
        this(
                gastoDetalle.getId(),
                gastoDetalle.getC_costo(),
                gastoDetalle.getC_gasto(),
                gastoDetalle.getC_contable(),
                gastoDetalle.getImporte(),
                gastoDetalle.getP_importe()
        );
    };
}
