package pe.com.cesel.prueba_cesel.domain.gasto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pe.com.cesel.prueba_cesel.domain.gasto.validaciones.ValidadorDeGastos;
import pe.com.cesel.prueba_cesel.domain.usuario.UsuarioRepository;
import pe.com.cesel.prueba_cesel.infra.errores.ValidacionDeIntegridad;

import java.util.List;

@Service
public class RegistroDeGastoService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private GastoRepository gastoRepository;
    @Autowired
    List<ValidadorDeGastos> validadores;

    public DatosDetalleGasto registrar(DatosRegistroGasto datos){

        if(!usuarioRepository.findById(datos.idUsuario()).isPresent()){
            throw new ValidacionDeIntegridad("este id para el usuario no fue encontrado");
        }

        validadores.forEach(v -> v.validar(datos));

        var usuario = usuarioRepository.findById(datos.idUsuario()).get();
        var gasto = new Gasto(usuario, datos);
        gastoRepository.save(gasto);

        return new DatosDetalleGasto(gasto);
    }
   public DatosDetalleGasto actualizar(DatosActualizarGasto datos){
       if(!gastoRepository.findById(datos.id()).isPresent()){
           throw new ValidacionDeIntegridad("este id para el gasto no fue encontrado");
       }
       Gasto gasto = gastoRepository.getReferenceById(datos.id());
       gasto.actualizarDatos((datos));
       return new DatosDetalleGasto(gasto);

   }
}
