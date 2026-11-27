/*==============================================================*/
/* DBMS name:      MySQL 5.x / 8.x                              */
/*==============================================================*/
/*==============================================================*/
/* Proyecto:       SIGH - Hospital General Monster Salud        */
/*==============================================================*/
/*==============================================================*/
/* Created on:     02/11/2026 01:49                             */
/*==============================================================*/

CREATE DATABASE IF NOT EXISTS SIGH_GR69 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci;
USE SIGH_GR69;

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS FIPAG_PAGO;
DROP TABLE IF EXISTS FIFPA_FORPA;
DROP TABLE IF EXISTS FICOM_COMPR;
DROP TABLE IF EXISTS FICAR_CARGO;
DROP TABLE IF EXISTS FICUE_CUENT;
DROP TABLE IF EXISTS FITAR_TARIF;
DROP TABLE IF EXISTS GHCIR_CIRUG;
DROP TABLE IF EXISTS GHSAL_SALA;
DROP TABLE IF EXISTS GHTRA_TRASL;
DROP TABLE IF EXISTS GHHOS_HOSPI;
DROP TABLE IF EXISTS GHCAM_CAMA;
DROP TABLE IF EXISTS GHHAB_HABIT;
DROP TABLE IF EXISTS GHDIS_DISPE;
DROP TABLE IF EXISTS GHDRE_DETRE;
DROP TABLE IF EXISTS GHREC_RECET;
DROP TABLE IF EXISTS GHMOV_MOVIN;
DROP TABLE IF EXISTS GHLOT_LOTE;
DROP TABLE IF EXISTS GHMED_MEDIC;
DROP TABLE IF EXISTS GHINF_INFIM;
DROP TABLE IF EXISTS GHRES_RESUL;
DROP TABLE IF EXISTS GHDSO_DETSO;
DROP TABLE IF EXISTS GHSOL_SOLEX;
DROP TABLE IF EXISTS GHEXA_EXAME;
DROP TABLE IF EXISTS GHDOC_DOCUM;
DROP TABLE IF EXISTS GHALE_ALERG;
DROP TABLE IF EXISTS GHSIG_SIGNO;
DROP TABLE IF EXISTS GHDXE_DIAEP;
DROP TABLE IF EXISTS GHCIE_CIE10;
DROP TABLE IF EXISTS GHNOT_NOTA;
DROP TABLE IF EXISTS GHEPI_EPISO;
DROP TABLE IF EXISTS GHCIT_CITA;
DROP TABLE IF EXISTS GHTUR_TURNO;
DROP TABLE IF EXISTS GHAGE_AGEND;
DROP TABLE IF EXISTS GHCON_CONSU;
DROP TABLE IF EXISTS GHESP_ESPEC;
DROP TABLE IF EXISTS GHPAC_PACIE;
DROP TABLE IF EXISTS XEAUD_AUDIT;
DROP TABLE IF EXISTS XESES_SESIO;
DROP TABLE IF EXISTS XEOXP_OPCPE;
DROP TABLE IF EXISTS XEUXP_USUPE;
DROP TABLE IF EXISTS XEUSU_USUAR;
DROP TABLE IF EXISTS XEPER_PERFI;
DROP TABLE IF EXISTS XEOPC_OPCIO;
DROP TABLE IF EXISTS XESIS_SISTE;
DROP TABLE IF EXISTS XEEST_ESTAD;
DROP TABLE IF EXISTS PEEMP_EMPLE;
DROP TABLE IF EXISTS PECAR_CARGO;
DROP TABLE IF EXISTS PEDEP_DEPAR;
DROP TABLE IF EXISTS PEESC_ESTCIV;
DROP TABLE IF EXISTS PEGEN_GENERO;

SET FOREIGN_KEY_CHECKS = 1;

/*==============================================================*/
/* Table: PEGEN_GENERO                                          */
/*==============================================================*/
CREATE TABLE PEGEN_GENERO
(
   PEGEN_CODIGO         CHAR(1) NOT NULL,
   PEGEN_DESCRI         VARCHAR(20) NOT NULL,
   PRIMARY KEY (PEGEN_CODIGO)
) ENGINE=InnoDB;

ALTER TABLE PEGEN_GENERO COMMENT 'Catálogo de género o sexo de pacientes y empleados (RF-PAC-001).';

/*==============================================================*/
/* Table: PEESC_ESTCIV                                          */
/*==============================================================*/
CREATE TABLE PEESC_ESTCIV
(
   PEESC_CODIGO         CHAR(1) NOT NULL,
   PEESC_DESCRI         VARCHAR(20) NOT NULL,
   PRIMARY KEY (PEESC_CODIGO)
) ENGINE=InnoDB;

ALTER TABLE PEESC_ESTCIV COMMENT 'Catálogo de estado civil de pacientes y empleados.';

/*==============================================================*/
/* Table: PEDEP_DEPAR                                           */
/*==============================================================*/
CREATE TABLE PEDEP_DEPAR
(
   PEDEP_CODIGO         CHAR(3) NOT NULL,
   PEDEP_DESCRI         VARCHAR(60) NOT NULL,
   PRIMARY KEY (PEDEP_CODIGO)
) ENGINE=InnoDB;

ALTER TABLE PEDEP_DEPAR COMMENT 'Departamento o servicio hospitalario (Consulta externa, Emergencia, Laboratorio, Imagenología, Farmacia, Hospitalización, Quirófano, Facturación).';

/*==============================================================*/
/* Table: PECAR_CARGO                                           */
/*==============================================================*/
CREATE TABLE PECAR_CARGO
(
   PEDEP_CODIGO         CHAR(3) NOT NULL,
   PECAR_CODIGO         CHAR(3) NOT NULL,
   PECAR_DESCRI         VARCHAR(60) NOT NULL,
   PRIMARY KEY (PEDEP_CODIGO, PECAR_CODIGO)
) ENGINE=InnoDB;

ALTER TABLE PECAR_CARGO COMMENT 'Cargo del personal dentro de un departamento (Médico, Enfermero, Farmacéutico, etc.).';

/*==============================================================*/
/* Table: PEEMP_EMPLE                                           */
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

ALTER TABLE PEEMP_EMPLE COMMENT 'Personal del hospital que usa el SIGH: médicos, enfermeros, admisión, laboratorio, imágenes, farmacia, hospitalización, quirófano, facturación, dirección y auditoría.';

/*==============================================================*/
/* Table: XEEST_ESTAD                                           */
/*==============================================================*/
CREATE TABLE XEEST_ESTAD
(
   XEEST_CODIGO         CHAR(1) NOT NULL,
   XEEST_DESCRI         VARCHAR(30) NOT NULL,
   PRIMARY KEY (XEEST_CODIGO)
) ENGINE=InnoDB;

ALTER TABLE XEEST_ESTAD COMMENT 'Estado de una cuenta de usuario: activo, inactivo o bloqueado (RF-SEG-002).';

