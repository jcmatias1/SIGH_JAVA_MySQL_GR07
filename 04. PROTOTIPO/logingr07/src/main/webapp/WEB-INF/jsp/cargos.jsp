<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%-- Administrar cargo (opción P002): crear, consultar, modificar y eliminar cargos por departamento --%>
<c:set var="titulo" value="Administrar cargos"/>
<c:set var="activo" value="P002"/>
<!DOCTYPE html>
<html lang="es">
<head>
    <%@ include file="fragmentos/head.jspf" %>
</head>
<body data-contexto="${pageContext.request.contextPath}">
<%@ include file="fragmentos/app-inicio.jspf" %>

<section class="encabezado-pagina">
    <div>
        <div class="antetitulo">Personal · P002</div>
        <h1>Administrar cargo</h1>
        <p>Registre los cargos de cada departamento. Un cargo asignado a empleados no se puede eliminar.</p>
    </div>
    <button class="btn btn-sigh" type="button" id="btnNuevo"><i class="bi bi-plus-circle mr-1"></i> Nuevo cargo</button>
</section>

<div class="panel">
    <div class="d-flex flex-wrap justify-content-between align-items-center mb-3" style="gap:12px">
        <div>
            <h2 class="mb-0">Listado</h2>
            <span class="text-suave" id="resumen" style="font-size:14px">Cargando...</span>
        </div>
        <div class="d-flex flex-wrap" style="gap:10px">
            <select class="custom-select" id="filtroDepartamento" style="width:220px">
                <option value="">Todos los departamentos</option>
            </select>
            <div class="campo buscador" style="width:300px">
                <i class="bi bi-search"></i>
                <input class="form-control" id="buscar" placeholder="Buscar por código o descripción">
            </div>
        </div>
    </div>
    <div class="table-responsive">
        <table class="table tabla-sigh">
            <thead>
            <tr>
                <th>Código cargo</th>
                <th>Descripción cargo</th>
                <th>Departamento</th>
                <th class="text-center">Empleados</th>
                <th class="text-right">Acciones</th>
            </tr>
            </thead>
            <tbody id="cuerpo">
            <tr><td colspan="5" class="vacio">Cargando cargos...</td></tr>
            </tbody>
        </table>
    </div>
</div>

