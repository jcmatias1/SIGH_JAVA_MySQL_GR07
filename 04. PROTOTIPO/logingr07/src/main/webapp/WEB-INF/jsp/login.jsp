<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%-- ECUD1 Iniciar sesión: formulario de usuario y contraseña del SIGH --%>
<c:set var="titulo" value="Iniciar sesión"/>
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
            <h2 class="acceso-titulo">Sistema Integral de Gestión Hospitalaria</h2>
            <p class="acceso-texto">Ingrese con su cuenta institucional. Cada usuario ve únicamente los módulos
                que su perfil tiene habilitados.</p>
            <div class="acceso-puntos">
                <div class="acceso-punto">
                    <i class="bi bi-shield-lock"></i>
                    <div><strong>Acceso por perfil</strong><span>Usuarios, perfiles y auditoría según su rol.</span></div>
                </div>
                <div class="acceso-punto">
                    <i class="bi bi-hourglass-split"></i>
                    <div><strong>Sesión protegida</strong><span>Se cierra tras 15 minutos de inactividad.</span></div>
                </div>
                <div class="acceso-punto">
                    <i class="bi bi-journal-text"></i>
                    <div><strong>Auditoría</strong><span>Cada ingreso, intento fallido y cierre queda registrado.</span></div>
                </div>
            </div>
        </div>

        <div class="acceso-pie">Talleres Login y CRUD MVC con Java · Grupo GR07</div>
    </section>

    <section class="acceso-form">
        <div class="tarjeta-acceso">
            <h1>Iniciar sesión</h1>
            <p class="bajada">Ingrese su usuario y contraseña del SIGH.</p>

            <c:if test="${not empty error}">
                <div class="aviso ${tipoError == 'BLOQUEADO' || tipoError == 'INACTIVO' ? 'aviso-alerta' : 'aviso-error'}" role="alert">
                    <i class="bi ${tipoError == 'BLOQUEADO' ? 'bi-lock' : 'bi-exclamation-octagon'}"></i>
                    <span><c:out value="${error}"/></span>
                </div>
            </c:if>
            <c:if test="${not empty aviso}">
                <div class="aviso aviso-alerta" role="status"><i class="bi bi-clock-history"></i><span>${aviso}</span></div>
            </c:if>
            <c:if test="${not empty info}">
                <div class="aviso aviso-info" role="status"><i class="bi bi-check-circle"></i><span>${info}</span></div>
            </c:if>

            <form method="post" action="${ctx}/login" id="formLogin" autocomplete="on" novalidate>
                <div class="form-group">
                    <label for="codigoUsuario">Usuario</label>
                    <div class="campo">
                        <i class="bi bi-person"></i>
                        <input class="form-control" id="codigoUsuario" name="codigoUsuario" maxlength="20"
                               value="<c:out value='${codigoUsuario}'/>" placeholder="Ej.: admin" autocomplete="username" autofocus required>
                    </div>
                </div>
                <div class="form-group">
                    <label for="password">Contraseña</label>
                    <div class="campo">
                        <i class="bi bi-key"></i>
                        <input class="form-control" id="password" name="password" type="password" maxlength="64"
                               placeholder="Su contraseña" autocomplete="current-password" required>
                        <button class="ver-clave" type="button" id="verClave" aria-label="Mostrar contraseña">
                            <i class="bi bi-eye"></i>
                        </button>
                    </div>
                </div>
                <div class="d-flex justify-content-end mb-3">
                    <a class="enlace-recuperar" href="${ctx}/recuperar-password">
                        <i class="bi bi-question-circle"></i> ¿Olvidó su contraseña?
                    </a>
                </div>
                <button class="btn btn-sigh btn-ingresar" type="submit" id="btnIngresar">
                    <i class="bi bi-box-arrow-in-right mr-1"></i> Ingresar
                </button>
            </form>
        </div>
    </section>
</div>

<script src="${ctx}/lib/sweetalert2/sweetalert2.all.min.js"></script>
<script>
    document.getElementById("verClave").addEventListener("click", function () {
        const clave = document.getElementById("password");
        const visible = clave.type === "text";
        clave.type = visible ? "password" : "text";
        this.innerHTML = visible ? '<i class="bi bi-eye"></i>' : '<i class="bi bi-eye-slash"></i>';
        this.setAttribute("aria-label", visible ? "Mostrar contraseña" : "Ocultar contraseña");
    });

    document.getElementById("formLogin").addEventListener("submit", function (evento) {
        const usuario = document.getElementById("codigoUsuario").value.trim();
        const clave = document.getElementById("password").value;
        if (!usuario || !clave) {
            evento.preventDefault();
            Swal.fire({
                icon: "warning",
                title: "Datos incompletos",
                text: "Ingrese su usuario y su contraseña.",
                confirmButtonColor: "#0b5cad"
            });
            return;
        }
        const boton = document.getElementById("btnIngresar");
        boton.disabled = true;
        boton.innerHTML = '<i class="bi bi-arrow-repeat girando mr-1"></i> Validando...';
    });
</script>
</body>
</html>