/*==============================================================*/
/* Table: XESIS_SISTE                                           */
/*==============================================================*/
CREATE TABLE XESIS_SISTE
(
   XESIS_CODIGO         CHAR(3) NOT NULL,
   XESIS_DESCRI         VARCHAR(60) NOT NULL,
   PRIMARY KEY (XESIS_CODIGO)
) ENGINE=InnoDB;

ALTER TABLE XESIS_SISTE COMMENT 'Módulo o subsistema del SIGH al que pertenecen las opciones (Seguridad, Pacientes, Citas, Atención clínica, etc.).';

/*==============================================================*/
/* Table: XEOPC_OPCIO                                           */
/*==============================================================*/
CREATE TABLE XEOPC_OPCIO
(
   XEOPC_CODIGO         CHAR(4) NOT NULL,
   XESIS_CODIGO         CHAR(3) NOT NULL,
   XEOPC_DESCRI         VARCHAR(80) NOT NULL,
   XEOPC_URL            VARCHAR(150),
   PRIMARY KEY (XEOPC_CODIGO)
) ENGINE=InnoDB;

ALTER TABLE XEOPC_OPCIO COMMENT 'Opción o funcionalidad de un módulo sobre la que se conceden permisos (RF-SEG-003).';

/*==============================================================*/
/* Table: XEPER_PERFI                                           */
/*==============================================================*/
CREATE TABLE XEPER_PERFI
(
   XEPER_CODIGO         CHAR(3) NOT NULL,
   XEPER_DESCRI         VARCHAR(60) NOT NULL,
   XEPER_OBSERV         VARCHAR(200),
   PRIMARY KEY (XEPER_CODIGO)
) ENGINE=InnoDB;

ALTER TABLE XEPER_PERFI COMMENT 'Perfil o rol del usuario: Administrador, Médico, Enfermero, Admisión, etc. (RF-SEG-003).';

/*==============================================================*/
/* Table: XEUSU_USUAR                                           */
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

ALTER TABLE XEUSU_USUAR COMMENT 'Cuenta de acceso de un empleado al SIGH (RF-SEG-001, RF-SEG-002, RF-SEG-005).';

/*==============================================================*/
/* Table: XEUXP_USUPE                                           */
/*==============================================================*/
CREATE TABLE XEUXP_USUPE
(
   XEUSU_CODIGO         VARCHAR(20) NOT NULL,
   XEPER_CODIGO         CHAR(3) NOT NULL,
   XEUXP_FECASI         DATETIME NOT NULL,
   XEUXP_FECRET         DATETIME,
   PRIMARY KEY (XEUSU_CODIGO, XEPER_CODIGO)
) ENGINE=InnoDB;

ALTER TABLE XEUXP_USUPE COMMENT 'Asignación de perfiles a usuarios con fecha de asignación y retiro (RF-SEG-003).';

/*==============================================================*/
/* Table: XEOXP_OPCPE                                           */
/*==============================================================*/
CREATE TABLE XEOXP_OPCPE
(
   XEPER_CODIGO         CHAR(3) NOT NULL,
   XEOPC_CODIGO         CHAR(4) NOT NULL,
   XEOXP_FECASI         DATETIME NOT NULL,
   XEOXP_FECRET         DATETIME,
   PRIMARY KEY (XEPER_CODIGO, XEOPC_CODIGO)
) ENGINE=InnoDB;

ALTER TABLE XEOXP_OPCPE COMMENT 'Asignación de opciones (permisos) a perfiles (RF-SEG-003).';

/*==============================================================*/
/* Table: XESES_SESIO                                           */
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

ALTER TABLE XESES_SESIO COMMENT 'Sesión de trabajo de un usuario: inicio, cierre y expiración (RF-SEG-004).';

/*==============================================================*/
/* Table: XEAUD_AUDIT                                           */
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

ALTER TABLE XEAUD_AUDIT COMMENT 'Registro inalterable de eventos y accesos denegados del sistema (RF-AUD-001 a RF-AUD-004, RF-CAT-003).';

/*==============================================================*/
/* Table: GHPAC_PACIE                                           */
/*==============================================================*/
CREATE TABLE GHPAC_PACIE
(
   GHPAC_CODIGO         INT NOT NULL AUTO_INCREMENT,
   PEGEN_CODIGO         CHAR(1) NOT NULL,
   PEESC_CODIGO         CHAR(1),
   GHPAC_TIPDOC         VARCHAR(15) NOT NULL,
   GHPAC_NUMDOC         VARCHAR(20) NOT NULL,
   GHPAC_APELLI         VARCHAR(60) NOT NULL,
   GHPAC_NOMBRE         VARCHAR(60) NOT NULL,
   GHPAC_FECNAC         DATE NOT NULL,
   GHPAC_DIRECC         VARCHAR(150),
   GHPAC_TELEFO         VARCHAR(15),
   GHPAC_EMAIL          VARCHAR(100),
   GHPAC_CONEME         VARCHAR(100),
   GHPAC_TELEME         VARCHAR(15),
   GHPAC_FECREG         DATETIME NOT NULL,
   GHPAC_ESTADO         CHAR(1) NOT NULL,
   PRIMARY KEY (GHPAC_CODIGO)
) ENGINE=InnoDB;

ALTER TABLE GHPAC_PACIE COMMENT 'Paciente registrado con identificador hospitalario único (RF-PAC-001 a RF-PAC-005, CU-03).';

/*==============================================================*/
/* Table: GHESP_ESPEC                                           */
/*==============================================================*/
CREATE TABLE GHESP_ESPEC
(
   GHESP_CODIGO         CHAR(3) NOT NULL,
   GHESP_DESCRI         VARCHAR(60) NOT NULL,
   PRIMARY KEY (GHESP_CODIGO)
) ENGINE=InnoDB;

ALTER TABLE GHESP_ESPEC COMMENT 'Catálogo de especialidades médicas (RF-CIT-001, RF-CAT-001).';

/*==============================================================*/
/* Table: GHCON_CONSU                                           */
/*==============================================================*/
CREATE TABLE GHCON_CONSU
(
   GHCON_CODIGO         CHAR(4) NOT NULL,
   PEDEP_CODIGO         CHAR(3) NOT NULL,
   GHCON_DESCRI         VARCHAR(60) NOT NULL,
   GHCON_UBICAC         VARCHAR(80),
   PRIMARY KEY (GHCON_CODIGO)
) ENGINE=InnoDB;

ALTER TABLE GHCON_CONSU COMMENT 'Consultorio físico de un servicio donde se atienden las citas (RF-CIT-001).';

