/*
 * SIGH - Hospital General Monster Salud
 * Talleres Login y CRUD MVC en Java - Grupo GR07
 * Subsistema de Seguridad
 */
package ec.edu.monster.G07.logingr07.servicio;

import ec.edu.monster.G07.logingr07.dto.UsuarioSesion;
import ec.edu.monster.G07.logingr07.modelo.Empleado;
import ec.edu.monster.G07.logingr07.modelo.Sesion;
import ec.edu.monster.G07.logingr07.modelo.UsuPer;
import ec.edu.monster.G07.logingr07.modelo.Usuario;
import ec.edu.monster.G07.logingr07.repository.EstadoRepository;
import ec.edu.monster.G07.logingr07.repository.SesionRepository;
import ec.edu.monster.G07.logingr07.repository.UsuPerRepository;
import ec.edu.monster.G07.logingr07.repository.UsuarioRepository;
import ec.edu.monster.G07.logingr07.utils.SeguridadUtil;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Lógica del caso de uso ECUD1 Iniciar sesión (RF-SEG-001, RF-SEG-004, RF-AUD-004):
 * valida credenciales y estado de la cuenta, bloquea tras varios intentos fallidos,
 * crea y cierra las sesiones (XESES_SESIO) y registra cada intento en la auditoría.
 */
@Service
public class AutenticacionServicio {

    public static final String ESTADO_ACTIVO = "A";
    public static final String ESTADO_INACTIVO = "I";
    public static final String ESTADO_BLOQUEADO = "B";

    /** Mensaje genérico: no indica si falló el usuario o la contraseña. */
    public static final String MENSAJE_CREDENCIALES = "Usuario o contraseña incorrectos.";

    public enum Tipo { EXITOSO, CREDENCIALES, BLOQUEADO, INACTIVO, SIN_PERFIL }

    /** Resultado del intento de inicio de sesión. */
    public record Resultado(Tipo tipo, String mensaje, UsuarioSesion usuario) {

        public boolean exitoso() {
            return tipo == Tipo.EXITOSO;
        }
    }

    private final UsuarioRepository usuarioRepository;
    private final EstadoRepository estadoRepository;
    private final UsuPerRepository usuPerRepository;
    private final SesionRepository sesionRepository;
    private final AuditoriaServicio auditoria;

    @Value("${sigh.seguridad.max-intentos:3}")
    private int maxIntentos;

    @Value("${sigh.seguridad.minutos-bloqueo:15}")
    private int minutosBloqueo;

    public AutenticacionServicio(UsuarioRepository usuarioRepository, EstadoRepository estadoRepository,
            UsuPerRepository usuPerRepository, SesionRepository sesionRepository, AuditoriaServicio auditoria) {
        this.usuarioRepository = usuarioRepository;
        this.estadoRepository = estadoRepository;
        this.usuPerRepository = usuPerRepository;
        this.sesionRepository = sesionRepository;
        this.auditoria = auditoria;
    }

    @Transactional
    public Resultado autenticar(String codigoUsuario, String password, String ip) {
        String codigo = codigoUsuario == null ? "" : codigoUsuario.trim();
        if (codigo.isEmpty() || password == null || password.isEmpty()) {
            return new Resultado(Tipo.CREDENCIALES, "Ingrese su usuario y contraseña.", null);
        }

        Optional<Usuario> encontrado = usuarioRepository.findById(codigo);
        if (encontrado.isEmpty()) {
            auditoria.registrar(null, "INICIAR SESION", "FALLIDO", "Usuario inexistente: " + codigo, ip);
            return new Resultado(Tipo.CREDENCIALES, MENSAJE_CREDENCIALES, null);
        }

        Usuario usuario = encontrado.get();
        String estado = usuario.getEstado().getCodigoEstado();

        if (ESTADO_INACTIVO.equals(estado)) {
            auditoria.registrar(codigo, "INICIAR SESION", "DENEGADO", "Intento de acceso con cuenta desactivada", ip);
            return new Resultado(Tipo.INACTIVO,
                    "La cuenta no está habilitada. Comuníquese con el administrador del sistema.", null);
        }

        if (ESTADO_BLOQUEADO.equals(estado)) {
            long minutosRestantes = minutosRestantesBloqueo(usuario);
            if (minutosRestantes > 0) {
                auditoria.registrar(codigo, "INICIAR SESION", "BLOQUEADO", "Intento de acceso con cuenta bloqueada", ip);
                return new Resultado(Tipo.BLOQUEADO, "La cuenta está bloqueada temporalmente por intentos fallidos. "
                        + "Intente nuevamente en " + minutosRestantes + " minuto(s) o recupere su contraseña.", null);
            }
            // El tiempo de bloqueo ya terminó: se habilita la cuenta de nuevo.
            usuario.setEstado(estadoRepository.getReferenceById(ESTADO_ACTIVO));
            usuario.setIntentosFallidos((short) 0);
            auditoria.registrar(codigo, "DESBLOQUEO AUTOMATICO", "EXITOSO",
                    "Terminó el tiempo de bloqueo de " + minutosBloqueo + " minutos", ip);
        }

        if (!SeguridadUtil.coincide(password, usuario.getPassword())) {
            return registrarIntentoFallido(usuario, ip);
        }

        List<UsuPer> perfiles = usuPerRepository.findByIdCodigoUsuarioAndFechaRetiroIsNull(codigo);
        if (perfiles.isEmpty()) {
            auditoria.registrar(codigo, "INICIAR SESION", "DENEGADO", "El usuario no tiene un perfil vigente", ip);
            return new Resultado(Tipo.SIN_PERFIL,
                    "Su cuenta no tiene un perfil asignado. Comuníquese con el administrador del sistema.", null);
        }

        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime accesoAnterior = usuario.getUltimoAcceso();
        usuario.setIntentosFallidos((short) 0);
        usuario.setUltimoAcceso(ahora);
        usuarioRepository.save(usuario);

        Sesion sesion = new Sesion();
        sesion.setUsuario(usuario);
        sesion.setFechaInicio(ahora);
        sesion.setDireccionIp(ip);
        sesion.setEstadoSesion("A");
        sesionRepository.save(sesion);

        auditoria.registrar(codigo, "INICIAR SESION", "EXITOSO", "Inicio de sesión correcto (sesión " + sesion.getCodigoSesion() + ")", ip);

        Empleado empleado = usuario.getEmpleado();
        String nombre = capitalizar(empleado.getNombres()) + " " + capitalizar(empleado.getApellidos());
        String iniciales = inicial(empleado.getNombres()) + inicial(empleado.getApellidos());
        List<String> nombresPerfiles = perfiles.stream().map(up -> up.getPerfil().getDescripcionPerfil()).toList();

        UsuarioSesion datos = new UsuarioSesion(codigo, nombre.trim(), iniciales, usuario.getPieFirma(),
                nombresPerfiles, sesion.getCodigoSesion(), ahora, accesoAnterior, ip);
        return new Resultado(Tipo.EXITOSO, "Bienvenido " + nombre.trim(), datos);
    }

