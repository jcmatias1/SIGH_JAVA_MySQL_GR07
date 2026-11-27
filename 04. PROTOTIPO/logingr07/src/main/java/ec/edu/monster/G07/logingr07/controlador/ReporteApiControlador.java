/*
 * SIGH - Hospital General Monster Salud
 * Taller 3: Reportes con JasperReports en Java - Grupo GR07
 * Subsistema de Personal
 */
package ec.edu.monster.G07.logingr07.controlador;

import ec.edu.monster.G07.logingr07.dto.ReglaNegocioException;
import ec.edu.monster.G07.logingr07.servicio.ReporteFiltroServicio;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * API JSON del módulo de reportes (opción R001):
 *   GET /reportes/api/catalogos                        valores de los combos de filtros
 *   GET /reportes/api/empleados?estado=A&buscar=ana    vista previa paginada con los filtros
 */
@RestController
@RequestMapping("/reportes/api")
public class ReporteApiControlador {

    private final ReporteFiltroServicio filtros;

    public ReporteApiControlador(ReporteFiltroServicio filtros) {
        this.filtros = filtros;
    }

    @GetMapping("/catalogos")
    public Map<String, Object> catalogos() {
        return filtros.catalogos();
    }

    @GetMapping("/{reporte}")
    public Map<String, Object> vistaPrevia(@PathVariable String reporte, @RequestParam Map<String, String> parametros,
            @RequestParam(defaultValue = "1") int pagina, @RequestParam(defaultValue = "10") int tamano) {
        if (!filtros.existe(reporte)) {
            throw new ReglaNegocioException("El reporte " + reporte + " no existe.");
        }
        return filtros.vistaPrevia(reporte, parametros, pagina, tamano);
    }
}
