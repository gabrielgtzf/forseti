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
<%@ page import="forseti.*, forseti.sets.*, java.util.*, java.io.*"%>
<%
	boolean registrado = JUtil.yaRegistradoEnFsi(request, response);
	
	if(!registrado) 
	{ 
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
 	}
	
	JPublicContMonedasSetV2 setMon = new JPublicContMonedasSetV2(request);
	setMon.m_OrderBy = "Clave ASC";
	setMon.Open();

%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Forseti</title>
<script language="JavaScript" type="text/javascript" src="../../compfsi/comps.js" >
</script>
<script language="JavaScript" type="text/javascript">
<!--
monedas = new Array(<% 		
	for(int i = 0; i< setMon.getNumRows(); i++)
	{
		out.print(setMon.getAbsRow(i).getTC() + ",");
	}
	%>1.0000);
	
function establecerTC(selMon, tc)
{
	tc.value = monedas[selMon.selectedIndex];
}

function enviarlo(formAct)
{
	if(	!esNumeroDecimal("<%= JUtil.Msj("GLB","GLB","GLB","TOTAL") %>", formAct.total.value, -9999999999, 9999999999, 2) ||
			!esNumeroDecimal("<%= JUtil.Msj("GLB","GLB","GLB","TC") %>", formAct.tc.value, 0, 9999999999, 4))
		return false;
	else
		return true;

}

-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css"></head>
<body background="../../imgfsi/cef_agua8.gif" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form onSubmit="return enviarlo(this)" action="<%= request.getParameter("verif") %>" method="post" enctype="multipart/form-data" name="subir_archivos" target="_self">
 <table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td class="titCuerpoBco" align="center" valign="middle" bgcolor="#0099FF"><%= JUtil.Msj("GLB","GLB","GLB","SAAS") %></td>
  </tr>
 <tr> 
    <td>&nbsp; </td>
  </tr>
  <tr> 
    <td> 
      <table width="100%" border="0" cellpadding="2" cellspacing="0">
          <tr>
			<td width="20%"><%= JUtil.Msj("GLB","GLB","GLB","ARCHIVO") %></td>
			<td colspan="3"><input name="archivo_1" type="file" size="50"></td>
	      </tr>
		  <tr>
		    <td><%= JUtil.Msj("GLB","GLB","GLB","TOTAL") %></td>
			<td><input name="total" type="text" id="total" class="cpoBco" size="15" maxlength="20"></td>
		  	<td><%= JUtil.Msj("GLB","GLB","GLB","MONEDA") + "/" +  JUtil.Msj("GLB","GLB","GLB","TC") %></td>
			<td><select name="idmoneda" class="cpoBco" onChange="javascript:establecerTC(this.form.idmoneda, this.form.tc)">
<% 				for(int i = 0; i< setMon.getNumRows(); i++)
				{	%>
					<option value="<%=setMon.getAbsRow(i).getClave()%>"><%= setMon.getAbsRow(i).getMoneda() %></option>
			<%	}
%>				</select> <input name="tc" type="text" id="tc" class="cpoBco" size="10" maxlength="15" value="1"></td>
			
		  </tr>
		  <tr>
			<td align="center" colspan="4">&nbsp;</td>
		  </tr>
		  <tr>
			<td bgcolor="#0099FF" align="center" colspan="4" class="titChico">Datos del CFD CBB</td>
		  </tr>
		  <tr>
			<td width="20%">Serie</td>
			<td width="30"><input name="cfd_cbb_serie" type="text" size="10" maxlength="10"></td>
			<td width="20%">Numero de folio</td>
			<td width="30"><input name="cfd_cbb_numfol" type="text" size="10" maxlength="10"></td>
	      </tr>
		  <tr>
			<td colspan="4">&nbsp;</td>
		  </tr>
		  <tr>
			<td bgcolor="#0099FF" align="center" colspan="4" class="titChico">Datos de la Factura Extranjera</td>
		  </tr>
		  <tr>
			<td width="20%">Numero de factura extranjera</td>
			<td colspan="3"><input name="factnumext" type="text" size="30" maxlength="36"></td>
	      </tr>
		  <tr>
			<td colspan="4">
<%
	//Ahora pone los parametros que no son archivos
	Enumeration nombresParam2 = request.getParameterNames();
    while(nombresParam2.hasMoreElements())
    {
        String nombreParam = (String)nombresParam2.nextElement();
		String[] valoresParam = request.getParameterValues(nombreParam);
        String valorParam = valoresParam[0];
		
		if(nombreParam.indexOf("verif") != -1)
			continue;
%>
       			<input name="<%= nombreParam %>" type="hidden" value="<%= valorParam %>">
<%			 
    } 
%>
			</td>
		  </tr>
		</table>
	    <br>
        <table width="100%" border="0" cellspacing="3" cellpadding="0">
          <tr> 
            <td align="right"> 
             	<input type="submit" name="aceptar" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
    			<input type="button" name="cancelar" onClick="javascript:history.back()" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
            </td>
          </tr>
        </table>
      
	 </td>
  </tr>
</table>
</form>
</body>
</html>
