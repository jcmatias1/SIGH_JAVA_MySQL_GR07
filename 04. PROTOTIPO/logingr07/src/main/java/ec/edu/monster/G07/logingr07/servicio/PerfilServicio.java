/*
 * SIGH - Hospital General Monster Salud
 * Talleres Login y CRUD MVC en Java - Grupo GR07
 * Subsistema de Seguridad
 */
package ec.edu.monster.G07.logingr07.servicio;

import ec.edu.monster.G07.logingr07.dto.PerfilDTO;
import ec.edu.monster.G07.logingr07.dto.PerfilFormDTO;
import ec.edu.monster.G07.logingr07.dto.PerfilDTO.OpcionPermisoDTO;
import ec.edu.monster.G07.logingr07.dto.ReglaNegocioException;
import ec.edu.monster.G07.logingr07.modelo.OpcPer;
import ec.edu.monster.G07.logingr07.modelo.OpcPerId;
import ec.edu.monster.G07.logingr07.modelo.Opcion;
import ec.edu.monster.G07.logingr07.modelo.Perfil;
import ec.edu.monster.G07.logingr07.repository.OpcPerRepository;
import ec.edu.monster.G07.logingr07.repository.OpcionRepository;
import ec.edu.monster.G07.logingr07.repository.PerfilRepository;
import ec.edu.monster.G07.logingr07.repository.UsuPerRepository;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * CRUD de perfiles (roles) y gestión de permisos (ECUD2, RF-SEG-003, RF-CAT-001):
 * crear, consultar, modificar y eliminar perfiles, y definir qué opciones de cada sistema
 * tiene cada uno. Asignar o revocar una opción actualiza XEOXP_OPCPE (la revocación solo
 * pone fecha de retiro).
 */
@Service
public class PerfilServicio {

    /** Opciones que el perfil administrador no puede perder (dejaría el sistema sin administrador). */
    private static final Set<String> OPCIONES_CRITICAS_ADM = Set.of("S001", "S002");

    private final PerfilRepository perfilRepository;
    private final OpcionRepository opcionRepository;
    private final OpcPerRepository opcPerRepository;
    private final UsuPerRepository usuPerRepository;
    private final AuditoriaServicio auditoria;

    public PerfilServicio(PerfilRepository perfilRepository, OpcionRepository opcionRepository,
            OpcPerRepository opcPerRepository, UsuPerRepository usuPerRepository, AuditoriaServicio auditoria) {
        this.perfilRepository = perfilRepository;
        this.opcionRepository = opcionRepository;
        this.opcPerRepository = opcPerRepository;
        this.usuPerRepository = usuPerRepository;
        this.auditoria = auditoria;
    }

    @Transactional(readOnly = true)
    public List<PerfilDTO> listar() {
        List<Opcion> opciones = opcionRepository.findAll().stream()
                .sorted(Comparator.comparing((Opcion o) -> o.getSistema().getCodigoSistema())
                        .thenComparing(Opcion::getCodigoOpcion))
                .toList();
        return perfilRepository.findAll().stream()
                .sorted(Comparator.comparing(Perfil::getCodigoPerfil))
                .map(perfil -> {
                    Set<String> vigentes = opcPerRepository.findByIdCodigoPerfil(perfil.getCodigoPerfil()).stream()
                            .filter(op -> op.getFechaRetiro() == null)
                            .map(op -> op.getId().getCodigoOpcion())
                            .collect(Collectors.toSet());
                    long usuarios = perfil.getUsuarios().stream()
                            .filter(up -> up.getFechaRetiro() == null)
                            .count();
                    long historicas = perfil.getUsuarios().size();
                    List<OpcionPermisoDTO> permisos = opciones.stream()
                            .map(o -> new OpcionPermisoDTO(o.getCodigoOpcion(), o.getDescripcionOpcion(),
                                    o.getSistema().getCodigoSistema(), o.getSistema().getDescripcionSistema(),
                                    o.getUrlOpcion(), vigentes.contains(o.getCodigoOpcion())))
                            .toList();
                    return new PerfilDTO(perfil.getCodigoPerfil(), perfil.getDescripcionPerfil(),
                            perfil.getObservaciones(), usuarios, historicas, permisos);
                })
                .toList();
    }

