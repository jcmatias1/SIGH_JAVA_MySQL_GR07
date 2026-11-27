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
import java.time.LocalDate;

/** Familiar de un empleado (tabla PEFAM_FAMIL). Un empleado puede registrar varios. */
@Entity
@Table(name = "PEFAM_FAMIL")
public class Familiar implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PEFAM_CODIGO")
    private Integer codigo;

    @Column(name = "PEEMP_CODIGO", length = 10, nullable = false)
    private String codigoEmpleado;

    @Column(name = "PEPAR_CODIGO", length = 1, nullable = false)
    private String codigoParentesco;

    @Column(name = "PEFAM_APELLI", length = 60, nullable = false)
    private String apellidos;

    @Column(name = "PEFAM_NOMBRE", length = 60, nullable = false)
    private String nombres;

    @Column(name = "PEFAM_FECNAC", nullable = false)
    private LocalDate fechaNacimiento;

    public Integer getCodigo() { return codigo; }
    public void setCodigo(Integer codigo) { this.codigo = codigo; }
    public String getCodigoEmpleado() { return codigoEmpleado; }
    public void setCodigoEmpleado(String codigoEmpleado) { this.codigoEmpleado = codigoEmpleado; }
    public String getCodigoParentesco() { return codigoParentesco; }
    public void setCodigoParentesco(String codigoParentesco) { this.codigoParentesco = codigoParentesco; }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
}
