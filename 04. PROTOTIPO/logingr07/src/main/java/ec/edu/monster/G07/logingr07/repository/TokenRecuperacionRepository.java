/*
 * SIGH - Hospital General Monster Salud
 * Talleres Login y CRUD MVC en Java - Grupo GR07
 * Subsistema de Seguridad
 */
package ec.edu.monster.G07.logingr07.repository;

import ec.edu.monster.G07.logingr07.modelo.TokenRecuperacion;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio JPA de TokenRecuperacion (tabla XEREC_RECUP).
 */
public interface TokenRecuperacionRepository extends JpaRepository<TokenRecuperacion, Long> {

    Optional<TokenRecuperacion> findByToken(String token);

    long countByUsuarioCodigoUsuarioAndFechaCreacionAfter(String codigoUsuario, LocalDateTime fecha);
}
