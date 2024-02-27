package pe.com.cesel.prueba_cesel.infra.security;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pe.com.cesel.prueba_cesel.domain.gasto.DatosActualizarGasto;
import pe.com.cesel.prueba_cesel.domain.gasto.Gasto;
import pe.com.cesel.prueba_cesel.domain.gasto.GastoRepository;
import pe.com.cesel.prueba_cesel.domain.gasto.gastoDetalle.*;
import pe.com.cesel.prueba_cesel.domain.usuario.Usuario;
import pe.com.cesel.prueba_cesel.domain.usuario.UsuarioRepository;
import pe.com.cesel.prueba_cesel.infra.errores.NotFoundEntity;

import java.util.ArrayList;
import java.util.List;

@Aspect
@Component
public class VerificacionDeUsuarioAspect {

    @Autowired
    private GastoRepository gastoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private DetallesGastoRepository detallesGastoRepository;

    @Pointcut("execution(* pe.com.cesel.prueba_cesel.controller.GastoController.eliminarGasto(..)) || " +
            "execution(* pe.com.cesel.prueba_cesel.controller.GastoController.actualizarGasto(..)) ||" +
            "execution(* pe.com.cesel.prueba_cesel.controller.GastoController.retornaDatosGasto(..))")
    public void metodosGasto() {}

    @Pointcut("execution(* pe.com.cesel.prueba_cesel.controller.GastoController.eliminarDetalleGasto(..)) || " +
            "execution(* pe.com.cesel.prueba_cesel.controller.GastoController.retornaDetalleGasto(..)) || "+
            "execution(* pe.com.cesel.prueba_cesel.controller.GastoController.eliminarDetallesGasto(..))")
    public void metodosDetalleGasto() {}

    @Pointcut("execution(* pe.com.cesel.prueba_cesel.controller.GastoController.registrarDetalle(..)) " +
            "|| execution(* pe.com.cesel.prueba_cesel.controller.GastoController.registrarDetalles(..))")
    public void metodoRegistroDetalleODetalles(){}

    @Around("metodoRegistroDetalleODetalles() && args(datos,..)")
    public Object consejoAlrededorRegistroDetalleODetalles(ProceedingJoinPoint joinPoint, Object datos)
            throws Throwable {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new AccessDeniedException("No se ha proporcionado autenticación");
        }

        String correoUsuarioToken = authentication.getName();
        Usuario usuario = usuarioRepository.findByEmail(correoUsuarioToken);

        if (usuario == null) {
            throw new NotFoundEntity("Usuario no encontrado");
        }

        Long idGasto;

        // Determina el tipo de datos y extrae el idGasto correspondiente
        if (datos instanceof DatosDetalleGastoRegistro) {
            idGasto = ((DatosDetalleGastoRegistro) datos).gastoId();
        } else if (datos instanceof DatosDetallesGastoRegistro) {
            idGasto = ((DatosDetallesGastoRegistro) datos).gastoId();
        } else {
            throw new IllegalArgumentException("Tipo de argumento no soportado");
        }

        Gasto gasto = gastoRepository.findById(idGasto).orElseThrow(() -> new NotFoundEntity("Gasto no encontrado"));

        if (!gasto.getUsuario().getId().equals(usuario.getId())) {
            throw new AccessDeniedException("El usuario no tiene permiso para registrar detalles en este gasto");
        }

        return joinPoint.proceed();
    }


    @Before("metodosGasto()")
    public void verificarUsuarioAntesDeEliminarGasto(JoinPoint joinPoint) throws Throwable {

        Object[] args = joinPoint.getArgs();
        Authentication authentication = null;
        Long id = null;

        for (Object arg : args) {
            if(arg instanceof Authentication) {
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

    @Before("metodosDetalleGasto()")
    public void verificarUsuarioAntesDeOperarDetalleGasto(JoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Authentication authentication = null;
        List<Long> idsDetalleGasto = new ArrayList<>();

        for (Object arg : args) {
            if (arg instanceof Authentication) {
                authentication = (Authentication) arg;
            } else if (arg instanceof Long) { // Caso de eliminación individual
                idsDetalleGasto.add((Long) arg);
            } else if (arg instanceof DatosEliminarDetallesGasto) { // Caso de eliminación en lote
                idsDetalleGasto.addAll(((DatosEliminarDetallesGasto) arg).ids());
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

        // Ahora procesamos la lista de IDs, que puede ser solo uno o varios, dependiendo del caso
        for (Long idDetalleGasto : idsDetalleGasto) {
            GastoDetalle detalleGasto = detallesGastoRepository.findById(idDetalleGasto)
                    .orElseThrow(() -> new NotFoundEntity("Detalle de Gasto no encontrado"));

            Gasto gasto = detalleGasto.getGasto();
            if (!gasto.getUsuario().getId().equals(usuario.getId())) {
                throw new AccessDeniedException("Acceso denegado para el detalle de gasto con ID: " + idDetalleGasto);
            }
        }
    }


    /*private <T> Optional<T> findArgumentOfType(Object[] args, Class<T> type) {
        return Arrays.stream(args)
                .filter(type::isInstance)
                .map(type::cast)
                .findFirst();
    }*/
}