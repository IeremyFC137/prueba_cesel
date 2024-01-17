package pe.com.cesel.prueba_cesel.domain.gasto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
        BigDecimal importe,
        @NotNull
        BigDecimal p_importe,
        @NotNull
        Moneda moneda,
        @NotBlank
        String c_costo,
        @NotBlank
        String c_gasto,
        @NotBlank
        String c_contable

) {
}
