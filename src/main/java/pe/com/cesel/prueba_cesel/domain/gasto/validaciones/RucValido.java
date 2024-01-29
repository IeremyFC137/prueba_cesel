package pe.com.cesel.prueba_cesel.domain.gasto.validaciones;

import jakarta.validation.ValidationException;
import org.springframework.stereotype.Component;
import pe.com.cesel.prueba_cesel.domain.gasto.DatosRegistroGasto;

@Component
public class RucValido implements ValidadorDeGastos{

    private static final String RUC_PATTERN = "\\d{11}";
    @Override
    public void validar(DatosRegistroGasto datos) {
        var ruc = datos.ruc();

        if (!ruc.matches(RUC_PATTERN)) {
            throw new ValidationException("El RUC no está en el formato correcto");
        }

    }
}
