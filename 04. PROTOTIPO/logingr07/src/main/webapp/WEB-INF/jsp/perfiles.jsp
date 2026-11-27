<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%-- ECUD2 / RF-SEG-003 (opción S002): definir qué opciones de cada sistema puede usar cada perfil --%>
<c:set var="titulo" value="Administrar Departamentos"/>
<c:set var="activo" value="S002"/>
<!DOCTYPE html>
<html lang="es">
<head>
    <%@ include file="fragmentos/head.jspf" %>
</head>
<body data-contexto="${pageContext.request.contextPath}">
<%@ include file="fragmentos/app-inicio.jspf" %>

<section class="encabezado-pagina">
    <div>
        <div class="antetitulo">Seguridad · S002</div>
        <h1>Administrador de Departamentos</h1>
        <p>Cree, modifique o elimine perfiles y active o retire sus opciones. Los cambios se aplican en la siguiente solicitud de sus usuarios.</p>
    </div>
    <button class="btn btn-sigh" type="button" id="btnNuevoPerfil"><i class="bi bi-plus-lg mr-1"></i> Nuevo perfil</button>
</section>

<div class="row">
    <div class="col-lg-4 mb-4">
        <div id="listaPerfiles"><div class="panel vacio">Cargando perfiles...</div></div>
    </div>
    <div class="col-lg-8 mb-4">
        <div class="panel">
            <div class="d-flex justify-content-between align-items-start" style="gap:12px">
                <div>
                    <h2 id="perfilTitulo">Seleccione un perfil</h2>
                    <p class="bajada" id="perfilDetalle">Las opciones se agrupan por sistema (XESIS_SISTE).</p>
                </div>
                <div class="acciones" id="accionesPerfil" style="display:none">
                    <button class="btn btn-suave btn-sm" type="button" id="btnEditarPerfil"><i class="bi bi-pencil-square mr-1"></i> Modificar</button>
                    <button class="btn btn-peligro-suave btn-sm" type="button" id="btnEliminarPerfil"><i class="bi bi-trash3 mr-1"></i> Eliminar</button>
                </div>
            </div>
            <div id="permisos"></div>
        </div>
    </div>
</div>

