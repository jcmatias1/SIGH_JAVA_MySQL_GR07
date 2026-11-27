/*
 * SIGH - Hospital General Monster Salud
 * Talleres Login y CRUD MVC en Java - Grupo GR07
 * Subsistema de Seguridad
 */
package ec.edu.monster.G07.logingr07.repository;

import ec.edu.monster.G07.logingr07.modelo.Opcion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repositorio JPA de Opcion (tabla XEOPC_OPCIO).
 */
public interface OpcionRepository extends JpaRepository<Opcion, String> {

    List<Opcion> findBySistemaCodigoSistema(String codigoSistema);

    /**
     * Menú del usuario: opciones vigentes de todos sus perfiles vigentes.
     * Recorre USUARIO -> XEUXP_USUPE -> PERFIL -> XEOXP_OPCPE -> OPCION.
     */
    @Query("""
            select distinct o
            from UsuPer up
              join up.perfil p
              join p.opciones op
              join op.opcion o
              join fetch o.sistema s
            where up.usuario.codigoUsuario = :codigoUsuario
              and up.fechaRetiro is null
              and op.fechaRetiro is null
            order by s.codigoSistema, o.codigoOpcion
            """)
    List<Opcion> findOpcionesPermitidas(@Param("codigoUsuario") String codigoUsuario);
}
