/*
 * SIGH - Hospital General Monster Salud
 * Talleres Login y CRUD MVC en Java - Grupo GR07
 * Subsistema de Personal
 */
package ec.edu.monster.G07.logingr07.servicio;

import ec.edu.monster.G07.logingr07.dto.AcademicoDTO;
import ec.edu.monster.G07.logingr07.dto.CatalogoDTO;
import ec.edu.monster.G07.logingr07.dto.EmpleadoFichaDTO;
import ec.edu.monster.G07.logingr07.dto.EmpleadoFormDTO;
import ec.edu.monster.G07.logingr07.dto.FamiliarDTO;
import ec.edu.monster.G07.logingr07.dto.ReglaNegocioException;
import ec.edu.monster.G07.logingr07.modelo.Cargo;
import ec.edu.monster.G07.logingr07.modelo.CargoId;
import ec.edu.monster.G07.logingr07.modelo.DatoAcademico;
import ec.edu.monster.G07.logingr07.modelo.Empleado;
import ec.edu.monster.G07.logingr07.modelo.EstadoCivil;
import ec.edu.monster.G07.logingr07.modelo.Familiar;
import ec.edu.monster.G07.logingr07.modelo.Genero;
import ec.edu.monster.G07.logingr07.modelo.NivelEstudio;
import ec.edu.monster.G07.logingr07.modelo.Parentesco;
import ec.edu.monster.G07.logingr07.repository.CargoRepository;
import ec.edu.monster.G07.logingr07.repository.DatoAcademicoRepository;
import ec.edu.monster.G07.logingr07.repository.EmpleadoRepository;
import ec.edu.monster.G07.logingr07.repository.EstadoCivilRepository;
import ec.edu.monster.G07.logingr07.repository.FamiliarRepository;
import ec.edu.monster.G07.logingr07.repository.GeneroRepository;
import ec.edu.monster.G07.logingr07.repository.NivelEstudioRepository;
import ec.edu.monster.G07.logingr07.repository.ParentescoRepository;
import ec.edu.monster.G07.logingr07.repository.TipoSangreRepository;
import ec.edu.monster.G07.logingr07.repository.UsuarioRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Mantenimiento de empleados (opción P003). CRUD sobre PEEMP_EMPLE con borrado lógico: «Borrar»
 * no elimina la fila, cambia PEEMP_ESTADO a I (inactivo). Un empleado con cuenta de usuario activa
 * no se puede desactivar hasta que se desactive su cuenta en Seguridad.
 */
@Service
public class EmpleadoServicio {

    public static final String MODULO = "PERSONAL";
    public static final String ACTIVO = "A";
    public static final String INACTIVO = "I";

    private static final String PATRON_NOMBRE = "^[A-ZÁÉÍÓÚÜÑ ]+$";
    private static final String PATRON_EMAIL = "^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,}$";
    private static final String PATRON_REGISTRO = "^[A-Z0-9\\-/.]+$";

    private final EmpleadoRepository empleadoRepository;
    private final GeneroRepository generoRepository;
    private final EstadoCivilRepository estadoCivilRepository;
    private final CargoRepository cargoRepository;
    private final UsuarioRepository usuarioRepository;
    private final TipoSangreRepository tipoSangreRepository;
    private final NivelEstudioRepository nivelRepository;
    private final ParentescoRepository parentescoRepository;
    private final DatoAcademicoRepository academicoRepository;
    private final FamiliarRepository familiarRepository;
    private final AuditoriaServicio auditoria;

    public EmpleadoServicio(EmpleadoRepository empleadoRepository, GeneroRepository generoRepository,
            EstadoCivilRepository estadoCivilRepository, CargoRepository cargoRepository,
            UsuarioRepository usuarioRepository, TipoSangreRepository tipoSangreRepository,
            NivelEstudioRepository nivelRepository, ParentescoRepository parentescoRepository,
            DatoAcademicoRepository academicoRepository, FamiliarRepository familiarRepository,
            AuditoriaServicio auditoria) {
        this.empleadoRepository = empleadoRepository;
        this.generoRepository = generoRepository;
        this.estadoCivilRepository = estadoCivilRepository;
        this.cargoRepository = cargoRepository;
        this.usuarioRepository = usuarioRepository;
        this.tipoSangreRepository = tipoSangreRepository;
        this.nivelRepository = nivelRepository;
        this.parentescoRepository = parentescoRepository;
        this.academicoRepository = academicoRepository;
        this.familiarRepository = familiarRepository;
        this.auditoria = auditoria;
    }

