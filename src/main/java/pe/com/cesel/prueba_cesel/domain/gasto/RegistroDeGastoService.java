package pe.com.cesel.prueba_cesel.domain.gasto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.com.cesel.prueba_cesel.domain.gasto.gastoDetalle.*;
import pe.com.cesel.prueba_cesel.domain.gasto.validaciones.ValidadorDeGastos;
import pe.com.cesel.prueba_cesel.domain.usuario.UsuarioRepository;
import pe.com.cesel.prueba_cesel.infra.errores.ValidacionDeIntegridad;

import java.util.ArrayList;
import java.util.List;

@Service
public class RegistroDeGastoService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private GastoRepository gastoRepository;
    @Autowired
    private DetallesGastoRepository detallesGastoRepository;
    @Autowired
    List<ValidadorDeGastos> validadores;

    public ResultadoRegistroGasto registrar(DatosRegistroGasto datos){

        if(!usuarioRepository.findById(datos.idUsuario()).isPresent()){
            throw new ValidacionDeIntegridad("este id para el usuario no fue encontrado");
        }

        validadores.forEach(v -> v.validar(datos));

        var usuario = usuarioRepository.findById(datos.idUsuario()).get();
        var gasto = new Gasto(usuario, datos);
        gastoRepository.save(gasto);

        return new ResultadoRegistroGasto(gasto);
    }

    public ResultadoDetalleGasto registrarDetalle(DatosDetalleGastoRegistro datos){
        if(!gastoRepository.findById(datos.gastoId()).isPresent()){
            throw new ValidacionDeIntegridad("este id para el gasto no fue encontrado");
        }

        var gasto = gastoRepository.findById(datos.gastoId()).get();
        var gastoDetalle = new GastoDetalle(gasto, datos);
        detallesGastoRepository.save(gastoDetalle);

        return new ResultadoDetalleGasto(gastoDetalle);
    }

    public ResultadoDetallesGasto registrarDetalles(DatosDetallesGastoRegistro datos){
        if(!gastoRepository.findById(datos.gastoId()).isPresent()){
            throw new ValidacionDeIntegridad("este id para el gasto no fue encontrado");
        }

        var gasto = gastoRepository.findById(datos.gastoId()).get();

        List<ResultadoDetalleGasto> gastoDetalleList = new ArrayList<>();

        for (DatosDetalleGastoRegistro detalle : datos.detalles()) {
            var gastoDetalle = new GastoDetalle(gasto, detalle);
            detallesGastoRepository.save(gastoDetalle);
            gastoDetalleList.add(new ResultadoDetalleGasto(gastoDetalle));
        }

        return new ResultadoDetallesGasto(gastoDetalleList);
    }

   public ResultadoRegistroGasto actualizar(DatosActualizarGasto datos){
       if(!gastoRepository.findById(datos.id()).isPresent()){
           throw new ValidacionDeIntegridad("este id para el gasto no fue encontrado");
       }
       Gasto gasto = gastoRepository.getReferenceById(datos.id());
       gasto.actualizarDatos((datos));
       return new ResultadoRegistroGasto(gasto);
   }

}
