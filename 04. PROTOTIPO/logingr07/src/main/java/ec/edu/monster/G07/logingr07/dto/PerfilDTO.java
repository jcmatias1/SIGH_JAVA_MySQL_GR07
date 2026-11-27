/*
 * SIGH - Hospital General Monster Salud
 * Talleres Login y CRUD MVC en Java - Grupo GR07
 * Subsistema de Seguridad
 */
package ec.edu.monster.G07.logingr07.dto;

import java.util.List;

/**
 * Perfil con sus opciones (asignadas o no) para la pantalla de perfiles y permisos.
 */
public record PerfilDTO(String codigoPerfil, String descripcion, String observaciones,
        long usuariosActivos, long asignacionesHistoricas, List<OpcionPermisoDTO> opciones) {

    /** Opción de un sistema y si está asignada (vigente) al perfil. */
    public record OpcionPermisoDTO(String codigoOpcion, String descripcion, String codigoSistema,
            String sistema, String url, boolean asignada) {
    }
}
