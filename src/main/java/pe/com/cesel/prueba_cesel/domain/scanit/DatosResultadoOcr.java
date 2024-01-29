package pe.com.cesel.prueba_cesel.domain.scanit;

import java.math.BigDecimal;

public record DatosResultadoOcr
        (
                String proveedor,
                String ruc,
                String tipo_documento,
                String documento,
                String fecha_emision,
                String moneda,
                BigDecimal sub_total,
                BigDecimal igv,
                BigDecimal total
         ) {
}
