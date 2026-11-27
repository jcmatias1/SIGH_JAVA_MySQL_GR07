/*
 * SIGH - Hospital General Monster Salud
 * Talleres Login y CRUD MVC en Java - Grupo GR07
 * Subsistema de Personal
 */
package ec.edu.monster.G07.logingr07.repository;

import ec.edu.monster.G07.logingr07.modelo.Parentesco;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/** Repositorio JPA de Parentesco (tabla PEPAR_PAREN). */
public interface ParentescoRepository extends JpaRepository<Parentesco, String> {

    List<Parentesco> findAllByOrderByDescripcionAsc();
}
