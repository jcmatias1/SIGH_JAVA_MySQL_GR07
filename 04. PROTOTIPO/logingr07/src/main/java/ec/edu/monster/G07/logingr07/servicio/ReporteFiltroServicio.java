/*
 * SIGH - Hospital General Monster Salud
 * Taller 3: Reportes con JasperReports en Java - Grupo GR07
 * Subsistema de Personal
 */
package ec.edu.monster.G07.logingr07.servicio;

import ec.edu.monster.G07.logingr07.dto.ReglaNegocioException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * Filtros, búsqueda y vista previa del módulo de reportes.
 * La vista previa ejecuta la MISMA consulta SQL del .jrxml (se lee del reporte compilado), así la tabla
 * que ve el usuario y el PDF/Excel que descarga siempre tienen las mismas filas.
 */
@Service
public class ReporteFiltroServicio {

    /** Filtro de la pantalla: nombre en la URL, parámetro del .jrxml y etiqueta para el encabezado del PDF. */
    public record Filtro(String url, String parametro, String etiqueta) {
    }

    /** Columna de la vista previa: campo de la consulta y título. */
    public record Columna(String campo, String titulo) {
    }

    private static final Filtro DEPARTAMENTO = new Filtro("departamento", "DEPARTAMENTO", "Departamento");
    private static final Filtro CARGO = new Filtro("cargo", "CARGO", "Cargo");
    private static final Filtro ESTADO = new Filtro("estado", "ESTADO", "Estado");
    private static final Filtro GENERO = new Filtro("genero", "GENERO", "Género");
    private static final Filtro ESTADO_CIVIL = new Filtro("estadoCivil", "ESTADO_CIVIL", "Estado civil");
    private static final Filtro TIPO_SANGRE = new Filtro("tipoSangre", "TIPO_SANGRE", "Tipo de sangre");
    private static final Filtro FECHA_DESDE = new Filtro("fechaDesde", "FECHA_DESDE", "Nacidos desde");
    private static final Filtro FECHA_HASTA = new Filtro("fechaHasta", "FECHA_HASTA", "Nacidos hasta");
    private static final Filtro BUSCAR = new Filtro("buscar", "BUSCAR", "Búsqueda");

    /** Filtros que acepta cada reporte (la ficha usa los del listado de empleados para elegir al empleado). */
    private static final Map<String, List<Filtro>> FILTROS = Map.of(
            "departamentos", List.of(BUSCAR),
            "cargos", List.of(DEPARTAMENTO, BUSCAR),
            "empleados", List.of(DEPARTAMENTO, CARGO, ESTADO, GENERO, ESTADO_CIVIL, TIPO_SANGRE, FECHA_DESDE, FECHA_HASTA, BUSCAR));

    private static final Map<String, List<Columna>> COLUMNAS = Map.of(
            "departamentos", List.of(new Columna("PEDEP_CODIGO", "Código"), new Columna("PEDEP_DESCRI", "Departamento"),
                    new Columna("CARGOS", "N.º de cargos"), new Columna("EMPLEADOS", "Empleados activos")),
            "cargos", List.of(new Columna("PEDEP_DESCRI", "Departamento"), new Columna("PECAR_CODIGO", "Código"),
                    new Columna("PECAR_DESCRI", "Cargo"), new Columna("EMPLEADOS", "Empleados activos")),
            "empleados", List.of(new Columna("PEEMP_CODIGO", "Código"), new Columna("PEEMP_CEDULA", "Cédula"),
                    new Columna("PEEMP_APELLI", "Apellidos"), new Columna("PEEMP_NOMBRE", "Nombres"),
                    new Columna("PEDEP_DESCRI", "Departamento"), new Columna("PECAR_DESCRI", "Cargo"),
                    new Columna("PEEMP_TELEFO", "Teléfono"), new Columna("PEEMP_EMAIL", "Email"),
                    new Columna("PEEMP_ESTADO", "Estado")));

    private static final Pattern PARAMETRO = Pattern.compile("\\$P\\{(\\w+)\\}");

    private final JdbcTemplate jdbc;
    private final ReporteServicio reportes;

    public ReporteFiltroServicio(JdbcTemplate jdbc, ReporteServicio reportes) {
        this.jdbc = jdbc;
        this.reportes = reportes;
    }

    public boolean existe(String reporte) {
        return FILTROS.containsKey(reporte);
    }

    /** Valores de los combos de cada filtro. */
    public Map<String, Object> catalogos() {
        Map<String, Object> c = new LinkedHashMap<>();
        c.put("departamento", jdbc.queryForList(
                "SELECT PEDEP_CODIGO AS codigo, PEDEP_DESCRI AS descripcion FROM PEDEP_DEPAR ORDER BY PEDEP_DESCRI"));
        c.put("cargo", jdbc.queryForList("""
                SELECT CONCAT(c.PEDEP_CODIGO, '|', c.PECAR_CODIGO) AS codigo, c.PECAR_DESCRI AS descripcion,
                       c.PEDEP_CODIGO AS departamento, d.PEDEP_DESCRI AS nombreDepartamento
                FROM PECAR_CARGO c JOIN PEDEP_DEPAR d ON d.PEDEP_CODIGO = c.PEDEP_CODIGO
                ORDER BY d.PEDEP_DESCRI, c.PECAR_DESCRI"""));
        c.put("estado", List.of(Map.of("codigo", "A", "descripcion", "Activo"), Map.of("codigo", "I", "descripcion", "Inactivo")));
        c.put("genero", jdbc.queryForList(
                "SELECT PEGEN_CODIGO AS codigo, PEGEN_DESCRI AS descripcion FROM PEGEN_GENER ORDER BY PEGEN_DESCRI"));
        c.put("estadoCivil", jdbc.queryForList(
                "SELECT PEESC_CODIGO AS codigo, PEESC_DESCRI AS descripcion FROM PEESC_ESTCI ORDER BY PEESC_DESCRI"));
        c.put("tipoSangre", jdbc.queryForList(
                "SELECT PETSA_CODIGO AS codigo, PETSA_DESCRI AS descripcion FROM PETSA_TIPSA ORDER BY PETSA_DESCRI"));
        return c;
    }

