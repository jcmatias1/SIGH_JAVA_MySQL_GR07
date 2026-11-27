/*==============================================================*/
/* SIGH - Hospital General Monster Salud                        */
/* Taller Login MVC en Java - Grupo GR07                        */
/* Base de datos del Subsistema de Seguridad + recuperación     */
/* Tablas tomadas del modelo físico SIGH_GR7_FISICO_MySQL       */
/* Ejecutar en phpMyAdmin (XAMPP) o en MySQL Workbench          */
/*==============================================================*/

DROP DATABASE IF EXISTS bd_monster_gr07;
CREATE DATABASE bd_monster_gr07 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci;
USE bd_monster_gr07;

/*==============================================================*/
/* Table: XEEST_ESTAD (ESTADO)                                  */
/*==============================================================*/
CREATE TABLE XEEST_ESTAD
(
   XEEST_CODIGO         CHAR(1) NOT NULL,
   XEEST_DESCRI         VARCHAR(30) NOT NULL,
   PRIMARY KEY (XEEST_CODIGO)
) ENGINE=InnoDB;

/*==============================================================*/
/* Table: PEEMP_EMPLE (EMPLEADO)                                */
/* PEGEN, PEESC, PEDEP y PECAR son códigos del subsistema de    */
/* Personal; en esta base del taller se guardan como columnas.  */
/*==============================================================*/
CREATE TABLE PEEMP_EMPLE
(
   PEEMP_CODIGO         CHAR(10) NOT NULL,
   PEGEN_CODIGO         CHAR(1) NOT NULL,
   PEESC_CODIGO         CHAR(1),
   PEDEP_CODIGO         CHAR(3) NOT NULL,
   PECAR_CODIGO         CHAR(3) NOT NULL,
   PEEMP_CEDULA         VARCHAR(13) NOT NULL,
   PEEMP_APELLI         VARCHAR(60) NOT NULL,
   PEEMP_NOMBRE         VARCHAR(60) NOT NULL,
   PEEMP_FECNAC         DATE NOT NULL,
   PEEMP_EMAIL          VARCHAR(100) NOT NULL,
   PEEMP_TELEFO         VARCHAR(15),
   PEEMP_DIRECC         VARCHAR(150),
   PEEMP_REGPRO         VARCHAR(30),
   PEEMP_ESTADO         CHAR(1) NOT NULL,
   PRIMARY KEY (PEEMP_CODIGO)
) ENGINE=InnoDB;

/*==============================================================*/
/* Table: XEUSU_USUAR (USUARIO)                                 */
/*==============================================================*/
CREATE TABLE XEUSU_USUAR
(
   XEUSU_CODIGO         VARCHAR(20) NOT NULL,
   PEEMP_CODIGO         CHAR(10) NOT NULL,
   XEEST_CODIGO         CHAR(1) NOT NULL,
   XEUSU_PASSWO         VARCHAR(255) NOT NULL,
   XEUSU_INTFAL         SMALLINT NOT NULL,
   XEUSU_FECCRE         DATETIME NOT NULL,
   XEUSU_FECMOD         DATETIME,
   XEUSU_ULTACC         DATETIME,
   XEUSU_PIEFIR         VARCHAR(100),
   PRIMARY KEY (XEUSU_CODIGO)
) ENGINE=InnoDB;

/*==============================================================*/
/* Table: XEPER_PERFI (PERFIL)                                  */
/*==============================================================*/
CREATE TABLE XEPER_PERFI
(
   XEPER_CODIGO         CHAR(3) NOT NULL,
   XEPER_DESCRI         VARCHAR(60) NOT NULL,
   XEPER_OBSERV         VARCHAR(200),
   PRIMARY KEY (XEPER_CODIGO)
) ENGINE=InnoDB;

/*==============================================================*/
/* Table: XESIS_SISTE (SISTEMA)                                 */
/*==============================================================*/
CREATE TABLE XESIS_SISTE
(
   XESIS_CODIGO         CHAR(3) NOT NULL,
   XESIS_DESCRI         VARCHAR(60) NOT NULL,
   PRIMARY KEY (XESIS_CODIGO)
) ENGINE=InnoDB;

