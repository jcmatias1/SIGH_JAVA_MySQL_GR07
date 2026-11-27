<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%-- RF-SEG-003 / RF-AUD-004: un usuario sin permiso no puede abrir la opción ni escribiendo la URL --%>
<c:set var="titulo" value="Acceso denegado"/>
<!DOCTYPE html>
<html lang="es">
<head>
    <%@ include file="fragmentos/head.jspf" %>
</head>
<body data-contexto="${pageContext.request.contextPath}">
<%@ include file="fragmentos/app-inicio.jspf" %>

<div class="panel text-center py-5">
    <div class="mb-3" style="font-size:54px;color:#b42318"><i class="bi bi-shield-x"></i></div>
    <h2>No tiene permiso para esta opción</h2>
    <p class="text-suave mx-auto" style="max-width:560px">
        La opción <strong><c:out value="${opcionDenegada}"/></strong> no está asignada a su perfil.
        El intento quedó registrado en la auditoría. Si necesita acceso, solicítelo al administrador del sistema.
    </p>
    <a class="btn btn-sigh mt-2" href="${ctx}/inicio"><i class="bi bi-house-door mr-1"></i> Volver al inicio</a>
</div>

<%@ include file="fragmentos/app-fin.jspf" %>
</body>
</html>
