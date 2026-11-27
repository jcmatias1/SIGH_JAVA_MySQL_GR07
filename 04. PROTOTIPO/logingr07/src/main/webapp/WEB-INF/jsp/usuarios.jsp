<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%-- ECUD2 Administrar usuarios y roles (opción S001): crear, modificar, asignar perfil, desactivar y desbloquear --%>
<c:set var="titulo" value="Administrar usuarios"/>
<c:set var="activo" value="S001"/>
<!DOCTYPE html>
<html lang="es">
<head>
    <%@ include file="fragmentos/head.jspf" %>
</head>
<body data-contexto="${pageContext.request.contextPath}">
<%@ include file="fragmentos/app-inicio.jspf" %>

<section class="encabezado-pagina">
    <div>
        <div class="antetitulo">Seguridad · S001</div>
        <h1>Cuentas de usuario</h1>
        <p>Cree cuentas para los empleados, asigne su perfil y desactive o desbloquee accesos. Ninguna cuenta se borra.</p>
    </div>
    <button class="btn btn-sigh" type="button" id="btnNuevo"><i class="bi bi-person-plus mr-1"></i> Nuevo usuario</button>
</section>

<div class="panel">
    <div class="d-flex flex-wrap justify-content-between align-items-center mb-3" style="gap:12px">
        <div>
            <h2 class="mb-0">Listado</h2>
            <span class="text-suave" id="resumen" style="font-size:14px">Cargando...</span>
        </div>
        <div class="d-flex flex-wrap" style="gap:10px">
            <select class="custom-select" id="filtroEstado" style="width:190px">
                <option value="">Todos los estados</option>
                <option value="A">Activos</option>
                <option value="I">Inactivos</option>
                <option value="B">Bloqueados</option>
            </select>
            <div class="campo buscador" style="width:300px">
                <i class="bi bi-search"></i>
                <input class="form-control" id="buscar" placeholder="Buscar por usuario, nombre o perfil">
            </div>
        </div>
    </div>
    <div class="table-responsive">
        <table class="table tabla-sigh">
            <thead>
            <tr>
                <th>Usuario</th>
                <th>Empleado</th>
                <th>Perfil</th>
                <th>Estado</th>
                <th>Intentos</th>
                <th>Último acceso</th>
                <th class="text-right">Acciones</th>
            </tr>
            </thead>
            <tbody id="cuerpo">
            <tr><td colspan="7" class="vacio">Cargando usuarios...</td></tr>
            </tbody>
        </table>
    </div>
</div>

