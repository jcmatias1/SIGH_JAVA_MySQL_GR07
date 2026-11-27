/*
 * SIGH - Hospital General Monster Salud
 * Talleres Login y CRUD MVC en Java - Grupo GR07
 * Subsistema de Seguridad
 */
package ec.edu.monster.G07.logingr07.repository;

import ec.edu.monster.G07.logingr07.modelo.UsuPer;
import ec.edu.monster.G07.logingr07.modelo.UsuPerId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio JPA de UsuPer (tabla XEUXP_USUPE): perfiles asignados a cada usuario.
 */
public interface UsuPerRepository extends JpaRepository<UsuPer, UsuPerId> {

    List<UsuPer> findByIdCodigoUsuario(String codigoUsuario);

    /** Perfiles vigentes (sin fecha de retiro) de un usuario. */
    List<UsuPer> findByIdCodigoUsuarioAndFechaRetiroIsNull(String codigoUsuario);

    /** Asignaciones (vigentes o históricas) de un perfil: si existen, el perfil no se puede eliminar. */
    long countByIdCodigoPerfil(String codigoPerfil);

    long countByIdCodigoPerfilAndFechaRetiroIsNull(String codigoPerfil);
}
