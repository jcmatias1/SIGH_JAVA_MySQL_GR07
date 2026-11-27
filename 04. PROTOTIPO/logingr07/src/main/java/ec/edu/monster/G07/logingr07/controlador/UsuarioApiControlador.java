/*
 * SIGH - Hospital General Monster Salud
 * Talleres Login y CRUD MVC en Java - Grupo GR07
 * Subsistema de Seguridad
 */
package ec.edu.monster.G07.logingr07.controlador;

import ec.edu.monster.G07.logingr07.config.SeguridadInterceptor;
import ec.edu.monster.G07.logingr07.dto.EmpleadoDTO;
import ec.edu.monster.G07.logingr07.dto.UsuarioDTO;
import ec.edu.monster.G07.logingr07.dto.UsuarioDetalleDTO;
import ec.edu.monster.G07.logingr07.dto.UsuarioSesion;
import ec.edu.monster.G07.logingr07.servicio.UsuarioServicio;
import ec.edu.monster.G07.logingr07.utils.SeguridadUtil;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

/**
 * API JSON que usa la pantalla usuarios.jsp (ECUD2). Está bajo /seguridad/usuarios, por lo que
 * solo responde a perfiles con la opción S001 «Administrar usuarios».
 */
@RestController
@RequestMapping("/seguridad/usuarios/api")
public class UsuarioApiControlador {

    private final UsuarioServicio usuarioServicio;

    public UsuarioApiControlador(UsuarioServicio usuarioServicio) {
        this.usuarioServicio = usuarioServicio;
    }

    @GetMapping
    public List<UsuarioDTO> listar() {
        return usuarioServicio.listar();
    }

    @GetMapping("/{codigo}")
    public UsuarioDetalleDTO detalle(@PathVariable String codigo) {
        return usuarioServicio.detalle(codigo);
    }

    @GetMapping("/empleados")
    public List<EmpleadoDTO> empleadosSinCuenta() {
        return usuarioServicio.empleadosSinCuenta();
    }

    @GetMapping("/perfiles")
    public List<Map<String, String>> perfiles() {
        return usuarioServicio.perfiles().stream()
                .map(p -> Map.of("codigo", p.getCodigoPerfil(), "descripcion", p.getDescripcionPerfil()))
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioDTO crear(@RequestBody UsuarioDTO datos,
            @SessionAttribute(SeguridadInterceptor.USUARIO_SESION) UsuarioSesion actor, HttpServletRequest request) {
        return usuarioServicio.crear(datos, actor.getCodigoUsuario(), SeguridadUtil.ipCliente(request));
    }

    @PutMapping("/{codigo}")
    public UsuarioDTO actualizar(@PathVariable String codigo, @RequestBody UsuarioDTO datos,
            @SessionAttribute(SeguridadInterceptor.USUARIO_SESION) UsuarioSesion actor, HttpServletRequest request) {
        return usuarioServicio.actualizar(codigo, datos, actor.getCodigoUsuario(), SeguridadUtil.ipCliente(request));
    }

    /** accion: desactivar | activar | desbloquear */
    @PatchMapping("/{codigo}/{accion}")
    public UsuarioDTO cambiarEstado(@PathVariable String codigo, @PathVariable String accion,
            @SessionAttribute(SeguridadInterceptor.USUARIO_SESION) UsuarioSesion actor, HttpServletRequest request) {
        return usuarioServicio.cambiarEstado(codigo, accion, actor.getCodigoUsuario(), SeguridadUtil.ipCliente(request));
    }
}
