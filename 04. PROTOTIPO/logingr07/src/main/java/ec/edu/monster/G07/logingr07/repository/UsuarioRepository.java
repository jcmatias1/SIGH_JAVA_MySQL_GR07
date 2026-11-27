/*
 * SIGH - Hospital General Monster Salud
 * Talleres Login y CRUD MVC en Java - Grupo GR07
 * Subsistema de Seguridad
 */
package ec.edu.monster.G07.logingr07.repository;

import ec.edu.monster.G07.logingr07.modelo.Usuario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio JPA de Usuario (tabla XEUSU_USUAR).
 */
public interface UsuarioRepository extends JpaRepository<Usuario, String> {

    List<Usuario> findByEstadoCodigoEstado(String codigoEstado);

    /** Busca la cuenta por el correo institucional del empleado (recuperación de contraseña). */
    Optional<Usuario> findFirstByEmpleadoEmailIgnoreCase(String email);

    boolean existsByEmpleadoCodigoEmpleado(String codigoEmpleado);

    /** Cuenta no inactiva de un empleado (impide el borrado lógico del empleado). */
    boolean existsByEmpleadoCodigoEmpleadoAndEstadoCodigoEstadoNot(String codigoEmpleado, String codigoEstado);
}
