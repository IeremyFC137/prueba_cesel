package pe.com.cesel.prueba_cesel.controller;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import pe.com.cesel.prueba_cesel.domain.gasto.DatosDetalleGasto;
import pe.com.cesel.prueba_cesel.domain.gasto.DatosRegistroGasto;
import pe.com.cesel.prueba_cesel.domain.gasto.GastoRepository;
import pe.com.cesel.prueba_cesel.domain.gasto.RegistroDeGastoService;
import org.springframework.transaction.annotation.Transactional;
import pe.com.cesel.prueba_cesel.domain.usuario.DatosListadoUsuario;
import pe.com.cesel.prueba_cesel.infra.errores.ValidacionDeIntegridad;

import java.net.URI;

@RestController
@RequestMapping("/gastos")
public class GastoController {
    @Autowired
    private RegistroDeGastoService service;

    @Autowired
    private GastoRepository gastoRepository;

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
    public ResponseEntity<Page<DatosDetalleGasto>> listadoUsuarios(
            @PageableDefault(size = 3) Pageable paginacion,
            @RequestParam(name = "userId") Long userId) {
        return ResponseEntity.ok(gastoRepository.findByUsuarioId(userId, paginacion).map(DatosDetalleGasto::new));
    }
}
