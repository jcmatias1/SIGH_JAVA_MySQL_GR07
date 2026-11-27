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

/** Parentesco de un familiar (tabla PEPAR_PAREN). */
@Entity
@Table(name = "PEPAR_PAREN")
public class Parentesco implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "PEPAR_CODIGO", length = 1, nullable = false)
    private String codigoParentesco;

    @Column(name = "PEPAR_DESCRI", length = 20, nullable = false)
    private String descripcion;

    public Parentesco() {
    }

    public String getCodigoParentesco() {
        return codigoParentesco;
    }

    public void setCodigoParentesco(String codigoParentesco) {
        this.codigoParentesco = codigoParentesco;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
