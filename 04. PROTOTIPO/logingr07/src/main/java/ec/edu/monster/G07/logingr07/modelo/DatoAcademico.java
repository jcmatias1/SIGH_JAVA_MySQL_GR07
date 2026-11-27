/*
 * SIGH - Hospital General Monster Salud
 * Talleres Login y CRUD MVC en Java - Grupo GR07
 * Subsistema de Personal
 */
package ec.edu.monster.G07.logingr07.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;

/** Título o estudio de un empleado (tabla PEACA_ACADE). Un empleado puede tener varios. */
@Entity
@Table(name = "PEACA_ACADE")
public class DatoAcademico implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PEACA_CODIGO")
    private Integer codigo;

    @Column(name = "PEEMP_CODIGO", length = 10, nullable = false)
    private String codigoEmpleado;

    @Column(name = "PENIV_CODIGO", length = 1, nullable = false)
    private String codigoNivel;

    @Column(name = "PEACA_INSTIT", length = 100, nullable = false)
    private String institucion;

    @Column(name = "PEACA_TITULO", length = 100, nullable = false)
    private String titulo;

    public Integer getCodigo() { return codigo; }
    public void setCodigo(Integer codigo) { this.codigo = codigo; }
    public String getCodigoEmpleado() { return codigoEmpleado; }
    public void setCodigoEmpleado(String codigoEmpleado) { this.codigoEmpleado = codigoEmpleado; }
    public String getCodigoNivel() { return codigoNivel; }
    public void setCodigoNivel(String codigoNivel) { this.codigoNivel = codigoNivel; }
    public String getInstitucion() { return institucion; }
    public void setInstitucion(String institucion) { this.institucion = institucion; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
}
