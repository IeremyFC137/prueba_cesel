package pe.com.cesel.prueba_cesel.domain.gasto.validaciones;

import jakarta.validation.ValidationException;
import org.springframework.stereotype.Component;
import pe.com.cesel.prueba_cesel.domain.gasto.DatosRegistroGasto;
import pe.com.cesel.prueba_cesel.domain.gasto.gastoDetalle.DatosDetalleGastoRegistro;

import java.math.BigDecimal;

@Component
public class MontosValido implements ValidadorDeGastos{
    @Override
    public void validar(DatosRegistroGasto datos) {
        var subTotal = datos.sub_total();
        var igv = datos.igv();

        if(subTotal.compareTo(BigDecimal.ZERO)<0){
            throw new ValidationException("El subtotal debe ser un monto positivo mayor que 0");
        }
        if(igv.compareTo(BigDecimal.ZERO)<0){
            throw new ValidationException("El igv debe ser un monto positivo mayor que 0");
        }

        for (DatosDetalleGastoRegistro detalle : datos.detalles()) {

            if (detalle.importe().compareTo(BigDecimal.ZERO) < 0) {
                throw new ValidationException("El importe del detalle debe ser un monto positivo mayor que 0");
            }
            if (detalle.p_importe().compareTo(BigDecimal.ZERO) < 0 || detalle.p_importe().compareTo(BigDecimal.ONE) > 0) {
                throw new ValidationException("El porcentaje de importe del detalle debe ser un número que se encuentre en el rango de 0 a 1");
            }

        }

    }

}
