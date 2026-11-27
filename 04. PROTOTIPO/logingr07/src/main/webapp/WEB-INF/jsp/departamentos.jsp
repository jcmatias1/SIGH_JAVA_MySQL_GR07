<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%-- Administrar departamento (opción P001): crear, consultar, modificar y eliminar departamentos --%>
<c:set var="titulo" value="Administrar departamentos"/>
<c:set var="activo" value="P001"/>
<!DOCTYPE html>
<html lang="es">
<head>
    <%@ include file="fragmentos/head.jspf" %>
</head>
<body data-contexto="${pageContext.request.contextPath}">
<%@ include file="fragmentos/app-inicio.jspf" %>

<section class="encabezado-pagina">
    <div>
        <div class="antetitulo">Personal · P001</div>
        <h1>Administrar departamento</h1>
        <p>Registre los departamentos del hospital. Los cargos se crean dentro de un departamento; uno con cargos o empleados no se puede eliminar.</p>
    </div>
    <button class="btn btn-sigh" type="button" id="btnNuevo"><i class="bi bi-plus-circle mr-1"></i> Nuevo departamento</button>
</section>

<div class="panel">
    <div class="d-flex flex-wrap justify-content-between align-items-center mb-3" style="gap:12px">
        <div>
            <h2 class="mb-0">Listado</h2>
            <span class="text-suave" id="resumen" style="font-size:14px">Cargando...</span>
        </div>
        <div class="campo buscador" style="width:300px">
            <i class="bi bi-search"></i>
            <input class="form-control" id="buscar" placeholder="Buscar por código o descripción">
        </div>
    </div>
    <div class="table-responsive">
        <table class="table tabla-sigh">
            <thead>
            <tr>
                <th>Código departamento</th>
                <th>Descripción departamento</th>
                <th class="text-center">Cargos</th>
                <th class="text-center">Empleados</th>
                <th class="text-right">Acciones</th>
            </tr>
            </thead>
            <tbody id="cuerpo">
            <tr><td colspan="5" class="vacio">Cargando departamentos...</td></tr>
            </tbody>
        </table>
    </div>
</div>

