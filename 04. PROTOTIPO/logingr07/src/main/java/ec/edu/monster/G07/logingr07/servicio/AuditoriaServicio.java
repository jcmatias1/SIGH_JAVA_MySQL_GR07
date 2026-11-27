/*
 * SIGH - Hospital General Monster Salud
 * Talleres Login y CRUD MVC en Java - Grupo GR07
 * Subsistema de Seguridad
 */
package ec.edu.monster.G07.logingr07.servicio;

import ec.edu.monster.G07.logingr07.dto.AuditoriaDTO;
import ec.edu.monster.G07.logingr07.modelo.Auditoria;
import ec.edu.monster.G07.logingr07.repository.AuditoriaRepository;
import ec.edu.monster.G07.logingr07.repository.UsuarioRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Registro y consulta de la auditoría del subsistema de seguridad (RF-AUD-001, RF-AUD-004).
 * El evento se guarda dentro de la transacción de la operación que lo origina, de modo que la
 * operación y su registro de auditoría se confirman juntos.
 */
@Service
public class AuditoriaServicio {

    public static final String MODULO = "SEGURIDAD";

    private final AuditoriaRepository auditoriaRepository;
    private final UsuarioRepository usuarioRepository;

    public AuditoriaServicio(AuditoriaRepository auditoriaRepository, UsuarioRepository usuarioRepository) {
        this.auditoriaRepository = auditoriaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public void registrar(String codigoUsuario, String operacion, String resultado, String detalle, String ip) {
        registrar(MODULO, codigoUsuario, operacion, resultado, detalle, ip);
    }

    /** Igual que el anterior, pero indicando el módulo (p. ej. PERSONAL). */
    @Transactional
    public void registrar(String modulo, String codigoUsuario, String operacion, String resultado, String detalle,
            String ip) {
        Auditoria evento = new Auditoria();
        if (codigoUsuario != null && usuarioRepository.existsById(codigoUsuario)) {
            evento.setUsuario(usuarioRepository.getReferenceById(codigoUsuario));
        }
        evento.setFechaHora(LocalDateTime.now());
        evento.setModulo(recortar(modulo, 40));
        evento.setOperacion(recortar(operacion, 40));
        evento.setResultado(recortar(resultado, 15));
        evento.setDetalle(recortar(detalle, 500));
        evento.setDireccionIp(recortar(ip, 45));
        auditoriaRepository.save(evento);
    }

    /** Últimos 300 eventos, del más reciente al más antiguo (RF-AUD-002). */
    @Transactional(readOnly = true)
    public List<AuditoriaDTO> listarRecientes() {
        return auditoriaRepository.findTop300ByOrderByFechaHoraDesc().stream()
                .map(a -> new AuditoriaDTO(a.getCodigoEvento(), a.getFechaHora(),
                        a.getUsuario() == null ? null : a.getUsuario().getCodigoUsuario(),
                        a.getModulo(), a.getOperacion(), a.getResultado(), a.getDetalle(), a.getDireccionIp()))
                .toList();
    }

    private static String recortar(String texto, int maximo) {
        if (texto == null) {
            return null;
        }
        return texto.length() > maximo ? texto.substring(0, maximo) : texto;
    }
}
