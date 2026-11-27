<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%-- Pantalla intermedia tras un inicio de sesión correcto; redirige al menú principal --%>
<c:set var="titulo" value="Bienvenido"/>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="es">
<head>
    <%@ include file="fragmentos/head.jspf" %>
    <meta http-equiv="refresh" content="2;url=${ctx}/inicio">
</head>
<body>
<div class="bienvenida">
    <div>
        <img src="${ctx}/img/logo-sigh.svg" alt="SIGH">
        <h1>Bienvenido, <c:out value="${sessionScope.usuarioSesion.nombreCompleto}"/></h1>
        <p>Perfil: <strong><c:out value="${sessionScope.usuarioSesion.perfilesTexto}"/></strong> ·
            Último acceso: <c:out value="${sessionScope.usuarioSesion.ultimoAccesoTexto}"/></p>
        <p class="mb-0">Cargando los módulos habilitados para su perfil...</p>
        <div class="barra-carga"><span></span></div>
        <p class="mt-3"><a class="text-white" href="${ctx}/inicio">Continuar <i class="bi bi-arrow-right"></i></a></p>
    </div>
</div>
</body>
</html>
