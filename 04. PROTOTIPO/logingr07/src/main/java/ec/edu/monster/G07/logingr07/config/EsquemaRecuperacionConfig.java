/*
 * SIGH - Hospital General Monster Salud
 * Talleres Login y CRUD MVC en Java - Grupo GR07
 * Subsistema de Seguridad
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
 * Prepara la base del taller ORM (bd_monster_gr07) para el Login sin perder datos:
 * crea la tabla de enlaces de recuperación XEREC_RECUP si no existe y agrega dos empleados
 * sin cuenta para probar «Nuevo usuario». Si se ejecutó BD_MONSTER_GR07_LOGIN.sql no cambia nada.
 */
@Configuration
public class EsquemaRecuperacionConfig {

    private static final Logger LOG = LoggerFactory.getLogger(EsquemaRecuperacionConfig.class);

    @Bean
    @Order(2)
    CommandLineRunner prepararEsquemaLogin(JdbcTemplate jdbc) {
        return args -> {
            jdbc.execute("""
                    CREATE TABLE IF NOT EXISTS XEREC_RECUP (
                       XEREC_CODIGO   BIGINT NOT NULL AUTO_INCREMENT,
                       XEUSU_CODIGO   VARCHAR(20) NOT NULL,
                       XEREC_TOKEN    VARCHAR(120) NOT NULL,
                       XEREC_FECCRE   DATETIME NOT NULL,
                       XEREC_FECEXP   DATETIME NOT NULL,
                       XEREC_USADO    BOOLEAN NOT NULL DEFAULT FALSE,
                       PRIMARY KEY (XEREC_CODIGO),
                       UNIQUE KEY AK_XEREC_TOKEN (XEREC_TOKEN),
                       CONSTRAINT FK_USU_REC FOREIGN KEY (XEUSU_CODIGO)
                          REFERENCES XEUSU_USUAR (XEUSU_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT
                    ) ENGINE=InnoDB
                    """);
            int nuevos = jdbc.update("""
                    INSERT IGNORE INTO PEEMP_EMPLE (PEEMP_CODIGO, PEGEN_CODIGO, PEESC_CODIGO, PEDEP_CODIGO, PECAR_CODIGO,
                     PEEMP_CEDULA, PEEMP_APELLI, PEEMP_NOMBRE, PEEMP_FECNAC, PEEMP_EMAIL, PEEMP_TELEFO, PEEMP_DIRECC,
                     PEEMP_REGPRO, PEEMP_ESTADO) VALUES
                     ('EMP0000005', 'F', 'S', 'CEX', 'MED', '1756789012', 'MORALES CEVALLOS', 'DIANA CAROLINA', '1991-06-14',
                      'dmorales@monstersalud.ec', '0954321098', 'Cumbaya, Quito', 'MSP-17-06612', 'A'),
                     ('EMP0000006', 'M', 'C', 'HOS', 'ENF', '1767890123', 'CHAVEZ ORTIZ', 'PABLO ANDRES', '1995-02-08',
                      'pchavez@monstersalud.ec', '0943210987', 'Conocoto, Quito', 'MSP-17-09105', 'A')
                    """);
            if (nuevos > 0) {
                LOG.info("[SIGH] Se agregaron {} empleados sin cuenta para el taller de Login", nuevos);
            }
        };
    }
}
