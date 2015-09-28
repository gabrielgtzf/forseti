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
	String nom_nomina_dlg = (String)request.getAttribute("nom_nomina_dlg");
	if(nom_nomina_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}
		
	JAdmCompaniasSet set = new JAdmCompaniasSet(request);
	set.m_Where = "ID_Compania = '0' and ID_Sucursal = '" + JUtil.getSesion(request).getSesion("NOM_NOMINA").getEspecial() + "'";
	set.Open();
		
	String titulo =  JUtil.getSesion(request).getSesion("NOM_NOMINA").generarTitulo(JUtil.Msj("CEF","NOM_NOMINA","VISTA",request.getParameter("proceso"),3),"../../forsetidoc/040205.html");
		
	Calendar fecha = GregorianCalendar.getInstance();
	int Ano = JUtil.obtAno(fecha);
    int Mes = JUtil.obtMes(fecha);
	String Meses = JUtil.Msj("GLB","GLB","GLB","MESES-ANO",2); 
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Forseti</title>
<script language="JavaScript" type="text/javascript" src="../../compfsi/comps.js" >
</script>
<script language="JavaScript" type="text/javascript" src="../../compfsi/staticbar.js">
</script>
<script language="JavaScript" type="text/javascript" src="../../compfsi/cefdatetimepicker.js" >
</script>
<script language="JavaScript" type="text/javascript">
<!--
function enviarlo(formAct)
{
	if(!esNumeroEntero('Mes:', formAct.mesincon.value, 1, 12) ||
			!esNumeroEntero('Año:', formAct.anoincon.value, 2000, 2100)  )
		return false;
	else
	{
		if(confirm("<%= JUtil.Msj("GLB","GLB","GLB","CONFIRMACION") %>"))
		{
			formAct.aceptar.disabled = true;
			return true;
		}
		else
			return false;
	}
}
-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css"></head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form onSubmit="return enviarlo(this)" action="/servlet/CEFNomMovDirDlg" method="post" enctype="application/x-www-form-urlencoded" name="nom_nomina_dlg" target="_self">
<div id="topbar"> 
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr> 
      <td class="titCuerpoBco" valign="middle" bgcolor="#333333"><%= titulo %></td>
    </tr>
    <tr> 
      <td>
	   <table width="100%" bordercolor="#333333" border="1" cellpadding="4" cellspacing="0">
          <tr>
            <td align="right" class="clockCef"> 
              <%  if(JUtil.getSesion(request).getID_Mensaje() == 0) { %>
        			<input type="submit" name="aceptar" disabled="true" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
        			<%  } else { %>
        			<input type="submit" name="aceptar" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
       				<%  } %>
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/CEFNomMovDirCtrl'" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
            </td>
          </tr>
        </table> 
      </td>
    </tr>
</table>
</div>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
 		<td height="109" bgcolor="#333333">&nbsp;</td>
	</tr>
<%	
	String mensaje = JUtil.getMensaje(request, response);	
	out.println(mensaje);
	//out.print(JUtil.depurarParametros(request));
%> 
  <tr> 
    <td>
	   <table width="100%" border="0" cellspacing="3" cellpadding="0">
          <tr align="center"> 
            <td colspan="4" class="titChico"> <input name="proceso" type="hidden" value="<%= request.getParameter("proceso")%>"> 
              <input name="subproceso" type="hidden" value="ENVIAR"> <input name="id" type="hidden" value="<%= request.getParameter("id")%>">
              Procesos</td>
          </tr>
          <tr> 
            <td width="20%"> <div align="right"></div></td>
            <td colspan="3"><input type="checkbox" name="inconsistencias" value="checkbox">
              Calcular Inconsistencias</td>
          </tr>
          <tr> 
            <td> <div align="right">Mes:</div></td>
            <td width="30%">
			  <select name="mesincon" size="1" class="cpoBco">
                <option value="1"<% if(Mes == 1) out.print(" selected"); %>><%= JUtil.Elm(Meses,1) %></option>
                <option value="2"<% if(Mes == 2) out.print(" selected"); %>><%= JUtil.Elm(Meses,2) %></option>
                <option value="3"<% if(Mes == 3) out.print(" selected"); %>><%= JUtil.Elm(Meses,3) %></option>
                <option value="4"<% if(Mes == 4) out.print(" selected"); %>><%= JUtil.Elm(Meses,4) %></option>
                <option value="5"<% if(Mes == 5) out.print(" selected"); %>><%= JUtil.Elm(Meses,5) %></option>
                <option value="6"<% if(Mes == 6) out.print(" selected"); %>><%= JUtil.Elm(Meses,6)  %></option>
                <option value="7"<% if(Mes == 7) out.print(" selected"); %>><%= JUtil.Elm(Meses,7) %></option>
                <option value="8"<% if(Mes == 8) out.print(" selected"); %>><%= JUtil.Elm(Meses,8) %></option>
                <option value="9"<% if(Mes == 9) out.print(" selected"); %>><%= JUtil.Elm(Meses,9) %></option>
                <option value="10"<% if(Mes == 10) out.print(" selected"); %>><%= JUtil.Elm(Meses,10) %></option>
                <option value="11"<% if(Mes == 11) out.print(" selected"); %>><%= JUtil.Elm(Meses,11) %></option>
                <option value="12"<% if(Mes == 12) out.print(" selected"); %>><%= JUtil.Elm(Meses,12) %></option>
              </select>
			</td>
            <td width="20%" align="right">A&ntilde;o:</td>
            <td width="30%"><input name="anoincon" type="text" id="anoincon" size="8" maxlength="4"></td>
          </tr>
          <tr> 
            <td>&nbsp;</td>
            <td><input type="checkbox" name="vales" value="checkbox">
              Generar vales de despensa</td>
            <td colspan="2"><input type="checkbox" name="hepf" value="checkbox">
              H.E.P.F.</td>
          </tr>
        </table>
      </td>
  </tr>
 </table>
 </form>
</body>
<script language="JavaScript1.2">
document.nom_nomina_dlg.anoincon.value = '<% if(request.getParameter("anoincon") != null) { out.print( request.getParameter("anoincon") ); } else { out.print( Ano ); } %>'
</script>
</html>
