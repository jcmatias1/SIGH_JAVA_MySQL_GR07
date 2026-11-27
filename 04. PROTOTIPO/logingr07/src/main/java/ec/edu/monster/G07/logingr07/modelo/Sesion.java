/*
 * SIGH - Hospital General Monster Salud
 * Talleres Login y CRUD MVC en Java - Grupo GR07
 * Subsistema de Seguridad
 */
package ec.edu.monster.G07.logingr07.modelo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Entidad Sesion - tabla XESES_SESIO.
 * Sesión iniciada por un usuario.
 */
@Entity
@Table(name = "XESES_SESIO")
public class Sesion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "XESES_CODIGO", nullable = false)
    private Long codigoSesion;

    @Column(name = "XESES_FECINI", nullable = false)
    private LocalDateTime fechaInicio;

    @Column(name = "XESES_FECFIN")
    private LocalDateTime fechaFin;

    @Column(name = "XESES_DIRIP", length = 45)
    private String direccionIp;

    @Column(name = "XESES_ESTADO", length = 1, nullable = false)
    private String estadoSesion;

    @ManyToOne
    @JoinColumn(name = "XEUSU_CODIGO", nullable = false)
    @JsonIgnoreProperties({"perfiles", "hibernateLazyInitializer", "handler"})
    private Usuario usuario;

    public Sesion() {
    }

    public Long getCodigoSesion() {
        return codigoSesion;
    }

    public void setCodigoSesion(Long codigoSesion) {
        this.codigoSesion = codigoSesion;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getDireccionIp() {
        return direccionIp;
    }

    public void setDireccionIp(String direccionIp) {
        this.direccionIp = direccionIp;
    }

    public String getEstadoSesion() {
        return estadoSesion;
    }

    public void setEstadoSesion(String estadoSesion) {
        this.estadoSesion = estadoSesion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
