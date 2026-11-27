/*
 * SIGH - Hospital General Monster Salud
 * Talleres Login y CRUD MVC en Java - Grupo GR07
 * Subsistema de Seguridad
 */
package ec.edu.monster.G07.logingr07.repository;

import ec.edu.monster.G07.logingr07.modelo.Empleado;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio JPA de Empleado: hereda listar, buscar, guardar y eliminar de JpaRepository.
 */
public interface EmpleadoRepository extends JpaRepository<Empleado, String> {

    /** Empleados asignados a un cargo (impide eliminar cargos en uso). */
    long countByCodigoDepartamento(String codigoDepartamento);

    long countByCodigoDepartamentoAndCodigoCargo(String codigoDepartamento, String codigoCargo);

    /** Mantenimiento de empleados (P003). */
    List<Empleado> findAllByOrderByApellidosAscNombresAsc();

    Optional<Empleado> findTopByOrderByCodigoEmpleadoDesc();

    boolean existsByCedula(String cedula);

    boolean existsByCedulaAndCodigoEmpleadoNot(String cedula, String codigoEmpleado);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCaseAndCodigoEmpleadoNot(String email, String codigoEmpleado);
}
