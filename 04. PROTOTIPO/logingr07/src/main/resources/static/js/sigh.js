/* SIGH - utilidades comunes de las vistas JSP (Taller Login MVC Java GR07) */
(function () {
    "use strict";

    const base = document.body.getAttribute("data-contexto") || "";
    let maximo = 900;
    let restante = 900;

    /** Escapa texto antes de insertarlo como HTML (evita inyección de código en las tablas). */
    function esc(valor) {
        return String(valor === null || valor === undefined ? "" : valor)
            .replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;")
            .replace(/"/g, "&quot;").replace(/'/g, "&#39;");
    }

    /** fetch con JSON: si la sesión expiró vuelve al login; si hay error lanza el mensaje del servidor. */
    async function api(url, opciones) {
        const config = Object.assign({ headers: { "Accept": "application/json" } }, opciones || {});
        if (config.body && typeof config.body !== "string") {
            config.body = JSON.stringify(config.body);
            config.headers["Content-Type"] = "application/json";
        }
        const respuesta = await fetch(base + url, config);
        if (respuesta.status === 401) {
            window.location.href = base + "/login?expirada";
            throw new Error("Sesión expirada");
        }
        let datos = null;
        try { datos = await respuesta.json(); } catch (e) { datos = null; }
        restante = maximo; // cada petición al servidor renueva la sesión
        if (!respuesta.ok) {
            throw new Error((datos && datos.mensaje) || "No se pudo completar la operación.");
        }
        return datos;
    }

    function fecha(valor) {
        if (!valor) { return "—"; }
        const d = new Date(valor);
        if (isNaN(d.getTime())) { return esc(valor); }
        return d.toLocaleDateString("es-EC", { day: "2-digit", month: "2-digit", year: "numeric" }) + " " +
            d.toLocaleTimeString("es-EC", { hour: "2-digit", minute: "2-digit" });
    }

    function exito(titulo, texto) {
        return Swal.fire({ icon: "success", title: titulo, text: texto || "", confirmButtonColor: "#0b5cad", timer: 2200 });
    }

    function error(texto) {
        return Swal.fire({ icon: "error", title: "Operación no completada", text: texto, confirmButtonColor: "#0b5cad" });
    }

    /** RF-SEG-004: cuenta regresiva de inactividad; al llegar a cero vuelve al login. */
    function iniciarTemporizador() {
        const reloj = document.getElementById("temporizador");
        if (!reloj) { return; }
        maximo = parseInt(reloj.getAttribute("data-segundos"), 10) || 900;
        restante = maximo;
        const texto = reloj.querySelector("span");
        const tick = function () {
            const m = Math.floor(restante / 60), s = restante % 60;
            texto.textContent = m + ":" + String(s).padStart(2, "0");
            reloj.classList.toggle("urgente", restante <= 60);
            if (restante <= 0) {
                window.location.href = base + "/login?expirada";
                return;
            }
            restante--;
        };
        tick();
        setInterval(tick, 1000);
    }

    document.addEventListener("DOMContentLoaded", iniciarTemporizador);

    window.SIGH = { base: base, esc: esc, api: api, fecha: fecha, exito: exito, error: error };
})();

/* Al corregir un campo de un formulario SweetAlert2 se oculta el mensaje de validación anterior. */
document.addEventListener("input", function (evento) {
    if (window.Swal && evento.target.closest && evento.target.closest(".swal2-popup")) {
        Swal.resetValidationMessage();
    }
});
