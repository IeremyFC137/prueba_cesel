package pe.com.cesel.prueba_cesel.domain.gasto;

import pe.com.cesel.prueba_cesel.domain.usuario.Usuario;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DatosDetalleGasto(
        Long id,
        Long idUsuario,
        String proveedor,
        String ruc,
        LocalDateTime fecha_emision,
        BigDecimal total,
        String estado
) {
    public DatosDetalleGasto (Gasto gasto){
        this(
                gasto.getId(),
                gasto.getUsuario().getId(),
                gasto.getProveedor(),
                gasto.getRuc(),
                gasto.getFecha_emision(),
                gasto.getSub_total().add(gasto.getIgv()),
                "Registro");
    }


}
