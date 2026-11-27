/*
 * SIGH - Hospital General Monster Salud
 * Talleres Login y CRUD MVC en Java - Grupo GR07
 * Subsistema de Seguridad
 */
package ec.edu.monster.G07.logingr07.servicio;

import ec.edu.monster.G07.logingr07.dto.EmpleadoDTO;
import ec.edu.monster.G07.logingr07.dto.ReglaNegocioException;
import ec.edu.monster.G07.logingr07.dto.UsuarioDTO;
import ec.edu.monster.G07.logingr07.dto.UsuarioDetalleDTO;
import ec.edu.monster.G07.logingr07.modelo.Empleado;
import ec.edu.monster.G07.logingr07.modelo.Perfil;
import ec.edu.monster.G07.logingr07.modelo.UsuPer;
import ec.edu.monster.G07.logingr07.modelo.UsuPerId;
import ec.edu.monster.G07.logingr07.modelo.Usuario;
import ec.edu.monster.G07.logingr07.repository.EmpleadoRepository;
import ec.edu.monster.G07.logingr07.repository.EstadoRepository;
import ec.edu.monster.G07.logingr07.repository.PerfilRepository;
import ec.edu.monster.G07.logingr07.repository.SesionRepository;
import ec.edu.monster.G07.logingr07.repository.UsuPerRepository;
import ec.edu.monster.G07.logingr07.repository.UsuarioRepository;
import ec.edu.monster.G07.logingr07.utils.SeguridadUtil;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * CRUD de usuarios del caso de uso ECUD2 Administrar usuarios y roles (RF-SEG-002, RF-SEG-003):
 * Create (crear cuenta), Read (listar y ver detalle), Update (modificar y asignar perfil) y
 * Delete lógico (desactivar). Nunca se borran usuarios físicamente: la desactivación conserva
 * su historial de sesiones y auditoría, como exige el ERS.
 */
@Service
public class UsuarioServicio {

    private static final Map<String, String> CARGOS = Map.of(
            "ADS", "Administrador de sistemas",
            "MED", "Médico",
            "ENF", "Enfermero/a",
            "FAR", "Farmacéutico/a");

    private static final Map<String, String> DEPARTAMENTOS = Map.of(
            "ADM", "Administración",
            "CEX", "Consulta externa",
            "HOS", "Hospitalización",
            "FAR", "Farmacia");

    private final UsuarioRepository usuarioRepository;
    private final EmpleadoRepository empleadoRepository;
    private final EstadoRepository estadoRepository;
    private final PerfilRepository perfilRepository;
    private final UsuPerRepository usuPerRepository;
    private final SesionRepository sesionRepository;
    private final AuditoriaServicio auditoria;

    public UsuarioServicio(UsuarioRepository usuarioRepository, EmpleadoRepository empleadoRepository,
            EstadoRepository estadoRepository, PerfilRepository perfilRepository,
            UsuPerRepository usuPerRepository, SesionRepository sesionRepository, AuditoriaServicio auditoria) {
        this.usuarioRepository = usuarioRepository;
        this.empleadoRepository = empleadoRepository;
        this.estadoRepository = estadoRepository;
        this.perfilRepository = perfilRepository;
        this.usuPerRepository = usuPerRepository;
        this.sesionRepository = sesionRepository;
        this.auditoria = auditoria;
    }

    @Transactional(readOnly = true)
    public List<UsuarioDTO> listar() {
        return usuarioRepository.findAll().stream()
                .sorted(Comparator.comparing(Usuario::getCodigoUsuario))
                .map(this::aDto)
                .toList();
    }

