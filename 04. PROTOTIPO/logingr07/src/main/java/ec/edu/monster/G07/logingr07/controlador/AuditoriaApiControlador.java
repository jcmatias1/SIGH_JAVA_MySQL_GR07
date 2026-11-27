/*
 * SIGH - Hospital General Monster Salud
 * Talleres Login y CRUD MVC en Java - Grupo GR07
 * Subsistema de Seguridad
 */
package ec.edu.monster.G07.logingr07.controlador;

import ec.edu.monster.G07.logingr07.dto.AuditoriaDTO;
import ec.edu.monster.G07.logingr07.servicio.AuditoriaServicio;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * API JSON de auditoria.jsp (opción S003 «Consultar auditoría»). Solo lectura (RF-AUD-003).
 */
@RestController
@RequestMapping("/seguridad/auditoria/api")
public class AuditoriaApiControlador {

    private final AuditoriaServicio auditoriaServicio;

    public AuditoriaApiControlador(AuditoriaServicio auditoriaServicio) {
        this.auditoriaServicio = auditoriaServicio;
    }

    @GetMapping
    public List<AuditoriaDTO> listar() {
        return auditoriaServicio.listarRecientes();
    }
}
