package pe.com.cesel.prueba_cesel.infra.errores;

public class ValidacionDeImagen extends RuntimeException {
    public ValidacionDeImagen(String s) {
        super(s);
    }
}
