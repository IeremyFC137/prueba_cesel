package pe.com.cesel.prueba_cesel.infra.errores;
import jakarta.validation.ConstraintViolation;
import lombok.Getter;
import java.util.Set;

@Getter
public class CustomValidationException extends RuntimeException {
    private final Set<ConstraintViolation<?>> constraintViolations;

    public CustomValidationException(Set<ConstraintViolation<?>> constraintViolations, String message) {
        super(message);
        this.constraintViolations = constraintViolations;
    }

}