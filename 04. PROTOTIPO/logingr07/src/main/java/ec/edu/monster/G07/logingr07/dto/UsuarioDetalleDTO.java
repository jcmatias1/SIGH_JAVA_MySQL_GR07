/*
 * SIGH - Hospital General Monster Salud
 * Talleres Login y CRUD MVC en Java - Grupo GR07
 * Subsistema de Seguridad
 */
package ec.edu.monster.G07.logingr07.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * READ (consulta individual): datos completos de una cuenta para la ventana «Ver detalle».
 */
public record UsuarioDetalleDTO(
        UsuarioDTO cuenta,
        String cargo,
        String departamento,
        String telefono,
        String registroProfesional,
        LocalDateTime fechaModificacion,
        List<PerfilHistorial> perfiles,
        List<SesionResumen> sesiones) {

    /** Asignación de perfil (vigente si fechaRetiro es null). */
    public record PerfilHistorial(String codigoPerfil, String descripcion, LocalDateTime fechaAsignacion,
            LocalDateTime fechaRetiro) {
    }

    /** Sesión registrada en XESES_SESIO. */
    public record SesionResumen(Long codigo, LocalDateTime inicio, LocalDateTime fin, String ip, String estado) {
    }
}
