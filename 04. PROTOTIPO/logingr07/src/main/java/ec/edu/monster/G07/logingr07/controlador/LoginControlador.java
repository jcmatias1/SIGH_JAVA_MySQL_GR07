/*
 * SIGH - Hospital General Monster Salud
 * Talleres Login y CRUD MVC en Java - Grupo GR07
 * Subsistema de Seguridad
 */
package ec.edu.monster.G07.logingr07.controlador;

import ec.edu.monster.G07.logingr07.config.SeguridadInterceptor;
import ec.edu.monster.G07.logingr07.dto.UsuarioSesion;
import ec.edu.monster.G07.logingr07.servicio.AutenticacionServicio;
import ec.edu.monster.G07.logingr07.servicio.MenuServicio;
import ec.edu.monster.G07.logingr07.utils.SeguridadUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

/**
 * Controlador del caso de uso ECUD1: Iniciar sesión, menú principal y Cerrar sesión.
 */
@Controller
public class LoginControlador {

    private final AutenticacionServicio autenticacionServicio;
    private final MenuServicio menuServicio;

    public LoginControlador(AutenticacionServicio autenticacionServicio, MenuServicio menuServicio) {
        this.autenticacionServicio = autenticacionServicio;
        this.menuServicio = menuServicio;
    }

    @GetMapping("/")
    public String raiz(HttpSession session) {
        return session.getAttribute(SeguridadInterceptor.USUARIO_SESION) != null ? "redirect:/inicio" : "redirect:/login";
    }

    /** Paso 1: el sistema muestra el formulario con los campos usuario y contraseña. */
    @GetMapping("/login")
    public String mostrarLogin(HttpSession session, Model model,
            @RequestParam(required = false) String expirada,
            @RequestParam(required = false) String salir,
            @RequestParam(required = false) String restablecida) {
        if (session.getAttribute(SeguridadInterceptor.USUARIO_SESION) != null) {
            return "redirect:/inicio";
        }
        if (expirada != null) {
            model.addAttribute("aviso", "Su sesión se cerró por inactividad. Inicie sesión nuevamente.");
        } else if (salir != null) {
            model.addAttribute("info", "Cerró sesión correctamente.");
        } else if (restablecida != null) {
            model.addAttribute("info", "Su contraseña fue actualizada. Ya puede iniciar sesión.");
        }
        return "login";
    }

    /** Paso 2: valida credenciales y estado; si es correcto crea la sesión y registra el acceso. */
    @PostMapping("/login")
    public String iniciarSesion(@RequestParam("codigoUsuario") String codigoUsuario,
            @RequestParam("password") String password,
            HttpServletRequest request, Model model) {
        AutenticacionServicio.Resultado resultado = autenticacionServicio.autenticar(
                codigoUsuario, password, SeguridadUtil.ipCliente(request));

        if (!resultado.exitoso()) {
            model.addAttribute("error", resultado.mensaje());
            model.addAttribute("tipoError", resultado.tipo().name());
            model.addAttribute("codigoUsuario", codigoUsuario);
            return "login";
        }
        request.getSession(true);
        request.changeSessionId(); // evita la fijación de sesión
        request.getSession().setAttribute(SeguridadInterceptor.USUARIO_SESION, resultado.usuario());
        return "redirect:/bienvenida";
    }

    @GetMapping("/bienvenida")
    public String bienvenida() {
        return "login-exitoso";
    }

    /** Paso 3: menú con los módulos habilitados para el rol del usuario. */
    @GetMapping("/inicio")
    public String inicio(@SessionAttribute(SeguridadInterceptor.USUARIO_SESION) UsuarioSesion usuario,
            HttpServletRequest request, Model model) {
        model.addAttribute("menu", menuServicio.menu(usuario.getCodigoUsuario()));
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        List<Map<String, String>> sesiones = autenticacionServicio.ultimasSesiones(usuario.getCodigoUsuario()).stream()
                .map(s -> Map.of(
                        "codigo", String.valueOf(s.getCodigoSesion()),
                        "inicio", s.getFechaInicio().format(formato),
                        "fin", s.getFechaFin() == null ? "En curso" : s.getFechaFin().format(formato),
                        "ip", s.getDireccionIp() == null ? "—" : s.getDireccionIp(),
                        "estado", s.getEstadoSesion(),
                        "actual", String.valueOf(s.getCodigoSesion().equals(usuario.getCodigoSesion()))))
                .toList();
        model.addAttribute("sesiones", sesiones);
        model.addAttribute("segundosSesion", request.getSession().getMaxInactiveInterval());
        return "inicio";
    }

    /** CERRAR SESIÓN: finaliza la sesión, registra el evento y vuelve al login. */
    @PostMapping("/logout")
    public String cerrarSesion(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            UsuarioSesion usuario = (UsuarioSesion) session.getAttribute(SeguridadInterceptor.USUARIO_SESION);
            if (usuario != null) {
                autenticacionServicio.cerrarSesion(usuario.getCodigoSesion(), usuario.getCodigoUsuario(),
                        SeguridadUtil.ipCliente(request), "C", "Sesión " + usuario.getCodigoSesion() + " cerrada por el usuario");
                session.removeAttribute(SeguridadInterceptor.USUARIO_SESION);
            }
            session.invalidate();
        }
        return "redirect:/login?salir";
    }

    @GetMapping("/logout")
    public String logoutGet() {
        return "redirect:/inicio";
    }
}
