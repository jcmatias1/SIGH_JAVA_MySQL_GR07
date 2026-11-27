/*
 * SIGH - Hospital General Monster Salud
 * Talleres Login y CRUD MVC en Java - Grupo GR07
 * Subsistema de Personal
 */
package ec.edu.monster.G07.logingr07.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/** Empleado completo para las tablas y el formulario de Mantenimiento de empleados. */
public record EmpleadoFichaDTO(String codigoEmpleado, String cedula, String apellidos, String nombres,
        LocalDate fechaNacimiento, String codigoGenero, String genero, String codigoEstadoCivil, String estadoCivil,
        String codigoTipoSangre, BigDecimal peso, BigDecimal estatura,
        String registroProfesional, String estado, String codigoDepartamento, String departamento,
        String codigoCargo, String cargo, String direccion, String telefono, String email, boolean tieneUsuario,
        List<AcademicoDTO> academicos, List<FamiliarDTO> familiares) {
}