/*==============================================================*/
/* Table: GHAGE_AGEND                                           */
/*==============================================================*/
CREATE TABLE GHAGE_AGEND
(
   GHAGE_CODIGO         INT NOT NULL AUTO_INCREMENT,
   PEEMP_CODIGO         CHAR(10) NOT NULL,
   GHESP_CODIGO         CHAR(3) NOT NULL,
   GHCON_CODIGO         CHAR(4) NOT NULL,
   GHAGE_FECINI         DATE NOT NULL,
   GHAGE_FECFIN         DATE NOT NULL,
   GHAGE_DIAS           VARCHAR(20) NOT NULL,
   GHAGE_HORINI         TIME NOT NULL,
   GHAGE_HORFIN         TIME NOT NULL,
   GHAGE_DURTUR         SMALLINT NOT NULL,
   GHAGE_ESTADO         CHAR(1) NOT NULL,
   PRIMARY KEY (GHAGE_CODIGO)
) ENGINE=InnoDB;

ALTER TABLE GHAGE_AGEND COMMENT 'Agenda de un profesional por especialidad y consultorio (CU-05, RF-CIT-001).';

/*==============================================================*/
/* Table: GHTUR_TURNO                                           */
/*==============================================================*/
CREATE TABLE GHTUR_TURNO
(
   GHTUR_CODIGO         INT NOT NULL AUTO_INCREMENT,
   GHAGE_CODIGO         INT NOT NULL,
   GHTUR_FECHA          DATE NOT NULL,
   GHTUR_HORA           TIME NOT NULL,
   GHTUR_ESTADO         CHAR(1) NOT NULL,
   GHTUR_MOTBLO         VARCHAR(150),
   PRIMARY KEY (GHTUR_CODIGO)
) ENGINE=InnoDB;

ALTER TABLE GHTUR_TURNO COMMENT 'Turno generado por una agenda; puede estar disponible, reservado o bloqueado (CU-05, CU-06).';

/*==============================================================*/
/* Table: GHCIT_CITA                                            */
/*==============================================================*/
CREATE TABLE GHCIT_CITA
(
   GHCIT_CODIGO         INT NOT NULL AUTO_INCREMENT,
   GHPAC_CODIGO         INT NOT NULL,
   GHTUR_CODIGO         INT NOT NULL,
   GHCIT_MOTIVO         VARCHAR(200) NOT NULL,
   GHCIT_ESTADO         VARCHAR(15) NOT NULL,
   GHCIT_FECREG         DATETIME NOT NULL,
   GHCIT_MOTCAM         VARCHAR(200),
   GHCIT_CITANT         INT,
   PRIMARY KEY (GHCIT_CODIGO)
) ENGINE=InnoDB;

ALTER TABLE GHCIT_CITA COMMENT 'Cita médica de un paciente en un turno; registra reprogramación, cancelación e inasistencia (CU-06, CU-07, RF-CIT-002 a RF-CIT-006).';

/*==============================================================*/
/* Table: GHEPI_EPISO                                           */
/*==============================================================*/
CREATE TABLE GHEPI_EPISO
(
   GHEPI_CODIGO         INT NOT NULL AUTO_INCREMENT,
   GHPAC_CODIGO         INT NOT NULL,
   GHCIT_CODIGO         INT,
   PEDEP_CODIGO         CHAR(3) NOT NULL,
   GHEPI_TIPO           VARCHAR(20) NOT NULL,
   GHEPI_FECING         DATETIME NOT NULL,
   GHEPI_MOTIVO         VARCHAR(250) NOT NULL,
   GHEPI_PRIORI         CHAR(1),
   GHEPI_DISPOS         VARCHAR(20),
   GHEPI_FECCIE         DATETIME,
   GHEPI_ESTADO         VARCHAR(15) NOT NULL,
   PRIMARY KEY (GHEPI_CODIGO)
) ENGINE=InnoDB;

ALTER TABLE GHEPI_EPISO COMMENT 'Episodio de atención: consulta externa, emergencia, atención no programada u hospitalización (CU-04, CU-08, CU-09, RF-HC-001).';

/*==============================================================*/
/* Table: GHNOT_NOTA                                            */
/*==============================================================*/
CREATE TABLE GHNOT_NOTA
(
   GHNOT_CODIGO         INT NOT NULL AUTO_INCREMENT,
   GHEPI_CODIGO         INT NOT NULL,
   PEEMP_CODIGO         CHAR(10) NOT NULL,
   GHNOT_TIPO           VARCHAR(15) NOT NULL,
   GHNOT_FECHA          DATETIME NOT NULL,
   GHNOT_MOTCON         TEXT,
   GHNOT_EXAFIS         TEXT,
   GHNOT_PLAN           TEXT,
   GHNOT_TEXTO          TEXT,
   GHNOT_ESTADO         VARCHAR(15) NOT NULL,
   GHNOT_FECFIN         DATETIME,
   PRIMARY KEY (GHNOT_CODIGO)
) ENGINE=InnoDB;

ALTER TABLE GHNOT_NOTA COMMENT 'Nota clínica del episodio: consulta, evolución, cuidado de enfermería o adenda (CU-08, CU-10, RF-HC-002, RF-HC-004, RF-HC-007).';

/*==============================================================*/
/* Table: GHCIE_CIE10                                           */
/*==============================================================*/
CREATE TABLE GHCIE_CIE10
(
   GHCIE_CODIGO         VARCHAR(10) NOT NULL,
   GHCIE_DESCRI         VARCHAR(250) NOT NULL,
   PRIMARY KEY (GHCIE_CODIGO)
) ENGINE=InnoDB;

ALTER TABLE GHCIE_CIE10 COMMENT 'Catálogo de diagnósticos codificados CIE-10 (RF-HC-003).';

/*==============================================================*/
/* Table: GHDXE_DIAEP                                           */
/*==============================================================*/
CREATE TABLE GHDXE_DIAEP
(
   GHEPI_CODIGO         INT NOT NULL,
   GHCIE_CODIGO         VARCHAR(10) NOT NULL,
   GHDXE_TIPO           VARCHAR(15) NOT NULL,
   GHDXE_CONDIC         VARCHAR(15) NOT NULL,
   GHDXE_FECHA          DATETIME NOT NULL,
   PRIMARY KEY (GHEPI_CODIGO, GHCIE_CODIGO)
) ENGINE=InnoDB;

ALTER TABLE GHDXE_DIAEP COMMENT 'Diagnóstico registrado en un episodio: principal o secundario, presuntivo o definitivo (RF-HC-003).';

/*==============================================================*/
/* Table: GHSIG_SIGNO                                           */
/*==============================================================*/
CREATE TABLE GHSIG_SIGNO
(
   GHSIG_CODIGO         INT NOT NULL AUTO_INCREMENT,
   GHEPI_CODIGO         INT NOT NULL,
   PEEMP_CODIGO         CHAR(10) NOT NULL,
   GHSIG_FECHA          DATETIME NOT NULL,
   GHSIG_TEMPER         DECIMAL(4,1),
   GHSIG_PRESIS         SMALLINT,
   GHSIG_PREDIA         SMALLINT,
   GHSIG_FRECAR         SMALLINT,
   GHSIG_FRERES         SMALLINT,
   GHSIG_SATOXI         SMALLINT,
   GHSIG_PESO           DECIMAL(5,2),
   GHSIG_TALLA          DECIMAL(5,2),
   GHSIG_OBSERV         VARCHAR(250),
   PRIMARY KEY (GHSIG_CODIGO)
) ENGINE=InnoDB;

