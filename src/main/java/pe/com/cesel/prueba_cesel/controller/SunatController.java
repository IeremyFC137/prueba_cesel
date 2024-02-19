package pe.com.cesel.prueba_cesel.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import pe.com.cesel.prueba_cesel.domain.sunat.DatosResultadoSunat;
import pe.com.cesel.prueba_cesel.domain.sunat.DatosValidarComprobante;
import pe.com.cesel.prueba_cesel.domain.sunat.ProcesoSunatService;
import pe.com.cesel.prueba_cesel.infra.errores.InvalidRequestError;

@Controller
@RequestMapping("/sunat")
public class SunatController {
    @Autowired
    private ProcesoSunatService service;
    @PostMapping("/validarComprobante")
    public ResponseEntity<DatosResultadoSunat> validarComprobante(
            @RequestBody
            @Valid
            DatosValidarComprobante datos)
    {

        try {

            DatosResultadoSunat data = service.validar(datos);

            return ResponseEntity.ok(data);

        } catch (Exception e){

            throw new InvalidRequestError(e.getMessage());

        }
    }
}
