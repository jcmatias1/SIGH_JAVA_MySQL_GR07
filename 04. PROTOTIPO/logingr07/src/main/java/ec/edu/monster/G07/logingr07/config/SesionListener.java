/*
 * SIGH - Hospital General Monster Salud
 * Talleres Login y CRUD MVC en Java - Grupo GR07
 * Subsistema de Seguridad
 */
package ec.edu.monster.G07.logingr07.config;

import ec.edu.monster.G07.logingr07.dto.UsuarioSesion;
import ec.edu.monster.G07.logingr07.servicio.AutenticacionServicio;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Escucha el fin de cada HttpSession. Si terminó sin «Cerrar sesión» (por inactividad o porque
 * se detuvo el servidor), cierra su registro en XESES_SESIO con estado E y lo audita (RF-SEG-004).
 */
@Component
public class SesionListener implements HttpSessionListener {

    private static final Logger LOG = LoggerFactory.getLogger(SesionListener.class);

    private final AutenticacionServicio autenticacionServicio;

    public SesionListener(AutenticacionServicio autenticacionServicio) {
        this.autenticacionServicio = autenticacionServicio;
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        HttpSession session = event.getSession();
        UsuarioSesion usuario = (UsuarioSesion) session.getAttribute(SeguridadInterceptor.USUARIO_SESION);
        if (usuario == null) {
            return; // sin usuario o ya cerrada desde «Cerrar sesión»
        }
        long inactivo = System.currentTimeMillis() - session.getLastAccessedTime();
        boolean porInactividad = session.getMaxInactiveInterval() > 0
                && inactivo >= session.getMaxInactiveInterval() * 1000L;
        String detalle = porInactividad
                ? "Sesión " + usuario.getCodigoSesion() + " cerrada por " + (session.getMaxInactiveInterval() / 60) + " minutos de inactividad"
                : "Sesión " + usuario.getCodigoSesion() + " finalizada al detener el servidor";
        try {
            autenticacionServicio.cerrarSesion(usuario.getCodigoSesion(), usuario.getCodigoUsuario(),
                    usuario.getDireccionIp(), "E", detalle);
        } catch (RuntimeException ex) {
            LOG.warn("[SIGH] No se pudo registrar el fin de la sesión {}: {}", usuario.getCodigoSesion(), ex.getMessage());
        }
    }
}
