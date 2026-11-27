/*
 * SIGH - Hospital General Monster Salud
 * Talleres Login y CRUD MVC en Java - Grupo GR07
 * Subsistema de Seguridad
 */
package ec.edu.monster.G07.logingr07.servicio;

import ec.edu.monster.G07.logingr07.modelo.TokenRecuperacion;
import ec.edu.monster.G07.logingr07.modelo.Usuario;
import ec.edu.monster.G07.logingr07.repository.EstadoRepository;
import ec.edu.monster.G07.logingr07.repository.TokenRecuperacionRepository;
import ec.edu.monster.G07.logingr07.repository.UsuarioRepository;
import ec.edu.monster.G07.logingr07.utils.SeguridadUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Flujo RECUPERAR CONTRASEÑA de ECUD1 (RF-SEG-005): genera un enlace temporal de un solo uso,
 * lo envía al correo institucional del empleado y permite fijar una nueva contraseña que cumpla
 * la política. Nunca se revela la contraseña existente.
 */
@Service
public class RecuperacionPasswordServicio {

    private static final Logger LOG = LoggerFactory.getLogger(RecuperacionPasswordServicio.class);
    private static final int MAX_SOLICITUDES = 5;
    private static final int HORAS_VENTANA = 5;

    /** Resultado de la solicitud: si se permitió y el enlace (solo para el modo taller). */
    public record Solicitud(boolean permitida, String enlaceDemo) {
    }

    private final UsuarioRepository usuarioRepository;
    private final TokenRecuperacionRepository tokenRepository;
    private final EstadoRepository estadoRepository;
    private final AuditoriaServicio auditoria;
    private final JavaMailSender mailSender;

    @Value("${sigh.recuperacion.minutos-vigencia:30}")
    private int minutosVigencia;

    @Value("${sigh.recuperacion.mostrar-enlace:false}")
    private boolean mostrarEnlace;

    @Value("${spring.mail.username:}")
    private String correoEmisor;

    public RecuperacionPasswordServicio(UsuarioRepository usuarioRepository, TokenRecuperacionRepository tokenRepository,
            EstadoRepository estadoRepository, AuditoriaServicio auditoria, ObjectProvider<JavaMailSender> mailSender) {
        this.usuarioRepository = usuarioRepository;
        this.tokenRepository = tokenRepository;
        this.estadoRepository = estadoRepository;
        this.auditoria = auditoria;
        this.mailSender = mailSender.getIfAvailable();
    }

    /**
     * El usuario ingresa su usuario o su correo institucional. La respuesta en pantalla es la
     * misma exista o no la cuenta, para no revelar qué cuentas existen.
     */
    @Transactional
    public Solicitud solicitar(String identificador, String urlBase, String ip) {
        String valor = identificador == null ? "" : identificador.trim();
        if (valor.isEmpty()) {
            return new Solicitud(true, null);
        }
        Optional<Usuario> encontrado = valor.contains("@")
                ? usuarioRepository.findFirstByEmpleadoEmailIgnoreCase(valor)
                : usuarioRepository.findById(valor.toLowerCase());

        if (encontrado.isEmpty()) {
            auditoria.registrar(null, "RECUPERAR CONTRASEÑA", "FALLIDO", "Identificador no registrado: " + valor, ip);
            return new Solicitud(true, null);
        }
        Usuario usuario = encontrado.get();
        if (AutenticacionServicio.ESTADO_INACTIVO.equals(usuario.getEstado().getCodigoEstado())) {
            auditoria.registrar(usuario.getCodigoUsuario(), "RECUPERAR CONTRASEÑA", "DENEGADO", "Cuenta desactivada", ip);
            return new Solicitud(true, null);
        }

        long recientes = tokenRepository.countByUsuarioCodigoUsuarioAndFechaCreacionAfter(
                usuario.getCodigoUsuario(), LocalDateTime.now().minusHours(HORAS_VENTANA));
        if (recientes >= MAX_SOLICITUDES) {
            auditoria.registrar(usuario.getCodigoUsuario(), "RECUPERAR CONTRASEÑA", "DENEGADO",
                    "Se superó el límite de " + MAX_SOLICITUDES + " enlaces en " + HORAS_VENTANA + " horas", ip);
            return new Solicitud(false, null);
        }

        LocalDateTime ahora = LocalDateTime.now();
        TokenRecuperacion token = new TokenRecuperacion();
        token.setUsuario(usuario);
        token.setToken(SeguridadUtil.generarToken());
        token.setFechaCreacion(ahora);
        token.setFechaExpiracion(ahora.plusMinutes(minutosVigencia));
        token.setUsado(false);
        tokenRepository.save(token);

        String enlace = urlBase + "/restablecer-password?token=" + token.getToken();
        String destino = usuario.getEmpleado().getEmail();
        boolean enviado = enviarCorreo(destino, usuario.getCodigoUsuario(), enlace);
        auditoria.registrar(usuario.getCodigoUsuario(), "RECUPERAR CONTRASEÑA", "SOLICITADO",
                (enviado ? "Enlace enviado a " : "Enlace generado (correo no configurado) para ") + destino, ip);
        return new Solicitud(true, mostrarEnlace ? enlace : null);
    }

