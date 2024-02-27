package pe.com.cesel.prueba_cesel.domain.gasto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import pe.com.cesel.prueba_cesel.domain.gasto.gastoDetalle.DatosDetalleGastoRegistro;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record DatosRegistroGasto(
        @NotNull
        Long idUsuario,
        @NotBlank
        String proveedor,
        @NotBlank
        String ruc,
        @NotNull
        TipoDocumento tipo_documento,
        @NotBlank
        String documento,
        @NotNull
        LocalDateTime fecha_emision,
        @NotNull
        BigDecimal sub_total,
        @NotNull
        BigDecimal igv,
        @NotNull
        Moneda moneda,
        @NotNull
        List<DatosDetalleGastoRegistro> detalles
) {
}