<%@ include file="fragmentos/app-fin.jspf" %>
<script>
    const API = "/personal/cargos/api";
    let cargos = [];
    let departamentos = [];

    document.addEventListener("DOMContentLoaded", async function () {
        try {
            departamentos = await SIGH.api(API + "/departamentos");
            document.getElementById("filtroDepartamento").insertAdjacentHTML("beforeend", departamentos.map(function (d) {
                return '<option value="' + SIGH.esc(d.codigo) + '">' + SIGH.esc(d.descripcion) + '</option>';
            }).join(""));
            await cargar();
        } catch (e) {
            SIGH.error(e.message);
        }
    });
    document.getElementById("buscar").addEventListener("input", pintar);
    document.getElementById("filtroDepartamento").addEventListener("change", pintar);
    document.getElementById("btnNuevo").addEventListener("click", function () { formulario(null); });

    async function cargar() {
        cargos = await SIGH.api(API);
        pintar();
    }

    function pintar() {
        const texto = document.getElementById("buscar").value.trim().toLowerCase();
        const dep = document.getElementById("filtroDepartamento").value;
        const lista = cargos.filter(function (c) {
            return (!dep || c.codigoDepartamento === dep) && (!texto || [c.codigoCargo, c.descripcion, c.departamento]
                .some(function (v) { return (v || "").toLowerCase().includes(texto); }));
        });
        document.getElementById("resumen").textContent = cargos.length + " cargos · " + departamentos.length + " departamentos";

        const cuerpo = document.getElementById("cuerpo");
        if (!lista.length) {
            cuerpo.innerHTML = '<tr><td colspan="5" class="vacio">No hay cargos para mostrar.</td></tr>';
            return;
        }
        cuerpo.innerHTML = lista.map(function (c) {
            const datos = ' data-dep="' + SIGH.esc(c.codigoDepartamento) + '" data-cargo="' + SIGH.esc(c.codigoCargo) + '"';
            const acciones = '<button class="btn btn-suave btn-icono" title="Modificar" data-accion="editar"' + datos + '><i class="bi bi-pencil-square"></i></button>'
                + '<button class="btn btn-peligro-suave btn-icono" title="Eliminar" data-accion="eliminar"' + datos + '><i class="bi bi-trash"></i></button>';
            return '<tr>'
                + '<td><span class="etiqueta etiqueta-azul">' + SIGH.esc(c.codigoCargo) + '</span></td>'
                + '<td><strong>' + SIGH.esc(c.descripcion) + '</strong></td>'
                + '<td>' + SIGH.esc(c.departamento) + '<small class="d-block text-suave">' + SIGH.esc(c.codigoDepartamento) + '</small></td>'
                + '<td class="text-center">' + SIGH.esc(c.empleados) + '</td>'
                + '<td><div class="acciones">' + acciones + '</div></td>'
                + '</tr>';
        }).join("");
    }

    function buscarCargo(dep, car) {
        return cargos.find(function (c) { return c.codigoDepartamento === dep && c.codigoCargo === car; });
    }

    document.getElementById("cuerpo").addEventListener("click", function (evento) {
        const boton = evento.target.closest("button[data-accion]");
        if (!boton) { return; }
        const c = buscarCargo(boton.getAttribute("data-dep"), boton.getAttribute("data-cargo"));
        if (!c) { return; }
        if (boton.getAttribute("data-accion") === "editar") {
            formulario(c);
        } else {
            eliminar(c);
        }
    });

    function textoDepartamento(d) {
        return d.codigo + " - " + d.descripcion;
    }

    // Lista con filtro: al escribir se muestran solo los departamentos cuyo código o nombre coinciden
    function opcionesDepartamento() {
        return departamentos.map(function (d) {
            return '<option value="' + SIGH.esc(textoDepartamento(d)) + '"></option>';
        }).join("");
    }

    /** Acepta «QUI - Quirófano», el código (QUI) o el nombre exacto (quirófano). */
    function departamentoElegido(texto) {
        const t = (texto || "").trim().toLowerCase();
        if (!t) { return null; }
        return departamentos.find(function (d) {
            return textoDepartamento(d).toLowerCase() === t || d.codigo.toLowerCase() === t
                || d.descripcion.toLowerCase() === t;
        }) || null;
    }

    // CREATE (c == null) y UPDATE (solo la descripción)
    async function formulario(c) {
        const editar = !!c;
        const resultado = await Swal.fire({
            title: editar ? "Modificar cargo " + c.codigoCargo : "Nuevo cargo",
            width: 560,
            showCancelButton: true,
            confirmButtonText: '<i class="bi bi-save"></i> Guardar',
            cancelButtonText: "Cancelar",
            confirmButtonColor: "#0b5cad",
            focusConfirm: false,
            html: '<div class="swal-form">'
                + '<label for="fCodigo">Código cargo (3 letras)</label><input id="fCodigo" class="form-control" maxlength="3" placeholder="Ej.: MED"'
                + (editar ? ' readonly value="' + SIGH.esc(c.codigoCargo) + '"' : '') + '>'
                + '<label for="fDescripcion">Descripción cargo</label><input id="fDescripcion" class="form-control" maxlength="60" placeholder="Ej.: Médico general"'
                + (editar ? ' value="' + SIGH.esc(c.descripcion) + '"' : '') + '>'
                + '<label for="fDepartamento">Departamento (escriba código o nombre para filtrar)</label>'
                + '<input id="fDepartamento" class="form-control" list="listaDepartamentos" autocomplete="off" placeholder="Ej.: QUI o Quirófano"'
                + (editar ? ' disabled value="' + SIGH.esc(c.codigoDepartamento + " - " + c.departamento) + '"' : '') + '>'
                + '<datalist id="listaDepartamentos">' + opcionesDepartamento() + '</datalist>'
                + '</div>',
            didOpen: function () {
                const codigo = document.getElementById("fCodigo");
                codigo.addEventListener("input", function () {
                    codigo.value = codigo.value.toUpperCase().replace(/[^A-Z]/g, "").slice(0, 3);
                });
            },
            preConfirm: function () {
                const datos = {
                    codigoCargo: document.getElementById("fCodigo").value.trim().toUpperCase(),
                    descripcion: document.getElementById("fDescripcion").value.trim(),
                    codigoDepartamento: (departamentoElegido(document.getElementById("fDepartamento").value) || { codigo: editar ? c.codigoDepartamento : "" }).codigo
                };
                if (!/^[A-Z]{3}$/.test(datos.codigoCargo)) {
                    Swal.showValidationMessage("El código del cargo debe tener exactamente 3 letras (A-Z).");
                    return false;
                }
                if (datos.descripcion.length < 3) {
                    Swal.showValidationMessage("La descripción del cargo es obligatoria (mínimo 3 caracteres).");
                    return false;
                }
                if (!datos.codigoDepartamento) {
                    Swal.showValidationMessage("Escriba o elija un departamento de la lista (código o nombre).");
                    return false;
                }
                const url = editar
                    ? API + "/" + encodeURIComponent(c.codigoDepartamento) + "/" + encodeURIComponent(c.codigoCargo)
                    : API;
                return SIGH.api(url, { method: editar ? "PUT" : "POST", body: datos }).catch(function (e) {
                    Swal.showValidationMessage(e.message);
                    return false;
                });
            }
        });
        if (resultado.isConfirmed && resultado.value) {
            await cargar();
            SIGH.exito(editar ? "Cargo actualizado" : "Cargo creado",
                editar ? "Se guardó la nueva descripción." : "El cargo " + resultado.value.codigoCargo + " quedó registrado.");
        }
    }

    // DELETE con confirmación
    async function eliminar(c) {
        const confirmacion = await Swal.fire({
            icon: "warning",
            title: "¿Eliminar el cargo " + c.codigoCargo + "?",
            text: c.descripcion + " (" + c.departamento + "). Esta acción no se puede deshacer.",
            showCancelButton: true,
            confirmButtonText: "Eliminar", cancelButtonText: "Cancelar",
            confirmButtonColor: "#b42318"
        });
        if (!confirmacion.isConfirmed) { return; }
        try {
            await SIGH.api(API + "/" + encodeURIComponent(c.codigoDepartamento) + "/" + encodeURIComponent(c.codigoCargo), { method: "DELETE" });
            await cargar();
            SIGH.exito("Cargo eliminado", "El cargo " + c.codigoCargo + " fue eliminado.");
        } catch (e) {
            SIGH.error(e.message);
        }
    }
</script>
</body>
</html>