ALTER TABLE GHSIG_SIGNO COMMENT 'Medición de signos vitales registrada por enfermería (CU-10, RF-HC-002).';

/*==============================================================*/
/* Table: GHALE_ALERG                                           */
/*==============================================================*/
CREATE TABLE GHALE_ALERG
(
   GHALE_CODIGO         INT NOT NULL AUTO_INCREMENT,
   GHPAC_CODIGO         INT NOT NULL,
   GHALE_SUSTAN         VARCHAR(100) NOT NULL,
   GHALE_REACCI         VARCHAR(200),
   GHALE_SEVERI         VARCHAR(15),
   GHALE_FUENTE         VARCHAR(50),
   GHALE_ESTVER         VARCHAR(20) NOT NULL,
   GHALE_FECREG         DATETIME NOT NULL,
   PRIMARY KEY (GHALE_CODIGO)
) ENGINE=InnoDB;

ALTER TABLE GHALE_ALERG COMMENT 'Alergia del paciente mostrada como alerta en la atención y la prescripción (CU-11, RF-HC-006).';

/*==============================================================*/
/* Table: GHDOC_DOCUM                                           */
/*==============================================================*/
CREATE TABLE GHDOC_DOCUM
(
   GHDOC_CODIGO         INT NOT NULL AUTO_INCREMENT,
   GHEPI_CODIGO         INT NOT NULL,
   GHDOC_NOMBRE         VARCHAR(150) NOT NULL,
   GHDOC_TIPO           VARCHAR(30) NOT NULL,
   GHDOC_RUTA           VARCHAR(255) NOT NULL,
   GHDOC_FECCAR         DATETIME NOT NULL,
   PRIMARY KEY (GHDOC_CODIGO)
) ENGINE=InnoDB;

ALTER TABLE GHDOC_DOCUM COMMENT 'Documento clínico digitalizado adjunto a un episodio (RF-HC-008).';

/*==============================================================*/
/* Table: GHEXA_EXAME                                           */
/*==============================================================*/
CREATE TABLE GHEXA_EXAME
(
   GHEXA_CODIGO         CHAR(6) NOT NULL,
   GHEXA_DESCRI         VARCHAR(120) NOT NULL,
   GHEXA_TIPO           VARCHAR(15) NOT NULL,
   GHEXA_UNIDAD         VARCHAR(20),
   GHEXA_VALREF         VARCHAR(100),
   GHEXA_ESTADO         CHAR(1) NOT NULL,
   PRIMARY KEY (GHEXA_CODIGO)
) ENGINE=InnoDB;

ALTER TABLE GHEXA_EXAME COMMENT 'Catálogo de exámenes de laboratorio y estudios de imagen (CU-12, RF-LAB-001, RF-IMG-001).';

/*==============================================================*/
/* Table: GHSOL_SOLEX                                           */
/*==============================================================*/
CREATE TABLE GHSOL_SOLEX
(
   GHSOL_CODIGO         INT NOT NULL AUTO_INCREMENT,
   GHEPI_CODIGO         INT NOT NULL,
   PEEMP_CODIGO         CHAR(10) NOT NULL,
   GHSOL_FECHA          DATETIME NOT NULL,
   GHSOL_PRIORI         VARCHAR(10) NOT NULL,
   GHSOL_INDCLI         VARCHAR(250) NOT NULL,
   GHSOL_ESTADO         VARCHAR(20) NOT NULL,
   PRIMARY KEY (GHSOL_CODIGO)
) ENGINE=InnoDB;

ALTER TABLE GHSOL_SOLEX COMMENT 'Solicitud de exámenes realizada por el médico dentro de un episodio (CU-12).';

/*==============================================================*/
/* Table: GHDSO_DETSO                                           */
/*==============================================================*/
CREATE TABLE GHDSO_DETSO
(
   GHSOL_CODIGO         INT NOT NULL,
   GHEXA_CODIGO         CHAR(6) NOT NULL,
   GHDSO_IDMUES         VARCHAR(20),
   GHDSO_FECREC         DATETIME,
   GHDSO_ESTADO         VARCHAR(20) NOT NULL,
   PRIMARY KEY (GHSOL_CODIGO, GHEXA_CODIGO)
) ENGINE=InnoDB;

ALTER TABLE GHDSO_DETSO COMMENT 'Examen solicitado dentro de una solicitud; controla la muestra y su estado (CU-13, RF-LAB-002).';

/*==============================================================*/
/* Table: GHRES_RESUL                                           */
/*==============================================================*/
CREATE TABLE GHRES_RESUL
(
   GHRES_CODIGO         INT NOT NULL AUTO_INCREMENT,
   GHSOL_CODIGO         INT NOT NULL,
   GHEXA_CODIGO         CHAR(6) NOT NULL,
   PEEMP_CODIGO         CHAR(10),
   GHRES_VALOR          VARCHAR(60) NOT NULL,
   GHRES_UNIDAD         VARCHAR(20),
   GHRES_VALREF         VARCHAR(100),
   GHRES_CRITIC         CHAR(1) NOT NULL,
   GHRES_ESTADO         VARCHAR(15) NOT NULL,
   GHRES_FECREG         DATETIME NOT NULL,
   GHRES_FECVAL         DATETIME,
   PRIMARY KEY (GHRES_CODIGO)
) ENGINE=InnoDB;

ALTER TABLE GHRES_RESUL COMMENT 'Resultado de laboratorio, preliminar o validado (CU-13, RF-LAB-003 a RF-LAB-005).';

/*==============================================================*/
/* Table: GHINF_INFIM                                           */
/*==============================================================*/
CREATE TABLE GHINF_INFIM
(
   GHINF_CODIGO         INT NOT NULL AUTO_INCREMENT,
   GHSOL_CODIGO         INT NOT NULL,
   GHEXA_CODIGO         CHAR(6) NOT NULL,
   PEEMP_CODIGO         CHAR(10) NOT NULL,
   GHINF_FECPRO         DATETIME,
   GHINF_FECEJE         DATETIME,
   GHINF_TEXTO          TEXT,
   GHINF_REFIMG         VARCHAR(255),
   GHINF_VERSIO         SMALLINT NOT NULL,
   GHINF_ESTADO         VARCHAR(15) NOT NULL,
   PRIMARY KEY (GHINF_CODIGO)
) ENGINE=InnoDB;

ALTER TABLE GHINF_INFIM COMMENT 'Informe de un estudio de imagenología con versión y referencia a imágenes (CU-14, RF-IMG-002 a RF-IMG-004).';

