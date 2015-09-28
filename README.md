# Forseti
Forseti es un ERP gratuito para PyMES Mexicanas. Es una aplicación WEB que utiliza Tomcat como servidor y PostgreSQL como 
base de datos. El proyecto se basa en dos interfaces las cuales sirven para diferentes propósitos:<br><br>
El Sistema Administrativo Forseti "<strong>SAF</strong>" es la interfaz web que te sirve para administrar el servidor 
Forseti instalado. El SAF permite dar de alta empresas en el servidor, que posteriormente serán controladas desde el CEF. 
Desde el SAF, se gestionan los siguientes recursos:<br><br><strong>ADMINISTRACION</strong><br><br>
<strong>Servidor y empresas</strong>:<br><ul> <li>Configuración de propiedades del servidor</li> 
<li>Actualización del servidor</li> <li>Agregar, cambiar o eliminar empresas</li> <li>Respaldo y restauración de todo 
el servidor o de empresas individuales</li> <li>Desarrollo de reportes y gráficas para el mismo SAF o para empresas 
del CEF<br></li></ul><strong>Documentación de Ayuda:<br></strong><ul> <li>Gestión de menus</li> <li>Gestión de submenus</li>
<li>Y gestión de páginas de ayuda</li></ul><strong>Control SSL:<br></strong><ul> <li>Creación y consultas de certificados
digitales auto-firmados</li> <li>Instalación de certificados para tomcat</li> <li>Configuración de certificados de 
confianza para la conexión de un cliente al servidor JPacConn de este servidor Forseti</li></ul>
<strong>Administracion de Roles (Usuarios):<br></strong><ul> <li>Agregar, cambiar y eliminar roles</li> 
<li>Configurar permisos de acceso a módulos individuales</li> <li>Enrolamientos a roles administrativos y otro roles de 
usuario </li> <li>Enlaces a reportes y gráficas permitadas<br></li></ul><strong><br>REGISROS</strong><br><br>
<strong>Inicios de sesión:<br></strong><ul> <li>Consulta y gestión de inicios de sesión en el SAF y CEF</li> </ul>
<strong>Registro de procesos:</strong><ul> <li>Consulta y gestión de procesos del SAF y CEF</li></ul><strong>
Registros Administrativos:<br></strong><ul> <li>De actualizaciones al servidor</li> <li>De creación y eliminación de 
bases de datos (empresas)</li> <li>De creacion de respaldos completos e individuales</li> <li>De restauración de 
respaldos de bases de datos</li></ul>
<br>
Físicamente, un servidor Forseti controla distintos objetos de base de 
datos. Lógicamente, cada objeto de base de datos es una empresa dada de 
alta en el servidor forseti por medio del SAF. <br>
<br>
El Centro Empresarial Forseti&nbsp; "<strong>CEF</strong>" es una aplicación web que te
 sirve para controlar una empresa instalada en un servidor Forseti. 
Desde el CEF, se gestionan los siguientes recursos:<br>
<br>
<strong>MÓDULOS</strong><br>
<br>
<strong>Contabilidad</strong>:<br>
<ul>
 <li>Catálogo de cuentas<br>
</li>
 <li>Rubros contables<br>
</li>
 <li>Clasificación de pólizas<br>
</li>
 <li>Enlaces contables</li>
 <li>Pólizas<br>
</li>
 <li>Póliza de cierre anual<br>
</li>
</ul>
<strong>Catálogos:<br>
</strong>
<ul>
 <li>Manejo de lineas y unidades<br>
</li>
 <li>Productos</li>
 <li>Servicios</li>
 <li>Gastos<br>
</li>
</ul>
<strong>Caja y Bancos:<br>
</strong>
<ul>
 <li>Movimientos bancarios<br>
</li>
 <li>Movimientos a las cajas<br>
</li>
 <li>Vales provisionales de caja</li>
 <li>Cierres de caja<br>
</li>
</ul>
<strong>Almacén<strong>:<br>
</strong>
</strong>
<ul>
 <li>Entradas y salidas de almacén</li>
 <li>Plantillas para supervisión de movimientos al almacén<br>
</li>
 <li>Traspasos de material entre almacenes</li>
 <li>Requerimientos de material entre almacenes</li>
 <li>Gestión de chequeos físicos<br>
</li>
</ul>
<strong>Compras<br>
</strong>
<ul>
 <li>Catálogo de proveedores</li>
 <li>Cuentas por pagar</li>
 <li>Órdenes de compras</li>
 <li>Recepción de materiales</li>
 <li>Facturas de materias primas</li>
 <li>Devoluciones y rebajas sobre compras</li>
 <li>Gestión de Gastos</li>
