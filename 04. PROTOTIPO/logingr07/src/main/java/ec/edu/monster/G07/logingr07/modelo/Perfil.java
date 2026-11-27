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
 * Entidad Perfil - tabla XEPER_PERFI.
 * Perfil o rol del sistema (Administrador, Médico, Enfermero...).
 */
@Entity
@Table(name = "XEPER_PERFI")
public class Perfil implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "XEPER_CODIGO", length = 3, nullable = false)
    private String codigoPerfil;

    @Column(name = "XEPER_DESCRI", length = 60, nullable = false)
    private String descripcionPerfil;

    @Column(name = "XEPER_OBSERV", length = 200)
    private String observaciones;

    @OneToMany(mappedBy = "perfil")
    @JsonIgnore
    private List<UsuPer> usuarios = new ArrayList<>();

    @OneToMany(mappedBy = "perfil")
    @JsonIgnore
    private List<OpcPer> opciones = new ArrayList<>();

    public Perfil() {
    }

    public String getCodigoPerfil() {
        return codigoPerfil;
    }

    public void setCodigoPerfil(String codigoPerfil) {
        this.codigoPerfil = codigoPerfil;
    }

    public String getDescripcionPerfil() {
        return descripcionPerfil;
    }

    public void setDescripcionPerfil(String descripcionPerfil) {
        this.descripcionPerfil = descripcionPerfil;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public List<UsuPer> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<UsuPer> usuarios) {
        this.usuarios = usuarios;
    }

    public List<OpcPer> getOpciones() {
        return opciones;
    }

    public void setOpciones(List<OpcPer> opciones) {
        this.opciones = opciones;
    }
}
