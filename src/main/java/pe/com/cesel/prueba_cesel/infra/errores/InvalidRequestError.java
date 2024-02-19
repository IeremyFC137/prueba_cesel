package pe.com.cesel.prueba_cesel.infra.errores;

public class InvalidRequestError extends RuntimeException{
    public InvalidRequestError(String s) {
        super(s);
    }
}
