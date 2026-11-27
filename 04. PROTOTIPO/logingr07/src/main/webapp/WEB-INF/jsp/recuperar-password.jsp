<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%-- ECUD1 - RECUPERAR CONTRASEÑA: el usuario ingresa su usuario o su correo institucional --%>
<c:set var="titulo" value="Recuperar contraseña"/>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="es">
<head>
    <%@ include file="fragmentos/head.jspf" %>
</head>
<body data-contexto="${ctx}">
<div class="acceso">
    <section class="acceso-marca">
        <div class="marca">
            <img src="${ctx}/img/logo-sigh.svg" alt="Logotipo SIGH">
            <div>
                <div class="marca-nombre">SIGH</div>
                <div class="marca-sub">Hospital General Monster Salud</div>
            </div>
        </div>
        <div>
            <h2 class="acceso-titulo">Recupere el acceso a su cuenta</h2>
            <p class="acceso-texto">Le enviaremos un enlace temporal a su correo institucional. El enlace vence en
                30 minutos y solo puede usarse una vez. El SIGH nunca muestra ni envía su contraseña actual.</p>
        </div>
        <div class="acceso-pie">Talleres Login y CRUD MVC con Java · Grupo GR07</div>
    </section>

    <section class="acceso-form">
        <div class="tarjeta-acceso">
            <h1>¿Olvidó su contraseña?</h1>
            <p class="bajada">Ingrese su usuario o su correo institucional.</p>

            <c:if test="${not empty error}">
                <div class="aviso aviso-error" role="alert"><i class="bi bi-exclamation-octagon"></i><span>${error}</span></div>
            </c:if>
            <c:if test="${not empty mensaje}">
                <div class="aviso aviso-info" role="status"><i class="bi bi-envelope-check"></i><span>${mensaje}</span></div>
            </c:if>
            <c:if test="${not empty enlaceDemo}">
                <div class="aviso aviso-demo">
                    <i class="bi bi-info-circle"></i>
                    <span><strong>Modo taller:</strong> el correo no está configurado, así que el enlace también se muestra aquí
                        y en la consola de NetBeans:<br>
                        <a href="<c:out value='${enlaceDemo}'/>"><c:out value="${enlaceDemo}"/></a></span>
                </div>
            </c:if>

            <form method="post" action="${ctx}/recuperar-password" id="formRecuperar" novalidate>
                <div class="form-group">
                    <label for="identificador">Usuario o correo institucional</label>
                    <div class="campo">
                        <i class="bi bi-envelope-at"></i>
                        <input class="form-control" id="identificador" name="identificador" maxlength="100"
                               value="<c:out value='${identificador}'/>" placeholder="Ej.: mtorres o mtorres@monstersalud.ec" required autofocus>
                    </div>
                </div>
                <button class="btn btn-sigh btn-ingresar" type="submit">
                    <i class="bi bi-send mr-1"></i> Enviar enlace
                </button>
            </form>
            <div class="text-center mt-3">
                <a class="enlace-recuperar" href="${ctx}/login"><i class="bi bi-arrow-left"></i> Volver a iniciar sesión</a>
            </div>
        </div>
    </section>
</div>
<script src="${ctx}/lib/sweetalert2/sweetalert2.all.min.js"></script>
<script>
    document.getElementById("formRecuperar").addEventListener("submit", function (evento) {
        if (!document.getElementById("identificador").value.trim()) {
            evento.preventDefault();
            Swal.fire({ icon: "warning", title: "Dato requerido", text: "Ingrese su usuario o su correo institucional.", confirmButtonColor: "#0b5cad" });
        }
    });
</script>
</body>
</html>
