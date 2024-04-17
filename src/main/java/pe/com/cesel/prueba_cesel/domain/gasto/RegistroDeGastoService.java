package pe.com.cesel.prueba_cesel.domain.gasto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.com.cesel.prueba_cesel.domain.gasto.gastoDetalle.*;
import pe.com.cesel.prueba_cesel.domain.gasto.validaciones.ValidadorDeGastos;
import pe.com.cesel.prueba_cesel.domain.usuario.UsuarioRepository;
import pe.com.cesel.prueba_cesel.infra.errores.ValidacionDeIntegridad;

import java.io.IOException;
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

    @Autowired
    private FileStorageService fileStorageService;

    public ResultadoRegistroGasto registrar(DatosRegistroGasto datos, MultipartFile imagen){

        if(!usuarioRepository.findById(datos.idUsuario()).isPresent()){
            throw new ValidacionDeIntegridad("este id para el usuario no fue encontrado");
        }

        validadores.forEach(v -> v.validar(datos));

        var usuario = usuarioRepository.findById(datos.idUsuario()).get();

        String fileName;
        String rutaImagen;
        try {

            String name =
                    datos.ruc()+
                            "-"+ (datos.tipo_documento().equals(TipoDocumento.FACTURA)
                            ? "01" : datos.tipo_documento().equals(TipoDocumento.BOLETA)
                            ? "03" : "12")
                            +"-"+datos.documento();

            fileName = fileStorageService.storeFile(imagen, name);
            rutaImagen = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/gastos/image/")
                    .path(fileName)
                    .toUriString();
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al almacenar el archivo", e);
        }

        var gasto = new Gasto(usuario, datos, rutaImagen);
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

    public ResultadoRegistroGasto actualizar(DatosActualizarGasto datos, MultipartFile imagen) {
        if (!gastoRepository.findById(datos.id()).isPresent()) {
            throw new ValidacionDeIntegridad("Este id para el gasto no fue encontrado");
        }
        Gasto gasto = gastoRepository.getReferenceById(datos.id());
        String fileName = null;
        String rutaImagen = null;
        if (imagen != null) {
            try {
                String name =
                        gasto.getRuc() +
                                "-" + (gasto.getTipo_documento().equals(TipoDocumento.FACTURA)
                                ? "01" : gasto.getTipo_documento().equals(TipoDocumento.BOLETA)
                                ? "03" : "12")
                                + "-" + gasto.getDocumento();

                fileName = fileStorageService.storeFile(imagen, name);
                rutaImagen = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/gastos/image/")
                        .path(fileName)
                        .toUriString();
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al almacenar el archivo", e);
            }
        }
        gasto.actualizarDatos(datos, rutaImagen);
        return new ResultadoRegistroGasto(gasto);
    }

}
