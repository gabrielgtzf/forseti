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
	Byte numero_nomina = (Byte)request.getAttribute("numero_nomina");
	Integer ano = (Integer)request.getAttribute("ano");
	Date desde = (Date)request.getAttribute("desde");
	Date hasta = (Date)request.getAttribute("hasta");
	Integer tipo_de_nomina = (Integer)request.getAttribute("tipo_de_nomina");
	
	JAdmCompaniasSet set = new JAdmCompaniasSet(request);
	set.m_Where = "ID_Compania = '0' and ID_Sucursal = '" + JUtil.getSesion(request).getSesion("NOM_NOMINA").getEspecial() + "'";
	set.Open();
		
	String titulo =  JUtil.getSesion(request).getSesion("NOM_NOMINA").generarTitulo(JUtil.Msj("CEF","NOM_NOMINA","VISTA",request.getParameter("proceso"),3),"../../forsetidoc/040205.html");
			
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
	if(	!esNumeroEntero('Numero:', formAct.numero_nomina.value, 0, 999) ||
			!esNumeroEntero('Año:', formAct.ano.value, 2000, 2100)  )
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
           <tr> 
            <td> <input name="proceso" type="hidden" value="<%= request.getParameter("proceso")%>">
                <input name="subproceso" type="hidden" value="ENVIAR">
                <input name="id" type="hidden" value="<%= (request.getParameter("id") == null ? "-1" : request.getParameter("id") )%>">
				<div align="right">Tipo de n&oacute;mina:</div></td>
            <td colspan="3">
<% 
		if(request.getParameter("proceso").equals("CAMBIAR_NOMINA")) 
		{ 
%> 
			<input name="tipo_de_nomina" type="hidden" value="<%= tipo_de_nomina.intValue() %>">
<%
			if(tipo_de_nomina.intValue()  == 1 || tipo_de_nomina.intValue()  == 2 || tipo_de_nomina.intValue()  == 12) 
				out.print("Normal"); 
			else if(tipo_de_nomina.intValue()  == 3 || tipo_de_nomina.intValue()  == 4 || tipo_de_nomina.intValue()  == 34) 
				out.print("Especial"); 
			else if(tipo_de_nomina.intValue()  == 5 || tipo_de_nomina.intValue()  == 6 || tipo_de_nomina.intValue()  == 56) 
				out.print("Aguinaldo"); 
			else if(tipo_de_nomina.intValue()  == 7 || tipo_de_nomina.intValue()  == 8 || tipo_de_nomina.intValue()  == 78) 
				out.println("Vales"); 
			else
				out.print(tipo_de_nomina.intValue());

		}
		else
		{
%>			
				<select name="tipo_de_nomina" id="tipo_de_nomina">
                <option value="-1"<% if(request.getParameter("tipo_de_nomina") != null) {
										if(request.getParameter("tipo_de_nomina").equals("-1")) {
											out.println(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_NOMINA")) { 
											if(tipo_de_nomina.intValue() == -1) {
												out.println(" selected"); 
											}
										}
									 } %>>--- Selecciona el tipo ---</option>
                <option value="12"<% if(request.getParameter("tipo_de_nomina") != null) {
										if(request.getParameter("tipo_de_nomina").equals("12")) {
											out.println(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_NOMINA")) { 
											if(tipo_de_nomina.intValue()  == 1 || tipo_de_nomina.intValue()  == 2 || tipo_de_nomina.intValue()  == 12) {
												out.println(" selected"); 
											}
										}
									 } %>>Normal</option>
		        <option value="56"<% if(request.getParameter("tipo_de_nomina") != null) {
										if(request.getParameter("tipo_de_nomina").equals("56")) {
											out.println(" selected");
										}
									 } else {
										if(!request.getParameter("proceso").equals("AGREGAR_NOMINA")) { 
											if(tipo_de_nomina.intValue()  == 5 || tipo_de_nomina.intValue()  == 6 || tipo_de_nomina.intValue()  == 56) {
												out.println(" selected"); 
											}
										}
									 } %>>Aguinaldo</option>
				
			    </select> 
<%
		}
%>
			  </td>
          </tr>
		  <tr> 
            <td width="20%"> <div align="right"> 
                     N&uacute;mero:</div></td>
            <td width="30%"> <input name="numero_nomina" type="text" id="numero_nomina" size="5" maxlength="2"<%= (request.getParameter("proceso").equals("CAMBIAR_NOMINA")) ? " readonly=\"true\"" : "" %>> 
            </td>
            <td width="20%">A&ntilde;o:</td>
            <td width="30%"> <input name="ano" type="text" id="ano" size="8" maxlength="4"<%= (request.getParameter("proceso").equals("CAMBIAR_NOMINA")) ? " readonly=\"true\"" : "" %>> 
            </td>
          </tr>
          <tr> 
            <td> <div align="right">Desde:</div></td>
            <td><input name="desde" type="text" id="desde" size="12" maxlength="15" readonly="true"> 
              <a href="javascript:NewCal('desde','ddmmmyyyy',false)"><img src="../../imgfsi/calendario.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CALENDARIO") %>" border="0" align="absmiddle"></a></td>
            <td>Hasta:</td>
            <td><input name="hasta" type="text" id="hasta" size="12" maxlength="15" readonly="true"> 
              <a href="javascript:NewCal('hasta','ddmmmyyyy',false)"><img src="../../imgfsi/calendario.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CALENDARIO") %>" border="0" align="absmiddle"></a></td>
          </tr>
         </table>
      </td>
  </tr>
 </table>
</form>
<script language="JavaScript1.2">
document.nom_nomina_dlg.numero_nomina.value = '<% if(request.getParameter("numero_nomina") != null) { out.print( request.getParameter("numero_nomina") ); } else { out.print( numero_nomina.toString() ); } %>'
document.nom_nomina_dlg.ano.value = '<% if(request.getParameter("ano") != null) { out.print( request.getParameter("ano") ); } else { out.print( ano.toString() ); } %>'
document.nom_nomina_dlg.desde.value = '<% if(request.getParameter("desde") != null) { out.print( request.getParameter("desde") ); } else { out.print( JUtil.obtFechaTxt(desde, "dd/MMM/yyyy") ); } %>'
document.nom_nomina_dlg.hasta.value = '<% if(request.getParameter("hasta") != null) { out.print( request.getParameter("hasta") ); } else { out.print( JUtil.obtFechaTxt(hasta, "dd/MMM/yyyy") ); } %>'
</script>
</body>
</html>