/*==============================================================*/
/* Table: GHMED_MEDIC                                           */
/*==============================================================*/
CREATE TABLE GHMED_MEDIC
(
   GHMED_CODIGO         CHAR(8) NOT NULL,
   GHMED_NOMGEN         VARCHAR(100) NOT NULL,
   GHMED_PRIACT         VARCHAR(100) NOT NULL,
   GHMED_PRESEN         VARCHAR(50) NOT NULL,
   GHMED_CONCEN         VARCHAR(30) NOT NULL,
   GHMED_FORFAR         VARCHAR(30) NOT NULL,
   GHMED_UNIDAD         VARCHAR(15) NOT NULL,
   GHMED_STKMIN         INT NOT NULL,
   GHMED_ESTADO         CHAR(1) NOT NULL,
   PRIMARY KEY (GHMED_CODIGO)
) ENGINE=InnoDB;

ALTER TABLE GHMED_MEDIC COMMENT 'Catálogo de medicamentos (CU-15, CU-17, RF-FAR-001).';

/*==============================================================*/
/* Table: GHLOT_LOTE                                            */
/*==============================================================*/
CREATE TABLE GHLOT_LOTE
(
   GHLOT_CODIGO         INT NOT NULL AUTO_INCREMENT,
   GHMED_CODIGO         CHAR(8) NOT NULL,
   GHLOT_NUMERO         VARCHAR(20) NOT NULL,
   GHLOT_FECCAD         DATE NOT NULL,
   GHLOT_CANTID         INT NOT NULL,
   GHLOT_UBICAC         VARCHAR(50),
   GHLOT_ESTADO         VARCHAR(15) NOT NULL,
   PRIMARY KEY (GHLOT_CODIGO)
) ENGINE=InnoDB;

ALTER TABLE GHLOT_LOTE COMMENT 'Lote de un medicamento con fecha de caducidad, ubicación y existencias (CU-17, RF-FAR-002).';

/*==============================================================*/
/* Table: GHMOV_MOVIN                                           */
/*==============================================================*/
CREATE TABLE GHMOV_MOVIN
(
   GHMOV_CODIGO         INT NOT NULL AUTO_INCREMENT,
   GHLOT_CODIGO         INT NOT NULL,
   PEEMP_CODIGO         CHAR(10) NOT NULL,
   GHMOV_TIPO           VARCHAR(15) NOT NULL,
   GHMOV_CANTID         INT NOT NULL,
   GHMOV_FECHA          DATETIME NOT NULL,
   GHMOV_MOTIVO         VARCHAR(200),
   GHMOV_DOCRES         VARCHAR(50),
   PRIMARY KEY (GHMOV_CODIGO)
) ENGINE=InnoDB;

ALTER TABLE GHMOV_MOVIN COMMENT 'Movimiento de inventario: ingreso, ajuste o dispensación (CU-17, RF-FAR-005 a RF-FAR-007).';

/*==============================================================*/
/* Table: GHREC_RECET                                           */
/*==============================================================*/
CREATE TABLE GHREC_RECET
(
   GHREC_CODIGO         INT NOT NULL AUTO_INCREMENT,
   GHEPI_CODIGO         INT NOT NULL,
   PEEMP_CODIGO         CHAR(10) NOT NULL,
   GHREC_FECHA          DATETIME NOT NULL,
   GHREC_ESTADO         VARCHAR(15) NOT NULL,
   GHREC_JUSALE         VARCHAR(250),
   PRIMARY KEY (GHREC_CODIGO)
) ENGINE=InnoDB;

ALTER TABLE GHREC_RECET COMMENT 'Receta médica emitida en un episodio y enviada a farmacia (CU-15, RF-FAR-003).';

/*==============================================================*/
/* Table: GHDRE_DETRE                                           */
/*==============================================================*/
CREATE TABLE GHDRE_DETRE
(
   GHREC_CODIGO         INT NOT NULL,
   GHMED_CODIGO         CHAR(8) NOT NULL,
   GHDRE_DOSIS          VARCHAR(50) NOT NULL,
   GHDRE_VIA            VARCHAR(30) NOT NULL,
   GHDRE_FRECUE         VARCHAR(50) NOT NULL,
   GHDRE_DURACI         VARCHAR(30) NOT NULL,
   GHDRE_CANTID         INT NOT NULL,
   GHDRE_CANDIS         INT NOT NULL,
   GHDRE_INDICA         VARCHAR(250),
   PRIMARY KEY (GHREC_CODIGO, GHMED_CODIGO)
) ENGINE=InnoDB;

ALTER TABLE GHDRE_DETRE COMMENT 'Medicamento prescrito en una receta con dosis, vía, frecuencia y duración (CU-15).';

/*==============================================================*/
/* Table: GHDIS_DISPE                                           */
/*==============================================================*/
CREATE TABLE GHDIS_DISPE
(
   GHDIS_CODIGO         INT NOT NULL AUTO_INCREMENT,
   GHREC_CODIGO         INT NOT NULL,
   GHMED_CODIGO         CHAR(8) NOT NULL,
   GHLOT_CODIGO         INT NOT NULL,
   PEEMP_CODIGO         CHAR(10) NOT NULL,
   GHDIS_CANTID         INT NOT NULL,
   GHDIS_FECHA          DATETIME NOT NULL,
   PRIMARY KEY (GHDIS_CODIGO)
) ENGINE=InnoDB;

ALTER TABLE GHDIS_DISPE COMMENT 'Entrega de un medicamento recetado desde un lote; permite entregas parciales (CU-16, RF-FAR-004).';

/*==============================================================*/
/* Table: GHHAB_HABIT                                           */
/*==============================================================*/
CREATE TABLE GHHAB_HABIT
(
   GHHAB_CODIGO         CHAR(5) NOT NULL,
   PEDEP_CODIGO         CHAR(3) NOT NULL,
   GHHAB_PISO           SMALLINT NOT NULL,
   GHHAB_TIPO           VARCHAR(20) NOT NULL,
   PRIMARY KEY (GHHAB_CODIGO)
) ENGINE=InnoDB;

ALTER TABLE GHHAB_HABIT COMMENT 'Habitación de un servicio de hospitalización (CU-18, RF-HOS-001).';

/*==============================================================*/
/* Table: GHCAM_CAMA                                            */
/*==============================================================*/
CREATE TABLE GHCAM_CAMA
(
   GHCAM_CODIGO         CHAR(6) NOT NULL,
   GHHAB_CODIGO         CHAR(5) NOT NULL,
   GHCAM_ESTADO         VARCHAR(20) NOT NULL,
   GHCAM_RESTRI         VARCHAR(50),
   PRIMARY KEY (GHCAM_CODIGO)
) ENGINE=InnoDB;

ALTER TABLE GHCAM_CAMA COMMENT 'Cama de una habitación con su estado operativo (CU-18, RF-HOS-001).';

