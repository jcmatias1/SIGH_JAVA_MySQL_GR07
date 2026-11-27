/*
 * SIGH - Hospital General Monster Salud
 * Taller 3: Reportes con JasperReports en Java - Grupo GR07
 * Subsistema de Personal
 */
package ec.edu.monster.G07.logingr07.controlador;

import ec.edu.monster.G07.logingr07.config.SeguridadInterceptor;
import ec.edu.monster.G07.logingr07.dto.UsuarioSesion;
import ec.edu.monster.G07.logingr07.repository.DepartamentoRepository;
import ec.edu.monster.G07.logingr07.repository.EmpleadoRepository;
import ec.edu.monster.G07.logingr07.servicio.AuditoriaServicio;
import ec.edu.monster.G07.logingr07.servicio.MenuServicio;
import ec.edu.monster.G07.logingr07.servicio.ReporteFiltroServicio;
import ec.edu.monster.G07.logingr07.servicio.ReporteServicio;
import ec.edu.monster.G07.logingr07.dto.ReglaNegocioException;
import ec.edu.monster.G07.logingr07.utils.SeguridadUtil;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.util.HtmlUtils;

/**
 * Reportes PDF del sistema PERSONAL. Las rutas cuelgan de la opción de cada pantalla, así que el
 * SeguridadInterceptor exige el mismo permiso: P001 departamentos, P002 cargos, P003 empleados.
 *   /personal/departamentos/reporte
 *   /personal/cargos/reporte?departamento=MED&buscar=jefe
 *   /personal/empleados/reporte?estado=A&departamento=MED&genero=F&buscar=ana
 *   /personal/empleados/ficha/EMP0000004
 * Con ?formato=excel cualquiera de ellos se descarga como .xlsx en lugar de PDF.
 * Las mismas rutas existen bajo /reportes/... para el módulo REPORTES (opción R001).
 */
@Controller
public class ReporteControlador {

    private final ReporteServicio reportes;
    private final DepartamentoRepository departamentoRepository;
    private final EmpleadoRepository empleadoRepository;
    private final AuditoriaServicio auditoria;
    private final MenuServicio menuServicio;
    private final ReporteFiltroServicio filtros;

    public ReporteControlador(ReporteServicio reportes, DepartamentoRepository departamentoRepository,
            EmpleadoRepository empleadoRepository, AuditoriaServicio auditoria, MenuServicio menuServicio,
            ReporteFiltroServicio filtros) {
        this.menuServicio = menuServicio;
        this.filtros = filtros;
        this.reportes = reportes;
        this.departamentoRepository = departamentoRepository;
        this.empleadoRepository = empleadoRepository;
        this.auditoria = auditoria;
    }

    @GetMapping({"/personal/departamentos/reporte", "/reportes/departamentos"})
    public ResponseEntity<byte[]> departamentos(@SessionAttribute(SeguridadInterceptor.USUARIO_SESION) UsuarioSesion usuario,
            @RequestParam Map<String, String> url, HttpServletRequest request) {
        return conFiltros("departamentos", url, usuario, request);
    }

    @GetMapping({"/personal/cargos/reporte", "/reportes/cargos"})
    public ResponseEntity<byte[]> cargos(@SessionAttribute(SeguridadInterceptor.USUARIO_SESION) UsuarioSesion usuario,
            @RequestParam Map<String, String> url, HttpServletRequest request) {
        return conFiltros("cargos", url, usuario, request);
    }

    @GetMapping({"/personal/empleados/reporte", "/reportes/empleados"})
    public ResponseEntity<byte[]> empleados(@SessionAttribute(SeguridadInterceptor.USUARIO_SESION) UsuarioSesion usuario,
            @RequestParam Map<String, String> url, HttpServletRequest request) {
        return conFiltros("empleados", url, usuario, request);
    }