    /**
     * Cierra el registro de la sesión en XESES_SESIO.
     * estadoFinal: C = cerrada por el usuario, E = expirada por inactividad.
     */
    @Transactional
    public void cerrarSesion(Long codigoSesion, String codigoUsuario, String ip, String estadoFinal, String detalle) {
        if (codigoSesion == null) {
            return;
        }
        sesionRepository.findById(codigoSesion)
                .filter(sesion -> "A".equals(sesion.getEstadoSesion()))
                .ifPresent(sesion -> {
                    sesion.setFechaFin(LocalDateTime.now());
                    sesion.setEstadoSesion(estadoFinal);
                    sesionRepository.save(sesion);
                    String operacion = "E".equals(estadoFinal) ? "SESION EXPIRADA" : "CERRAR SESION";
                    auditoria.registrar(codigoUsuario, operacion, "EXITOSO", detalle, ip);
                });
    }

    @Transactional(readOnly = true)
    public List<Sesion> ultimasSesiones(String codigoUsuario) {
        return sesionRepository.findTop5ByUsuarioCodigoUsuarioOrderByFechaInicioDesc(codigoUsuario);
    }

    private Resultado registrarIntentoFallido(Usuario usuario, String ip) {
        short intentos = (short) ((usuario.getIntentosFallidos() == null ? 0 : usuario.getIntentosFallidos()) + 1);
        usuario.setIntentosFallidos(intentos);

        if (intentos >= maxIntentos) {
            usuario.setEstado(estadoRepository.getReferenceById(ESTADO_BLOQUEADO));
            usuario.setFechaModificacion(LocalDateTime.now());
            usuarioRepository.save(usuario);
            auditoria.registrar(usuario.getCodigoUsuario(), "INICIAR SESION", "BLOQUEADO",
                    "Cuenta bloqueada tras " + intentos + " intentos fallidos", ip);
            return new Resultado(Tipo.BLOQUEADO, "Se superó el número de intentos permitidos. La cuenta quedó bloqueada "
                    + minutosBloqueo + " minutos. Puede recuperar su contraseña.", null);
        }

        usuarioRepository.save(usuario);
        auditoria.registrar(usuario.getCodigoUsuario(), "INICIAR SESION", "FALLIDO",
                "Contraseña incorrecta (intento " + intentos + " de " + maxIntentos + ")", ip);
        return new Resultado(Tipo.CREDENCIALES, MENSAJE_CREDENCIALES, null);
    }

    private long minutosRestantesBloqueo(Usuario usuario) {
        LocalDateTime desde = usuario.getFechaModificacion() != null ? usuario.getFechaModificacion() : usuario.getFechaCreacion();
        LocalDateTime hasta = desde.plusMinutes(minutosBloqueo);
        long segundos = Duration.between(LocalDateTime.now(), hasta).getSeconds();
        return segundos <= 0 ? 0 : (segundos + 59) / 60;
    }

    private static String capitalizar(String texto) {
        if (texto == null || texto.isBlank()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String parte : texto.trim().toLowerCase().split("\\s+")) {
            sb.append(Character.toUpperCase(parte.charAt(0))).append(parte.substring(1)).append(' ');
        }
        return sb.toString().trim();
    }

    private static String inicial(String texto) {
        return texto == null || texto.isBlank() ? "" : texto.trim().substring(0, 1).toUpperCase();
    }
}
