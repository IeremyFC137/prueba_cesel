package pe.com.cesel.prueba_cesel.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import pe.com.cesel.prueba_cesel.domain.usuario.Usuario;
import pe.com.cesel.prueba_cesel.domain.usuario.UsuarioRepository;
import pe.com.cesel.prueba_cesel.infra.errores.AuthenticationInvalid;
import pe.com.cesel.prueba_cesel.infra.errores.NotFoundEntity;
import pe.com.cesel.prueba_cesel.infra.security.DatosAutenticacion;
import pe.com.cesel.prueba_cesel.infra.security.TokenService;
import pe.com.cesel.prueba_cesel.domain.authentication.DatosAutenticacionUsuario;
import pe.com.cesel.prueba_cesel.domain.authentication.Authentication;


import java.util.stream.Collectors;


@RestController
@RequestMapping("/auth")
public class AutenticacionController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/login")
    public ResponseEntity<DatosAutenticacion> autenticarUsuario(
            @RequestBody
            @Valid
            DatosAutenticacionUsuario datosAutenticacionUsuario) {

        try{
            org.springframework.security.core.Authentication authToken = new UsernamePasswordAuthenticationToken(datosAutenticacionUsuario.login(),
                    datosAutenticacionUsuario.clave());

            var usuarioAutenticado = authenticationManager.authenticate(authToken);

            var JWTtoken = tokenService.generarToken((Authentication) usuarioAutenticado.getPrincipal());

            Usuario usuario = usuarioRepository.findByEmail(datosAutenticacionUsuario.login());
            return ResponseEntity.ok(
                    new DatosAutenticacion(
                            usuario.getId(),
                            usuario.getEmail(),
                            usuario.getNombre(),
                            usuario.getActivo(),
                            usuario.getRoles().stream().map(Enum::name).collect(Collectors.toList()),
                            JWTtoken)
            );
        } catch(AuthenticationException e){
            throw new AuthenticationInvalid("Credenciales incorrectas");
        }
    }
    @GetMapping("/check-status")
    public ResponseEntity<DatosAutenticacion> checkUserStatus(HttpServletRequest request) {

        try {
            String token = tokenService.extractToken(request); // Extraer el token
            // Validar el formato del token antes de proceder

            String userEmail = tokenService.getSubject(token);
            String nuevoToken = tokenService.generarTokenDesdeTokenExistente(token);

            Usuario usuario = usuarioRepository.findByEmail(userEmail);

            if (usuario == null) {
                throw new NotFoundEntity("Usuario no encontrado");
            }

            return ResponseEntity.ok(
                    new DatosAutenticacion(
                            usuario.getId(),
                            usuario.getEmail(),
                            usuario.getNombre(),
                            usuario.getActivo(),
                            usuario.getRoles().stream().map(Enum::name).collect(Collectors.toList()),
                            nuevoToken)
            );
        } catch (AuthenticationInvalid e) {
            throw new AuthenticationInvalid(e.getMessage());
        }
    }


}
