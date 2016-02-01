INSERT INTO TBL_VARIABLES
VALUES('URLAYUDA','La URL relativa o absoluta para la búsqueda de la ayuda de forseti ( documentación )',null,null,null,'http://www.forseti.org.mx/forsetidoc/',1);

--@FIN_BLOQUE
INSERT INTO tbl_catalogos(id_catalogo, nombre, select_clause, pridefault, secdefault, seguridad,aplrep)
VALUES (36,'UTENSILIOS',' * FROM view_catalog_gastos_utensilios ',' select Clave, Descripcion from view_catalog_gastos_utensilios Order By Clave ASC limit 1 ',' select Clave, Descripcion from view_catalog_gastos_utensilios Order By Clave DESC limit 1 ','','1');

--@FIN_BLOQUE
