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
 * Clave primaria compuesta de la tabla UsuPer.
 */
@Embeddable
public class UsuPerId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "XEUSU_CODIGO", length = 20)
    private String codigoUsuario;

    @Column(name = "XEPER_CODIGO", length = 3)
    private String codigoPerfil;

    public UsuPerId() {
    }

    public UsuPerId(String codigoUsuario, String codigoPerfil) {
        this.codigoUsuario = codigoUsuario;
        this.codigoPerfil = codigoPerfil;
    }

    public String getCodigoUsuario() {
        return codigoUsuario;
    }

    public void setCodigoUsuario(String codigoUsuario) {
        this.codigoUsuario = codigoUsuario;
    }

    public String getCodigoPerfil() {
        return codigoPerfil;
    }

    public void setCodigoPerfil(String codigoPerfil) {
        this.codigoPerfil = codigoPerfil;
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigoUsuario, codigoPerfil);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof UsuPerId)) {
            return false;
        }
        UsuPerId other = (UsuPerId) obj;
        return Objects.equals(codigoUsuario, other.codigoUsuario)
                && Objects.equals(codigoPerfil, other.codigoPerfil);
    }
}