    /** READ individual: cuenta, datos del empleado, historial de perfiles y últimas sesiones. */
    @Transactional(readOnly = true)
    public UsuarioDetalleDTO detalle(String codigoUsuario) {
        Usuario u = buscarUsuario(codigoUsuario);
        Empleado e = u.getEmpleado();
        List<UsuarioDetalleDTO.PerfilHistorial> perfiles = usuPerRepository.findByIdCodigoUsuario(u.getCodigoUsuario()).stream()
                .sorted(Comparator.comparing(UsuPer::getFechaAsignacion).reversed())
                .map(up -> new UsuarioDetalleDTO.PerfilHistorial(up.getPerfil().getCodigoPerfil(),
                        up.getPerfil().getDescripcionPerfil(), up.getFechaAsignacion(), up.getFechaRetiro()))
                .toList();
        List<UsuarioDetalleDTO.SesionResumen> sesiones = sesionRepository
                .findTop5ByUsuarioCodigoUsuarioOrderByFechaInicioDesc(u.getCodigoUsuario()).stream()
                .map(s -> new UsuarioDetalleDTO.SesionResumen(s.getCodigoSesion(), s.getFechaInicio(), s.getFechaFin(),
                        s.getDireccionIp(), s.getEstadoSesion()))
                .toList();
        return new UsuarioDetalleDTO(aDto(u), CARGOS.getOrDefault(e.getCodigoCargo(), e.getCodigoCargo()),
                DEPARTAMENTOS.getOrDefault(e.getCodigoDepartamento(), e.getCodigoDepartamento()),
                e.getTelefono(), e.getRegistroProfesional(), u.getFechaModificacion(), perfiles, sesiones);
    }

