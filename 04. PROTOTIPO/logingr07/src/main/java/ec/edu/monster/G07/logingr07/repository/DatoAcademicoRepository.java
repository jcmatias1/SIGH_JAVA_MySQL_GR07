/*
 * SIGH - Hospital General Monster Salud
 * Talleres Login y CRUD MVC en Java - Grupo GR07
 * Subsistema de Personal
 */
package ec.edu.monster.G07.logingr07.repository;

import ec.edu.monster.G07.logingr07.modelo.DatoAcademico;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/** Repositorio JPA de DatoAcademico: filas hijas de un empleado (se reemplazan al guardar). */
public interface DatoAcademicoRepository extends JpaRepository<DatoAcademico, Integer> {

    List<DatoAcademico> findAllByOrderByCodigoAsc();

    @Modifying
    @Query("delete from DatoAcademico x where x.codigoEmpleado = :codigoEmpleado")
    void borrarDeEmpleado(@Param("codigoEmpleado") String codigoEmpleado);
}
