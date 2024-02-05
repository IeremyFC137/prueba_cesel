package pe.com.cesel.prueba_cesel.infra.security;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import pe.com.cesel.prueba_cesel.domain.gasto.DatosActualizarGasto;
import pe.com.cesel.prueba_cesel.domain.gasto.Gasto;
import pe.com.cesel.prueba_cesel.domain.gasto.GastoRepository;
import pe.com.cesel.prueba_cesel.domain.usuario.Usuario;
import pe.com.cesel.prueba_cesel.domain.usuario.UsuarioRepository;
import pe.com.cesel.prueba_cesel.infra.errores.NotFoundEntity;

@Aspect
@Component
public class VerificacionDeUsuarioAspect {

    @Autowired
    private GastoRepository gastoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Pointcut("execution(* pe.com.cesel.prueba_cesel.controller.GastoController.eliminarGasto(..)) || " +
            "execution(* pe.com.cesel.prueba_cesel.controller.GastoController.actualizarGasto(..)) ||" +
            "execution(* pe.com.cesel.prueba_cesel.controller.GastoController.retornaDatosGasto(..))")
    public void metodosGasto() {}


    @Before("metodosGasto()")
    public void verificarUsuarioAntesDeEliminarGasto(JoinPoint joinPoint) throws Throwable {

        Object[] args = joinPoint.getArgs();
        Authentication authentication = null;
        Long id = null;

        for (Object arg : args) {
            if (arg instanceof Authentication) {
                authentication = (Authentication) arg;
            } else if (arg instanceof DatosActualizarGasto) {
                id = ((DatosActualizarGasto) arg).id();
            } else if (arg instanceof Long) {
                id = (Long) arg;
            }
        }

        if (authentication == null) {
            throw new AccessDeniedException("No se ha proporcionado autenticación");
        }

        String correoUsuarioToken = authentication.getName();
        Usuario usuario = usuarioRepository.findByEmail(correoUsuarioToken);

        if (usuario == null) {
            throw new NotFoundEntity("Usuario no encontrado");
        }

        assert id != null;
        Gasto gasto = gastoRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntity("Gasto no encontrado"));

        if (!gasto.getUsuario().getId().equals(usuario.getId())) {
            throw new AccessDeniedException("Acceso denegado");
        }
    }
}