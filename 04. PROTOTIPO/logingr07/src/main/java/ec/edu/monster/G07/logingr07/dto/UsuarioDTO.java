/*
 * SIGH - Hospital General Monster Salud
 * Talleres Login y CRUD MVC en Java - Grupo GR07
 * Subsistema de Seguridad
 */
package ec.edu.monster.G07.logingr07.dto;

import java.time.LocalDateTime;

/**
 * Datos que intercambian la pantalla de administración de usuarios (ECUD2) y el controlador.
 * La contraseña solo se recibe; nunca se envía al navegador.
 */
public record UsuarioDTO(
        String codigoUsuario,
        String codigoEmpleado,
        String nombreEmpleado,
        String cedula,
        String email,
        String codigoEstado,
        String descripcionEstado,
        String codigoPerfil,
        String descripcionPerfil,
        String pieFirma,
        Short intentosFallidos,
        LocalDateTime fechaCreacion,
        LocalDateTime ultimoAcceso,
        String password) {
}
