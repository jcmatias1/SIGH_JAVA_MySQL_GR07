/*
 * SIGH - Hospital General Monster Salud
 * Talleres Login y CRUD MVC en Java - Grupo GR07
 * Subsistema de Seguridad
 */
package ec.edu.monster.G07.logingr07.dto;

/**
 * Datos que envía el formulario «Nuevo perfil» / «Modificar perfil».
 */
public record PerfilFormDTO(String codigoPerfil, String descripcion, String observaciones) {
}
