/*
 * SIGH - Hospital General Monster Salud
 * Talleres Login y CRUD MVC en Java - Grupo GR07
 * Subsistema de Seguridad
 */
package ec.edu.monster.G07.logingr07.controlador;

import ec.edu.monster.G07.logingr07.servicio.RecuperacionPasswordServicio;
import ec.edu.monster.G07.logingr07.utils.SeguridadUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * Flujo alterno RECUPERAR CONTRASEÑA del ECUD1 (RF-SEG-005).
 */
@Controller
public class RecuperacionPasswordControlador {

    private final RecuperacionPasswordServicio recuperacionServicio;

    public RecuperacionPasswordControlador(RecuperacionPasswordServicio recuperacionServicio) {
        this.recuperacionServicio = recuperacionServicio;
    }

    @GetMapping("/recuperar-password")
    public String mostrarRecuperacion() {
        return "recuperar-password";
    }

    @PostMapping("/recuperar-password")
    public String solicitar(@RequestParam("identificador") String identificador, HttpServletRequest request, Model model) {
        String urlBase = ServletUriComponentsBuilder.fromContextPath(request).toUriString();
        RecuperacionPasswordServicio.Solicitud solicitud = recuperacionServicio.solicitar(
                identificador, urlBase, SeguridadUtil.ipCliente(request));
        if (!solicitud.permitida()) {
            model.addAttribute("error", "Se enviaron demasiados enlaces de recuperación. Intente nuevamente más tarde.");
        } else {
            model.addAttribute("mensaje", "Si los datos corresponden a una cuenta habilitada, enviamos un enlace "
                    + "temporal a su correo institucional. Revise su bandeja de entrada.");
            model.addAttribute("enlaceDemo", solicitud.enlaceDemo());
        }
        model.addAttribute("identificador", identificador);
        return "recuperar-password";
    }

    @GetMapping("/restablecer-password")
    public String mostrarRestablecer(@RequestParam(value = "token", required = false) String token, Model model) {
        model.addAttribute("token", token);
        model.addAttribute("tokenValido", recuperacionServicio.tokenValido(token));
        return "restablecer-password";
    }

    @PostMapping("/restablecer-password")
    public String restablecer(@RequestParam("token") String token,
            @RequestParam("password") String password,
            @RequestParam("confirmacion") String confirmacion,
            HttpServletRequest request, Model model) {
        String error = recuperacionServicio.restablecer(token, password, confirmacion, SeguridadUtil.ipCliente(request));
        if (error == null) {
            return "redirect:/login?restablecida";
        }
        model.addAttribute("token", token);
        model.addAttribute("tokenValido", recuperacionServicio.tokenValido(token));
        model.addAttribute("error", error);
        return "restablecer-password";
    }
}
