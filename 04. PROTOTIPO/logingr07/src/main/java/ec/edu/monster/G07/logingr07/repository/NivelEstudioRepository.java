/*
 * SIGH - Hospital General Monster Salud
 * Talleres Login y CRUD MVC en Java - Grupo GR07
 * Subsistema de Personal
 */
package ec.edu.monster.G07.logingr07.repository;

import ec.edu.monster.G07.logingr07.modelo.NivelEstudio;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/** Repositorio JPA de NivelEstudio (tabla PENIV_NIVEL). */
public interface NivelEstudioRepository extends JpaRepository<NivelEstudio, String> {

    List<NivelEstudio> findAllByOrderByCodigoNivelAsc();
}
