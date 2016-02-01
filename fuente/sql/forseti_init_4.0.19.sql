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
  compania character varying(255) NOT NULL,
  direccion character varying(255) NOT NULL,
  poblacion character varying(50) NOT NULL,
  cp character varying(9) NOT NULL,
  mail character varying(255) NOT NULL,
  web character varying(255) NOT NULL,
  su character varying(10) NOT NULL,
  rfc character varying(15) NOT NULL DEFAULT ''::character varying,
  cfd smallint NOT NULL DEFAULT 0::smallint,
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
  CONSTRAINT pk_tbl_catalogos PRIMARY KEY (id_catalogo)
);

INSERT INTO TBL_CATALOGOS(ID_Catalogo, Nombre, Select_Clause, PriDefault, SecDefault, Seguridad, AplRep )
VALUES(1,'USUARIOS',' * from view_catalog_usuarios ',' select Clave, Descripcion from view_catalog_usuarios Order By Clave ASC limit 1 ',' select Clave, Descripcion from view_catalog_usuarios Order By Clave DESC limit 1 ','','1');
INSERT INTO TBL_CATALOGOS(ID_Catalogo, Nombre, Select_Clause, PriDefault, SecDefault, Seguridad, AplRep )
VALUES(2,'FORMATOS',' * from view_catalog_formatos ',' select Clave, Descripcion from view_catalog_formatos Order By Clave ASC limit 1 ',' select Clave, Descripcion from view_catalog_formatos Order By Clave DESC limit 1 ','','1');
INSERT INTO TBL_CATALOGOS(ID_Catalogo, Nombre, Select_Clause, PriDefault, SecDefault, Seguridad, AplRep )
VALUES(3,'CUENTAS CONTABLES',' * from view_catalog_cuentascont ',' select Clave, Descripcion from view_catalog_cuentascont Order By Clave ASC limit 1 ',' select Clave, Descripcion from view_catalog_cuentascont Order By Clave DESC limit 1 ','','1');
INSERT INTO TBL_CATALOGOS(ID_Catalogo, Nombre, Select_Clause, PriDefault, SecDefault, Seguridad, AplRep )
VALUES(4,'LINEAS DE PRODUCTOS',' * FROM view_catalog_prod_lineas ',' select Clave, Descripcion from view_catalog_prod_lineas Order By Clave ASC limit 1 ',' select Clave, Descripcion from view_catalog_prod_lineas Order By Clave DESC limit 1 ','','1');
INSERT INTO TBL_CATALOGOS(ID_Catalogo, Nombre, Select_Clause, PriDefault, SecDefault, Seguridad, AplRep )
VALUES(5,'LINEAS DE SERVICIOS',' * FROM view_catalog_prodserv_lineas ',' select Clave, Descripcion from view_catalog_prodserv_lineas Order By Clave ASC limit 1 ',' select Clave, Descripcion from view_catalog_prodserv_lineas Order By Clave DESC limit 1 ','','1');
INSERT INTO TBL_CATALOGOS(ID_Catalogo, Nombre, Select_Clause, PriDefault, SecDefault, Seguridad, AplRep )
VALUES(6,'LINEAS DE GASTOS',' * FROM view_catalog_gastos_lineas ',' select Clave, Descripcion from view_catalog_gastos_lineas Order By Clave ASC limit 1 ',' select Clave, Descripcion from view_catalog_gastos_lineas Order By Clave DESC limit 1 ','','1');
INSERT INTO TBL_CATALOGOS(ID_Catalogo, Nombre, Select_Clause, PriDefault, SecDefault, Seguridad, AplRep )
VALUES(7,'ENTIDADES DE COMPRAS',' * FROM view_catalog_entidades_compras ',' select Clave, Descripcion from view_catalog_entidades_compras Order By Clave ASC limit 1 ','','COMPRAS','1');
INSERT INTO TBL_CATALOGOS(ID_Catalogo, Nombre, Select_Clause, PriDefault, SecDefault, Seguridad, AplRep )
VALUES(8,'ENTIDADES DE VENTAS',' * FROM view_catalog_entidades_ventas ',' select Clave, Descripcion from view_catalog_entidades_ventas Order By Clave ASC limit 1 ','','VENTAS','1');
INSERT INTO TBL_CATALOGOS(ID_Catalogo, Nombre, Select_Clause, PriDefault, SecDefault, Seguridad, AplRep )
VALUES(9,'CONCEPTOS DE CUENTAS POR PAGAR',' * FROM view_catalog_provee_cxp_conceptos ',' select Clave, Descripcion from view_catalog_cxp_conceptos Order By Clave ASC limit 1 ',' select Clave, Descripcion from view_catalog_cxp_conceptos Order By Clave DESC limit 1 ','','1');
INSERT INTO TBL_CATALOGOS(ID_Catalogo, Nombre, Select_Clause, PriDefault, SecDefault, Seguridad, AplRep )
VALUES(10,'CONCEPTOS DE CUENTAS POR COBRAR',' * FROM view_catalog_client_cxc_conceptos ',' select Clave, Descripcion from view_catalog_cxc_conceptos Order By Clave ASC limit 1 ',' select Clave, Descripcion from view_catalog_cxc_conceptos Order By Clave DESC limit 1 ','','1');
INSERT INTO TBL_CATALOGOS(ID_Catalogo, Nombre, Select_Clause, PriDefault, SecDefault, Seguridad, AplRep )
VALUES(11,'PRODUCTOS',' * FROM view_catalog_prod2 ','','','','0');
INSERT INTO TBL_CATALOGOS(ID_Catalogo, Nombre, Select_Clause, PriDefault, SecDefault, Seguridad, AplRep )
VALUES(12,'PROVEEDORES',' Clave, Descripcion, Especial FROM view_catalog_provee ','','','','0');
INSERT INTO TBL_CATALOGOS(ID_Catalogo, Nombre, Select_Clause, PriDefault, SecDefault, Seguridad, AplRep )
VALUES(13,'PRODUCTOS',' Clave, Descripcion, Especial FROM view_catalog_prod4 ','','','','0');
INSERT INTO TBL_CATALOGOS(ID_Catalogo, Nombre, Select_Clause, PriDefault, SecDefault, Seguridad, AplRep )
VALUES(14,'CLIENTES',' Clave, Descripcion, Especial FROM view_catalog_client ','','','','0');
INSERT INTO TBL_CATALOGOS(ID_Catalogo, Nombre, Select_Clause, PriDefault, SecDefault, Seguridad, AplRep )
VALUES(15,'GASTOS',' * FROM view_catalog_gastos ',' select Clave, Descripcion from view_catalog_gastos Order By Clave ASC limit 1 ',' select Clave, Descripcion from view_catalog_gastos Order By Clave DESC limit 1 ','','1');
INSERT INTO TBL_CATALOGOS(ID_Catalogo, Nombre, Select_Clause, PriDefault, SecDefault, Seguridad, AplRep )
VALUES(16,'PROVEEDORES',' Clave, Descripcion, Especial FROM view_catalog_provee ',' select Clave, Descripcion from view_catalog_provee Order By Clave ASC limit 1 ',' select Clave, Descripcion from view_catalog_provee Order By Clave DESC limit 1 ','','1');
INSERT INTO TBL_CATALOGOS(ID_Catalogo, Nombre, Select_Clause, PriDefault, SecDefault, Seguridad, AplRep )
VALUES(17,'CONCEPTOS DE ENTRADAS AL ALMACEN',' * FROM view_catalog_prod_costos_conceptos_ent ',' select Clave, Descripcion from view_catalog_prod_costos_conceptos_ent Order By Clave ASC limit 1 ',' select Clave, Descripcion from view_catalog_prod_costos_conceptos_ent Order By Clave DESC limit 1 ','','1');
INSERT INTO TBL_CATALOGOS(ID_Catalogo, Nombre, Select_Clause, PriDefault, SecDefault, Seguridad, AplRep )
VALUES(18,'CONCEPTOS DE SALIDAS DEL ALMACEN',' * FROM view_catalog_prod_costos_conceptos_sal ',' select Clave, Descripcion from view_catalog_prod_costos_conceptos_sal Order By Clave ASC limit 1 ',' select Clave, Descripcion from view_catalog_prod_costos_conceptos_sal Order By Clave DESC limit 1 ','','1');
INSERT INTO TBL_CATALOGOS(ID_Catalogo, Nombre, Select_Clause, PriDefault, SecDefault, Seguridad, AplRep )
VALUES(19,'PRODUCTOS',' * FROM view_catalog_prod ',' select Clave, Descripcion from view_catalog_prod Order By Clave ASC limit 1 ',' select Clave, Descripcion from view_catalog_prod Order By Clave DESC limit 1 ','','1');
INSERT INTO TBL_CATALOGOS(ID_Catalogo, Nombre, Select_Clause, PriDefault, SecDefault, Seguridad, AplRep )
VALUES(20,'BODEGAS',' * FROM view_catalog_bodegas ',' select Clave, Descripcion from view_catalog_bodegas Order By Clave ASC limit 1 ','','BODEGAS','1');
INSERT INTO TBL_CATALOGOS(ID_Catalogo, Nombre, Select_Clause, PriDefault, SecDefault, Seguridad, AplRep )
VALUES(21,'PRODUCTOS',' * FROM view_catalog_prod3 ','','','','0');
INSERT INTO TBL_CATALOGOS(ID_Catalogo, Nombre, Select_Clause, PriDefault, SecDefault, Seguridad, AplRep )
VALUES(22,'FORMULAS',' * FROM view_catalog_formulas ',' select Clave, Descripcion from view_catalog_formulas Order By Clave ASC limit 1 ',' select Clave, Descripcion from view_catalog_formulas Order By Clave DESC limit 1 ','','1');
INSERT INTO TBL_CATALOGOS(ID_Catalogo, Nombre, Select_Clause, PriDefault, SecDefault, Seguridad, AplRep )
VALUES(23,'VENDEDORES',' * FROM view_catalog_vendedores ',' select Clave, Descripcion from view_catalog_vendedores Order By Clave ASC limit 1 ','','VENDEDORES','1');
INSERT INTO TBL_CATALOGOS(ID_Catalogo, Nombre, Select_Clause, PriDefault, SecDefault, Seguridad, AplRep )
VALUES(24,'CUENTAS BANCARIAS',' * from view_catalog_bancos ',' select Clave, Descripcion from view_catalog_bancos Order By Clave ASC limit 1 ','','BANCOS','1');
INSERT INTO TBL_CATALOGOS(ID_Catalogo, Nombre, Select_Clause, PriDefault, SecDefault, Seguridad, AplRep )
VALUES(25,'CAJAS',' * from view_catalog_cajas ',' select Clave, Descripcion from view_catalog_cajas Order By Clave ASC limit 1 ','','CAJAS','1');
INSERT INTO TBL_CATALOGOS(ID_Catalogo, Nombre, Select_Clause, PriDefault, SecDefault, Seguridad, AplRep )
VALUES(26,'DEPARTAMENTOS',' * from view_catalog_nomdep ',' select Clave, Descripcion from view_catalog_nomdep Order By Clave ASC limit 1 ',' select Clave, Descripcion from view_catalog_nomdep Order By Clave DESC limit 1 ','','1');
INSERT INTO TBL_CATALOGOS(ID_Catalogo, Nombre, Select_Clause, PriDefault, SecDefault, Seguridad, AplRep )
VALUES(27,'MOVIMIENTOS',' * from view_catalog_movimientos ',' select Clave, Descripcion from view_catalog_movimientos Order By Clave ASC limit 1 ',' select Clave, Descripcion from view_catalog_movimientos Order By Clave DESC limit 1 ','','1');
INSERT INTO TBL_CATALOGOS(ID_Catalogo, Nombre, Select_Clause, PriDefault, SecDefault, Seguridad, AplRep )
VALUES(28,'EMPLEADOS',' Clave, Descripcion, Especial from view_catalog_empleados ','','','','0');
INSERT INTO TBL_CATALOGOS(ID_Catalogo, Nombre, Select_Clause, PriDefault, SecDefault, Seguridad, AplRep )
VALUES(29,'CONCEPTOS DE NOMINA',' * from view_catalog_nommov ',' select Clave, Descripcion from view_catalog_nommov Order By Clave ASC limit 1 ',' select Clave, Descripcion from view_catalog_nommov Order By Clave DESC limit 1 ','','1');
INSERT INTO TBL_CATALOGOS(ID_Catalogo, Nombre, Select_Clause, PriDefault, SecDefault, Seguridad, AplRep )
VALUES(30,'CONCEPTOS DE ENTRADAS Y SALIDAS DE ALMACEN',' * FROM view_catalog_prod_costos_conceptos_entsal ',' select Clave, Descripcion from view_catalog_prod_costos_conceptos_entsal Order By Clave ASC limit 1 ',' select Clave, Descripcion from view_catalog_prod_costos_conceptos_entsal Order By Clave DESC limit 1 ','','1');
INSERT INTO TBL_CATALOGOS(ID_Catalogo, Nombre, Select_Clause, PriDefault, SecDefault, Seguridad, AplRep )
VALUES(31,'BODEGA DE UTENSILIOS',' * FROM view_catalog_boduten ',' select Clave, Descripcion from view_catalog_boduten Order By Clave ASC limit 1 ','','BODEGAS','1');
INSERT INTO TBL_CATALOGOS(ID_Catalogo, Nombre, Select_Clause, PriDefault, SecDefault, Seguridad, AplRep )
VALUES(32,'CATALOGO DE ALMACENES',' * FROM view_catalog_almacenes ',' select Clave, Descripcion from view_catalog_almacenes Order By Clave ASC limit 1 ','','BODEGAS','1');
INSERT INTO TBL_CATALOGOS(ID_Catalogo, Nombre, Select_Clause, PriDefault, SecDefault, Seguridad, AplRep )
VALUES(33,'LINEAS GENERALES',' * FROM view_catalog_lineas ',' select Clave, Descripcion from view_catalog_lineas Order By Clave ASC limit 1 ',' select Clave, Descripcion from view_catalog_lineas Order By Clave DESC limit 1 ','','1');
INSERT INTO TBL_CATALOGOS(ID_Catalogo, Nombre, Select_Clause, PriDefault, SecDefault, Seguridad, AplRep )
VALUES(34,'COMPAÑIAS',' Clave, Descripcion, Especial FROM view_catalog_companias ','','','','0');
INSERT INTO TBL_CATALOGOS(ID_Catalogo, Nombre, Select_Clause, PriDefault, SecDefault, Seguridad, AplRep )
VALUES(35,'PERSONAS',' Clave, Descripcion, Especial FROM view_catalog_personas ','','','','0');
INSERT INTO tbl_catalogos(id_catalogo, nombre, select_clause, pridefault, secdefault, seguridad,aplrep)
VALUES (36,'UTENSILIOS',' * FROM view_catalog_gastos_utensilios ',' select Clave, Descripcion from view_catalog_gastos_utensilios Order By Clave ASC limit 1 ',' select Clave, Descripcion from view_catalog_gastos_utensilios Order By Clave DESC limit 1 ','','1');


--@FIN_BLOQUE
CREATE OR REPLACE VIEW view_catalogos AS 
 SELECT c.id_catalogo, m.msj1 AS nombre, c.select_clause, c.pridefault, c.secdefault, c.seguridad, c.aplrep
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
values('ALM_PLANTILLAS','view_invserv_plantillas_impcab','view_invserv_plantillas_impdet');
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

--////////////////////////////////////////////////////////////////////////////////////////////////////////////////
--//////////////////////////////////////////////// ACTUALIZACION DE LA VERSION ///////////////////////////////////
--///////////////////////////////////////////////////////////////////////////////////////////////////////////////

UPDATE TBL_VARIABLES
SET VEntero = 19, VDecimal = 4.0, VAlfanumerico = '4.0.19'
WHERE ID_Variable = 'VERSION';
   
--@FIN_BLOQUE
