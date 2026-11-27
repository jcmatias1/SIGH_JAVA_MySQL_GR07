/*
 * SIGH - Hospital General Monster Salud
 * Talleres Login y CRUD MVC en Java - Grupo GR07
 * Subsistema de Personal
 */
package ec.edu.monster.G07.logingr07.dto;

/** Fila de la grilla de datos familiares (fecha en formato aaaa-mm-dd). */
public record FamiliarDTO(String codigoParentesco, String parentesco, String apellidos, String nombres,
        String fechaNacimiento) {
}
