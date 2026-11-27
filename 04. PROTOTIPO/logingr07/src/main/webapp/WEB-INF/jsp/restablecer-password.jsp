<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%-- ECUD1 - RECUPERAR CONTRASEÑA: el usuario abre el enlace e ingresa la nueva contraseña dos veces --%>
<c:set var="titulo" value="Restablecer contraseña"/>
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
            <h2 class="acceso-titulo">Defina una nueva contraseña</h2>
            <p class="acceso-texto">La contraseña se guarda cifrada con SHA-256. Al cambiarla, la cuenta queda
                desbloqueada y el enlace deja de funcionar.</p>
        </div>
        <div class="acceso-pie">Talleres Login y CRUD MVC con Java · Grupo GR07</div>
    </section>

    <section class="acceso-form">
        <div class="tarjeta-acceso">
            <c:choose>
                <c:when test="${tokenValido}">
                    <h1>Nueva contraseña</h1>
                    <p class="bajada">Escríbala dos veces para confirmarla.</p>
                    <c:if test="${not empty error}">
                        <div class="aviso aviso-error" role="alert"><i class="bi bi-exclamation-octagon"></i><span>${error}</span></div>
                    </c:if>
                    <form method="post" action="${ctx}/restablecer-password" id="formRestablecer" novalidate>
                        <input type="hidden" name="token" value="<c:out value='${token}'/>">
                        <div class="form-group">
                            <label for="password">Nueva contraseña</label>
                            <div class="campo">
                                <i class="bi bi-key"></i>
                                <input class="form-control" id="password" name="password" type="password" maxlength="64" autocomplete="new-password" required autofocus>
                            </div>
                            <ul class="politica" id="politica">
                                <li data-regla="largo"><i class="bi bi-circle"></i> Al menos 8 caracteres</li>
                                <li data-regla="letra"><i class="bi bi-circle"></i> Al menos una letra</li>
                                <li data-regla="numero"><i class="bi bi-circle"></i> Al menos un número</li>
                            </ul>
                        </div>
                        <div class="form-group">
                            <label for="confirmacion">Confirmar contraseña</label>
                            <div class="campo">
                                <i class="bi bi-key-fill"></i>
                                <input class="form-control" id="confirmacion" name="confirmacion" type="password" maxlength="64" autocomplete="new-password" required>
                            </div>
                        </div>
                        <button class="btn btn-sigh btn-ingresar" type="submit">
                            <i class="bi bi-check2-circle mr-1"></i> Guardar contraseña
                        </button>
                    </form>
                </c:when>
                <c:otherwise>
                    <h1>Enlace no válido</h1>
                    <div class="aviso aviso-alerta" role="alert">
                        <i class="bi bi-clock-history"></i>
                        <span>El enlace de restablecimiento venció, ya fue usado o no existe. Solicite uno nuevo.</span>
                    </div>
                    <a class="btn btn-sigh btn-ingresar" href="${ctx}/recuperar-password">
                        <i class="bi bi-arrow-repeat mr-1"></i> Solicitar un nuevo enlace
                    </a>
                </c:otherwise>
            </c:choose>
            <div class="text-center mt-3">
                <a class="enlace-recuperar" href="${ctx}/login"><i class="bi bi-arrow-left"></i> Volver a iniciar sesión</a>
            </div>
        </div>
    </section>
</div>
<script src="${ctx}/lib/sweetalert2/sweetalert2.all.min.js"></script>
<script>
    (function () {
        const clave = document.getElementById("password");
        if (!clave) { return; }
        const reglas = {
            largo: function (v) { return v.length >= 8; },
            letra: function (v) { return /[A-Za-zÁÉÍÓÚáéíóúÑñ]/.test(v); },
            numero: function (v) { return /\d/.test(v); }
        };
        function revisar() {
            let cumple = true;
            document.querySelectorAll("#politica li").forEach(function (li) {
                const ok = reglas[li.getAttribute("data-regla")](clave.value);
                li.classList.toggle("ok", ok);
                li.querySelector("i").className = ok ? "bi bi-check-circle-fill" : "bi bi-circle";
                cumple = cumple && ok;
            });
            return cumple;
        }
        clave.addEventListener("input", revisar);
        document.getElementById("formRestablecer").addEventListener("submit", function (evento) {
            const confirmacion = document.getElementById("confirmacion").value;
            let mensaje = null;
            if (!revisar()) {
                mensaje = "La contraseña no cumple la política del hospital.";
            } else if (clave.value !== confirmacion) {
                mensaje = "Las contraseñas no coinciden.";
            }
            if (mensaje) {
                evento.preventDefault();
                Swal.fire({ icon: "warning", title: "Revise la contraseña", text: mensaje, confirmButtonColor: "#0b5cad" });
            }
        });
    })();
</script>
</body>
</html>