    /** CREATE: registra un perfil nuevo sin opciones; luego se le asignan con los interruptores. */
    @Transactional
    public PerfilDTO crear(PerfilFormDTO datos, String actor, String ip) {
        String codigo = texto(datos.codigoPerfil()).toUpperCase();
        if (!codigo.matches("^[A-Z]{3}$")) {
            throw new ReglaNegocioException("El código del perfil debe tener exactamente 3 letras. Ejemplo: LAB.");
        }
        if (perfilRepository.existsById(codigo)) {
            throw new ReglaNegocioException("Ya existe un perfil con el código " + codigo + ".");
        }
        String descripcion = validarDescripcion(datos.descripcion());
        if (perfilRepository.existsByDescripcionPerfilIgnoreCase(descripcion)) {
            throw new ReglaNegocioException("Ya existe un perfil llamado «" + descripcion + "».");
        }
        Perfil perfil = new Perfil();
        perfil.setCodigoPerfil(codigo);
        perfil.setDescripcionPerfil(descripcion);
        perfil.setObservaciones(validarObservaciones(datos.observaciones()));
        perfilRepository.save(perfil);
        auditoria.registrar(actor, "CREAR PERFIL", "EXITOSO", "Se creó el perfil " + codigo + " - " + descripcion, ip);
        return new PerfilDTO(codigo, descripcion, perfil.getObservaciones(), 0, 0, List.of());
    }

    /** UPDATE: modifica el nombre y las observaciones; el código es la clave y no cambia. */
    @Transactional
    public void actualizar(String codigoPerfil, PerfilFormDTO datos, String actor, String ip) {
        Perfil perfil = buscar(codigoPerfil);
        String descripcion = validarDescripcion(datos.descripcion());
        if (perfilRepository.existsByDescripcionPerfilIgnoreCaseAndCodigoPerfilNot(descripcion, perfil.getCodigoPerfil())) {
            throw new ReglaNegocioException("Ya existe otro perfil llamado «" + descripcion + "».");
        }
        String observaciones = validarObservaciones(datos.observaciones());
        String anterior = perfil.getDescripcionPerfil();
        perfil.setDescripcionPerfil(descripcion);
        perfil.setObservaciones(observaciones);
        perfilRepository.save(perfil);
        auditoria.registrar(actor, "MODIFICAR PERFIL", "EXITOSO",
                "Perfil " + perfil.getCodigoPerfil() + ": «" + anterior + "» -> «" + descripcion + "»", ip);
    }

    /**
     * DELETE: elimina físicamente el perfil y su configuración de opciones, solo si nunca se
     * asignó a un usuario (así no se pierde el historial de XEUXP_USUPE).
     */
    @Transactional
    public void eliminar(String codigoPerfil, String actor, String ip) {
        Perfil perfil = buscar(codigoPerfil);
        if ("ADM".equals(perfil.getCodigoPerfil())) {
            throw new ReglaNegocioException("El perfil Administrador no se puede eliminar.");
        }
        long vigentes = usuPerRepository.countByIdCodigoPerfilAndFechaRetiroIsNull(perfil.getCodigoPerfil());
        if (vigentes > 0) {
            throw new ReglaNegocioException("No se puede eliminar: el perfil está asignado a " + vigentes
                    + " usuario(s). Cambie primero el perfil de esos usuarios.");
        }
        long historicas = usuPerRepository.countByIdCodigoPerfil(perfil.getCodigoPerfil());
        if (historicas > 0) {
            throw new ReglaNegocioException("No se puede eliminar: el perfil tiene " + historicas
                    + " asignación(es) en el historial de usuarios. Para conservar ese historial, retire sus opciones.");
        }
        opcPerRepository.deleteByIdCodigoPerfil(perfil.getCodigoPerfil());
        perfilRepository.delete(perfil);
        auditoria.registrar(actor, "ELIMINAR PERFIL", "EXITOSO",
                "Se eliminó el perfil " + perfil.getCodigoPerfil() + " - " + perfil.getDescripcionPerfil(), ip);
    }

