/*
 * SIGH - Hospital General Monster Salud
 * Talleres Login y CRUD MVC en Java - Grupo GR07
 * Subsistema de Personal
 */
package ec.edu.monster.G07.logingr07.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import java.io.Serializable;

/**
 * Cargo del personal dentro de un departamento (tabla PECAR_CARGO).
 */
@Entity
@Table(name = "PECAR_CARGO")
public class Cargo implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private CargoId id;

    @ManyToOne
    @MapsId("codigoDepartamento")
    @JoinColumn(name = "PEDEP_CODIGO")
    private Departamento departamento;

    @Column(name = "PECAR_DESCRI", length = 60, nullable = false)
    private String descripcion;

    public Cargo() {
    }

    public CargoId getId() {
        return id;
    }

    public void setId(CargoId id) {
        this.id = id;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
