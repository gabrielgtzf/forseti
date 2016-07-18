/*
    Forseti, El ERP Gratuito para PyMEs
    Copyright (C) 2015 Gabriel Gutiérrez Fuentes.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
 
CREATE OR REPLACE FUNCTION getfsipass()
  RETURNS varchar AS
$BODY$  
BEGIN
	return 'fsi';
END
$BODY$
  LANGUAGE 'plpgsql';

--@FIN_BLOQUE
CREATE TABLE tbl_msj
(
  alc character(3) NOT NULL,
  mod character varying(30) NOT NULL,
  sub character varying(30) NOT NULL,
  elm character varying(50) NOT NULL,
  msj1 character varying(255) NOT NULL,
  msj2 character varying(255),
  msj3 character varying(255),
  msj4 character varying(255),
  msj5 character varying(255),
  CONSTRAINT pk_tbl_msj PRIMARY KEY (alc, mod, sub, elm)
);

--@FIN_BLOQUE
CREATE TABLE tbl_bd
(
  id_bd serial NOT NULL,
  nombre character varying(30) NOT NULL,
  usuario character varying(30) NOT NULL,
  "password" character varying(30) NOT NULL,
  fechaalta date NOT NULL,
  compania character varying(255) NOT NULL DEFAULT ''::character varying,
  direccion character varying(255) NOT NULL DEFAULT ''::character varying,
  poblacion character varying(50) NOT NULL DEFAULT ''::character varying,
  cp character varying(9) NOT NULL DEFAULT ''::character varying,
  mail character varying(255) NOT NULL DEFAULT ''::character varying,
  web character varying(255) NOT NULL DEFAULT ''::character varying,
  su character varying(10) NOT NULL,
  rfc character varying(15) NOT NULL DEFAULT ''::character varying,
  cfd smallint NOT NULL DEFAULT (0)::smallint,
  cfd_calle character varying(80) NOT NULL DEFAULT ''::character varying,
  cfd_noext character varying(10) NOT NULL DEFAULT ''::character varying,
  cfd_noint character varying(10) NOT NULL DEFAULT ''::character varying,
  cfd_colonia character varying(40) NOT NULL DEFAULT ''::character varying,
  cfd_localidad character varying(80) NOT NULL DEFAULT ''::character varying,
  cfd_municipio character varying(40) NOT NULL DEFAULT ''::character varying,
  cfd_estado character varying(40) NOT NULL DEFAULT ''::character varying,
  cfd_pais character varying(20) NOT NULL DEFAULT ''::character varying,
  cfd_cp character varying(7) NOT NULL DEFAULT ''::character varying,
  cfd_regimenfiscal character varying(255) NOT NULL DEFAULT ''::character varying,
  CONSTRAINT pk_tbl_bd PRIMARY KEY (id_bd)
);

--@FIN_BLOQUE
CREATE TABLE tbl_registros
(
  id_registro serial NOT NULL,
  ip character varying(255) NOT NULL,
  host character varying(255) NOT NULL,
  fecha timestamp without time zone NOT NULL,
  id_sesion character varying(255) NOT NULL,
  fechadesde timestamp without time zone NOT NULL,
  fechahasta timestamp without time zone NOT NULL,
  status character(1) NOT NULL,
  fsibd character varying(255) NOT NULL,
  id_usuario character varying(255) NOT NULL,
  "password" character varying(255) NOT NULL,
  id_tipo character(3) NOT NULL,
  CONSTRAINT pk_tbl_registros PRIMARY KEY (id_registro)
);

--@FIN_BLOQUE
CREATE TABLE tbl_regproc
(
  id_regproc serial NOT NULL,
  id_tipo character(3) NOT NULL,
  fsibd character varying(255) NOT NULL,
  fecha timestamp without time zone NOT NULL,
  status character(2) NOT NULL,
  id_usuario character varying(255) NOT NULL,
  id_proceso character varying(255) NOT NULL,
  id_modulo character varying(100) NOT NULL,
  resultado character varying(8000) NOT NULL,
  CONSTRAINT pk_tbl_regproc PRIMARY KEY (id_regproc)
);

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION rdp(_id_tipo character, _fsibd character, _status character, _id_usuario character varying, _id_proceso character varying, _id_modulo character varying, _resultado character varying)
  RETURNS void AS
$BODY$  
BEGIN
	INSERT INTO TBL_REGPROC
	VALUES(default, _id_tipo, _fsibd, LOCALTIMESTAMP, _status, _id_usuario, _id_proceso, _id_modulo, _resultado);
END
$BODY$
  LANGUAGE 'plpgsql';

--@FIN_BLOQUE
CREATE OR REPLACE VIEW view_regproc_modulo AS 
 SELECT p.id_regproc, p.id_tipo, p.fsibd, p.fecha, p.status, p.id_usuario, p.id_proceso, m.msj2 AS proceso, p.id_modulo, p.resultado
   FROM tbl_regproc p
   JOIN tbl_msj m ON m.alc::text = p.id_tipo::text AND m.mod::text = 'PERMISOS'::text AND m.sub::text = 'CATALOGO'::text AND m.elm::text = p.id_proceso::text;


--@FIN_BLOQUE
CREATE TABLE tbl_variables
(
  id_variable character varying(10) NOT NULL,
  descripcion character varying(100) NOT NULL,
  ventero integer,
  vdecimal numeric(18,6),
  vfecha timestamp without time zone,
  valfanumerico character varying(255) NOT NULL,
  desistema smallint NOT NULL,
  CONSTRAINT pk_tbl_variables PRIMARY KEY (id_variable)
);

--@FIN_BLOQUE
CREATE OR REPLACE VIEW view_variables_modulo AS 
 SELECT v.id_variable, v.descripcion, ''::character varying AS tipo, v.ventero, v.vdecimal, v.vfecha, v.valfanumerico, v.desistema, ''::character varying AS modulo
   FROM tbl_variables v;
  
INSERT INTO TBL_VARIABLES
VALUES('SERVIDOR','Nombre del servidor',null,null,null,'GNU-FORSETI',1);

INSERT INTO TBL_VARIABLES
VALUES('VERSION','Version de este servidor',0,4.0,null,'4.0.0',1);

INSERT INTO TBL_VARIABLES
VALUES('SRV-PRUEBA','¿ Es este un servidor de pruebas ?',0,null,null,'',1);

INSERT INTO TBL_VARIABLES
VALUES('CREARBDEXT','¿ Permite crear bases de datos desde el exterior ?',0,null,null,'',1);

INSERT INTO TBL_VARIABLES
VALUES('BD-EJE','Nombre de la base de datos eje',null,null,null,'FSIBD_FORSETI',1);

INSERT INTO TBL_VARIABLES
VALUES('CRM-EJE','ID del area de trabajo del CRM de la base de datos eje',0,null,null,'',1);

INSERT INTO TBL_VARIABLES
VALUES('TOMCAT','Ruta de instalacion de tomcat',null,null,null,'NC',1);

INSERT INTO TBL_VARIABLES
VALUES('JVM','Ruta de instalacion de OpenJDK o Java JDK',null,null,null,'NC',1);

INSERT INTO TBL_VARIABLES
VALUES('ACTUALIZAR','URL de actualizaciones forseti',null,null,null,'https://s3-us-west-2.amazonaws.com/forseti.org.mx/descargas/actualizaciones/estable',1);

INSERT INTO TBL_VARIABLES
VALUES('POSTGRESQL','Infraestructura PostgreSQL',null,null,null,'general',1);

INSERT INTO TBL_VARIABLES
VALUES('RESPALDOS','Ruta para los respaldos de infraestructura general',null,null,null,'NC',1);

INSERT INTO TBL_VARIABLES
VALUES('AUTO_HORA','Hora de tareas automaticas',null,null,'01/01/1970 23:59:00','',1);

INSERT INTO TBL_VARIABLES
VALUES('AUTO_ACT','Actualizar automatico',0,null,null,'',1);

INSERT INTO TBL_VARIABLES
VALUES('AUTO_RESP','Respaldos Automaticos',0,null,null,'',1);

INSERT INTO TBL_VARIABLES
VALUES('AUTO_SLDS','Actualizacion de saldos y otros automatico',0,null,null,'',1);

INSERT INTO TBL_VARIABLES
VALUES('PAC_PURL','URL de conexion al servidor JPacConn con puerto explicito',null,null,null,'127.0.0.1:8443',1);

INSERT INTO TBL_VARIABLES
VALUES('PAC_SERV','ID de este servidor para Timbrar en un servicio de JPacConn',null,null,null,'111111111111111111111111111111111111',1);

INSERT INTO TBL_VARIABLES
VALUES('PAC_USER','Usuario para el JPacConn',null,null,null,'cambiarlo',1);

INSERT INTO TBL_VARIABLES
VALUES('PAC_PASS','',null,null,null,'cambiarlo',1);

INSERT INTO TBL_VARIABLES
VALUES('EDI_PURL','URL de conexion al PAC',null,null,null,'NC',1);

INSERT INTO TBL_VARIABLES
VALUES('EDI_USER','Usuario para el PAC',null,null,null,'cambiarlo',1);

INSERT INTO TBL_VARIABLES
VALUES('EDI_PASS','',null,null,null,'cambiarlo',1);

INSERT INTO TBL_VARIABLES
VALUES('SMTP_HOST','URL de conexion al servidor SMTP',null,null,null,'false',1);

INSERT INTO TBL_VARIABLES
VALUES('SMTP_PORT','Puerto del SMTP',25,null,null,'',1);

INSERT INTO TBL_VARIABLES
VALUES('SMTP_USER','Usuario del SMTP',null,null,null,'cambiarlo',1);

INSERT INTO TBL_VARIABLES
VALUES('SMTP_PASS','',null,null,null,'cambiarlo',1);

INSERT INTO TBL_VARIABLES
VALUES('S3_BUKT','Nombre del Bucket en el servicio Amazon S3',null,null,null,'NC',1);

INSERT INTO TBL_VARIABLES
VALUES('S3_USER','Access Key del servicio Amazon S3',null,null,null,'cambiarlo',1);

INSERT INTO TBL_VARIABLES
VALUES('S3_PASS','',null,null,null,'cambiarlo',1);

INSERT INTO TBL_VARIABLES
VALUES('IDEMPAYUDA','ID de la empresa del CEF donde buscará los reportes al generar la ayuda ( documentación )',1,null,null,'',1);

INSERT INTO TBL_VARIABLES
VALUES('URLAYUDA','La URL relativa o absoluta para la búsqueda de la ayuda de forseti ( documentación )',null,null,null,'http://www.forseti.org.mx/forsetidoc/',1);

--@FIN_BLOQUE
CREATE TABLE tbl_usuarios
(
  id_usuario character varying(10) NOT NULL,
  "password" character varying(10) NOT NULL,
  nombre character varying(80) NOT NULL,
  CONSTRAINT pk_tbl_usuarios PRIMARY KEY (id_usuario)
);

--@FIN_BLOQUE
CREATE TABLE tbl_usuarios_permisos_catalogo
(
  id_permiso character varying(30) NOT NULL,
  CONSTRAINT pk_tbl_usuarios_permisos_catalogo PRIMARY KEY (id_permiso)
);

INSERT INTO TBL_USUARIOS_PERMISOS_CATALOGO
VALUES('ADMIN');

INSERT INTO TBL_USUARIOS_PERMISOS_CATALOGO
VALUES('ADMIN_BD');

INSERT INTO TBL_USUARIOS_PERMISOS_CATALOGO
VALUES('ADMIN_BD_PROPIEDADES');

INSERT INTO TBL_USUARIOS_PERMISOS_CATALOGO
VALUES('ADMIN_BD_CREAR');

INSERT INTO TBL_USUARIOS_PERMISOS_CATALOGO
VALUES('ADMIN_BD_CAMBIAR');

INSERT INTO TBL_USUARIOS_PERMISOS_CATALOGO
VALUES('ADMIN_BD_ELIMINAR');

INSERT INTO TBL_USUARIOS_PERMISOS_CATALOGO
VALUES('ADMIN_BD_GENREP');

INSERT INTO TBL_USUARIOS_PERMISOS_CATALOGO
VALUES('ADMIN_AYUDA');

INSERT INTO TBL_USUARIOS_PERMISOS_CATALOGO
VALUES('ADMIN_AYUDA_CREAR');

INSERT INTO TBL_USUARIOS_PERMISOS_CATALOGO
VALUES('ADMIN_AYUDA_CAMBIAR');

INSERT INTO TBL_USUARIOS_PERMISOS_CATALOGO
VALUES('ADMIN_AYUDA_ELIMINAR');

INSERT INTO TBL_USUARIOS_PERMISOS_CATALOGO
VALUES('ADMIN_AYUDA_GENERAR');

INSERT INTO TBL_USUARIOS_PERMISOS_CATALOGO
VALUES('ADMIN_SSL');

INSERT INTO TBL_USUARIOS_PERMISOS_CATALOGO
VALUES('ADMIN_SSL_MANEJO');


INSERT INTO TBL_USUARIOS_PERMISOS_CATALOGO
VALUES('ADMIN_USUARIOS');

INSERT INTO TBL_USUARIOS_PERMISOS_CATALOGO
VALUES('ADMIN_USUARIOS_AGREGAR');

INSERT INTO TBL_USUARIOS_PERMISOS_CATALOGO
VALUES('ADMIN_USUARIOS_ELIMINAR');


INSERT INTO TBL_USUARIOS_PERMISOS_CATALOGO
VALUES('REGIST');

INSERT INTO TBL_USUARIOS_PERMISOS_CATALOGO
VALUES('REGIST_INISES');

INSERT INTO TBL_USUARIOS_PERMISOS_CATALOGO
VALUES('REGIST_INISES_TRUNCAR');

INSERT INTO TBL_USUARIOS_PERMISOS_CATALOGO
VALUES('REGIST_INISES_ELIMINAR');


INSERT INTO TBL_USUARIOS_PERMISOS_CATALOGO
VALUES('REGIST_INISES_DESBLOQUEAR');

INSERT INTO TBL_USUARIOS_PERMISOS_CATALOGO
VALUES('REGIST_PROC');

INSERT INTO TBL_USUARIOS_PERMISOS_CATALOGO
VALUES('REGIST_PROC_TRUNCAR');

INSERT INTO TBL_USUARIOS_PERMISOS_CATALOGO
VALUES('REGIST_PROC_LIBERAR');


INSERT INTO TBL_USUARIOS_PERMISOS_CATALOGO
VALUES('REGIST_PROC_RASTREAR');

INSERT INTO TBL_USUARIOS_PERMISOS_CATALOGO
VALUES('REGIST_ADMIN');

INSERT INTO TBL_USUARIOS_PERMISOS_CATALOGO
VALUES('REGIST_ADMIN_TRUNCAR');

INSERT INTO TBL_USUARIOS_PERMISOS_CATALOGO
VALUES('REGIST_ADMIN_LIBERAR');

INSERT INTO TBL_USUARIOS_PERMISOS_CATALOGO
VALUES('REPS');

INSERT INTO TBL_USUARIOS_PERMISOS_CATALOGO
VALUES('REPS_REPORTES');

--@FIN_BLOQUE
CREATE TABLE tbl_usuarios_permisos
(
  id_usuario character varying(10) NOT NULL,
  id_permiso character varying(30) NOT NULL,
  permitido bit(1) NOT NULL,
  CONSTRAINT pk_tbl_usuarios_permisos PRIMARY KEY (id_usuario, id_permiso),
  CONSTRAINT fk_tbl_usuarios_permisos_tbl_usuarios FOREIGN KEY (id_usuario)
      REFERENCES tbl_usuarios (id_usuario) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_tbl_usuarios_permisos_tbl_usuarios_permisos_catalogo FOREIGN KEY (id_permiso)
      REFERENCES tbl_usuarios_permisos_catalogo (id_permiso) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

--@FIN_BLOQUE
CREATE TABLE tbl_usuarios_permisos_catalogo_cef
(
  id_permiso character varying(30) NOT NULL,
  CONSTRAINT pk_tbl_usuarios_permisos_catalogo_cef PRIMARY KEY (id_permiso)
);

--@FIN_BLOQUE
insert into tbl_usuarios_permisos_catalogo_cef
values('ADM');
insert into tbl_usuarios_permisos_catalogo_cef
values('ADM_AWSS3');
insert into tbl_usuarios_permisos_catalogo_cef
values('ADM_AWSS3_GESTIONAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('ADM_CFDI');
insert into tbl_usuarios_permisos_catalogo_cef
values('ADM_CFDI_AGREGAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('ADM_CFDI_CARGAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('ADM_CFDI_CARGAREXT');
insert into tbl_usuarios_permisos_catalogo_cef
values('ADM_CFDI_DESENLAZAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('ADM_CFDI_GCEXML');
insert into tbl_usuarios_permisos_catalogo_cef
values('ADM_CFDI_GESTIONAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('ADM_ENTIDADES');
insert into tbl_usuarios_permisos_catalogo_cef
values('ADM_ENTIDADES_AGREGAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('ADM_FORMATOS');
insert into tbl_usuarios_permisos_catalogo_cef
values('ADM_FORMATOS_AGREGAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('ADM_MONEDAS');
insert into tbl_usuarios_permisos_catalogo_cef
values('ADM_MONEDAS_AGREGAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('ADM_PERIODOS');
insert into tbl_usuarios_permisos_catalogo_cef
values('ADM_PERIODOS_AGREGAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('ADM_SALDOS');
insert into tbl_usuarios_permisos_catalogo_cef
values('ADM_SALDOS_INDIV');
insert into tbl_usuarios_permisos_catalogo_cef
values('ADM_SALDOS_TODO');
insert into tbl_usuarios_permisos_catalogo_cef
values('ADM_USUARIOS');
insert into tbl_usuarios_permisos_catalogo_cef
values('ADM_USUARIOS_AGREGAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('ADM_USUARIOS_ELIMINAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('ADM_VARIABLES');
insert into tbl_usuarios_permisos_catalogo_cef
values('ADM_VARIABLES_AGREGAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('ADM_VENDEDORES');
insert into tbl_usuarios_permisos_catalogo_cef
values('ADM_VENDEDORES_AGREGAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('ADM_VENDEDORES_CAMBIAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('ADM_VENDEDORES_ELIMINAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('ALM');
insert into tbl_usuarios_permisos_catalogo_cef
values('ALM_CHFIS');
insert into tbl_usuarios_permisos_catalogo_cef
values('ALM_CHFIS_AGREGAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('ALM_CHFIS_CAMBIAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('ALM_CHFIS_CANCELAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('ALM_CHFIS_CONSULTAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('ALM_CHFIS_GENPROC');
insert into tbl_usuarios_permisos_catalogo_cef
values('ALM_MOVIM');
insert into tbl_usuarios_permisos_catalogo_cef
values('ALM_MOVIM_AGREGAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('ALM_MOVIM_AUDITAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('ALM_MOVIM_CANCELAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('ALM_MOVIM_CONSULTAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('ALM_MOVPLANT');
insert into tbl_usuarios_permisos_catalogo_cef
values('ALM_MOVPLANT_AGREGAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('ALM_MOVPLANT_CAMBIAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('ALM_MOVPLANT_CANCELAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('ALM_MOVPLANT_CONSULTAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('ALM_MOVPLANT_GENERAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('ALM_REQUERIMIENTOS');
insert into tbl_usuarios_permisos_catalogo_cef
values('ALM_REQUERIMIENTOS_AGREGAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('ALM_REQUERIMIENTOS_CAMBIAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('ALM_REQUERIMIENTOS_CANCELAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('ALM_REQUERIMIENTOS_CONSULTAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('ALM_REQUERIMIENTOS_TRASPASAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('ALM_TRASPASOS');
insert into tbl_usuarios_permisos_catalogo_cef
values('ALM_TRASPASOS_AGREGAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('ALM_TRASPASOS_CANCELAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('ALM_TRASPASOS_CONSULTAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('ALM_UTENSILIOS');
insert into tbl_usuarios_permisos_catalogo_cef
values('ALM_UTENSILIOS_CANCELAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('ALM_UTENSILIOS_ENTRADA');
insert into tbl_usuarios_permisos_catalogo_cef
values('ALM_UTENSILIOS_SALIDA');
insert into tbl_usuarios_permisos_catalogo_cef
values('BANCAJ');
insert into tbl_usuarios_permisos_catalogo_cef
values('BANCAJ_BANCOS');
insert into tbl_usuarios_permisos_catalogo_cef
values('BANCAJ_BANCOS_AGREGAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('BANCAJ_BANCOS_CANCELAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('BANCAJ_BANCOS_CONSULTAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('BANCAJ_BANCOS_GENPROC');
insert into tbl_usuarios_permisos_catalogo_cef
values('BANCAJ_BANCOS_TRASPASAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('BANCAJ_CAJAS');
insert into tbl_usuarios_permisos_catalogo_cef
values('BANCAJ_CAJAS_AGREGAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('BANCAJ_CAJAS_CANCELAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('BANCAJ_CAJAS_CONSULTAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('BANCAJ_CAJAS_GENPROC');
insert into tbl_usuarios_permisos_catalogo_cef
values('BANCAJ_CAJAS_TRASPASAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('BANCAJ_CIERRES');
insert into tbl_usuarios_permisos_catalogo_cef
values('BANCAJ_CIERRES_AGREGAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('BANCAJ_CIERRES_CANCELAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('BANCAJ_VALES');
insert into tbl_usuarios_permisos_catalogo_cef
values('BANCAJ_VALES_AGREGAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('BANCAJ_VALES_CAMBIAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('BANCAJ_VALES_ELIMINAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('BANCAJ_VALES_GENPROC');
insert into tbl_usuarios_permisos_catalogo_cef
values('BANCAJ_VALES_TRASPASAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('COMP');
insert into tbl_usuarios_permisos_catalogo_cef
values('COMP_CXP');
insert into tbl_usuarios_permisos_catalogo_cef
values('COMP_CXP_AGREGAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('COMP_CXP_CANCELAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('COMP_CXP_CONSULTAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('COMP_CXP_PAGAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('COMP_CXP_SALDAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('COMP_DEV');
insert into tbl_usuarios_permisos_catalogo_cef
values('COMP_DEV_CANCELAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('COMP_DEV_CONSULTAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('COMP_DEV_DEVOLVER');
insert into tbl_usuarios_permisos_catalogo_cef
values('COMP_DEV_REBAJAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('COMP_FAC');
insert into tbl_usuarios_permisos_catalogo_cef
values('COMP_FAC_AGREGAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('COMP_FAC_CANCELAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('COMP_FAC_CONSULTAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('COMP_GAS');
insert into tbl_usuarios_permisos_catalogo_cef
values('COMP_GAS_AGREGAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('COMP_GAS_CANCELAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('COMP_GAS_CONSULTAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('COMP_ORD');
insert into tbl_usuarios_permisos_catalogo_cef
values('COMP_ORD_AGREGAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('COMP_ORD_CAMBIAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('COMP_ORD_CANCELAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('COMP_ORD_CONSULTAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('COMP_POL');
insert into tbl_usuarios_permisos_catalogo_cef
values('COMP_POL_PRODUCTOS');
insert into tbl_usuarios_permisos_catalogo_cef
values('COMP_PROVEE');
insert into tbl_usuarios_permisos_catalogo_cef
values('COMP_PROVEE_AGREGAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('COMP_PROVEE_CAMBIAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('COMP_REC');
insert into tbl_usuarios_permisos_catalogo_cef
values('COMP_REC_AGREGAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('COMP_REC_CANCELAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('COMP_REC_CONSULTAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('CONT');
insert into tbl_usuarios_permisos_catalogo_cef
values('CONT_CATCUENTAS');
insert into tbl_usuarios_permisos_catalogo_cef
values('CONT_CATCUENTAS_CAMBIAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('CONT_CATCUENTAS_CREAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('CONT_CATCUENTAS_ELIMINAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('CONT_ENLACES');
insert into tbl_usuarios_permisos_catalogo_cef
values('CONT_ENLACES_AGREGAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('CONT_ENLACES_CAMBIAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('CONT_ENLACES_ELIMINAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('CONT_POLCIERRE');
insert into tbl_usuarios_permisos_catalogo_cef
values('CONT_POLCIERRE_CAMBIAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('CONT_POLCIERRE_CANCELAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('CONT_POLCIERRE_CREAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('CONT_POLCIERRE_ELIMINAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('CONT_POLCIERRE_GENERAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('CONT_POLIZAS');
insert into tbl_usuarios_permisos_catalogo_cef
values('CONT_POLIZAS_CAMBIAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('CONT_POLIZAS_CANCELAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('CONT_POLIZAS_CE');
insert into tbl_usuarios_permisos_catalogo_cef
values('CONT_POLIZAS_CONSULTAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('CONT_POLIZAS_CREAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('CONT_RUBROS');
insert into tbl_usuarios_permisos_catalogo_cef
values('CONT_RUBROS_CAMBIAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('CONT_RUBROS_CREAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('CONT_RUBROS_ELIMINAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('CONT_TIPOPOLIZA');
insert into tbl_usuarios_permisos_catalogo_cef
values('CONT_TIPOPOLIZA_CAMBIAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('CONT_TIPOPOLIZA_CREAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('CONT_TIPOPOLIZA_ELIMINAR');
insert into tbl_usuarios_permisos_catalogo_cef
VALUES('CRM');
insert into tbl_usuarios_permisos_catalogo_cef
values('CRM_CALENDARIO');
insert into tbl_usuarios_permisos_catalogo_cef
values('CRM_CALENDARIO_ELIMINAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('CRM_CALENDARIO_GESTIONAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('CRM_COMPANIAS');
insert into tbl_usuarios_permisos_catalogo_cef
values('CRM_COMPANIAS_ELIMINAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('CRM_COMPANIAS_GESTIONAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('CRM_OPORTUNIDADES');
insert into tbl_usuarios_permisos_catalogo_cef
values('CRM_OPORTUNIDADES_ELIMINAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('CRM_OPORTUNIDADES_GESTIONAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('CRM_PERSONAS');
insert into tbl_usuarios_permisos_catalogo_cef
values('CRM_PERSONAS_ELIMINAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('CRM_PERSONAS_GESTIONAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('INVSERV');
insert into tbl_usuarios_permisos_catalogo_cef
values('INVSERV_GASTOS');
insert into tbl_usuarios_permisos_catalogo_cef
values('INVSERV_GASTOS_AGREGAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('INVSERV_GASTOS_CAMBIAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('INVSERV_LINEAS');
insert into tbl_usuarios_permisos_catalogo_cef
values('INVSERV_LINEAS_AGREGAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('INVSERV_LINEAS_CAMBIAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('INVSERV_PROD');
insert into tbl_usuarios_permisos_catalogo_cef
values('INVSERV_PROD_AGREGAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('INVSERV_PROD_CAMBIAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('INVSERV_SERV');
insert into tbl_usuarios_permisos_catalogo_cef
values('INVSERV_SERV_AGREGAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('INVSERV_SERV_CAMBIAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('NOM');
insert into tbl_usuarios_permisos_catalogo_cef
values('NOM_AGUINALDO');
insert into tbl_usuarios_permisos_catalogo_cef
values('NOM_AGUINALDO_AGREGAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('NOM_AGUINALDO_CAMBIAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('NOM_AGUINALDO_ELIMINAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('NOM_ASISTENCIAS');
insert into tbl_usuarios_permisos_catalogo_cef
values('NOM_ASISTENCIAS_AGREGAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('NOM_ASISTENCIAS_CAMBIAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('NOM_ASISTENCIAS_ELIMINAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('NOM_CATEGORIAS');
insert into tbl_usuarios_permisos_catalogo_cef
values('NOM_CATEGORIAS_AGREGAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('NOM_CATEGORIAS_CAMBIAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('NOM_CIERRE');
insert into tbl_usuarios_permisos_catalogo_cef
values('NOM_CIERRE_AGREGAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('NOM_CIERRE_CAMBIAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('NOM_CREDSAL');
insert into tbl_usuarios_permisos_catalogo_cef
values('NOM_CREDSAL_AGREGAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('NOM_CREDSAL_CAMBIAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('NOM_CREDSAL_ELIMINAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('NOM_DEPARTAMENTOS');
insert into tbl_usuarios_permisos_catalogo_cef
values('NOM_DEPARTAMENTOS_AGREGAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('NOM_DEPARTAMENTOS_CAMBIAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('NOM_EMPLEADOS');
insert into tbl_usuarios_permisos_catalogo_cef
values('NOM_EMPLEADOS_AGREGAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('NOM_EMPLEADOS_CAMBIAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('NOM_FONACOT');
insert into tbl_usuarios_permisos_catalogo_cef
values('NOM_FONACOT_AGREGAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('NOM_FONACOT_CAMBIAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('NOM_FONACOT_ELIMINAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('NOM_IMSS');
insert into tbl_usuarios_permisos_catalogo_cef
values('NOM_IMSS_AGREGAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('NOM_IMSS_CAMBIAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('NOM_IMSS_ELIMINAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('NOM_ISR');
insert into tbl_usuarios_permisos_catalogo_cef
values('NOM_ISR_AGREGAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('NOM_ISR_CAMBIAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('NOM_ISR_ELIMINAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('NOM_MOVIMIENTOS');
insert into tbl_usuarios_permisos_catalogo_cef
values('NOM_MOVIMIENTOS_AGREGAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('NOM_MOVIMIENTOS_CAMBIAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('NOM_NOMINA');
insert into tbl_usuarios_permisos_catalogo_cef
values('NOM_NOMINA_AGREGAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('NOM_NOMINA_CAMBIAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('NOM_NOMINA_ELIMINAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('NOM_PERMISOS');
insert into tbl_usuarios_permisos_catalogo_cef
values('NOM_PERMISOS_AGREGAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('NOM_PERMISOS_CAMBIAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('NOM_PERMISOS_ELIMINAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('NOM_PLANTILLAS');
insert into tbl_usuarios_permisos_catalogo_cef
values('NOM_PLANTILLAS_AGREGAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('NOM_PLANTILLAS_CAMBIAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('NOM_PLANTILLAS_ELIMINAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('NOM_TURNOS');
insert into tbl_usuarios_permisos_catalogo_cef
values('NOM_TURNOS_AGREGAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('NOM_TURNOS_CAMBIAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('NOM_VACACIONES');
insert into tbl_usuarios_permisos_catalogo_cef
values('NOM_VACACIONES_AGREGAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('NOM_VACACIONES_CAMBIAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('NOM_VACACIONES_ELIMINAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('PROD');
insert into tbl_usuarios_permisos_catalogo_cef
values('PROD_FORMULAS');
insert into tbl_usuarios_permisos_catalogo_cef
values('PROD_FORMULAS_AGREGAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('PROD_FORMULAS_CAMBIAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('PROD_PRODUCCION');
insert into tbl_usuarios_permisos_catalogo_cef
values('PROD_PRODUCCION_AGREGAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('PROD_PRODUCCION_CANCELAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('PROD_PRODUCCION_CONSULTAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('REP');
insert into tbl_usuarios_permisos_catalogo_cef
values('REP_REPORTES');
insert into tbl_usuarios_permisos_catalogo_cef
values('VEN');
insert into tbl_usuarios_permisos_catalogo_cef
values('VEN_CLIENT');
insert into tbl_usuarios_permisos_catalogo_cef
values('VEN_CLIENT_AGREGAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('VEN_CLIENT_CAMBIAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('VEN_COT');
insert into tbl_usuarios_permisos_catalogo_cef
values('VEN_COT_AGREGAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('VEN_COT_CAMBIAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('VEN_COT_CANCELAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('VEN_COT_CONSULTAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('VEN_CXC');
insert into tbl_usuarios_permisos_catalogo_cef
values('VEN_CXC_AGREGAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('VEN_CXC_CANCELAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('VEN_CXC_CONSULTAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('VEN_CXC_PAGAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('VEN_CXC_SALDAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('VEN_DEV');
insert into tbl_usuarios_permisos_catalogo_cef
values('VEN_DEV_CANCELAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('VEN_DEV_CONSULTAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('VEN_DEV_DEVOLVER');
insert into tbl_usuarios_permisos_catalogo_cef
values('VEN_DEV_REBAJAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('VEN_FAC');
insert into tbl_usuarios_permisos_catalogo_cef
values('VEN_FAC_AGREGAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('VEN_FAC_CANCELAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('VEN_FAC_CONSULTAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('VEN_PED');
insert into tbl_usuarios_permisos_catalogo_cef
values('VEN_PED_AGREGAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('VEN_PED_CAMBIAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('VEN_PED_CANCELAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('VEN_PED_CONSULTAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('VEN_POL');
insert into tbl_usuarios_permisos_catalogo_cef
values('VEN_POL_CLIENTES');
insert into tbl_usuarios_permisos_catalogo_cef
values('VEN_POL_ENTIDAD');
insert into tbl_usuarios_permisos_catalogo_cef
values('VEN_POL_PRODUCTOS');
insert into tbl_usuarios_permisos_catalogo_cef
values('VEN_REM');
insert into tbl_usuarios_permisos_catalogo_cef
values('VEN_REM_AGREGAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('VEN_REM_CANCELAR');
insert into tbl_usuarios_permisos_catalogo_cef
values('VEN_REM_CONSULTAR');

--@FIN_BLOQUE
CREATE OR REPLACE VIEW view_usuarios_permisos_catalogo_cef AS 
 SELECT p.id_permiso, m.msj1 AS descripcion, m.msj2 AS modulo
   FROM tbl_usuarios_permisos_catalogo_cef p
   JOIN tbl_msj m ON m.alc::text = 'CEF'::text AND m.mod::text = 'PERMISOS'::text AND m.sub::text = 'CATALOGO'::text AND m.elm::text = p.id_permiso::text;

--@FIN_BLOQUE
CREATE OR REPLACE VIEW view_usuarios AS 
 SELECT id_usuario, password, nombre
   FROM tbl_usuarios;

--@FIN_BLOQUE
CREATE OR REPLACE VIEW view_usuarios_permisos AS 
 SELECT c.id_usuario, c.id_permiso, m.msj1 as descripcion, c.permitido
   FROM tbl_usuarios_permisos c
   JOIN tbl_usuarios_permisos_catalogo p ON c.id_permiso::text = p.id_permiso::text
   JOIN tbl_msj m ON m.alc::text = 'SAF' and m.mod::text = 'PERMISOS' and m.sub::text = 'CATALOGO' and m.elm::text = p.id_permiso::text;

--@FIN_BLOQUE
CREATE OR REPLACE VIEW view_usuarios_permisos_catalogo AS 
 SELECT p.id_permiso, m.msj1 as descripcion, m.msj2 as modulo
   FROM tbl_usuarios_permisos_catalogo p 
   JOIN tbl_msj m ON m.alc::text = 'SAF' and m.mod::text = 'PERMISOS' and m.sub::text = 'CATALOGO' and m.elm::text = p.id_permiso::text;

--@FIN_BLOQUE
CREATE TABLE tbl_catalogos
(
  id_catalogo integer NOT NULL,
  nombre character varying(50) NOT NULL,
  select_clause character varying(254) NOT NULL,
  pridefault character varying(254) NOT NULL,
  secdefault character varying(254) NOT NULL,
  seguridad character varying(254) NOT NULL,
  aplrep bit(1) NOT NULL,
  replong bit(1) NOT NULL,
  CONSTRAINT pk_tbl_catalogos PRIMARY KEY (id_catalogo)
);

INSERT INTO tbl_catalogos(id_catalogo, nombre, select_clause, pridefault, secdefault, seguridad,aplrep,replong)
VALUES (1,'USUARIOS',' * from view_catalog_usuarios ',' select Clave, Descripcion from view_catalog_usuarios Order By Clave ASC limit 1 ',' select Clave, Descripcion from view_catalog_usuarios Order By Clave DESC limit 1 ','','1','1');
INSERT INTO tbl_catalogos(id_catalogo, nombre, select_clause, pridefault, secdefault, seguridad,aplrep,replong)
VALUES (2,'FORMATOS',' * from view_catalog_formatos ',' select Clave, Descripcion from view_catalog_formatos Order By Clave ASC limit 1 ',' select Clave, Descripcion from view_catalog_formatos Order By Clave DESC limit 1 ','','1','1');
INSERT INTO tbl_catalogos(id_catalogo, nombre, select_clause, pridefault, secdefault, seguridad,aplrep,replong)
VALUES (3,'CUENTAS CONTABLES',' * from view_catalog_cuentascont ',' select Clave, Descripcion from view_catalog_cuentascont Order By Clave ASC limit 1 ',' select Clave, Descripcion from view_catalog_cuentascont Order By Clave DESC limit 1 ','','1','1');
INSERT INTO tbl_catalogos(id_catalogo, nombre, select_clause, pridefault, secdefault, seguridad,aplrep,replong)
VALUES (4,'LINEAS DE PRODUCTOS',' * FROM view_catalog_prod_lineas ',' select Clave, Descripcion from view_catalog_prod_lineas Order By Clave ASC limit 1 ',' select Clave, Descripcion from view_catalog_prod_lineas Order By Clave DESC limit 1 ','','1','1');
INSERT INTO tbl_catalogos(id_catalogo, nombre, select_clause, pridefault, secdefault, seguridad,aplrep,replong)
VALUES (5,'LINEAS DE SERVICIOS',' * FROM view_catalog_prodserv_lineas ',' select Clave, Descripcion from view_catalog_prodserv_lineas Order By Clave ASC limit 1 ',' select Clave, Descripcion from view_catalog_prodserv_lineas Order By Clave DESC limit 1 ','','1','0');
INSERT INTO tbl_catalogos(id_catalogo, nombre, select_clause, pridefault, secdefault, seguridad,aplrep,replong)
VALUES (6,'LINEAS DE GASTOS',' * FROM view_catalog_gastos_lineas ',' select Clave, Descripcion from view_catalog_gastos_lineas Order By Clave ASC limit 1 ',' select Clave, Descripcion from view_catalog_gastos_lineas Order By Clave DESC limit 1 ','','1','0');
INSERT INTO tbl_catalogos(id_catalogo, nombre, select_clause, pridefault, secdefault, seguridad,aplrep,replong)
VALUES (7,'ENTIDADES DE COMPRAS',' * FROM view_catalog_entidades_compras ',' select Clave, Descripcion from view_catalog_entidades_compras Order By Clave ASC limit 1 ','','COMPRAS','1','0');
INSERT INTO tbl_catalogos(id_catalogo, nombre, select_clause, pridefault, secdefault, seguridad,aplrep,replong)
VALUES (8,'ENTIDADES DE VENTAS',' * FROM view_catalog_entidades_ventas ',' select Clave, Descripcion from view_catalog_entidades_ventas Order By Clave ASC limit 1 ','','VENTAS','1','0');
INSERT INTO tbl_catalogos(id_catalogo, nombre, select_clause, pridefault, secdefault, seguridad,aplrep,replong)
VALUES (9,'CONCEPTOS DE CUENTAS POR PAGAR',' * FROM view_catalog_provee_cxp_conceptos ',' select Clave, Descripcion from view_catalog_cxp_conceptos Order By Clave ASC limit 1 ',' select Clave, Descripcion from view_catalog_cxp_conceptos Order By Clave DESC limit 1 ','','1','1');
INSERT INTO tbl_catalogos(id_catalogo, nombre, select_clause, pridefault, secdefault, seguridad,aplrep,replong)
VALUES (10,'CONCEPTOS DE CUENTAS POR COBRAR',' * FROM view_catalog_client_cxc_conceptos ',' select Clave, Descripcion from view_catalog_cxc_conceptos Order By Clave ASC limit 1 ',' select Clave, Descripcion from view_catalog_cxc_conceptos Order By Clave DESC limit 1 ','','1','1');
INSERT INTO tbl_catalogos(id_catalogo, nombre, select_clause, pridefault, secdefault, seguridad,aplrep,replong)
VALUES (11,'PRODUCTOS (MATERIAS PRIMAS)',' * FROM view_catalog_prod2 ',' select Clave, Descripcion from view_catalog_prod2 Order By Clave ASC limit 1 ',' select Clave, Descripcion from view_catalog_prod2 Order By Clave DESC limit 1 ','','1','1');
INSERT INTO tbl_catalogos(id_catalogo, nombre, select_clause, pridefault, secdefault, seguridad,aplrep,replong)
VALUES (12,'PROVEEDORES',' Clave, Descripcion, Especial FROM view_catalog_provee ','','','','0','1');
INSERT INTO tbl_catalogos(id_catalogo, nombre, select_clause, pridefault, secdefault, seguridad,aplrep,replong)
VALUES (13,'PRODUCTOS Y SERVICIOS',' Clave, Descripcion, Especial FROM view_catalog_prod4 ','','','','0','1');
INSERT INTO tbl_catalogos(id_catalogo, nombre, select_clause, pridefault, secdefault, seguridad,aplrep,replong)
VALUES (14,'CLIENTES',' Clave, Descripcion, Especial FROM view_catalog_client ','','','','0','1');
INSERT INTO tbl_catalogos(id_catalogo, nombre, select_clause, pridefault, secdefault, seguridad,aplrep,replong)
VALUES (15,'GASTOS',' * FROM view_catalog_gastos ',' select Clave, Descripcion from view_catalog_gastos Order By Clave ASC limit 1 ',' select Clave, Descripcion from view_catalog_gastos Order By Clave DESC limit 1 ','','1','1');
INSERT INTO tbl_catalogos(id_catalogo, nombre, select_clause, pridefault, secdefault, seguridad,aplrep,replong)
VALUES (16,'PROVEEDORES',' * FROM view_catalog_provee_poriden ',' select Clave, Descripcion from view_catalog_provee_poriden Order By Clave ASC limit 1 ',' select Clave, Descripcion from view_catalog_provee_poriden Order By Clave DESC limit 1 ','PROVEEDORES','1','1');
INSERT INTO tbl_catalogos(id_catalogo, nombre, select_clause, pridefault, secdefault, seguridad,aplrep,replong)
VALUES (17,'CONCEPTOS DE ENTRADAS AL ALMACEN',' * FROM view_catalog_prod_costos_conceptos_ent ',' select Clave, Descripcion from view_catalog_prod_costos_conceptos_ent Order By Clave ASC limit 1 ',' select Clave, Descripcion from view_catalog_prod_costos_conceptos_ent Order By Clave DESC limit 1 ','','1','1');
INSERT INTO tbl_catalogos(id_catalogo, nombre, select_clause, pridefault, secdefault, seguridad,aplrep,replong)
VALUES (18,'CONCEPTOS DE SALIDAS DEL ALMACEN',' * FROM view_catalog_prod_costos_conceptos_sal ',' select Clave, Descripcion from view_catalog_prod_costos_conceptos_sal Order By Clave ASC limit 1 ',' select Clave, Descripcion from view_catalog_prod_costos_conceptos_sal Order By Clave DESC limit 1 ','','1','1');
INSERT INTO tbl_catalogos(id_catalogo, nombre, select_clause, pridefault, secdefault, seguridad,aplrep,replong)
VALUES (19,'PRODUCTOS',' * FROM view_catalog_prod ',' select Clave, Descripcion from view_catalog_prod Order By Clave ASC limit 1 ',' select Clave, Descripcion from view_catalog_prod Order By Clave DESC limit 1 ','','1','1');
INSERT INTO tbl_catalogos(id_catalogo, nombre, select_clause, pridefault, secdefault, seguridad,aplrep,replong)
VALUES (20,'BODEGAS',' * FROM view_catalog_bodegas ',' select Clave, Descripcion from view_catalog_bodegas Order By Clave ASC limit 1 ','','BODEGAS','1','0');
INSERT INTO tbl_catalogos(id_catalogo, nombre, select_clause, pridefault, secdefault, seguridad,aplrep,replong)
VALUES (21,'PRODUCTOS TERMINADOS',' * FROM view_catalog_prod3 ',' select Clave, Descripcion from view_catalog_prod3 Order By Clave ASC limit 1 ',' select Clave, Descripcion from view_catalog_prod3 Order By Clave DESC limit 1 ','','1','1');
INSERT INTO tbl_catalogos(id_catalogo, nombre, select_clause, pridefault, secdefault, seguridad,aplrep,replong)
VALUES (22,'FORMULAS',' * FROM view_catalog_formulas ',' select Clave, Descripcion from view_catalog_formulas Order By Clave ASC limit 1 ',' select Clave, Descripcion from view_catalog_formulas Order By Clave DESC limit 1 ','','1','1');
INSERT INTO tbl_catalogos(id_catalogo, nombre, select_clause, pridefault, secdefault, seguridad,aplrep,replong)
VALUES (23,'VENDEDORES',' * FROM view_catalog_vendedores ',' select Clave, Descripcion from view_catalog_vendedores Order By Clave ASC limit 1 ','','VENDEDORES','1','0');
INSERT INTO tbl_catalogos(id_catalogo, nombre, select_clause, pridefault, secdefault, seguridad,aplrep,replong)
VALUES (24,'CUENTAS BANCARIAS',' * from view_catalog_bancos ',' select Clave, Descripcion from view_catalog_bancos Order By Clave ASC limit 1 ','','BANCOS','1','0');
INSERT INTO tbl_catalogos(id_catalogo, nombre, select_clause, pridefault, secdefault, seguridad,aplrep,replong)
VALUES (25,'CAJAS',' * from view_catalog_cajas ',' select Clave, Descripcion from view_catalog_cajas Order By Clave ASC limit 1 ','','CAJAS','1','0');
INSERT INTO tbl_catalogos(id_catalogo, nombre, select_clause, pridefault, secdefault, seguridad,aplrep,replong)
VALUES (26,'DEPARTAMENTOS',' * from view_catalog_nomdep ',' select Clave, Descripcion from view_catalog_nomdep Order By Clave ASC limit 1 ',' select Clave, Descripcion from view_catalog_nomdep Order By Clave DESC limit 1 ','','1','1');
INSERT INTO tbl_catalogos(id_catalogo, nombre, select_clause, pridefault, secdefault, seguridad,aplrep,replong)
VALUES (27,'MOVIMIENTOS',' * from view_catalog_movimientos ',' select Clave, Descripcion from view_catalog_movimientos Order By Clave ASC limit 1 ',' select Clave, Descripcion from view_catalog_movimientos Order By Clave DESC limit 1 ','','1','1');
INSERT INTO tbl_catalogos(id_catalogo, nombre, select_clause, pridefault, secdefault, seguridad,aplrep,replong)
VALUES (28,'EMPLEADOS',' Clave, Descripcion, Especial from view_catalog_empleados ','','','','0','1');
INSERT INTO tbl_catalogos(id_catalogo, nombre, select_clause, pridefault, secdefault, seguridad,aplrep,replong)
VALUES (29,'CONCEPTOS DE NOMINA',' * from view_catalog_nommov ',' select Clave, Descripcion from view_catalog_nommov Order By Clave ASC limit 1 ',' select Clave, Descripcion from view_catalog_nommov Order By Clave DESC limit 1 ','','1','1');
INSERT INTO tbl_catalogos(id_catalogo, nombre, select_clause, pridefault, secdefault, seguridad,aplrep,replong)
VALUES (30,'CONCEPTOS DE ENTRADAS Y SALIDAS DE ALMACEN',' * FROM view_catalog_prod_costos_conceptos_entsal ',' select Clave, Descripcion from view_catalog_prod_costos_conceptos_entsal Order By Clave ASC limit 1 ',' select Clave, Descripcion from view_catalog_prod_costos_conceptos_entsal Order By Clave DESC limit 1 ','','1','1');
INSERT INTO tbl_catalogos(id_catalogo, nombre, select_clause, pridefault, secdefault, seguridad,aplrep,replong)
VALUES (31,'BODEGA DE UTENSILIOS',' * FROM view_catalog_boduten ',' select Clave, Descripcion from view_catalog_boduten Order By Clave ASC limit 1 ','','BODEGAS','1','0');
INSERT INTO tbl_catalogos(id_catalogo, nombre, select_clause, pridefault, secdefault, seguridad,aplrep,replong)
VALUES (32,'CATALOGO DE ALMACENES',' * FROM view_catalog_almacenes ',' select Clave, Descripcion from view_catalog_almacenes Order By Clave ASC limit 1 ','','BODEGAS','1','0');
INSERT INTO tbl_catalogos(id_catalogo, nombre, select_clause, pridefault, secdefault, seguridad,aplrep,replong)
VALUES (33,'LINEAS GENERALES',' * FROM view_catalog_lineas ',' select Clave, Descripcion from view_catalog_lineas Order By Clave ASC limit 1 ',' select Clave, Descripcion from view_catalog_lineas Order By Clave DESC limit 1 ','','1','1');
INSERT INTO tbl_catalogos(id_catalogo, nombre, select_clause, pridefault, secdefault, seguridad,aplrep,replong)
VALUES (34,'COMPAÑIAS',' Clave, Descripcion, Especial FROM view_catalog_companias ','','','','0','1');
INSERT INTO tbl_catalogos(id_catalogo, nombre, select_clause, pridefault, secdefault, seguridad,aplrep,replong)
VALUES (35,'PERSONAS',' Clave, Descripcion, Especial FROM view_catalog_personas ','','','','0','1');
INSERT INTO tbl_catalogos(id_catalogo, nombre, select_clause, pridefault, secdefault, seguridad,aplrep,replong)
VALUES (36,'UTENSILIOS',' * FROM view_catalog_gastos_utensilios ',' select Clave, Descripcion from view_catalog_gastos_utensilios Order By Clave ASC limit 1 ',' select Clave, Descripcion from view_catalog_gastos_utensilios Order By Clave DESC limit 1 ','','1','1');
INSERT INTO tbl_catalogos(id_catalogo, nombre, select_clause, pridefault, secdefault, seguridad,aplrep,replong)
VALUES (37,'CLASIFICACIONES',' * FROM view_catalog_clasificaciones ',' select Clave, Descripcion from view_catalog_clasificaciones Order By Clave ASC limit 1 ',' select Clave, Descripcion from view_catalog_clasificaciones Order By Clave DESC limit 1 ','','1','1');
INSERT INTO tbl_catalogos(id_catalogo, nombre, select_clause, pridefault, secdefault, seguridad,aplrep,replong)
VALUES (38,'CUENTAS CONTABLES DE DETALLE',' * from view_catalog_cuentascontdet ',' select Clave, Descripcion from view_catalog_cuentascontdet Order By Clave ASC limit 1 ',' select Clave, Descripcion from view_catalog_cuentascontdet Order By Clave DESC limit 1 ','','1','1');
INSERT INTO tbl_catalogos(id_catalogo, nombre, select_clause, pridefault, secdefault, seguridad,aplrep,replong)
VALUES (39,'CUENTAS CONTABLES DE ACUMULATIVAS',' * from view_catalog_cuentascontacum ',' select Clave, Descripcion from view_catalog_cuentascontacum Order By Clave ASC limit 1 ',' select Clave, Descripcion from view_catalog_cuentascontacum Order By Clave DESC limit 1 ','','1','1');
INSERT INTO tbl_catalogos(id_catalogo, nombre, select_clause, pridefault, secdefault, seguridad,aplrep,replong)
VALUES (40,'ENTIDADES DE GASTO',' * FROM view_catalog_entidades_gastos ',' select Clave, Descripcion from view_catalog_entidades_gastos Order By Clave ASC limit 1 ','','COMPRAS','1','0');
INSERT INTO tbl_catalogos(id_catalogo, nombre, select_clause, pridefault, secdefault, seguridad,aplrep,replong)
VALUES (41,'ENTIDADES DE COMPRAS GENERALES',' * FROM view_catalog_entidades_compgas ',' select Clave, Descripcion from view_catalog_entidades_compgas Order By Clave ASC limit 1 ','','COMPRAS','1','0');
INSERT INTO tbl_catalogos(id_catalogo, nombre, select_clause, pridefault, secdefault, seguridad,aplrep,replong)
VALUES (42,'MATERIAS PRIMAS Y GASTOS',' * FROM view_catalog_prod2_gas ',' select Clave, Descripcion from view_catalog_prod2_gas Order By Clave ASC limit 1 ',' select Clave, Descripcion from view_catalog_prod2_gas Order By Clave DESC limit 1 ','','1','1');
INSERT INTO tbl_catalogos(id_catalogo, nombre, select_clause, pridefault, secdefault, seguridad,aplrep,replong)
VALUES (43,'LINEAS DE PRODUCTOS Y GASTOS',' * FROM view_catalog_lineas_prodgas ',' select Clave, Descripcion from view_catalog_lineas_prodgas Order By Clave ASC limit 1 ',' select Clave, Descripcion from view_catalog_lineas_prodgas Order By Clave DESC limit 1 ','','1','1');
INSERT INTO tbl_catalogos(id_catalogo, nombre, select_clause, pridefault, secdefault, seguridad,aplrep,replong)
VALUES (44,'CLIENTES',' * FROM view_catalog_client_poriden ',' select Clave, Descripcion from view_catalog_client_poriden Order By Clave ASC limit 1 ',' select Clave, Descripcion from view_catalog_client_poriden Order By Clave DESC limit 1 ','CLIENTES','1','1');
INSERT INTO tbl_catalogos(id_catalogo, nombre, select_clause, pridefault, secdefault, seguridad,aplrep,replong)
VALUES (45,'LINEAS DE PRODUCTOS Y SERVICIOS',' * FROM view_catalog_lineas_prodserv ',' select Clave, Descripcion from view_catalog_lineas_prodserv Order By Clave ASC limit 1 ',' select Clave, Descripcion from view_catalog_lineas_prodserv Order By Clave DESC limit 1 ','','1','1');
INSERT INTO tbl_catalogos(id_catalogo, nombre, select_clause, pridefault, secdefault, seguridad,aplrep,replong)
VALUES (46,'PRODUCTOS Y SERVICIOS',' * FROM view_catalog_prod4_serv ',' select Clave, Descripcion from view_catalog_prod4_serv Order By Clave ASC limit 1 ',' select Clave, Descripcion from view_catalog_prod4_serv Order By Clave DESC limit 1 ','','1','1');
INSERT INTO tbl_catalogos(id_catalogo, nombre, select_clause, pridefault, secdefault, seguridad,aplrep,replong)
VALUES (47,'ENTIDADES DE NOMINA',' * FROM view_catalog_entidades_nomina ',' select Clave, Descripcion from view_catalog_entidades_nomina Order By Clave ASC limit 1 ','','NOMINA','1','0');
INSERT INTO tbl_catalogos(id_catalogo, nombre, select_clause, pridefault, secdefault, seguridad,aplrep,replong)
VALUES (48,'EMPLEADOS',' * FROM view_catalog_empleados_poriden ',' select Clave, Descripcion from view_catalog_empleados_poriden Order By Clave ASC limit 1 ',' select Clave, Descripcion from view_catalog_empleados_poriden Order By Clave DESC limit 1 ','EMPLEADOS','1','1');
INSERT INTO tbl_catalogos(id_catalogo, nombre, select_clause, pridefault, secdefault, seguridad,aplrep,replong)
VALUES (49,'MOVIMIENTOS DE SISTEMA',' * FROM view_catalog_movimientos_sis ',' select Clave, Descripcion from view_catalog_movimientos_sis Order By Clave ASC limit 1 ',' select Clave, Descripcion from view_catalog_movimientos_sis Order By Clave DESC limit 1 ','','1','0');

--@FIN_BLOQUE
CREATE OR REPLACE VIEW view_catalogos AS 
 SELECT c.id_catalogo, m.msj1 AS nombre, c.select_clause, c.pridefault, c.secdefault, c.seguridad, c.aplrep, c.replong
   FROM tbl_catalogos c
   JOIN tbl_msj m ON m.alc::text = 'SAF'::text AND m.mod::text = 'CATALOGOS'::text AND m.sub::text = 'ID'::text AND m.elm::text = c.id_catalogo::text;


--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_adm_bd_cambiar(_id_bd integer, _nombre character varying, _usuario character varying, _password character varying, _compania character varying, _direccion character varying, _poblacion character varying, _cp character varying, _mail character varying, _web character varying)
  RETURNS SETOF record AS
$BODY$
DECLARE _err int; _result varchar(255);
BEGIN
	_err := 0;
	_result := (select msj2 from tbl_msj m where m.alc::text = 'SAF' and m.mod::text = 'ADMIN_BD' and m.sub::text = 'DLG' and m.elm::text = 'MSJ-PROCOK');

	IF(select count(*) from TBL_BD where ID_BD = _id_bd and Nombre = _nombre and Usuario = _usuario) < 1
	THEN
		_err := 3;
		_result := (select msj3 from tbl_msj m where m.alc::text = 'SAF' and m.mod::text = 'ADMIN_BD' and m.sub::text = 'DLG' and m.elm::text = 'MSJ-PROCERR');
	END IF;

	IF _err = 0
	THEN
		UPDATE TBL_BD
		SET Password = _password, Compania = _compania, Direccion = _direccion, Poblacion = _poblacion, 
			CP = _cp, Mail = _mail, Web = _web
		WHERE ID_BD = _id_bd and Nombre = _nombre and Usuario = _usuario;
		
	END IF;
	
		
	RETURN QUERY SELECT _err, _result, _id_bd;
END
$BODY$
  LANGUAGE 'plpgsql';

--@FIN_BLOQUE
CREATE TABLE tbl_ayuda_paginas
(
  id_pagina character varying(8) NOT NULL,
  descripcion character varying(50) NOT NULL,
  busqueda character varying(254) NOT NULL,
  cuerpo text NOT NULL,
  status smallint NOT NULL,
  tipo character varying(8) NOT NULL,
  id_alternativo character varying(30),
  CONSTRAINT pk_tbl_ayuda_paginas PRIMARY KEY (id_pagina)
);

--@FIN_BLOQUE
CREATE TABLE tbl_ayuda_tipos
(
  id_tipo character varying(8) NOT NULL,
  descripcion character varying(50) NOT NULL,
  CONSTRAINT pk_tbl_ayuda_tipos PRIMARY KEY (id_tipo)
);

--@FIN_BLOQUE
CREATE TABLE tbl_ayuda_subtipos
(
  id_subtipo character varying(8) NOT NULL,
  id_tipo character varying(8) NOT NULL,
  descripcion character varying(50) NOT NULL,
  CONSTRAINT pk_tbl_ayuda_subtipos PRIMARY KEY (id_subtipo),
  CONSTRAINT fk_tbl_ayuda_subtipos_tbl_ayuda_tipos FOREIGN KEY (id_tipo)
      REFERENCES tbl_ayuda_tipos (id_tipo) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
);

--@FIN_BLOQUE
CREATE TABLE tbl_ayuda_paginas_enlaces
(
  id_pagina character varying(8) NOT NULL,
  id_enlace character varying(8) NOT NULL,
  CONSTRAINT pk_tbl_ayuda_paginas_enlaces PRIMARY KEY (id_pagina, id_enlace)
);

--@FIN_BLOQUE
CREATE TABLE tbl_ayuda_paginas_subtipos
(
  id_subtipo character varying(8) NOT NULL,
  id_pagina character varying(8) NOT NULL,
  CONSTRAINT pk_tbl_ayuda_paginas_subtipos PRIMARY KEY (id_subtipo, id_pagina),
  CONSTRAINT fk_tbl_ayuda_paginas_subtipos_tbl_ayuda_paginas FOREIGN KEY (id_pagina)
      REFERENCES tbl_ayuda_paginas (id_pagina) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_tbl_ayuda_paginas_subtipos_tbl_ayuda_subtipos FOREIGN KEY (id_subtipo)
      REFERENCES tbl_ayuda_subtipos (id_subtipo) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
);

--@FIN_BLOQUE
CREATE OR REPLACE VIEW view_ayuda_paginas_modulo AS 
 SELECT st.id_subtipo, m.id_pagina, m.descripcion, m.busqueda, m.status, 
        CASE
            WHEN m.status = 0 THEN 'SREV'::text
            WHEN m.status = 1 THEN 'REV'::text
            WHEN m.status = 2 THEN 'FIN'::text
            ELSE 'DES'::text
        END AS st, m.tipo, 
        CASE
            WHEN m.id_alternativo IS NULL THEN ''::character varying
            ELSE m.id_alternativo
        END AS id_alternativo
   FROM tbl_ayuda_paginas m
   JOIN tbl_ayuda_paginas_subtipos st ON m.id_pagina::text = st.id_pagina::text;

--@FIN_BLOQUE
CREATE OR REPLACE VIEW view_ayuda_paginas_modulo_gen AS 
 SELECT 'fsi'::character varying AS id_subtipo, m.id_pagina, m.descripcion, m.busqueda, m.status, 
        CASE
            WHEN m.status = 0 THEN 'SREV'::text
            WHEN m.status = 1 THEN 'REV'::text
            WHEN m.status = 2 THEN 'FIN'::text
            ELSE 'DES'::text
        END AS st, m.tipo, 
        CASE
            WHEN m.id_alternativo IS NULL THEN ''::character varying
            ELSE m.id_alternativo
        END AS id_alternativo
   FROM tbl_ayuda_paginas m;

--@FIN_BLOQUE
CREATE OR REPLACE VIEW view_ayuda_paginas_enlaces AS 
 SELECT st.id_enlace, st.id_pagina, m.descripcion, m.busqueda
   FROM tbl_ayuda_paginas m
   JOIN tbl_ayuda_paginas_enlaces st ON m.id_pagina::text = st.id_pagina::text;

--@FIN_BLOQUE
CREATE OR REPLACE VIEW view_ayuda_pagina_vs_pagina AS 
 SELECT p.id_pagina, c.id_pagina AS id_enlace, c.descripcion, 
        CASE
            WHEN (( SELECT count(*) AS count
               FROM tbl_ayuda_paginas_enlaces ce
              WHERE ce.id_pagina::text = p.id_pagina::text AND ce.id_enlace::text = c.id_pagina::text)) = 0 THEN 0
            ELSE 1
        END AS enlazado
   FROM tbl_ayuda_paginas c, tbl_ayuda_paginas p;

--@FIN_BLOQUE
CREATE OR REPLACE VIEW view_ayuda_subtipo_vs_pagina AS 
 SELECT p.id_pagina, c.id_subtipo, c.id_tipo, c.descripcion, 
        CASE
            WHEN (( SELECT count(*) AS count
               FROM tbl_ayuda_paginas_subtipos ce
              WHERE ce.id_pagina::text = p.id_pagina::text AND ce.id_subtipo::text = c.id_subtipo::text)) = 0 THEN 0
            ELSE 1
        END AS enlazado, p.Status
   FROM tbl_ayuda_subtipos c, tbl_ayuda_paginas p;


--@FIN_BLOQUE
CREATE OR REPLACE VIEW view_ayuda_paginas_subtipos_tipos AS 
 SELECT p.id_pagina, c.id_subtipo, c.id_tipo, c.descripcion, p.status
   FROM tbl_ayuda_subtipos c
   JOIN tbl_ayuda_paginas_subtipos s ON c.id_subtipo::text = s.id_subtipo::text
   JOIN tbl_ayuda_paginas p ON s.id_pagina::text = p.id_pagina::text;


--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_ayuda_pagina_agregar(_id_pagina character varying, _descripcion character varying, _busqueda character varying, _tipo character varying, _id_alternativo character varying)
  RETURNS SETOF record AS
$BODY$
DECLARE _err int; _result varchar(255);
BEGIN
	_err := 0;
	_result := (select msj1 from tbl_msj m where m.alc::text = 'SAF' and m.mod::text = 'ADMIN_AYUDA' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCOK'); --'La p?gina se agreg? correctamente'

	IF(select count(*) from TBL_AYUDA_PAGINAS where ID_Pagina = _ID_Pagina) > 0
	THEN
		_err := 3;
		_result := (select msj1 from tbl_msj m where m.alc::text = 'SAF' and m.mod::text = 'ADMIN_AYUDA' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); --'ERROR: El id de la nueva p?gina ya existe' 
	END IF;

	IF _err = 0
	THEN
		insert into TBL_AYUDA_PAGINAS
		select _ID_Pagina, _Descripcion, _Busqueda, '', 0, _tipo, _id_alternativo;
		
		-- INSERTA ASOCIACIONES
		insert into TBL_AYUDA_PAGINAS_SUBTIPOS
		select ID_SubTipo, _ID_Pagina
		from _TMP_AYUDA_PAGINAS_SUBTIPOS;
		
	END IF;
	
		
	RETURN QUERY SELECT _err, _result, _id_pagina;

	
		
END
$BODY$
  LANGUAGE plpgsql;


--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_ayuda_pagina_aplicar(_id_pagina character varying)
  RETURNS SETOF record AS
$BODY$
DECLARE _err int; _result varchar(255); _status smallint;
BEGIN
	_err := 0;
	_result := (select msj2 from tbl_msj m where m.alc::text = 'SAF' and m.mod::text = 'ADMIN_AYUDA' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCOK'); --'La p?gina se cambio correctamente'

	IF(select count(*) from TBL_AYUDA_PAGINAS where ID_Pagina = _ID_Pagina) < 1
	THEN
		_err := 3;
		_result := (select msj2 from tbl_msj m where m.alc::text = 'SAF' and m.mod::text = 'ADMIN_AYUDA' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); --'ERROR: El id de la p?gina no existe' 
	END IF;

	IF _err = 0
	THEN
		_status := (select Status from TBL_AYUDA_PAGINAS where ID_Pagina = _ID_Pagina);
		
		if(_status <= 2)
		then
			_status := _status + 1;
 		else
			_status := 0;
		end if;

		update TBL_AYUDA_PAGINAS
		set Status = _status
		where ID_Pagina = _ID_Pagina;
		
		
	END IF;
	
	RETURN QUERY SELECT _err, _result, _id_pagina;
		
END
$BODY$
  LANGUAGE 'plpgsql';

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_ayuda_pagina_cambiar(_id_paginaold character varying, _id_pagina character varying, _descripcion character varying, _busqueda character varying, _tipo character varying, _id_alternativo character varying)
  RETURNS SETOF record AS
$BODY$
DECLARE _err int; _result varchar(255);
BEGIN
	_err := 0;
	_result := (select msj2 from tbl_msj m where m.alc::text = 'SAF' and m.mod::text = 'ADMIN_AYUDA' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCOK'); --'La p?gina se cambio correctamente'

	IF(select count(*) from TBL_AYUDA_PAGINAS where ID_Pagina = _ID_PaginaOld) < 1
	THEN
		_err := 3;
		_result := (select msj2 from tbl_msj m where m.alc::text = 'SAF' and m.mod::text = 'ADMIN_AYUDA' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); --'ERROR: El id de la p?gina no existe' 
	END IF;

	IF _err = 0
	THEN
		-- INSERTA ASOCIACIONES
		delete from TBL_AYUDA_PAGINAS_SUBTIPOS
		where ID_Pagina = _ID_PaginaOld;
		
		insert into TBL_AYUDA_PAGINAS_SUBTIPOS
		select ID_SubTipo, _ID_PaginaOld
		from _TMP_AYUDA_PAGINAS_SUBTIPOS;
		
		if(_ID_PaginaOld = _ID_Pagina) -- significa que solo esta cambiando la descripcion y/o el padre
		then
			update TBL_AYUDA_PAGINAS
			set Descripcion = _Descripcion, Busqueda = _Busqueda, Tipo = _Tipo, ID_Alternativo = _ID_Alternativo
			where ID_Pagina = _ID_Pagina;
			
		else
		 	-- cambia el ID por lo que los cambios deben afectar toda la jerarquia
			update TBL_AYUDA_PAGINAS
			set ID_Pagina = _ID_Pagina, Descripcion = _Descripcion, Busqueda = _Busqueda, Tipo = _Tipo, ID_Alternativo = _ID_Alternativo
			where ID_Pagina = _ID_PaginaOld;
			
			update TBL_AYUDA_PAGINAS_ENLACES
			set ID_Pagina = _ID_Pagina
			where ID_Pagina = _ID_PaginaOld;
			
			update TBL_AYUDA_PAGINAS_ENLACES
			set ID_Enlace = _ID_Pagina
			where ID_Enlace = _ID_PaginaOld;
			
	
		end if;
		
	END IF;
	
	RETURN QUERY SELECT _err, _result, _id_pagina;
		
END
$BODY$
  LANGUAGE plpgsql;


--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_ayuda_pagina_editar(_id_pagina character varying, _cuerpo text)
  RETURNS SETOF record AS
$BODY$
DECLARE _err int; _result varchar(255);
BEGIN
	_err := 0;
	_result := (select msj2 from tbl_msj m where m.alc::text = 'SAF' and m.mod::text = 'ADMIN_AYUDA' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCOK'); --'La p?gina se cambio correctamente'

	IF(select count(*) from TBL_AYUDA_PAGINAS where ID_Pagina = _ID_Pagina) < 1
	THEN
		_err := 3;
		_result := (select msj2 from tbl_msj m where m.alc::text = 'SAF' and m.mod::text = 'ADMIN_AYUDA' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); --'ERROR: El id de la p?gina no existe' 
	END IF;

	IF _err = 0
	THEN
		update TBL_AYUDA_PAGINAS
		set Cuerpo = _Cuerpo
		where ID_Pagina = _ID_Pagina;
		
	END IF;
	
	RETURN QUERY SELECT _err, _result, _id_pagina;
		
END
$BODY$
  LANGUAGE 'plpgsql';

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_ayuda_pagina_eliminar(_id_pagina character varying)
  RETURNS SETOF record AS
$BODY$
DECLARE _err int; _result varchar(255);
BEGIN
	_err := 0;
	_result := (select msj3 from tbl_msj m where m.alc::text = 'SAF' and m.mod::text = 'ADMIN_AYUDA' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCOK'); --'La p?gina se elimino correctamente'

	IF(select count(*) from TBL_AYUDA_PAGINAS where ID_Pagina = _ID_Pagina) < 1
	THEN
		_err := 3;
		_result := (select msj3 from tbl_msj m where m.alc::text = 'SAF' and m.mod::text = 'ADMIN_AYUDA' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); --'ERROR: El id de la p?gina no existe' 
	END IF;

	IF _err = 0
	THEN
		delete from TBL_AYUDA_PAGINAS
		where ID_Pagina = _ID_Pagina;
		
		delete from TBL_AYUDA_PAGINAS_ENLACES
		where ID_Pagina = _ID_Pagina or ID_Enlace = _ID_Pagina;
		
	END IF;
	
	RETURN QUERY SELECT _err, _result, _id_pagina;
		
END
$BODY$
  LANGUAGE 'plpgsql';

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_ayuda_pagina_enlaces(_id_pagina character varying)
  RETURNS SETOF record AS
$BODY$
DECLARE _err int; _result varchar(255);
BEGIN
	_err := 0;
	_result := (select msj2 from tbl_msj m where m.alc::text = 'SAF' and m.mod::text = 'ADMIN_AYUDA' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCOK'); --'La p?gina se cambio correctamente'

	IF(select count(*) from TBL_AYUDA_PAGINAS where ID_Pagina = _ID_Pagina) < 1
	THEN
		_err := 3;
		_result := (select msj2 from tbl_msj m where m.alc::text = 'SAF' and m.mod::text = 'ADMIN_AYUDA' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); --'ERROR: El id de la p?gina no existe' 
	END IF;

	IF _err = 0
	THEN
		delete from TBL_AYUDA_PAGINAS_ENLACES
		where ID_Pagina = _ID_Pagina or ID_Enlace = _ID_Pagina;
		
		insert into TBL_AYUDA_PAGINAS_ENLACES
		select _ID_Pagina, ID_Enlace
		from _TMP_AYUDA_PAGINAS_ENLACES;
		
		insert into TBL_AYUDA_PAGINAS_ENLACES
		select ID_Enlace, _ID_Pagina
		from _TMP_AYUDA_PAGINAS_ENLACES;
		
	END IF;
	
	RETURN QUERY SELECT _err, _result, _id_pagina;
		
END
$BODY$
  LANGUAGE 'plpgsql';

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_ayuda_subtipo_agregar(_id_subtipo character varying, _descripcion character varying, _id_tipo character varying)
  RETURNS SETOF record AS
$BODY$
DECLARE _err int; _result varchar(255);
BEGIN
	_err := 0;
	_result := (select msj1 from tbl_msj m where m.alc::text = 'SAF' and m.mod::text = 'ADMIN_AYUDA' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCOK'); --'La p?gina se elimino correctamente'

	IF(select count(*) from TBL_AYUDA_SUBTIPOS where ID_SubTipo = _ID_SubTipo) > 0
	THEN
		_err := 3;
		_result := (select msj1 from tbl_msj m where m.alc::text = 'SAF' and m.mod::text = 'ADMIN_AYUDA' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); --'ERROR: El id de la p?gina no existe' 
	END IF;

	IF(select count(*) from TBL_AYUDA_TIPOS where ID_Tipo = _ID_Tipo) < 1
	THEN
		_err := 3;
		_result := (select msj4 from tbl_msj m where m.alc::text = 'SAF' and m.mod::text = 'ADMIN_AYUDA' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); -- PRECAUCION: No se puede borrar el menu principal porque tiene submenus asociados. Primero se tienen que eliminar los sub-menus '
	
	END IF;

	IF _err = 0
	THEN
		insert into TBL_AYUDA_SUBTIPOS
		values(_ID_SubTipo, _ID_Tipo, _Descripcion);
		
		
	END IF;

	
	RETURN QUERY SELECT _err, _result, _id_subtipo;
		
END
$BODY$
  LANGUAGE 'plpgsql';

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_ayuda_subtipo_cambiar(_id_subold character varying, _id_subtipo character varying, _descripcion character varying, _id_tipo character varying)
  RETURNS SETOF record AS
$BODY$
DECLARE _err int; _result varchar(255);
BEGIN
	_err := 0;
	_result := (select msj2 from tbl_msj m where m.alc::text = 'SAF' and m.mod::text = 'ADMIN_AYUDA' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCOK'); --'La p?gina se elimino correctamente'

	IF(select count(*) from TBL_AYUDA_SUBTIPOS where ID_SubTipo = _ID_SubOld) < 1
	THEN
		_err := 3;
		_result := (select msj2 from tbl_msj m where m.alc::text = 'SAF' and m.mod::text = 'ADMIN_AYUDA' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); --'ERROR: El id de la p?gina no existe' 
	END IF;

	IF(select count(*) from TBL_AYUDA_TIPOS where ID_Tipo = _ID_Tipo) < 1
	THEN
		_err := 3;
		_result := (select msj5 from tbl_msj m where m.alc::text = 'SAF' and m.mod::text = 'ADMIN_AYUDA' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); -- PRECAUCION: No se puede borrar el menu principal porque tiene submenus asociados. Primero se tienen que eliminar los sub-menus '
	
	END IF;

	IF _err = 0
	THEN
		if(_ID_SubOld = _ID_SubTipo) -- significa que solo esta cambiando la descripcion y/o el padre
		then
			update TBL_AYUDA_SUBTIPOS
			set Descripcion = _Descripcion, ID_Tipo = _ID_Tipo
			where ID_SubTipo = _ID_SubTipo;
			
		else
		 	-- cambia el ID por lo que los cambios deben afectar toda la jerarquia
			update TBL_AYUDA_SUBTIPOS
			set ID_SubTipo = _ID_SubTipo, ID_Tipo = _ID_Tipo, Descripcion = _Descripcion
			where ID_SubTipo = _ID_SubOld;
			
		end if;
		
	END IF;

	
	RETURN QUERY SELECT _err, _result, _id_subtipo;
		
END
$BODY$
  LANGUAGE 'plpgsql';

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_ayuda_subtipo_eliminar(_id_subtipo character varying)
  RETURNS SETOF record AS
$BODY$
DECLARE _err int; _result varchar(255);
BEGIN
	_err := 0;
	_result := (select msj3 from tbl_msj m where m.alc::text = 'SAF' and m.mod::text = 'ADMIN_AYUDA' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCOK'); --'La p?gina se elimino correctamente'

	IF(select count(*) from TBL_AYUDA_SUBTIPOS where ID_SubTipo = _ID_SubTipo) < 1
	THEN
		_err := 3;
		_result := (select msj3 from tbl_msj m where m.alc::text = 'SAF' and m.mod::text = 'ADMIN_AYUDA' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); --'ERROR: El id de la p?gina no existe' 
	END IF;

	IF(select count(*) from TBL_AYUDA_PAGINAS_SUBTIPOS where ID_SubTipo = _ID_SubTipo) > 0
	THEN
		_err := 3;
		_result := (select msj5 from tbl_msj m where m.alc::text = 'SAF' and m.mod::text = 'ADMIN_AYUDA' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); -- PRECAUCION: No se puede borrar el menu principal porque tiene submenus asociados. Primero se tienen que eliminar los sub-menus '
	
	END IF;

	IF _err = 0
	THEN
		delete from TBL_AYUDA_SUBTIPOS
		where ID_SubTipo = _ID_SubTipo;
	END IF;

	
	RETURN QUERY SELECT _err, _result, _id_subtipo;
		
END
$BODY$
  LANGUAGE 'plpgsql';

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_ayuda_tipo_agregar(_id_tipo character varying, _descripcion character varying)
  RETURNS SETOF record AS
$BODY$
DECLARE _err int; _result varchar(255);
BEGIN
	_err := 0;
	_result := (select msj1 from tbl_msj m where m.alc::text = 'SAF' and m.mod::text = 'ADMIN_AYUDA' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCOK'); --'La p?gina se agrego correctamente'

	IF(select count(*) from TBL_AYUDA_TIPOS where ID_Tipo = _ID_Tipo) > 0
	THEN
		_err := 3;
		_result := (select msj1 from tbl_msj m where m.alc::text = 'SAF' and m.mod::text = 'ADMIN_AYUDA' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); --'ERROR: El id de la p?gina ya existe' 
	END IF;

	IF _err = 0
	THEN
		insert into TBL_AYUDA_TIPOS
		values(_ID_Tipo, _Descripcion);
		
	END IF;
	
	RETURN QUERY SELECT _err, _result, _id_tipo;
		
END
$BODY$
  LANGUAGE 'plpgsql';

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_ayuda_tipo_cambiar(_id_old character varying, _id_tipo character varying, _descripcion character varying)
  RETURNS SETOF record AS
$BODY$
DECLARE _err int; _result varchar(255);
BEGIN
	_err := 0;
	_result := (select msj2 from tbl_msj m where m.alc::text = 'SAF' and m.mod::text = 'ADMIN_AYUDA' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCOK'); --'La p?gina se cambio correctamente'

	IF(select count(*) from TBL_AYUDA_TIPOS where ID_Tipo = _ID_Old) < 1
	THEN
		_err := 3;
		_result := (select msj2 from tbl_msj m where m.alc::text = 'SAF' and m.mod::text = 'ADMIN_AYUDA' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); --'ERROR: El id de la p?gina no existe' 
	END IF;

	IF _err = 0
	THEN
		if(_ID_Old = _ID_Tipo) -- significa que solo esta cambiando la descripcion
		then
			update TBL_AYUDA_TIPOS
			set Descripcion = _Descripcion
			where ID_Tipo = _ID_Tipo;
		else
		 	-- cambia el ID por lo que los cambios deben afectar toda la jerarquia
			update TBL_AYUDA_TIPOS
			set ID_Tipo = _ID_Tipo, Descripcion = _Descripcion
			where ID_Tipo = _ID_Old;
		end if;
		
	END IF;
	
	RETURN QUERY SELECT _err, _result, _id_tipo;
		
END
$BODY$
  LANGUAGE 'plpgsql';

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_ayuda_tipo_eliminar(_id_tipo character varying)
  RETURNS SETOF record AS
$BODY$
DECLARE _err int; _result varchar(255);
BEGIN
	_err := 0;
	_result := (select msj3 from tbl_msj m where m.alc::text = 'SAF' and m.mod::text = 'ADMIN_AYUDA' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCOK'); --'La p?gina se elimino correctamente'

	IF(select count(*) from TBL_AYUDA_TIPOS where ID_Tipo = _ID_Tipo) < 1
	THEN
		_err := 3;
		_result := (select msj3 from tbl_msj m where m.alc::text = 'SAF' and m.mod::text = 'ADMIN_AYUDA' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); --'ERROR: El id de la p?gina no existe' 
	END IF;

	IF(select count(*) from TBL_AYUDA_SUBTIPOS where ID_Tipo = _ID_Tipo) > 0
	THEN
		_err := 3;
		_result := (select msj5 from tbl_msj m where m.alc::text = 'SAF' and m.mod::text = 'ADMIN_AYUDA' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR'); -- PRECAUCION: No se puede borrar el menu principal porque tiene submenus asociados. Primero se tienen que eliminar los sub-menus '
	
	END IF;

	IF _err = 0
	THEN
		delete from TBL_AYUDA_TIPOS
		where ID_Tipo = _ID_Tipo;
	END IF;

	
	RETURN QUERY SELECT _err, _result, _id_tipo;
		
END
$BODY$
  LANGUAGE 'plpgsql';

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_cfd_cambiar(_bdnombre character varying, _cfd_regimenfiscal character varying, _rfc character varying, _cfd smallint, _cfd_calle character varying, _cfd_noext character varying, _cfd_noint character varying, _cfd_colonia character varying, _cfd_localidad character varying, _cfd_municipio character varying, _cfd_estado character varying, _cfd_pais character varying, _cfd_cp character varying)
  RETURNS SETOF record AS
$BODY$
DECLARE 
	_err int; _result varchar(255);
BEGIN
	_err := 0;
	_result := (select msj1 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'ADM_CFDI' and m.sub::text = 'BD' 
			and m.elm::text = 'MSJ-PROCOK');
	
	IF(select count(*) from TBL_BD where Nombre = _BDNombre) < 1 
	THEN
		_err = 3;
		_result = (select msj2 from tbl_msj m where m.alc::text = 'CEF' and m.mod::text = 'ADM_CFDI' and m.sub::text = 'BD' 
			and m.elm::text = 'MSJ-PROCERR'); --'ERROR: La empresa no existe, No se puede cambiar';
	END IF; 

	IF _err = 0
	THEN
		UPDATE TBL_BD
		SET CFD_RegimenFiscal = _CFD_RegimenFiscal, RFC = _RFC,
				CFD = _CFD, CFD_Calle = _CFD_Calle, 
				CFD_NoExt = _CFD_NoExt, CFD_NoInt = _CFD_NoInt, CFD_Colonia = _CFD_Colonia, CFD_Localidad = _CFD_Localidad, CFD_Municipio = _CFD_Municipio, CFD_Estado = _CFD_Estado, CFD_Pais = _CFD_Pais, CFD_CP = _CFD_CP
		WHERE Nombre = _BDNombre;
	END IF;
		
	RETURN QUERY SELECT _err, _result, _BDNombre;

END
$BODY$
  LANGUAGE 'plpgsql';


--@FIN_BLOQUE
CREATE TABLE tbl_formatos_tipos
(
  id_tipo character varying(30) NOT NULL,
  impcab character varying(254) NOT NULL,
  impdet character varying(254) NOT NULL,
  CONSTRAINT pk_tbl_formatos_tipos PRIMARY KEY (id_tipo )
);

--@FIN_BLOQUE
CREATE OR REPLACE VIEW view_formatos_tipos AS 
 SELECT c.id_tipo, m.msj2 AS descripcion, c.impcab, c.impdet
   FROM tbl_formatos_tipos c
   JOIN tbl_msj m ON m.alc::text = 'CEF'::text AND m.mod::text = 'PERMISOS'::text AND m.sub::text = 'CATALOGO'::text AND m.elm::text = c.id_tipo::text;

insert into TBL_FORMATOS_TIPOS
values('CONT_POLIZAS','view_cont_polizas_impcab','view_cont_polizas_impdet');

insert into TBL_FORMATOS_TIPOS
values('BANCAJ_BANCOS','view_bancos_movimientos_impcab','view_cont_polizas_impdet');
insert into TBL_FORMATOS_TIPOS
values('BANCAJ_CAJAS','view_cajas_movimientos_impcab','view_cont_polizas_impdet');
insert into TBL_FORMATOS_TIPOS
values('BANCAJ_VALES','view_cajas_vales_impcab','');
insert into TBL_FORMATOS_TIPOS
values('BANCAJ_CIERRES','view_cajas_cierres_impcab','view_cajas_cierres_impdet');

insert into TBL_FORMATOS_TIPOS
values('ALM_MOVIM','view_invserv_almacen_movim_impcab','view_invserv_almacen_movim_impdet');
insert into TBL_FORMATOS_TIPOS
values('ALM_MOVPLANT','view_invserv_plantillas_impcab','view_invserv_plantillas_impdet');
insert into TBL_FORMATOS_TIPOS
values('ALM_TRASPASOS','view_invserv_traspasos_impcab','view_invserv_traspasos_impdet');
insert into TBL_FORMATOS_TIPOS
values('ALM_REQUERIMIENTOS','view_invserv_requerimientos_impcab','view_invserv_requerimientos_impdet');
insert into TBL_FORMATOS_TIPOS
values('ALM_CHFIS','view_invserv_chfis_impcab','view_invserv_chfis_impdet');

insert into TBL_FORMATOS_TIPOS
values('COMP_FAC','view_compras_facturas_impcab','view_compras_facturas_impdet');
insert into TBL_FORMATOS_TIPOS
values('COMP_REC','view_compras_recepciones_impcab','view_compras_recepciones_impdet');
insert into TBL_FORMATOS_TIPOS
values('COMP_DEV','view_compras_devoluciones_impcab','view_compras_devoluciones_impdet');
insert into TBL_FORMATOS_TIPOS
values('COMP_GAS','view_compras_gastos_impcab','view_compras_gastos_impdet');
insert into TBL_FORMATOS_TIPOS
values('COMP_ORD','view_compras_ordenes_impcab','view_compras_ordenes_impdet');

insert into TBL_FORMATOS_TIPOS
values('VEN_FAC','view_ventas_facturas_impcab','view_ventas_facturas_impdet');
insert into TBL_FORMATOS_TIPOS
values('VEN_REM','view_ventas_remisiones_impcab','view_ventas_remisiones_impdet');
insert into TBL_FORMATOS_TIPOS
values('VEN_DEV','view_ventas_devoluciones_impcab','view_ventas_devoluciones_impdet');
insert into TBL_FORMATOS_TIPOS
values('VEN_PED','view_ventas_pedidos_impcab','view_ventas_pedidos_impdet');
insert into TBL_FORMATOS_TIPOS
values('VEN_COT','view_ventas_cotizaciones_impcab','view_ventas_cotizaciones_impdet');

insert into TBL_FORMATOS_TIPOS
values('NOM_NOMINA','view_nomina_recibos_impcab','view_nomina_recibos_impdet');


--@FIN_BLOQUE
CREATE TABLE tbl_pac_servidores
(
  id_servidor character(36) NOT NULL,
  usuario character varying(20) NOT NULL,
  pass character varying(20) NOT NULL,
  fechaalta timestamp without time zone NOT NULL,
  rfc character varying(15) NOT NULL,
  compania character varying(255) NOT NULL,
  direccion character varying(255) NOT NULL,
  poblacion character varying(50) NOT NULL,
  cp character varying(9) NOT NULL,
  mail character varying(255) NOT NULL,
  web character varying(255) NOT NULL,
  tel character varying(10) NOT NULL,
  status character varying(10) NOT NULL,
  CONSTRAINT pk_tbl_pac_servidores PRIMARY KEY (id_servidor)
);
  
--@FIN_BLOQUE
CREATE TABLE tbl_pac_registros_fallidos
(
  id_registro serial NOT NULL,
  ip character varying(255) NOT NULL,
  host character varying(255) NOT NULL,
  fecha timestamp without time zone NOT NULL,
  servidor text NOT NULL,
  usuario text NOT NULL,
  pass text NOT NULL,
  request text NOT NULL,
  error text NOT NULL,
  nivelerror smallint NOT NULL,
  tipo_registro character varying(5) NOT NULL,
  CONSTRAINT pk_tbl_pac_registros_fallidos PRIMARY KEY (id_registro)
);

--@FIN_BLOQUE
CREATE TABLE tbl_pac_registros_exitosos
(
  id_registro serial NOT NULL,
  fecha timestamp without time zone NOT NULL,
  servidor character(36) NOT NULL,
  bd character varying(30) NOT NULL,
  nombrearchivo character varying(254) NOT NULL,
  test bit(1) NOT NULL,
  tipo_registro character varying(5) NOT NULL,
  costo numeric(19,4) NOT NULL,
  saldo numeric(19,4) NOT NULL,
  CONSTRAINT pk_tbl_pac_registros_exitosos PRIMARY KEY (id_registro)
);

--@FIN_BLOQUE
CREATE TABLE tbl_s3_registros_exitosos
(
  servidor character(36) NOT NULL,
  bd character varying(30) NOT NULL,
  id_modulo character varying(30) NOT NULL,
  obj_id1 character varying(50) NOT NULL,
  obj_id2 character varying(20) NOT NULL,
  nombre character varying(110) NOT NULL,
  fecha timestamp without time zone NOT NULL,
  tambites bigint NOT NULL,
  CONSTRAINT pk_tbl_s3_registros_exitosos PRIMARY KEY (servidor, bd, id_modulo, obj_id1, obj_id2, nombre)
);

--@FIN_BLOQUE
CREATE TABLE tbl_s3_registros_exitosos_conn
(
  servidor character(36) NOT NULL,
  bd character varying(30) NOT NULL,
  id_modulo character varying(30) NOT NULL,
  obj_id1 character varying(50) NOT NULL,
  obj_id2 character varying(20) NOT NULL,
  nombre character varying(110) NOT NULL,
  fecha timestamp without time zone NOT NULL,
  tambites bigint NOT NULL,
  CONSTRAINT pk_tbl_s3_registros_exitosos_conn PRIMARY KEY (servidor, bd, id_modulo, obj_id1, obj_id2, nombre)
);

--@FIN_BLOQUE
CREATE TABLE tbl_srv_servidores
(
  id_servidor character(36) NOT NULL,
  usuario character varying(20) NOT NULL,
  pass character varying(20) NOT NULL,
  fechaalta timestamp without time zone NOT NULL,
  rfc character varying(15) NOT NULL,
  compania character varying(255) NOT NULL,
  direccion character varying(255) NOT NULL,
  poblacion character varying(50) NOT NULL,
  cp character varying(9) NOT NULL,
  mail character varying(255) NOT NULL,
  web character varying(255) NOT NULL,
  tel character varying(10) NOT NULL,
  status character varying(10) NOT NULL,
  CONSTRAINT pk_tbl_srv_servidores PRIMARY KEY (id_servidor)
);

--@FIN_BLOQUE
CREATE TABLE tbl_srv_registros_fallidos
(
  id_registro serial NOT NULL,
  ip character varying(255) NOT NULL,
  host character varying(255) NOT NULL,
  fecha timestamp without time zone NOT NULL,
  servidor text NOT NULL,
  usuario text NOT NULL,
  pass text NOT NULL,
  request text NOT NULL,
  error text NOT NULL,
  nivelerror smallint NOT NULL,
  CONSTRAINT pk_tbl_srv_registros_fallidos PRIMARY KEY (id_registro)
);

--@FIN_BLOQUE
CREATE TABLE tbl_srv_registros_exitosos
(
  id_registro serial NOT NULL,
  fecha timestamp without time zone NOT NULL,
  servidor character(36) NOT NULL,
  bd character varying(30) NOT NULL,
  CONSTRAINT pk_tbl_srv_registros_exitosos PRIMARY KEY (id_registro)
);

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_adm_bd_propserv(
    _tomcat character varying,
    _jvm character varying,
    _servidor character varying,
    _actualizar character varying,
    _postgresql character varying,
    _fsi_pass character varying,
    _respaldos character varying,
    _auto_hora timestamp without time zone,
    _auto_act integer,
    _auto_resp integer,
    _auto_slds integer,
    _pac_purl character varying,
    _pac_serv character varying,
    _pac_user character varying,
    _pac_pass character varying,
    _edi_purl character varying,
    _edi_user character varying,
    _edi_pass character varying,
    _smtp_host character varying,
    _smtp_port integer,
    _smtp_user character varying,
    _smtp_pass character varying,
    _s3_bukt character varying,
    _s3_user character varying,
    _s3_pass character varying)
  RETURNS SETOF record AS
$BODY$
DECLARE _err int; _result varchar(255);
BEGIN
	_err := 0;
	_result := 'Se configuraron con exito las propiedades de este servidor';
	
	IF _err = 0
	THEN
		UPDATE TBL_VARIABLES SET VAlfanumerico = _tomcat WHERE ID_Variable = 'TOMCAT';
		UPDATE TBL_VARIABLES SET VAlfanumerico = _jvm  WHERE ID_Variable = 'JVM';
		UPDATE TBL_VARIABLES SET VAlfanumerico = _servidor  WHERE ID_Variable = 'SERVIDOR';
		UPDATE TBL_VARIABLES SET VAlfanumerico = _actualizar  WHERE ID_Variable = 'ACTUALIZAR'; 
		UPDATE TBL_VARIABLES SET VAlfanumerico = _postgresql  WHERE ID_Variable = 'POSTGRESQL'; 
		--UPDATE TBL_VARIABLES SET VAlfanumerico = _fsi_pass  WHERE ID_Variable = 'FSI_PASS'; 
		UPDATE TBL_VARIABLES SET VAlfanumerico = _respaldos  WHERE ID_Variable = 'RESPALDOS'; 
		UPDATE TBL_VARIABLES SET VFecha = _auto_hora WHERE ID_Variable = 'AUTO_HORA'; 
		UPDATE TBL_VARIABLES SET VEntero = _auto_act WHERE ID_Variable = 'AUTO_ACT'; 
		UPDATE TBL_VARIABLES SET VEntero = _auto_resp WHERE ID_Variable = 'AUTO_RESP';
		UPDATE TBL_VARIABLES SET VEntero = _auto_slds WHERE ID_Variable = 'AUTO_SLDS';
    		UPDATE TBL_VARIABLES SET VAlfanumerico = _pac_purl  WHERE ID_Variable = 'PAC_PURL'; 
    		UPDATE TBL_VARIABLES SET VAlfanumerico = _pac_serv  WHERE ID_Variable = 'PAC_SERV'; 
    		UPDATE TBL_VARIABLES SET VAlfanumerico = _pac_user  WHERE ID_Variable = 'PAC_USER';
    		IF _pac_pass <> ''
    		THEN 
			UPDATE TBL_VARIABLES SET VAlfanumerico = _pac_pass  WHERE ID_Variable = 'PAC_PASS'; 
		END IF;
    		UPDATE TBL_VARIABLES SET VAlfanumerico = _edi_purl  WHERE ID_Variable = 'EDI_PURL'; 
    		UPDATE TBL_VARIABLES SET VAlfanumerico = _edi_user  WHERE ID_Variable = 'EDI_USER';
    		IF _edi_pass <> ''
    		THEN  
			UPDATE TBL_VARIABLES SET VAlfanumerico = _edi_pass  WHERE ID_Variable = 'EDI_PASS';
		END IF; 
    		UPDATE TBL_VARIABLES SET VAlfanumerico = _smtp_host  WHERE ID_Variable = 'SMTP_HOST'; 
    		UPDATE TBL_VARIABLES SET VEntero = _smtp_port WHERE ID_Variable = 'SMTP_PORT';
    		UPDATE TBL_VARIABLES SET VAlfanumerico = _smtp_user  WHERE ID_Variable = 'SMTP_USER'; 
    		IF _smtp_pass <> ''
    		THEN 
			UPDATE TBL_VARIABLES SET VAlfanumerico = _smtp_pass  WHERE ID_Variable = 'SMTP_PASS';
		END IF;
		UPDATE TBL_VARIABLES SET VAlfanumerico = _s3_bukt  WHERE ID_Variable = 'S3_BUKT'; 
    		UPDATE TBL_VARIABLES SET VAlfanumerico = _s3_user  WHERE ID_Variable = 'S3_USER'; 
    		IF _s3_pass <> ''
    		THEN 
			UPDATE TBL_VARIABLES SET VAlfanumerico = _s3_pass  WHERE ID_Variable = 'S3_PASS';
		END IF;
	END IF;
	
		
	RETURN QUERY SELECT _err, _result, ''::varchar;
END
$BODY$
  LANGUAGE plpgsql;
   
--@FIN_BLOQUE
CREATE TABLE tbl_pac_registros_cancelados
(
  id_registro serial NOT NULL,
  fecha timestamp without time zone NOT NULL,
  servidor character(36) NOT NULL,
  bd character varying(30) NOT NULL,
  nombrearchivo character varying(254) NOT NULL,
  CONSTRAINT pk_tbl_pac_registros_cancelados PRIMARY KEY (id_registro)
); 

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_s3_registros_exitosos_conn(
    _servidor character,
    _bd character varying,
    _id_modulo character varying,
    _objids character varying,
    _idsep character varying,
    _nombre character varying,
    _tambites bigint)
  RETURNS SETOF record AS
$BODY$  
BEGIN
	IF (select count(*) from tbl_s3_registros_exitosos_conn where servidor = _servidor and bd = _bd and id_modulo = _id_modulo and obj_id1 = _objids and obj_id2 = _idsep and nombre = _nombre) = 0
	THEN
		INSERT INTO  tbl_s3_registros_exitosos_conn
		VALUES(_servidor, _bd, _id_modulo, _objids, _idsep, _nombre, now(), _tambites);
	ELSE
		UPDATE tbl_s3_registros_exitosos_conn
		SET fecha = now(), tambites = _tambites
		WHERE servidor = _servidor and bd = _bd and id_modulo = _id_modulo and obj_id1 = _objids and obj_id2 = _idsep and nombre = _nombre;
	END IF;
	
	RETURN QUERY select 0 as Res;
END
$BODY$
  LANGUAGE plpgsql;
  
--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_s3_registros_exitosos(
    _servidor character,
    _bd character varying,
    _id_modulo character varying,
    _objids character varying,
    _idsep character varying,
    _nombre character varying,
    _tambites bigint)
  RETURNS SETOF record AS
$BODY$  
BEGIN
	IF (select count(*) from tbl_s3_registros_exitosos where servidor = _servidor and bd = _bd and id_modulo = _id_modulo and obj_id1 = _objids and obj_id2 = _idsep and nombre = _nombre) = 0
	THEN
		INSERT INTO  tbl_s3_registros_exitosos
		VALUES(_servidor, _bd, _id_modulo, _objids, _idsep, _nombre, now(), _tambites);
	ELSE
		UPDATE tbl_s3_registros_exitosos
		SET fecha = now(), tambites = _tambites
		WHERE servidor = _servidor and bd = _bd and id_modulo = _id_modulo and obj_id1 = _objids and obj_id2 = _idsep and nombre = _nombre;
	END IF;
	
	RETURN QUERY select 0 as Res;
END
$BODY$
  LANGUAGE plpgsql;
  
--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_s3_registros_exitosos_eliminar_conn(
    _servidor character,
    _bd character varying,
    _id_modulo character varying,
    _objids character varying,
    _idsep character varying,
    _nombre character varying)
  RETURNS SETOF record AS
$BODY$  
BEGIN
	IF (select count(*) from tbl_s3_registros_exitosos_conn where servidor = _servidor and bd = _bd and id_modulo = _id_modulo and obj_id1 = _objids and obj_id2 = _idsep and nombre = _nombre) > 0
	THEN
		DELETE FROM  tbl_s3_registros_exitosos_conn
		WHERE servidor = _servidor and bd = _bd and id_modulo = _id_modulo and obj_id1 = _objids and obj_id2 = _idsep and nombre = _nombre;
	END IF;
	
	RETURN QUERY select 0 as Res;
END
$BODY$
  LANGUAGE plpgsql;

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_s3_registros_exitosos_eliminar(
    _servidor character,
    _bd character varying,
    _id_modulo character varying,
    _objids character varying,
    _idsep character varying,
    _nombre character varying)
  RETURNS SETOF record AS
$BODY$  
BEGIN
	IF (select count(*) from tbl_s3_registros_exitosos where servidor = _servidor and bd = _bd and id_modulo = _id_modulo and obj_id1 = _objids and obj_id2 = _idsep and nombre = _nombre) > 0
	THEN
		DELETE FROM  tbl_s3_registros_exitosos
		WHERE servidor = _servidor and bd = _bd and id_modulo = _id_modulo and obj_id1 = _objids and obj_id2 = _idsep and nombre = _nombre;
	END IF;
	
	RETURN QUERY select 0 as Res;
END
$BODY$
  LANGUAGE plpgsql;
  
--@FIN_BLOQUE
CREATE TABLE tbl_srv_servicios_bd
(
  id_servidor character(36) NOT NULL,
  basedatos character varying(30) NOT NULL,
  status character varying(10) NOT NULL,
  costomail numeric(19,4) NOT NULL,
  costos3mb numeric(19,4) NOT NULL,
  costosello numeric(19,4) NOT NULL,
  saldo numeric(19,4) NOT NULL,
  cobrarsello bit(1),
  cobrarmail bit(1),
  cobrars3mb bit(1),
  CONSTRAINT pk_tbl_srv_servicios_bd PRIMARY KEY (id_servidor, basedatos)
);

   
--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_regist_inises_eliminar(_id_registro integer)
  RETURNS SETOF record AS
$BODY$
DECLARE _err int; _result varchar(255); 
BEGIN
	_err := 0;
	_result := 'El registro de sesión bloqueado se elimino de la base de datos. Si no hay mas bloqueados para esta IP asociada, ya se puede utilizar de nuevo';
	
	IF(select count(*) from TBL_REGISTROS where ID_Registro = _ID_Registro) < 1
	THEN
		_err := 3;
		_result = 'No existe el id de registro de sesion. No se puede eliminar'; 
	ELSE
		IF(select Status from TBL_REGISTROS where ID_Registro = _ID_Registro) != 'B'
		THEN
			_err := 3;
			_result = 'ERROR: No se puede desbloquear la IP porque este registro no la esta bloqueando'; 
		END IF;
	END IF;

	IF _err = 0
	THEN
		DELETE FROM TBL_REGISTROS
		WHERE ID_Registro = _ID_Registro;

	END IF;
	
	RETURN QUERY SELECT _err, _result, _ID_Registro;
	
END
$BODY$
  LANGUAGE plpgsql;
  
--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_regist_inises_eliminar_desde(_fecha timestamp without time zone)
  RETURNS SETOF record AS
$BODY$
DECLARE _err int; _result varchar(255); 
BEGIN
	_err := 0;
	_result := 'El registro de sesión se ha eliminado desde la fecha indicada hacia atras';
	
	IF _err = 0
	THEN
		DELETE FROM TBL_REGISTROS
		WHERE Fecha <= _Fecha;

	END IF;
	
	RETURN QUERY SELECT _err, _result, _fecha::varchar;
	
END
$BODY$
  LANGUAGE plpgsql;
  

--@FIN_BLOQUE
CREATE OR REPLACE FUNCTION sp_regist_proc_eliminar_desde(_fecha timestamp without time zone)
  RETURNS SETOF record AS
$BODY$
DECLARE _err int; _result varchar(255); 
BEGIN
	_err := 0;
	_result := 'El registro de procesos se ha liberado desde la fecha indicada hacia atras';
	
	IF _err = 0
	THEN
		DELETE FROM TBL_REGPROC
		WHERE Fecha <= _Fecha;

	END IF;
	
	RETURN QUERY SELECT _err, _result, _fecha::varchar;
	
END
$BODY$
  LANGUAGE plpgsql;

--@FIN_BLOQUE

CREATE OR REPLACE VIEW view_usuarios_modulo AS 
 SELECT tbl_usuarios.id_usuario AS usuario, tbl_usuarios.nombre
   FROM tbl_usuarios;

--@FIN_BLOQUE

CREATE TABLE tbl_usuarios_roles
(
  id_rol character varying(10) NOT NULL,
  nombre character varying(80) NOT NULL,
  CONSTRAINT pk_tbl_usuarios_roles PRIMARY KEY (id_rol)
);

--@FIN_BLOQUE

CREATE TABLE tbl_usuarios_submodulo_roles
(
  id_usuario character varying(10) NOT NULL,
  id_rol character varying(10) NOT NULL,
  CONSTRAINT pk_tbl_usuarios_submodulo_roles PRIMARY KEY (id_usuario, id_rol),
  CONSTRAINT fk_tbl_usuarios_submodulo_roles_tbl_usuarios FOREIGN KEY (id_usuario)
      REFERENCES tbl_usuarios (id_usuario) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_tbl_usuarios_submodulo_roles_tbl_usuarios_roles FOREIGN KEY (id_rol)
      REFERENCES tbl_usuarios_roles (id_rol) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
);

--@FIN_BLOQUE

CREATE OR REPLACE VIEW view_usuarios_submodulo_roles AS 
 SELECT p.id_usuario, c.id_rol, 
        CASE
            WHEN c.id_rol::text ~~ 'saf-%'::text THEN (( SELECT m.msj1
               FROM tbl_msj m
              WHERE m.alc = 'SAF'::bpchar AND m.mod::text = 'ROLES'::text AND m.sub::text = 'ROL_SIS'::text AND m.elm::text = c.id_rol::text))::character varying(80)
            ELSE c.nombre
        END AS nombre, 
        CASE
            WHEN (( SELECT count(*) AS count
               FROM tbl_usuarios_submodulo_roles
              WHERE tbl_usuarios_submodulo_roles.id_usuario::text = p.id_usuario::text AND tbl_usuarios_submodulo_roles.id_rol::text = c.id_rol::text)) = 0 THEN 0
            ELSE 1
        END AS enrolado
   FROM tbl_usuarios_roles c, tbl_usuarios p
  WHERE p.id_usuario::text <> c.id_rol::text;

--@FIN_BLOQUE

CREATE OR REPLACE FUNCTION sp_usuarios_agregar(_id_usuario character varying, _password character varying, _nombre character varying)
  RETURNS SETOF record AS
$BODY$
DECLARE 
	_err int; _result varchar(255);
BEGIN
	_err := 0;
	_result := (select msj1 from tbl_msj m where m.alc::text = 'SAF' and m.mod::text = 'ADMIN_USUARIOS' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCOK');
	
	IF(select count(*) from TBL_USUARIOS where ID_Usuario = _ID_Usuario) > 0
	THEN
		_err := 3;
		_result := (select msj1 from tbl_msj m where m.alc::text = 'SAF' and m.mod::text = 'ADMIN_USUARIOS' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR');
	END IF;

	IF(_ID_Usuario = 'saf-su' or _ID_Usuario = 'saf-serv' or _ID_Usuario like 'saf-%')
	THEN
		_err := 3;
		_result = (select msj1 from tbl_msj m where m.alc::text = 'SAF' and m.mod::text = 'ADMIN_USUARIOS' and m.sub::text = 'DLG' and m.elm::text = 'MSJ-PROCERR');
	END IF;
	
	IF _err = 0
	THEN
		INSERT INTO TBL_USUARIOS
		VALUES(_ID_Usuario, _Password, _Nombre);

		INSERT INTO TBL_USUARIOS_PERMISOS
		SELECT _ID_Usuario, ID_Permiso, '0'
		FROM TBL_USUARIOS_PERMISOS_CATALOGO;

		INSERT INTO TBL_USUARIOS_ROLES
		VALUES(_ID_Usuario, _Nombre);
	END IF;

	RETURN QUERY SELECT _err, _result, _ID_Usuario;
END
$BODY$
  LANGUAGE plpgsql;
  
--@FIN_BLOQUE

CREATE OR REPLACE FUNCTION sp_usuarios_cambiar(_id_usuario character varying, _password character varying, _nombre character varying)
  RETURNS SETOF record AS
$BODY$
DECLARE 
	_err int; _result varchar(255);
BEGIN
	_err := 0;
	_result := (select msj2 from tbl_msj m where m.alc::text = 'SAF' and m.mod::text = 'ADMIN_USUARIOS' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCOK');

	IF(select count(*) from TBL_USUARIOS where ID_Usuario = _ID_Usuario) < 1
	THEN
		_err := 3;
		_result := (select msj2 from tbl_msj m where m.alc::text = 'SAF' and m.mod::text = 'ADMIN_USUARIOS' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR');
	END IF;

	IF(_ID_Usuario = 'saf-su' or _ID_Usuario = 'saf-serv' or _ID_Usuario like 'saf-%')
	THEN
		_err := 3;
		_result = (select msj1 from tbl_msj m where m.alc::text = 'SAF' and m.mod::text = 'ADMIN_USUARIOS' and m.sub::text = 'DLG' and m.elm::text = 'MSJ-PROCERR');
	END IF;

	IF _err = 0
	THEN
		UPDATE TBL_USUARIOS
		SET Password = _Password, Nombre = _Nombre
		WHERE ID_Usuario = _ID_Usuario;

		UPDATE TBL_USUARIOS_ROLES
		SET Nombre = _Nombre
		WHERE ID_Rol = _ID_Usuario;
	END IF;

	RETURN QUERY SELECT _err, _result, _ID_Usuario;
END
$BODY$
  LANGUAGE plpgsql;

--@FIN_BLOQUE

CREATE OR REPLACE FUNCTION sp_usuarios_eliminar(_id_usuario character varying)
  RETURNS SETOF record AS
$BODY$
DECLARE 
	_err int; _result varchar(255);
BEGIN
	_err := 0;
	_result := (select msj3 from tbl_msj m where m.alc::text = 'SAF' and m.mod::text = 'ADMIN_USUARIOS' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCOK');
	
	IF(select count(*) from TBL_USUARIOS where ID_Usuario = _ID_Usuario) < 1
	THEN
		_err := 3;
		_result := (select msj2 from tbl_msj m where m.alc::text = 'SAF' and m.mod::text = 'ADMIN_USUARIOS' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR');
	END IF;

	IF(_ID_Usuario = 'saf-su' or _ID_Usuario = 'saf-serv' or _ID_Usuario like 'saf-%')
	THEN
		_err := 3;
		_result = (select msj1 from tbl_msj m where m.alc::text = 'SAF' and m.mod::text = 'ADMIN_USUARIOS' and m.sub::text = 'DLG' and m.elm::text = 'MSJ-PROCERR');
	END IF;
	
	IF _err = 0
	THEN
		DELETE FROM TBL_USUARIOS_PERMISOS
		WHERE ID_Usuario = _ID_Usuario;
		
		DELETE FROM TBL_USUARIOS
		WHERE ID_Usuario = _ID_Usuario;

		DELETE FROM TBL_USUARIOS_ROLES
		WHERE ID_Rol = _ID_Usuario;
	END IF;

	RETURN QUERY SELECT _err, _result, _ID_Usuario;
END
$BODY$
  LANGUAGE plpgsql;

--@FIN_BLOQUE

CREATE OR REPLACE FUNCTION sp_usuarios_enrol(_id_usuario character varying)
  RETURNS SETOF record AS
$BODY$
DECLARE 
	_err int; _result varchar(255);
BEGIN
	_err := 0;
	_result := (select msj1 from tbl_msj m where m.alc::text = 'SAF' and m.mod::text = 'ADMIN_USUARIOS' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCOK2');
	
	IF(select count(*) from TBL_USUARIOS where ID_Usuario = _ID_Usuario) < 1
	THEN
		_err := 3;
		_result := (select msj2 from tbl_msj m where m.alc::text = 'SAF' and m.mod::text = 'ADMIN_USUARIOS' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR');
	END IF;

	IF(_ID_Usuario = 'saf-su' or _ID_Usuario = 'saf-serv' or _ID_Usuario like 'saf-%')
	THEN
		_err := 3;
		_result = (select msj1 from tbl_msj m where m.alc::text = 'SAF' and m.mod::text = 'ADMIN_USUARIOS' and m.sub::text = 'DLG' and m.elm::text = 'MSJ-PROCERR');
	END IF;

	IF _err = 0
	THEN
		-- Primero borra todos los erolamientos
		delete from TBL_USUARIOS_SUBMODULO_ROLES
		where ID_Usuario = _ID_Usuario;
		
		-- Ahora agrega todos los roles desde las tablas temporales
		insert into TBL_USUARIOS_SUBMODULO_ROLES
		select _ID_Usuario, ID_Rol
		from _TMP_USUARIOS_SUBMODULO_ROLES;
		
	END IF;

	RETURN QUERY SELECT _err, _result, _ID_Usuario;
END
$BODY$
  LANGUAGE plpgsql;

--@FIN_BLOQUE

CREATE OR REPLACE FUNCTION sp_usuarios_permisos(_id_usuario character varying)
  RETURNS SETOF record AS
$BODY$
DECLARE 
	_err int; _result varchar(255);
BEGIN
	_err := 0;
	_result := (select msj2 from tbl_msj m where m.alc::text = 'SAF' and m.mod::text = 'ADMIN_USUARIOS' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCOK2');
	
	IF(select count(*) from TBL_USUARIOS where ID_Usuario = _ID_Usuario) < 1
	THEN
		_err := 3;
		_result := (select msj2 from tbl_msj m where m.alc::text = 'SAF' and m.mod::text = 'ADMIN_USUARIOS' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR');
	END IF;

	IF(_ID_Usuario = 'saf-su' or _ID_Usuario = 'saf-serv' or _ID_Usuario like 'saf-%')
	THEN
		_err := 3;
		_result = (select msj1 from tbl_msj m where m.alc::text = 'SAF' and m.mod::text = 'ADMIN_USUARIOS' and m.sub::text = 'DLG' and m.elm::text = 'MSJ-PROCERR');
	END IF;

	IF _err = 0
	THEN
		-- Primero borra todos los permisos
		DELETE FROM TBL_USUARIOS_PERMISOS
		WHERE ID_Usuario = _ID_Usuario;

		-- Ahora agrega todos los permisos desde las tablas temporales
		INSERT INTO TBL_USUARIOS_PERMISOS
		SELECT _ID_Usuario, c.ID_Permiso,
			(case when ( select count(*) from _TMP_USUARIOS_PERMISOS where ID_Permiso = c.ID_Permiso ) = 0 
				then '0' else '1' end)::bit
		FROM TBL_USUARIOS_PERMISOS_CATALOGO c;

	END IF;

	RETURN QUERY SELECT _err, _result, _ID_Usuario;
END
$BODY$
  LANGUAGE plpgsql;
  
--@FIN_BLOQUE

insert into tbl_usuarios_roles
values('saf-serv','');

insert into tbl_usuarios_roles
values('saf-doc','');

insert into tbl_usuarios_roles
values('saf-rol','');

insert into tbl_usuarios_roles
values('saf-regs','');

--@FIN_BLOQUE

CREATE TABLE tbl_reports
(
  id_report smallint NOT NULL,
  description character varying(254) NOT NULL,
  tipo character varying(30) NOT NULL,
  titulo character varying(254),
  encl1 character varying(254),
  encl2 character varying(254),
  encl3 character varying(254),
  l1 character varying(254),
  l2 character varying(254),
  l3 character varying(254),
  cl1 character varying(254),
  cl2 character varying(254),
  cl3 character varying(254),
  hw smallint,
  vw smallint,
  subtipo character varying(30) NOT NULL,
  clave character varying(10),
  graficar bit(1) NOT NULL,
  CONSTRAINT pk_tbl_reports PRIMARY KEY (id_report),
  CONSTRAINT fk_tbl_reports_tbl_usuarios_permisos_catalogo FOREIGN KEY (tipo)
      REFERENCES tbl_usuarios_permisos_catalogo (id_permiso) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_tbl_reports_tbl_usuarios_permisos_catalogo2 FOREIGN KEY (subtipo)
      REFERENCES tbl_usuarios_permisos_catalogo (id_permiso) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

--@FIN_BLOQUE

CREATE TABLE tbl_reports_filter
(
  id_report smallint NOT NULL,
  id_column smallint NOT NULL,
  instructions character varying(254) NOT NULL,
  isrange bit(1) NOT NULL,
  pridataname character varying(254) NOT NULL,
  pridefault character varying(8000),
  secdataname character varying(254) NOT NULL,
  secdefault character varying(8000),
  binddatatype character varying(50) NOT NULL,
  fromcatalog bit(1) NOT NULL,
  select_clause character varying(8000) NOT NULL,
  CONSTRAINT pk_tbl_reports_filter PRIMARY KEY (id_report, id_column),
  CONSTRAINT fk_tbl_reports_filter_tbl_reports FOREIGN KEY (id_report)
      REFERENCES tbl_reports (id_report) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT ck_tbl_reports_filter CHECK (binddatatype::text = 'INT'::text OR binddatatype::text = 'STRING'::text OR binddatatype::text = 'BYTE'::text OR binddatatype::text = 'BOOL'::text OR binddatatype::text = 'TIME'::text OR binddatatype::text = 'NUMERIC'::text OR binddatatype::text = 'MONEY'::text)
);

--@FIN_BLOQUE

CREATE TABLE tbl_reports_sentences
(
  id_report smallint NOT NULL,
  id_sentence smallint NOT NULL,
  id_iscompute smallint NOT NULL,
  select_clause character varying(8000) NOT NULL,
  tabprintpnt numeric(5,2),
  format character varying(254),
  CONSTRAINT pk_tbl_reports_sentences PRIMARY KEY (id_report, id_sentence, id_iscompute),
  CONSTRAINT fk_tbl_reports_sentences_tbl_reports FOREIGN KEY (id_report)
      REFERENCES tbl_reports (id_report) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT ck_tbl_reports_sentences CHECK (id_sentence = 1 OR id_sentence = 2 OR id_sentence = 3)
);

--@FIN_BLOQUE

CREATE TABLE tbl_reports_sentences_columns
(
  id_report smallint NOT NULL,
  id_sentence smallint NOT NULL,
  id_iscompute smallint NOT NULL,
  id_column smallint NOT NULL,
  colname character varying(254) NOT NULL,
  binddatatype character varying(50) NOT NULL,
  willshow bit(1) NOT NULL,
  format character varying(254) NOT NULL,
  ancho numeric(5,2) NOT NULL,
  alinhor character varying(20),
  fgcolor character(6),
  CONSTRAINT pk_tbl_reports_sentences_columns PRIMARY KEY (id_report, id_sentence, id_iscompute, id_column),
  CONSTRAINT fk_tbl_reports_sentences_columns_tbl_reports_sentences FOREIGN KEY (id_report, id_sentence, id_iscompute)
      REFERENCES tbl_reports_sentences (id_report, id_sentence, id_iscompute) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT ck_tbl_reports_sentences_columns CHECK (id_iscompute = 0 OR id_iscompute = 1)
);

--@FIN_BLOQUE
CREATE TABLE tbl_reports_help
(
  id_report smallint NOT NULL,
  help text NOT NULL,
  CONSTRAINT pk_tbl_reports_help PRIMARY KEY (id_report),
  CONSTRAINT fk_tbl_reports_help_tbl_reports FOREIGN KEY (id_report)
      REFERENCES tbl_reports (id_report) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
);

--@FIN_BLOQUE

CREATE TABLE tbl_usuarios_submodulo_reportes
(
  id_usuario character varying(10) NOT NULL,
  id_report smallint NOT NULL,
  CONSTRAINT pk_tbl_usuario_submodulo_reportes PRIMARY KEY (id_usuario, id_report),
  CONSTRAINT fk_tbl_usuarios_submodulo_reportes_tbl_reports FOREIGN KEY (id_report)
      REFERENCES tbl_reports (id_report) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_tbl_usuarios_submodulo_reportes_tbl_usuarios FOREIGN KEY (id_usuario)
      REFERENCES tbl_usuarios (id_usuario) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
);

--@FIN_BLOQUE

CREATE OR REPLACE VIEW view_reportes AS 
 SELECT u.id_usuario, m.id_report, m.description, m.tipo, m.subtipo, m.clave, m.graficar
   FROM tbl_reports m
   JOIN tbl_usuarios_submodulo_reportes u ON m.id_report = u.id_report;

--@FIN_BLOQUE

CREATE OR REPLACE VIEW view_reportes_su AS 
 SELECT 'saf-su'::character varying AS id_usuario, tbl_reports.id_report
   FROM tbl_reports;

--@FIN_BLOQUE

CREATE OR REPLACE VIEW view_usuarios_submodulo_reportes AS 
 SELECT p.id_usuario, c.id_report, c.tipo, c.subtipo, c.description, 
        CASE
            WHEN (( SELECT count(*) AS count
               FROM tbl_usuarios_submodulo_reportes
              WHERE tbl_usuarios_submodulo_reportes.id_usuario::text = p.id_usuario::text AND tbl_usuarios_submodulo_reportes.id_report = c.id_report)) = 0 THEN 0
            ELSE 1
        END AS permitido
   FROM tbl_reports c, tbl_usuarios p;

--@FIN_BLOQUE   

CREATE OR REPLACE FUNCTION view_reportes(_id_usuario character varying, _tipo character varying, _entidad character varying)
  RETURNS SETOF record AS
$BODY$  
BEGIN
	IF _ID_Usuario = 'saf-su'
	THEN
		IF _Entidad = 'SAF-X' -- Regresa todo el set
		THEN
			RETURN QUERY
			SELECT _id_usuario as id_usuario, m.id_report, m.description, ms.msj2 as tipo, m.subtipo, m.clave, m.graficar
			FROM tbl_reports m JOIN tbl_msj ms ON 
				ms.alc::text = 'SAF'::text AND ms.mod::text = 'PERMISOS'::text AND ms.sub::text = 'CATALOGO'::text AND ms.elm::text = m.tipo::text
			WHERE m.tipo LIKE _tipo || '_%'
			ORDER BY m.id_report ASC; 
		ELSE -- _Entidad = 'SAF-1' --Regresa el reporte especificado en _tipo... _tipo contendrá el id del reporte
			RETURN QUERY 
			SELECT _id_usuario as id_usuario, m.id_report, m.description, ms.msj2 as tipo, m.subtipo, m.clave, m.graficar
			FROM tbl_reports m JOIN tbl_msj ms ON 
				ms.alc::text = 'SAF'::text AND ms.mod::text = 'PERMISOS'::text AND ms.sub::text = 'CATALOGO'::text AND ms.elm::text = m.tipo::text
			WHERE m.id_report = _tipo::smallint;
		END IF;
	ELSE
		IF _Entidad = 'SAF-X' -- Regresa todo el set
		THEN
			RETURN QUERY 
			SELECT _id_usuario as id_usuario, m.id_report, m.description, ms.msj2 as tipo, m.subtipo, m.clave, m.graficar
			FROM tbl_reports m JOIN tbl_msj ms ON 
				ms.alc::text = 'SAF'::text AND ms.mod::text = 'PERMISOS'::text AND ms.sub::text = 'CATALOGO'::text AND ms.elm::text = m.tipo::text
			JOIN tbl_usuarios_submodulo_reportes u ON m.id_report = u.id_report
			WHERE m.tipo LIKE _tipo || '_%' AND u.id_usuario = _id_usuario
			ORDER BY m.id_report ASC;
		ELSE -- _Entidad = 'SAF-1' --Especifico
			RETURN QUERY 
			SELECT _id_usuario as id_usuario, m.id_report, m.description, ms.msj2 as tipo, m.subtipo, m.clave, m.graficar
			FROM tbl_reports m JOIN tbl_msj ms ON 
				ms.alc::text = 'SAF'::text AND ms.mod::text = 'PERMISOS'::text AND ms.sub::text = 'CATALOGO'::text AND ms.elm::text = m.tipo::text
			JOIN tbl_usuarios_submodulo_reportes u ON m.id_report = u.id_report
			WHERE m.id_report = _tipo::smallint  AND u.id_usuario = _id_usuario;
		END IF;
	END IF;
END
$BODY$
  LANGUAGE plpgsql;
  
--@FIN_BLOQUE

CREATE OR REPLACE FUNCTION sp_usuarios_enlrep(_id_usuario character varying)
  RETURNS SETOF record AS
$BODY$
DECLARE 
	_err int; _result varchar(255);
BEGIN
	_err := 0;
	_result := (select msj5 from tbl_msj m where m.alc::text = 'SAF' and m.mod::text = 'ADMIN_USUARIOS' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCOK');
	
	IF(select count(*) from TBL_USUARIOS where ID_Usuario = _ID_Usuario) < 1
	THEN
		_err := 3;
		_result := (select msj2 from tbl_msj m where m.alc::text = 'SAF' and m.mod::text = 'ADMIN_USUARIOS' and m.sub::text = 'BD' and m.elm::text = 'MSJ-PROCERR');
	END IF;

	IF(_ID_Usuario = 'saf-su' or _ID_Usuario = 'saf-admin' or _ID_Usuario like 'saf-%')
	THEN
		_err := 3;
		_result = (select msj1 from tbl_msj m where m.alc::text = 'SAF' and m.mod::text = 'ADMIN_USUARIOS' and m.sub::text = 'DLG' and m.elm::text = 'MSJ-PROCERR');
	END IF;

	IF _err = 0
	THEN
		-- Primero borra todos los permisos
		delete from TBL_USUARIOS_SUBMODULO_REPORTES
		where ID_Usuario = _ID_Usuario;
		
		-- Ahora agrega todos los permisos desde las tablas temporales
		insert into TBL_USUARIOS_SUBMODULO_REPORTES
		select _ID_Usuario, ID_Report
		from _TMP_USUARIOS_SUBMODULO_REPORTES;
		
	END IF;

	RETURN QUERY SELECT _err, _result, _ID_Usuario;
END
$BODY$
  LANGUAGE plpgsql;
  
--@FIN_BLOQUE
CREATE TABLE tbl_sat_bancos
(
  clave character(3) NOT NULL,
  nombre character varying(50) NOT NULL,
  razonsocial character varying(254) NOT NULL,
  CONSTRAINT pk_tbl_sat_bancos PRIMARY KEY (clave)
);

--@FIN_BLOQUE
insert into tbl_sat_bancos
values('000','---------------','');
insert into tbl_sat_bancos
values('002','BANAMEX','Banco Nacional de México, S.A., Institución de Banca Múltiple, Grupo Financiero Banamex');
insert into tbl_sat_bancos
values('006','BANCOMEXT','Banco Nacional de Comercio Exterior, Sociedad Desarrollo');
insert into tbl_sat_bancos
values('009','BANOBRAS','Banco Nacional de Obras y Servicios Públicos, Sociedad Nacional de Crédito, Institución de Banca de Desarrollo');
insert into tbl_sat_bancos
values('012','BBVA BANCOMER','BBVA Bancomer, S.A., Institución de Banca Múltiple, Grupo Financiero BBVA Bancomer');
insert into tbl_sat_bancos
values('014','SANTANDER','Banco Santander (México), S.A., Institución de Banca Múltiple, Grupo Financiero Santander');
insert into tbl_sat_bancos
values('019','BANJERCITO','Banco Nacional del Ejército, Fuerza Aérea y Armada, Sociedad Nacional de Crédito, Institución de Banca de Desarrollo');
insert into tbl_sat_bancos
values('021','HSBC','HSBC México, S.A., institución De Banca Múltiple, Grupo Financiero HSBC');
insert into tbl_sat_bancos
values('030','BAJIO','Banco del Bajío, S.A., Institución de Banca Múltiple');
insert into tbl_sat_bancos
values('032','IXE','IXE Banco, S.A., Institución de Banca Múltiple, IXE Grupo Financiero');
insert into tbl_sat_bancos
values('036','INBURSA','Banco Inbursa, S.A., Institución de Banca Múltiple, Grupo Financiero Inbursa');
insert into tbl_sat_bancos
values('037','INTERACCIONES','Banco Interacciones, S.A., Institución de Banca Múltiple');
insert into tbl_sat_bancos
values('042','MIFEL','Banca Mifel, S.A., Institución de Banca Múltiple, Grupo Financiero Mifel');
insert into tbl_sat_bancos
values('044','SCOTIABANK','Scotiabank Inverlat, S.A.');
insert into tbl_sat_bancos
values('058','BANREGIO','Banco Regional de Monterrey, S.A., Institución de Banca Múltiple, Banregio Grupo Financiero');
insert into tbl_sat_bancos
values('059','INVEX','Banco Invex, S.A., Institución de Banca Múltiple, Invex Grupo Financiero');
insert into tbl_sat_bancos
values('060','BANSI','Bansi, S.A., Institución de Banca Múltiple');
insert into tbl_sat_bancos
values('062','AFIRME','Banca Afirme, S.A., Institución de Banca Múltiple');
insert into tbl_sat_bancos
values('072','BANORTE','Banco Mercantil del Norte, S.A., Institución de Banca Múltiple, Grupo Financiero Banorte');
insert into tbl_sat_bancos
values('102','THE ROYAL BANK','The Royal Bank of Scotland México, S.A., Institución de Banca Múltiple');
insert into tbl_sat_bancos
values('103','AMERICAN EXPRESS','American Express Bank (México), S.A., Institución de Banca Múltiple');
insert into tbl_sat_bancos
values('106','BAMSA','Bank of America México, S.A., Institución de Banca Múltiple, Grupo Financiero Bank of America');
insert into tbl_sat_bancos
values('108','TOKYO','Bank of Tokyo-Mitsubishi UFJ (México), S.A.');
insert into tbl_sat_bancos
values('110','JP MORGAN','Banco J.P. Morgan, S.A., Institución de Banca Múltiple, J.P. Morgan Grupo Financiero');
insert into tbl_sat_bancos
values('112','BMONEX','Banco Monex, S.A., Institución de Banca Múltiple');
insert into tbl_sat_bancos
values('113','VE POR MAS','Banco Ve Por Mas, S.A. Institución de Banca Múltiple');
insert into tbl_sat_bancos
values('116','ING','ING Bank (México), S.A., Institución de Banca Múltiple, ING Grupo Financiero');
insert into tbl_sat_bancos
values('124','DEUTSCHE','Deutsche Bank México, S.A., Institución de Banca Múltiple');
insert into tbl_sat_bancos
values('126','CREDIT SUISSE','Banco Credit Suisse (México), S.A. Institución de Banca Múltiple, Grupo Financiero Credit Suisse (México)');
insert into tbl_sat_bancos
values('127','AZTECA','Banco Azteca, S.A. Institución de Banca Múltiple.');
insert into tbl_sat_bancos
values('128','AUTOFIN','Banco Autofin México, S.A. Institución de Banca Múltiple');
insert into tbl_sat_bancos
values('129','BARCLAYS','Barclays Bank México, S.A., Institución de Banca Múltiple, Grupo Financiero Barclays México');
insert into tbl_sat_bancos
values('130','COMPARTAMOS','Banco Compartamos, S.A., Institución de Banca Múltiple');
insert into tbl_sat_bancos
values('131','BANCO FAMSA','Banco Ahorro Famsa, S.A., Institución de Banca Múltiple');
insert into tbl_sat_bancos
values('132','BMULTIVA','Banco Multiva, S.A., Institución de Banca Múltiple, Multivalores Grupo Financiero');
insert into tbl_sat_bancos
values('133','ACTINVER','Banco Actinver, S.A. Institución de Banca Múltiple, Grupo Financiero Actinver');
insert into tbl_sat_bancos
values('134','WAL-MART','Banco Wal-Mart de México Adelante, S.A., Institución de Banca Múltiple');
insert into tbl_sat_bancos
values('135','NAFIN','Nacional Financiera, Sociedad Nacional de Crédito, Institución de Banca de Desarrollo');
insert into tbl_sat_bancos
values('136','INTERBANCO','Inter Banco, S.A. Institución de Banca Múltiple');
insert into tbl_sat_bancos
values('137','BANCOPPEL','BanCoppel, S.A., Institución de Banca Múltiple');
insert into tbl_sat_bancos
values('138','ABC CAPITAL','ABC Capital, S.A., Institución de Banca Múltiple');
insert into tbl_sat_bancos
values('139','UBS BANK','UBS Bank México, S.A., Institución de Banca Múltiple, UBS Grupo Financiero');
insert into tbl_sat_bancos
values('140','CONSUBANCO','Consubanco, S.A. Institución de Banca Múltiple');
insert into tbl_sat_bancos
values('141','VOLKSWAGEN','Volkswagen Bank, S.A., Institución de Banca Múltiple');
insert into tbl_sat_bancos
values('143','CIBANCO','CIBanco, S.A.');
insert into tbl_sat_bancos
values('145','BBASE','Banco Base, S.A., Institución de Banca Múltiple');
insert into tbl_sat_bancos
values('166','BANSEFI','Banco del Ahorro Nacional y Servicios Financieros, Sociedad Nacional de Crédito, Institución de Banca de Desarrollo');
insert into tbl_sat_bancos
values('168','HIPOTECARIA FEDERAL','Sociedad Hipotecaria Federal, Sociedad Nacional de Crédito, Institución de Banca de Desarrollo');
insert into tbl_sat_bancos
values('600','MONEXCB','Monex Casa de Bolsa, S.A. de C.V. Monex Grupo Financiero');
insert into tbl_sat_bancos
values('601','GBM','GBM Grupo Bursátil Mexicano, S.A. de C.V. Casa de Bolsa');
insert into tbl_sat_bancos
values('602','MASARI','Masari Casa de Bolsa, S.A.');
insert into tbl_sat_bancos
values('605','VALUE','Value, S.A. de C.V. Casa de Bolsa');
insert into tbl_sat_bancos
values('606','ESTRUCTURADORES','Estructuradores del Mercado de Valores Casa de Bolsa, S.A. de C.V.');
insert into tbl_sat_bancos
values('607','TIBER','Casa de Cambio Tiber, S.A. de C.V.');
insert into tbl_sat_bancos
values('608','VECTOR','Vector Casa de Bolsa, S.A. de C.V.');
insert into tbl_sat_bancos
values('610','B&B','B y B, Casa de Cambio, S.A. de C.V.');
insert into tbl_sat_bancos
values('614','ACCIVAL','Acciones y Valores Banamex, S.A. de C.V., Casa de Bolsa');
insert into tbl_sat_bancos
values('615','MERRILL LYNCH','Merrill Lynch México, S.A. de C.V. Casa de Bolsa');
insert into tbl_sat_bancos
values('616','FINAMEX','Casa de Bolsa Finamex, S.A. de C.V.');
insert into tbl_sat_bancos
values('617','VALMEX','Valores Mexicanos Casa de Bolsa, S.A. de C.V.');
insert into tbl_sat_bancos
values('618','UNICA','Unica Casa de Cambio, S.A. de C.V.');
insert into tbl_sat_bancos
values('619','MAPFRE','MAPFRE Tepeyac, S.A.');
insert into tbl_sat_bancos
values('620','PROFUTURO','Profuturo G.N.P., S.A. de C.V., Afore');
insert into tbl_sat_bancos
values('621','CB ACTINVER','Actinver Casa de Bolsa, S.A. de C.V.');
insert into tbl_sat_bancos
values('622','OACTIN','OPERADORA ACTINVER, S.A. DE C.V.');
insert into tbl_sat_bancos
values('623','SKANDIA','Skandia Vida, S.A. de C.V.');
insert into tbl_sat_bancos
values('626','CBDEUTSCHE','Deutsche Securities, S.A. de C.V. CASA DE BOLSA');
insert into tbl_sat_bancos
values('627','ZURICH','Zurich Compañía de Seguros, S.A.');
insert into tbl_sat_bancos
values('628','ZURICHVI','Zurich Vida, Compañía de Seguros, S.A.');
insert into tbl_sat_bancos
values('629','SU CASITA','Hipotecaria Su Casita, S.A. de C.V. SOFOM ENR');
insert into tbl_sat_bancos
values('630','CB INTERCAM','Intercam Casa de Bolsa, S.A. de C.V.');
insert into tbl_sat_bancos
values('631','CI BOLSA','CI Casa de Bolsa, S.A. de C.V.');
insert into tbl_sat_bancos
values('632','BULLTICK CB','Bulltick Casa de Bolsa, S.A., de C.V.');
insert into tbl_sat_bancos
values('633','STERLING','Sterling Casa de Cambio, S.A. de C.V.');
insert into tbl_sat_bancos
values('634','FINCOMUN','Fincomún, Servicios Financieros Comunitarios, S.A. de C.V.');
insert into tbl_sat_bancos
values('636','HDI SEGUROS','HDI Seguros, S.A. de C.V.');
insert into tbl_sat_bancos
values('637','ORDER','Order Express Casa de Cambio, S.A. de C.V');
insert into tbl_sat_bancos
values('638','AKALA','Akala, S.A. de C.V., Sociedad Financiera Popular');
insert into tbl_sat_bancos
values('640','CB JPMORGAN','J.P. Morgan Casa de Bolsa, S.A. de C.V. J.P. Morgan Grupo Financiero');
insert into tbl_sat_bancos
values('642','REFORMA','Operadora de Recursos Reforma, S.A. de C.V., S.F.P.');
insert into tbl_sat_bancos
values('646','STP','Sistema de Transferencias y Pagos STP, S.A. de C.V.SOFOM ENR');
insert into tbl_sat_bancos
values('647','TELECOMM','Telecomunicaciones de México');
insert into tbl_sat_bancos
values('648','EVERCORE','Evercore Casa de Bolsa, S.A. de C.V.');
insert into tbl_sat_bancos
values('649','SKANDIA','Skandia Operadora de Fondos, S.A. de C.V.');
insert into tbl_sat_bancos
values('651','SEGMTY','Seguros Monterrey New York Life, S.A de C.V');
insert into tbl_sat_bancos
values('652','ASEA','Solución Asea, S.A. de C.V., Sociedad Financiera Popular');
insert into tbl_sat_bancos
values('653','KUSPIT','Kuspit Casa de Bolsa, S.A. de C.V.');
insert into tbl_sat_bancos
values('655','SOFIEXPRESS','J.P. SOFIEXPRESS, S.A. de C.V., S.F.P.');
insert into tbl_sat_bancos
values('656','UNAGRA','UNAGRA, S.A. de C.V., S.F.P.');
insert into tbl_sat_bancos
values('659','OPCIONES EMPRESARIALES DEL NOROESTE','OPCIONES EMPRESARIALES DEL NORESTE, S.A. DE C.V., S.F.P.');
insert into tbl_sat_bancos
values('901','CLS','Cls Bank International');
insert into tbl_sat_bancos
values('902','INDEVAL','SD. Indeval, S.A. de C.V.');
insert into tbl_sat_bancos
values('670','LIBERTAD','Libertad Servicios Financieros, S.A. De C.V.');

--@FIN_BLOQUE
CREATE TABLE tbl_sat_paises
(
  alfa2 character(2) NOT NULL,
  alfa3 character(3) NOT NULL,
  numerico smallint NOT NULL,
  nombre character varying(254) NOT NULL,
  CONSTRAINT pk_tbl_sat_paises PRIMARY KEY (alfa2)
);

--@FIN_BLOQUE
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Afganistán','AF','AFG','004');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Islas Aland','AX','ALA','248');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Albania','AL','ALB','008');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Alemania','DE','DEU','276');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Andorra','AD','AND','020');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Angola','AO','AGO','024');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Anguila','AI','AIA','660');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Antártida','AQ','ATA','010');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Antigua y Barbuda','AG','ATG','028');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Arabia Saudita','SA','SAU','682');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Argelia','DZ','DZA','012');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Argentina','AR','ARG','032');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Armenia','AM','ARM','051');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Aruba','AW','ABW','533');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Australia','AU','AUS','036');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Austria','AT','AUT','040');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Azerbaiyán','AZ','AZE','031');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Las Bahamas','BS','BHS','044');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Bangladesh','BD','BGD','050');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Barbados','BB','BRB','052');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Bahrein','BH','BHR','048');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Bélgica','BE','BEL','056');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Belice','BZ','BLZ','084');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Benin','BJ','BEN','204');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Bermudas','BM','BMU','060');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Belarús','BY','BLR','112');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Bolivia','BO','BOL','068');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('San Eustaquio y Saba','BQ','BES','535');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Bosnia y Herzegovina','BA','BIH','070');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Botswana','BW','BWA','072');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Brasil','BR','BRA','076');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Brunei Darussalam','BN','BRN','096');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Bulgaria','BG','BGR','100');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Burkina Faso','BF','BFA','854');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Burundi','BI','BDI','108');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Bhután','BT','BTN','064');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Cabo Verde','CV','CPV','132');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Camboya','KH','KHM','116');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Camerún','CM','CMR','120');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Canadá','CA','CAN','124');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Qatar','QA','QAT','634');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Chad','TD','TCD','148');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Chile','CL','CHL','152');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('China','CN','CHN','156');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Chipre','CY','CYP','196');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Colombia','CO','COL','170');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Las Comoras','KM','COM','174');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Corea del Norte','KP','PRK','408');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Corea del Sur','KR','KOR','410');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Costa de Marfil','CI','CIV','384');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Costa Rica','CR','CRI','188');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Croacia','HR','HRV','191');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Cuba','CU','CUB','192');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Curaçao','CW','CUW','531');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Dinamarca','DK','DNK','208');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Dominica','DM','DMA','212');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Ecuador','EC','ECU','218');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Egipto','EG','EGY','818');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('El Salvador','SV','SLV','222');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Emiratos Árabes Unidos','AE','ARE','784');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Eritrea','ER','ERI','232');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Eslovaquia','SK','SVK','703');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Eslovenia','SI','SVN','705');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('España','ES','ESP','724');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Estados Unidos de América','US','USA','840');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Estonia','EE','EST','233');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Etiopía','ET','ETH','231');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Filipinas','PH','PHL','608');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Finlandia','FI','FIN','246');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Fiji','FJ','FJI','242');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Francia','FR','FRA','250');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Gabón','GA','GAB','266');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Gambia','GM','GMB','270');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Georgia','GE','GEO','268');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Ghana','GH','GHA','288');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Gibraltar','GI','GIB','292');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Granada','GD','GRD','308');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Grecia','GR','GRC','300');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Groenlandia','GL','GRL','304');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Guadeloupe','GP','GLP','312');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Guam','GU','GUM','316');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Guatemala','GT','GTM','320');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Guayana Francesa','GF','GUF','254');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Guernsey','GG','GGY','831');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Guinea','GN','GIN','324');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Guinea Bissau','GW','GNB','624');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Guinea Ecuatorial','GQ','GNQ','226');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Guyana','GY','GUY','328');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Haití','HT','HTI','332');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Honduras','HN','HND','340');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Hong Kong','HK','HKG','344');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Hungría','HU','HUN','348');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('India','IN','IND','356');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Indonesia','ID','IDN','360');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Iraq','IQ','IRQ','368');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Irán','IR','IRN','364');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Irlanda','IE','IRL','372');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Isla Bouvet','BV','BVT','074');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Isla de Man','IM','IMN','833');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Isla de Navidad','CX','CXR','162');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Islandia','IS','ISL','352');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Islas Caimán','KY','CYM','136');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Islas Cocos','CC','CCK','166');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Islas Cook','CK','COK','184');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Islas Feroe','FO','FRO','234');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Islas Georgias del Sur y Sandwich del Sur','GS','SGS','239');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Heard (Isla) e Islas McDonald','HM','HMD','334');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Islas Malvinas','FK','FLK','238');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Islas Marianas del Norte','MP','MNP','580');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Islas Marshall','MH','MHL','584');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Islas Pitcairn','PN','PCN','612');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Islas Salomón','SB','SLB','090');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Islas Turcas y Caicos','TC','TCA','796');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Islas ultramarinas de Estados Unidos','UM','UMI','581');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Islas Vírgenes Británicas','VG','VGB','092');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Islas Vírgenes de los Estados Unidos','VI','VIR','850');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Israel','IL','ISR','376');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Italia','IT','ITA','380');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Jamaica','JM','JAM','388');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Japón','JP','JPN','392');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Jersey','JE','JEY','832');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Jordania','JO','JOR','400');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Kazajstán','KZ','KAZ','398');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Kenya','KE','KEN','404');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Kirguistán','KG','KGZ','417');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Kiribati','KI','KIR','296');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Kuwait','KW','KWT','414');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('República Democrática Popular','LA','LAO','418');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Lesotho','LS','LSO','426');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Letonia','LV','LVA','428');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Líbano','LB','LBN','422');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Liberia','LR','LBR','430');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Libia','LY','LBY','434');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Liechtenstein','LI','LIE','438');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Lituania','LT','LTU','440');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Luxemburgo','LU','LUX','442');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Macao','MO','MAC','446');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Macedonia','MK','MKD','807');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Madagascar','MG','MDG','450');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Malasia','MY','MYS','458');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Malawi','MW','MWI','454');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Maldivas','MV','MDV','462');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Malí','ML','MLI','466');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Malta','MT','MLT','470');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Marruecos','MA','MAR','504');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Martinique','MQ','MTQ','474');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Mauricio','MU','MUS','480');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Mauritania','MR','MRT','478');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Mayotte','YT','MYT','175');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('México','MX','MEX','484');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Micronesia','FM','FSM','583');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Moldova','MD','MDA','498');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Mónaco','MC','MCO','492');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Mongolia','MN','MNG','496');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Montenegro','ME','MNE','499');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Montserrat','MS','MSR','500');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Mozambique','MZ','MOZ','508');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Myanmar','MM','MMR','104');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Namibia','NA','NAM','516');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Nauru','NR','NRU','520');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Nepal','NP','NPL','524');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Nicaragua','NI','NIC','558');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Níger','NE','NER','562');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Nigeria','NG','NGA','566');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Niue','NU','NIU','570');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Norfolk, Isla','NF','NFK','574');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Noruega','NO','NOR','578');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Nueva Caledonia','NC','NCL','540');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Nueva Zelandia','NZ','NZL','554');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Omán','OM','OMN','512');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Países Bajos','NL','NLD','528');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Pakistán','PK','PAK','586');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Palau','PW','PLW','585');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Palestina','PS','PSE','275');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Panamá','PA','PAN','591');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Nueva Guinea','PG','PNG','598');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Paraguay','PY','PRY','600');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Perú','PE','PER','604');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Polinesia Francesa','PF','PYF','258');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Polonia','PL','POL','616');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Portugal','PT','PRT','620');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Puerto Rico','PR','PRI','630');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Reino Unido','GB','GBR','826');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Sahara Occidental','EH','ESH','732');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('República Centroafricana','CF','CAF','140');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('República Checa','CZ','CZE','203');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('República del Congo','CG','COG','178');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('República Democrática del Congo','CD','COD','180');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('República Dominicana','DO','DOM','214');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Reunión','RE','REU','638');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Rwanda','RW','RWA','646');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Rumania','RO','ROU','642');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Rusia','RU','RUS','643');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Samoa','WS','WSM','882');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Samoa Americana','AS','ASM','016');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('San Bartolomé','BL','BLM','652');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('San Cristóbal y Nieves','KN','KNA','659');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('San Marino','SM','SMR','674');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('San Martín','MF','MAF','663');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('San Pedro y Miquelón','PM','SPM','666');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('San Vicente y las Granadinas','VC','VCT','670');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Santa Elena, Ascensión y Tristán de Acuña','SH','SHN','654');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Santa Lucía','LC','LCA','662');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Santo Tomé y Príncipe','ST','STP','678');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Senegal','SN','SEN','686');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Serbia','RS','SRB','688');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Seychelles','SC','SYC','690');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Sierra leona','SL','SLE','694');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Singapur','SG','SGP','702');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Sint Maarten','SX','SXM','534');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Siria','SY','SYR','760');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Somalia','SO','SOM','706');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Sri Lanka','LK','LKA','144');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Swazilandia','SZ','SWZ','748');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Sudáfrica','ZA','ZAF','710');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Sudán','SD','SDN','729');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Sudán del Sur','SS','SSD','728');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Suecia','SE','SWE','752');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Suiza','CH','CHE','756');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Surinam','SR','SUR','740');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Svalbard y Jan Mayen','SJ','SJM','744');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Tailandia','TH','THA','764');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Taiwán','TW','TWN','158');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Tanzania','TZ','TZA','834');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Tayikistán','TJ','TJK','762');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Territorio Británico del Océano Índico','IO','IOT','086');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Tierras Australes Francesas','TF','ATF','260');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Timor-Leste','TL','TLS','626');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Togo','TG','TGO','768');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Tokelau','TK','TKL','772');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Tonga','TO','TON','776');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Trinidad y Tabago','TT','TTO','780');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Túnez','TN','TUN','788');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Turkmenistán','TM','TKM','795');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Turquía','TR','TUR','792');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Tuvalu','TV','TUV','798');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Ucrania','UA','UKR','804');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Uganda','UG','UGA','800');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Uruguay','UY','URY','858');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Uzbekistán','UZ','UZB','860');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Vanuatu','VU','VUT','548');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Vaticano','VA','VAT','336');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Venezuela','VE','VEN','862');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Vietnam','VN','VNM','704');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Wallis y Futuna','WF','WLF','876');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Yemen','YE','YEM','887');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Djibouti','DJ','DJI','262');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Zambia','ZM','ZMB','894');
INSERT INTO TBL_SAT_PAISES(Nombre, Alfa2, Alfa3, Numerico)
VALUES('Zimbabwe','ZW','ZWE','716');

--@FIN_BLOQUE
CREATE TABLE tbl_sat_estados
(
  codpais2 character(2) NOT NULL,
  codpais3 character(3) NOT NULL,
  codpaisnum smallint NOT NULL,
  codestado character varying(10) NOT NULL,
  nombre character varying(254) NOT NULL,
  CONSTRAINT pk_tbl_sat_estados PRIMARY KEY (codpais2, codestado)
);

--@FIN_BLOQUE
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('AGU','MX','MEX','484','Aguascalientes');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('BCN','MX','MEX','484','Baja California');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('BCS','MX','MEX','484','Baja California Sur');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('CAM','MX','MEX','484','Campeche');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('CHP','MX','MEX','484','Chiapas');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('CHH','MX','MEX','484','Chihuahua');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('COA','MX','MEX','484','Coahuila');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('COL','MX','MEX','484','Colima');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('DIF','MX','MEX','484','Distrito Federal');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('DUR','MX','MEX','484','Durango');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('GUA','MX','MEX','484','Guanajuato');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('GRO','MX','MEX','484','Guerrero');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('HID','MX','MEX','484','Hidalgo');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('JAL','MX','MEX','484','Jalisco');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('MEX','MX','MEX','484','Estado de México');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('MIC','MX','MEX','484','Michoacán');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('MOR','MX','MEX','484','Morelos');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('NAY','MX','MEX','484','Nayarit');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('NLE','MX','MEX','484','Nuevo León');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('OAX','MX','MEX','484','Oaxaca');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('PUE','MX','MEX','484','Puebla');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('QTO','MX','MEX','484','Querétaro');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('ROO','MX','MEX','484','Quintana Roo');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('SLP','MX','MEX','484','San Luis Potosí');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('SIN','MX','MEX','484','Sinaloa');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('SON','MX','MEX','484','Sonora');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('TAB','MX','MEX','484','Tabasco');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('TAM','MX','MEX','484','Tamaulipas');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('TLA','MX','MEX','484','Tlaxcala');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('VER','MX','MEX','484','Veracruz');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('UC','MX','MEX','484','Yucatán');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('ZAC','MX','MEX','484','Zacatecas');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('AL','US','USA','840','Alabama');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('AK','US','USA','840','Alaska');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('AZ','US','USA','840','Arizona');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('AR','US','USA','840','Arkansas');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('CA','US','USA','840','California');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('NC','US','USA','840','Carolina del Norte');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('SC','US','USA','840','Carolina del Sur');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('CO','US','USA','840','Colorado');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('CT','US','USA','840','Connecticut');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('ND','US','USA','840','Dakota del Norte');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('SD','US','USA','840','Dakota del Sur');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('DE','US','USA','840','Delaware');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('FL','US','USA','840','Florida');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('GA','US','USA','840','Georgia');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('HI','US','USA','840','Hawái');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('ID','US','USA','840','Idaho');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('IL','US','USA','840','Illinois');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('IN','US','USA','840','Indiana');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('IA','US','USA','840','Iowa');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('KS','US','USA','840','Kansas');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('KY','US','USA','840','Kentucky');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('LA','US','USA','840','Luisiana');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('ME','US','USA','840','Maine');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('MD','US','USA','840','Maryland');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('MA','US','USA','840','Massachusetts');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('MI','US','USA','840','Míchigan');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('MN','US','USA','840','Minnesota');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('MS','US','USA','840','Misisipi');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('MO','US','USA','840','Misuri');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('MT','US','USA','840','Montana');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('NE','US','USA','840','Nebraska');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('NV','US','USA','840','Nevada');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('NJ','US','USA','840','Nueva Jersey');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('NY','US','USA','840','Nueva York');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('NH','US','USA','840','Nuevo Hampshire');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('NM','US','USA','840','Nuevo México');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('OH','US','USA','840','Ohio');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('OK','US','USA','840','Oklahoma');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('OR','US','USA','840','Oregón');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('PA','US','USA','840','Pensilvania');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('RI','US','USA','840','Rhode Island');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('TN','US','USA','840','Tennessee');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('TX','US','USA','840','Texas');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('UT','US','USA','840','Utah');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('VT','US','USA','840','Vermont');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('VA','US','USA','840','Virginia');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('WV','US','USA','840','Virginia Occidental');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('WA','US','USA','840','Washington');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('WI','US','USA','840','Wisconsin');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('WY','US','USA','840','Wyoming');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('ON','CA','CAN','124','Ontario ');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('QC','CA','CAN','124','Quebec ');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('NS','CA','CAN','124','Nueva Escocia');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('NB','CA','CAN','124','Nuevo Brunswick ');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('MB','CA','CAN','124','Manitoba');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('BC','CA','CAN','124','Columbia Británica');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('PE','CA','CAN','124','Isla del Príncipe Eduardo');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('SK','CA','CAN','124','Saskatchewan');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('AB','CA','CAN','124','Alberta');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('NL','CA','CAN','124','Terranova y Labrador');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('NT','CA','CAN','124','Territorios del Noroeste');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('YT','CA','CAN','124','Yukón');
INSERT INTO tbl_sat_estados(Codestado, Codpais2, Codpais3, Codpaisnum, Nombre)
VALUES('UN','CA','CAN','124','Nunavut');

--@FIN_BLOQUE
--////////////////////////////////////////////////////////////////////////////////////////////////////////////////
--//////////////////////////////////////////////// ACTUALIZACION DE LA VERSION ///////////////////////////////////
--///////////////////////////////////////////////////////////////////////////////////////////////////////////////

UPDATE TBL_VARIABLES
SET VEntero = 20, VDecimal = 4.0, VAlfanumerico = '4.0.20'
WHERE ID_Variable = 'VERSION';
   
--@FIN_BLOQUE
