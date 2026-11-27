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

/** Género del empleado (tabla PEGEN_GENER). */
@Entity
@Table(name = "PEGEN_GENER")
public class Genero implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "PEGEN_CODIGO", length = 1, nullable = false)
    private String codigoGenero;

    @Column(name = "PEGEN_DESCRI", length = 20, nullable = false)
    private String descripcion;

    public Genero() {
    }

    public String getCodigoGenero() {
        return codigoGenero;
    }

    public void setCodigoGenero(String codigoGenero) {
        this.codigoGenero = codigoGenero;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
