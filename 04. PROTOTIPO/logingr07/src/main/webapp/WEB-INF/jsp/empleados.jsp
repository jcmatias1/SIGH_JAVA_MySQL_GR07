<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%-- Mantenimiento de empleados (opción P003): CRUD de PEEMP_EMPLE con borrado lógico --%>
<c:set var="titulo" value="Mantenimiento de empleados"/>
<c:set var="activo" value="P003"/>
<!DOCTYPE html>
<html lang="es">
<head>
    <%@ include file="fragmentos/head.jspf" %>
    <style>
        .ficha { background: #e6e9ee; border-color: #d3d8df; }
        .ficha .malla { display: grid; gap: 12px 28px; grid-template-columns: repeat(2, minmax(0, 1fr)); }
        .ficha .fila-campo { align-items: center; display: grid; gap: 12px; grid-template-columns: 170px minmax(0, 1fr); }
        .ficha .fila-campo label { color: #374151; font-size: 14px; font-weight: 600; margin: 0; }
        .ficha .form-control, .ficha .custom-select { background-color: #fff; height: 38px; width: 100%; }
        .ficha .form-control[readonly] { background-color: #f1f3f6; }
        .pestanas { border-bottom: 1px solid #c9cfd8; display: flex; flex-wrap: wrap; gap: 4px; margin: 20px 0 0; }
        .pestanas button { background: transparent; border: 1px solid transparent; border-bottom: 0; border-radius: 8px 8px 0 0;
            color: #4b5563; font-weight: 600; margin-bottom: -1px; padding: 9px 16px; }
        .pestanas button.activa { background: #f4f6f8; border-color: #c9cfd8; color: var(--azul-800); }
        .pestana { background: #f4f6f8; border: 1px solid #c9cfd8; border-top: 0; border-radius: 0 0 8px 8px; display: none; padding: 18px; }
        .pestana.activa { display: block; }
        .subtitulo { border-top: 1px solid #d3d8df; color: var(--azul-900); font-size: 15px; font-weight: 700; margin: 18px 0 10px; padding-top: 14px; }
        .captura { align-items: end; display: grid; gap: 10px; }
        .captura.academica { grid-template-columns: 1fr 1.4fr 1.4fr auto; }
        .captura.familiar { grid-template-columns: 1fr 1.2fr 1.2fr 1fr auto; }
        .captura label { color: #374151; font-size: 13px; font-weight: 600; margin-bottom: 4px; }
        .grilla { background: #fff; font-size: 13px; margin: 10px 0 0; }
        .grilla th { background: #eef2f7; font-size: 12px; text-transform: uppercase; }
        .grilla td, .grilla th { padding: 6px 10px; vertical-align: middle; }
        .resumen { margin-top: 6px; max-height: 260px; overflow: auto; }
        .resumen tbody tr { cursor: pointer; }
        .resumen tbody tr.seleccionada td { background: var(--azul-100); }
        @media (max-width: 991px) {
            .ficha .malla { grid-template-columns: 1fr; }
            .ficha .fila-campo { grid-template-columns: 140px minmax(0, 1fr); }
            .captura.academica, .captura.familiar { grid-template-columns: 1fr; }
        }
        .botonera { display: flex; flex-wrap: wrap; gap: 8px; justify-content: flex-end; margin-top: 18px; }
        .tabla-sigh tbody tr { cursor: pointer; }
        .tabla-sigh tbody tr.seleccionada td { background: var(--azul-100); }
        .modo { font-size: 13px; }
    </style>
</head>
<body data-contexto="${pageContext.request.contextPath}">
<%@ include file="fragmentos/app-inicio.jspf" %>

<section class="encabezado-pagina">
    <div>
        <div class="antetitulo">Personal · P003</div>
        <h1>Mantenimiento de empleados</h1>
        <p>Registre y actualice los datos del personal. «Borrar» no elimina el registro: el empleado pasa a Inactivo y se conserva su historial.</p>
    </div>
</section>

<div class="panel ficha mb-4">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h2 class="mb-0" id="tituloFicha">Nuevo empleado</h2>
        <span class="etiqueta etiqueta-verde modo" id="modo">Registro nuevo</span>
    </div>
    <div class="malla">
            <div class="fila-campo"><label for="fCodigo">Código empleado:</label><input id="fCodigo" class="form-control" readonly></div>
            <div class="fila-campo"><label for="fCedula">Cédula:</label><input id="fCedula" class="form-control" maxlength="10" inputmode="numeric" placeholder="10 dígitos"></div>
            <div class="fila-campo"><label for="fApellidos">Apellidos empleado:</label><input id="fApellidos" class="form-control" maxlength="60" placeholder="Ej.: PEREZ LOPEZ"></div>
            <div class="fila-campo"><label for="fNombres">Nombres empleado:</label><input id="fNombres" class="form-control" maxlength="60" placeholder="Ej.: JUAN CARLOS"></div>
    </div>

    <div class="pestanas" role="tablist">
        <button type="button" class="activa" data-pestana="tabPersonal"><i class="bi bi-person mr-1"></i> Inf personal</button>
        <button type="button" data-pestana="tabProfesional"><i class="bi bi-briefcase mr-1"></i> Inf profesional</button>
        <button type="button" data-pestana="tabContacto"><i class="bi bi-telephone mr-1"></i> Inf contacto</button>
    </div>
    <div class="pestana activa" id="tabPersonal">
        <div class="malla">
            <div class="fila-campo"><label for="fFecha">Fecha nac. empleado:</label><input id="fFecha" type="date" class="form-control"></div>
            <div class="fila-campo"><label for="fGenero">Género:</label><select id="fGenero" class="custom-select"></select></div>
            <div class="fila-campo"><label for="fEstadoCivil">Estado civil:</label><select id="fEstadoCivil" class="custom-select"></select></div>
            <div class="fila-campo"><label for="fSangre">Tipo de sangre:</label><select id="fSangre" class="custom-select"></select></div>
            <div class="fila-campo"><label for="fPeso">Peso (kg):</label><input id="fPeso" type="number" step="0.1" min="2" max="300" class="form-control" placeholder="Ej.: 68.5"></div>
            <div class="fila-campo"><label for="fEstatura">Estatura (m):</label><input id="fEstatura" type="number" step="0.01" min="0.4" max="2.5" class="form-control" placeholder="Ej.: 1.70"></div>
        </div>
        <div class="subtitulo">Resumen</div>
        <div class="table-responsive resumen">
            <table class="table tabla-sigh grilla"><thead><tr><th>Código</th><th>Apellidos y nombres</th><th>Fecha nac.</th><th>Género</th><th>Estado civil</th><th>Sangre</th><th>Peso</th><th>Estatura</th></tr></thead><tbody id="resPersonal"></tbody></table>
        </div>
    </div>
    <div class="pestana" id="tabProfesional">
        <div class="malla">
            <div class="fila-campo"><label for="fRegistro">Registro prof.:</label><input id="fRegistro" class="form-control" maxlength="30" placeholder="Ej.: MSP-17-04521"></div>
            <div class="fila-campo"><label for="fEstado">Estado:</label><select id="fEstado" class="custom-select"><option value="A">Activo</option><option value="I">Inactivo</option></select></div>
            <div class="fila-campo"><label for="fCargo">Cargo:</label><select id="fCargo" class="custom-select"></select></div>
        </div>
        <div class="subtitulo">Datos académicos</div>
        <div class="captura academica">
            <div><label for="aNivel">Nivel de estudio</label><select id="aNivel" class="custom-select"></select></div>
            <div><label for="aInstitucion">Institución</label><input id="aInstitucion" class="form-control" maxlength="100" placeholder="Ej.: Universidad Central del Ecuador"></div>
            <div><label for="aTitulo">Título obtenido</label><input id="aTitulo" class="form-control" maxlength="100" placeholder="Ej.: Médico General"></div>
            <div><button class="btn btn-sigh" type="button" id="btnAgregarAcademico"><i class="bi bi-plus-lg mr-1"></i> Agregar</button></div>
        </div>
        <div class="table-responsive">
            <table class="table tabla-sigh grilla"><thead><tr><th>#</th><th>Nivel de estudio</th><th>Institución</th><th>Título obtenido</th><th></th></tr></thead>
                <tbody id="gridAcademicos"></tbody></table>
        </div>
        <div class="subtitulo">Resumen</div>
        <div class="table-responsive resumen">
            <table class="table tabla-sigh grilla"><thead><tr><th>Código</th><th>Apellidos y nombres</th><th>Departamento</th><th>Cargo</th><th>Registro prof.</th><th>Títulos</th><th>Estado</th></tr></thead><tbody id="resProfesional"></tbody></table>
        </div>
    </div>
    <div class="pestana" id="tabContacto">
        <div class="malla">
            <div class="fila-campo"><label for="fDireccion">Dirección:</label><input id="fDireccion" class="form-control" maxlength="150" placeholder="Calle, número y ciudad"></div>
            <div class="fila-campo"><label for="fTelefono">Teléfono:</label><input id="fTelefono" class="form-control" maxlength="10" inputmode="numeric" placeholder="Ej.: 0991234567"></div>
            <div class="fila-campo"><label for="fEmail">Email:</label><input id="fEmail" type="email" class="form-control" maxlength="100" placeholder="nombre@monstersalud.ec"></div>
        </div>
        <div class="subtitulo">Datos familiares</div>
        <div class="captura familiar">
            <div><label for="xParentesco">Parentesco</label><select id="xParentesco" class="custom-select"></select></div>
            <div><label for="xApellidos">Apellidos</label><input id="xApellidos" class="form-control" maxlength="60" placeholder="Ej.: PEREZ GOMEZ"></div>
            <div><label for="xNombres">Nombres</label><input id="xNombres" class="form-control" maxlength="60" placeholder="Ej.: ANA MARIA"></div>
            <div><label for="xFecha">Fecha de nacimiento</label><input id="xFecha" type="date" class="form-control"></div>
            <div><button class="btn btn-sigh" type="button" id="btnAgregarFamiliar"><i class="bi bi-plus-lg mr-1"></i> Agregar</button></div>
        </div>
        <div class="table-responsive">
            <table class="table tabla-sigh grilla"><thead><tr><th>#</th><th>Parentesco</th><th>Apellidos</th><th>Nombres</th><th>Fecha nac.</th><th></th></tr></thead>
                <tbody id="gridFamiliares"></tbody></table>
        </div>
        <div class="subtitulo">Resumen</div>
        <div class="table-responsive resumen">
            <table class="table tabla-sigh grilla"><thead><tr><th>Código</th><th>Apellidos y nombres</th><th>Dirección</th><th>Teléfono</th><th>Email</th><th>Familiares</th></tr></thead><tbody id="resContacto"></tbody></table>
        </div>
    </div>

    <div class="botonera">
        <button class="btn btn-sigh" type="button" id="btnGuardar"><i class="bi bi-save mr-1"></i> Guardar</button>
        <button class="btn btn-suave" type="button" id="btnActualizar"><i class="bi bi-pencil-square mr-1"></i> Actualizar</button>
        <button class="btn btn-peligro-suave" type="button" id="btnBorrar"><i class="bi bi-trash mr-1"></i> Borrar</button>
        <button class="btn btn-suave" type="button" id="btnCancelar"><i class="bi bi-x-lg mr-1"></i> Cancelar</button>
        <button class="btn btn-suave" type="button" id="btnImprimir" title="Ficha PDF del empleado seleccionado"><i class="bi bi-printer mr-1"></i> Imprimir</button>
    </div>
</div>

<div class="panel" id="panelLista">
    <div class="d-flex flex-wrap justify-content-between align-items-center mb-3" style="gap:12px">
        <div>
            <h2 class="mb-0">Empleados registrados</h2>
            <span class="text-suave" id="resumen" style="font-size:14px">Cargando...</span>
        </div>
        <div class="d-flex flex-wrap" style="gap:10px">
            <%-- Taller 3: listado PDF con JasperReports (usa el filtro de estado) --%>
            <button class="btn btn-suave" type="button" id="btnListado"><i class="bi bi-file-earmark-pdf mr-1"></i> Listado PDF</button>
            <select class="custom-select" id="filtroEstado" style="width:170px">
                <option value="A">Activos</option>
                <option value="I">Inactivos</option>
                <option value="">Todos</option>
            </select>
            <div class="campo buscador" style="width:300px">
                <i class="bi bi-search"></i>
                <input class="form-control" id="filtroTexto" placeholder="Filtrar por código, cédula o nombre">
            </div>
        </div>
    </div>
    <div class="table-responsive">
        <table class="table tabla-sigh">
            <thead>
            <tr>
                <th>Código</th>
                <th>Cédula</th>
                <th>Apellidos y nombres</th>
                <th>Cargo</th>
                <th>Teléfono</th>
                <th>Email</th>
                <th class="text-center">Estado</th>
            </tr>
            </thead>
            <tbody id="cuerpo">
            <tr><td colspan="7" class="vacio">Cargando empleados...</td></tr>
            </tbody>
        </table>
    </div>
</div>

<%@ include file="fragmentos/app-fin.jspf" %>
<script>
    const API = "/personal/empleados/api";
    let empleados = [];
    let catalogos = { generos: [], estadosCiviles: [], cargos: [], siguienteCodigo: "" };
    let seleccionado = null;
    let academicos = [];   // filas de la grilla de datos académicos del empleado en edición
    let familiares = [];   // filas de la grilla de datos familiares

    const $ = function (id) { return document.getElementById(id); };

    document.addEventListener("DOMContentLoaded", async function () {
        try {
            await cargarCatalogos();
            await listar();
            limpiar();
        } catch (e) {
            SIGH.error(e.message);
        }
    });

    // Pestañas Inf personal / profesional / contacto
    document.querySelectorAll(".pestanas button").forEach(function (b) {
        b.addEventListener("click", function () { mostrarPestana(b.getAttribute("data-pestana")); });
    });
    function mostrarPestana(id) {
        document.querySelectorAll(".pestanas button").forEach(function (b) {
            b.classList.toggle("activa", b.getAttribute("data-pestana") === id);
        });
        document.querySelectorAll(".pestana").forEach(function (p) { p.classList.toggle("activa", p.id === id); });
    }

    // Solo dígitos en cédula y teléfono; mayúsculas en nombres
    ["fCedula", "fTelefono"].forEach(function (id) {
        $(id).addEventListener("input", function () { this.value = this.value.replace(/\D/g, ""); });
    });
    ["fApellidos", "fNombres", "fRegistro"].forEach(function (id) {
        $(id).addEventListener("input", function () { this.value = this.value.toUpperCase(); });
    });

    async function cargarCatalogos() {
        catalogos = await SIGH.api(API + "/catalogos");
        $("fGenero").innerHTML = opciones(catalogos.generos);
        $("fEstadoCivil").innerHTML = opciones(catalogos.estadosCiviles);
        $("fSangre").innerHTML = opciones(catalogos.tiposSangre);
        $("aNivel").innerHTML = opciones(catalogos.niveles);
        $("xParentesco").innerHTML = opciones(catalogos.parentescos);
        // Cargos agrupados por departamento; el valor es DEPARTAMENTO|CARGO
        const grupos = {};
        catalogos.cargos.forEach(function (c) { (grupos[c.departamento] = grupos[c.departamento] || []).push(c); });
        $("fCargo").innerHTML = '<option value="">Seleccionar...</option>' + Object.keys(grupos).map(function (dep) {
            return '<optgroup label="' + SIGH.esc(dep) + '">' + grupos[dep].map(function (c) {
                return '<option value="' + SIGH.esc(c.codigoDepartamento + "|" + c.codigoCargo) + '">' + SIGH.esc(c.descripcion) + '</option>';
            }).join("") + '</optgroup>';
        }).join("");
    }

    function opciones(lista) {
        return '<option value="">Seleccionar...</option>' + lista.map(function (o) {
            return '<option value="' + SIGH.esc(o.codigo) + '">' + SIGH.esc(o.descripcion) + '</option>';
        }).join("");
    }

    // LISTAR
    async function listar() {
        empleados = await SIGH.api(API);
        pintar();
    }

    function filtrados() {
        const estado = $("filtroEstado").value;
        const texto = $("filtroTexto").value.trim().toLowerCase();
        return empleados.filter(function (e) {
            return (!estado || e.estado === estado) && (!texto || [e.codigoEmpleado, e.cedula, e.apellidos + " " + e.nombres]
                .some(function (v) { return (v || "").toLowerCase().includes(texto); }));
        });
    }

    function pintar() {
        const lista = filtrados();
        pintarResumenes(lista);
        const activos = empleados.filter(function (e) { return e.estado === "A"; }).length;
        $("resumen").textContent = empleados.length + " empleados · " + activos + " activos · " + (empleados.length - activos) + " inactivos";
        if (!lista.length) {
            $("cuerpo").innerHTML = '<tr><td colspan="7" class="vacio">No hay empleados para mostrar.</td></tr>';
            return;
        }
        $("cuerpo").innerHTML = lista.map(function (e) {
            return '<tr data-codigo="' + SIGH.esc(e.codigoEmpleado) + '"' + (e.codigoEmpleado === seleccionado ? ' class="seleccionada"' : '') + '>'
                + '<td><span class="etiqueta etiqueta-azul">' + SIGH.esc(e.codigoEmpleado) + '</span></td>'
                + '<td>' + SIGH.esc(e.cedula) + '</td>'
                + '<td><strong>' + SIGH.esc(e.apellidos + " " + e.nombres) + '</strong></td>'
                + '<td>' + SIGH.esc(e.cargo) + '<small class="d-block text-suave">' + SIGH.esc(e.departamento) + '</small></td>'
                + '<td>' + SIGH.esc(e.telefono || "—") + '</td>'
                + '<td>' + SIGH.esc(e.email) + '</td>'
                + '<td class="text-center">' + (e.estado === "A"
                    ? '<span class="etiqueta etiqueta-verde">Activo</span>'
                    : '<span class="etiqueta etiqueta-gris">Inactivo</span>') + '</td>'
                + '</tr>';
        }).join("");
    }

    $("filtroEstado").addEventListener("change", pintar);
    $("filtroTexto").addEventListener("input", pintar);
    $("cuerpo").addEventListener("click", function (evento) {
        const fila = evento.target.closest("tr[data-codigo]");
        if (fila) { seleccionar(fila.getAttribute("data-codigo")); }
    });

    function seleccionar(codigo) {
        const e = empleados.find(function (x) { return x.codigoEmpleado === codigo; });
        if (!e) { return; }
        seleccionado = codigo;
        $("fCodigo").value = e.codigoEmpleado;
        $("fCedula").value = e.cedula;
        $("fApellidos").value = e.apellidos;
        $("fNombres").value = e.nombres;
        $("fFecha").value = e.fechaNacimiento || "";
        $("fGenero").value = e.codigoGenero || "";
        $("fEstadoCivil").value = e.codigoEstadoCivil || "";
        $("fSangre").value = e.codigoTipoSangre || "";
        $("fPeso").value = e.peso === null || e.peso === undefined ? "" : e.peso;
        $("fEstatura").value = e.estatura === null || e.estatura === undefined ? "" : e.estatura;
        academicos = (e.academicos || []).map(function (a) { return Object.assign({}, a); });
        familiares = (e.familiares || []).map(function (f) { return Object.assign({}, f); });
        pintarGrillas();
        $("fRegistro").value = e.registroProfesional || "";
        $("fEstado").value = e.estado;
        $("fCargo").value = e.codigoDepartamento + "|" + e.codigoCargo;
        $("fDireccion").value = e.direccion || "";
        $("fTelefono").value = e.telefono || "";
        $("fEmail").value = e.email;
        modo();
        pintar();
        window.scrollTo({ top: 0, behavior: "smooth" });
    }

    // CANCELAR: deja el formulario listo para un registro nuevo
    function limpiar() {
        seleccionado = null;
        ["fCedula", "fApellidos", "fNombres", "fFecha", "fGenero", "fEstadoCivil", "fSangre", "fPeso", "fEstatura",
            "fRegistro", "fCargo", "fDireccion", "fTelefono", "fEmail", "aNivel", "aInstitucion", "aTitulo",
            "xParentesco", "xApellidos", "xNombres", "xFecha"].forEach(function (id) { $(id).value = ""; });
        academicos = [];
        familiares = [];
        pintarGrillas();
        $("fCodigo").value = catalogos.siguienteCodigo || "";
        $("fEstado").value = "A";
        mostrarPestana("tabPersonal");
        modo();
        pintar();
    }

    function modo() {
        const editando = !!seleccionado;
        $("tituloFicha").textContent = editando ? "Empleado " + seleccionado : "Nuevo empleado";
        $("modo").textContent = editando ? "Editando registro existente" : "Registro nuevo";
        $("modo").className = "etiqueta modo " + (editando ? "etiqueta-ambar" : "etiqueta-verde");
        $("btnGuardar").disabled = editando;
        $("btnActualizar").disabled = !editando;
        const e = empleados.find(function (x) { return x.codigoEmpleado === seleccionado; });
        $("btnBorrar").disabled = !editando || (e && e.estado === "I");
        $("btnImprimir").disabled = !editando; // la ficha se imprime del empleado seleccionado
        $("fEstado").disabled = !editando; // un registro nuevo siempre nace Activo
    }

    function datos() {
        const cargo = $("fCargo").value.split("|");
        return {
            cedula: $("fCedula").value.trim(),
            apellidos: $("fApellidos").value.trim(),
            nombres: $("fNombres").value.trim(),
            fechaNacimiento: $("fFecha").value,
            codigoGenero: $("fGenero").value,
            codigoEstadoCivil: $("fEstadoCivil").value,
            codigoTipoSangre: $("fSangre").value,
            peso: $("fPeso").value.trim(),
            estatura: $("fEstatura").value.trim(),
            academicos: academicos,
            familiares: familiares,
            registroProfesional: $("fRegistro").value.trim(),
            estado: $("fEstado").value,
            codigoDepartamento: cargo[0] || "",
            codigoCargo: cargo[1] || "",
            direccion: $("fDireccion").value.trim(),
            telefono: $("fTelefono").value.trim(),
            email: $("fEmail").value.trim()
        };
    }

    /** Validación rápida en pantalla; el servidor vuelve a validar todo. */
    function validar(d) {
        const faltan = [];
        if (!/^\d{10}$/.test(d.cedula)) { faltan.push("Cédula (10 dígitos)"); }
        if (d.apellidos.length < 2) { faltan.push("Apellidos"); }
        if (d.nombres.length < 2) { faltan.push("Nombres"); }
        if (!d.fechaNacimiento) { faltan.push("Fecha de nacimiento (Inf personal)"); }
        if (!d.codigoGenero) { faltan.push("Género (Inf personal)"); }
        if (!d.codigoEstadoCivil) { faltan.push("Estado civil (Inf personal)"); }
        if (!d.codigoTipoSangre) { faltan.push("Tipo de sangre (Inf personal)"); }
        if (!d.peso) { faltan.push("Peso (Inf personal)"); }
        if (!d.estatura) { faltan.push("Estatura (Inf personal)"); }
        if (!d.codigoCargo) { faltan.push("Cargo (Inf profesional)"); }
        if (!d.email) { faltan.push("Email (Inf contacto)"); }
        if (faltan.length) {
            SIGH.error("Complete los campos: " + faltan.join(", ") + ".");
            return false;
        }
        return true;
    }

    // ---------- Grillas de datos académicos y familiares ----------
    function descripcion(lista, codigo) {
        const o = lista.find(function (x) { return x.codigo === codigo; });
        return o ? o.descripcion : codigo;
    }

    function pintarGrillas() {
        $("gridAcademicos").innerHTML = academicos.length ? academicos.map(function (a, i) {
            return '<tr><td>' + (i + 1) + '</td><td>' + SIGH.esc(a.nivel) + '</td><td>' + SIGH.esc(a.institucion) + '</td><td>'
                + SIGH.esc(a.titulo) + '</td><td class="text-right"><button type="button" class="btn btn-peligro-suave btn-icono" title="Quitar" data-quitar-academico="'
                + i + '"><i class="bi bi-trash"></i></button></td></tr>';
        }).join("") : '<tr><td colspan="5" class="vacio">Sin datos académicos. Llene los campos y pulse «Agregar».</td></tr>';
        $("gridFamiliares").innerHTML = familiares.length ? familiares.map(function (f, i) {
            return '<tr><td>' + (i + 1) + '</td><td>' + SIGH.esc(f.parentesco) + '</td><td>' + SIGH.esc(f.apellidos) + '</td><td>'
                + SIGH.esc(f.nombres) + '</td><td>' + SIGH.esc(fechaCorta(f.fechaNacimiento)) + '</td><td class="text-right">'
                + '<button type="button" class="btn btn-peligro-suave btn-icono" title="Quitar" data-quitar-familiar="' + i
                + '"><i class="bi bi-trash"></i></button></td></tr>';
        }).join("") : '<tr><td colspan="6" class="vacio">Sin familiares registrados. Llene los campos y pulse «Agregar».</td></tr>';
    }

    function fechaCorta(iso) {
        if (!iso) { return ""; }
        const p = String(iso).split("-");
        return p.length === 3 ? p[2] + "/" + p[1] + "/" + p[0] : iso;
    }

    $("btnAgregarAcademico").addEventListener("click", function () {
        const fila = {
            codigoNivel: $("aNivel").value,
            institucion: $("aInstitucion").value.trim(),
            titulo: $("aTitulo").value.trim()
        };
        if (!fila.codigoNivel || fila.institucion.length < 3 || fila.titulo.length < 3) {
            SIGH.error("Para agregar un dato académico elija el nivel y escriba la institución y el título (mínimo 3 caracteres).");
            return;
        }
        fila.nivel = descripcion(catalogos.niveles, fila.codigoNivel);
        academicos.push(fila);
        ["aNivel", "aInstitucion", "aTitulo"].forEach(function (id) { $(id).value = ""; });
        pintarGrillas();
    });

    $("btnAgregarFamiliar").addEventListener("click", function () {
        const fila = {
            codigoParentesco: $("xParentesco").value,
            apellidos: $("xApellidos").value.trim().toUpperCase(),
            nombres: $("xNombres").value.trim().toUpperCase(),
            fechaNacimiento: $("xFecha").value
        };
        if (!fila.codigoParentesco || fila.apellidos.length < 2 || fila.nombres.length < 2 || !fila.fechaNacimiento) {
            SIGH.error("Para agregar un familiar elija el parentesco y escriba apellidos, nombres y fecha de nacimiento.");
            return;
        }
        fila.parentesco = descripcion(catalogos.parentescos, fila.codigoParentesco);
        familiares.push(fila);
        ["xParentesco", "xApellidos", "xNombres", "xFecha"].forEach(function (id) { $(id).value = ""; });
        pintarGrillas();
    });

    ["xApellidos", "xNombres"].forEach(function (id) {
        $(id).addEventListener("input", function () { this.value = this.value.toUpperCase(); });
    });

    document.addEventListener("click", function (evento) {
        const a = evento.target.closest("[data-quitar-academico]");
        if (a) { academicos.splice(parseInt(a.getAttribute("data-quitar-academico"), 10), 1); pintarGrillas(); return; }
        const f = evento.target.closest("[data-quitar-familiar]");
        if (f) { familiares.splice(parseInt(f.getAttribute("data-quitar-familiar"), 10), 1); pintarGrillas(); }
    });

    // ---------- Tablas resumen de cada pestaña ----------
    function filaResumen(e, celdas) {
        return '<tr data-codigo="' + SIGH.esc(e.codigoEmpleado) + '"' + (e.codigoEmpleado === seleccionado ? ' class="seleccionada"' : '') + '>'
            + '<td><span class="etiqueta etiqueta-azul">' + SIGH.esc(e.codigoEmpleado) + '</span></td>'
            + '<td><strong>' + SIGH.esc(e.apellidos + " " + e.nombres) + '</strong></td>'
            + celdas.map(function (c) { return '<td>' + SIGH.esc(c === null || c === undefined || c === "" ? "—" : c) + '</td>'; }).join("")
            + '</tr>';
    }

    function pintarResumenes(lista) {
        const vacio = function (n) { return '<tr><td colspan="' + n + '" class="vacio">No hay empleados para mostrar.</td></tr>'; };
        $("resPersonal").innerHTML = lista.length ? lista.map(function (e) {
            return filaResumen(e, [fechaCorta(e.fechaNacimiento), e.genero, e.estadoCivil, e.codigoTipoSangre,
                e.peso === null || e.peso === undefined ? "" : e.peso + " kg",
                e.estatura === null || e.estatura === undefined ? "" : e.estatura + " m"]);
        }).join("") : vacio(8);
        $("resProfesional").innerHTML = lista.length ? lista.map(function (e) {
            return filaResumen(e, [e.departamento, e.cargo, e.registroProfesional, (e.academicos || []).length,
                e.estado === "A" ? "Activo" : "Inactivo"]);
        }).join("") : vacio(7);
        $("resContacto").innerHTML = lista.length ? lista.map(function (e) {
            return filaResumen(e, [e.direccion, e.telefono, e.email, (e.familiares || []).length]);
        }).join("") : vacio(6);
    }

    ["resPersonal", "resProfesional", "resContacto"].forEach(function (id) {
        $(id).addEventListener("click", function (evento) {
            const fila = evento.target.closest("tr[data-codigo]");
            if (fila) { seleccionar(fila.getAttribute("data-codigo")); }
        });
    });

    // GUARDAR (registro nuevo)
    $("btnGuardar").addEventListener("click", async function () {
        const d = datos();
        if (!validar(d)) { return; }
        try {
            const nuevo = await SIGH.api(API, { method: "POST", body: d });
            catalogos = await SIGH.api(API + "/catalogos");
            await listar();
            limpiar();
            SIGH.exito("Empleado registrado", "Se registró a " + nuevo.apellidos + " " + nuevo.nombres + " con el código " + nuevo.codigoEmpleado + ".");
        } catch (e) {
            SIGH.error(e.message);
        }
    });

    // ACTUALIZAR (registro seleccionado)
    $("btnActualizar").addEventListener("click", async function () {
        if (!seleccionado) { return; }
        const d = datos();
        if (!validar(d)) { return; }
        try {
            await SIGH.api(API + "/" + encodeURIComponent(seleccionado), { method: "PUT", body: d });
            const codigo = seleccionado;
            await listar();
            seleccionar(codigo);
            SIGH.exito("Empleado actualizado", "Los datos de " + codigo + " se guardaron correctamente.");
        } catch (e) {
            SIGH.error(e.message);
        }
    });

    // BORRAR (lógico: pasa a Inactivo)
    $("btnBorrar").addEventListener("click", async function () {
        const e = empleados.find(function (x) { return x.codigoEmpleado === seleccionado; });
        if (!e) { return; }
        const r = await Swal.fire({
            icon: "warning",
            title: "¿Borrar al empleado " + e.codigoEmpleado + "?",
            text: e.apellidos + " " + e.nombres + " pasará a estado Inactivo. El registro no se elimina y se puede reactivar con «Actualizar».",
            showCancelButton: true, confirmButtonText: "Borrar", cancelButtonText: "Cancelar", confirmButtonColor: "#b42318"
        });
        if (!r.isConfirmed) { return; }
        try {
            await SIGH.api(API + "/" + encodeURIComponent(e.codigoEmpleado), { method: "DELETE" });
            await listar();
            limpiar();
            SIGH.exito("Empleado desactivado", e.codigoEmpleado + " quedó Inactivo. Puede verlo con el filtro «Inactivos».");
        } catch (err) {
            SIGH.error(err.message);
        }
    });

    $("btnCancelar").addEventListener("click", limpiar);

    // IMPRIMIR: ficha PDF (JasperReports) del empleado seleccionado, con sus 3 pestañas,
    // sus datos académicos y sus familiares
    $("btnImprimir").addEventListener("click", function () {
        if (!seleccionado) { SIGH.error("Seleccione un empleado del resumen para imprimir su ficha."); return; }
        window.open("${pageContext.request.contextPath}/personal/empleados/ficha/" + encodeURIComponent(seleccionado), "_blank");
    });

    // LISTADO PDF: reporte de empleados con el filtro de estado actual (A, I o todos)
    $("btnListado").addEventListener("click", function () {
        window.open("${pageContext.request.contextPath}/personal/empleados/reporte?estado=" + encodeURIComponent($("filtroEstado").value), "_blank");
    });
</script>
</body>
</html>
