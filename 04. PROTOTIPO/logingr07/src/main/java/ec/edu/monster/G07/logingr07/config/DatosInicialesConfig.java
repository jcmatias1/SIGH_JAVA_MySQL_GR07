/*
 * SIGH - Hospital General Monster Salud
 * Talleres Login y CRUD MVC en Java - Grupo GR07
 * Datos iniciales del sistema
 */
package ec.edu.monster.G07.logingr07.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Carga los datos base del SIGH al arrancar. Las tablas las crea Hibernate
 * (spring.jpa.hibernate.ddl-auto=update) a partir de las entidades, así que el proyecto
 * funciona sin ejecutar ningún script .sql: basta con que MariaDB/MySQL esté encendido.
 *
 * Todas las sentencias usan INSERT IGNORE: si el registro ya existe no se toca, por lo que
 * se puede ejecutar en cada arranque sin duplicar ni sobrescribir datos.
 * Contraseña de los usuarios de prueba: Monster2026 (SHA-256).
 */
@Configuration
public class DatosInicialesConfig {

    private static final Logger LOG = LoggerFactory.getLogger(DatosInicialesConfig.class);

    private static final String HASH_MONSTER2026 = "eae5f19894f1c02d982e2df6434b2dcbf4844a6afa52b96bd2fac1dc6c6ede38";

