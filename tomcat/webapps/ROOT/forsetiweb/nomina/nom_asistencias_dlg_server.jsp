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
<%@ page import="forseti.*, forseti.sets.*, java.util.*, java.io.*" %>
<%
	String nom_asistencias_dlg = (String)request.getAttribute("nom_asistencias_dlg");
	if(nom_asistencias_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}

	String titulo = "ASISTENCIAS: " + (( request.getParameter("proceso") != null ) ? request.getParameter("proceso") : "" );
	
	JAsistenciasChequeosSet set = new JAsistenciasChequeosSet(request);
	if( request.getParameter("proceso").equals("CAMBIAR") )
	{
		set.m_Where = "ID_Empleado = '" + JUtil.p(JUtil.obtSubCadena(request.getParameter("id"),"_FE_","|")) + "' and ID_Fecha = '" +
			 JUtil.p(JUtil.obtSubCadena(request.getParameter("id"),"_FF_","|")) + "'";
		
		System.out.println(set.m_Where);
		set.Open();
	}
%>
<html>
<head>
<title>Forseti - <%= titulo %></title>
<script language="JavaScript" type="text/javascript" src="../../forsetiweb/comps.js" >
</script>
<script language="JavaScript" type="text/javascript" src="../../forsetiweb/datetimepicker.js " >
</script>
<script language="JavaScript" type="text/javascript">
<!--
function enviarlo(formAct)
{
	return true;
}
-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="../../forsetiweb/estilos.css" rel="stylesheet" type="text/css"></head>

<body bgcolor="#999999" text="#000000" link="#FF6600" vlink="#FF0000" alink="#000099" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td class="titCuerpoBco" align="center" valign="middle" bgcolor="#FF6600"><%= titulo %></td>
  </tr>
  <%	
	String mensaje = JUtil.getMensaje(request, response);	
	out.println(mensaje);
	//out.print(JUtil.depurarParametros(request));
%>
  <tr> 
    <td> 
	  <form onSubmit="return enviarlo(this)" action="/servlet/forseti.nomina.JNomAsistenciasDlg" method="post" enctype="application/x-www-form-urlencoded" name="nom_asistencias_dlg" target="_self">
        <table width="100%" border="0" cellspacing="3" cellpadding="0">
          <tr> 
            <td width="20%" valign="middle"> <div align="right"> 
                <input name="proceso" type="hidden" value="<%= request.getParameter("proceso")%>">
                <input name="subproceso" type="hidden" value="ENVIAR">
                Clave: </div></td>
            <td colspan="2" valign="middle"> <input name="id_empleado" type="text" id="id_empleado" size="10" maxlength="6"> 
              &nbsp; <input name="submit" type="image" onClick="" src="../../forsetiweb/Aceptar.gif" align="middle" border="0"> 
            </td>
          </tr>
          <tr> 
            <td colspan="3" align="center"> 
              <%
	if(JUtil.getID_Mensaje(request, response) < 0)
	{	
%>
		<table width="100%" border="0" cellspacing="0" cellpadding="2">
          <tr> 
                  <td align="center" class="titCuerpoBco">ESPERANDO CAPTURA</td>
          </tr>
		</table>
<%
	}
	else
	{
		if(JUtil.getID_Mensaje(request, response) == 0)
		{	
			
%>
			<table width="100%" border="0" cellspacing="0" cellpadding="2">
                <tr> 
                  <td width="25%" class="cpoColNg">Clave</td>
                  <td colspan="2" class="cpoColNg">Nombre</td>
                </tr>
                <tr> 
                  <td class="cpoBco"><%= (String)request.getAttribute("ID_Empleado") %></td>
                  <td colspan="2" class="cpoBco"><%= (String)request.getAttribute("Nombre") %></td>
                </tr>
                <tr> 
                  <td colspan="2" align="center" class="cpoColNg">ENTRADA</td>
                  <td width="50%" align="center" class="cpoColNg">SALIDA</td>
                </tr>
                <tr> 
                  <td colspan="2" align="center" class="cpoBco"><%=  JUtil.obtFechaTxt((Date)request.getAttribute("Entrada"),"HH:mm")  %></td>
                  <td width="50%" align="center" class="cpoBco"><%=  JUtil.obtFechaTxt((Date)request.getAttribute("Salida"),"HH:mm")  %></td>
                </tr>
				<tr> 
                  <td colspan="2" align="center" class="cpoColNg">SEGUNDA ENTRADA</td>
                  <td width="50%" align="center" class="cpoColNg">SEGUNDA SALIDA</td>
                </tr>
                <tr> 
                  <td colspan="2" align="center" class="cpoBco"><%=  JUtil.obtFechaTxt((Date)request.getAttribute("Entrada2"),"HH:mm")  %></td>
                  <td width="50%" align="center" class="cpoBco"><%=  JUtil.obtFechaTxt((Date)request.getAttribute("Salida2"),"HH:mm")  %></td>
                </tr>
				<tr> 
                  <td  colspan="3" align="center" class="titCuerpoBco">ESPERANDO CAPTURA</td>
          		</tr>
              </table>

<%
		}
		else
		{
%>
		<table width="100%" border="0" cellspacing="0" cellpadding="2">
                <tr> 
                  <td align="center" class="cpoBcoRojo">ERRORES AL TRATAR DE CAPTURAR 
                    EL REGISTRO</td>
          </tr>
		  <tr> 
                  <td align="center" class="titCuerpoBco">ESPERANDO CAPTURA</td>
          </tr>
		</table>
<%
		}	
	}
%>			
			</td>
          </tr>
		  <tr> 
            <td colspan="3">&nbsp; </td>
          </tr>
        </table>
      </form></td>
  </tr>
 </table>
</body>
</html>
