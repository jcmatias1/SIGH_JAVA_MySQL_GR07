/*
 * SIGH - Hospital General Monster Salud
 * Talleres Login y CRUD MVC en Java - Grupo GR07
 * Subsistema de Seguridad
 */
package ec.edu.monster.G07.logingr07.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Grupo del menú principal: un sistema (XESIS_SISTE) con sus opciones permitidas.
 */
public class MenuSistema implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String codigo;
    private final String descripcion;
    private final String icono;
    private final List<OpcionMenu> opciones = new ArrayList<>();

    public MenuSistema(String codigo, String descripcion, String icono) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.icono = icono;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getIcono() {
        return icono;
    }

    public List<OpcionMenu> getOpciones() {
        return opciones;
    }
}
