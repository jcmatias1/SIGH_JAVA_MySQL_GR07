/*
 * SIGH - Hospital General Monster Salud
 * Talleres Login y CRUD MVC en Java - Grupo GR07
 * Subsistema de Seguridad
 */
package ec.edu.monster.G07.logingr07.modelo;

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
 * Entidad TokenRecuperacion - tabla XEREC_RECUP.
 * Enlace temporal de un solo uso para restablecer la contraseña (RF-SEG-005).
 */
@Entity
@Table(name = "XEREC_RECUP")
public class TokenRecuperacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "XEREC_CODIGO", nullable = false)
    private Long codigoToken;

    @Column(name = "XEREC_TOKEN", length = 120, nullable = false, unique = true)
    private String token;

    @Column(name = "XEREC_FECCRE", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "XEREC_FECEXP", nullable = false)
    private LocalDateTime fechaExpiracion;

    @Column(name = "XEREC_USADO", nullable = false)
    private Boolean usado;

    @ManyToOne
    @JoinColumn(name = "XEUSU_CODIGO", nullable = false)
    private Usuario usuario;

    public TokenRecuperacion() {
    }

    public Long getCodigoToken() {
        return codigoToken;
    }

    public void setCodigoToken(Long codigoToken) {
        this.codigoToken = codigoToken;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(LocalDateTime fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public Boolean getUsado() {
        return usado;
    }

    public void setUsado(Boolean usado) {
        this.usado = usado;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
