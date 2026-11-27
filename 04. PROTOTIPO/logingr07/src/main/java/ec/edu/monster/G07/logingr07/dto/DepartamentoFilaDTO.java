/*
 * SIGH - Hospital General Monster Salud
 * Talleres Login y CRUD MVC en Java - Grupo GR07
 * Subsistema de Personal
 */
package ec.edu.monster.G07.logingr07.dto;

/** Fila del listado de departamentos, con sus cargos y empleados. */
public record DepartamentoFilaDTO(String codigo, String descripcion, long cargos, long empleados) {
}