    @Bean
    @Order(1)
    CommandLineRunner cargarDatosIniciales(JdbcTemplate jdbc) {
        return args -> {
            int total = 0;

            // ---------- Catálogos ----------
            total += jdbc.update("""
                    INSERT IGNORE INTO XEEST_ESTAD (XEEST_CODIGO, XEEST_DESCRI) VALUES
                     ('A', 'ACTIVO'), ('I', 'INACTIVO'), ('B', 'BLOQUEADO')
                    """);
            total += jdbc.update("""
                    INSERT IGNORE INTO PEGEN_GENER (PEGEN_CODIGO, PEGEN_DESCRI) VALUES
                     ('M', 'Masculino'), ('F', 'Femenino')
                    """);
            total += jdbc.update("""
                    INSERT IGNORE INTO PEESC_ESTCI (PEESC_CODIGO, PEESC_DESCRI) VALUES
                     ('S', 'Soltero/a'), ('C', 'Casado/a'), ('D', 'Divorciado/a'), ('V', 'Viudo/a'), ('U', 'Unión de hecho')
                    """);
            total += jdbc.update("""
                    INSERT IGNORE INTO PETSA_TIPSA (PETSA_CODIGO, PETSA_DESCRI) VALUES
                     ('A+', 'A+'), ('A-', 'A-'), ('B+', 'B+'), ('B-', 'B-'),
                     ('AB+', 'AB+'), ('AB-', 'AB-'), ('O+', 'O+'), ('O-', 'O-')
                    """);
            total += jdbc.update("""
                    INSERT IGNORE INTO PENIV_NIVEL (PENIV_CODIGO, PENIV_DESCRI) VALUES
                     ('1', 'Bachillerato'), ('2', 'Técnico / Tecnológico'), ('3', 'Tercer nivel'), ('4', 'Cuarto nivel')
                    """);
            total += jdbc.update("""
                    INSERT IGNORE INTO PEPAR_PAREN (PEPAR_CODIGO, PEPAR_DESCRI) VALUES
                     ('P', 'Padre'), ('M', 'Madre'), ('C', 'Cónyuge'), ('H', 'Hijo/a'), ('E', 'Hermano/a'), ('O', 'Otro')
                    """);

            // ---------- Departamentos y cargos (listbox del Taller 1) ----------
            total += jdbc.update("""
                    INSERT IGNORE INTO PEDEP_DEPAR (PEDEP_CODIGO, PEDEP_DESCRI) VALUES
                     ('ADM', 'Administración'), ('CEX', 'Consulta externa'), ('EME', 'Emergencia'),
                     ('HOS', 'Hospitalización'), ('LAB', 'Laboratorio'), ('IMA', 'Imagenología'),
                     ('FAR', 'Farmacia'), ('QUI', 'Quirófano'), ('FAC', 'Facturación')
                    """);
            total += jdbc.update("""
                    INSERT IGNORE INTO PECAR_CARGO (PEDEP_CODIGO, PECAR_CODIGO, PECAR_DESCRI) VALUES
                     ('ADM', 'ADS', 'Administrador de sistemas'), ('CEX', 'MED', 'Médico'),
                     ('HOS', 'ENF', 'Enfermero/a'), ('FAR', 'FAR', 'Farmacéutico/a')
                    """);

            // ---------- Empleados ----------
            total += jdbc.update("""
                    INSERT IGNORE INTO PEEMP_EMPLE (PEEMP_CODIGO, PEGEN_CODIGO, PEESC_CODIGO, PEDEP_CODIGO, PECAR_CODIGO,
                     PEEMP_CEDULA, PEEMP_APELLI, PEEMP_NOMBRE, PEEMP_FECNAC, PEEMP_EMAIL, PEEMP_TELEFO, PEEMP_DIRECC,
                     PEEMP_REGPRO, PEEMP_ESTADO) VALUES
                     ('EMP0000001', 'M', 'C', 'ADM', 'ADS', '1712345678', 'PEREZ LOPEZ', 'JUAN CARLOS', '1988-04-12',
                      'jperez@monstersalud.ec', '0991234567', 'Av. Amazonas N24-15, Quito', NULL, 'A'),
                     ('EMP0000002', 'F', 'S', 'CEX', 'MED', '1723456789', 'TORRES VACA', 'MARIA JOSE', '1990-09-03',
                      'mtorres@monstersalud.ec', '0987654321', 'Calle Rio Coca E5-20, Quito', 'MSP-17-04521', 'A'),
                     ('EMP0000003', 'F', 'C', 'HOS', 'ENF', '1734567890', 'GUERRERO PAZ', 'ANA LUCIA', '1993-01-25',
                      'aguerrero@monstersalud.ec', '0976543210', 'Av. 6 de Diciembre, Quito', 'MSP-17-08830', 'A'),
                     ('EMP0000004', 'M', 'S', 'FAR', 'FAR', '1745678901', 'ANDRADE SALAZAR', 'LUIS FERNANDO', '1985-11-30',
                      'landrade@monstersalud.ec', '0965432109', 'Sangolqui, Pichincha', 'MSP-17-01177', 'A')
                    """);

            // ---------- Seguridad: perfiles, usuarios, sistemas, opciones y permisos ----------
            total += jdbc.update("""
                    INSERT IGNORE INTO XEPER_PERFI (XEPER_CODIGO, XEPER_DESCRI, XEPER_OBSERV) VALUES
                     ('ADM', 'ADMINISTRADOR DEL SISTEMA', 'Gestiona usuarios, perfiles, opciones y auditoría'),
                     ('MED', 'MEDICO', 'Atiende consultas, emite recetas y ordena exámenes'),
                     ('ENF', 'ENFERMERO', 'Registra signos vitales y apoya la atención'),
                     ('FAR', 'FARMACEUTICO', 'Dispensa recetas y controla inventario')
                    """);
            total += jdbc.update("""
                    INSERT IGNORE INTO XEUSU_USUAR (XEUSU_CODIGO, PEEMP_CODIGO, XEEST_CODIGO, XEUSU_PASSWO, XEUSU_INTFAL,
                     XEUSU_FECCRE, XEUSU_FECMOD, XEUSU_ULTACC, XEUSU_PIEFIR) VALUES
                     ('admin', 'EMP0000001', 'A', ?, 0, NOW(), NULL, NULL, 'Administrador del SIGH'),
                     ('mtorres', 'EMP0000002', 'A', ?, 0, NOW(), NULL, NULL, 'Dra. María José Torres - Medicina General'),
                     ('aguerrero', 'EMP0000003', 'A', ?, 0, NOW(), NULL, NULL, 'Lcda. Ana Guerrero - Enfermería'),
                     ('landrade', 'EMP0000004', 'A', ?, 0, NOW(), NULL, NULL, 'Q.F. Luis Andrade - Farmacia')
                    """, HASH_MONSTER2026, HASH_MONSTER2026, HASH_MONSTER2026, HASH_MONSTER2026);
            total += jdbc.update("""
                    INSERT IGNORE INTO XEUXP_USUPE (XEUSU_CODIGO, XEPER_CODIGO, XEUXP_FECASI, XEUXP_FECRET) VALUES
                     ('admin', 'ADM', NOW(), NULL), ('mtorres', 'MED', NOW(), NULL),
                     ('aguerrero', 'ENF', NOW(), NULL), ('landrade', 'FAR', NOW(), NULL)
                    """);
            total += jdbc.update("""
                    INSERT IGNORE INTO XESIS_SISTE (XESIS_CODIGO, XESIS_DESCRI) VALUES
                     ('SEG', 'SEGURIDAD'), ('PER', 'PERSONAL')
                    """);
            total += jdbc.update("""
                    INSERT IGNORE INTO XEOPC_OPCIO (XEOPC_CODIGO, XESIS_CODIGO, XEOPC_DESCRI, XEOPC_URL) VALUES
                     ('S001', 'SEG', 'Administrar usuarios', '/seguridad/usuarios'),
                     ('S002', 'SEG', 'Administrar perfiles', '/seguridad/perfiles'),
                     ('S003', 'SEG', 'Consultar auditoría', '/seguridad/auditoria'),
                     ('P001', 'PER', 'Administrar departamentos', '/personal/departamentos'),
                     ('P002', 'PER', 'Administrar cargos', '/personal/cargos'),
                     ('P003', 'PER', 'Mantenimiento de empleados', '/personal/empleados')
                    """);
            total += jdbc.update("""
                    INSERT IGNORE INTO XEOXP_OPCPE (XEPER_CODIGO, XEOPC_CODIGO, XEOXP_FECASI, XEOXP_FECRET) VALUES
                     ('ADM', 'S001', NOW(), NULL), ('ADM', 'S002', NOW(), NULL), ('ADM', 'S003', NOW(), NULL),
                     ('ADM', 'P001', NOW(), NULL), ('ADM', 'P002', NOW(), NULL), ('ADM', 'P003', NOW(), NULL)
                    """);

            if (total > 0) {
                LOG.info("[SIGH] Datos iniciales cargados: {} registros nuevos", total);
            } else {
                LOG.info("[SIGH] Datos iniciales ya presentes, no se agregó nada");
            }
        };
    }
}
