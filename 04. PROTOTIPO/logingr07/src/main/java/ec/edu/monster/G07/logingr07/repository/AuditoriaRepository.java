/*
 * SIGH - Hospital General Monster Salud
 * Talleres Login y CRUD MVC en Java - Grupo GR07
 * Subsistema de Seguridad
 */
package ec.edu.monster.G07.logingr07.repository;

import ec.edu.monster.G07.logingr07.modelo.Auditoria;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio JPA de Auditoria (tabla XEAUD_AUDIT).
 */
public interface AuditoriaRepository extends JpaRepository<Auditoria, Long> {

    List<Auditoria> findByUsuarioCodigoUsuario(String codigoUsuario);

    List<Auditoria> findTop300ByOrderByFechaHoraDesc();
}
