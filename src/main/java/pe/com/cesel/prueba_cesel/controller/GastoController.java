package pe.com.cesel.prueba_cesel.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import pe.com.cesel.prueba_cesel.domain.gasto.*;
import org.springframework.transaction.annotation.Transactional;
import pe.com.cesel.prueba_cesel.domain.gasto.gastoDetalle.*;
import pe.com.cesel.prueba_cesel.domain.usuario.Usuario;
import pe.com.cesel.prueba_cesel.domain.usuario.UsuarioRepository;
import pe.com.cesel.prueba_cesel.infra.errores.ValidacionDeIntegridad;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/gastos")
public class GastoController {
    @Autowired
    private RegistroDeGastoService service;

    @Autowired
    private GastoRepository gastoRepository;

    @Autowired
    private DetallesGastoRepository detallesGastoRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<ResultadoRegistroGasto> registrarGasto(
            @RequestBody
            @Valid
            DatosRegistroGasto datos,
            UriComponentsBuilder uriComponentsBuilder)
            throws ValidacionDeIntegridad {

        var response = service.registrar(datos);

        URI url = uriComponentsBuilder.path("/gastos/{id}").buildAndExpand(response.id()).toUri();

        return ResponseEntity.created(url).body(response);
    }

    @PostMapping("/detalle")
    @Transactional
    public ResponseEntity<ResultadoDetalleGasto> registrarDetalle(
            @RequestBody
            @Valid
            DatosDetalleGastoRegistro datos,
            UriComponentsBuilder uriComponentsBuilder
           ) throws ValidacionDeIntegridad {

        System.out.println(datos);

        var response = service.registrarDetalle(datos);

        URI url = uriComponentsBuilder.path("/gastos/detalle/{id}").buildAndExpand(response.id()).toUri();

        return ResponseEntity.created(url).body(response);
    }

    @PostMapping("/detalles")
    @Transactional
    public ResponseEntity<ResultadoDetallesGasto> registrarDetalles(
            @RequestBody
            @Valid
            DatosDetallesGastoRegistro datos
    ) throws ValidacionDeIntegridad {

        System.out.println(datos);

        var response = service.registrarDetalles(datos);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<ResultadoRegistroGasto>> listadoGastos(
            @PageableDefault(size = 3) Pageable paginacion,
            Authentication authentication
    ) {

        var email = authentication.getName();

        Usuario usuario = usuarioRepository.findByEmail(email);

        return ResponseEntity.ok(gastoRepository.findByUsuarioId(usuario.getId(), paginacion).map(ResultadoRegistroGasto::new));
    }

    @GetMapping("/obtenerCentroCosto")
    public ResponseEntity<ListaCampoDetalleGasto> listaCentroCosto(){

        List<String> datos = gastoRepository.obtenerCentroDeCosto();

        return ResponseEntity.ok(new ListaCampoDetalleGasto(datos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResultadoRegistroGasto> retornaDatosGasto(
            @PathVariable("id")
            Long id,
            Authentication authentication) {

        Gasto gasto = gastoRepository.getReferenceById(id);

        var datosGasto = new ResultadoRegistroGasto(gasto);

        return ResponseEntity.ok(datosGasto);
    }

    @GetMapping("/detalle/{id}")
    public ResponseEntity<ResultadoDetalleGasto> retornaDetalleGasto(
            @PathVariable("id")
            Long id,
            Authentication authentication) {

        GastoDetalle gasto = detallesGastoRepository.getReferenceById(id);

        var datosGasto = new ResultadoDetalleGasto(gasto);

        return ResponseEntity.ok(datosGasto);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity eliminarGasto(
            @PathVariable("id")
            Long id,
            Authentication authentication) {

        gastoRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/detalle/{id}")
    @Transactional
    public ResponseEntity eliminarDetalleGasto(
            @PathVariable("id")
            Long id,
            Authentication authentication){

        detallesGastoRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/detalles")
    public ResponseEntity eliminarDetallesGasto(
            @RequestBody
            @Valid
            DatosEliminarDetallesGasto datos,
            Authentication authentication
    ){
        detallesGastoRepository.deleteByIdIn(datos.ids());

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    @Transactional
    public ResponseEntity actualizarGasto(
            @RequestBody
            @Valid
            DatosActualizarGasto
                    datosActualizarGasto,
            Authentication authentication
    ){
        var response = service.actualizar(datosActualizarGasto);

        return ResponseEntity.ok(response);
    }

}
