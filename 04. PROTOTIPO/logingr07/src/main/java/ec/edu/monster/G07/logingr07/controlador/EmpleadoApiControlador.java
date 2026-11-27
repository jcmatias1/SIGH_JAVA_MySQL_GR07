/*
 * SIGH - Hospital General Monster Salud
 * Talleres Login y CRUD MVC en Java - Grupo GR07
 * Subsistema de Personal
 */
package ec.edu.monster.G07.logingr07.controlador;

import ec.edu.monster.G07.logingr07.config.SeguridadInterceptor;
import ec.edu.monster.G07.logingr07.dto.EmpleadoFichaDTO;
import ec.edu.monster.G07.logingr07.dto.EmpleadoFormDTO;
import ec.edu.monster.G07.logingr07.dto.UsuarioSesion;
import ec.edu.monster.G07.logingr07.servicio.CargoServicio;
import ec.edu.monster.G07.logingr07.servicio.EmpleadoServicio;
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
 * API JSON de empleados.jsp (opción P003 «Mantenimiento de empleados»). DELETE hace borrado lógico.
 */
@RestController
@RequestMapping("/personal/empleados/api")
public class EmpleadoApiControlador {

    private final EmpleadoServicio empleadoServicio;
    private final CargoServicio cargoServicio;

    public EmpleadoApiControlador(EmpleadoServicio empleadoServicio, CargoServicio cargoServicio) {
        this.empleadoServicio = empleadoServicio;
        this.cargoServicio = cargoServicio;
    }

    @GetMapping
    public List<EmpleadoFichaDTO> listar() {
        return empleadoServicio.listar();
    }

    /** Combos del formulario y el código que tendrá el próximo empleado. */
    @GetMapping("/catalogos")
    public Map<String, Object> catalogos() {
        return Map.of(
                "generos", empleadoServicio.generos(),
                "estadosCiviles", empleadoServicio.estadosCiviles(),
                "tiposSangre", empleadoServicio.tiposSangre(),
                "niveles", empleadoServicio.niveles(),
                "parentescos", empleadoServicio.parentescos(),
                "cargos", cargoServicio.listar(),
                "siguienteCodigo", empleadoServicio.siguienteCodigo());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmpleadoFichaDTO crear(@RequestBody EmpleadoFormDTO datos,
            @SessionAttribute(SeguridadInterceptor.USUARIO_SESION) UsuarioSesion actor, HttpServletRequest request) {
        return empleadoServicio.crear(datos, actor.getCodigoUsuario(), SeguridadUtil.ipCliente(request));
    }

    @PutMapping("/{codigo}")
    public Map<String, String> actualizar(@PathVariable String codigo, @RequestBody EmpleadoFormDTO datos,
            @SessionAttribute(SeguridadInterceptor.USUARIO_SESION) UsuarioSesion actor, HttpServletRequest request) {
        empleadoServicio.actualizar(codigo, datos, actor.getCodigoUsuario(), SeguridadUtil.ipCliente(request));
        return Map.of("mensaje", "Empleado actualizado");
    }

    @DeleteMapping("/{codigo}")
    public Map<String, String> borrar(@PathVariable String codigo,
            @SessionAttribute(SeguridadInterceptor.USUARIO_SESION) UsuarioSesion actor, HttpServletRequest request) {
        empleadoServicio.borrar(codigo, actor.getCodigoUsuario(), SeguridadUtil.ipCliente(request));
        return Map.of("mensaje", "Empleado desactivado");
    }
}
