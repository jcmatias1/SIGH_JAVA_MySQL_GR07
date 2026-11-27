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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Sistema - tabla XESIS_SISTE.
 * Módulo o subsistema del SIGH al que pertenecen las opciones.
 */
@Entity
@Table(name = "XESIS_SISTE")
public class Sistema implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "XESIS_CODIGO", length = 3, nullable = false)
    private String codigoSistema;

    @Column(name = "XESIS_DESCRI", length = 60, nullable = false)
    private String descripcionSistema;

    @OneToMany(mappedBy = "sistema")
    @JsonIgnore
    private List<Opcion> opciones = new ArrayList<>();

    public Sistema() {
    }

    public String getCodigoSistema() {
        return codigoSistema;
    }

    public void setCodigoSistema(String codigoSistema) {
        this.codigoSistema = codigoSistema;
    }

    public String getDescripcionSistema() {
        return descripcionSistema;
    }

    public void setDescripcionSistema(String descripcionSistema) {
        this.descripcionSistema = descripcionSistema;
    }

    public List<Opcion> getOpciones() {
        return opciones;
    }

    public void setOpciones(List<Opcion> opciones) {
        this.opciones = opciones;
    }
}
