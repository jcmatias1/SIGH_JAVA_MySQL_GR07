/*
 * SIGH - Hospital General Monster Salud
 * Talleres Login y CRUD MVC en Java - Grupo GR07
 * Subsistema de Seguridad
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
 * Páginas JSP del sistema SEGURIDAD. El permiso de cada una (S001, S002, S003) lo verifica
 * el SeguridadInterceptor antes de llegar aquí.
 */
@Controller
public class SeguridadControlador {

    private final MenuServicio menuServicio;

    public SeguridadControlador(MenuServicio menuServicio) {
        this.menuServicio = menuServicio;
    }

    @GetMapping("/seguridad/usuarios")
    public String usuarios(@SessionAttribute(SeguridadInterceptor.USUARIO_SESION) UsuarioSesion usuario, Model model) {
        model.addAttribute("menu", menuServicio.menu(usuario.getCodigoUsuario()));
        return "usuarios";
    }

    @GetMapping("/seguridad/perfiles")
    public String perfiles(@SessionAttribute(SeguridadInterceptor.USUARIO_SESION) UsuarioSesion usuario, Model model) {
        model.addAttribute("menu", menuServicio.menu(usuario.getCodigoUsuario()));
        return "perfiles";
    }

    @GetMapping("/seguridad/auditoria")
    public String auditoria(@SessionAttribute(SeguridadInterceptor.USUARIO_SESION) UsuarioSesion usuario, Model model) {
        model.addAttribute("menu", menuServicio.menu(usuario.getCodigoUsuario()));
        return "auditoria";
    }
}
