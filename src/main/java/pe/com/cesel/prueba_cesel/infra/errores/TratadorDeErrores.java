package pe.com.cesel.prueba_cesel.infra.errores;

import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;
import java.util.Collections;
import java.util.List;

@RestControllerAdvice
public class TratadorDeErrores {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity tratarError404(){
        return ResponseEntity.notFound().build();
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity tratarError400(MethodArgumentNotValidException e){

        var errores = e.getFieldErrors().stream().map(DatosErrorValidacion::new).toList();
        return ResponseEntity.badRequest().body(
                new ErrorDetails(
                        400,
                        errores,
                        e.getClass().getSimpleName()
                )
        );
    }
    @ExceptionHandler(AuthenticationInvalid.class)
    public ResponseEntity errorHandlerAuthenticationInvalid(Exception  e){
        return ResponseEntity.status(401).body(ErrorDetails.fromException(e,401));
    }

    private record DatosErrorValidacion(String campo, String error){
        public DatosErrorValidacion(FieldError error){
            this(
                    error.getField(),
                    error.getDefaultMessage()
            );
        }
    }

    @ExceptionHandler(ValidacionDeIntegridad.class)
    public ResponseEntity errorHandlerValidacionesIntegridad(Exception e){
        return ResponseEntity.badRequest().body(ErrorDetails.fromException(e,400));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity errorHandlerValidacionesDeNegocio(Exception e){
        return ResponseEntity.badRequest().body(ErrorDetails.fromException(e,400));
    }

    public record ErrorDetails(int statusCode, List message, String error) {

        public static ErrorDetails fromException(Exception exception, int statusCode) {
            return new ErrorDetails(
                    statusCode,
                    Collections.singletonList(exception.getMessage()),
                    exception.getClass().getSimpleName()
            );
        }

    }

}