    @Transactional(readOnly = true)
    public List<EmpleadoDTO> empleadosSinCuenta() {
        return empleadoRepository.findAll().stream()
                .filter(e -> "A".equals(e.getEstadoEmpleado()))
                .filter(e -> !usuarioRepository.existsByEmpleadoCodigoEmpleado(e.getCodigoEmpleado()))
                .sorted(Comparator.comparing(Empleado::getApellidos))
                .map(e -> new EmpleadoDTO(e.getCodigoEmpleado(), e.getNombres() + " " + e.getApellidos(),
                        e.getCedula(), e.getEmail(), CARGOS.getOrDefault(e.getCodigoCargo(), e.getCodigoCargo())))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Perfil> perfiles() {
        return perfilRepository.findAll().stream()
                .sorted(Comparator.comparing(Perfil::getCodigoPerfil))
                .toList();
    }

    /** CREAR CUENTA DE USUARIO (flujo básico ECUD2). */
    @Transactional
    public UsuarioDTO crear(UsuarioDTO datos, String actor, String ip) {
        String codigo = texto(datos.codigoUsuario()).toLowerCase();
        if (!codigo.matches("^[a-z][a-z0-9._]{3,19}$")) {
            throw new ReglaNegocioException("El usuario debe tener de 4 a 20 caracteres: letras minúsculas, "
                    + "números, punto o guion bajo, y empezar con una letra.");
        }
        if (usuarioRepository.existsById(codigo)) {
            throw new ReglaNegocioException("Ya existe una cuenta con el usuario «" + codigo + "».");
        }
        Empleado empleado = empleadoRepository.findById(texto(datos.codigoEmpleado()))
                .orElseThrow(() -> new ReglaNegocioException("Seleccione un empleado válido."));
        if (usuarioRepository.existsByEmpleadoCodigoEmpleado(empleado.getCodigoEmpleado())) {
            throw new ReglaNegocioException("El empleado seleccionado ya tiene una cuenta de usuario.");
        }
        Perfil perfil = buscarPerfil(datos.codigoPerfil());
        String errorPassword = SeguridadUtil.validarPolitica(datos.password());
        if (errorPassword != null) {
            throw new ReglaNegocioException(errorPassword);
        }

        LocalDateTime ahora = LocalDateTime.now();
        Usuario usuario = new Usuario();
        usuario.setCodigoUsuario(codigo);
        usuario.setEmpleado(empleado);
        usuario.setEstado(estadoRepository.getReferenceById(AutenticacionServicio.ESTADO_ACTIVO));
        usuario.setPassword(SeguridadUtil.sha256(datos.password()));
        usuario.setIntentosFallidos((short) 0);
        usuario.setFechaCreacion(ahora);
        usuario.setPieFirma(limpiarPieFirma(datos.pieFirma()));
        usuarioRepository.save(usuario);

        asignarPerfil(usuario, perfil, ahora);
        auditoria.registrar(actor, "CREAR USUARIO", "EXITOSO",
                "Se creó el usuario " + codigo + " con perfil " + perfil.getCodigoPerfil(), ip);
        return aDto(usuario);
    }

    /** Modificar pie de firma, perfil (ASIGNAR ROLES Y PERMISOS) y, opcionalmente, la contraseña. */
    @Transactional
    public UsuarioDTO actualizar(String codigoUsuario, UsuarioDTO datos, String actor, String ip) {
        Usuario usuario = buscarUsuario(codigoUsuario);
        Perfil perfil = buscarPerfil(datos.codigoPerfil());
        LocalDateTime ahora = LocalDateTime.now();

        StringBuilder cambios = new StringBuilder();
        String perfilAnterior = perfilVigente(usuario.getCodigoUsuario()).map(up -> up.getPerfil().getCodigoPerfil()).orElse("-");
        if (!perfil.getCodigoPerfil().equals(perfilAnterior)) {
            if (actor.equals(usuario.getCodigoUsuario())) {
                throw new ReglaNegocioException("No puede cambiar su propio perfil.");
            }
            asignarPerfil(usuario, perfil, ahora);
            cambios.append("perfil ").append(perfilAnterior).append(" -> ").append(perfil.getCodigoPerfil()).append("; ");
        }
        String pieFirma = limpiarPieFirma(datos.pieFirma());
        if (!java.util.Objects.equals(pieFirma, usuario.getPieFirma())) {
            usuario.setPieFirma(pieFirma);
            cambios.append("pie de firma; ");
        }
        if (datos.password() != null && !datos.password().isBlank()) {
            String errorPassword = SeguridadUtil.validarPolitica(datos.password());
            if (errorPassword != null) {
                throw new ReglaNegocioException(errorPassword);
            }
            usuario.setPassword(SeguridadUtil.sha256(datos.password()));
            cambios.append("contraseña restablecida por el administrador; ");
        }
        if (cambios.isEmpty()) {
            throw new ReglaNegocioException("No hay cambios para guardar.");
        }
        usuario.setFechaModificacion(ahora);
        usuarioRepository.save(usuario);
        auditoria.registrar(actor, "MODIFICAR USUARIO", "EXITOSO",
                "Usuario " + usuario.getCodigoUsuario() + ": " + cambios.toString().trim(), ip);
        return aDto(usuario);
    }

    /** DESACTIVAR CUENTA, activarla de nuevo o desbloquearla. */
    @Transactional
    public UsuarioDTO cambiarEstado(String codigoUsuario, String accion, String actor, String ip) {
        Usuario usuario = buscarUsuario(codigoUsuario);
        String estadoActual = usuario.getEstado().getCodigoEstado();
        String nuevoEstado;
        String operacion;
        switch (accion) {
            case "desactivar" -> {
                if (actor.equals(usuario.getCodigoUsuario())) {
                    throw new ReglaNegocioException("No puede desactivar su propia cuenta.");
                }
                if (AutenticacionServicio.ESTADO_INACTIVO.equals(estadoActual)) {
                    throw new ReglaNegocioException("La cuenta ya está desactivada.");
                }
                nuevoEstado = AutenticacionServicio.ESTADO_INACTIVO;
                operacion = "DESACTIVAR USUARIO";
            }
            case "activar" -> {
                if (!AutenticacionServicio.ESTADO_INACTIVO.equals(estadoActual)) {
                    throw new ReglaNegocioException("Solo se pueden activar cuentas desactivadas.");
                }
                nuevoEstado = AutenticacionServicio.ESTADO_ACTIVO;
                operacion = "ACTIVAR USUARIO";
            }
            case "desbloquear" -> {
                if (!AutenticacionServicio.ESTADO_BLOQUEADO.equals(estadoActual)) {
                    throw new ReglaNegocioException("La cuenta no está bloqueada.");
                }
                nuevoEstado = AutenticacionServicio.ESTADO_ACTIVO;
                operacion = "DESBLOQUEAR USUARIO";
            }
            default -> throw new ReglaNegocioException("Acción no válida.");
        }
        usuario.setEstado(estadoRepository.getReferenceById(nuevoEstado));
        usuario.setIntentosFallidos((short) 0);
        usuario.setFechaModificacion(LocalDateTime.now());
        usuarioRepository.save(usuario);
        auditoria.registrar(actor, operacion, "EXITOSO",
                "Usuario " + usuario.getCodigoUsuario() + ": estado " + estadoActual + " -> " + nuevoEstado, ip);
        return aDto(usuario);
    }

    /** Retira el perfil vigente (fecha de retiro) y asigna el nuevo, conservando el historial. */
    private void asignarPerfil(Usuario usuario, Perfil perfil, LocalDateTime ahora) {
        for (UsuPer vigente : usuPerRepository.findByIdCodigoUsuarioAndFechaRetiroIsNull(usuario.getCodigoUsuario())) {
            vigente.setFechaRetiro(ahora);
            usuPerRepository.save(vigente);
        }
        UsuPerId id = new UsuPerId(usuario.getCodigoUsuario(), perfil.getCodigoPerfil());
        UsuPer asignacion = usuPerRepository.findById(id).orElseGet(UsuPer::new);
        asignacion.setId(id);
        asignacion.setUsuario(usuario);
        asignacion.setPerfil(perfil);
        asignacion.setFechaAsignacion(ahora);
        asignacion.setFechaRetiro(null);
        usuPerRepository.save(asignacion);
    }

    private Optional<UsuPer> perfilVigente(String codigoUsuario) {
        return usuPerRepository.findByIdCodigoUsuarioAndFechaRetiroIsNull(codigoUsuario).stream()
                .max(Comparator.comparing(UsuPer::getFechaAsignacion));
    }

    private Usuario buscarUsuario(String codigoUsuario) {
        return usuarioRepository.findById(texto(codigoUsuario))
                .orElseThrow(() -> new ReglaNegocioException("La cuenta no existe."));
    }

    private Perfil buscarPerfil(String codigoPerfil) {
        return perfilRepository.findById(texto(codigoPerfil))
                .orElseThrow(() -> new ReglaNegocioException("Seleccione un perfil válido."));
    }

    private UsuarioDTO aDto(Usuario u) {
        Empleado e = u.getEmpleado();
        Optional<UsuPer> perfil = perfilVigente(u.getCodigoUsuario());
        return new UsuarioDTO(u.getCodigoUsuario(), e.getCodigoEmpleado(), e.getNombres() + " " + e.getApellidos(),
                e.getCedula(), e.getEmail(), u.getEstado().getCodigoEstado(), u.getEstado().getDescripcionEstado(),
                perfil.map(up -> up.getPerfil().getCodigoPerfil()).orElse(null),
                perfil.map(up -> up.getPerfil().getDescripcionPerfil()).orElse("Sin perfil"),
                u.getPieFirma(), u.getIntentosFallidos(), u.getFechaCreacion(), u.getUltimoAcceso(), null);
    }

    private static String limpiarPieFirma(String pieFirma) {
        String valor = texto(pieFirma);
        if (valor.length() > 100) {
            throw new ReglaNegocioException("El pie de firma no puede superar 100 caracteres.");
        }
        return valor.isEmpty() ? null : valor;
    }

    private static String texto(String valor) {
        return valor == null ? "" : valor.trim();
    }
}
