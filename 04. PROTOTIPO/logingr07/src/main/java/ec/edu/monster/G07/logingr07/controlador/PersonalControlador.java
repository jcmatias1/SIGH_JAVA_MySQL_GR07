/*
 * SIGH - Hospital General Monster Salud
 * Talleres Login y CRUD MVC en Java - Grupo GR07
 * Subsistema de Personal
 */
package ec.edu.monster.G07.logingr07.controlador;

import ec.edu.monster.G07.logingr07.config.SeguridadInterceptor;
import ec.edu.monster.G07.logingr07.dto.UsuarioSesion;
import ec.edu.monster.G07.logingr07.servicio.MenuServicio;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

/**
 * Páginas JSP del sistema PERSONAL. Los permisos (P001 departamentos, P002 cargos, P003 empleados) los verifica el SeguridadInterceptor.
 */
@Controller
public class PersonalControlador {

    private final MenuServicio menuServicio;

    public PersonalControlador(MenuServicio menuServicio) {
        this.menuServicio = menuServicio;
    }

    @GetMapping("/personal/departamentos")
    public String departamentos(@SessionAttribute(SeguridadInterceptor.USUARIO_SESION) UsuarioSesion usuario,
            Model model) {
        model.addAttribute("menu", menuServicio.menu(usuario.getCodigoUsuario()));
        return "departamentos";
    }

    @GetMapping("/personal/cargos")
    public String cargos(@SessionAttribute(SeguridadInterceptor.USUARIO_SESION) UsuarioSesion usuario, Model model) {
        model.addAttribute("menu", menuServicio.menu(usuario.getCodigoUsuario()));
        return "cargos";
    }

    @GetMapping("/personal/empleados")
    public String empleados(@SessionAttribute(SeguridadInterceptor.USUARIO_SESION) UsuarioSesion usuario, Model model) {
        model.addAttribute("menu", menuServicio.menu(usuario.getCodigoUsuario()));
        return "empleados";
    }
}
