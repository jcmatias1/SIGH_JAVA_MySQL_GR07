/*
 * SIGH - Hospital General Monster Salud
 * Talleres Login y CRUD MVC en Java - Grupo GR07
 * Subsistema de Personal
 */
package ec.edu.monster.G07.logingr07.controlador;

import ec.edu.monster.G07.logingr07.config.SeguridadInterceptor;
import ec.edu.monster.G07.logingr07.dto.DepartamentoFilaDTO;
import ec.edu.monster.G07.logingr07.dto.DepartamentoFormDTO;
import ec.edu.monster.G07.logingr07.dto.UsuarioSesion;
import ec.edu.monster.G07.logingr07.servicio.DepartamentoServicio;
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
 * API JSON de departamentos.jsp (opción P001 «Administrar departamentos»).
 */
@RestController
@RequestMapping("/personal/departamentos/api")
public class DepartamentoApiControlador {

    private final DepartamentoServicio departamentoServicio;

    public DepartamentoApiControlador(DepartamentoServicio departamentoServicio) {
        this.departamentoServicio = departamentoServicio;
    }

    @GetMapping
    public List<DepartamentoFilaDTO> listar() {
        return departamentoServicio.listar();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DepartamentoFilaDTO crear(@RequestBody DepartamentoFormDTO datos,
            @SessionAttribute(SeguridadInterceptor.USUARIO_SESION) UsuarioSesion actor, HttpServletRequest request) {
        return departamentoServicio.crear(datos, actor.getCodigoUsuario(), SeguridadUtil.ipCliente(request));
    }

    @PutMapping("/{codigo}")
    public Map<String, String> actualizar(@PathVariable String codigo, @RequestBody DepartamentoFormDTO datos,
            @SessionAttribute(SeguridadInterceptor.USUARIO_SESION) UsuarioSesion actor, HttpServletRequest request) {
        departamentoServicio.actualizar(codigo, datos, actor.getCodigoUsuario(), SeguridadUtil.ipCliente(request));
        return Map.of("mensaje", "Departamento actualizado");
    }

    @DeleteMapping("/{codigo}")
    public Map<String, String> eliminar(@PathVariable String codigo,
            @SessionAttribute(SeguridadInterceptor.USUARIO_SESION) UsuarioSesion actor, HttpServletRequest request) {
        departamentoServicio.eliminar(codigo, actor.getCodigoUsuario(), SeguridadUtil.ipCliente(request));
        return Map.of("mensaje", "Departamento eliminado");
    }
}