    private Perfil buscar(String codigoPerfil) {
        return perfilRepository.findById(texto(codigoPerfil).toUpperCase())
                .orElseThrow(() -> new ReglaNegocioException("El perfil no existe."));
    }

    private static String validarDescripcion(String descripcion) {
        String valor = texto(descripcion).toUpperCase();
        if (valor.length() < 3) {
            throw new ReglaNegocioException("El nombre del perfil es obligatorio (mínimo 3 caracteres).");
        }
        if (valor.length() > 60) {
            throw new ReglaNegocioException("El nombre del perfil no puede superar 60 caracteres.");
        }
        if (!valor.matches("^[A-ZÁÉÍÓÚÑÜ ]+$")) {
            throw new ReglaNegocioException("El nombre del perfil solo admite letras y espacios.");
        }
        return valor.replaceAll("\\s+", " ");
    }

    private static String validarObservaciones(String observaciones) {
        String valor = texto(observaciones);
        if (valor.length() > 200) {
            throw new ReglaNegocioException("Las observaciones no pueden superar 200 caracteres.");
        }
        return valor.isEmpty() ? null : valor;
    }

    private static String texto(String valor) {
        return valor == null ? "" : valor.trim();
    }

    @Transactional
    public void asignar(String codigoPerfil, String codigoOpcion, String actor, String ip) {
        Perfil perfil = perfilRepository.findById(codigoPerfil)
                .orElseThrow(() -> new ReglaNegocioException("El perfil no existe."));
        Opcion opcion = opcionRepository.findById(codigoOpcion)
                .orElseThrow(() -> new ReglaNegocioException("La opción no existe."));
        OpcPerId id = new OpcPerId(codigoPerfil, codigoOpcion);
        OpcPer asignacion = opcPerRepository.findById(id).orElseGet(OpcPer::new);
        if (asignacion.getId() != null && asignacion.getFechaRetiro() == null) {
            throw new ReglaNegocioException("La opción ya está asignada a este perfil.");
        }
        asignacion.setId(id);
        asignacion.setPerfil(perfil);
        asignacion.setOpcion(opcion);
        asignacion.setFechaAsignacion(LocalDateTime.now());
        asignacion.setFechaRetiro(null);
        opcPerRepository.save(asignacion);
        auditoria.registrar(actor, "ASIGNAR PERMISO", "EXITOSO",
                "Perfil " + codigoPerfil + " recibe la opción " + codigoOpcion + " (" + opcion.getDescripcionOpcion() + ")", ip);
    }

    @Transactional
    public void revocar(String codigoPerfil, String codigoOpcion, String actor, String ip) {
        if ("ADM".equals(codigoPerfil) && OPCIONES_CRITICAS_ADM.contains(codigoOpcion)) {
            throw new ReglaNegocioException("No se puede quitar esta opción al Administrador: el sistema quedaría sin quien administre usuarios y permisos.");
        }
        OpcPer asignacion = opcPerRepository.findById(new OpcPerId(codigoPerfil, codigoOpcion))
                .filter(op -> op.getFechaRetiro() == null)
                .orElseThrow(() -> new ReglaNegocioException("La opción no está asignada a este perfil."));
        asignacion.setFechaRetiro(LocalDateTime.now());
        opcPerRepository.save(asignacion);
        auditoria.registrar(actor, "REVOCAR PERMISO", "EXITOSO",
                "Perfil " + codigoPerfil + " pierde la opción " + codigoOpcion, ip);
    }
}
