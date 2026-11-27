/*
 * SIGH - Hospital General Monster Salud
 * Talleres Login y CRUD MVC en Java - Grupo GR07
 * Subsistema de Seguridad
 */
package ec.edu.monster.G07.logingr07.modelo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Opcion - tabla XEOPC_OPCIO.
 * Opción (permiso o pantalla) de un sistema.
 */
@Entity
@Table(name = "XEOPC_OPCIO")
public class Opcion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "XEOPC_CODIGO", length = 4, nullable = false)
    private String codigoOpcion;

    @Column(name = "XEOPC_DESCRI", length = 80, nullable = false)
    private String descripcionOpcion;

    @Column(name = "XEOPC_URL", length = 150)
    private String urlOpcion;

    @ManyToOne
    @JoinColumn(name = "XESIS_CODIGO", nullable = false)
    private Sistema sistema;

    @OneToMany(mappedBy = "opcion")
    @JsonIgnore
    private List<OpcPer> perfiles = new ArrayList<>();

    public Opcion() {
    }

    public String getCodigoOpcion() {
        return codigoOpcion;
    }

    public void setCodigoOpcion(String codigoOpcion) {
        this.codigoOpcion = codigoOpcion;
    }

    public String getDescripcionOpcion() {
        return descripcionOpcion;
    }

    public void setDescripcionOpcion(String descripcionOpcion) {
        this.descripcionOpcion = descripcionOpcion;
    }

    public String getUrlOpcion() {
        return urlOpcion;
    }

    public void setUrlOpcion(String urlOpcion) {
        this.urlOpcion = urlOpcion;
    }

    public Sistema getSistema() {
        return sistema;
    }

    public void setSistema(Sistema sistema) {
        this.sistema = sistema;
    }

    public List<OpcPer> getPerfiles() {
        return perfiles;
    }

    public void setPerfiles(List<OpcPer> perfiles) {
        this.perfiles = perfiles;
    }
}
