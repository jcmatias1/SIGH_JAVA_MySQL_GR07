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
 * Entidad OpcPer - tabla XEOXP_OPCPE.
 * Asignación de opciones (permisos) a perfiles (relación N:M).
 */
@Entity
@Table(name = "XEOXP_OPCPE")
public class OpcPer implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private OpcPerId id;

    @ManyToOne
    @MapsId("codigoPerfil")
    @JoinColumn(name = "XEPER_CODIGO")
    @JsonIgnore
    private Perfil perfil;

    @ManyToOne
    @MapsId("codigoOpcion")
    @JoinColumn(name = "XEOPC_CODIGO")
    private Opcion opcion;

    @Column(name = "XEOXP_FECASI", nullable = false)
    private LocalDateTime fechaAsignacion;

    @Column(name = "XEOXP_FECRET")
    private LocalDateTime fechaRetiro;

    public OpcPer() {
    }

    public OpcPerId getId() {
        return id;
    }

    public void setId(OpcPerId id) {
        this.id = id;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public Opcion getOpcion() {
        return opcion;
    }

    public void setOpcion(Opcion opcion) {
        this.opcion = opcion;
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