/*==============================================================*/
/* Table: GHHOS_HOSPI                                           */
/*==============================================================*/
CREATE TABLE GHHOS_HOSPI
(
   GHHOS_CODIGO         INT NOT NULL AUTO_INCREMENT,
   GHEPI_CODIGO         INT NOT NULL,
   GHCAM_CODIGO         CHAR(6),
   PEEMP_CODIGO         CHAR(10) NOT NULL,
   GHHOS_FECSOL         DATETIME NOT NULL,
   GHHOS_FECING         DATETIME,
   GHHOS_ESTADO         VARCHAR(20) NOT NULL,
   GHHOS_FECALM         DATETIME,
   GHHOS_INDALT         TEXT,
   GHHOS_FECALA         DATETIME,
   GHHOS_CAUEGR         VARCHAR(30),
   PRIMARY KEY (GHHOS_CODIGO)
) ENGINE=InnoDB;

ALTER TABLE GHHOS_HOSPI COMMENT 'Hospitalización de un paciente: solicitud, ingreso, alta médica y alta administrativa (CU-18, CU-19, RF-HOS-002 a RF-HOS-006).';

/*==============================================================*/
/* Table: GHTRA_TRASL                                           */
/*==============================================================*/
CREATE TABLE GHTRA_TRASL
(
   GHTRA_CODIGO         INT NOT NULL AUTO_INCREMENT,
   GHHOS_CODIGO         INT NOT NULL,
   GHCAM_CODIGO         CHAR(6) NOT NULL,
   PEEMP_CODIGO         CHAR(10) NOT NULL,
   GHTRA_CAMORI         CHAR(6) NOT NULL,
   GHTRA_FECHA          DATETIME NOT NULL,
   GHTRA_MOTIVO         VARCHAR(200) NOT NULL,
   PRIMARY KEY (GHTRA_CODIGO)
) ENGINE=InnoDB;

ALTER TABLE GHTRA_TRASL COMMENT 'Traslado de un paciente hospitalizado a otra cama (CU-19, RF-HOS-004).';

/*==============================================================*/
/* Table: GHSAL_SALA                                            */
/*==============================================================*/
CREATE TABLE GHSAL_SALA
(
   GHSAL_CODIGO         CHAR(4) NOT NULL,
   GHSAL_DESCRI         VARCHAR(60) NOT NULL,
   GHSAL_ESTADO         CHAR(1) NOT NULL,
   PRIMARY KEY (GHSAL_CODIGO)
) ENGINE=InnoDB;

ALTER TABLE GHSAL_SALA COMMENT 'Sala de quirófano (CU-20, RF-QUI-001).';

/*==============================================================*/
/* Table: GHCIR_CIRUG                                           */
/*==============================================================*/
CREATE TABLE GHCIR_CIRUG
(
   GHCIR_CODIGO         INT NOT NULL AUTO_INCREMENT,
   GHEPI_CODIGO         INT NOT NULL,
   GHSAL_CODIGO         CHAR(4) NOT NULL,
   PEEMP_CODIGO         CHAR(10) NOT NULL,
   GHCIR_PROCED         VARCHAR(200) NOT NULL,
   GHCIR_EQUIPO         VARCHAR(250) NOT NULL,
   GHCIR_FECPRO         DATETIME NOT NULL,
   GHCIR_HORINI         DATETIME,
   GHCIR_HORFIN         DATETIME,
   GHCIR_LISVER         CHAR(1) NOT NULL,
   GHCIR_DETALL         TEXT,
   GHCIR_ESTADO         VARCHAR(15) NOT NULL,
   GHCIR_MOTSUS         VARCHAR(200),
   PRIMARY KEY (GHCIR_CODIGO)
) ENGINE=InnoDB;

ALTER TABLE GHCIR_CIRUG COMMENT 'Cirugía programada y su registro operatorio (CU-20, RF-QUI-001 a RF-QUI-004).';

/*==============================================================*/
/* Table: FITAR_TARIF                                           */
/*==============================================================*/
CREATE TABLE FITAR_TARIF
(
   FITAR_CODIGO         CHAR(6) NOT NULL,
   FITAR_DESCRI         VARCHAR(120) NOT NULL,
   FITAR_TIPO           VARCHAR(20) NOT NULL,
   FITAR_VALOR          DECIMAL(10,2) NOT NULL,
   FITAR_FECVIG         DATE NOT NULL,
   FITAR_ESTADO         CHAR(1) NOT NULL,
   PRIMARY KEY (FITAR_CODIGO)
) ENGINE=InnoDB;

ALTER TABLE FITAR_TARIF COMMENT 'Tarifa vigente de un servicio: consulta, examen, procedimiento, medicamento o estancia (CU-21, RF-FAC-001, RF-CAT-002).';

/*==============================================================*/
/* Table: FICUE_CUENT                                           */
/*==============================================================*/
CREATE TABLE FICUE_CUENT
(
   FICUE_CODIGO         INT NOT NULL AUTO_INCREMENT,
   GHEPI_CODIGO         INT NOT NULL,
   FICUE_FECAPE         DATETIME NOT NULL,
   FICUE_TOTAL          DECIMAL(12,2) NOT NULL,
   FICUE_SALDO          DECIMAL(12,2) NOT NULL,
   FICUE_ESTADO         VARCHAR(15) NOT NULL,
   PRIMARY KEY (FICUE_CODIGO)
) ENGINE=InnoDB;

ALTER TABLE FICUE_CUENT COMMENT 'Cuenta del paciente asociada a un episodio de atención (CU-21, RF-FAC-001, RF-FAC-006).';

/*==============================================================*/
/* Table: FICAR_CARGO                                           */
/*==============================================================*/
CREATE TABLE FICAR_CARGO
(
   FICAR_CODIGO         INT NOT NULL AUTO_INCREMENT,
   FICUE_CODIGO         INT NOT NULL,
   FITAR_CODIGO         CHAR(6) NOT NULL,
   FICAR_CANTID         DECIMAL(8,2) NOT NULL,
   FICAR_VALUNI         DECIMAL(10,2) NOT NULL,
   FICAR_SUBTOT         DECIMAL(12,2) NOT NULL,
   FICAR_FECHA          DATETIME NOT NULL,
   PRIMARY KEY (FICAR_CODIGO)
) ENGINE=InnoDB;

ALTER TABLE FICAR_CARGO COMMENT 'Cargo de un servicio a la cuenta del paciente (CU-21, RF-FAC-002).';

/*==============================================================*/
/* Table: FICOM_COMPR                                           */
/*==============================================================*/
CREATE TABLE FICOM_COMPR
(
   FICOM_CODIGO         INT NOT NULL AUTO_INCREMENT,
   FICUE_CODIGO         INT NOT NULL,
   PEEMP_CODIGO         CHAR(10) NOT NULL,
   FICOM_NUMERO         VARCHAR(20) NOT NULL,
   FICOM_FECHA          DATETIME NOT NULL,
   FICOM_SUBTOT         DECIMAL(12,2) NOT NULL,
   FICOM_IVA            DECIMAL(12,2) NOT NULL,
   FICOM_TOTAL          DECIMAL(12,2) NOT NULL,
   FICOM_ESTADO         VARCHAR(15) NOT NULL,
   FICOM_MOTANU         VARCHAR(200),
   PRIMARY KEY (FICOM_CODIGO)
) ENGINE=InnoDB;

