/*
 * SIGH - Hospital General Monster Salud
 * Talleres Login y CRUD MVC en Java - Grupo GR07
 * Subsistema de Seguridad
 */
package ec.edu.monster.G07.logingr07.modelo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Usuario - tabla XEUSU_USUAR.
 * Cuenta de acceso de un empleado al SIGH.
 */
@Entity
@Table(name = "XEUSU_USUAR")
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "XEUSU_CODIGO", length = 20, nullable = false)
    private String codigoUsuario;

    @Column(name = "XEUSU_PASSWO", length = 255, nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(name = "XEUSU_INTFAL", nullable = false)
    private Short intentosFallidos;

    @Column(name = "XEUSU_FECCRE", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "XEUSU_FECMOD")
    private LocalDateTime fechaModificacion;

    @Column(name = "XEUSU_ULTACC")
    private LocalDateTime ultimoAcceso;

    @Column(name = "XEUSU_PIEFIR", length = 100)
    private String pieFirma;

    @ManyToOne
    @JoinColumn(name = "PEEMP_CODIGO", nullable = false)
    private Empleado empleado;

    @ManyToOne
    @JoinColumn(name = "XEEST_CODIGO", nullable = false)
    private Estado estado;

    @OneToMany(mappedBy = "usuario")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<UsuPer> perfiles = new ArrayList<>();

    @OneToMany(mappedBy = "usuario")
    @JsonIgnore
    private List<Sesion> sesiones = new ArrayList<>();

    @OneToMany(mappedBy = "usuario")
    @JsonIgnore
    private List<Auditoria> auditorias = new ArrayList<>();

    public Usuario() {
    }

    public String getCodigoUsuario() {
        return codigoUsuario;
    }

    public void setCodigoUsuario(String codigoUsuario) {
        this.codigoUsuario = codigoUsuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Short getIntentosFallidos() {
        return intentosFallidos;
    }

    public void setIntentosFallidos(Short intentosFallidos) {
        this.intentosFallidos = intentosFallidos;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(LocalDateTime fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public LocalDateTime getUltimoAcceso() {
        return ultimoAcceso;
    }

    public void setUltimoAcceso(LocalDateTime ultimoAcceso) {
        this.ultimoAcceso = ultimoAcceso;
    }

    public String getPieFirma() {
        return pieFirma;
    }

    public void setPieFirma(String pieFirma) {
        this.pieFirma = pieFirma;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public List<UsuPer> getPerfiles() {
        return perfiles;
    }

    public void setPerfiles(List<UsuPer> perfiles) {
        this.perfiles = perfiles;
    }

    public List<Sesion> getSesiones() {
        return sesiones;
    }

    public void setSesiones(List<Sesion> sesiones) {
        this.sesiones = sesiones;
    }

    public List<Auditoria> getAuditorias() {
        return auditorias;
    }

    public void setAuditorias(List<Auditoria> auditorias) {
        this.auditorias = auditorias;
    }
}
