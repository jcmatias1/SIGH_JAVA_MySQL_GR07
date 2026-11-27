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
 * Entidad Auditoria - tabla XEAUD_AUDIT.
 * Evento de auditoría registrado por el sistema.
 */
@Entity
@Table(name = "XEAUD_AUDIT")
public class Auditoria implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "XEAUD_CODIGO", nullable = false)
    private Long codigoEvento;

    @Column(name = "XEAUD_FECHOR", nullable = false)
    private LocalDateTime fechaHora;

    @Column(name = "XEAUD_MODULO", length = 40, nullable = false)
    private String modulo;

    @Column(name = "XEAUD_OPERAC", length = 40, nullable = false)
    private String operacion;

    @Column(name = "XEAUD_RESULT", length = 15, nullable = false)
    private String resultado;

    @Column(name = "XEAUD_DETALL", length = 500)
    private String detalle;

    @Column(name = "XEAUD_DIRIP", length = 45)
    private String direccionIp;

    @ManyToOne
    @JoinColumn(name = "XEUSU_CODIGO")
    @JsonIgnoreProperties({"perfiles", "hibernateLazyInitializer", "handler"})
    private Usuario usuario;

    public Auditoria() {
    }

    public Long getCodigoEvento() {
        return codigoEvento;
    }

    public void setCodigoEvento(Long codigoEvento) {
        this.codigoEvento = codigoEvento;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getModulo() {
        return modulo;
    }

    public void setModulo(String modulo) {
        this.modulo = modulo;
    }

    public String getOperacion() {
        return operacion;
    }

    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getDireccionIp() {
        return direccionIp;
    }

    public void setDireccionIp(String direccionIp) {
        this.direccionIp = direccionIp;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
