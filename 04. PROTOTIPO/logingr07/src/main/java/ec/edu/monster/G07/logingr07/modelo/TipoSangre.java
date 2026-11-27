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

/** Tipo de sangre (tabla PETSA_TIPSA). */
@Entity
@Table(name = "PETSA_TIPSA")
public class TipoSangre implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "PETSA_CODIGO", length = 3, nullable = false)
    private String codigoTipoSangre;

    @Column(name = "PETSA_DESCRI", length = 10, nullable = false)
    private String descripcion;

    public TipoSangre() {
    }

    public String getCodigoTipoSangre() {
        return codigoTipoSangre;
    }

    public void setCodigoTipoSangre(String codigoTipoSangre) {
        this.codigoTipoSangre = codigoTipoSangre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
