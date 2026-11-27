/*
 * SIGH - Hospital General Monster Salud
 * Talleres Login y CRUD MVC en Java - Grupo GR07
 * Subsistema de Seguridad
 */
package ec.edu.monster.G07.logingr07.config;

import ec.edu.monster.G07.logingr07.dto.UsuarioSesion;
import ec.edu.monster.G07.logingr07.modelo.Opcion;
import ec.edu.monster.G07.logingr07.servicio.AuditoriaServicio;
import ec.edu.monster.G07.logingr07.servicio.MenuServicio;
import ec.edu.monster.G07.logingr07.utils.SeguridadUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Control de acceso de cada petición (RF-SEG-003, RF-SEG-004, RF-AUD-004):
 * 1) exige una sesión iniciada; si expiró, regresa al login;
 * 2) si la ruta pertenece a una opción (XEOPC_URL), verifica que algún perfil vigente del
 *    usuario tenga esa opción. Así, escribir la URL directamente tampoco salta los permisos.
 */
@Component
public class SeguridadInterceptor implements HandlerInterceptor {

    public static final String USUARIO_SESION = "usuarioSesion";

    private final MenuServicio menuServicio;
    private final AuditoriaServicio auditoria;

    public SeguridadInterceptor(MenuServicio menuServicio, AuditoriaServicio auditoria) {
        this.menuServicio = menuServicio;
        this.auditoria = auditoria;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ruta = request.getRequestURI().substring(request.getContextPath().length());
        boolean esApi = ruta.contains("/api");

        // Las páginas protegidas no se guardan en caché: tras cerrar sesión, «Atrás» no las muestra.
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.setHeader("Pragma", "no-cache");

        HttpSession session = request.getSession(false);
        UsuarioSesion usuario = session == null ? null : (UsuarioSesion) session.getAttribute(USUARIO_SESION);
        if (usuario == null) {
            boolean expirada = request.getRequestedSessionId() != null && !request.isRequestedSessionIdValid();
            if (esApi) {
                responderJson(response, HttpServletResponse.SC_UNAUTHORIZED,
                        "Su sesión terminó. Inicie sesión nuevamente.");
            } else {
                response.sendRedirect(request.getContextPath() + (expirada ? "/login?expirada" : "/login"));
            }
            return false;
        }

        Optional<Opcion> opcion = menuServicio.opcionRequerida(ruta);
        if (opcion.isPresent() && !menuServicio.tieneOpcion(usuario.getCodigoUsuario(), opcion.get().getCodigoOpcion())) {
            auditoria.registrar(usuario.getCodigoUsuario(), "ACCESO DENEGADO", "DENEGADO",
                    request.getMethod() + " " + ruta + " requiere la opción " + opcion.get().getCodigoOpcion(),
                    SeguridadUtil.ipCliente(request));
            if (esApi) {
                responderJson(response, HttpServletResponse.SC_FORBIDDEN, "Su perfil no tiene permiso para esta operación.");
            } else {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                request.setAttribute("opcionDenegada", opcion.get().getDescripcionOpcion());
                request.setAttribute("menu", menuServicio.menu(usuario.getCodigoUsuario()));
                request.getRequestDispatcher("/WEB-INF/jsp/acceso-denegado.jsp").forward(request, response);
            }
            return false;
        }
        return true;
    }

    private static void responderJson(HttpServletResponse response, int estado, String mensaje) throws Exception {
        response.setStatus(estado);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"mensaje\":\"" + mensaje.replace("\"", "'") + "\"}");
    }
}