<%@ include file="fragmentos/app-fin.jspf" %>
<script>
    const API = "/seguridad/perfiles/api";
    let perfiles = [];
    let seleccionado = null;

    document.addEventListener("DOMContentLoaded", cargar);

    async function cargar() {
        try {
            perfiles = await SIGH.api(API);
            if ((!seleccionado || !perfiles.some(function (p) { return p.codigoPerfil === seleccionado; })) && perfiles.length) {
                seleccionado = perfiles[0].codigoPerfil;
            }
            pintarPerfiles();
            pintarPermisos();
        } catch (e) {
            SIGH.error(e.message);
        }
    }

    function pintarPerfiles() {
        document.getElementById("listaPerfiles").innerHTML = perfiles.map(function (p) {
            const asignadas = p.opciones.filter(function (o) { return o.asignada; }).length;
            return '<button type="button" class="perfil-tab' + (p.codigoPerfil === seleccionado ? " activo" : "") + '" data-perfil="' + SIGH.esc(p.codigoPerfil) + '">'
                + '<strong>' + SIGH.esc(p.codigoPerfil + " · " + p.descripcion) + '</strong>'
                + '<span>' + asignadas + ' opciones · ' + p.usuariosActivos + ' usuario(s) asignados</span>'
                + '</button>';
        }).join("");
    }

    document.getElementById("listaPerfiles").addEventListener("click", function (evento) {
        const boton = evento.target.closest("[data-perfil]");
        if (!boton) { return; }
        seleccionado = boton.getAttribute("data-perfil");
        pintarPerfiles();
        pintarPermisos();
    });

    function pintarPermisos() {
        const p = perfiles.find(function (x) { return x.codigoPerfil === seleccionado; });
        if (!p) { return; }
        document.getElementById("accionesPerfil").style.display = "flex";
        document.getElementById("btnEliminarPerfil").style.display = p.codigoPerfil === "ADM" ? "none" : "";
        document.getElementById("perfilTitulo").textContent = p.codigoPerfil + " · " + p.descripcion;
        document.getElementById("perfilDetalle").textContent = p.observaciones || "Sin observaciones.";
        let sistemaActual = null;
        let html = "";
        p.opciones.forEach(function (o) {
            if (o.codigoSistema !== sistemaActual) {
                sistemaActual = o.codigoSistema;
                html += '<div class="sistema-titulo mt-3"><i class="bi bi-grid"></i> ' + SIGH.esc(o.sistema) + '</div>';
            }
            const id = "op-" + o.codigoOpcion;
            html += '<div class="permiso">'
                + '<div><strong>' + SIGH.esc(o.descripcion) + '</strong><small>' + SIGH.esc(o.codigoOpcion + " · " + (o.url || "")) + '</small></div>'
                + '<label class="interruptor mb-0" title="' + (o.asignada ? "Retirar" : "Asignar") + '">'
                + '<input type="checkbox" id="' + id + '" data-opcion="' + SIGH.esc(o.codigoOpcion) + '"' + (o.asignada ? " checked" : "") + '>'
                + '<span></span></label>'
                + '</div>';
        });
        document.getElementById("permisos").innerHTML = html;
    }

    function formularioPerfil(p) {
        return '<div class="swal-form">'
            + '<label for="fCodigo">Código (3 letras)</label>'
            + '<input id="fCodigo" class="form-control" maxlength="3" placeholder="Ej.: LAB" value="' + SIGH.esc(p ? p.codigoPerfil : "") + '"' + (p ? " readonly" : "") + '>'
            + '<label for="fNombre">Nombre del perfil</label>'
            + '<input id="fNombre" class="form-control" maxlength="60" placeholder="Ej.: LABORATORISTA" value="' + SIGH.esc(p ? p.descripcion : "") + '">'
            + '<label for="fObs">Observaciones</label>'
            + '<textarea id="fObs" class="form-control" maxlength="200" rows="3" placeholder="Qué hace este perfil en el hospital">' + SIGH.esc(p && p.observaciones ? p.observaciones : "") + '</textarea>'
            + '</div>';
    }

    function datosFormulario() {
        return {
            codigoPerfil: document.getElementById("fCodigo").value.trim().toUpperCase(),
            descripcion: document.getElementById("fNombre").value.trim(),
            observaciones: document.getElementById("fObs").value.trim()
        };
    }

    // CREATE
    document.getElementById("btnNuevoPerfil").addEventListener("click", async function () {
        const r = await Swal.fire({
            title: "Nuevo perfil", width: 520, html: formularioPerfil(null), focusConfirm: false,
            showCancelButton: true, confirmButtonText: '<i class="bi bi-save"></i> Guardar', cancelButtonText: "Cancelar",
            confirmButtonColor: "#0b5cad",
            didOpen: function () {
                const c = document.getElementById("fCodigo");
                c.addEventListener("input", function () { c.value = c.value.toUpperCase().replace(/[^A-Z]/g, "").slice(0, 3); });
            },
            preConfirm: function () {
                const d = datosFormulario();
                if (d.codigoPerfil.length !== 3 || !d.descripcion) {
                    Swal.showValidationMessage("El código (3 letras) y el nombre son obligatorios.");
                    return false;
                }
                return SIGH.api(API, { method: "POST", body: d }).catch(function (e) { Swal.showValidationMessage(e.message); return false; });
            }
        });
        if (r.isConfirmed && r.value) {
            seleccionado = r.value.codigoPerfil;
            await cargar();
            SIGH.exito("Perfil creado", "Ahora active las opciones que tendrá el perfil " + r.value.codigoPerfil + ".");
        }
    });

    // UPDATE
    document.getElementById("btnEditarPerfil").addEventListener("click", async function () {
        const p = perfiles.find(function (x) { return x.codigoPerfil === seleccionado; });
        if (!p) { return; }
        const r = await Swal.fire({
            title: "Modificar perfil " + p.codigoPerfil, width: 520, html: formularioPerfil(p), focusConfirm: false,
            showCancelButton: true, confirmButtonText: '<i class="bi bi-save"></i> Guardar', cancelButtonText: "Cancelar",
            confirmButtonColor: "#0b5cad",
            preConfirm: function () {
                return SIGH.api(API + "/" + encodeURIComponent(p.codigoPerfil), { method: "PUT", body: datosFormulario() })
                    .catch(function (e) { Swal.showValidationMessage(e.message); return false; });
            }
        });
        if (r.isConfirmed && r.value) {
            await cargar();
            SIGH.exito("Perfil actualizado", "Los cambios se guardaron correctamente.");
        }
    });

    // DELETE
    document.getElementById("btnEliminarPerfil").addEventListener("click", async function () {
        const p = perfiles.find(function (x) { return x.codigoPerfil === seleccionado; });
        if (!p) { return; }
        const r = await Swal.fire({
            icon: "warning", title: "¿Eliminar el perfil " + p.codigoPerfil + "?",
            text: "Se eliminará «" + p.descripcion + "» y su configuración de opciones. Esta acción no se puede deshacer.",
            showCancelButton: true, confirmButtonText: "Eliminar", cancelButtonText: "Cancelar", confirmButtonColor: "#b42318"
        });
        if (!r.isConfirmed) { return; }
        try {
            await SIGH.api(API + "/" + encodeURIComponent(p.codigoPerfil), { method: "DELETE" });
            seleccionado = null;
            await cargar();
            SIGH.exito("Perfil eliminado", "El perfil " + p.codigoPerfil + " fue eliminado.");
        } catch (e) {
            SIGH.error(e.message);
        }
    });

    document.getElementById("permisos").addEventListener("change", async function (evento) {
        const casilla = evento.target.closest("input[data-opcion]");
        if (!casilla) { return; }
        const opcion = casilla.getAttribute("data-opcion");
        const asignar = casilla.checked;
        try {
            await SIGH.api(API + "/" + encodeURIComponent(seleccionado) + "/opciones/" + encodeURIComponent(opcion),
                { method: asignar ? "POST" : "DELETE" });
            Swal.fire({
                toast: true, position: "top-end", timer: 2000, showConfirmButton: false, icon: "success",
                title: (asignar ? "Opción " + opcion + " asignada a " : "Opción " + opcion + " retirada de ") + seleccionado
            });
            await cargar();
        } catch (e) {
            casilla.checked = !asignar;
            SIGH.error(e.message);
        }
    });
</script>
</body>
</html>
