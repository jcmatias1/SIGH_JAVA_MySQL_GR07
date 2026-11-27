/*
 * SIGH - Hospital General Monster Salud
 * Talleres Login y CRUD MVC en Java - Grupo GR07
 * Subsistema de Seguridad
 */
package ec.edu.monster.G07.logingr07.dto;

import java.io.Serializable;

/**
 * Opción del menú principal habilitada para el usuario (XEOPC_OPCIO).
 */
public class OpcionMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String codigo;
    private final String descripcion;
    private final String url;
    private final String icono;

    public OpcionMenu(String codigo, String descripcion, String url, String icono) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.url = url;
        this.icono = icono;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getUrl() {
        return url;
    }

    public String getIcono() {
        return icono;
    }
}
