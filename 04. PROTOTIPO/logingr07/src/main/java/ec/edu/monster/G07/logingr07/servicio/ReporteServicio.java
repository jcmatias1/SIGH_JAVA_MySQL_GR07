/*
 * SIGH - Hospital General Monster Salud
 * Taller 3: Reportes con JasperReports en Java - Grupo GR07
 * Subsistema de Personal
 */
package ec.edu.monster.G07.logingr07.servicio;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

/**
 * Genera los reportes PDF diseñados en Jaspersoft Studio (src/main/resources/reportes/*.jrxml).
 * Pasos: 1) compilar el .jrxml (solo la primera vez, luego se usa la caché),
 *        2) llenarlo con los parámetros y una conexión JDBC del pool de Spring,
 *        3) exportar el resultado a PDF o a Excel (.xlsx).
 */
@Service
public class ReporteServicio {

    private final DataSource dataSource;
    private final Map<String, JasperReport> compilados = new ConcurrentHashMap<>();

    static {
        // El servidor no tiene pantalla: Jasper mide los textos sin abrir ventanas.
        System.setProperty("java.awt.headless", "true");
    }

    public ReporteServicio(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /** Llena el reporte con los parámetros recibidos y lo devuelve como PDF. */
    public byte[] generarPdf(String nombre, Map<String, Object> parametros) {
        return generar(nombre, parametros, false);
    }

    /** Llena el reporte y lo exporta a Excel (.xlsx), una hoja con todas las filas. */
    public byte[] generarExcel(String nombre, Map<String, Object> parametros) {
        return generar(nombre, parametros, true);
    }

    private byte[] generar(String nombre, Map<String, Object> parametros, boolean excel) {
        Map<String, Object> todos = new HashMap<>(parametros);
        todos.put(JRParameter.REPORT_LOCALE, Locale.of("es", "EC"));
        try (Connection conexion = dataSource.getConnection()) {
            JasperPrint impresion = JasperFillManager.fillReport(compilar(nombre), todos, conexion);
            return excel ? exportarExcel(impresion) : JasperExportManager.exportReportToPdf(impresion);
        } catch (JRException | SQLException | IOException e) {
            throw new IllegalStateException("No se pudo generar el reporte " + nombre + ": " + e.getMessage(), e);
        }
    }

    /** Texto SQL de la consulta principal del .jrxml, con sus $P{...}; lo usa la vista previa. */
    public String consulta(String nombre) {
        try {
            return compilar(nombre).getQuery().getText();
        } catch (JRException | IOException e) {
            throw new IllegalStateException("No se pudo leer el reporte " + nombre + ": " + e.getMessage(), e);
        }
    }

    /** Mismo JasperPrint del PDF, exportado con JRXlsxExporter (Apache POI). */
    private static byte[] exportarExcel(JasperPrint impresion) throws JRException {
        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        JRXlsxExporter exportador = new JRXlsxExporter();
        exportador.setExporterInput(new SimpleExporterInput(impresion));
        exportador.setExporterOutput(new SimpleOutputStreamExporterOutput(salida));
        SimpleXlsxReportConfiguration configuracion = new SimpleXlsxReportConfiguration();
        configuracion.setOnePagePerSheet(false);           // todas las páginas en una sola hoja
        configuracion.setDetectCellType(true);             // los números quedan como números
        configuracion.setRemoveEmptySpaceBetweenRows(true);
        configuracion.setRemoveEmptySpaceBetweenColumns(true);
        configuracion.setWhitePageBackground(false);
        configuracion.setSheetNames(new String[] {impresion.getName()});
        exportador.setConfiguration(configuracion);
        exportador.exportReport();
        return salida.toByteArray();
    }

    /** Compila el .jrxml la primera vez y guarda el resultado en memoria. */
    private JasperReport compilar(String nombre) throws JRException, IOException {
        JasperReport reporte = compilados.get(nombre);
        if (reporte == null) {
            ClassPathResource archivo = new ClassPathResource("reportes/" + nombre + ".jrxml");
            if (!archivo.exists()) {
                throw new IOException("No existe reportes/" + nombre + ".jrxml");
            }
            try (InputStream in = archivo.getInputStream()) {
                reporte = JasperCompileManager.compileReport(in);
            }
            compilados.put(nombre, reporte);
        }
        return reporte;
    }
}
