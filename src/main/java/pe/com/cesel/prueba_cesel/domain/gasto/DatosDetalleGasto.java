package pe.com.cesel.prueba_cesel.domain.gasto;

import pe.com.cesel.prueba_cesel.domain.usuario.Usuario;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DatosDetalleGasto(
        Long id,
        Long idUsuario,
        String proveedor,
        String ruc,
        TipoDocumento tipo_documento,
        String documento,
        LocalDateTime fecha_emision,
        BigDecimal sub_total,
        BigDecimal igv,
        BigDecimal importe,
        BigDecimal p_importe,
        Moneda moneda,
        String c_costo,
        String c_gasto,
        String c_contable,
        String estado
) {
    public DatosDetalleGasto (Gasto gasto){
        this(
                gasto.getId(),
                gasto.getUsuario().getId(),
                gasto.getProveedor(),
                gasto.getRuc(),
                gasto.getTipo_documento(),
                gasto.getDocumento(),
                gasto.getFecha_emision(),
                gasto.getSub_total(),
                gasto.getIgv(),
                gasto.getImporte(),
                gasto.getP_importe(),
                gasto.getMoneda(),
                gasto.getC_costo(),
                gasto.getC_gasto(),
                gasto.getC_contable(),
                "Registro");
    }


}