ALTER TABLE FICOM_COMPR COMMENT 'Comprobante de venta emitido sobre una cuenta; puede anularse con motivo (CU-21, RF-FAC-003, RF-FAC-005).';

/*==============================================================*/
/* Table: FIFPA_FORPA                                           */
/*==============================================================*/
CREATE TABLE FIFPA_FORPA
(
   FIFPA_CODIGO         CHAR(2) NOT NULL,
   FIFPA_DESCRI         VARCHAR(40) NOT NULL,
   PRIMARY KEY (FIFPA_CODIGO)
) ENGINE=InnoDB;

ALTER TABLE FIFPA_FORPA COMMENT 'Catálogo de formas de pago (efectivo, tarjeta, transferencia, seguro).';

/*==============================================================*/
/* Table: FIPAG_PAGO                                            */
/*==============================================================*/
CREATE TABLE FIPAG_PAGO
(
   FIPAG_CODIGO         INT NOT NULL AUTO_INCREMENT,
   FICOM_CODIGO         INT NOT NULL,
   FIFPA_CODIGO         CHAR(2) NOT NULL,
   FIPAG_IMPORT         DECIMAL(12,2) NOT NULL,
   FIPAG_FECHA          DATETIME NOT NULL,
   FIPAG_REFERE         VARCHAR(50),
   PRIMARY KEY (FIPAG_CODIGO)
) ENGINE=InnoDB;

ALTER TABLE FIPAG_PAGO COMMENT 'Pago registrado sobre un comprobante; admite pagos parciales (CU-21, RF-FAC-004).';

