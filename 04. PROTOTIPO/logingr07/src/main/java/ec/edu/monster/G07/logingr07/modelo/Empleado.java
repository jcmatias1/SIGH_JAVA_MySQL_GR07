/*
 * SIGH - Hospital General Monster Salud
 * Talleres Login y CRUD MVC en Java - Grupo GR07
 * Subsistema de Seguridad
 */
package ec.edu.monster.G07.logingr07.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entidad Empleado - tabla PEEMP_EMPLE.
 * Personal del hospital que puede tener una cuenta de usuario.
 */
@Entity
@Table(name = "PEEMP_EMPLE")
public class Empleado implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "PEEMP_CODIGO", length = 10, nullable = false)
    private String codigoEmpleado;

    @Column(name = "PEGEN_CODIGO", length = 1, nullable = false)
    private String codigoGenero;

    @Column(name = "PEESC_CODIGO", length = 1)
    private String codigoEstadoCivil;

    @Column(name = "PETSA_CODIGO", length = 3)
    private String codigoTipoSangre;

    @Column(name = "PEEMP_PESO", precision = 5, scale = 2)
    private BigDecimal peso;

    @Column(name = "PEEMP_ESTATU", precision = 3, scale = 2)
    private BigDecimal estatura;

    @Column(name = "PEDEP_CODIGO", length = 3, nullable = false)
    private String codigoDepartamento;

    @Column(name = "PECAR_CODIGO", length = 3, nullable = false)
    private String codigoCargo;

    @Column(name = "PEEMP_CEDULA", length = 13, nullable = false)
    private String cedula;

    @Column(name = "PEEMP_APELLI", length = 60, nullable = false)
    private String apellidos;

    @Column(name = "PEEMP_NOMBRE", length = 60, nullable = false)
    private String nombres;

    @Column(name = "PEEMP_FECNAC", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(name = "PEEMP_EMAIL", length = 100, nullable = false)
    private String email;

    @Column(name = "PEEMP_TELEFO", length = 15)
    private String telefono;

    @Column(name = "PEEMP_DIRECC", length = 150)
    private String direccion;

    @Column(name = "PEEMP_REGPRO", length = 30)
    private String registroProfesional;

    @Column(name = "PEEMP_ESTADO", length = 1, nullable = false)
    private String estadoEmpleado;

    public Empleado() {
    }

    public String getCodigoEmpleado() {
        return codigoEmpleado;
    }

    public void setCodigoEmpleado(String codigoEmpleado) {
        this.codigoEmpleado = codigoEmpleado;
    }

    public String getCodigoGenero() {
        return codigoGenero;
    }

    public void setCodigoGenero(String codigoGenero) {
        this.codigoGenero = codigoGenero;
    }

    public String getCodigoEstadoCivil() {
        return codigoEstadoCivil;
    }

    public void setCodigoEstadoCivil(String codigoEstadoCivil) {
        this.codigoEstadoCivil = codigoEstadoCivil;
    }

    public String getCodigoDepartamento() {
        return codigoDepartamento;
    }

    public void setCodigoDepartamento(String codigoDepartamento) {
        this.codigoDepartamento = codigoDepartamento;
    }

    public String getCodigoCargo() {
        return codigoCargo;
    }

    public void setCodigoCargo(String codigoCargo) {
        this.codigoCargo = codigoCargo;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getRegistroProfesional() {
        return registroProfesional;
    }

    public void setRegistroProfesional(String registroProfesional) {
        this.registroProfesional = registroProfesional;
    }

    public String getEstadoEmpleado() {
        return estadoEmpleado;
    }

    public void setEstadoEmpleado(String estadoEmpleado) {
        this.estadoEmpleado = estadoEmpleado;
    }

    public String getCodigoTipoSangre() {
        return codigoTipoSangre;
    }

    public void setCodigoTipoSangre(String codigoTipoSangre) {
        this.codigoTipoSangre = codigoTipoSangre;
    }

    public BigDecimal getPeso() {
        return peso;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }

    public BigDecimal getEstatura() {
        return estatura;
    }

    public void setEstatura(BigDecimal estatura) {
        this.estatura = estatura;
    }
}
