package pe.com.cesel.prueba_cesel.domain.gasto.validaciones;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import pe.com.cesel.prueba_cesel.domain.gasto.DatosRegistroGasto;
import pe.com.cesel.prueba_cesel.domain.gasto.TipoDocumento;

@Component
public class TipoDeDocumentoValido implements ValidadorDeGastos{

    @Override
    public void validar(DatosRegistroGasto datos) {
        var tipoDocumento = datos.tipo_documento();

        if (tipoDocumento != TipoDocumento.BOLETA && tipoDocumento != TipoDocumento.FACTURA) {
            throw new HttpMessageNotReadableException("El tipo de documento debe ser BOLETA o FACTURA");
        }
    }
}
