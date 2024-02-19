package pe.com.cesel.prueba_cesel.infra.errores;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

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

    @ExceptionHandler(InvalidRequestError.class)
    public ResponseEntity errorHandlerRequestError(Exception e){
        return ResponseEntity.status(400).body(ErrorDetails.fromException(e,400));
    }

    @ExceptionHandler(ValidacionDeImagen.class)
    public ResponseEntity errorHandlerValidacionDeIntegridad(Exception e){
        return ResponseEntity.badRequest().body(ErrorDetails.fromException(e,400));
    }

    @ExceptionHandler(ValidacionDeIntegridad.class)
    public ResponseEntity errorHandlerValidacionesIntegridad(Exception e){
        return ResponseEntity.badRequest().body(ErrorDetails.fromException(e,400));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity errorHandlerValidacionesDeNegocio(Exception e){
        return ResponseEntity.badRequest().body(ErrorDetails.fromException(e,400));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity errorHandlerHttpMessageNotReadableException(Exception e){
        return ResponseEntity.badRequest().body(ErrorDetails.fromException(e,400));
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity handleMissingServletRequestPart(MissingServletRequestPartException e) {
        return ResponseEntity.badRequest().body(ErrorDetails.fromException(e,400));
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity handleMultipartException(MultipartException e) {
        return ResponseEntity.badRequest().body(ErrorDetails.fromException(e,400));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity handleAccessDeniedException(AccessDeniedException e) {
        return ResponseEntity.status(403).body(ErrorDetails.fromException(e,403));
    }

    @ExceptionHandler(NotFoundEntity.class)
    public ResponseEntity handleNotFoundEntity(NotFoundEntity e) {
        return ResponseEntity.status(404).body(ErrorDetails.fromException(e, 404));
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

    private record DatosErrorValidacion(String campo, String error){
        public DatosErrorValidacion(FieldError error){
            this(
                    error.getField(),
                    error.getDefaultMessage()
            );
        }
    }

}
