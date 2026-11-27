/*
 * SIGH - Hospital General Monster Salud
 * Talleres Login y CRUD MVC en Java - Grupo GR07
 * Subsistema de Personal
 */
package ec.edu.monster.G07.logingr07.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;

/** Estado civil del empleado (tabla PEESC_ESTCI). */
@Entity
@Table(name = "PEESC_ESTCI")
public class EstadoCivil implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "PEESC_CODIGO", length = 1, nullable = false)
    private String codigoEstadoCivil;

    @Column(name = "PEESC_DESCRI", length = 20, nullable = false)
    private String descripcion;

    public EstadoCivil() {
    }

    public String getCodigoEstadoCivil() {
        return codigoEstadoCivil;
    }

    public void setCodigoEstadoCivil(String codigoEstadoCivil) {
        this.codigoEstadoCivil = codigoEstadoCivil;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
