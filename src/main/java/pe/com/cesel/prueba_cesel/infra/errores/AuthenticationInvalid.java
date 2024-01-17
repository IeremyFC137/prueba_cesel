package pe.com.cesel.prueba_cesel.infra.errores;

public class AuthenticationInvalid extends RuntimeException{
    public AuthenticationInvalid(String s) {
        super(s);
    }
}