/*==============================================================*/
/* Table: XEOPC_OPCIO (OPCION)                                  */
/*==============================================================*/
CREATE TABLE XEOPC_OPCIO
(
   XEOPC_CODIGO         CHAR(4) NOT NULL,
   XESIS_CODIGO         CHAR(3) NOT NULL,
   XEOPC_DESCRI         VARCHAR(80) NOT NULL,
   XEOPC_URL            VARCHAR(150),
   PRIMARY KEY (XEOPC_CODIGO)
) ENGINE=InnoDB;

/*==============================================================*/
/* Table: XEUXP_USUPE (USUPER)                                  */
/*==============================================================*/
CREATE TABLE XEUXP_USUPE
(
   XEUSU_CODIGO         VARCHAR(20) NOT NULL,
   XEPER_CODIGO         CHAR(3) NOT NULL,
   XEUXP_FECASI         DATETIME NOT NULL,
   XEUXP_FECRET         DATETIME,
   PRIMARY KEY (XEUSU_CODIGO, XEPER_CODIGO)
) ENGINE=InnoDB;

/*==============================================================*/
/* Table: XEOXP_OPCPE (OPC_PER)                                 */
/*==============================================================*/
CREATE TABLE XEOXP_OPCPE
(
   XEPER_CODIGO         CHAR(3) NOT NULL,
   XEOPC_CODIGO         CHAR(4) NOT NULL,
   XEOXP_FECASI         DATETIME NOT NULL,
   XEOXP_FECRET         DATETIME,
   PRIMARY KEY (XEPER_CODIGO, XEOPC_CODIGO)
) ENGINE=InnoDB;

/*==============================================================*/
/* Table: XESES_SESIO (SESION)                                  */
/*==============================================================*/
CREATE TABLE XESES_SESIO
(
   XESES_CODIGO         BIGINT NOT NULL AUTO_INCREMENT,
   XEUSU_CODIGO         VARCHAR(20) NOT NULL,
   XESES_FECINI         DATETIME NOT NULL,
   XESES_FECFIN         DATETIME,
   XESES_DIRIP          VARCHAR(45),
   XESES_ESTADO         CHAR(1) NOT NULL,
   PRIMARY KEY (XESES_CODIGO)
) ENGINE=InnoDB;

/*==============================================================*/
/* Table: XEAUD_AUDIT (AUDITORIA)                               */
/*==============================================================*/
CREATE TABLE XEAUD_AUDIT
(
   XEAUD_CODIGO         BIGINT NOT NULL AUTO_INCREMENT,
   XEUSU_CODIGO         VARCHAR(20),
   XEAUD_FECHOR         DATETIME NOT NULL,
   XEAUD_MODULO         VARCHAR(40) NOT NULL,
   XEAUD_OPERAC         VARCHAR(40) NOT NULL,
   XEAUD_RESULT         VARCHAR(15) NOT NULL,
   XEAUD_DETALL         VARCHAR(500),
   XEAUD_DIRIP          VARCHAR(45),
   PRIMARY KEY (XEAUD_CODIGO)
) ENGINE=InnoDB;

/*==============================================================*/
/* Table: XEREC_RECUP (TOKEN DE RECUPERACION) - nueva en Login  */
/* Enlaces temporales de un solo uso para RF-SEG-005            */
/*==============================================================*/
CREATE TABLE XEREC_RECUP
(
   XEREC_CODIGO         BIGINT NOT NULL AUTO_INCREMENT,
   XEUSU_CODIGO         VARCHAR(20) NOT NULL,
   XEREC_TOKEN          VARCHAR(120) NOT NULL,
   XEREC_FECCRE         DATETIME NOT NULL,
   XEREC_FECEXP         DATETIME NOT NULL,
   XEREC_USADO          BOOLEAN NOT NULL DEFAULT FALSE,
   PRIMARY KEY (XEREC_CODIGO),
   UNIQUE KEY AK_XEREC_TOKEN (XEREC_TOKEN)
) ENGINE=InnoDB;

