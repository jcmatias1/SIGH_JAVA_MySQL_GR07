/*
 * SIGH - Hospital General Monster Salud
 * Talleres Login y CRUD MVC en Java - Grupo GR07
 * Subsistema de Seguridad
 */
package ec.edu.monster.G07.logingr07.dto;

/**
 * Empleado disponible para crear una cuenta (PEEMP_EMPLE sin usuario).
 */
public record EmpleadoDTO(String codigoEmpleado, String nombreCompleto, String cedula, String email, String cargo) {
}