<%@ include file="fragmentos/app-fin.jspf" %>
<script>
    const API = "/personal/departamentos/api";
    let departamentos = [];

    document.addEventListener("DOMContentLoaded", async function () {
        try {
            await cargar();
        } catch (e) {
            SIGH.error(e.message);
        }
    });
    document.getElementById("buscar").addEventListener("input", pintar);
    document.getElementById("btnNuevo").addEventListener("click", function () { formulario(null); });

    async function cargar() {
        departamentos = await SIGH.api(API);
        pintar();
    }

    function pintar() {
        const texto = document.getElementById("buscar").value.trim().toLowerCase();
        const lista = departamentos.filter(function (d) {
            return !texto || [d.codigo, d.descripcion].some(function (v) { return (v || "").toLowerCase().includes(texto); });
        });
        const cargos = departamentos.reduce(function (s, d) { return s + d.cargos; }, 0);
        document.getElementById("resumen").textContent = departamentos.length + " departamentos · " + cargos + " cargos";

        const cuerpo = document.getElementById("cuerpo");
        if (!lista.length) {
            cuerpo.innerHTML = '<tr><td colspan="5" class="vacio">No hay departamentos para mostrar.</td></tr>';
            return;
        }
        cuerpo.innerHTML = lista.map(function (d) {
            const dato = ' data-codigo="' + SIGH.esc(d.codigo) + '"';
            const acciones = '<button class="btn btn-suave btn-icono" title="Modificar" data-accion="editar"' + dato + '><i class="bi bi-pencil-square"></i></button>'
                + '<button class="btn btn-peligro-suave btn-icono" title="Eliminar" data-accion="eliminar"' + dato + '><i class="bi bi-trash"></i></button>';
            return '<tr>'
                + '<td><span class="etiqueta etiqueta-azul">' + SIGH.esc(d.codigo) + '</span></td>'
                + '<td><strong>' + SIGH.esc(d.descripcion) + '</strong></td>'
                + '<td class="text-center">' + SIGH.esc(d.cargos) + '</td>'
                + '<td class="text-center">' + SIGH.esc(d.empleados) + '</td>'
                + '<td><div class="acciones">' + acciones + '</div></td>'
                + '</tr>';
        }).join("");
    }

    document.getElementById("cuerpo").addEventListener("click", function (evento) {
        const boton = evento.target.closest("button[data-accion]");
        if (!boton) { return; }
        const codigo = boton.getAttribute("data-codigo");
        const d = departamentos.find(function (x) { return x.codigo === codigo; });
        if (!d) { return; }
        if (boton.getAttribute("data-accion") === "editar") {
            formulario(d);
        } else {
            eliminar(d);
        }
    });

    // CREATE (d == null) y UPDATE (solo la descripción)
    async function formulario(d) {
        const editar = !!d;
        const resultado = await Swal.fire({
            title: editar ? "Modificar departamento " + d.codigo : "Nuevo departamento",
            width: 520,
            showCancelButton: true,
            confirmButtonText: '<i class="bi bi-save"></i> Guardar',
            cancelButtonText: "Cancelar",
            confirmButtonColor: "#0b5cad",
            focusConfirm: false,
            html: '<div class="swal-form">'
                + '<label for="fCodigo">Código departamento (3 letras)</label><input id="fCodigo" class="form-control" maxlength="3" placeholder="Ej.: LAB"'
                + (editar ? ' readonly value="' + SIGH.esc(d.codigo) + '"' : '') + '>'
                + '<label for="fDescripcion">Descripción departamento</label><input id="fDescripcion" class="form-control" maxlength="60" placeholder="Ej.: Laboratorio"'
                + (editar ? ' value="' + SIGH.esc(d.descripcion) + '"' : '') + '>'
                + '</div>',
            didOpen: function () {
                const codigo = document.getElementById("fCodigo");
                codigo.addEventListener("input", function () {
                    codigo.value = codigo.value.toUpperCase().replace(/[^A-Z]/g, "").slice(0, 3);
                });
            },
            preConfirm: function () {
                const datos = {
                    codigoDepartamento: document.getElementById("fCodigo").value.trim().toUpperCase(),
                    descripcion: document.getElementById("fDescripcion").value.trim()
                };
                if (!/^[A-Z]{3}$/.test(datos.codigoDepartamento)) {
                    Swal.showValidationMessage("El código del departamento debe tener exactamente 3 letras (A-Z).");
                    return false;
                }
                if (datos.descripcion.length < 3) {
                    Swal.showValidationMessage("La descripción del departamento es obligatoria (mínimo 3 caracteres).");
                    return false;
                }
                const url = editar ? API + "/" + encodeURIComponent(d.codigo) : API;
                return SIGH.api(url, { method: editar ? "PUT" : "POST", body: datos }).catch(function (e) {
                    Swal.showValidationMessage(e.message);
                    return false;
                });
            }
        });
        if (resultado.isConfirmed && resultado.value) {
            await cargar();
            SIGH.exito(editar ? "Departamento actualizado" : "Departamento creado",
                editar ? "Se guardó la nueva descripción." : "El departamento " + resultado.value.codigo + " quedó registrado.");
        }
    }

    // DELETE con confirmación
    async function eliminar(d) {
        const confirmacion = await Swal.fire({
            icon: "warning",
            title: "¿Eliminar el departamento " + d.codigo + "?",
            text: d.descripcion + ". Esta acción no se puede deshacer.",
            showCancelButton: true,
            confirmButtonText: "Eliminar", cancelButtonText: "Cancelar",
            confirmButtonColor: "#b42318"
        });
        if (!confirmacion.isConfirmed) { return; }
        try {
            await SIGH.api(API + "/" + encodeURIComponent(d.codigo), { method: "DELETE" });
            await cargar();
            SIGH.exito("Departamento eliminado", "El departamento " + d.codigo + " fue eliminado.");
        } catch (e) {
            SIGH.error(e.message);
        }
    }
</script>
</body>
</html>
