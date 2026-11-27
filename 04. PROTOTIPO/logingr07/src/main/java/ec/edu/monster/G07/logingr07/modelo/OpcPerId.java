/*
 * SIGH - Hospital General Monster Salud
 * Talleres Login y CRUD MVC en Java - Grupo GR07
 * Subsistema de Seguridad
 */
package ec.edu.monster.G07.logingr07.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/**
 * Clave primaria compuesta de la tabla OpcPer.
 */
@Embeddable
public class OpcPerId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "XEPER_CODIGO", length = 3)
    private String codigoPerfil;

    @Column(name = "XEOPC_CODIGO", length = 4)
    private String codigoOpcion;

    public OpcPerId() {
    }

    public OpcPerId(String codigoPerfil, String codigoOpcion) {
        this.codigoPerfil = codigoPerfil;
        this.codigoOpcion = codigoOpcion;
    }

    public String getCodigoPerfil() {
        return codigoPerfil;
    }

    public void setCodigoPerfil(String codigoPerfil) {
        this.codigoPerfil = codigoPerfil;
    }

    public String getCodigoOpcion() {
        return codigoOpcion;
    }

    public void setCodigoOpcion(String codigoOpcion) {
        this.codigoOpcion = codigoOpcion;
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigoPerfil, codigoOpcion);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof OpcPerId)) {
            return false;
        }
        OpcPerId other = (OpcPerId) obj;
        return Objects.equals(codigoPerfil, other.codigoPerfil)
                && Objects.equals(codigoOpcion, other.codigoOpcion);
    }
}