</ul>
<strong>Ventas<br>
</strong>
<ul>
 <li>Catálogo de clientes</li>
 <li>Cuentas por cobrar</li>
 <li>Cotizaciones</li>
 <li>Pedidos</li>
 <li>Facturas</li>
 <li>Devoluciones y rebajas sobre ventas</li>
 <li>Políticas de venta</li>
</ul>
<strong>Producción<br>
</strong>
<ul>
 <li>Formulaciones de productos terminados</li>
 <li>Ordenes de producción</li>
</ul>
<strong>Nómina<br>
</strong>
<ul>
 <li>
<strong>Catálogos<br>
</strong>
<ul>
 <li>Movimientos de percepciones y deducciones</li>
 <li>Departamentos</li>
 <li>Turnos</li>
 <li>Categorías de sueldos (automatización de los aumentos)</li>
 <li>Empleados</li>
</ul>
</li>
 <li>
<strong>Tablas<br>
</strong>
<ul>
 <li>ISR</li>
 <li>IMSS</li>
 <li>Crédito al Salario</li>
 <li>Aguinaldo</li>
 <li>Vacaciones</li>
</ul>
</li>
 <li>
<strong>Datos<br>
</strong>
<ul>
 <li>Control de asistencias</li>
 <li>Control de permisos como faltas justificadas, horas extras, etc.</li>
 <li>Plantillas para percepciones o deducciones especiales</li>
 <li>Control de créditos Fonacot</li>
</ul>
</li>
 <li>
<strong>Procesos<br>
</strong>
<ul>
 <li>Cierre diario para control estricto previo al cálculo de nómina<br>
</li>
 <li>Cálculo y otros procesos de nómina</li>
</ul>
</li>
</ul>
<strong>CENTRO DE CONTROL</strong><br>
<br>
<strong>Saldos:<br>
</strong>
<ul>
 <li>Control de saldos iniciales de contabilidad, cajas, bancos, inventario, clientes y proveedores<br>
</li>
 <li>Actualización en contabilidad, cajas, bancos, inventario, clientes y
 proveedores, de saldos, existencias y otros tipos, en caso de 
desajustarse. <br>
</li>
</ul>
<strong>
<strong>Administración de Roles (Usuarios):<br>
</strong>
</strong>
<ul>
 <li>Agregar, cambiar y eliminar roles</li>
 <li>Configurar permisos de acceso a módulos individuales</li>
 <li>Enlaces a las entidades permitidas.<br>
</li>
 <li>Enrolamientos a roles administrativos y otro roles de usuario </li>
 <li>Enlaces a reportes y gráficas permitadas</li>
</ul>
<strong>Altas, Cambios, Propiedades y Configuración de las siguientes Entidades:<br>
</strong>
<ul>
 <li>Cajas</li>
 <li>Cuentas Bancarias</li>
 <li>Bodegas o almacenes<br>
</li>
 <li>Puntos de compra</li>
 <li>Puntos de venta</li>
 <li>Puntos de producción</li>
 <li>Nóminas</li>
 <li>Áreas de trabajo de CRM (En desarrollo)<br>
</li>
</ul>
<strong>Vendedores:</strong>
<ul>
 <li>Gestión de vendedores de la empresa</li>
</ul>
<strong>CFDI y CE<strong>:</strong>
</strong>
<ul>
 <li>Parámetros del emisor de Comprobantes Fiscales Digitales por Internet<br>
</li>
 <li>Gestión de certificados</li>
 <li>Gestión de entidades de expedición de CFDI</li>
 <li>Gestión de Contabilidad Electrónica<br>
</li>
</ul>
<strong>Períodos:<br>
</strong>
<ul>
 <li>Control de los períodos de trabajos de la empresa</li>
</ul>
<strong>Monedas</strong><br>
<ul>
 <li>Control de las monedas soportadas en la empresa</li>
 <li>Gestión de los tipos de cambio</li>
</ul>
<strong>Variables (propiedades globales) de la empresa para:<br>
</strong>
<ul>
 <li>Módulo de Contabilidad</li>
 <li>Módulos de Caja y Bancos</li>
 <li>Módulo de Almacén</li>
 <li>Módulo de Compras</li>
 <li>Módulo de Ventas</li>
 <li>Módulo de Producción</li>
 <li>Módulo de Nómina</li>
 <li>Gestión del Sistema para esta empresa</li>
 <li>Definidas por el Usuario (para uso con reportes especiales)</li>
</ul>
<strong>Formatos:<br>
</strong>
<ul>
 <li>Gestión de los formatos de impresión para los distintos documentos 
(Pólizas, Cheques, Salidas de almacén, Facturas, Recibos, etc.)</li>
</ul>
