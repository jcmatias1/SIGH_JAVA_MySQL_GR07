/*
 * SIGH - Hospital General Monster Salud
 * Talleres Login y CRUD MVC en Java - Grupo GR07
 * Subsistema de Seguridad
 */
package ec.edu.monster.G07.logingr07.servicio;

import ec.edu.monster.G07.logingr07.dto.MenuSistema;
import ec.edu.monster.G07.logingr07.dto.OpcionMenu;
import ec.edu.monster.G07.logingr07.modelo.Opcion;
import ec.edu.monster.G07.logingr07.repository.OpcionRepository;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Construye el menú del usuario a partir de sus perfiles y opciones (RF-SEG-003) y decide si
 * una URL está permitida. Los permisos se leen de la base en cada solicitud, por eso una
 * revocación se aplica en la siguiente petición del usuario.
 */
@Service
public class MenuServicio {

    private static final Map<String, String> ICONOS_SISTEMA = Map.of(
            "SEG", "bi-shield-lock",
            "PER", "bi-person-badge");

    private static final Map<String, String> ICONOS_OPCION = Map.ofEntries(
            Map.entry("S001", "bi-people"),
            Map.entry("S002", "bi-diagram-3"),
            Map.entry("S003", "bi-journal-text"),
            Map.entry("P001", "bi-building"),
            Map.entry("P002", "bi-briefcase"),
            Map.entry("P003", "bi-person-vcard"));

    private final OpcionRepository opcionRepository;

    public MenuServicio(OpcionRepository opcionRepository) {
        this.opcionRepository = opcionRepository;
    }

    /** Opciones permitidas agrupadas por sistema, en el orden del menú. */
    @Transactional(readOnly = true)
    public List<MenuSistema> menu(String codigoUsuario) {
        Map<String, MenuSistema> grupos = new LinkedHashMap<>();
        for (Opcion opcion : opcionRepository.findOpcionesPermitidas(codigoUsuario)) {
            String codigoSistema = opcion.getSistema().getCodigoSistema();
            MenuSistema grupo = grupos.computeIfAbsent(codigoSistema, c -> new MenuSistema(c,
                    opcion.getSistema().getDescripcionSistema(), ICONOS_SISTEMA.getOrDefault(c, "bi-grid")));
            grupo.getOpciones().add(new OpcionMenu(opcion.getCodigoOpcion(), opcion.getDescripcionOpcion(),
                    opcion.getUrlOpcion(), ICONOS_OPCION.getOrDefault(opcion.getCodigoOpcion(), "bi-box-arrow-in-right")));
        }
        return List.copyOf(grupos.values());
    }

    /**
     * Opción que protege una ruta: la de URL más larga que sea prefijo de la ruta pedida.
     * Ej.: /seguridad/usuarios/api/admin está protegida por S001 (/seguridad/usuarios).
     */
    @Transactional(readOnly = true)
    public Optional<Opcion> opcionRequerida(String ruta) {
        return opcionRepository.findAll().stream()
                .filter(o -> o.getUrlOpcion() != null && !o.getUrlOpcion().isBlank())
                .filter(o -> ruta.equals(o.getUrlOpcion()) || ruta.startsWith(o.getUrlOpcion() + "/"))
                .max(Comparator.comparingInt(o -> o.getUrlOpcion().length()));
    }

    @Transactional(readOnly = true)
    public boolean tieneOpcion(String codigoUsuario, String codigoOpcion) {
        return opcionRepository.findOpcionesPermitidas(codigoUsuario).stream()
                .anyMatch(o -> o.getCodigoOpcion().equals(codigoOpcion));
    }
}
