/*
 * SIGH - Hospital General Monster Salud
 * Talleres Login y CRUD MVC en Java - Grupo GR07
 * Subsistema de Seguridad
 */
package ec.edu.monster.G07.logingr07.modelo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Entidad UsuPer - tabla XEUXP_USUPE.
 * Asignación de perfiles a usuarios (relación N:M).
 */
@Entity
@Table(name = "XEUXP_USUPE")
public class UsuPer implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private UsuPerId id;

    @ManyToOne
    @MapsId("codigoUsuario")
    @JoinColumn(name = "XEUSU_CODIGO")
    @JsonIgnore
    private Usuario usuario;

    @ManyToOne
    @MapsId("codigoPerfil")
    @JoinColumn(name = "XEPER_CODIGO")
    private Perfil perfil;

    @Column(name = "XEUXP_FECASI", nullable = false)
    private LocalDateTime fechaAsignacion;

    @Column(name = "XEUXP_FECRET")
    private LocalDateTime fechaRetiro;

    public UsuPer() {
    }

    public UsuPerId getId() {
        return id;
    }

    public void setId(UsuPerId id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public LocalDateTime getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(LocalDateTime fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    public LocalDateTime getFechaRetiro() {
        return fechaRetiro;
    }

    public void setFechaRetiro(LocalDateTime fechaRetiro) {
        this.fechaRetiro = fechaRetiro;
    }
}