    /**
     * Convierte los filtros recibidos en la URL (departamento=MED, buscar=ana...) en los parámetros del .jrxml.
     * Los que no llegan van vacíos, que en la consulta significa "todos".
     */
    public Map<String, Object> parametros(String reporte, Map<String, String> url) {
        Map<String, Object> p = new LinkedHashMap<>();
        for (Filtro f : FILTROS.get(reporte)) {
            p.put(f.parametro(), validar(f, url.getOrDefault(f.url(), "")));
        }
        p.put("FILTROS", describir(reporte, p));
        return p;
    }

    /** Página de la vista previa: columnas, filas y total, con la consulta del .jrxml. */
    public Map<String, Object> vistaPrevia(String reporte, Map<String, String> url, int pagina, int tamano) {
        Map<String, Object> p = parametros(reporte, url);
        List<Object> valores = new ArrayList<>();
        Matcher m = PARAMETRO.matcher(reportes.consulta(reporte));
        StringBuilder sql = new StringBuilder();
        while (m.find()) {
            valores.add(p.getOrDefault(m.group(1), ""));
            m.appendReplacement(sql, "?");
        }
        m.appendTail(sql);
        long total = jdbc.queryForObject("SELECT COUNT(*) FROM (" + sql + ") t", Long.class, valores.toArray());
        int tam = Math.max(5, Math.min(tamano, 100));
        int paginas = (int) Math.max(1, (total + tam - 1) / tam);
        int pag = Math.max(1, Math.min(pagina, paginas));
        List<Object> conLimite = new ArrayList<>(valores);
        conLimite.add(tam);
        conLimite.add((pag - 1) * tam);
        List<Map<String, Object>> filas = jdbc.queryForList("SELECT * FROM (" + sql + ") t LIMIT ? OFFSET ?", conLimite.toArray());
        Map<String, Object> r = new LinkedHashMap<>();
        r.put("columnas", COLUMNAS.get(reporte));
        r.put("filas", filas);
        r.put("total", total);
        r.put("pagina", pag);
        r.put("paginas", paginas);
        r.put("tamano", tam);
        r.put("filtros", p.get("FILTROS"));
        return r;
    }

    private static String validar(Filtro f, String valor) {
        String v = valor == null ? "" : valor.strip();
        if (v.length() > 60) {
            throw new ReglaNegocioException(f.etiqueta() + ": el valor es demasiado largo.");
        }
        if (v.isEmpty()) {
            return v;
        }
        if (f == ESTADO) {
            v = v.toUpperCase(Locale.ROOT);
            if (!v.equals("A") && !v.equals("I")) {
                throw new ReglaNegocioException("El estado debe ser A (activo) o I (inactivo).");
            }
        } else if (f == FECHA_DESDE || f == FECHA_HASTA) {
            try {
                LocalDate.parse(v);
            } catch (DateTimeParseException e) {
                throw new ReglaNegocioException(f.etiqueta() + ": la fecha debe tener el formato AAAA-MM-DD.");
            }
        } else if (f != BUSCAR) {
            v = v.toUpperCase(Locale.ROOT);
        }
        return v;
    }

    /** Texto de los filtros aplicados que se imprime en el encabezado del PDF, p. ej. "Departamento: Medicina · Estado: Activo". */
    private String describir(String reporte, Map<String, Object> p) {
        List<String> partes = new ArrayList<>();
        for (Filtro f : FILTROS.get(reporte)) {
            String v = (String) p.get(f.parametro());
            if (v.isEmpty()) {
                continue;
            }
            String texto = v;
            if (f == DEPARTAMENTO) {
                texto = nombre("SELECT PEDEP_DESCRI FROM PEDEP_DEPAR WHERE PEDEP_CODIGO = ?", v);
            } else if (f == CARGO) {
                texto = nombre("SELECT PECAR_DESCRI FROM PECAR_CARGO WHERE CONCAT(PEDEP_CODIGO, '|', PECAR_CODIGO) = ?", v);
            } else if (f == ESTADO) {
                texto = v.equals("A") ? "Activo" : "Inactivo";
            } else if (f == GENERO) {
                texto = nombre("SELECT PEGEN_DESCRI FROM PEGEN_GENER WHERE PEGEN_CODIGO = ?", v);
            } else if (f == ESTADO_CIVIL) {
                texto = nombre("SELECT PEESC_DESCRI FROM PEESC_ESTCI WHERE PEESC_CODIGO = ?", v);
            } else if (f == TIPO_SANGRE) {
                texto = nombre("SELECT PETSA_DESCRI FROM PETSA_TIPSA WHERE PETSA_CODIGO = ?", v);
            } else if (f == FECHA_DESDE || f == FECHA_HASTA) {
                LocalDate d = LocalDate.parse(v);
                texto = String.format("%02d/%02d/%d", d.getDayOfMonth(), d.getMonthValue(), d.getYear());
            } else if (f == BUSCAR) {
                texto = "\"" + v + "\"";
            }
            partes.add(f.etiqueta() + ": " + texto);
        }
        return String.join(" · ", partes);
    }

    private String nombre(String sql, String codigo) {
        List<String> r = jdbc.queryForList(sql, String.class, codigo);
        if (r.isEmpty()) {
            throw new ReglaNegocioException("El valor " + codigo + " no existe.");
        }
        return r.get(0);
    }
}
