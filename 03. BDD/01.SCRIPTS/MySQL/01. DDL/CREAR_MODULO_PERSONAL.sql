/*==============================================================*/
/* SIGH - Hospital General Monster Salud - GR07                 */
/* Módulo PERSONAL: Administrar departamentos (P001) y          */
/* Administrar cargos (P002). Se puede ejecutar varias veces.   */
/*==============================================================*/
USE bd_monster_gr07;
SET NAMES utf8mb4;

/* Table: PEDEP_DEPAR (DEPARTAMENTO) */
CREATE TABLE IF NOT EXISTS PEDEP_DEPAR
(
   PEDEP_CODIGO         CHAR(3) NOT NULL,
   PEDEP_DESCRI         VARCHAR(60) NOT NULL,
   PRIMARY KEY (PEDEP_CODIGO)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;

/* Table: PECAR_CARGO (CARGO) */
CREATE TABLE IF NOT EXISTS PECAR_CARGO
(
   PEDEP_CODIGO         CHAR(3) NOT NULL,
   PECAR_CODIGO         CHAR(3) NOT NULL,
   PECAR_DESCRI         VARCHAR(60) NOT NULL,
   PRIMARY KEY (PEDEP_CODIGO, PECAR_CODIGO),
   CONSTRAINT FK_DEP_CAR FOREIGN KEY (PEDEP_CODIGO)
      REFERENCES PEDEP_DEPAR (PEDEP_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;

/* Departamentos base */
INSERT IGNORE INTO PEDEP_DEPAR (PEDEP_CODIGO, PEDEP_DESCRI) VALUES
 ('ADM', 'Administración'),
 ('CEX', 'Consulta externa'),
 ('EME', 'Emergencia'),
 ('HOS', 'Hospitalización'),
 ('LAB', 'Laboratorio'),
 ('IMA', 'Imagenología'),
 ('FAR', 'Farmacia'),
 ('QUI', 'Quirófano'),
 ('FAC', 'Facturación');

/* Cargos base */
INSERT IGNORE INTO PECAR_CARGO (PEDEP_CODIGO, PECAR_CODIGO, PECAR_DESCRI) VALUES
 ('ADM', 'ADS', 'Administrador de sistemas'),
 ('CEX', 'MED', 'Médico'),
 ('HOS', 'ENF', 'Enfermero/a'),
 ('FAR', 'FAR', 'Farmacéutico/a');

/* Sistema PERSONAL, sus opciones y permisos del perfil Administrador */
INSERT IGNORE INTO XESIS_SISTE (XESIS_CODIGO, XESIS_DESCRI) VALUES ('PER', 'PERSONAL');

/* ON DUPLICATE KEY UPDATE corrige P001 si una versión anterior la creó como «Administrar cargos» */
INSERT INTO XEOPC_OPCIO (XEOPC_CODIGO, XESIS_CODIGO, XEOPC_DESCRI, XEOPC_URL) VALUES
 ('P001', 'PER', 'Administrar departamentos', '/personal/departamentos'),
 ('P002', 'PER', 'Administrar cargos', '/personal/cargos')
ON DUPLICATE KEY UPDATE XESIS_CODIGO = VALUES(XESIS_CODIGO), XEOPC_DESCRI = VALUES(XEOPC_DESCRI),
 XEOPC_URL = VALUES(XEOPC_URL);

INSERT IGNORE INTO XEOXP_OPCPE (XEPER_CODIGO, XEOPC_CODIGO, XEOXP_FECASI, XEOXP_FECRET) VALUES
 ('ADM', 'P001', NOW(), NULL),
 ('ADM', 'P002', NOW(), NULL);

/* Verificación */
SELECT * FROM PEDEP_DEPAR ORDER BY PEDEP_DESCRI;
SELECT c.PEDEP_CODIGO, d.PEDEP_DESCRI, c.PECAR_CODIGO, c.PECAR_DESCRI
  FROM PECAR_CARGO c JOIN PEDEP_DEPAR d ON d.PEDEP_CODIGO = c.PEDEP_CODIGO
 ORDER BY d.PEDEP_DESCRI, c.PECAR_DESCRI;
SELECT * FROM XEOPC_OPCIO WHERE XESIS_CODIGO = 'PER';
SELECT * FROM XEOXP_OPCPE WHERE XEOPC_CODIGO IN ('P001', 'P002');
