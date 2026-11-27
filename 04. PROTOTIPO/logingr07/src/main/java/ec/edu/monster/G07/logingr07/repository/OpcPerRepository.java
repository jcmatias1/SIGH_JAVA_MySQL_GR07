/*
 * SIGH - Hospital General Monster Salud
 * Talleres Login y CRUD MVC en Java - Grupo GR07
 * Subsistema de Seguridad
 */
package ec.edu.monster.G07.logingr07.repository;

import ec.edu.monster.G07.logingr07.modelo.OpcPer;
import ec.edu.monster.G07.logingr07.modelo.OpcPerId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio JPA de OpcPer (tabla XEOXP_OPCPE): opciones (permisos) de cada perfil.
 */
public interface OpcPerRepository extends JpaRepository<OpcPer, OpcPerId> {

    List<OpcPer> findByIdCodigoPerfil(String codigoPerfil);

    /** Quita la configuración de opciones de un perfil antes de eliminarlo. */
    void deleteByIdCodigoPerfil(String codigoPerfil);
}
