package pe.com.cesel.prueba_cesel.domain.gasto.validaciones;

import org.springframework.stereotype.Component;
import pe.com.cesel.prueba_cesel.domain.gasto.DatosRegistroGasto;
import jakarta.validation.ValidationException;
import java.time.LocalDateTime;

@Component
public class FechaDeEmisionValido implements ValidadorDeGastos{
    @Override
    public void validar(DatosRegistroGasto datos) {
        LocalDateTime fechaDeEmision = datos.fecha_emision();

        if (fechaDeEmision.isAfter(LocalDateTime.now())) {
            throw new ValidationException("La fecha de emisión es incorrecta");
        }


    }
}
