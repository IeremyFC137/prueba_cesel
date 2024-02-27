package pe.com.cesel.prueba_cesel.domain.gasto;

import pe.com.cesel.prueba_cesel.domain.gasto.gastoDetalle.GastoDetalle;
import pe.com.cesel.prueba_cesel.domain.gasto.gastoDetalle.ResultadoDetalleGasto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public record ResultadoRegistroGasto(
        Long id,
        Long idUsuario,
        String proveedor,
        String ruc,
        TipoDocumento tipo_documento,
        String documento,
        LocalDateTime fecha_emision,
        BigDecimal sub_total,
        BigDecimal igv,
        Moneda moneda,
        List<ResultadoDetalleGasto> detalles,
        String estado,
        List<String> images
) {
    public ResultadoRegistroGasto(Gasto gasto){
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
                gasto.getMoneda(),
                gasto.getDetallesGasto().stream().map(detalle ->
                        new ResultadoDetalleGasto(
                                detalle.getId(),
                                detalle.getC_costo(),
                                detalle.getC_gasto(),
                                detalle.getC_contable(),
                                detalle.getImporte(),
                                detalle.getP_importe()
                        )
                ).collect(Collectors.toList()),
                "Registro",
                new ArrayList<String>());
    }

}
