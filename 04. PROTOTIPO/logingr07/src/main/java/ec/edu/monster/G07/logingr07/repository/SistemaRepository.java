/*
 * SIGH - Hospital General Monster Salud
 * Talleres Login y CRUD MVC en Java - Grupo GR07
 * Subsistema de Seguridad
 */
package ec.edu.monster.G07.logingr07.repository;

import ec.edu.monster.G07.logingr07.modelo.Sistema;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio JPA de Sistema: hereda listar, buscar, guardar y eliminar de JpaRepository.
 */
public interface SistemaRepository extends JpaRepository<Sistema, String> {
}
