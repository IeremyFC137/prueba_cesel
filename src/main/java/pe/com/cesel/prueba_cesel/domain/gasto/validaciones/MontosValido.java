package pe.com.cesel.prueba_cesel.domain.gasto.validaciones;

import jakarta.validation.ValidationException;
import org.springframework.stereotype.Component;
import pe.com.cesel.prueba_cesel.domain.gasto.DatosRegistroGasto;

import java.math.BigDecimal;

@Component
public class MontosValido implements ValidadorDeGastos{
    @Override
    public void validar(DatosRegistroGasto datos) {
        var subTotal = datos.sub_total();
        var igv = datos.igv();
        var importe = datos.importe();
        var pImporte = datos.p_importe();

        if(subTotal.compareTo(BigDecimal.ZERO)<0){
            throw new ValidationException("El subtotal debe ser un monto positivo mayor que 0");
        }
        if(igv.compareTo(BigDecimal.ZERO)<0){
            throw new ValidationException("El igv debe ser un monto positivo mayor que 0");
        }
        if(importe.compareTo(BigDecimal.ZERO)<0){
            throw new ValidationException("El importe debe ser un monto positivo mayor que 0");
        }
        if(pImporte.compareTo(BigDecimal.ZERO)<0 || pImporte.compareTo(BigDecimal.ONE) > 0){
            throw new ValidationException("El porcentaje de importe debe ser un numero que se encuentre en el rango de 0 a 1");
        }

    }

}
