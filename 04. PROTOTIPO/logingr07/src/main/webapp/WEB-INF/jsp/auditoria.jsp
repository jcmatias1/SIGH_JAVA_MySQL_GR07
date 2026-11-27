<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%-- RF-AUD-002 (opción S003): consulta de solo lectura de los eventos de seguridad --%>
<c:set var="titulo" value="Consultar auditoría"/>
<c:set var="activo" value="S003"/>
<!DOCTYPE html>
<html lang="es">
<head>
    <%@ include file="fragmentos/head.jspf" %>
</head>
<body data-contexto="${pageContext.request.contextPath}">
<%@ include file="fragmentos/app-inicio.jspf" %>

<section class="encabezado-pagina">
    <div>
        <div class="antetitulo">Seguridad · S003</div>
        <h1>Auditoría de seguridad</h1>
        <p>Inicios de sesión, intentos fallidos, bloqueos, cierres y cambios de cuentas (tabla XEAUD_AUDIT). Solo lectura.</p>
    </div>
    <button class="btn btn-suave" type="button" id="btnRecargar"><i class="bi bi-arrow-clockwise mr-1"></i> Actualizar</button>
</section>

<div class="panel">
    <div class="d-flex flex-wrap align-items-center mb-3" style="gap:12px">
        <div class="campo buscador w-100">
            <i class="bi bi-search"></i>
            <input class="form-control" id="buscar" placeholder="Buscar usuario, operación o detalle">
        </div>
        <select class="custom-select" id="resultado" style="max-width:200px">
            <option value="">Todos los resultados</option>
            <option>EXITOSO</option>
            <option>FALLIDO</option>
            <option>BLOQUEADO</option>
            <option>DENEGADO</option>
            <option>SOLICITADO</option>
        </select>
        <span class="text-suave ml-auto" id="resumen" style="font-size:14px"></span>
    </div>
    <div class="table-responsive">
        <table class="table tabla-sigh">
            <thead>
            <tr>
                <th>#</th>
                <th>Fecha y hora</th>
                <th>Usuario</th>
                <th>Operación</th>
                <th>Resultado</th>
                <th>Detalle</th>
                <th>IP</th>
            </tr>
            </thead>
            <tbody id="cuerpo"><tr><td colspan="7" class="vacio">Cargando eventos...</td></tr></tbody>
        </table>
    </div>
</div>

<%@ include file="fragmentos/app-fin.jspf" %>
<script>
    let eventos = [];
    const colores = { EXITOSO: "etiqueta-verde", FALLIDO: "etiqueta-ambar", BLOQUEADO: "etiqueta-roja", DENEGADO: "etiqueta-roja", SOLICITADO: "etiqueta-azul" };

    async function cargar() {
        try {
            eventos = await SIGH.api("/seguridad/auditoria/api");
            pintar();
        } catch (e) {
            SIGH.error(e.message);
        }
    }

    function pintar() {
        const texto = document.getElementById("buscar").value.trim().toLowerCase();
        const resultado = document.getElementById("resultado").value;
        const lista = eventos.filter(function (e) {
            return (!resultado || e.resultado === resultado)
                && (!texto || [e.usuario, e.operacion, e.detalle, e.direccionIp].some(function (v) { return (v || "").toLowerCase().includes(texto); }));
        });
        document.getElementById("resumen").textContent = lista.length + " de " + eventos.length + " eventos";
        document.getElementById("cuerpo").innerHTML = lista.length ? lista.map(function (e) {
            return '<tr>'
                + '<td class="text-suave">' + SIGH.esc(e.codigo) + '</td>'
                + '<td style="white-space:nowrap">' + SIGH.fecha(e.fechaHora) + '</td>'
                + '<td>' + (e.usuario ? '<strong>' + SIGH.esc(e.usuario) + '</strong>' : '<span class="text-suave">(desconocido)</span>') + '</td>'
                + '<td>' + SIGH.esc(e.operacion) + '</td>'
                + '<td><span class="etiqueta ' + (colores[e.resultado] || "etiqueta-gris") + '">' + SIGH.esc(e.resultado) + '</span></td>'
                + '<td>' + SIGH.esc(e.detalle) + '</td>'
                + '<td class="text-suave">' + SIGH.esc(e.direccionIp || "—") + '</td>'
                + '</tr>';
        }).join("") : '<tr><td colspan="7" class="vacio">No hay eventos con esos filtros.</td></tr>';
    }

    document.addEventListener("DOMContentLoaded", cargar);
    document.getElementById("buscar").addEventListener("input", pintar);
    document.getElementById("resultado").addEventListener("change", pintar);
    document.getElementById("btnRecargar").addEventListener("click", cargar);
</script>
</body>
</html>
