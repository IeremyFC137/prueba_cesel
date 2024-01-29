package pe.com.cesel.prueba_cesel.domain.gasto.validaciones;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import pe.com.cesel.prueba_cesel.domain.gasto.DatosRegistroGasto;
import pe.com.cesel.prueba_cesel.domain.gasto.Moneda;

@Component
public class TipoDeMonedaValido implements ValidadorDeGastos{

    @Override
    public void validar(DatosRegistroGasto datos) {
        var moneda = datos.moneda();

        if (moneda != Moneda.SOLES && moneda != Moneda.DOLARES) {
            throw new HttpMessageNotReadableException("La moneda debe ser SOLES o DOLARES");
        }
    }
}
