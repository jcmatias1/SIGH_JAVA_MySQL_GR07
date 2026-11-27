/*
 * SIGH - Hospital General Monster Salud
 * Talleres Login y CRUD MVC en Java - Grupo GR07
 * Subsistema de Personal
 */
package ec.edu.monster.G07.logingr07.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/**
 * Clave compuesta de PECAR_CARGO: departamento + código del cargo.
 */
@Embeddable
public class CargoId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "PEDEP_CODIGO", length = 3)
    private String codigoDepartamento;

    @Column(name = "PECAR_CODIGO", length = 3)
    private String codigoCargo;

    public CargoId() {
    }

    public CargoId(String codigoDepartamento, String codigoCargo) {
        this.codigoDepartamento = codigoDepartamento;
        this.codigoCargo = codigoCargo;
    }

    public String getCodigoDepartamento() {
        return codigoDepartamento;
    }

    public void setCodigoDepartamento(String codigoDepartamento) {
        this.codigoDepartamento = codigoDepartamento;
    }

    public String getCodigoCargo() {
        return codigoCargo;
    }

    public void setCodigoCargo(String codigoCargo) {
        this.codigoCargo = codigoCargo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigoDepartamento, codigoCargo);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CargoId)) {
            return false;
        }
        CargoId other = (CargoId) obj;
        return Objects.equals(codigoDepartamento, other.codigoDepartamento)
                && Objects.equals(codigoCargo, other.codigoCargo);
    }
}
