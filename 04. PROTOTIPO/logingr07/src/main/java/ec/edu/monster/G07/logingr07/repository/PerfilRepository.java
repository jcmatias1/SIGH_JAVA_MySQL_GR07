/*
 * SIGH - Hospital General Monster Salud
 * Talleres Login y CRUD MVC en Java - Grupo GR07
 * Subsistema de Seguridad
 */
package ec.edu.monster.G07.logingr07.repository;

import ec.edu.monster.G07.logingr07.modelo.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio JPA de Perfil (XEPER_PERFI): hereda listar, buscar, guardar y eliminar de JpaRepository.
 */
public interface PerfilRepository extends JpaRepository<Perfil, String> {

    /** Evita dos perfiles con el mismo nombre (sin distinguir mayúsculas). */
    boolean existsByDescripcionPerfilIgnoreCase(String descripcionPerfil);

    boolean existsByDescripcionPerfilIgnoreCaseAndCodigoPerfilNot(String descripcionPerfil, String codigoPerfil);
}
