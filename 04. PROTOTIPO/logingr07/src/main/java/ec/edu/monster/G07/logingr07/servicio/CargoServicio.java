/*
 * SIGH - Hospital General Monster Salud
 * Talleres Login y CRUD MVC en Java - Grupo GR07
 * Subsistema de Personal
 */
package ec.edu.monster.G07.logingr07.servicio;

import ec.edu.monster.G07.logingr07.dto.CargoDTO;
import ec.edu.monster.G07.logingr07.dto.CargoFormDTO;
import ec.edu.monster.G07.logingr07.dto.DepartamentoDTO;
import ec.edu.monster.G07.logingr07.dto.ReglaNegocioException;
import ec.edu.monster.G07.logingr07.modelo.Cargo;
import ec.edu.monster.G07.logingr07.modelo.CargoId;
import ec.edu.monster.G07.logingr07.modelo.Departamento;
import ec.edu.monster.G07.logingr07.repository.CargoRepository;
import ec.edu.monster.G07.logingr07.repository.DepartamentoRepository;
import ec.edu.monster.G07.logingr07.repository.EmpleadoRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * CRUD de cargos del personal (opción P002 «Administrar cargos»). Cada cargo pertenece a un
 * departamento; su clave es departamento + código. Todas las operaciones quedan auditadas en
 * el módulo PERSONAL.
 */
@Service
public class CargoServicio {

    public static final String MODULO = "PERSONAL";

    private static final String PATRON_DESCRIPCION = "^[A-Za-zÁÉÍÓÚÜÑáéíóúüñ /.\\-]+$";

    private final CargoRepository cargoRepository;
    private final DepartamentoRepository departamentoRepository;
    private final EmpleadoRepository empleadoRepository;
    private final AuditoriaServicio auditoria;

    public CargoServicio(CargoRepository cargoRepository, DepartamentoRepository departamentoRepository,
            EmpleadoRepository empleadoRepository, AuditoriaServicio auditoria) {
        this.cargoRepository = cargoRepository;
        this.departamentoRepository = departamentoRepository;
        this.empleadoRepository = empleadoRepository;
        this.auditoria = auditoria;
    }

