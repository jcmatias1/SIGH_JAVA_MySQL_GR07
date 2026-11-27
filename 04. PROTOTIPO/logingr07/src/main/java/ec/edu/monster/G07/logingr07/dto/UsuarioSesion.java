/*
 * SIGH - Hospital General Monster Salud
 * Talleres Login y CRUD MVC en Java - Grupo GR07
 * Subsistema de Seguridad
 */
package ec.edu.monster.G07.logingr07.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Datos del usuario autenticado que se guardan en la HttpSession.
 * Se usa una clase con getters (no la entidad) para que las vistas JSP la lean con EL.
 */
public class UsuarioSesion implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final String codigoUsuario;
    private final String nombreCompleto;
    private final String iniciales;
    private final String pieFirma;
    private final List<String> perfiles;
    private final Long codigoSesion;
    private final LocalDateTime inicioSesion;
    private final LocalDateTime ultimoAccesoAnterior;
    private final String direccionIp;

    public UsuarioSesion(String codigoUsuario, String nombreCompleto, String iniciales, String pieFirma,
            List<String> perfiles, Long codigoSesion, LocalDateTime inicioSesion,
            LocalDateTime ultimoAccesoAnterior, String direccionIp) {
        this.codigoUsuario = codigoUsuario;
        this.nombreCompleto = nombreCompleto;
        this.iniciales = iniciales;
        this.pieFirma = pieFirma;
        this.perfiles = List.copyOf(perfiles);
        this.codigoSesion = codigoSesion;
        this.inicioSesion = inicioSesion;
        this.ultimoAccesoAnterior = ultimoAccesoAnterior;
        this.direccionIp = direccionIp;
    }

    public String getCodigoUsuario() {
        return codigoUsuario;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getIniciales() {
        return iniciales;
    }

    public String getPieFirma() {
        return pieFirma;
    }

    public List<String> getPerfiles() {
        return perfiles;
    }

    public String getPerfilesTexto() {
        return String.join(", ", perfiles);
    }

    public Long getCodigoSesion() {
        return codigoSesion;
    }

    public LocalDateTime getInicioSesion() {
        return inicioSesion;
    }

    public LocalDateTime getUltimoAccesoAnterior() {
        return ultimoAccesoAnterior;
    }

    public String getDireccionIp() {
        return direccionIp;
    }

    /** Fecha de inicio de la sesión formateada para la vista. */
    public String getInicioSesionTexto() {
        return inicioSesion == null ? "—" : inicioSesion.format(FORMATO);
    }

    /** Último acceso anterior al actual (o «Primer acceso»). */
    public String getUltimoAccesoTexto() {
        return ultimoAccesoAnterior == null ? "Primer acceso" : ultimoAccesoAnterior.format(FORMATO);
    }
}
