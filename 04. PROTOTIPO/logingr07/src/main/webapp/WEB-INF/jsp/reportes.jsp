<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%-- Módulo REPORTES (opción R001): pestañas por reporte con filtros, búsqueda, vista previa y JasperReports --%>
<c:set var="titulo" value="Reportes de personal"/>
<c:set var="activo" value="R001"/>
<!DOCTYPE html>
<html lang="es">
<head>
    <%@ include file="fragmentos/head.jspf" %>
    <style>
        .barra-reportes { align-items: flex-end; border-bottom: 1px solid #c9cfd8; display: flex; flex-wrap: wrap; gap: 10px; justify-content: space-between; }
        .pestanas-rep { display: flex; flex-wrap: wrap; gap: 4px; }
        .pestanas-rep .tab { align-items: center; background: transparent; border: 1px solid transparent; border-bottom: 0;
            border-radius: 8px 8px 0 0; color: #4b5563; display: inline-flex; font-weight: 600; gap: 8px; margin-bottom: -1px; padding: 9px 14px; }
        .pestanas-rep .tab.activa { background: #fff; border-color: #c9cfd8; color: var(--azul-800); }
        .pestanas-rep .tab .cerrar { border-radius: 4px; color: #6b7280; font-size: 15px; line-height: 1; padding: 1px 3px; }
        .pestanas-rep .tab .cerrar:hover { background: #e5e7eb; color: #111827; }
        .pestanas-rep .nueva { background: transparent; border: 0; color: #6b7280; font-size: 18px; padding: 6px 10px; }
        .buscar-reporte { align-items: center; display: flex; gap: 8px; margin-bottom: 8px; }
        .buscar-reporte label { color: #374151; font-size: 13px; font-weight: 600; margin: 0; white-space: nowrap; }
        .buscar-reporte select { min-width: 230px; }
        .hoja { background: #fff; border: 1px solid #c9cfd8; border-top: 0; border-radius: 0 0 10px 10px; display: none; padding: 18px; }
        .hoja.activa { display: block; }
        .rejilla-reportes { display: grid; gap: 16px; grid-template-columns: repeat(auto-fill, minmax(250px, 1fr)); }
        .tarjeta-rep { background: #f8fbff; border: 1px solid var(--linea); border-radius: 10px; cursor: pointer; padding: 18px; text-align: left; transition: .15s; }
        .tarjeta-rep:hover { border-color: var(--azul-500); box-shadow: var(--sombra); transform: translateY(-2px); }
        .tarjeta-rep .icono { align-items: center; background: var(--azul-100); border-radius: 10px; color: var(--azul-700);
            display: inline-flex; font-size: 22px; height: 44px; justify-content: center; margin-bottom: 10px; width: 44px; }
        .tarjeta-rep h2 { color: var(--azul-900); font-size: 16px; font-weight: 700; margin: 0 0 4px; }
        .tarjeta-rep p { color: #4b5563; font-size: 13px; margin: 0; }
        .area { display: grid; gap: 16px; grid-template-columns: 300px minmax(0, 1fr); }
        .caja { border: 1px solid #c9cfd8; border-radius: 10px; padding: 14px; }
        .caja h3 { color: var(--azul-900); font-size: 16px; font-weight: 700; margin: 0 0 10px; }
        .lista-filtros { border: 1px solid #e3e8ef; border-radius: 8px; max-height: 330px; overflow: auto; padding: 4px 10px; }
        .filtro { border-bottom: 1px solid #eef2f7; padding: 7px 0; }
        .filtro:last-child { border-bottom: 0; }
        .filtro > label { align-items: center; cursor: pointer; display: flex; font-size: 14px; gap: 8px; margin: 0; }
        .filtro > label i { color: #9ca3af; font-size: 12px; margin-left: auto; transition: transform .15s; }
        .filtro.abierto > label i { transform: rotate(180deg); }
        .filtro .valor { display: none; margin-top: 6px; }
        .filtro.abierto .valor { display: block; }
        .filtro .rango { display: grid; gap: 6px; grid-template-columns: 1fr 1fr; }
        .filtro .rango small { color: #6b7280; display: block; }
        .sin-filtros { color: #6b7280; font-size: 13px; padding: 8px 0; }
        .buscador { display: flex; gap: 6px; margin-top: 12px; }
        .acciones-exp { display: grid; gap: 6px; grid-template-columns: repeat(3, 1fr); margin-top: 12px; }
        .vista h3 { margin-bottom: 12px; }
        .tabla-vista { max-height: 420px; overflow: auto; }
        .tabla-vista table { font-size: 13px; }
        .tabla-vista td, .tabla-vista th { padding: 7px 10px; white-space: nowrap; }
        .tabla-vista tbody tr.elegible { cursor: pointer; }
        .tabla-vista tbody tr.seleccionada td { background: var(--azul-100); }
        .pie-vista { align-items: center; display: flex; flex-wrap: wrap; gap: 10px; justify-content: space-between; margin-top: 12px; }
        .pie-vista .info { color: #4b5563; flex: 1 1 320px; font-size: 13px; }
        .chip-estado { border-radius: 999px; font-size: 12px; font-weight: 600; padding: 2px 9px; }
        .chip-estado.A { background: #dcfce7; color: #166534; }
        .chip-estado.I { background: #fee2e2; color: #991b1b; }
        .elegido { background: var(--azul-100); border-radius: 8px; color: var(--azul-900); font-size: 13px; margin-top: 10px; padding: 8px 10px; }
        @media (max-width: 991px) { .area { grid-template-columns: 1fr; } }
    </style>
</head>
<body data-contexto="${pageContext.request.contextPath}">
<%@ include file="fragmentos/app-inicio.jspf" %>

<section class="encabezado-pagina">
    <div>
        <div class="antetitulo">Reportes · R001</div>
        <h1>Reportes de personal</h1>
        <p>Abra un reporte, marque los filtros que necesite y genere la vista previa. Con JasperReports se exporta a PDF, a Excel o se imprime directamente.</p>
    </div>
</section>

<div class="barra-reportes">
    <div class="pestanas-rep" id="pestanas" role="tablist">
        <button class="tab activa" type="button" data-hoja="inicio"><i class="bi bi-file-earmark-text"></i> Reportes</button>
        <button class="nueva" type="button" id="btnNueva" title="Abrir otro reporte"><i class="bi bi-plus-lg"></i></button>
    </div>
    <div class="buscar-reporte">
        <label for="cmbReporte">Buscar reporte:</label>
        <select class="custom-select custom-select-sm" id="cmbReporte">
            <option value="">Seleccionar reporte...</option>
        </select>
    </div>
</div>

<div id="hojas">
    <div class="hoja activa" data-hoja="inicio">
        <div class="rejilla-reportes" id="tarjetas"></div>
    </div>
</div>

<iframe id="marcoImpresion" title="Impresión" style="position:fixed;right:0;bottom:0;width:0;height:0;border:0"></iframe>

<%@ include file="fragmentos/app-fin.jspf" %>
<script>
(function () {
    "use strict";
    const esc = SIGH.esc;

    /** Filtros disponibles: combo (se llena con /reportes/api/catalogos) o rango de fechas. */
    const FILTROS = {
        departamento: { etiqueta: "Departamento", tipo: "combo" },
        cargo: { etiqueta: "Cargo", tipo: "combo" },
        estado: { etiqueta: "Estado", tipo: "combo" },
        genero: { etiqueta: "Género", tipo: "combo" },
        estadoCivil: { etiqueta: "Estado civil", tipo: "combo" },
        tipoSangre: { etiqueta: "Tipo de sangre", tipo: "combo" },
        fechaNacimiento: { etiqueta: "Fecha de nacimiento", tipo: "rango", desde: "fechaDesde", hasta: "fechaHasta" }
    };

    const REPORTES = {
        departamentos: { titulo: "Departamentos", icono: "bi-building", api: "departamentos",
            texto: "Departamentos con su número de cargos y de empleados activos, con gráfico de barras.", filtros: [] },
        cargos: { titulo: "Cargos por departamento", icono: "bi-briefcase", api: "cargos",
            texto: "Cargos agrupados por departamento, con subtotales y total general.", filtros: ["departamento"] },
        empleados: { titulo: "Listado de empleados", icono: "bi-people", api: "empleados",
            texto: "Empleados con cédula, departamento, cargo, contacto y estado, con gráfico de pastel.",
            filtros: ["departamento", "cargo", "estado", "genero", "estadoCivil", "tipoSangre", "fechaNacimiento"] },
        ficha: { titulo: "Ficha del empleado", icono: "bi-person-vcard", api: "empleados", ficha: true,
            texto: "Datos personales, profesionales y de contacto de un empleado, con títulos y familiares.",
            filtros: ["departamento", "cargo", "estado"] }
    };

    let catalogos = null;
    const estado = {}; // por reporte: { pagina, seleccionado }

    // ---------- Pestaña inicial y combo "Buscar reporte" ----------
    const tarjetas = document.getElementById("tarjetas");
    const combo = document.getElementById("cmbReporte");
    Object.keys(REPORTES).forEach(function (clave) {
        const r = REPORTES[clave];
        tarjetas.insertAdjacentHTML("beforeend",
            '<button type="button" class="tarjeta-rep" data-abrir="' + clave + '"><span class="icono"><i class="bi ' + r.icono +
            '"></i></span><h2>' + esc(r.titulo) + '</h2><p>' + esc(r.texto) + '</p></button>');
        combo.insertAdjacentHTML("beforeend", '<option value="' + clave + '">' + esc(r.titulo) + '</option>');
    });
    tarjetas.addEventListener("click", function (e) {
        const t = e.target.closest("[data-abrir]");
        if (t) { abrir(t.dataset.abrir); }
    });
    combo.addEventListener("change", function () {
        if (combo.value) { abrir(combo.value); }
    });
    document.getElementById("btnNueva").addEventListener("click", function () { activar("inicio"); combo.focus(); });
    document.getElementById("pestanas").addEventListener("click", function (e) {
        const cerrar = e.target.closest(".cerrar");
        const tab = e.target.closest(".tab");
        if (cerrar) { e.stopPropagation(); quitar(cerrar.dataset.cerrar); return; }
        if (tab) { activar(tab.dataset.hoja); }
    });

    function activar(clave) {
        document.querySelectorAll("#pestanas .tab").forEach(function (t) { t.classList.toggle("activa", t.dataset.hoja === clave); });
        document.querySelectorAll("#hojas .hoja").forEach(function (h) { h.classList.toggle("activa", h.dataset.hoja === clave); });
        combo.value = clave === "inicio" ? "" : clave;
    }

    function quitar(clave) {
        const tab = document.querySelector('#pestanas .tab[data-hoja="' + clave + '"]');
        const hoja = document.querySelector('#hojas .hoja[data-hoja="' + clave + '"]');
        const activa = tab.classList.contains("activa");
        tab.remove(); hoja.remove(); delete estado[clave];
        if (activa) { activar("inicio"); }
    }

    // ---------- Pestaña de un reporte ----------
    async function abrir(clave) {
        if (!document.querySelector('#hojas .hoja[data-hoja="' + clave + '"]')) {
            if (!catalogos) {
                try { catalogos = await SIGH.api("/reportes/api/catalogos"); }
                catch (err) { SIGH.error(err.message); return; }
            }
            crearHoja(clave);
        }
        activar(clave);
    }

    function crearHoja(clave) {
        const r = REPORTES[clave];
        estado[clave] = { pagina: 1, seleccionado: null };
        document.getElementById("btnNueva").insertAdjacentHTML("beforebegin",
            '<button class="tab" type="button" data-hoja="' + clave + '"><i class="bi ' + r.icono + '"></i> ' + esc(r.titulo) +
            '<span class="cerrar" data-cerrar="' + clave + '" title="Cerrar">&times;</span></button>');
        const filtrosHtml = r.filtros.length ? r.filtros.map(function (f) { return filtroHtml(clave, f); }).join("")
            : '<div class="sin-filtros">Este reporte no tiene filtros por campo; use la búsqueda.</div>';
        document.getElementById("hojas").insertAdjacentHTML("beforeend",
            '<div class="hoja" data-hoja="' + clave + '"><div class="area">' +
            '<div class="caja"><h3>Filtros: ' + esc(r.titulo) + '</h3>' +
            '<div class="lista-filtros">' + filtrosHtml + '</div>' +
            '<div class="buscador"><input class="form-control form-control-sm" type="search" maxlength="60" placeholder="Buscar en todos los campos" data-buscar>' +
            '<button class="btn btn-suave btn-sm" type="button" data-accion="buscar"><i class="bi bi-search"></i> Buscar</button></div>' +
            '<button class="btn btn-sigh btn-block mt-3" type="button" data-accion="generar"><i class="bi bi-file-earmark-bar-graph mr-1"></i> Generar reporte</button>' +
            (r.ficha ? '<div class="elegido" data-elegido>Seleccione un empleado en la vista previa.</div>' : '') +
            '<div class="acciones-exp">' +
            '<button class="btn btn-sigh btn-sm" type="button" data-accion="pdf"><i class="bi bi-file-earmark-pdf"></i> PDF</button>' +
            '<button class="btn btn-suave btn-sm" type="button" data-accion="excel"><i class="bi bi-file-earmark-excel"></i> Excel</button>' +
            '<button class="btn btn-suave btn-sm" type="button" data-accion="imprimir"><i class="bi bi-printer"></i> Imprimir</button>' +
            '</div></div>' +
            '<div class="caja vista"><h3>Vista previa: ' + esc(r.titulo) + '</h3>' +
            '<div class="tabla-vista"><table class="table tabla-sigh"><thead></thead><tbody></tbody></table></div>' +
            '<div class="pie-vista"><span class="info" data-info></span><div class="d-flex" style="gap:6px">' +
            '<button class="btn btn-suave btn-sm" type="button" data-accion="anterior" title="Página anterior"><i class="bi bi-chevron-left"></i></button>' +
            '<button class="btn btn-suave btn-sm" type="button" data-accion="siguiente" title="Página siguiente"><i class="bi bi-chevron-right"></i></button>' +
            '<button class="btn btn-outline-secondary btn-sm" type="button" data-accion="limpiar"><i class="bi bi-eraser"></i> Limpiar filtros</button>' +
            '</div></div></div></div></div>');
        const hoja = hojaDe(clave);
        hoja.addEventListener("change", function (e) {
            if (e.target.matches("[data-check]")) {
                e.target.closest(".filtro").classList.toggle("abierto", e.target.checked);
            }
            if (e.target.matches('[data-valor="departamento"]')) { llenarCargos(hoja); }
        });
        hoja.addEventListener("click", function (e) {
            const b = e.target.closest("[data-accion]");
            if (b) { accion(clave, b.dataset.accion); return; }
            const fila = e.target.closest("tr.elegible");
            if (fila) { elegir(clave, fila); }
        });
        hoja.querySelector("[data-buscar]").addEventListener("keydown", function (e) {
            if (e.key === "Enter") { e.preventDefault(); accion(clave, "buscar"); }
        });
        llenarCargos(hoja);
        generar(clave);
    }

    function filtroHtml(clave, f) {
        const def = FILTROS[f];
        const id = clave + "-" + f;
        let control;
        if (def.tipo === "rango") {
            control = '<div class="rango"><div><small>Desde</small><input class="form-control form-control-sm" type="date" data-valor="' + def.desde +
                '"></div><div><small>Hasta</small><input class="form-control form-control-sm" type="date" data-valor="' + def.hasta + '"></div></div>';
        } else {
            const opciones = (catalogos[f] || []).map(function (o) {
                return '<option value="' + esc(o.codigo) + '">' + esc(o.descripcion) + '</option>';
            }).join("");
            control = '<select class="custom-select custom-select-sm" data-valor="' + f + '">' + opciones + '</select>';
        }
        return '<div class="filtro" data-filtro="' + f + '"><label for="' + id + '"><input type="checkbox" id="' + id + '" data-check> ' +
            esc(def.etiqueta) + ' <i class="bi bi-chevron-down"></i></label><div class="valor">' + control + '</div></div>';
    }

    /** El combo de cargos muestra solo los del departamento elegido cuando ese filtro está marcado. */
    function llenarCargos(hoja) {
        const cargo = hoja.querySelector('[data-valor="cargo"]');
        if (!cargo) { return; }
        const dep = hoja.querySelector('[data-filtro="departamento"]');
        const filtrar = dep && dep.querySelector("[data-check]").checked ? dep.querySelector("select").value : "";
        const actual = cargo.value;
        cargo.innerHTML = catalogos.cargo.filter(function (c) { return !filtrar || c.departamento === filtrar; })
            .map(function (c) {
                return '<option value="' + esc(c.codigo) + '">' + esc(c.descripcion) + (filtrar ? "" : " (" + esc(c.nombreDepartamento) + ")") + '</option>';
            }).join("");
        if ([].some.call(cargo.options, function (o) { return o.value === actual; })) { cargo.value = actual; }
    }

    function hojaDe(clave) { return document.querySelector('#hojas .hoja[data-hoja="' + clave + '"]'); }

    /** Parámetros de la URL: solo los filtros marcados y el texto de búsqueda. */
    function parametros(clave) {
        const hoja = hojaDe(clave);
        const p = new URLSearchParams();
        hoja.querySelectorAll(".filtro").forEach(function (f) {
            if (!f.querySelector("[data-check]").checked) { return; }
            f.querySelectorAll("[data-valor]").forEach(function (c) {
                if (c.value) { p.set(c.dataset.valor, c.value); }
            });
        });
        const texto = hoja.querySelector("[data-buscar]").value.trim();
        if (texto) { p.set("buscar", texto); }
        return p;
    }

    async function generar(clave) {
        const r = REPORTES[clave];
        const hoja = hojaDe(clave);
        const p = parametros(clave);
        p.set("pagina", estado[clave].pagina);
        p.set("tamano", 10);
        let datos;
        try { datos = await SIGH.api("/reportes/api/" + r.api + "?" + p.toString()); }
        catch (err) { SIGH.error(err.message); return; }
        estado[clave].pagina = datos.pagina;
        estado[clave].paginas = datos.paginas;
        hoja.querySelector("thead").innerHTML = "<tr>" + datos.columnas.map(function (c) { return "<th>" + esc(c.titulo) + "</th>"; }).join("") + "</tr>";
        const cuerpo = hoja.querySelector("tbody");
        if (!datos.filas.length) {
            cuerpo.innerHTML = '<tr><td colspan="' + datos.columnas.length + '" class="text-center text-muted py-4">No hay registros para los filtros seleccionados.</td></tr>';
        } else {
            cuerpo.innerHTML = datos.filas.map(function (f) {
                const sel = r.ficha && f.PEEMP_CODIGO === estado[clave].seleccionado ? " seleccionada" : "";
                return '<tr' + (r.ficha ? ' class="elegible' + sel + '" data-codigo="' + esc(f.PEEMP_CODIGO) + '" data-nombre="' +
                    esc(f.PEEMP_APELLI + " " + f.PEEMP_NOMBRE) + '"' : "") + ">" +
                    datos.columnas.map(function (c) { return "<td>" + celda(c.campo, f[c.campo]) + "</td>"; }).join("") + "</tr>";
            }).join("");
        }
        const desde = datos.total ? (datos.pagina - 1) * datos.tamano + 1 : 0;
        const hasta = Math.min(datos.pagina * datos.tamano, datos.total);
        hoja.querySelector("[data-info]").textContent = "Mostrando " + desde + "-" + hasta + " de " + datos.total + " registros" +
            (datos.filtros ? " · " + datos.filtros : "");
        hoja.querySelector('[data-accion="anterior"]').disabled = datos.pagina <= 1;
        hoja.querySelector('[data-accion="siguiente"]').disabled = datos.pagina >= datos.paginas;
    }

    function celda(campo, valor) {
        if (campo === "PEEMP_ESTADO") {
            return '<span class="chip-estado ' + esc(valor) + '">' + (valor === "A" ? "Activo" : "Inactivo") + "</span>";
        }
        return esc(valor === null || valor === undefined ? "—" : valor);
    }

    function elegir(clave, fila) {
        hojaDe(clave).querySelectorAll("tr.elegible").forEach(function (t) { t.classList.toggle("seleccionada", t === fila); });
        estado[clave].seleccionado = fila.dataset.codigo;
        hojaDe(clave).querySelector("[data-elegido]").innerHTML = '<i class="bi bi-person-check mr-1"></i> Empleado: <strong>' +
            esc(fila.dataset.codigo) + "</strong> · " + esc(fila.dataset.nombre);
    }

    /** URL del archivo de JasperReports con los mismos filtros de la vista previa. */
    function urlArchivo(clave, formato) {
        const r = REPORTES[clave];
        if (r.ficha) {
            if (!estado[clave].seleccionado) { SIGH.error("Seleccione un empleado en la vista previa para generar su ficha."); return null; }
            return SIGH.base + "/reportes/ficha/" + encodeURIComponent(estado[clave].seleccionado) + "?formato=" + formato;
        }
        const p = parametros(clave);
        p.set("formato", formato);
        return SIGH.base + "/reportes/" + r.api + "?" + p.toString();
    }

    function accion(clave, nombre) {
        const hoja = hojaDe(clave);
        switch (nombre) {
            case "buscar":
            case "generar":
                estado[clave].pagina = 1; generar(clave); break;
            case "anterior": estado[clave].pagina--; generar(clave); break;
            case "siguiente": estado[clave].pagina++; generar(clave); break;
            case "limpiar":
                hoja.querySelectorAll("[data-check]").forEach(function (c) { c.checked = false; c.closest(".filtro").classList.remove("abierto"); });
                hoja.querySelectorAll('input[type="date"]').forEach(function (c) { c.value = ""; });
                hoja.querySelector("[data-buscar]").value = "";
                llenarCargos(hoja);
                estado[clave].pagina = 1; generar(clave); break;
            case "pdf": {
                const url = urlArchivo(clave, "pdf");
                if (url) { window.open(url, "_blank"); }
                break;
            }
            case "excel": {
                const url = urlArchivo(clave, "excel");
                if (url) { window.location.href = url; }
                break;
            }
            case "imprimir": imprimir(urlArchivo(clave, "pdf")); break;
        }
    }

    /** Impresión directa: el PDF se carga en un marco oculto y se abre el diálogo de impresión del navegador. */
    function imprimir(url) {
        if (!url) { return; }
        const marco = document.getElementById("marcoImpresion");
        marco.onload = function () {
            setTimeout(function () {
                try { marco.contentWindow.focus(); marco.contentWindow.print(); }
                catch (e) { window.open(url, "_blank"); }
            }, 400);
        };
        marco.src = url + "&imprimir=1";
    }

    // Abrir directamente un reporte con /reportes#empleados
    const inicial = window.location.hash.replace("#", "");
    if (REPORTES[inicial]) { abrir(inicial); }
})();
</script>
</body>
</html>