    /** LISTAR: todos los empleados (activos e inactivos) con las descripciones de sus códigos. */
    @Transactional(readOnly = true)
    public List<EmpleadoFichaDTO> listar() {
        Map<String, String> generos = generoRepository.findAll().stream()
                .collect(Collectors.toMap(Genero::getCodigoGenero, Genero::getDescripcion));
        Map<String, String> estados = estadoCivilRepository.findAll().stream()
                .collect(Collectors.toMap(EstadoCivil::getCodigoEstadoCivil, EstadoCivil::getDescripcion));
        Map<CargoId, Cargo> cargos = cargoRepository.listarConDepartamento().stream()
                .collect(Collectors.toMap(Cargo::getId, Function.identity()));
        Map<String, String> niveles = nivelRepository.findAll().stream()
                .collect(Collectors.toMap(NivelEstudio::getCodigoNivel, NivelEstudio::getDescripcion));
        Map<String, String> parentescos = parentescoRepository.findAll().stream()
                .collect(Collectors.toMap(Parentesco::getCodigoParentesco, Parentesco::getDescripcion));
        Map<String, List<AcademicoDTO>> academicos = academicoRepository.findAllByOrderByCodigoAsc().stream()
                .collect(Collectors.groupingBy(DatoAcademico::getCodigoEmpleado, Collectors.mapping(
                        a -> new AcademicoDTO(a.getCodigoNivel(), niveles.getOrDefault(a.getCodigoNivel(), a.getCodigoNivel()),
                                a.getInstitucion(), a.getTitulo()), Collectors.toList())));
        Map<String, List<FamiliarDTO>> familiares = familiarRepository.findAllByOrderByCodigoAsc().stream()
                .collect(Collectors.groupingBy(Familiar::getCodigoEmpleado, Collectors.mapping(
                        f -> new FamiliarDTO(f.getCodigoParentesco(),
                                parentescos.getOrDefault(f.getCodigoParentesco(), f.getCodigoParentesco()),
                                f.getApellidos(), f.getNombres(), f.getFechaNacimiento().toString()), Collectors.toList())));
        return empleadoRepository.findAllByOrderByApellidosAscNombresAsc().stream()
                .map(e -> aDto(e, generos, estados, cargos,
                        academicos.getOrDefault(e.getCodigoEmpleado(), List.of()),
                        familiares.getOrDefault(e.getCodigoEmpleado(), List.of())))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CatalogoDTO> generos() {
        return generoRepository.findAllByOrderByDescripcionAsc().stream()
                .map(g -> new CatalogoDTO(g.getCodigoGenero(), g.getDescripcion()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CatalogoDTO> estadosCiviles() {
        return estadoCivilRepository.findAllByOrderByDescripcionAsc().stream()
                .map(e -> new CatalogoDTO(e.getCodigoEstadoCivil(), e.getDescripcion()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CatalogoDTO> tiposSangre() {
        return tipoSangreRepository.findAllByOrderByCodigoTipoSangreAsc().stream()
                .map(t -> new CatalogoDTO(t.getCodigoTipoSangre(), t.getDescripcion()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CatalogoDTO> niveles() {
        return nivelRepository.findAllByOrderByCodigoNivelAsc().stream()
                .map(n -> new CatalogoDTO(n.getCodigoNivel(), n.getDescripcion()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CatalogoDTO> parentescos() {
        return parentescoRepository.findAllByOrderByDescripcionAsc().stream()
                .map(p -> new CatalogoDTO(p.getCodigoParentesco(), p.getDescripcion()))
                .toList();
    }

    /** Código que tendrá el próximo empleado: EMP + 7 dígitos (EMP0000007). */
    @Transactional(readOnly = true)
    public String siguienteCodigo() {
        int ultimo = empleadoRepository.findTopByOrderByCodigoEmpleadoDesc()
                .map(e -> e.getCodigoEmpleado().replaceAll("\\D", ""))
                .filter(n -> !n.isEmpty())
                .map(Integer::parseInt)
                .orElse(0);
        return String.format("EMP%07d", ultimo + 1);
    }

    /** GUARDAR: registra un empleado nuevo (activo). */
    @Transactional
    public EmpleadoFichaDTO crear(EmpleadoFormDTO datos, String actor, String ip) {
        Empleado e = new Empleado();
        e.setCodigoEmpleado(siguienteCodigo());
        aplicar(e, datos, true);
        e.setEstadoEmpleado(ACTIVO);
        List<DatoAcademico> academicos = academicos(datos.academicos());
        List<Familiar> familiares = familiares(datos.familiares());
        empleadoRepository.saveAndFlush(e);
        guardarHijos(e.getCodigoEmpleado(), academicos, familiares);
        auditoria.registrar(MODULO, actor, "CREAR EMPLEADO", "EXITOSO",
                "Se registró el empleado " + e.getCodigoEmpleado() + " - " + e.getApellidos() + " " + e.getNombres()
                        + " (cédula " + e.getCedula() + ")", ip);
        return listar().stream().filter(f -> f.codigoEmpleado().equals(e.getCodigoEmpleado())).findFirst().orElseThrow();
    }

    /** ACTUALIZAR: modifica todos los datos menos el código; permite reactivar un empleado inactivo. */
    @Transactional
    public void actualizar(String codigoEmpleado, EmpleadoFormDTO datos, String actor, String ip) {
        Empleado e = buscar(codigoEmpleado);
        String estadoAnterior = e.getEstadoEmpleado();
        aplicar(e, datos, false);
        String estado = texto(datos.estado()).toUpperCase(Locale.ROOT);
        if (!ACTIVO.equals(estado) && !INACTIVO.equals(estado)) {
            throw new ReglaNegocioException("Seleccione el estado del empleado (Activo o Inactivo).");
        }
        if (INACTIVO.equals(estado) && ACTIVO.equals(estadoAnterior)) {
            validarSinCuentaActiva(e);
        }
        e.setEstadoEmpleado(estado);
        List<DatoAcademico> academicos = academicos(datos.academicos());
        List<Familiar> familiares = familiares(datos.familiares());
        empleadoRepository.save(e);
        guardarHijos(e.getCodigoEmpleado(), academicos, familiares);
        String cambioEstado = estado.equals(estadoAnterior) ? ""
                : " Estado: " + nombreEstado(estadoAnterior) + " -> " + nombreEstado(estado) + ".";
        auditoria.registrar(MODULO, actor, "MODIFICAR EMPLEADO", "EXITOSO",
                "Se actualizaron los datos del empleado " + e.getCodigoEmpleado() + " - " + e.getApellidos() + " "
                        + e.getNombres() + "." + cambioEstado, ip);
    }

    /** BORRAR: borrado lógico, el empleado pasa a Inactivo y se conserva su historial. */
    @Transactional
    public void borrar(String codigoEmpleado, String actor, String ip) {
        Empleado e = buscar(codigoEmpleado);
        if (INACTIVO.equals(e.getEstadoEmpleado())) {
            throw new ReglaNegocioException("El empleado " + e.getCodigoEmpleado() + " ya está inactivo.");
        }
        validarSinCuentaActiva(e);
        e.setEstadoEmpleado(INACTIVO);
        empleadoRepository.save(e);
        auditoria.registrar(MODULO, actor, "BORRAR EMPLEADO", "EXITOSO",
                "Borrado lógico del empleado " + e.getCodigoEmpleado() + " - " + e.getApellidos() + " "
                        + e.getNombres() + " (estado: Inactivo)", ip);
    }

    private void validarSinCuentaActiva(Empleado e) {
        if (usuarioRepository.existsByEmpleadoCodigoEmpleadoAndEstadoCodigoEstadoNot(e.getCodigoEmpleado(), INACTIVO)) {
            throw new ReglaNegocioException("El empleado " + e.getCodigoEmpleado()
                    + " tiene una cuenta de usuario activa. Desactive primero su cuenta en Seguridad > Administrar usuarios.");
        }
    }

    /** Valida y copia los datos del formulario al empleado. */
    private void aplicar(Empleado e, EmpleadoFormDTO d, boolean nuevo) {
        String cedula = texto(d.cedula());
        // Los registros anteriores pueden tener cédulas de prueba: solo se valida el dígito verificador si cambia
        boolean cedulaNueva = nuevo || !cedula.equals(e.getCedula());
        if (cedulaNueva && !cedulaValida(cedula)) {
            throw new ReglaNegocioException("La cédula no es válida: debe tener 10 dígitos y un dígito verificador correcto.");
        }
        boolean cedulaRepetida = nuevo ? empleadoRepository.existsByCedula(cedula)
                : empleadoRepository.existsByCedulaAndCodigoEmpleadoNot(cedula, e.getCodigoEmpleado());
        if (cedulaRepetida) {
            throw new ReglaNegocioException("Ya existe un empleado con la cédula " + cedula + ".");
        }
        String apellidos = nombre(d.apellidos(), "Los apellidos");
        String nombres = nombre(d.nombres(), "Los nombres");

        LocalDate fecha;
        try {
            fecha = LocalDate.parse(texto(d.fechaNacimiento()));
        } catch (DateTimeParseException ex) {
            throw new ReglaNegocioException("Ingrese la fecha de nacimiento.");
        }
        int edad = Period.between(fecha, LocalDate.now()).getYears();
        if (edad < 18 || edad > 100) {
            throw new ReglaNegocioException("La fecha de nacimiento no es válida: el empleado debe tener entre 18 y 100 años.");
        }

        String genero = texto(d.codigoGenero()).toUpperCase(Locale.ROOT);
        if (genero.isEmpty() || !generoRepository.existsById(genero)) {
            throw new ReglaNegocioException("Seleccione el género.");
        }
        String estadoCivil = texto(d.codigoEstadoCivil()).toUpperCase(Locale.ROOT);
        if (estadoCivil.isEmpty() || !estadoCivilRepository.existsById(estadoCivil)) {
            throw new ReglaNegocioException("Seleccione el estado civil.");
        }

        String tipoSangre = texto(d.codigoTipoSangre()).toUpperCase(Locale.ROOT);
        if (tipoSangre.isEmpty() || !tipoSangreRepository.existsById(tipoSangre)) {
            throw new ReglaNegocioException("Seleccione el tipo de sangre.");
        }
        BigDecimal peso = decimal(d.peso(), "El peso", new BigDecimal("2"), new BigDecimal("300"), 2);
        BigDecimal estatura = decimal(d.estatura(), "La estatura", new BigDecimal("0.40"), new BigDecimal("2.50"), 2);

        String registro = texto(d.registroProfesional()).toUpperCase(Locale.ROOT);
        if (registro.length() > 30 || (!registro.isEmpty() && !registro.matches(PATRON_REGISTRO))) {
            throw new ReglaNegocioException("El registro profesional admite hasta 30 letras, números y los signos - / .");
        }

        String dep = texto(d.codigoDepartamento()).toUpperCase(Locale.ROOT);
        String car = texto(d.codigoCargo()).toUpperCase(Locale.ROOT);
        if (dep.isEmpty() || car.isEmpty() || !cargoRepository.existsById(new CargoId(dep, car))) {
            throw new ReglaNegocioException("Seleccione el cargo del empleado.");
        }

        String direccion = texto(d.direccion()).replaceAll("\\s+", " ");
        if (direccion.length() > 150) {
            throw new ReglaNegocioException("La dirección no puede superar 150 caracteres.");
        }
        String telefono = texto(d.telefono());
        if (!telefono.isEmpty() && !telefono.matches("^\\d{7,10}$")) {
            throw new ReglaNegocioException("El teléfono debe tener entre 7 y 10 dígitos.");
        }
        String email = texto(d.email()).toLowerCase(Locale.ROOT);
        if (email.length() > 100 || !email.matches(PATRON_EMAIL)) {
            throw new ReglaNegocioException("Ingrese un email válido. Ejemplo: nombre@monstersalud.ec");
        }
        boolean emailRepetido = nuevo ? empleadoRepository.existsByEmailIgnoreCase(email)
                : empleadoRepository.existsByEmailIgnoreCaseAndCodigoEmpleadoNot(email, e.getCodigoEmpleado());
        if (emailRepetido) {
            throw new ReglaNegocioException("El email " + email + " ya pertenece a otro empleado.");
        }

        e.setCedula(cedula);
        e.setApellidos(apellidos);
        e.setNombres(nombres);
        e.setFechaNacimiento(fecha);
        e.setCodigoGenero(genero);
        e.setCodigoEstadoCivil(estadoCivil);
        e.setCodigoTipoSangre(tipoSangre);
        e.setPeso(peso);
        e.setEstatura(estatura);
        e.setRegistroProfesional(registro.isEmpty() ? null : registro);
        e.setCodigoDepartamento(dep);
        e.setCodigoCargo(car);
        e.setDireccion(direccion.isEmpty() ? null : direccion);
        e.setTelefono(telefono.isEmpty() ? null : telefono);
        e.setEmail(email);
    }

    /** Número decimal obligatorio dentro de un rango (acepta coma o punto). */
    private static BigDecimal decimal(String valor, String campo, BigDecimal minimo, BigDecimal maximo, int decimales) {
        String v = texto(valor).replace(',', '.');
        BigDecimal numero;
        try {
            numero = new BigDecimal(v);
        } catch (NumberFormatException ex) {
            throw new ReglaNegocioException(campo + " es obligatorio y debe ser un número.");
        }
        if (numero.compareTo(minimo) < 0 || numero.compareTo(maximo) > 0) {
            throw new ReglaNegocioException(campo + " debe estar entre " + minimo + " y " + maximo + ".");
        }
        return numero.setScale(decimales, RoundingMode.HALF_UP);
    }

    /** Valida las filas de la grilla de datos académicos. */
    private List<DatoAcademico> academicos(List<AcademicoDTO> filas) {
        List<DatoAcademico> lista = new ArrayList<>();
        if (filas == null) {
            return lista;
        }
        int n = 0;
        for (AcademicoDTO f : filas) {
            n++;
            String nivel = texto(f.codigoNivel());
            if (nivel.isEmpty() || !nivelRepository.existsById(nivel)) {
                throw new ReglaNegocioException("Datos académicos, fila " + n + ": seleccione el nivel de estudio.");
            }
            String institucion = texto(f.institucion()).replaceAll("\\s+", " ");
            String titulo = texto(f.titulo()).replaceAll("\\s+", " ");
            if (institucion.length() < 3 || institucion.length() > 100) {
                throw new ReglaNegocioException("Datos académicos, fila " + n + ": la institución debe tener de 3 a 100 caracteres.");
            }
            if (titulo.length() < 3 || titulo.length() > 100) {
                throw new ReglaNegocioException("Datos académicos, fila " + n + ": el título debe tener de 3 a 100 caracteres.");
            }
            DatoAcademico a = new DatoAcademico();
            a.setCodigoNivel(nivel);
            a.setInstitucion(institucion);
            a.setTitulo(titulo);
            lista.add(a);
        }
        return lista;
    }

    /** Valida las filas de la grilla de datos familiares. */
    private List<Familiar> familiares(List<FamiliarDTO> filas) {
        List<Familiar> lista = new ArrayList<>();
        if (filas == null) {
            return lista;
        }
        int n = 0;
        for (FamiliarDTO f : filas) {
            n++;
            String parentesco = texto(f.codigoParentesco());
            if (parentesco.isEmpty() || !parentescoRepository.existsById(parentesco)) {
                throw new ReglaNegocioException("Datos familiares, fila " + n + ": seleccione el parentesco.");
            }
            String apellidos = nombre(f.apellidos(), "Datos familiares, fila " + n + ": los apellidos");
            String nombres = nombre(f.nombres(), "Datos familiares, fila " + n + ": los nombres");
            LocalDate fecha;
            try {
                fecha = LocalDate.parse(texto(f.fechaNacimiento()));
            } catch (DateTimeParseException ex) {
                throw new ReglaNegocioException("Datos familiares, fila " + n + ": ingrese la fecha de nacimiento.");
            }
            if (fecha.isAfter(LocalDate.now()) || fecha.isBefore(LocalDate.now().minusYears(120))) {
                throw new ReglaNegocioException("Datos familiares, fila " + n + ": la fecha de nacimiento no es válida.");
            }
            Familiar x = new Familiar();
            x.setCodigoParentesco(parentesco);
            x.setApellidos(apellidos);
            x.setNombres(nombres);
            x.setFechaNacimiento(fecha);
            lista.add(x);
        }
        return lista;
    }

    /** Reemplaza las filas de las grillas del empleado por las que envió el formulario. */
    private void guardarHijos(String codigoEmpleado, List<DatoAcademico> academicos, List<Familiar> familiares) {
        academicoRepository.borrarDeEmpleado(codigoEmpleado);
        familiarRepository.borrarDeEmpleado(codigoEmpleado);
        academicos.forEach(a -> a.setCodigoEmpleado(codigoEmpleado));
        familiares.forEach(f -> f.setCodigoEmpleado(codigoEmpleado));
        academicoRepository.saveAll(academicos);
        familiarRepository.saveAll(familiares);
    }

    /** Cédula ecuatoriana: 10 dígitos, provincia 01-24 o 30, tercer dígito menor a 6 y verificador módulo 10. */
    static boolean cedulaValida(String cedula) {
        if (cedula == null || !cedula.matches("^\\d{10}$")) {
            return false;
        }
        int provincia = Integer.parseInt(cedula.substring(0, 2));
        if ((provincia < 1 || provincia > 24) && provincia != 30) {
            return false;
        }
        if (cedula.charAt(2) - '0' >= 6) {
            return false;
        }
        int suma = 0;
        for (int i = 0; i < 9; i++) {
            int digito = (cedula.charAt(i) - '0') * (i % 2 == 0 ? 2 : 1);
            suma += digito > 9 ? digito - 9 : digito;
        }
        int verificador = (10 - suma % 10) % 10;
        return verificador == cedula.charAt(9) - '0';
    }

    private static String nombre(String valor, String campo) {
        String v = texto(valor).replaceAll("\\s+", " ").toUpperCase(Locale.forLanguageTag("es-EC"));
        if (v.length() < 2 || v.length() > 60) {
            throw new ReglaNegocioException(campo + " son obligatorios (de 2 a 60 caracteres).");
        }
        if (!v.matches(PATRON_NOMBRE)) {
            throw new ReglaNegocioException(campo + " solo admiten letras y espacios.");
        }
        return v;
    }

    private Empleado buscar(String codigoEmpleado) {
        return empleadoRepository.findById(texto(codigoEmpleado).toUpperCase(Locale.ROOT))
                .orElseThrow(() -> new ReglaNegocioException("El empleado no existe."));
    }

    private EmpleadoFichaDTO aDto(Empleado e, Map<String, String> generos, Map<String, String> estados,
            Map<CargoId, Cargo> cargos, List<AcademicoDTO> academicos, List<FamiliarDTO> familiares) {
        Cargo cargo = cargos.get(new CargoId(e.getCodigoDepartamento(), e.getCodigoCargo()));
        return new EmpleadoFichaDTO(e.getCodigoEmpleado(), e.getCedula(), e.getApellidos(), e.getNombres(),
                e.getFechaNacimiento(), e.getCodigoGenero(), generos.getOrDefault(e.getCodigoGenero(), e.getCodigoGenero()),
                e.getCodigoEstadoCivil(), e.getCodigoEstadoCivil() == null ? "" : estados.getOrDefault(
                        e.getCodigoEstadoCivil(), e.getCodigoEstadoCivil()),
                e.getCodigoTipoSangre(), e.getPeso(), e.getEstatura(),
                e.getRegistroProfesional(), e.getEstadoEmpleado(), e.getCodigoDepartamento(),
                cargo == null ? e.getCodigoDepartamento() : cargo.getDepartamento().getDescripcion(),
                e.getCodigoCargo(), cargo == null ? e.getCodigoCargo() : cargo.getDescripcion(),
                e.getDireccion(), e.getTelefono(), e.getEmail(),
                usuarioRepository.existsByEmpleadoCodigoEmpleado(e.getCodigoEmpleado()), academicos, familiares);
    }

    private static String nombreEstado(String estado) {
        return ACTIVO.equals(estado) ? "Activo" : "Inactivo";
    }

    private static String texto(String valor) {
        return valor == null ? "" : valor.trim();
    }
}
