/*
 * SIGH - Hospital General Monster Salud
 * Talleres Login y CRUD MVC en Java - Grupo GR07
 * Subsistema de Personal
 */
package ec.edu.monster.G07.logingr07.repository;

import ec.edu.monster.G07.logingr07.modelo.Cargo;
import ec.edu.monster.G07.logingr07.modelo.CargoId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Repositorio JPA de Cargo (tabla PECAR_CARGO).
 */
public interface CargoRepository extends JpaRepository<Cargo, CargoId> {

    @Query("""
            select c
            from Cargo c
              join fetch c.departamento d
            order by d.descripcion, c.descripcion
            """)
    List<Cargo> listarConDepartamento();

    /** Evita dos cargos con el mismo nombre dentro de un departamento. */
    /** Cargos de un departamento (impide eliminar departamentos en uso). */
    long countByIdCodigoDepartamento(String codigoDepartamento);

    boolean existsByIdCodigoDepartamentoAndDescripcionIgnoreCase(String codigoDepartamento, String descripcion);

    boolean existsByIdCodigoDepartamentoAndDescripcionIgnoreCaseAndIdCodigoCargoNot(String codigoDepartamento,
            String descripcion, String codigoCargo);
}
