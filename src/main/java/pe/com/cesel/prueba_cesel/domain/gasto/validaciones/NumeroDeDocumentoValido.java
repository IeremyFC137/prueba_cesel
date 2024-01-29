package pe.com.cesel.prueba_cesel.domain.gasto.validaciones;

import jakarta.validation.ValidationException;
import org.springframework.stereotype.Component;
import pe.com.cesel.prueba_cesel.domain.gasto.DatosRegistroGasto;

@Component
public class NumeroDeDocumentoValido implements ValidadorDeGastos{

    private static final String DOCUMENTO_PATTERN = "\\b([FBfb]\\d{3}-\\d{3,10}|\\d{1,5}-\\d{5,10})\\b";
    @Override
    public void validar(DatosRegistroGasto datos) {
        var numeroDocumento = datos.documento();

        if (!numeroDocumento.matches(DOCUMENTO_PATTERN)) {
            throw new ValidationException("El número de documento no está en el formato correcto");
        }

    }
}