/*==============================================================*/
/* Llaves foráneas del subsistema de seguridad                  */
/*==============================================================*/
ALTER TABLE XEUSU_USUAR ADD CONSTRAINT FK_EMP_USU FOREIGN KEY (PEEMP_CODIGO)
      REFERENCES PEEMP_EMPLE (PEEMP_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE XEUSU_USUAR ADD CONSTRAINT FK_EST_USU FOREIGN KEY (XEEST_CODIGO)
      REFERENCES XEEST_ESTAD (XEEST_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE XEOPC_OPCIO ADD CONSTRAINT FK_SIS_OPC FOREIGN KEY (XESIS_CODIGO)
      REFERENCES XESIS_SISTE (XESIS_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE XEUXP_USUPE ADD CONSTRAINT FK_USU_UXP FOREIGN KEY (XEUSU_CODIGO)
      REFERENCES XEUSU_USUAR (XEUSU_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE XEUXP_USUPE ADD CONSTRAINT FK_PER_UXP FOREIGN KEY (XEPER_CODIGO)
      REFERENCES XEPER_PERFI (XEPER_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE XEOXP_OPCPE ADD CONSTRAINT FK_PER_OXP FOREIGN KEY (XEPER_CODIGO)
      REFERENCES XEPER_PERFI (XEPER_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE XEOXP_OPCPE ADD CONSTRAINT FK_OPC_OXP FOREIGN KEY (XEOPC_CODIGO)
      REFERENCES XEOPC_OPCIO (XEOPC_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE XESES_SESIO ADD CONSTRAINT FK_USU_SES FOREIGN KEY (XEUSU_CODIGO)
      REFERENCES XEUSU_USUAR (XEUSU_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE XEAUD_AUDIT ADD CONSTRAINT FK_USU_AUD FOREIGN KEY (XEUSU_CODIGO)
      REFERENCES XEUSU_USUAR (XEUSU_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE XEREC_RECUP ADD CONSTRAINT FK_USU_REC FOREIGN KEY (XEUSU_CODIGO)
      REFERENCES XEUSU_USUAR (XEUSU_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;

/*==============================================================*/
/* Datos de prueba                                              */
/*==============================================================*/
INSERT INTO XEEST_ESTAD VALUES
 ('A', 'ACTIVO'),
 ('I', 'INACTIVO'),
 ('B', 'BLOQUEADO');

INSERT INTO PEEMP_EMPLE VALUES
 ('EMP0000001', 'M', 'C', 'ADM', 'ADS', '1712345678', 'PEREZ LOPEZ',    'JUAN CARLOS',  '1988-04-12', 'jperez@monstersalud.ec',   '0991234567', 'Av. Amazonas N24-15, Quito', NULL,          'A'),
 ('EMP0000002', 'F', 'S', 'CEX', 'MED', '1723456789', 'TORRES VACA',    'MARIA JOSE',   '1990-09-03', 'mtorres@monstersalud.ec',  '0987654321', 'Calle Rio Coca E5-20, Quito','MSP-17-04521', 'A'),
 ('EMP0000003', 'F', 'C', 'HOS', 'ENF', '1734567890', 'GUERRERO PAZ',   'ANA LUCIA',    '1993-01-25', 'aguerrero@monstersalud.ec','0976543210', 'Av. 6 de Diciembre, Quito',  'MSP-17-08830', 'A'),
 ('EMP0000004', 'M', 'S', 'FAR', 'FAR', '1745678901', 'ANDRADE SALAZAR','LUIS FERNANDO','1985-11-30', 'landrade@monstersalud.ec', '0965432109', 'Sangolqui, Pichincha',       'MSP-17-01177', 'A'),
 /* Empleados sin cuenta: sirven para probar «Nuevo usuario» (ECUD2) */
 ('EMP0000005', 'F', 'S', 'CEX', 'MED', '1756789012', 'MORALES CEVALLOS','DIANA CAROLINA','1991-06-14', 'dmorales@monstersalud.ec', '0954321098', 'Cumbaya, Quito',             'MSP-17-06612', 'A'),
 ('EMP0000006', 'M', 'C', 'HOS', 'ENF', '1767890123', 'CHAVEZ ORTIZ',    'PABLO ANDRES',  '1995-02-08', 'pchavez@monstersalud.ec',  '0943210987', 'Conocoto, Quito',            'MSP-17-09105', 'A');

/* Contraseña de todos los usuarios de prueba: Monster2026 (SHA-256) */
INSERT INTO XEUSU_USUAR VALUES
 ('admin',    'EMP0000001', 'A', 'eae5f19894f1c02d982e2df6434b2dcbf4844a6afa52b96bd2fac1dc6c6ede38', 0, '2026-10-26 08:00:00', NULL, '2026-11-04 08:15:00', 'Administrador del SIGH'),
 ('mtorres',  'EMP0000002', 'A', 'eae5f19894f1c02d982e2df6434b2dcbf4844a6afa52b96bd2fac1dc6c6ede38', 0, '2026-10-27 09:30:00', NULL, '2026-11-04 10:02:00', 'Dra. María José Torres - Medicina General'),
 ('aguerrero','EMP0000003', 'A', 'eae5f19894f1c02d982e2df6434b2dcbf4844a6afa52b96bd2fac1dc6c6ede38', 1, '2026-10-27 10:00:00', NULL, '2026-11-03 19:40:00', 'Lcda. Ana Guerrero - Enfermería'),
 ('landrade', 'EMP0000004', 'B', 'eae5f19894f1c02d982e2df6434b2dcbf4844a6afa52b96bd2fac1dc6c6ede38', 3, '2026-10-28 11:00:00', '2026-11-02 16:20:00', NULL, 'Q.F. Luis Andrade - Farmacia');

INSERT INTO XEPER_PERFI VALUES
 ('ADM', 'ADMINISTRADOR DEL SISTEMA', 'Gestiona usuarios, perfiles, opciones y auditoría'),
 ('MED', 'MEDICO',                    'Atiende consultas, emite recetas y ordena exámenes'),
 ('ENF', 'ENFERMERO',                 'Registra signos vitales y apoya la atención'),
 ('FAR', 'FARMACEUTICO',              'Dispensa recetas y controla inventario');

INSERT INTO XESIS_SISTE VALUES
 ('SEG', 'SEGURIDAD');

INSERT INTO XEOPC_OPCIO VALUES
 ('S001', 'SEG', 'Administrar usuarios',        '/seguridad/usuarios'),
 ('S002', 'SEG', 'Administrar perfiles',        '/seguridad/perfiles'),
 ('S003', 'SEG', 'Consultar auditoría',         '/seguridad/auditoria');

INSERT INTO XEUXP_USUPE VALUES
 ('admin',     'ADM', '2026-10-26 08:00:00', NULL),
 ('mtorres',   'MED', '2026-10-27 09:30:00', NULL),
 ('aguerrero', 'ENF', '2026-10-27 10:00:00', NULL),
 ('landrade',  'FAR', '2026-10-28 11:00:00', NULL);

INSERT INTO XEOXP_OPCPE VALUES
 ('ADM', 'S001', '2026-10-26 08:00:00', NULL),
 ('ADM', 'S002', '2026-10-26 08:00:00', NULL),
 ('ADM', 'S003', '2026-10-26 08:00:00', NULL);

INSERT INTO XESES_SESIO (XEUSU_CODIGO, XESES_FECINI, XESES_FECFIN, XESES_DIRIP, XESES_ESTADO) VALUES
 ('admin',     '2026-11-04 08:15:00', '2026-11-04 12:00:00', '192.168.1.10', 'C'),
 ('mtorres',   '2026-11-04 10:02:00', NULL,                  '192.168.1.25', 'A'),
 ('aguerrero', '2026-11-03 19:40:00', '2026-11-03 23:55:00', '192.168.1.31', 'C');

INSERT INTO XEAUD_AUDIT (XEUSU_CODIGO, XEAUD_FECHOR, XEAUD_MODULO, XEAUD_OPERAC, XEAUD_RESULT, XEAUD_DETALL, XEAUD_DIRIP) VALUES
 ('admin',    '2026-11-04 08:15:00', 'SEGURIDAD', 'INICIAR SESION', 'EXITOSO',   'Inicio de sesión correcto', '192.168.1.10'),
 ('admin',    '2026-11-04 08:20:00', 'SEGURIDAD', 'CREAR USUARIO',  'EXITOSO',   'Se creó el usuario landrade', '192.168.1.10'),
 ('landrade', '2026-11-02 16:20:00', 'SEGURIDAD', 'INICIAR SESION', 'BLOQUEADO', 'Cuenta bloqueada tras 3 intentos fallidos', '192.168.1.44'),
 (NULL,       '2026-11-02 16:19:00', 'SEGURIDAD', 'INICIAR SESION', 'FALLIDO',   'Usuario inexistente: farmacia1', '192.168.1.44');
