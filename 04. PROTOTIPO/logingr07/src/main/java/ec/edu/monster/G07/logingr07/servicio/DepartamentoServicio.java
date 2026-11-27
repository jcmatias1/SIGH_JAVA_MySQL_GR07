/*
 * SIGH - Hospital General Monster Salud
 * Talleres Login y CRUD MVC en Java - Grupo GR07
 * Subsistema de Personal
 */
package ec.edu.monster.G07.logingr07.servicio;

import ec.edu.monster.G07.logingr07.dto.DepartamentoFilaDTO;
import ec.edu.monster.G07.logingr07.dto.DepartamentoFormDTO;
import ec.edu.monster.G07.logingr07.dto.ReglaNegocioException;
import ec.edu.monster.G07.logingr07.modelo.Departamento;
import ec.edu.monster.G07.logingr07.repository.CargoRepository;
import ec.edu.monster.G07.logingr07.repository.DepartamentoRepository;
import ec.edu.monster.G07.logingr07.repository.EmpleadoRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * CRUD de departamentos (opción P001 «Administrar departamentos»). Los cargos (P002) se crean
 * dentro de un departamento, por eso no se elimina un departamento que tenga cargos o empleados.
 */
@Service
public class DepartamentoServicio {

    public static final String MODULO = "PERSONAL";

    private static final String PATRON_DESCRIPCION = "^[A-Za-zÁÉÍÓÚÜÑáéíóúüñ /.\\-]+$";

    private final DepartamentoRepository departamentoRepository;
    private final CargoRepository cargoRepository;
    private final EmpleadoRepository empleadoRepository;
    private final AuditoriaServicio auditoria;

    public DepartamentoServicio(DepartamentoRepository departamentoRepository, CargoRepository cargoRepository,
            EmpleadoRepository empleadoRepository, AuditoriaServicio auditoria) {
        this.departamentoRepository = departamentoRepository;
        this.cargoRepository = cargoRepository;
        this.empleadoRepository = empleadoRepository;
        this.auditoria = auditoria;
    }

    @Transactional(readOnly = true)
    public List<DepartamentoFilaDTO> listar() {
        return departamentoRepository.findAllByOrderByDescripcionAsc().stream()
                .map(d -> new DepartamentoFilaDTO(d.getCodigoDepartamento(), d.getDescripcion(),
                        cargoRepository.countByIdCodigoDepartamento(d.getCodigoDepartamento()),
                        empleadoRepository.countByCodigoDepartamento(d.getCodigoDepartamento())))
                .toList();
    }

    /** CREATE: registra un departamento nuevo. */
    @Transactional
    public DepartamentoFilaDTO crear(DepartamentoFormDTO datos, String actor, String ip) {
        String codigo = texto(datos.codigoDepartamento()).toUpperCase();
        if (!codigo.matches("^[A-Z]{3}$")) {
            throw new ReglaNegocioException("El código del departamento debe tener exactamente 3 letras. Ejemplo: LAB.");
        }
        if (departamentoRepository.existsById(codigo)) {
            throw new ReglaNegocioException("Ya existe un departamento con el código " + codigo + ".");
        }
        String descripcion = validarDescripcion(datos.descripcion());
        if (departamentoRepository.existsByDescripcionIgnoreCase(descripcion)) {
            throw new ReglaNegocioException("Ya existe un departamento llamado «" + descripcion + "».");
        }
        Departamento departamento = new Departamento();
        departamento.setCodigoDepartamento(codigo);
        departamento.setDescripcion(descripcion);
        departamentoRepository.save(departamento);
        auditoria.registrar(MODULO, actor, "CREAR DEPARTAMENTO", "EXITOSO",
                "Se creó el departamento " + codigo + " - " + descripcion, ip);
        return new DepartamentoFilaDTO(codigo, descripcion, 0, 0);
    }

    /** UPDATE: solo cambia la descripción; el código es la clave y no cambia. */
    @Transactional
    public void actualizar(String codigoDepartamento, DepartamentoFormDTO datos, String actor, String ip) {
        Departamento departamento = buscar(codigoDepartamento);
        String descripcion = validarDescripcion(datos.descripcion());
        if (departamentoRepository.existsByDescripcionIgnoreCaseAndCodigoDepartamentoNot(descripcion,
                departamento.getCodigoDepartamento())) {
            throw new ReglaNegocioException("Ya existe otro departamento llamado «" + descripcion + "».");
        }
        String anterior = departamento.getDescripcion();
        departamento.setDescripcion(descripcion);
        departamentoRepository.save(departamento);
        auditoria.registrar(MODULO, actor, "MODIFICAR DEPARTAMENTO", "EXITOSO",
                "Departamento " + departamento.getCodigoDepartamento() + ": «" + anterior + "» -> «" + descripcion + "»",
                ip);
    }

    /** DELETE: solo si el departamento no tiene cargos ni empleados. */
    @Transactional
    public void eliminar(String codigoDepartamento, String actor, String ip) {
        Departamento departamento = buscar(codigoDepartamento);
        String codigo = departamento.getCodigoDepartamento();
        long cargos = cargoRepository.countByIdCodigoDepartamento(codigo);
        if (cargos > 0) {
            throw new ReglaNegocioException("No se puede eliminar: el departamento tiene " + cargos
                    + " cargo(s). Elimine primero sus cargos.");
        }
        long empleados = empleadoRepository.countByCodigoDepartamento(codigo);
        if (empleados > 0) {
            throw new ReglaNegocioException("No se puede eliminar: el departamento tiene " + empleados
                    + " empleado(s) asignado(s).");
        }
        departamentoRepository.delete(departamento);
        auditoria.registrar(MODULO, actor, "ELIMINAR DEPARTAMENTO", "EXITOSO",
                "Se eliminó el departamento " + codigo + " - " + departamento.getDescripcion(), ip);
    }

    private Departamento buscar(String codigoDepartamento) {
        return departamentoRepository.findById(texto(codigoDepartamento).toUpperCase())
                .orElseThrow(() -> new ReglaNegocioException("El departamento no existe."));
    }

    private static String validarDescripcion(String descripcion) {
        String valor = texto(descripcion).replaceAll("\\s+", " ");
        if (valor.length() < 3) {
            throw new ReglaNegocioException("La descripción del departamento es obligatoria (mínimo 3 caracteres).");
        }
        if (valor.length() > 60) {
            throw new ReglaNegocioException("La descripción del departamento no puede superar 60 caracteres.");
        }
        if (!valor.matches(PATRON_DESCRIPCION)) {
            throw new ReglaNegocioException(
                    "La descripción del departamento solo admite letras, espacios y los signos / . -");
        }
        return valor;
    }

    private static String texto(String valor) {
        return valor == null ? "" : valor.trim();
    }
}