    @Transactional(readOnly = true)
    public boolean tokenValido(String token) {
        return buscarTokenValido(token).isPresent();
    }

    /** Devuelve null si se cambió la contraseña o el mensaje de error. */
    @Transactional
    public String restablecer(String token, String password, String confirmacion, String ip) {
        Optional<TokenRecuperacion> encontrado = buscarTokenValido(token);
        if (encontrado.isEmpty()) {
            return "El enlace no es válido o ya venció. Solicite uno nuevo.";
        }
        if (password == null || !password.equals(confirmacion)) {
            return "Las contraseñas no coinciden.";
        }
        String error = SeguridadUtil.validarPolitica(password);
        if (error != null) {
            return error;
        }
        TokenRecuperacion recuperacion = encontrado.get();
        Usuario usuario = recuperacion.getUsuario();
        if (SeguridadUtil.coincide(password, usuario.getPassword())) {
            return "La nueva contraseña debe ser diferente a la anterior.";
        }
        usuario.setPassword(SeguridadUtil.sha256(password));
        usuario.setFechaModificacion(LocalDateTime.now());
        usuario.setIntentosFallidos((short) 0);
        if (AutenticacionServicio.ESTADO_BLOQUEADO.equals(usuario.getEstado().getCodigoEstado())) {
            usuario.setEstado(estadoRepository.getReferenceById(AutenticacionServicio.ESTADO_ACTIVO));
        }
        recuperacion.setUsado(true);
        usuarioRepository.save(usuario);
        tokenRepository.save(recuperacion);
        auditoria.registrar(usuario.getCodigoUsuario(), "RESTABLECER CONTRASEÑA", "EXITOSO",
                "Contraseña cambiada con enlace de recuperación", ip);
        return null;
    }

    private Optional<TokenRecuperacion> buscarTokenValido(String token) {
        if (token == null || token.isBlank()) {
            return Optional.empty();
        }
        return tokenRepository.findByToken(token.trim())
                .filter(t -> !Boolean.TRUE.equals(t.getUsado()))
                .filter(t -> t.getFechaExpiracion().isAfter(LocalDateTime.now()))
                .filter(t -> !AutenticacionServicio.ESTADO_INACTIVO.equals(t.getUsuario().getEstado().getCodigoEstado()));
    }

    private boolean enviarCorreo(String destino, String codigoUsuario, String enlace) {
        if (mailSender == null || correoEmisor == null || correoEmisor.isBlank()) {
            LOG.info("[SIGH] Enlace de recuperación para {} ({}): {}", codigoUsuario, destino, enlace);
            return false;
        }
        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");
            helper.setFrom(correoEmisor);
            helper.setTo(destino);
            helper.setSubject("SIGH - Restablecer contraseña");
            helper.setText(correoHtml(codigoUsuario, enlace), true);
            mailSender.send(mensaje);
            return true;
        } catch (MailException | MessagingException ex) {
            LOG.warn("[SIGH] No se pudo enviar el correo. Enlace de recuperación para {}: {}", codigoUsuario, enlace);
            return false;
        }
    }

    private String correoHtml(String codigoUsuario, String enlace) {
        return """
                <div style="font-family:Segoe UI,Arial,sans-serif;background:#eef3f9;padding:28px">
                  <div style="max-width:560px;margin:auto;background:#fff;border-radius:10px;overflow:hidden;border:1px solid #d6e0ec">
                    <div style="background:#0b3d75;color:#fff;padding:20px 26px">
                      <div style="font-size:20px;font-weight:700">SIGH · Hospital General Monster Salud</div>
                      <div style="font-size:12px;opacity:.8">Sistema Integral de Gestión Hospitalaria</div>
                    </div>
                    <div style="padding:26px;color:#1f2d3d;line-height:1.6">
                      <h2 style="margin-top:0;color:#0b3d75">Restablecer contraseña</h2>
                      <p>Se solicitó restablecer la contraseña del usuario <b>%s</b>.</p>
                      <p>El enlace vence en <b>%d minutos</b> y solo puede usarse una vez.</p>
                      <p style="text-align:center;margin:26px 0">
                        <a href="%s" style="background:#0b5cad;color:#fff;padding:12px 22px;border-radius:6px;text-decoration:none;font-weight:600">Restablecer contraseña</a>
                      </p>
                      <p style="font-size:12px;color:#5b6b7c">Si usted no lo solicitó, ignore este correo. El SIGH nunca le pedirá su contraseña.</p>
                    </div>
                  </div>
                </div>
                """.formatted(codigoUsuario, minutosVigencia, enlace);
    }
}
