<!--
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
-->
<%@ page import="forseti.*, forseti.sets.*, java.util.* " %>
<%
	String RGST = (String)request.getAttribute("RGST");
	if(RGST == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}
	
	String BLOQ = (String)request.getAttribute("BLOQ");
	boolean registrado = JUtil.yaRegistradoEnFsi(request, response);

	
	String mensaje = JUtil.getMensaje(request, response);
	String datos = JUtil.Msj("CEF","REGISTRO","SESION","DATOS");
	String empresa = JUtil.Msj("CEF","REGISTRO","SESION","EMPRESA");
	String usuario = JUtil.Msj("CEF","REGISTRO","SESION","USUARIO");
	String password = JUtil.Msj("CEF","REGISTRO","SESION","PASSWORD");
	String hola = JUtil.Msj("CEF","REGISTRO","OK","BIENVENIDO");
	String bienvenido = JUtil.Msj("CEF","REGISTRO","OK","BIENVENIDO", 2, 3);
	String actividades1 = JUtil.Msj("CEF","REGISTRO","OK","ACTIVIDADES");
	String actividades2 = JUtil.Msj("CEF","REGISTRO","OK","ACTIVIDADES", 2);
	String actividades3 = JUtil.Msj("CEF","REGISTRO","OK","ACTIVIDADES", 3);
	String actividades4 = JUtil.Msj("CEF","REGISTRO","OK","ACTIVIDADES", 4);
	String actividades5 = JUtil.Msj("CEF","REGISTRO","OK","ACTIVIDADES", 5);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<script language="JavaScript" type="text/javascript">
<!-- 
if(top.location == self.location) {
	top.location.href = "../forsetiweb/forseti.html"
}
<%
	if(registrado) 
	{
%>  
if(parent.folder.document.URL.indexOf('folder.jsp') == -1) {
	parent.folder.document.location.href = "../forsetiweb/folder.jsp"
}
if(parent.menu.document.URL.indexOf('menu.jsp') == -1) {
	parent.menu.document.location.href = "../forsetiweb/menu.jsp"
}
if(parent.barra.document.URL.indexOf('barra.jsp') == -1) {
	parent.barra.document.location.href = "../forsetiweb/barra.jsp"
}
<%
	}
	else  
	{
%>  
if(parent.folder.document.URL.indexOf('folder.html') == -1) {
	parent.folder.document.location.href = "../forsetiweb/folder.html"
}
if(parent.menu.document.URL.indexOf('menu.html') == -1) {
	parent.menu.document.location.href = "../forsetiweb/menu.html"
}
if(parent.barra.document.URL.indexOf('barra.html') == -1) {
	parent.barra.document.location.href = "../forsetiweb/barra.html"
}
<%
	}
%>
if(parent.tiempo.document.URL.indexOf('tiempo.html') == -1) {
	parent.tiempo.document.location.href = "../forsetiweb/tiempo.html"
}
if(parent.entidad.document.URL.indexOf('entidad.html') == -1) {
	parent.entidad.document.location.href = "../forsetiweb/entidad.html"
}
if(parent.ztatuz.document.URL.indexOf('status.html') == -1) {
	parent.ztatuz.document.location.href = "../forsetiweb/status.html"
}

function ventanaMSJ(idmensaje)
{
	var ventana;
	parametrs = "toolbar=0,location=0,directories=0,status=1,menubar=1,scrollbars=1,resizable=1,width=320,height=400";
	ventana = open('/servlet/forseti.mensajes.JMensajesDlg?proceso=LEER&MENID=' + idmensaje, 'ventMSJ', parametrs);
	ventana.focus();
}
-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="../compfsi/estilos.css" rel="stylesheet" type="text/css">
</head>

<body bgcolor="#333333" text="#000000" link="#0099FF" vlink="#FF0000" alink="#000099" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="titCuerpoBco" align="center" valign="middle" bgcolor="#0099FF"><%= JUtil.Msj("GLB","GLB","GLB","CEF") %></td>
  </tr>
  
