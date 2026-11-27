/*
 * SIGH - Hospital General Monster Salud
 * Talleres Login y CRUD MVC en Java - Grupo GR07
 * Subsistema de Personal
 */
package ec.edu.monster.G07.logingr07.repository;

import ec.edu.monster.G07.logingr07.modelo.Departamento;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio JPA de Departamento (tabla PEDEP_DEPAR).
 */
public interface DepartamentoRepository extends JpaRepository<Departamento, String> {

    List<Departamento> findAllByOrderByDescripcionAsc();

    boolean existsByDescripcionIgnoreCase(String descripcion);

    boolean existsByDescripcionIgnoreCaseAndCodigoDepartamentoNot(String descripcion, String codigoDepartamento);
}