    @Transactional(readOnly = true)
    public List<CargoDTO> listar() {
        return cargoRepository.listarConDepartamento().stream()
                .map(this::aDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<DepartamentoDTO> departamentos() {
        return departamentoRepository.findAllByOrderByDescripcionAsc().stream()
                .map(d -> new DepartamentoDTO(d.getCodigoDepartamento(), d.getDescripcion()))
                .toList();
    }

    /** CREATE: registra un cargo nuevo dentro de un departamento. */
    @Transactional
    public CargoDTO crear(CargoFormDTO datos, String actor, String ip) {
        String codigo = texto(datos.codigoCargo()).toUpperCase();
        if (!codigo.matches("^[A-Z]{3}$")) {
            throw new ReglaNegocioException("El código del cargo debe tener exactamente 3 letras. Ejemplo: MED.");
        }
        String codigoDepartamento = texto(datos.codigoDepartamento()).toUpperCase();
        if (codigoDepartamento.isEmpty()) {
            throw new ReglaNegocioException("Seleccione el departamento del cargo.");
        }
        Departamento departamento = departamentoRepository.findById(codigoDepartamento)
                .orElseThrow(() -> new ReglaNegocioException("El departamento seleccionado no existe."));
        CargoId id = new CargoId(codigoDepartamento, codigo);
        if (cargoRepository.existsById(id)) {
            throw new ReglaNegocioException("Ya existe el cargo " + codigo + " en el departamento "
                    + departamento.getDescripcion() + ".");
        }
        String descripcion = validarDescripcion(datos.descripcion());
        if (cargoRepository.existsByIdCodigoDepartamentoAndDescripcionIgnoreCase(codigoDepartamento, descripcion)) {
            throw new ReglaNegocioException("Ya existe un cargo llamado «" + descripcion + "» en el departamento "
                    + departamento.getDescripcion() + ".");
        }
        Cargo cargo = new Cargo();
        cargo.setId(id);
        cargo.setDepartamento(departamento);
        cargo.setDescripcion(descripcion);
        cargoRepository.save(cargo);
        auditoria.registrar(MODULO, actor, "CREAR CARGO", "EXITOSO",
                "Se creó el cargo " + codigoDepartamento + "/" + codigo + " - " + descripcion
                        + " (departamento " + departamento.getDescripcion() + ")", ip);
        return new CargoDTO(codigo, descripcion, codigoDepartamento, departamento.getDescripcion(), 0);
    }

    /** UPDATE: solo cambia la descripción; departamento y código forman la clave y no cambian. */
    @Transactional
    public void actualizar(String codigoDepartamento, String codigoCargo, CargoFormDTO datos, String actor, String ip) {
        Cargo cargo = buscar(codigoDepartamento, codigoCargo);
        String descripcion = validarDescripcion(datos.descripcion());
        CargoId id = cargo.getId();
        if (cargoRepository.existsByIdCodigoDepartamentoAndDescripcionIgnoreCaseAndIdCodigoCargoNot(
                id.getCodigoDepartamento(), descripcion, id.getCodigoCargo())) {
            throw new ReglaNegocioException("Ya existe otro cargo llamado «" + descripcion + "» en el departamento "
                    + cargo.getDepartamento().getDescripcion() + ".");
        }
        String anterior = cargo.getDescripcion();
        cargo.setDescripcion(descripcion);
        cargoRepository.save(cargo);
        auditoria.registrar(MODULO, actor, "MODIFICAR CARGO", "EXITOSO",
                "Cargo " + id.getCodigoDepartamento() + "/" + id.getCodigoCargo() + ": «" + anterior + "» -> «"
                        + descripcion + "»", ip);
    }

    /** DELETE: elimina el cargo solo si ningún empleado lo tiene asignado. */
    @Transactional
    public void eliminar(String codigoDepartamento, String codigoCargo, String actor, String ip) {
        Cargo cargo = buscar(codigoDepartamento, codigoCargo);
        CargoId id = cargo.getId();
        long empleados = empleadoRepository.countByCodigoDepartamentoAndCodigoCargo(id.getCodigoDepartamento(),
                id.getCodigoCargo());
        if (empleados > 0) {
            throw new ReglaNegocioException("No se puede eliminar: el cargo está asignado a " + empleados
                    + " empleado(s).");
        }
        String departamento = cargo.getDepartamento().getDescripcion();
        cargoRepository.delete(cargo);
        auditoria.registrar(MODULO, actor, "ELIMINAR CARGO", "EXITOSO",
                "Se eliminó el cargo " + id.getCodigoDepartamento() + "/" + id.getCodigoCargo() + " - "
                        + cargo.getDescripcion() + " (departamento " + departamento + ")", ip);
    }

    private CargoDTO aDto(Cargo c) {
        String dep = c.getId().getCodigoDepartamento();
        String car = c.getId().getCodigoCargo();
        return new CargoDTO(car, c.getDescripcion(), dep, c.getDepartamento().getDescripcion(),
                empleadoRepository.countByCodigoDepartamentoAndCodigoCargo(dep, car));
    }

    private Cargo buscar(String codigoDepartamento, String codigoCargo) {
        return cargoRepository.findById(new CargoId(texto(codigoDepartamento).toUpperCase(),
                        texto(codigoCargo).toUpperCase()))
                .orElseThrow(() -> new ReglaNegocioException("El cargo no existe."));
    }

    private static String validarDescripcion(String descripcion) {
        String valor = texto(descripcion).replaceAll("\\s+", " ");
        if (valor.length() < 3) {
            throw new ReglaNegocioException("La descripción del cargo es obligatoria (mínimo 3 caracteres).");
        }
        if (valor.length() > 60) {
            throw new ReglaNegocioException("La descripción del cargo no puede superar 60 caracteres.");
        }
        if (!valor.matches(PATRON_DESCRIPCION)) {
            throw new ReglaNegocioException("La descripción del cargo solo admite letras, espacios y los signos / . -");
        }
        return valor;
    }

    private static String texto(String valor) {
        return valor == null ? "" : valor.trim();
    }
}
