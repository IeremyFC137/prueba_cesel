package pe.com.cesel.prueba_cesel.domain.gasto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.com.cesel.prueba_cesel.domain.usuario.UsuarioRepository;
import pe.com.cesel.prueba_cesel.infra.errores.ValidacionDeIntegridad;

@Service
public class RegistroDeGastoService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private GastoRepository gastoRepository;

    public DatosDetalleGasto registrar(DatosRegistroGasto datos){

        if(!usuarioRepository.findById(datos.idUsuario()).isPresent()){
            throw new ValidacionDeIntegridad("este id para el usuario no fue encontrado");
        }

        var usuario = usuarioRepository.findById(datos.idUsuario()).get();
        var gasto = new Gasto(usuario, datos);
        gastoRepository.save(gasto);

        return new DatosDetalleGasto(gasto);
    }
}
