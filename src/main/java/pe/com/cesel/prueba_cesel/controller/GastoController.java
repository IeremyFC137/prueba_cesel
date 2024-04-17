package pe.com.cesel.prueba_cesel.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.core.io.Resource;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import pe.com.cesel.prueba_cesel.domain.gasto.*;
import org.springframework.transaction.annotation.Transactional;
import pe.com.cesel.prueba_cesel.domain.gasto.gastoDetalle.*;
import pe.com.cesel.prueba_cesel.domain.usuario.Usuario;
import pe.com.cesel.prueba_cesel.domain.usuario.UsuarioRepository;
import pe.com.cesel.prueba_cesel.infra.errores.CustomValidationException;
import pe.com.cesel.prueba_cesel.infra.errores.ValidacionDeIntegridad;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.io.IOException;
import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private Validator validator;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<ResultadoRegistroGasto> registrarGasto(
            @RequestParam("datos") String datosJson,
            @RequestParam("imagen") MultipartFile imagen,
            UriComponentsBuilder uriComponentsBuilder) {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        DatosRegistroGasto datos;
        try {
            datos = objectMapper.readValue(datosJson, DatosRegistroGasto.class);
            Set<ConstraintViolation<DatosRegistroGasto>> violations = validator.validate(datos);

            if (!violations.isEmpty()) {
                Set<ConstraintViolation<?>> wildcardViolations = new HashSet<>(violations);
                throw new CustomValidationException(wildcardViolations, "Mensaje de error de validación");
            }
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error al procesar los datos del gasto", e);
        }

        ResultadoRegistroGasto response = service.registrar(datos, imagen);
        URI url = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

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

    @GetMapping("/image/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("fileName") String fileName) {
        try {
            final Path fileStorageLocation = Paths.get("assets/images");
            Path filePath = fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException ex) {
            return ResponseEntity.badRequest().build();
        }
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
            @RequestParam("datos") String datosJson,
            @RequestParam(value = "imagen", required = false) MultipartFile imagen,
            Authentication authentication
    ){
        ObjectMapper objectMapper = new ObjectMapper();
        DatosActualizarGasto datos;
        try {
            datos = objectMapper.readValue(datosJson, DatosActualizarGasto.class);
            Set<ConstraintViolation<DatosActualizarGasto>> violations = validator.validate(datos);

            if (!violations.isEmpty()) {
                Set<ConstraintViolation<?>> wildcardViolations = new HashSet<>(violations);
                throw new CustomValidationException(wildcardViolations, "Mensaje de error de validación");
            }
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error al procesar los datos del gasto", e);
        }
        var response = service.actualizar(datos, imagen);

        return ResponseEntity.ok(response);
    }

}
