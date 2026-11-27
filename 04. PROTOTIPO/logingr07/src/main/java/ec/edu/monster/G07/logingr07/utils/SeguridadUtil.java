/*
 * SIGH - Hospital General Monster Salud
 * Talleres Login y CRUD MVC en Java - Grupo GR07
 * Subsistema de Seguridad
 */
package ec.edu.monster.G07.logingr07.utils;

import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HexFormat;

/**
 * Utilidades de seguridad: hash SHA-256 de contraseñas, política de contraseñas,
 * generación de tokens y obtención de la IP del cliente.
 */
public final class SeguridadUtil {

    private static final SecureRandom ALEATORIO = new SecureRandom();

    private SeguridadUtil() {
    }

    /** Devuelve el hash SHA-256 (64 caracteres hexadecimales) del texto recibido. */
    public static String sha256(String texto) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(texto.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("No se pudo inicializar SHA-256", ex);
        }
    }

    /** Compara dos hashes en tiempo constante para no filtrar información por tiempos. */
    public static boolean coincide(String passwordPlano, String hashGuardado) {
        if (passwordPlano == null || hashGuardado == null) {
            return false;
        }
        return MessageDigest.isEqual(
                sha256(passwordPlano).getBytes(StandardCharsets.UTF_8),
                hashGuardado.toLowerCase().getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Política de contraseñas del hospital: mínimo 8 caracteres, al menos una letra
     * y al menos un número. Devuelve null si cumple o el mensaje del error.
     */
    public static String validarPolitica(String password) {
        if (password == null || password.length() < 8) {
            return "La contraseña debe tener al menos 8 caracteres.";
        }
        if (password.length() > 64) {
            return "La contraseña no puede superar 64 caracteres.";
        }
        if (!password.matches(".*[A-Za-zÁÉÍÓÚáéíóúÑñ].*")) {
            return "La contraseña debe incluir al menos una letra.";
        }
        if (!password.matches(".*\\d.*")) {
            return "La contraseña debe incluir al menos un número.";
        }
        return null;
    }

    /** Token aleatorio de 64 caracteres hexadecimales (256 bits) para los enlaces de recuperación. */
    public static String generarToken() {
        byte[] bytes = new byte[32];
        ALEATORIO.nextBytes(bytes);
        return HexFormat.of().formatHex(bytes);
    }

    /** IP del cliente recortada al tamaño de la columna XEAUD_DIRIP / XESES_DIRIP (45). */
    public static String ipCliente(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        if (ip == null) {
            return null;
        }
        if ("0:0:0:0:0:0:0:1".equals(ip)) {
            ip = "127.0.0.1";
        }
        return ip.length() > 45 ? ip.substring(0, 45) : ip;
    }
}
