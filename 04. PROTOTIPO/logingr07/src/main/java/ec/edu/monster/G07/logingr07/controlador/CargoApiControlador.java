/*
 * SIGH - Hospital General Monster Salud
 * Talleres Login y CRUD MVC en Java - Grupo GR07
 * Subsistema de Personal
 */
package ec.edu.monster.G07.logingr07.controlador;

import ec.edu.monster.G07.logingr07.config.SeguridadInterceptor;
import ec.edu.monster.G07.logingr07.dto.CargoDTO;
import ec.edu.monster.G07.logingr07.dto.CargoFormDTO;
import ec.edu.monster.G07.logingr07.dto.DepartamentoDTO;
import ec.edu.monster.G07.logingr07.dto.UsuarioSesion;
import ec.edu.monster.G07.logingr07.servicio.CargoServicio;
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
 * API JSON de cargos.jsp (opción P002 «Administrar cargos»): CRUD de cargos y combo de departamentos.
 */
@RestController
@RequestMapping("/personal/cargos/api")
public class CargoApiControlador {

    private final CargoServicio cargoServicio;

    public CargoApiControlador(CargoServicio cargoServicio) {
        this.cargoServicio = cargoServicio;
    }

    @GetMapping
    public List<CargoDTO> listar() {
        return cargoServicio.listar();
    }

    @GetMapping("/departamentos")
    public List<DepartamentoDTO> departamentos() {
        return cargoServicio.departamentos();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CargoDTO crear(@RequestBody CargoFormDTO datos,
            @SessionAttribute(SeguridadInterceptor.USUARIO_SESION) UsuarioSesion actor, HttpServletRequest request) {
        return cargoServicio.crear(datos, actor.getCodigoUsuario(), SeguridadUtil.ipCliente(request));
    }

    @PutMapping("/{departamento}/{cargo}")
    public Map<String, String> actualizar(@PathVariable String departamento, @PathVariable String cargo,
            @RequestBody CargoFormDTO datos,
            @SessionAttribute(SeguridadInterceptor.USUARIO_SESION) UsuarioSesion actor, HttpServletRequest request) {
        cargoServicio.actualizar(departamento, cargo, datos, actor.getCodigoUsuario(), SeguridadUtil.ipCliente(request));
        return Map.of("mensaje", "Cargo actualizado");
    }

    @DeleteMapping("/{departamento}/{cargo}")
    public Map<String, String> eliminar(@PathVariable String departamento, @PathVariable String cargo,
            @SessionAttribute(SeguridadInterceptor.USUARIO_SESION) UsuarioSesion actor, HttpServletRequest request) {
        cargoServicio.eliminar(departamento, cargo, actor.getCodigoUsuario(), SeguridadUtil.ipCliente(request));
        return Map.of("mensaje", "Cargo eliminado");
    }
}
