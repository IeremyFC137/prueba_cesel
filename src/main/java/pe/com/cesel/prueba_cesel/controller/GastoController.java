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
import pe.com.cesel.prueba_cesel.domain.direccion.DatosDireccion;
import pe.com.cesel.prueba_cesel.domain.gasto.*;
import org.springframework.transaction.annotation.Transactional;
import pe.com.cesel.prueba_cesel.domain.usuario.DatosRespuestaUsuario;
import pe.com.cesel.prueba_cesel.domain.usuario.Usuario;
import pe.com.cesel.prueba_cesel.domain.usuario.UsuarioRepository;
import pe.com.cesel.prueba_cesel.infra.errores.ValidacionDeIntegridad;

import java.net.URI;

@RestController
@RequestMapping("/gastos")
public class GastoController {
    @Autowired
    private RegistroDeGastoService service;

    @Autowired
    private GastoRepository gastoRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<DatosDetalleGasto> registrarGasto(
            @RequestBody
            @Valid
            DatosRegistroGasto datos,
            UriComponentsBuilder uriComponentsBuilder)
            throws ValidacionDeIntegridad {
        var response = service.registrar(datos);
        URI url = uriComponentsBuilder.path("/gastos/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(url).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<DatosDetalleGasto>> listadoGastos(
            @PageableDefault(size = 3) Pageable paginacion,
            Authentication authentication
    ) {

        var email = authentication.getName();

        Usuario usuario = usuarioRepository.findByEmail(email);

        return ResponseEntity.ok(gastoRepository.findByUsuarioId(usuario.getId(), paginacion).map(DatosDetalleGasto::new));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosDetalleGasto> retornaDatosGasto(
            @PathVariable("id")
            Long id,
            Authentication authentication) {
        Gasto gasto = gastoRepository.getReferenceById(id);
        var datosGasto = new DatosDetalleGasto(gasto);
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
