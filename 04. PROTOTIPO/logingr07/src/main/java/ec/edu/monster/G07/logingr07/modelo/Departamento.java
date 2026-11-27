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

/**
 * Departamento o servicio hospitalario (tabla PEDEP_DEPAR).
 */
@Entity
@Table(name = "PEDEP_DEPAR")
public class Departamento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "PEDEP_CODIGO", length = 3, nullable = false)
    private String codigoDepartamento;

    @Column(name = "PEDEP_DESCRI", length = 60, nullable = false)
    private String descripcion;

    public Departamento() {
    }

    public String getCodigoDepartamento() {
        return codigoDepartamento;
    }

    public void setCodigoDepartamento(String codigoDepartamento) {
        this.codigoDepartamento = codigoDepartamento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
