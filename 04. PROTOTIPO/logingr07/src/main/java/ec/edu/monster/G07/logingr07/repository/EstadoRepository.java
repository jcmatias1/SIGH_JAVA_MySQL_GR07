/*
 * SIGH - Hospital General Monster Salud
 * Talleres Login y CRUD MVC en Java - Grupo GR07
 * Subsistema de Seguridad
 */
package ec.edu.monster.G07.logingr07.repository;

import ec.edu.monster.G07.logingr07.modelo.Estado;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio JPA de Estado: hereda listar, buscar, guardar y eliminar de JpaRepository.
 */
public interface EstadoRepository extends JpaRepository<Estado, String> {
}
