/*
 * SIGH - Hospital General Monster Salud
 * Talleres Login y CRUD MVC en Java - Grupo GR07
 * Subsistema de Seguridad
 */
package ec.edu.monster.G07.logingr07.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;

/**
 * Entidad Estado - tabla XEEST_ESTAD.
 * Estado del usuario (Activo, Inactivo, Bloqueado).
 */
@Entity
@Table(name = "XEEST_ESTAD")
public class Estado implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "XEEST_CODIGO", length = 1, nullable = false)
    private String codigoEstado;

    @Column(name = "XEEST_DESCRI", length = 30, nullable = false)
    private String descripcionEstado;

    public Estado() {
    }

    public String getCodigoEstado() {
        return codigoEstado;
    }

    public void setCodigoEstado(String codigoEstado) {
        this.codigoEstado = codigoEstado;
    }

    public String getDescripcionEstado() {
        return descripcionEstado;
    }

    public void setDescripcionEstado(String descripcionEstado) {
        this.descripcionEstado = descripcionEstado;
    }
}