ALTER TABLE PECAR_CARGO ADD CONSTRAINT FK_DEP_CAR FOREIGN KEY (PEDEP_CODIGO)
      REFERENCES PEDEP_DEPAR (PEDEP_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE PEEMP_EMPLE ADD CONSTRAINT FK_GEN_EMP FOREIGN KEY (PEGEN_CODIGO)
      REFERENCES PEGEN_GENERO (PEGEN_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE PEEMP_EMPLE ADD CONSTRAINT FK_ESC_EMP FOREIGN KEY (PEESC_CODIGO)
      REFERENCES PEESC_ESTCIV (PEESC_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE PEEMP_EMPLE ADD CONSTRAINT FK_CAR_EMP FOREIGN KEY (PEDEP_CODIGO, PECAR_CODIGO)
      REFERENCES PECAR_CARGO (PEDEP_CODIGO, PECAR_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;

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

ALTER TABLE GHPAC_PACIE ADD CONSTRAINT FK_GEN_PAC FOREIGN KEY (PEGEN_CODIGO)
      REFERENCES PEGEN_GENERO (PEGEN_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE GHPAC_PACIE ADD CONSTRAINT FK_ESC_PAC FOREIGN KEY (PEESC_CODIGO)
      REFERENCES PEESC_ESTCIV (PEESC_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE GHCON_CONSU ADD CONSTRAINT FK_DEP_CON FOREIGN KEY (PEDEP_CODIGO)
      REFERENCES PEDEP_DEPAR (PEDEP_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE GHAGE_AGEND ADD CONSTRAINT FK_EMP_AGE FOREIGN KEY (PEEMP_CODIGO)
      REFERENCES PEEMP_EMPLE (PEEMP_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE GHAGE_AGEND ADD CONSTRAINT FK_ESP_AGE FOREIGN KEY (GHESP_CODIGO)
      REFERENCES GHESP_ESPEC (GHESP_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE GHAGE_AGEND ADD CONSTRAINT FK_CON_AGE FOREIGN KEY (GHCON_CODIGO)
      REFERENCES GHCON_CONSU (GHCON_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE GHTUR_TURNO ADD CONSTRAINT FK_AGE_TUR FOREIGN KEY (GHAGE_CODIGO)
      REFERENCES GHAGE_AGEND (GHAGE_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE GHCIT_CITA ADD CONSTRAINT FK_PAC_CIT FOREIGN KEY (GHPAC_CODIGO)
      REFERENCES GHPAC_PACIE (GHPAC_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE GHCIT_CITA ADD CONSTRAINT FK_TUR_CIT FOREIGN KEY (GHTUR_CODIGO)
      REFERENCES GHTUR_TURNO (GHTUR_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE GHEPI_EPISO ADD CONSTRAINT FK_PAC_EPI FOREIGN KEY (GHPAC_CODIGO)
      REFERENCES GHPAC_PACIE (GHPAC_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE GHEPI_EPISO ADD CONSTRAINT FK_CIT_EPI FOREIGN KEY (GHCIT_CODIGO)
      REFERENCES GHCIT_CITA (GHCIT_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE GHEPI_EPISO ADD CONSTRAINT FK_DEP_EPI FOREIGN KEY (PEDEP_CODIGO)
      REFERENCES PEDEP_DEPAR (PEDEP_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE GHNOT_NOTA ADD CONSTRAINT FK_EPI_NOT FOREIGN KEY (GHEPI_CODIGO)
      REFERENCES GHEPI_EPISO (GHEPI_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE GHNOT_NOTA ADD CONSTRAINT FK_EMP_NOT FOREIGN KEY (PEEMP_CODIGO)
      REFERENCES PEEMP_EMPLE (PEEMP_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE GHDXE_DIAEP ADD CONSTRAINT FK_EPI_DXE FOREIGN KEY (GHEPI_CODIGO)
      REFERENCES GHEPI_EPISO (GHEPI_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE GHDXE_DIAEP ADD CONSTRAINT FK_CIE_DXE FOREIGN KEY (GHCIE_CODIGO)
      REFERENCES GHCIE_CIE10 (GHCIE_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE GHSIG_SIGNO ADD CONSTRAINT FK_EPI_SIG FOREIGN KEY (GHEPI_CODIGO)
      REFERENCES GHEPI_EPISO (GHEPI_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE GHSIG_SIGNO ADD CONSTRAINT FK_EMP_SIG FOREIGN KEY (PEEMP_CODIGO)
      REFERENCES PEEMP_EMPLE (PEEMP_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE GHALE_ALERG ADD CONSTRAINT FK_PAC_ALE FOREIGN KEY (GHPAC_CODIGO)
      REFERENCES GHPAC_PACIE (GHPAC_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE GHDOC_DOCUM ADD CONSTRAINT FK_EPI_DOC FOREIGN KEY (GHEPI_CODIGO)
      REFERENCES GHEPI_EPISO (GHEPI_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE GHSOL_SOLEX ADD CONSTRAINT FK_EPI_SOL FOREIGN KEY (GHEPI_CODIGO)
      REFERENCES GHEPI_EPISO (GHEPI_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE GHSOL_SOLEX ADD CONSTRAINT FK_EMP_SOL FOREIGN KEY (PEEMP_CODIGO)
      REFERENCES PEEMP_EMPLE (PEEMP_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE GHDSO_DETSO ADD CONSTRAINT FK_SOL_DSO FOREIGN KEY (GHSOL_CODIGO)
      REFERENCES GHSOL_SOLEX (GHSOL_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE GHDSO_DETSO ADD CONSTRAINT FK_EXA_DSO FOREIGN KEY (GHEXA_CODIGO)
      REFERENCES GHEXA_EXAME (GHEXA_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE GHRES_RESUL ADD CONSTRAINT FK_DSO_RES FOREIGN KEY (GHSOL_CODIGO, GHEXA_CODIGO)
      REFERENCES GHDSO_DETSO (GHSOL_CODIGO, GHEXA_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE GHRES_RESUL ADD CONSTRAINT FK_EMP_RES FOREIGN KEY (PEEMP_CODIGO)
      REFERENCES PEEMP_EMPLE (PEEMP_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE GHINF_INFIM ADD CONSTRAINT FK_DSO_INF FOREIGN KEY (GHSOL_CODIGO, GHEXA_CODIGO)
      REFERENCES GHDSO_DETSO (GHSOL_CODIGO, GHEXA_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE GHINF_INFIM ADD CONSTRAINT FK_EMP_INF FOREIGN KEY (PEEMP_CODIGO)
      REFERENCES PEEMP_EMPLE (PEEMP_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE GHLOT_LOTE ADD CONSTRAINT FK_MED_LOT FOREIGN KEY (GHMED_CODIGO)
      REFERENCES GHMED_MEDIC (GHMED_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE GHMOV_MOVIN ADD CONSTRAINT FK_LOT_MOV FOREIGN KEY (GHLOT_CODIGO)
      REFERENCES GHLOT_LOTE (GHLOT_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE GHMOV_MOVIN ADD CONSTRAINT FK_EMP_MOV FOREIGN KEY (PEEMP_CODIGO)
      REFERENCES PEEMP_EMPLE (PEEMP_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE GHREC_RECET ADD CONSTRAINT FK_EPI_REC FOREIGN KEY (GHEPI_CODIGO)
      REFERENCES GHEPI_EPISO (GHEPI_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE GHREC_RECET ADD CONSTRAINT FK_EMP_REC FOREIGN KEY (PEEMP_CODIGO)
      REFERENCES PEEMP_EMPLE (PEEMP_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE GHDRE_DETRE ADD CONSTRAINT FK_REC_DRE FOREIGN KEY (GHREC_CODIGO)
      REFERENCES GHREC_RECET (GHREC_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE GHDRE_DETRE ADD CONSTRAINT FK_MED_DRE FOREIGN KEY (GHMED_CODIGO)
      REFERENCES GHMED_MEDIC (GHMED_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE GHDIS_DISPE ADD CONSTRAINT FK_DRE_DIS FOREIGN KEY (GHREC_CODIGO, GHMED_CODIGO)
      REFERENCES GHDRE_DETRE (GHREC_CODIGO, GHMED_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE GHDIS_DISPE ADD CONSTRAINT FK_LOT_DIS FOREIGN KEY (GHLOT_CODIGO)
      REFERENCES GHLOT_LOTE (GHLOT_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE GHDIS_DISPE ADD CONSTRAINT FK_EMP_DIS FOREIGN KEY (PEEMP_CODIGO)
      REFERENCES PEEMP_EMPLE (PEEMP_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE GHHAB_HABIT ADD CONSTRAINT FK_DEP_HAB FOREIGN KEY (PEDEP_CODIGO)
      REFERENCES PEDEP_DEPAR (PEDEP_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE GHCAM_CAMA ADD CONSTRAINT FK_HAB_CAM FOREIGN KEY (GHHAB_CODIGO)
      REFERENCES GHHAB_HABIT (GHHAB_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE GHHOS_HOSPI ADD CONSTRAINT FK_EPI_HOS FOREIGN KEY (GHEPI_CODIGO)
      REFERENCES GHEPI_EPISO (GHEPI_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE GHHOS_HOSPI ADD CONSTRAINT FK_CAM_HOS FOREIGN KEY (GHCAM_CODIGO)
      REFERENCES GHCAM_CAMA (GHCAM_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE GHHOS_HOSPI ADD CONSTRAINT FK_EMP_HOS FOREIGN KEY (PEEMP_CODIGO)
      REFERENCES PEEMP_EMPLE (PEEMP_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE GHTRA_TRASL ADD CONSTRAINT FK_HOS_TRA FOREIGN KEY (GHHOS_CODIGO)
      REFERENCES GHHOS_HOSPI (GHHOS_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE GHTRA_TRASL ADD CONSTRAINT FK_CAM_TRA FOREIGN KEY (GHCAM_CODIGO)
      REFERENCES GHCAM_CAMA (GHCAM_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE GHTRA_TRASL ADD CONSTRAINT FK_EMP_TRA FOREIGN KEY (PEEMP_CODIGO)
      REFERENCES PEEMP_EMPLE (PEEMP_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE GHCIR_CIRUG ADD CONSTRAINT FK_EPI_CIR FOREIGN KEY (GHEPI_CODIGO)
      REFERENCES GHEPI_EPISO (GHEPI_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE GHCIR_CIRUG ADD CONSTRAINT FK_SAL_CIR FOREIGN KEY (GHSAL_CODIGO)
      REFERENCES GHSAL_SALA (GHSAL_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE GHCIR_CIRUG ADD CONSTRAINT FK_EMP_CIR FOREIGN KEY (PEEMP_CODIGO)
      REFERENCES PEEMP_EMPLE (PEEMP_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE FICUE_CUENT ADD CONSTRAINT FK_EPI_CUE FOREIGN KEY (GHEPI_CODIGO)
      REFERENCES GHEPI_EPISO (GHEPI_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE FICAR_CARGO ADD CONSTRAINT FK_CUE_CRG FOREIGN KEY (FICUE_CODIGO)
      REFERENCES FICUE_CUENT (FICUE_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE FICAR_CARGO ADD CONSTRAINT FK_TAR_CRG FOREIGN KEY (FITAR_CODIGO)
      REFERENCES FITAR_TARIF (FITAR_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE FICOM_COMPR ADD CONSTRAINT FK_CUE_COM FOREIGN KEY (FICUE_CODIGO)
      REFERENCES FICUE_CUENT (FICUE_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE FICOM_COMPR ADD CONSTRAINT FK_EMP_COM FOREIGN KEY (PEEMP_CODIGO)
      REFERENCES PEEMP_EMPLE (PEEMP_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE FIPAG_PAGO ADD CONSTRAINT FK_COM_PAG FOREIGN KEY (FICOM_CODIGO)
      REFERENCES FICOM_COMPR (FICOM_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE FIPAG_PAGO ADD CONSTRAINT FK_FPA_PAG FOREIGN KEY (FIFPA_CODIGO)
      REFERENCES FIFPA_FORPA (FIFPA_CODIGO) ON DELETE RESTRICT ON UPDATE RESTRICT;

