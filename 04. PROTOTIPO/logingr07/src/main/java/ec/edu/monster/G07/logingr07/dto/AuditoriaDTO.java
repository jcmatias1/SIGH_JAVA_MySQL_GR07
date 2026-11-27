/*
 * SIGH - Hospital General Monster Salud
 * Talleres Login y CRUD MVC en Java - Grupo GR07
 * Subsistema de Seguridad
 */
package ec.edu.monster.G07.logingr07.dto;

import java.time.LocalDateTime;

/**
 * Evento de auditoría (XEAUD_AUDIT) para la consulta de auditoría.
 */
public record AuditoriaDTO(Long codigo, LocalDateTime fechaHora, String usuario, String modulo,
        String operacion, String resultado, String detalle, String direccionIp) {
}