<%

	out.println(mensaje);

	// Inicia con registrar el objeto de sesion si no esta registrado
	if(BLOQ.equals("false"))
	{
		if(!registrado) 
		{
%>  
  <tr>
    <td>
      <p>&nbsp;</p><form action="/servlet/CEFRegistro" method="post" name="registro" target="_self">
	  	<table width="300" border="0" align="center" cellpadding="0" cellspacing="0" bgcolor="#CCCCCC">
          <tr> 
            <td class="titChico" colspan="2" align="center" bgcolor="#0099FF">
              <%= datos %></td>
          </tr>
          <tr> 
            <td>&nbsp;</td>
            <td>&nbsp;</td>
          </tr>
          <tr> 
            <td align="right"><%= empresa %></td>
            <td align="center"> <input name="basededatos" type="text" id="basededatos" size="20" maxlength="255"<% if(request.getParameter("basededatos") != null) { out.println(" value=\"" + request.getParameter("basededatos") + "\""); } %>> 
            </td>
          </tr>
          <tr> 
            <td>&nbsp;</td>
            <td>&nbsp;</td>
          </tr>
          <tr> 
            <td align="right"><%= usuario %></td>
            <td align="center"> <input name="usuario" type="text" id="usuario" size="20" maxlength="30"<% if(request.getParameter("usuario") != null) {	out.println(" value=\"" + request.getParameter("usuario") + "\""); } %>> 
            </td>
          </tr>
          <tr> 
            <td>&nbsp;</td>
            <td align="center">&nbsp;</td>
          </tr>
          <tr> 
            <td align="right"><%= password %></td>
            <td align="center"> <input name="password" type="password" id="password" size="20" maxlength="30">  
            </td>
          </tr>
          <tr> 
            <td>&nbsp;</td>
            <td align="center">&nbsp;</td>
          </tr>
          <tr> 
            <td colspan="2" align="center">
				<input type="submit" name="submit" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
			</td>
          </tr>
          <tr> 
            <td>&nbsp;</td>
            <td align="center">&nbsp;</td>
          </tr>
        </table> 
	   </form> 
	</td>
  </tr>
<%
		}
		else // si ya esta registrado
		{
			Calendar ini = GregorianCalendar.getInstance();
          	ini.add(Calendar.MONTH, -1);
         	String donde;
			if(JUtil.getSesion(request).getID_Usuario().equals("cef-su"))
				donde = "FechaNota >= '" + JUtil.obtFechaSQL(ini) + "'";
			else
				donde = "FechaNota >= '" + JUtil.obtFechaSQL(ini) + "' AND ID_Block IN (select ID_Block from TBL_USUARIOS_SUBMODULO_BLOCKS where ID_Usuario ='" + JUtil.getSesion(request).getID_Usuario() + "')";
          
		  	JNotasSet not = new JNotasSet(request);
			not.m_Where = donde;
			not.m_OrderBy = "FechaNota DESC, ID_Nota ASC"; 
			not.Open();
			
%>
  <tr>
    <td class="titCuerpoBco" align="center" valign="middle">&nbsp;</td>
  </tr>
<%
			JAdmVariablesSet serv = new JAdmVariablesSet(null);
			serv.ConCat(true);
			serv.m_Where = "ID_Variable = 'VERSION'";
			serv.Open();
			JAdmVariablesSet bd = new JAdmVariablesSet(request);
			bd.m_Where = "ID_Variable = 'VERSION'";
			bd.Open();
			if(bd.getAbsRow(0).getVDecimal() != serv.getAbsRow(0).getVDecimal() || bd.getAbsRow(0).getVEntero() != serv.getAbsRow(0).getVEntero())
			{ 
%>	  
  <tr> 
    <td width="30" align="center" class="titGiganteAzc"> Alerta</td>
  </tr>
  <tr>
    <td class="titCuerpoBco" align="center" valign="middle">&nbsp;</td>
  </tr>  
  <tr> 
      <td>
	  <p class="txtCuerpoBco">Esta base de datos tiene una version diferente a la del servidor, esto puede deberse a 
	  alguna de las siguientes circunstancias:</p>
	  <ul class="txtChicoAzc">
        <li>Se acaba de restaurar la base de datos de un respaldo que tenia una version anterior a la actual en este servidor</li>
        <li>Se interrumpio inesperadamente la actualizacion del sistema, quedando desfasada del servidor</li>
		<li>En estos momentos se esta actualizando el servidor y no ha acabado el proceso</li>
        <li>Aunque muy rara vez pasa, se ha borrado o cambiado algun archivo o estructura de base de datos por algun proceso externo o alguna persona</li>
      </ul>
	  <p class="txtCuerpoBco">Aunque se puede continuar trabajando, no es recomendable en lo absoluto porque se pueden presentar errores
	  inesperados. Es indispensable avisar al administrador del sistema para que revise los archivos de registro sobre actualizaciones,
	  y arregle el problema de inmediato</p>
	    <table align="center" width="95%" border="0" cellspacing="0" cellpadding="2">
        <tr> 
		  <td bgcolor="#999999" class="txtChicoBco" align="center" width="50%">Version del Servidor</td>
          <td bgcolor="#999999" class="txtChicoBco" align="center">Version de la Empresa</td>
		</tr>
        <tr> 
		  <td bgcolor="#999999" class="txtChicoBco" align="center" width="50%"><%= serv.getAbsRow(0).getVAlfanumerico() %></td>
          <td bgcolor="#999999" class="txtChicoBco" align="center"><%= bd.getAbsRow(0).getVAlfanumerico() %></td>
		</tr>
		</table>
	  </td>
  </tr>
<%
			}
			else
			{
%>	  
  <tr> 
    <td width="30" align="center" class="titGiganteAzc"><%= hola %></td>
  </tr>
  <tr>
    <td class="titCuerpoBco" align="center" valign="middle">&nbsp;</td>
  </tr>  
  <tr> 
      <td>
	  <p class="txtCuerpoBco"><%= JUtil.getSesion(request).getNombreUsuario() + " " + bienvenido %></p>
	  <ul class="txtChicoAzc">
        <li><%= actividades1 %></li>
        <li><%= actividades2 %></li>
        <li><%= actividades3 %></li>
        <li><%= actividades4 %></li>
		<li><%= actividades5 %></li>
      </ul>
      </td>
  </tr>
<%
			}
			
			if(not.getNumRows() > 0)
			{
%>
  <tr>
    <td class="titCuerpoBco" align="center" valign="middle">&nbsp;</td>
  </tr>
  <tr>
    <td class="titCuerpoBco" align="center" valign="middle"> 
      <table width="95%" border="0" cellspacing="0" cellpadding="2">
        <tr> 
          <td colspan="6" align="center" bgcolor="#0099FF" class="titCuerpoBco">Notas 
            del &uacute;ltimo mes</td>
        </tr>
        <tr> 
		  <td bgcolor="#999999" class="txtChicoBco" align="left" width="5%">&nbsp;</td>
          <td bgcolor="#999999" class="txtChicoBco" align="center" width="15%">Fecha</td>
          <td bgcolor="#999999" class="txtChicoBco" align="left" width="45%">Nota</td>
		  <td bgcolor="#999999" class="txtChicoBco" align="left" width="10%">Entidad</td>
          <td bgcolor="#999999" class="txtChicoBco" align="left" width="20%">Usuario</td>
          <td bgcolor="#999999" class="txtChicoBco" align="center" width="5%">&nbsp;</td>
        </tr>
<%
				for(int i = 0; i < not.getNumRows(); i++)
				{
%>
		<tr> 
			<td class="cpoBco" width="5%" align="left" valign="top">
				<% for(short j= 0; j < not.getAbsRow(i).getID_Nivel(); j++) { out.print("&nbsp;&nbsp;&nbsp;"); } %>
		  		<img src="../../imgfsi/<%= (not.getAbsRow(i).getID_Nivel() == 0) ? "flecha.gif" : "flechare.gif" %>"></td>
            <td class="cpoBco" width="15%" align="center" valign="top"><%= JUtil.obtFechaTxt(not.getAbsRow(i).getFecha(), "dd/MMM/yyyy") + " " + JUtil.obtHoraTxt(not.getAbsRow(i).getHora(), "hh:mm") %></td>
          	<td class="cpoBco" width="45%" align="left" valign="top"><%= not.getAbsRow(i).getMensaje() %></td>
			<td class="cpoBco" width="10%" align="left" valign="top"><%= not.getAbsRow(i).getEtiqueta() %></td>
           	<td class="cpoBco" width="20%" align="left" valign="top"><%= not.getAbsRow(i).getNombre() %></td>
          	<td class="cpoBco" width="5%" align="center" valign="top"><a href="/servlet/forseti.mensajes.JNotasCtrl" target="_self">Citar</a></td>
	    </tr>
<%
				}
%>		
     </table>
	</td>
  </tr>  
<%
			}
			
		}
	}
	else
	{
%>
  <tr> 
     <td><p class="textoGris">&nbsp;</p>
      </td>
  </tr>
 <%	
	}
%>
</table>

</body>
</html>
