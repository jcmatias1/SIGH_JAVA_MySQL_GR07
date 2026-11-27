<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%-- ECUD1 paso 3: menú principal con los módulos habilitados para el perfil del usuario --%>
<c:set var="titulo" value="Inicio"/>
<c:set var="activo" value="INICIO"/>
<!DOCTYPE html>
<html lang="es">
<head>
    <%@ include file="fragmentos/head.jspf" %>
</head>
<body data-contexto="${pageContext.request.contextPath}">
<%@ include file="fragmentos/app-inicio.jspf" %>

<section class="encabezado-pagina">
    <div>
        <div class="antetitulo">Panel principal</div>
        <h1>Hola, <c:out value="${sessionScope.usuarioSesion.nombreCompleto}"/></h1>
        <p>Estos son los módulos que su perfil <strong><c:out value="${sessionScope.usuarioSesion.perfilesTexto}"/></strong> tiene habilitados.</p>
    </div>
</section>

<div class="row">
    <div class="col-xl-8 mb-4">
        <div class="panel">
            <c:choose>
                <c:when test="${empty menu}">
                    <p class="vacio mb-0">Su perfil todavía no tiene opciones asignadas.</p>
                </c:when>
                <c:otherwise>
                    <c:forEach var="sistema" items="${menu}">
                        <div class="sistema-titulo"><i class="bi ${sistema.icono}"></i> <c:out value="${sistema.descripcion}"/></div>
                        <div class="modulos">
                            <c:forEach var="op" items="${sistema.opciones}">
                                <a class="modulo" href="${ctx}${op.url}">
                                    <span class="icono"><i class="bi ${op.icono}"></i></span>
                                    <strong><c:out value="${op.descripcion}"/></strong>
                                    <span>Opción <c:out value="${op.codigo}"/> · <c:out value="${op.url}"/></span>
                                </a>
                            </c:forEach>
                        </div>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <div class="col-xl-4 mb-4">
        <div class="panel mb-4">
            <h2>Sesión actual</h2>
            <p class="bajada">Datos registrados en XESES_SESIO.</p>
            <div class="dato"><span>Usuario</span><strong><c:out value="${sessionScope.usuarioSesion.codigoUsuario}"/></strong></div>
            <div class="dato"><span>Sesión N.º</span><strong>${sessionScope.usuarioSesion.codigoSesion}</strong></div>
            <div class="dato"><span>Inicio</span><strong>${sessionScope.usuarioSesion.inicioSesionTexto}</strong></div>
            <div class="dato"><span>Dirección IP</span><strong><c:out value="${sessionScope.usuarioSesion.direccionIp}"/></strong></div>
            <div class="dato"><span>Acceso anterior</span><strong>${sessionScope.usuarioSesion.ultimoAccesoTexto}</strong></div>
            <div class="dato"><span>Expira por inactividad</span><strong>${Math.round(pageContext.session.maxInactiveInterval / 60)} minutos</strong></div>
        </div>

        <div class="panel">
            <h2>Últimas sesiones</h2>
            <p class="bajada">Historial de acceso de su cuenta.</p>
            <c:forEach var="s" items="${sesiones}">
                <div class="dato">
                    <span>
                        <c:out value="${s.inicio}"/><br>
                        <small>IP <c:out value="${s.ip}"/> · fin: <c:out value="${s.fin}"/></small>
                    </span>
                    <strong>
                        <c:choose>
                            <c:when test="${s.actual == 'true'}"><span class="etiqueta etiqueta-azul">Actual</span></c:when>
                            <c:when test="${s.estado == 'A'}"><span class="etiqueta etiqueta-verde">Abierta</span></c:when>
                            <c:when test="${s.estado == 'E'}"><span class="etiqueta etiqueta-ambar">Expirada</span></c:when>
                            <c:otherwise><span class="etiqueta etiqueta-gris">Cerrada</span></c:otherwise>
                        </c:choose>
                    </strong>
                </div>
            </c:forEach>
        </div>
    </div>
</div>

<%@ include file="fragmentos/app-fin.jspf" %>
</body>
</html>
