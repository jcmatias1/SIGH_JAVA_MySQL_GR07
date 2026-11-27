/*
 * SIGH - Hospital General Monster Salud
 * Talleres Login y CRUD MVC en Java - Grupo GR07
 * Subsistema de Personal
 */
package ec.edu.monster.G07.logingr07.dto;

/** Fila del listado de cargos. */
public record CargoDTO(String codigoCargo, String descripcion, String codigoDepartamento, String departamento,
        long empleados) {
}
