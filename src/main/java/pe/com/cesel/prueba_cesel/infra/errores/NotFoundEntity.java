package pe.com.cesel.prueba_cesel.infra.errores;

public class NotFoundEntity extends RuntimeException{
    public NotFoundEntity(String s) {
        super(s);
    }
}
