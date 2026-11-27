/*
 * SIGH - Hospital General Monster Salud
 * Talleres Login y CRUD MVC en Java - Grupo GR07
 * Subsistema de Seguridad
 */
package ec.edu.monster.G07.logingr07.repository;

import ec.edu.monster.G07.logingr07.modelo.Sesion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio JPA de Sesion (tabla XESES_SESIO).
 */
public interface SesionRepository extends JpaRepository<Sesion, Long> {

    List<Sesion> findByUsuarioCodigoUsuario(String codigoUsuario);

    List<Sesion> findTop5ByUsuarioCodigoUsuarioOrderByFechaInicioDesc(String codigoUsuario);
}
