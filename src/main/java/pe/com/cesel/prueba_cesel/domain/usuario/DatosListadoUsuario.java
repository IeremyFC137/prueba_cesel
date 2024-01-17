package pe.com.cesel.prueba_cesel.domain.usuario;

public record DatosListadoUsuario(
        Long id,
        String nombre,
        String especialidad,
        String documento,
        String email
) {

    public DatosListadoUsuario(Usuario usuario){
        this(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEspecialidad().toString(),
                usuario.getDocumento(),
                usuario.getEmail());
    }
}
