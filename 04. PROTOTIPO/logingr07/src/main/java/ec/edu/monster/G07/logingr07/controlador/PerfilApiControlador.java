/*
 * SIGH - Hospital General Monster Salud
 * Talleres Login y CRUD MVC en Java - Grupo GR07
 * Subsistema de Seguridad
 */
package ec.edu.monster.G07.logingr07.controlador;

import ec.edu.monster.G07.logingr07.config.SeguridadInterceptor;
import ec.edu.monster.G07.logingr07.dto.PerfilDTO;
import ec.edu.monster.G07.logingr07.dto.PerfilFormDTO;
import ec.edu.monster.G07.logingr07.dto.UsuarioSesion;
import ec.edu.monster.G07.logingr07.servicio.PerfilServicio;
import ec.edu.monster.G07.logingr07.utils.SeguridadUtil;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

/**
 * API JSON de perfiles.jsp (opción S002 «Administrar perfiles»): CRUD de perfiles
 * (GET, POST, PUT, DELETE) y asignación o retiro de sus opciones.
 */
@RestController
@RequestMapping("/seguridad/perfiles/api")
public class PerfilApiControlador {

    private final PerfilServicio perfilServicio;

    public PerfilApiControlador(PerfilServicio perfilServicio) {
        this.perfilServicio = perfilServicio;
    }

    @GetMapping
    public List<PerfilDTO> listar() {
        return perfilServicio.listar();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PerfilDTO crear(@RequestBody PerfilFormDTO datos,
            @SessionAttribute(SeguridadInterceptor.USUARIO_SESION) UsuarioSesion actor, HttpServletRequest request) {
        return perfilServicio.crear(datos, actor.getCodigoUsuario(), SeguridadUtil.ipCliente(request));
    }

    @PutMapping("/{perfil}")
    public Map<String, String> actualizar(@PathVariable String perfil, @RequestBody PerfilFormDTO datos,
            @SessionAttribute(SeguridadInterceptor.USUARIO_SESION) UsuarioSesion actor, HttpServletRequest request) {
        perfilServicio.actualizar(perfil, datos, actor.getCodigoUsuario(), SeguridadUtil.ipCliente(request));
        return Map.of("mensaje", "Perfil actualizado");
    }

    @DeleteMapping("/{perfil}")
    public Map<String, String> eliminar(@PathVariable String perfil,
            @SessionAttribute(SeguridadInterceptor.USUARIO_SESION) UsuarioSesion actor, HttpServletRequest request) {
        perfilServicio.eliminar(perfil, actor.getCodigoUsuario(), SeguridadUtil.ipCliente(request));
        return Map.of("mensaje", "Perfil eliminado");
    }

    @PostMapping("/{perfil}/opciones/{opcion}")
    public Map<String, String> asignar(@PathVariable String perfil, @PathVariable String opcion,
            @SessionAttribute(SeguridadInterceptor.USUARIO_SESION) UsuarioSesion actor, HttpServletRequest request) {
        perfilServicio.asignar(perfil, opcion, actor.getCodigoUsuario(), SeguridadUtil.ipCliente(request));
        return Map.of("mensaje", "Opción asignada");
    }

    @DeleteMapping("/{perfil}/opciones/{opcion}")
    public Map<String, String> revocar(@PathVariable String perfil, @PathVariable String opcion,
            @SessionAttribute(SeguridadInterceptor.USUARIO_SESION) UsuarioSesion actor, HttpServletRequest request) {
        perfilServicio.revocar(perfil, opcion, actor.getCodigoUsuario(), SeguridadUtil.ipCliente(request));
        return Map.of("mensaje", "Opción revocada");
    }
}
