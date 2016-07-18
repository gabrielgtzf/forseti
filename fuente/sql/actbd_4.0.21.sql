ALTER TABLE tbl_sat_paises
  OWNER TO [[owner]];

--@FIN_BLOQUE
ALTER TABLE tbl_sat_estados
  OWNER TO [[owner]];

--@FIN_BLOQUE

--Actualizacion de usuarios vs permisos nuevos
insert into tbl_usuarios_permisos
select u.id_usuario, upc.id_permiso, '0'
from tbl_usuarios_permisos_catalogo upc, tbl_usuarios u 
where upc.id_permiso not in ( select tp.id_permiso from tbl_usuarios_permisos tp where tp.id_usuario = u.id_usuario )
order by u.id_usuario, upc.id_permiso;

--@FIN_BLOQUE


/*
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

-------------------------- PREVIO A ESTA ACTUALIZACION --------------------------------------------------------

*.- Establecer boton de carga de asistencias ya que es idéntico al de agregar asistencia. Ademas, verificar que tenga permiso exclusivo este proceso, independiente al de agregar asistencia
*.- Checar el calculo de Iva y Ieps pendiente en devolucion de anticipos, liquidaciones, etc, cuando la compra o factura involucra retenciones... Probablemente no haga bien el cálculo... Hay que hacer pruebas
*.- Checar cancelación de recepciones, remisiones, pedidos, etc., cuando fueron generados a través de un documento anterior (por ejemplo, una recepción generada por una orden de compra), al cancelar la recepción no se cancela la orden por lo menos cuando la orden es de un mes cerrado. Hay que hacer pruebas en todos estos ámbitos.  Ya que haya ordenes, remisiones y cotizaciones, checar sus reportes. (No se checaron al momento del desarrollo portque no existian registros, solo se checó su sintaxis las cuales pasaron las pruebas)
*.- Rechazar alta o cambios en empleados de nómina cuando la fecha de ingreso sea 29 de febrero (año biciesto) porque falla en calculo de vacaciones
*.- Reparar modulo de plantillas, eliminar panel inferior de tiempo porque sale sobrando, y reparar panel izquierdo. Nomina y Tipo de este panel están 
intercambiados, ademas Nómina debería mostrar la Entidad ya que a esto se refiere. Actualizar ayuda tambien
*.- Agregar desarrollo de proceso del PTU al módulo de empleados.
*.- Agregar esta ayuda de ptu en empleados y actualizar ayuda de reportes y gráficas
*.- Actualizar función _p(String str)... Al agregar una cadena con & en la base de datos tiene que registrarse como &amp; porque de lo contrario falla en al render del pdf. Verificar si al hacer el sellado el & si lo manda como &amp; porque de ser asi, pudiera corregirse lo del pdf y amolarse al sellar o timbrar.
*.- Actualizar ayuda en clientes y facturas, cotizacines, remisiones y pedidos. El Metodo de pago conforme al catálogo del sat 
*.- Actualizar ayuda de CFDI y CE para Domicilios Fiscales y Expedidos En, conforme a base de datos de paises, estados, etc.
*.- Actualizar ayuda de Clientes y Proveedores (Por cambios en metodos de pago y complemento de comercio exterior)
*.- Eliminar posibilidad de varias lineas en campos de observación de compras, ventas, etc.. Al consultar el registro, cuando en sus observaciones tiene mas de una linea, Falla la carga. 

-------------------------- AL GENERAR ESTA ACTUALIZACION PARA DISTRIBUCION -----------------------------------------------------

ACT.- Agregar a la actualización el archivo cuentas_cont.csv sobre el directorio rec, archivo indice_reportes.html y estructura_reportes.html sobre bin/forseti_doc.
ACT.- ELIMINAR JMsj y HelloWorld antes de preparar la actualización
ACT.- Descomentar en JAdmBDDlg GENERAR_REPORTE / DOCUMENTACION y PROCESO donde indica que si es reporte de sistema no se puede cambiar if(repid < 10000) { .... 
ACT.- En AWS Ryrsa, copiar sp_cajas_cierrez antes de actualizar para despues poder reconstruirlo con OPT
ACT.- En .forseti_init reconstuir "insert into"s a TBL_CATALOGOS para que los catalogos queden configurados como se hicieron en las pruebas, es necesario agregarles la columna replong a cada insert into. 

-------------------------- EN PROXIMAS ACTUALIZACIONES ---------------------------

1.- Generar Web Services REST para JPacConn, JSmtpConn y JAwsS3Conn
2.- Leer QRCode desde archivo de imagen al cargarlo en compras y gastos para automatizar datos de CBB
3.- Corregir cabecero de movimientos de caja y bancos respecto a CE. En la poliza esta correcto pero el cabecero de mov presenta valores raros e inconsistentes
4.- Definir las unidades del sat a unidades de productos forseti y enlazarlas en codigo de inicio... Que queden por default al agregar una nueva empresa (Verificar proceso de agregado no virgen para que no se vayan a duplicar y marque un error).
5.- Checar diferencias de tablas, funciones, etc entre TAJA y TAJAB para verificar si están consistentes los desarrollos

-------------------------- OTRAS REVISIONES ----------------------------------------

1.- CHECAR CON VEDIA EL CALCULO DE Descuentos por inconsistencias en sp_calculo_nómina_faltas..... Aqui en forseti no aplicaba DIAS SIN PAGO DE TIEMPO. y se modificó.

*/


