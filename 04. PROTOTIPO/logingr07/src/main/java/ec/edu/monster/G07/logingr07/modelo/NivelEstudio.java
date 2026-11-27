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

/** Nivel de estudio (tabla PENIV_NIVEL). */
@Entity
@Table(name = "PENIV_NIVEL")
public class NivelEstudio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "PENIV_CODIGO", length = 1, nullable = false)
    private String codigoNivel;

    @Column(name = "PENIV_DESCRI", length = 40, nullable = false)
    private String descripcion;

    public NivelEstudio() {
    }

    public String getCodigoNivel() {
        return codigoNivel;
    }

    public void setCodigoNivel(String codigoNivel) {
        this.codigoNivel = codigoNivel;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