    /**
     * Reportes de listado: los filtros de la URL (departamento, cargo, estado, genero, estadoCivil, tipoSangre,
     * fechaDesde, fechaHasta, buscar) pasan como parámetros al .jrxml, igual que en la vista previa.
     */
    private ResponseEntity<byte[]> conFiltros(String reporte, Map<String, String> url, UsuarioSesion usuario,
            HttpServletRequest request) {
        Map<String, Object> p;
        try {
            p = filtros.parametros(reporte, url);
        } catch (ReglaNegocioException e) {
            return error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        p.putAll(parametros(usuario));
        return archivo(reporte, reporte, url.getOrDefault("formato", "pdf"), p, usuario, request);
    }

    @GetMapping({"/personal/empleados/ficha/{codigo}", "/reportes/ficha/{codigo}"})
    public ResponseEntity<byte[]> ficha(@SessionAttribute(SeguridadInterceptor.USUARIO_SESION) UsuarioSesion usuario,
            @PathVariable String codigo, @RequestParam(defaultValue = "pdf") String formato, HttpServletRequest request) {
        String limpio = codigo.strip().toUpperCase(Locale.ROOT);
        if (!empleadoRepository.existsById(limpio)) {
            return error(HttpStatus.NOT_FOUND, "El empleado " + limpio + " no existe.");
        }
        Map<String, Object> p = parametros(usuario);
        p.put("EMPLEADO", limpio);
        return archivo("empleado_ficha", "ficha-" + limpio, formato, p, usuario, request);
    }

    /** Pantalla del módulo REPORTES: pestañas por reporte con filtros, búsqueda y vista previa. */
    @GetMapping("/reportes")
    public String modulo(@SessionAttribute(SeguridadInterceptor.USUARIO_SESION) UsuarioSesion usuario, Model model) {
        model.addAttribute("menu", menuServicio.menu(usuario.getCodigoUsuario()));
        return "reportes";
    }

    private static Map<String, Object> parametros(UsuarioSesion usuario) {
        Map<String, Object> p = new HashMap<>();
        p.put("USUARIO", usuario.getNombreCompleto());
        return p;
    }

    /**
     * Genera el reporte en PDF (se muestra en el navegador) o en Excel (se descarga),
     * y lo registra en la auditoría.
     */
    private ResponseEntity<byte[]> archivo(String reporte, String nombre, String formato, Map<String, Object> parametros,
            UsuarioSesion usuario, HttpServletRequest request) {
        boolean excel = "excel".equalsIgnoreCase(formato) || "xlsx".equalsIgnoreCase(formato);
        if (!excel && !"pdf".equalsIgnoreCase(formato)) {
            return error(HttpStatus.BAD_REQUEST, "El formato debe ser pdf o excel.");
        }
        byte[] datos = excel ? reportes.generarExcel(reporte, parametros) : reportes.generarPdf(reporte, parametros);
        String archivo = nombre + (excel ? ".xlsx" : ".pdf");
        auditoria.registrar("PERSONAL", usuario.getCodigoUsuario(), "GENERAR REPORTE", "EXITOSO",
                detalle(archivo, parametros), SeguridadUtil.ipCliente(request));
        return ResponseEntity.ok()
                .contentType(excel
                        ? MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                        : MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, (excel ? "attachment" : "inline") + "; filename=\"" + archivo + "\"")
                .header(HttpHeaders.CACHE_CONTROL, "no-store")
                .body(datos);
    }

    private static String detalle(String archivo, Map<String, Object> parametros) {
        Object f = parametros.get("FILTROS");
        String texto = "Reporte " + archivo + (f == null || f.toString().isEmpty() ? "" : " · " + f);
        return texto.length() > 240 ? texto.substring(0, 240) : texto;
    }

    private static ResponseEntity<byte[]> error(HttpStatus estado, String mensaje) {
        String html = "<!DOCTYPE html><html lang=\"es\"><head><meta charset=\"UTF-8\"><title>Reporte</title></head>"
                + "<body style=\"font-family:Segoe UI,Arial,sans-serif;padding:40px;color:#082a52\">"
                + "<h2>No se pudo generar el reporte</h2><p>" + HtmlUtils.htmlEscape(mensaje) + "</p></body></html>";
        return ResponseEntity.status(estado).contentType(new MediaType("text", "html", java.nio.charset.StandardCharsets.UTF_8))
                .body(html.getBytes(java.nio.charset.StandardCharsets.UTF_8));
    }
}
