/*
 * SIGH - Hospital General Monster Salud
 * Talleres Login y CRUD MVC en Java - Grupo GR07
 * Subsistema de Personal
 */
package ec.edu.monster.G07.logingr07.dto;

/** Datos del formulario Administrar Cargo: Código Cargo, Descripción Cargo y Departamento. */
public record CargoFormDTO(String codigoCargo, String descripcion, String codigoDepartamento) {
}