<%@ include file="fragmentos/app-fin.jspf" %>
<script>
    const API = "/seguridad/usuarios/api";
    const yo = "<c:out value='${sessionScope.usuarioSesion.codigoUsuario}'/>";
    let usuarios = [];
    let perfiles = [];

    document.addEventListener("DOMContentLoaded", async function () {
        try {
            perfiles = await SIGH.api(API + "/perfiles");
            await cargar();
        } catch (e) {
            SIGH.error(e.message);
        }
    });
    document.getElementById("buscar").addEventListener("input", pintar);
    document.getElementById("filtroEstado").addEventListener("change", pintar);
    document.getElementById("btnNuevo").addEventListener("click", nuevoUsuario);

    async function cargar() {
        usuarios = await SIGH.api(API);
        pintar();
    }

    function etiquetaEstado(u) {
        const clases = { A: "etiqueta-verde", I: "etiqueta-gris", B: "etiqueta-roja" };
        return '<span class="etiqueta ' + (clases[u.codigoEstado] || "etiqueta-gris") + '">' + SIGH.esc(u.descripcionEstado) + '</span>';
    }

    function pintar() {
        const texto = document.getElementById("buscar").value.trim().toLowerCase();
        const estado = document.getElementById("filtroEstado").value;
        const lista = usuarios.filter(function (u) {
            return (!estado || u.codigoEstado === estado) && (!texto || [u.codigoUsuario, u.nombreEmpleado, u.descripcionPerfil, u.email]
                .some(function (v) { return (v || "").toLowerCase().includes(texto); }));
        });
        const activos = usuarios.filter(function (u) { return u.codigoEstado === "A"; }).length;
        document.getElementById("resumen").textContent = usuarios.length + " cuentas · " + activos + " activas";

        const cuerpo = document.getElementById("cuerpo");
        if (!lista.length) {
            cuerpo.innerHTML = '<tr><td colspan="7" class="vacio">No hay usuarios para mostrar.</td></tr>';
            return;
        }
        cuerpo.innerHTML = lista.map(function (u) {
            const iniciales = (u.nombreEmpleado || u.codigoUsuario).split(" ").filter(Boolean).slice(0, 2)
                .map(function (p) { return p.charAt(0); }).join("");
            let acciones = '<button class="btn btn-suave btn-icono" title="Ver detalle" data-accion="detalle" data-codigo="' + SIGH.esc(u.codigoUsuario) + '"><i class="bi bi-eye"></i></button>';
            acciones += '<button class="btn btn-suave btn-icono" title="Modificar / asignar perfil" data-accion="editar" data-codigo="' + SIGH.esc(u.codigoUsuario) + '"><i class="bi bi-pencil-square"></i></button>';
            if (u.codigoEstado === "B") {
                acciones += '<button class="btn btn-exito-suave btn-icono" title="Desbloquear" data-accion="desbloquear" data-codigo="' + SIGH.esc(u.codigoUsuario) + '"><i class="bi bi-unlock"></i></button>';
            }
            if (u.codigoEstado === "I") {
                acciones += '<button class="btn btn-exito-suave btn-icono" title="Activar" data-accion="activar" data-codigo="' + SIGH.esc(u.codigoUsuario) + '"><i class="bi bi-person-check"></i></button>';
            } else if (u.codigoUsuario !== yo) {
                acciones += '<button class="btn btn-peligro-suave btn-icono" title="Desactivar" data-accion="desactivar" data-codigo="' + SIGH.esc(u.codigoUsuario) + '"><i class="bi bi-person-x"></i></button>';
            }
            return '<tr>'
                + '<td><div class="celda-usuario"><span class="avatar">' + SIGH.esc(iniciales) + '</span><div><strong>' + SIGH.esc(u.codigoUsuario) + '</strong>'
                + (u.codigoUsuario === yo ? ' <span class="etiqueta etiqueta-azul">Usted</span>' : '')
                + '<small>' + SIGH.esc(u.pieFirma || "Sin pie de firma") + '</small></div></div></td>'
                + '<td>' + SIGH.esc(u.nombreEmpleado) + '<small class="d-block text-suave">' + SIGH.esc(u.email) + '</small></td>'
                + '<td><span class="etiqueta etiqueta-azul">' + SIGH.esc(u.codigoPerfil || "—") + '</span> ' + SIGH.esc(u.descripcionPerfil) + '</td>'
                + '<td>' + etiquetaEstado(u) + '</td>'
                + '<td class="text-center">' + SIGH.esc(u.intentosFallidos) + '</td>'
                + '<td>' + SIGH.fecha(u.ultimoAcceso) + '</td>'
                + '<td><div class="acciones">' + acciones + '</div></td>'
                + '</tr>';
        }).join("");
    }

    document.getElementById("cuerpo").addEventListener("click", function (evento) {
        const boton = evento.target.closest("button[data-accion]");
        if (!boton) { return; }
        const codigo = boton.getAttribute("data-codigo");
        const accion = boton.getAttribute("data-accion");
        if (accion === "detalle") {
            verDetalle(codigo);
        } else if (accion === "editar") {
            editarUsuario(codigo);
        } else {
            cambiarEstado(codigo, accion);
        }
    });

    // READ individual: ventana con los datos completos de la cuenta
    async function verDetalle(codigo) {
        let d;
        try {
            d = await SIGH.api(API + "/" + encodeURIComponent(codigo));
        } catch (e) {
            SIGH.error(e.message);
            return;
        }
        const c = d.cuenta;
        const fila = function (k, v) { return '<div class="dato"><span>' + k + '</span><strong>' + SIGH.esc(v || "—") + '</strong></div>'; };
        const perfilesHtml = d.perfiles.map(function (p) {
            return '<div class="dato"><span>' + SIGH.esc(p.codigoPerfil + " · " + p.descripcion) + '</span><strong>'
                + (p.fechaRetiro ? '<span class="etiqueta etiqueta-gris">Retirado ' + SIGH.fecha(p.fechaRetiro) + '</span>'
                                 : '<span class="etiqueta etiqueta-verde">Vigente desde ' + SIGH.fecha(p.fechaAsignacion) + '</span>')
                + '</strong></div>';
        }).join("") || '<p class="text-suave">Sin perfiles.</p>';
        const estados = { A: "Abierta", C: "Cerrada", E: "Expirada" };
        const sesionesHtml = d.sesiones.map(function (s) {
            return '<div class="dato"><span>' + SIGH.fecha(s.inicio) + ' · IP ' + SIGH.esc(s.ip || "—") + '</span><strong>'
                + '<span class="etiqueta ' + ({ A: "etiqueta-verde", E: "etiqueta-ambar" }[s.estado] || "etiqueta-gris") + '">'
                + SIGH.esc(estados[s.estado] || s.estado) + '</span></strong></div>';
        }).join("") || '<p class="text-suave">Todavía no ha iniciado sesión.</p>';
        Swal.fire({
            title: "Usuario " + c.codigoUsuario, width: 720, confirmButtonText: "Cerrar", confirmButtonColor: "#0b5cad",
            html: '<div class="row text-left">'
                + '<div class="col-md-6">' + '<div class="sistema-titulo">Cuenta</div>'
                + fila("Estado", c.descripcionEstado) + fila("Perfil", c.descripcionPerfil) + fila("Intentos fallidos", String(c.intentosFallidos))
                + fila("Creación", SIGH.fecha(c.fechaCreacion)) + fila("Última modificación", SIGH.fecha(d.fechaModificacion))
                + fila("Último acceso", SIGH.fecha(c.ultimoAcceso)) + fila("Pie de firma", c.pieFirma) + '</div>'
                + '<div class="col-md-6">' + '<div class="sistema-titulo">Empleado</div>'
                + fila("Nombre", c.nombreEmpleado) + fila("Cédula", c.cedula) + fila("Correo", c.email) + fila("Teléfono", d.telefono)
                + fila("Cargo", d.cargo) + fila("Departamento", d.departamento) + fila("Registro profesional", d.registroProfesional) + '</div>'
                + '<div class="col-md-6"><div class="sistema-titulo mt-3">Historial de perfiles</div>' + perfilesHtml + '</div>'
                + '<div class="col-md-6"><div class="sistema-titulo mt-3">Últimas sesiones</div>' + sesionesHtml + '</div>'
                + '</div>'
        });
    }

    function opcionesPerfil(seleccionado) {
        return perfiles.map(function (p) {
            return '<option value="' + SIGH.esc(p.codigo) + '"' + (p.codigo === seleccionado ? " selected" : "") + '>'
                + SIGH.esc(p.codigo + " - " + p.descripcion) + '</option>';
        }).join("");
    }

    async function nuevoUsuario() {
        let empleados;
        try {
            empleados = await SIGH.api(API + "/empleados");
        } catch (e) {
            SIGH.error(e.message);
            return;
        }
        if (!empleados.length) {
            SIGH.error("Todos los empleados activos ya tienen una cuenta de usuario.");
            return;
        }
        const opcionesEmpleado = empleados.map(function (e) {
            return '<option value="' + SIGH.esc(e.codigoEmpleado) + '" data-email="' + SIGH.esc(e.email) + '">'
                + SIGH.esc(e.nombreCompleto + " · " + e.cargo) + '</option>';
        }).join("");

        const resultado = await Swal.fire({
            title: "Nuevo usuario",
            width: 560,
            showCancelButton: true,
            confirmButtonText: '<i class="bi bi-save"></i> Guardar',
            cancelButtonText: "Cancelar",
            confirmButtonColor: "#0b5cad",
            focusConfirm: false,
            html: '<div class="swal-form">'
                + '<label for="fEmpleado">Empleado</label><select id="fEmpleado" class="custom-select">' + opcionesEmpleado + '</select>'
                + '<label for="fCodigo">Usuario</label><input id="fCodigo" class="form-control" maxlength="20" placeholder="Ej.: dmorales">'
                + '<label for="fPerfil">Perfil</label><select id="fPerfil" class="custom-select">' + opcionesPerfil("MED") + '</select>'
                + '<label for="fPassword">Contraseña inicial</label><input id="fPassword" type="password" class="form-control" maxlength="64" placeholder="Mínimo 8 caracteres con letras y números">'
                + '<label for="fPie">Pie de firma</label><input id="fPie" class="form-control" maxlength="100" placeholder="Ej.: Dra. Diana Morales - Medicina General">'
                + '</div>',
            didOpen: function () {
                const empleado = document.getElementById("fEmpleado");
                const sugerir = function () {
                    const email = empleado.selectedOptions[0].getAttribute("data-email") || "";
                    document.getElementById("fCodigo").value = email.split("@")[0];
                };
                empleado.addEventListener("change", sugerir);
                sugerir();
            },
            preConfirm: function () {
                const datos = {
                    codigoEmpleado: document.getElementById("fEmpleado").value,
                    codigoUsuario: document.getElementById("fCodigo").value.trim(),
                    codigoPerfil: document.getElementById("fPerfil").value,
                    password: document.getElementById("fPassword").value,
                    pieFirma: document.getElementById("fPie").value.trim()
                };
                if (!datos.codigoUsuario || !datos.password) {
                    Swal.showValidationMessage("El usuario y la contraseña inicial son obligatorios.");
                    return false;
                }
                return SIGH.api(API, { method: "POST", body: datos }).catch(function (e) {
                    Swal.showValidationMessage(e.message);
                    return false;
                });
            }
        });
        if (resultado.isConfirmed && resultado.value) {
            await cargar();
            SIGH.exito("Usuario creado", "La cuenta " + resultado.value.codigoUsuario + " quedó activa.");
        }
    }

    async function editarUsuario(codigo) {
        const u = usuarios.find(function (x) { return x.codigoUsuario === codigo; });
        if (!u) { return; }
        const resultado = await Swal.fire({
            title: "Modificar " + u.codigoUsuario,
            width: 560,
            showCancelButton: true,
            confirmButtonText: '<i class="bi bi-save"></i> Guardar',
            cancelButtonText: "Cancelar",
            confirmButtonColor: "#0b5cad",
            focusConfirm: false,
            html: '<div class="swal-form">'
                + '<p class="text-suave mb-1">' + SIGH.esc(u.nombreEmpleado) + ' · ' + SIGH.esc(u.email) + '</p>'
                + '<label for="fPerfil">Perfil (asignar rol)</label><select id="fPerfil" class="custom-select"' + (u.codigoUsuario === yo ? " disabled" : "") + '>' + opcionesPerfil(u.codigoPerfil) + '</select>'
                + '<label for="fPie">Pie de firma</label><input id="fPie" class="form-control" maxlength="100" value="' + SIGH.esc(u.pieFirma || "") + '">'
                + '<label for="fPassword">Nueva contraseña (opcional)</label><input id="fPassword" type="password" class="form-control" maxlength="64" placeholder="Déjela vacía para no cambiarla">'
                + '</div>',
            preConfirm: function () {
                const datos = {
                    codigoPerfil: document.getElementById("fPerfil").value,
                    pieFirma: document.getElementById("fPie").value.trim(),
                    password: document.getElementById("fPassword").value
                };
                return SIGH.api(API + "/" + encodeURIComponent(codigo), { method: "PUT", body: datos }).catch(function (e) {
                    Swal.showValidationMessage(e.message);
                    return false;
                });
            }
        });
        if (resultado.isConfirmed && resultado.value) {
            await cargar();
            SIGH.exito("Usuario actualizado", "Los permisos se aplican en la siguiente solicitud del usuario.");
        }
    }

    async function cambiarEstado(codigo, accion) {
        const textos = {
            desactivar: ["¿Desactivar la cuenta " + codigo + "?", "La cuenta no podrá iniciar sesión, pero se conserva su historial.", "Desactivar", "#b42318"],
            activar: ["¿Activar la cuenta " + codigo + "?", "El usuario podrá volver a iniciar sesión.", "Activar", "#157347"],
            desbloquear: ["¿Desbloquear la cuenta " + codigo + "?", "Se reinician los intentos fallidos.", "Desbloquear", "#157347"]
        };
        const t = textos[accion];
        const confirmacion = await Swal.fire({
            icon: accion === "desactivar" ? "warning" : "question",
            title: t[0], text: t[1],
            showCancelButton: true,
            confirmButtonText: t[2], cancelButtonText: "Cancelar",
            confirmButtonColor: t[3]
        });
        if (!confirmacion.isConfirmed) { return; }
        try {
            await SIGH.api(API + "/" + encodeURIComponent(codigo) + "/" + accion, { method: "PATCH" });
            await cargar();
            SIGH.exito("Listo", "La cuenta " + codigo + " fue actualizada.");
        } catch (e) {
            SIGH.error(e.message);
        }
    }
</script>
</body>
</html>
