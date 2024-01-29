package pe.com.cesel.prueba_cesel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pe.com.cesel.prueba_cesel.domain.scanit.DatosResultadoOcr;
import pe.com.cesel.prueba_cesel.domain.scanit.ProcesoOcrFotoService;
import pe.com.cesel.prueba_cesel.infra.errores.ValidacionDeImagen;

import java.io.IOException;

@RestController
@RequestMapping("/scanit")
public class ScanitController {

    @Autowired
    private ProcesoOcrFotoService service;

    @PostMapping
    public ResponseEntity<DatosResultadoOcr> escanearImagen(
            @RequestParam("file") MultipartFile file){

        try {

            if (file.isEmpty()) {
                throw new ValidacionDeImagen("No se ha enviado la imagen");
            }

            if (file.getSize() > 1048576) {
                throw new ValidacionDeImagen("La foto supera el 1MB de tamaño");
            }

            DatosResultadoOcr data = service.procesar(file);
            return ResponseEntity.ok(data);
        }

        catch (IOException e){

            throw new RuntimeException("Error al leer la imagen");

        }

    }
}
