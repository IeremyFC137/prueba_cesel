package pe.com.cesel.prueba_cesel.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import pe.com.cesel.prueba_cesel.domain.direccion.DatosDireccion;
import pe.com.cesel.prueba_cesel.domain.usuario.*;

import java.net.URI;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    public ResponseEntity<DatosRespuestaUsuario> registrarUsuario(
            @RequestBody
            @Valid
            DatosRegistroUsuario datosRegistroUsuario,
            UriComponentsBuilder uriComponentsBuilder) {
        Usuario usuario = usuarioRepository.save(new Usuario(datosRegistroUsuario));
        DatosRespuestaUsuario datosRespuestaUsuario = new DatosRespuestaUsuario(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getTelefono(),
                usuario.getEspecialidad().toString(),
                new DatosDireccion(
                        usuario.getDireccion().getCalle(),
                        usuario.getDireccion().getDistrito(),
                        usuario.getDireccion().getCiudad(),
                        usuario.getDireccion().getNumero(),
                        usuario.getDireccion().getComplemento()));

        URI url = uriComponentsBuilder.path("/medicos/{id}").buildAndExpand(usuario.getId()).toUri();
        return ResponseEntity.created(url).body(datosRespuestaUsuario);

    }

    @GetMapping
    public ResponseEntity<Page<DatosListadoUsuario>> listadoUsuarios(@PageableDefault(size = 2) Pageable paginacion) {
//        return medicoRepository.findAll(paginacion).map(DatosListadoMedico::new);
        return ResponseEntity.ok(usuarioRepository.findByActivoTrue(paginacion).map(DatosListadoUsuario::new));
    }

    @PutMapping
    @Transactional
    public ResponseEntity actualizarUsuario(
            @RequestBody
            @Valid
            DatosActualizarUsuario
                    datosActualizarUsuario) {
        Usuario usuario = usuarioRepository.getReferenceById(datosActualizarUsuario.id());
        usuario.actualizarDatos(datosActualizarUsuario);
        return ResponseEntity.ok(new DatosRespuestaUsuario(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getTelefono(),
                usuario.getEspecialidad().toString(),
                new DatosDireccion(
                        usuario.getDireccion().getCalle(),
                        usuario.getDireccion().getDistrito(),
                        usuario.getDireccion().getCiudad(),
                        usuario.getDireccion().getNumero(),
                        usuario.getDireccion().getComplemento())));
    }

    // DELETE LOGICO
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity eliminarUsuario(
            @PathVariable
            Long id) {
        Usuario usuario = usuarioRepository.getReferenceById(id);
        usuario.desactivarUsuario();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosRespuestaUsuario> retornaDatosUsuario(
            @PathVariable
            Long id) {
        Usuario usuario = usuarioRepository.getReferenceById(id);
        var datosMedico = new DatosRespuestaUsuario(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getTelefono(),
                usuario.getEspecialidad().toString(),
                new DatosDireccion(
                        usuario.getDireccion().getCalle(),
                        usuario.getDireccion().getDistrito(),
                        usuario.getDireccion().getCiudad(),
                        usuario.getDireccion().getNumero(),
                        usuario.getDireccion().getComplemento()));
        return ResponseEntity.ok(datosMedico);
    }

}
