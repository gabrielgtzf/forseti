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
<%@ page import="forseti.*, fsi_admin.*, forseti.sets.*, java.util.*, java.io.*, org.apache.commons.lang.*"%>
<%
	String adm_bd_dlg = (String)request.getAttribute("adm_bd_dlg");
	if(adm_bd_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiadmin/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}

	String titulo =  JUtil.getSesionAdmin(request).getSesion("ADMIN_BD").generarTitulo(JUtil.Msj("SAF","ADMIN_BD","VISTA",request.getParameter("proceso"),3));
	
	session = request.getSession(true);
    JRepGenSes rec = (JRepGenSes)session.getAttribute("rep_gen_dlg");
	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Forseti</title>
<script language="JavaScript" type="text/javascript" src="../../compfsi/comps.js" >
</script>
<script language="JavaScript" type="text/javascript" src="../../compfsi/staticbar.js">
</script>
<script language="JavaScript" type="text/javascript">
<!--
function enviarlo(formAct)
{
	if(formAct.subproceso.value == "ENVIAR")
	{
		if(confirm("<%= JUtil.Msj("GLB","GLB","GLB","CONFIRMACION") %>"))
			return true;
		else
			return false;
	}
	else
		return true;
	
}
-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form onSubmit="return enviarlo(this)" action="/servlet/SAFAdmBDDlg" method="post" enctype="application/x-www-form-urlencoded" name="rep_gen_dlg" target="_self">
<div id="topbar"> 
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr> 
      <td class="titCuerpoBco" valign="middle" bgcolor="#333333"><%= titulo %></td>
    </tr>
    <tr> 
      <td>
	   <table width="100%" bordercolor="#333333" border="1" cellpadding="4" cellspacing="0">
          <tr>
            <td align="right" class="clock"> 
              <%  if(JUtil.getSesionAdmin(request).getID_Mensaje() == 0 && rec.getID_Report() != rec.getID_ReportPlnt()) { %>
        			<input type="submit" name="aceptar" disabled="true" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
        			<%  } else { %>
        			<input type="submit" name="aceptar" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
       				<%  } %>
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/SAFAdmBDCtrl';" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
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
	String mensaje = JUtil.getMensajeAdmin(request, response);	
	out.println(mensaje);
	//out.print(JUtil.depurarParametros(request));
%>
    <tr> 
      <td>
	  		<% if(request.getParameter("id") != null) { %><input name="id" type="hidden" value="<%= request.getParameter("id") %>"><% } %> 
			<input name="proceso" type="hidden" value="<%= request.getParameter("proceso") %>">
			<input name="REPID" type="hidden" value="<%= request.getParameter("REPID") %>">
			<input name="subproceso" type="hidden" value="ENVIAR">
	</td>
  </tr>
  <tr> 
    <td>
        <table width="100%" border="0" cellspacing="2" cellpadding="0">
          <tr> 
            <td width="20%">Base de datos de salida:</td>
            <td colspan="3" class="titCuerpoNar"><%= rec.getBDP() %></td>
          </tr>
          <tr> 
            <td width="20%">ID de Reporte:</td>
            <td width="30%"> <input name="idreport" type="text" size="10" maxlength="7" value="<%=  rec.getID_Report() %>" readonly="true"></td>
            <td width="20%">Tipo y Nombre Corto:</td>
            <td width="30%"> 
			  <select name="tipo" class="cpoCol">
<%
			JUsuariosPermisosCatalogoSet pc = new JUsuariosPermisosCatalogoSet(request);
			pc.ConCat(3);
			pc.setBD(rec.getBDP());
   			pc.Open();
	
			for(int i = 0; i < pc.getNumRows(); i++)
			{
				if(StringUtils.countMatches(pc.getAbsRow(i).getID_Permiso(), "_") == 1)
				{
%>			
                <option value="<%= pc.getAbsRow(i).getID_Permiso() %>"<%  if(rec.getTipo().equals(pc.getAbsRow(i).getID_Permiso())) { out.print(" selected"); } %>><%= pc.getAbsRow(i).getModulo() %></option>
<%
				}
			}
%>
				</select> 			
			  <input name="clave" type="text" id="clave" value="<%=  rec.getClave() %>" size="8" maxlength="10"> 
            </td>
          </tr>
          <tr> 
            <td width="20%">Descripci&oacute;n:</td>
            <td width="30%"><input name="descripcion" type="text" size="80" maxlength="254" value="<%=  rec.getDescripcion() %>"></td>
            <td><input name="FSI_PANTALLA" type="checkbox"<% if(request.getParameter("FSI_PANTALLA") != null) { out.print(" checked"); } %>>
              Salida del c&oacute;digo a la pantalla</td>
            <td><input name="graficar" type="checkbox"<%= ((rec.getGraficar()) ? " checked" : "") %>>
              Este reporte permite graficar.</td>
          </tr>
          <tr> 
            <td width="20%">Ventana Horizontal:</td>
            <td width="30%"><input name="hw" type="text" id="hw" size="12" maxlength="10" value="<%=  rec.getHW() %>"> 
              &nbsp;pixeles</td>
            <td>Ventana Vertical</td>
            <td width="30%"><input name="vw" type="text" id="vw" size="12" maxlength="10" value="<%=  rec.getVW() %>"> 
              &nbsp;pixeles</td>
          </tr>
          <tr> 
            <td width="20%">T&iacute;itulo:</td>
            <td colspan="3"> <select name="titulo_fuente" class="cpoBco">
                <option value="Arial, Helvetica, sans-serif"<% if(rec.getTituloFuente().equals("Arial, Helvetica, sans-serif")) { out.print(" selected"); } %>>Arial, 
                Helvetica, sans-serif</option>
                <option value="Times New Roman, Times, serif"<% if(rec.getTituloFuente().equals("Times New Roman, Times, serif")) { out.print(" selected"); } %>>Times 
                New Roman, Times, serif</option>
                <option value="Courier New, Courier, mono"<% if(rec.getTituloFuente().equals("Courier New, Courier, mono")) { out.print(" selected"); } %>>Courier 
                New, Courier, mono</option>
                <option value="Georgia, Times New Roman, Times, serif"<% if(rec.getTituloFuente().equals("Georgia, Times New Roman, Times, serif")) { out.print(" selected"); } %>>Georgia, 
                Times New Roman, Times, serif</option>
                <option value="Verdana, Arial, Helvetica, sans-serif"<% if(rec.getTituloFuente().equals("Verdana, Arial, Helvetica, sans-serif")) { out.print(" selected"); } %>>Verdana, 
                Arial, Helvetica, sans-serif</option>
                <option value="Geneva, Arial, Helvetica, sans-serif"<% if(rec.getTituloFuente().equals("Geneva, Arial, Helvetica, sans-serif")) { out.print(" selected"); } %>>Geneva, 
                Arial, Helvetica, sans-serif</option>
              </select> &nbsp; <select name="titulo_tam" class="cpoBco">
                <option value="4pt"<% if(rec.getTituloTam().equals("4pt")) { out.print(" selected"); } %>>4</option>
                <option value="5pt"<% if(rec.getTituloTam().equals("5pt")) { out.print(" selected"); } %>>5</option>
                <option value="6pt"<% if(rec.getTituloTam().equals("6pt")) { out.print(" selected"); } %>>6</option>
                <option value="7pt"<% if(rec.getTituloTam().equals("7pt")) { out.print(" selected"); } %>>7</option>
                <option value="8pt"<% if(rec.getTituloTam().equals("8pt")) { out.print(" selected"); } %>>8</option>
                <option value="9pt"<% if(rec.getTituloTam().equals("9pt")) { out.print(" selected"); } %>>9</option>
                <option value="10pt"<% if(rec.getTituloTam().equals("10pt")) { out.print(" selected"); } %>>10</option>
                <option value="11pt"<% if(rec.getTituloTam().equals("11pt")) { out.print(" selected"); } %>>11</option>
                <option value="12pt"<% if(rec.getTituloTam().equals("12pt")) { out.print(" selected"); } %>>12</option>
                <option value="13pt"<% if(rec.getTituloTam().equals("13pt")) { out.print(" selected"); } %>>13</option>
                <option value="14pt"<% if(rec.getTituloTam().equals("14pt")) { out.print(" selected"); } %>>14</option>
                <option value="15pt"<% if(rec.getTituloTam().equals("15pt")) { out.print(" selected"); } %>>15</option>
                <option value="16pt"<% if(rec.getTituloTam().equals("16pt")) { out.print(" selected"); } %>>16</option>
                <option value="18pt"<% if(rec.getTituloTam().equals("18pt")) { out.print(" selected"); } %>>18</option>
                <option value="20pt"<% if(rec.getTituloTam().equals("20pt")) { out.print(" selected"); } %>>20</option>
                <option value="22pt"<% if(rec.getTituloTam().equals("22pt")) { out.print(" selected"); } %>>22</option>
                <option value="24pt"<% if(rec.getTituloTam().equals("24pt")) { out.print(" selected"); } %>>24</option>
                <option value="26pt"<% if(rec.getTituloTam().equals("26pt")) { out.print(" selected"); } %>>26</option>
                <option value="28pt"<% if(rec.getTituloTam().equals("28pt")) { out.print(" selected"); } %>>28</option>
                <option value="32pt"<% if(rec.getTituloTam().equals("32pt")) { out.print(" selected"); } %>>32</option>
                <option value="36pt"<% if(rec.getTituloTam().equals("36pt")) { out.print(" selected"); } %>>36</option>
                <option value="40pt"<% if(rec.getTituloTam().equals("40pt")) { out.print(" selected"); } %>>40</option>
                <option value="44pt"<% if(rec.getTituloTam().equals("44pt")) { out.print(" selected"); } %>>44</option>
                <option value="48pt"<% if(rec.getTituloTam().equals("48pt")) { out.print(" selected"); } %>>48</option>
                <option value="54pt"<% if(rec.getTituloTam().equals("54pt")) { out.print(" selected"); } %>>54</option>
                <option value="60pt"<% if(rec.getTituloTam().equals("60pt")) { out.print(" selected"); } %>>60</option>
                <option value="66pt"<% if(rec.getTituloTam().equals("66pt")) { out.print(" selected"); } %>>66</option>
                <option value="72pt"<% if(rec.getTituloTam().equals("72pt")) { out.print(" selected"); } %>>72</option>
                <option value="80pt"<% if(rec.getTituloTam().equals("80pt")) { out.print(" selected"); } %>>80</option>
                <option value="88pt"<% if(rec.getTituloTam().equals("88pt")) { out.print(" selected"); } %>>88</option>
                <option value="96pt"<% if(rec.getTituloTam().equals("96pt")) { out.print(" selected"); } %>>96</option>
              </select> &nbsp; <select name="titulo_grosor" class="cpoBco">
                <option value="normal"<% if(rec.getTituloGrosor().equals("normal")) { out.print(" selected"); } %>>normal</option>
                <option value="bold"<% if(rec.getTituloGrosor().equals("bold")) { out.print(" selected"); } %>>negrita</option>
              </select> &nbsp; <select name="titulo_estilo" class="cpoBco">
                <option value="normal"<% if(rec.getTituloEstilo().equals("normal")) { out.print(" selected"); } %>>normal</option>
                <option value="italic"<% if(rec.getTituloEstilo().equals("italic")) { out.print(" selected"); } %>>cursiva</option>
              </select> </td>
          </tr>
          <tr> 
            <td width="20%">Encabezado primer nivel:</td>
            <td colspan="3"> <select name="encl1_fuente" class="cpoBco">
                <option value="Arial, Helvetica, sans-serif"<% if(rec.getEncL1Fuente().equals("Arial, Helvetica, sans-serif")) { out.print(" selected"); } %>>Arial, 
                Helvetica, sans-serif</option>
                <option value="Times New Roman, Times, serif"<% if(rec.getEncL1Fuente().equals("Times New Roman, Times, serif")) { out.print(" selected"); } %>>Times 
                New Roman, Times, serif</option>
                <option value="Courier New, Courier, mono"<% if(rec.getEncL1Fuente().equals("Courier New, Courier, mono")) { out.print(" selected"); } %>>Courier 
                New, Courier, mono</option>
                <option value="Georgia, Times New Roman, Times, serif"<% if(rec.getEncL1Fuente().equals("Georgia, Times New Roman, Times, serif")) { out.print(" selected"); } %>>Georgia, 
                Times New Roman, Times, serif</option>
                <option value="Verdana, Arial, Helvetica, sans-serif"<% if(rec.getEncL1Fuente().equals("Verdana, Arial, Helvetica, sans-serif")) { out.print(" selected"); } %>>Verdana, 
                Arial, Helvetica, sans-serif</option>
                <option value="Geneva, Arial, Helvetica, sans-serif"<% if(rec.getEncL1Fuente().equals("Geneva, Arial, Helvetica, sans-serif")) { out.print(" selected"); } %>>Geneva, 
                Arial, Helvetica, sans-serif</option>
              </select> &nbsp; <select name="encl1_tam" class="cpoBco">
                <option value="4pt"<% if(rec.getEncL1Tam().equals("4pt")) { out.print(" selected"); } %>>4</option>
                <option value="5pt"<% if(rec.getEncL1Tam().equals("5pt")) { out.print(" selected"); } %>>5</option>
                <option value="6pt"<% if(rec.getEncL1Tam().equals("6pt")) { out.print(" selected"); } %>>6</option>
                <option value="7pt"<% if(rec.getEncL1Tam().equals("7pt")) { out.print(" selected"); } %>>7</option>
                <option value="8pt"<% if(rec.getEncL1Tam().equals("8pt")) { out.print(" selected"); } %>>8</option>
                <option value="9pt"<% if(rec.getEncL1Tam().equals("9pt")) { out.print(" selected"); } %>>9</option>
                <option value="10pt"<% if(rec.getEncL1Tam().equals("10pt")) { out.print(" selected"); } %>>10</option>
                <option value="11pt"<% if(rec.getEncL1Tam().equals("11pt")) { out.print(" selected"); } %>>11</option>
                <option value="12pt"<% if(rec.getEncL1Tam().equals("12pt")) { out.print(" selected"); } %>>12</option>
                <option value="13pt"<% if(rec.getEncL1Tam().equals("13pt")) { out.print(" selected"); } %>>13</option>
                <option value="14pt"<% if(rec.getEncL1Tam().equals("14pt")) { out.print(" selected"); } %>>14</option>
                <option value="15pt"<% if(rec.getEncL1Tam().equals("15pt")) { out.print(" selected"); } %>>15</option>
                <option value="16pt"<% if(rec.getEncL1Tam().equals("16pt")) { out.print(" selected"); } %>>16</option>
                <option value="18pt"<% if(rec.getEncL1Tam().equals("18pt")) { out.print(" selected"); } %>>18</option>
                <option value="20pt"<% if(rec.getEncL1Tam().equals("20pt")) { out.print(" selected"); } %>>20</option>
                <option value="22pt"<% if(rec.getEncL1Tam().equals("22pt")) { out.print(" selected"); } %>>22</option>
                <option value="24pt"<% if(rec.getEncL1Tam().equals("24pt")) { out.print(" selected"); } %>>24</option>
                <option value="26pt"<% if(rec.getEncL1Tam().equals("26pt")) { out.print(" selected"); } %>>26</option>
                <option value="28pt"<% if(rec.getEncL1Tam().equals("28pt")) { out.print(" selected"); } %>>28</option>
                <option value="32pt"<% if(rec.getEncL1Tam().equals("32pt")) { out.print(" selected"); } %>>32</option>
                <option value="36pt"<% if(rec.getEncL1Tam().equals("36pt")) { out.print(" selected"); } %>>36</option>
                <option value="40pt"<% if(rec.getEncL1Tam().equals("40pt")) { out.print(" selected"); } %>>40</option>
                <option value="44pt"<% if(rec.getEncL1Tam().equals("44pt")) { out.print(" selected"); } %>>44</option>
                <option value="48pt"<% if(rec.getEncL1Tam().equals("48pt")) { out.print(" selected"); } %>>48</option>
                <option value="54pt"<% if(rec.getEncL1Tam().equals("54pt")) { out.print(" selected"); } %>>54</option>
                <option value="60pt"<% if(rec.getEncL1Tam().equals("60pt")) { out.print(" selected"); } %>>60</option>
                <option value="66pt"<% if(rec.getEncL1Tam().equals("66pt")) { out.print(" selected"); } %>>66</option>
                <option value="72pt"<% if(rec.getEncL1Tam().equals("72pt")) { out.print(" selected"); } %>>72</option>
                <option value="80pt"<% if(rec.getEncL1Tam().equals("80pt")) { out.print(" selected"); } %>>80</option>
                <option value="88pt"<% if(rec.getEncL1Tam().equals("88pt")) { out.print(" selected"); } %>>88</option>
                <option value="96pt"<% if(rec.getEncL1Tam().equals("96pt")) { out.print(" selected"); } %>>96</option>
              </select> &nbsp; <select name="encl1_grosor" class="cpoBco">
                <option value="normal"<% if(rec.getEncL1Grosor().equals("normal")) { out.print(" selected"); } %>>normal</option>
                <option value="bold"<% if(rec.getEncL1Grosor().equals("bold")) { out.print(" selected"); } %>>negrita</option>
              </select> &nbsp; <select name="encl1_estilo" class="cpoBco">
                <option value="normal"<% if(rec.getEncL1Estilo().equals("normal")) { out.print(" selected"); } %>>normal</option>
                <option value="italic"<% if(rec.getEncL1Estilo().equals("italic")) { out.print(" selected"); } %>>cursiva</option>
              </select> </td>
          </tr>
          <tr> 
            <td width="20%">Encabezado segundo nivel:</td>
            <td colspan="3"> <select name="encl2_fuente" class="cpoBco">
                <option value="Arial, Helvetica, sans-serif"<% if(rec.getEncL2Fuente().equals("Arial, Helvetica, sans-serif")) { out.print(" selected"); } %>>Arial, 
                Helvetica, sans-serif</option>
                <option value="Times New Roman, Times, serif"<% if(rec.getEncL2Fuente().equals("Times New Roman, Times, serif")) { out.print(" selected"); } %>>Times 
                New Roman, Times, serif</option>
                <option value="Courier New, Courier, mono"<% if(rec.getEncL2Fuente().equals("Courier New, Courier, mono")) { out.print(" selected"); } %>>Courier 
                New, Courier, mono</option>
                <option value="Georgia, Times New Roman, Times, serif"<% if(rec.getEncL2Fuente().equals("Georgia, Times New Roman, Times, serif")) { out.print(" selected"); } %>>Georgia, 
                Times New Roman, Times, serif</option>
                <option value="Verdana, Arial, Helvetica, sans-serif"<% if(rec.getEncL2Fuente().equals("Verdana, Arial, Helvetica, sans-serif")) { out.print(" selected"); } %>>Verdana, 
                Arial, Helvetica, sans-serif</option>
                <option value="Geneva, Arial, Helvetica, sans-serif"<% if(rec.getEncL2Fuente().equals("Geneva, Arial, Helvetica, sans-serif")) { out.print(" selected"); } %>>Geneva, 
                Arial, Helvetica, sans-serif</option>
              </select> &nbsp; <select name="encl2_tam" class="cpoBco">
                <option value="4pt"<% if(rec.getEncL2Tam().equals("4pt")) { out.print(" selected"); } %>>4</option>
                <option value="5pt"<% if(rec.getEncL2Tam().equals("5pt")) { out.print(" selected"); } %>>5</option>
                <option value="6pt"<% if(rec.getEncL2Tam().equals("6pt")) { out.print(" selected"); } %>>6</option>
                <option value="7pt"<% if(rec.getEncL2Tam().equals("7pt")) { out.print(" selected"); } %>>7</option>
                <option value="8pt"<% if(rec.getEncL2Tam().equals("8pt")) { out.print(" selected"); } %>>8</option>
                <option value="9pt"<% if(rec.getEncL2Tam().equals("9pt")) { out.print(" selected"); } %>>9</option>
                <option value="10pt"<% if(rec.getEncL2Tam().equals("10pt")) { out.print(" selected"); } %>>10</option>
                <option value="11pt"<% if(rec.getEncL2Tam().equals("11pt")) { out.print(" selected"); } %>>11</option>
                <option value="12pt"<% if(rec.getEncL2Tam().equals("12pt")) { out.print(" selected"); } %>>12</option>
                <option value="13pt"<% if(rec.getEncL2Tam().equals("13pt")) { out.print(" selected"); } %>>13</option>
                <option value="14pt"<% if(rec.getEncL2Tam().equals("14pt")) { out.print(" selected"); } %>>14</option>
                <option value="15pt"<% if(rec.getEncL2Tam().equals("15pt")) { out.print(" selected"); } %>>15</option>
                <option value="16pt"<% if(rec.getEncL2Tam().equals("16pt")) { out.print(" selected"); } %>>16</option>
                <option value="18pt"<% if(rec.getEncL2Tam().equals("18pt")) { out.print(" selected"); } %>>18</option>
                <option value="20pt"<% if(rec.getEncL2Tam().equals("20pt")) { out.print(" selected"); } %>>20</option>
                <option value="22pt"<% if(rec.getEncL2Tam().equals("22pt")) { out.print(" selected"); } %>>22</option>
                <option value="24pt"<% if(rec.getEncL2Tam().equals("24pt")) { out.print(" selected"); } %>>24</option>
                <option value="26pt"<% if(rec.getEncL2Tam().equals("26pt")) { out.print(" selected"); } %>>26</option>
                <option value="28pt"<% if(rec.getEncL2Tam().equals("28pt")) { out.print(" selected"); } %>>28</option>
                <option value="32pt"<% if(rec.getEncL2Tam().equals("32pt")) { out.print(" selected"); } %>>32</option>
                <option value="36pt"<% if(rec.getEncL2Tam().equals("36pt")) { out.print(" selected"); } %>>36</option>
                <option value="40pt"<% if(rec.getEncL2Tam().equals("40pt")) { out.print(" selected"); } %>>40</option>
                <option value="44pt"<% if(rec.getEncL2Tam().equals("44pt")) { out.print(" selected"); } %>>44</option>
                <option value="48pt"<% if(rec.getEncL2Tam().equals("48pt")) { out.print(" selected"); } %>>48</option>
                <option value="54pt"<% if(rec.getEncL2Tam().equals("54pt")) { out.print(" selected"); } %>>54</option>
                <option value="60pt"<% if(rec.getEncL2Tam().equals("60pt")) { out.print(" selected"); } %>>60</option>
                <option value="66pt"<% if(rec.getEncL2Tam().equals("66pt")) { out.print(" selected"); } %>>66</option>
                <option value="72pt"<% if(rec.getEncL2Tam().equals("72pt")) { out.print(" selected"); } %>>72</option>
                <option value="80pt"<% if(rec.getEncL2Tam().equals("80pt")) { out.print(" selected"); } %>>80</option>
                <option value="88pt"<% if(rec.getEncL2Tam().equals("88pt")) { out.print(" selected"); } %>>88</option>
                <option value="96pt"<% if(rec.getEncL2Tam().equals("96pt")) { out.print(" selected"); } %>>96</option>
              </select> &nbsp; <select name="encl2_grosor" class="cpoBco">
                <option value="normal"<% if(rec.getEncL2Grosor().equals("normal")) { out.print(" selected"); } %>>normal</option>
                <option value="bold"<% if(rec.getEncL2Grosor().equals("bold")) { out.print(" selected"); } %>>negrita</option>
              </select> &nbsp; <select name="encl2_estilo" class="cpoBco">
                <option value="normal"<% if(rec.getEncL2Estilo().equals("normal")) { out.print(" selected"); } %>>normal</option>
                <option value="italic"<% if(rec.getEncL2Estilo().equals("italic")) { out.print(" selected"); } %>>cursiva</option>
              </select> </td>
          </tr>
          <tr> 
            <td width="20%">Encabezado tercer nivel:</td>
            <td colspan="3"> <select name="encl3_fuente" class="cpoBco">
                <option value="Arial, Helvetica, sans-serif"<% if(rec.getEncL3Fuente().equals("Arial, Helvetica, sans-serif")) { out.print(" selected"); } %>>Arial, 
                Helvetica, sans-serif</option>
                <option value="Times New Roman, Times, serif"<% if(rec.getEncL3Fuente().equals("Times New Roman, Times, serif")) { out.print(" selected"); } %>>Times 
                New Roman, Times, serif</option>
                <option value="Courier New, Courier, mono"<% if(rec.getEncL3Fuente().equals("Courier New, Courier, mono")) { out.print(" selected"); } %>>Courier 
                New, Courier, mono</option>
                <option value="Georgia, Times New Roman, Times, serif"<% if(rec.getEncL3Fuente().equals("Georgia, Times New Roman, Times, serif")) { out.print(" selected"); } %>>Georgia, 
                Times New Roman, Times, serif</option>
                <option value="Verdana, Arial, Helvetica, sans-serif"<% if(rec.getEncL3Fuente().equals("Verdana, Arial, Helvetica, sans-serif")) { out.print(" selected"); } %>>Verdana, 
                Arial, Helvetica, sans-serif</option>
                <option value="Geneva, Arial, Helvetica, sans-serif"<% if(rec.getEncL3Fuente().equals("Geneva, Arial, Helvetica, sans-serif")) { out.print(" selected"); } %>>Geneva, 
                Arial, Helvetica, sans-serif</option>
              </select> &nbsp; <select name="encl3_tam" class="cpoBco">
                <option value="4pt"<% if(rec.getEncL3Tam().equals("4pt")) { out.print(" selected"); } %>>4</option>
                <option value="5pt"<% if(rec.getEncL3Tam().equals("5pt")) { out.print(" selected"); } %>>5</option>
                <option value="6pt"<% if(rec.getEncL3Tam().equals("6pt")) { out.print(" selected"); } %>>6</option>
                <option value="7pt"<% if(rec.getEncL3Tam().equals("7pt")) { out.print(" selected"); } %>>7</option>
                <option value="8pt"<% if(rec.getEncL3Tam().equals("8pt")) { out.print(" selected"); } %>>8</option>
                <option value="9pt"<% if(rec.getEncL3Tam().equals("9pt")) { out.print(" selected"); } %>>9</option>
                <option value="10pt"<% if(rec.getEncL3Tam().equals("10pt")) { out.print(" selected"); } %>>10</option>
                <option value="11pt"<% if(rec.getEncL3Tam().equals("11pt")) { out.print(" selected"); } %>>11</option>
                <option value="12pt"<% if(rec.getEncL3Tam().equals("12pt")) { out.print(" selected"); } %>>12</option>
                <option value="13pt"<% if(rec.getEncL3Tam().equals("13pt")) { out.print(" selected"); } %>>13</option>
                <option value="14pt"<% if(rec.getEncL3Tam().equals("14pt")) { out.print(" selected"); } %>>14</option>
                <option value="15pt"<% if(rec.getEncL3Tam().equals("15pt")) { out.print(" selected"); } %>>15</option>
                <option value="16pt"<% if(rec.getEncL3Tam().equals("16pt")) { out.print(" selected"); } %>>16</option>
                <option value="18pt"<% if(rec.getEncL3Tam().equals("18pt")) { out.print(" selected"); } %>>18</option>
                <option value="20pt"<% if(rec.getEncL3Tam().equals("20pt")) { out.print(" selected"); } %>>20</option>
                <option value="22pt"<% if(rec.getEncL3Tam().equals("22pt")) { out.print(" selected"); } %>>22</option>
                <option value="24pt"<% if(rec.getEncL3Tam().equals("24pt")) { out.print(" selected"); } %>>24</option>
                <option value="26pt"<% if(rec.getEncL3Tam().equals("26pt")) { out.print(" selected"); } %>>26</option>
                <option value="28pt"<% if(rec.getEncL3Tam().equals("28pt")) { out.print(" selected"); } %>>28</option>
                <option value="32pt"<% if(rec.getEncL3Tam().equals("32pt")) { out.print(" selected"); } %>>32</option>
                <option value="36pt"<% if(rec.getEncL3Tam().equals("36pt")) { out.print(" selected"); } %>>36</option>
                <option value="40pt"<% if(rec.getEncL3Tam().equals("40pt")) { out.print(" selected"); } %>>40</option>
                <option value="44pt"<% if(rec.getEncL3Tam().equals("44pt")) { out.print(" selected"); } %>>44</option>
                <option value="48pt"<% if(rec.getEncL3Tam().equals("48pt")) { out.print(" selected"); } %>>48</option>
                <option value="54pt"<% if(rec.getEncL3Tam().equals("54pt")) { out.print(" selected"); } %>>54</option>
                <option value="60pt"<% if(rec.getEncL3Tam().equals("60pt")) { out.print(" selected"); } %>>60</option>
                <option value="66pt"<% if(rec.getEncL3Tam().equals("66pt")) { out.print(" selected"); } %>>66</option>
                <option value="72pt"<% if(rec.getEncL3Tam().equals("72pt")) { out.print(" selected"); } %>>72</option>
                <option value="80pt"<% if(rec.getEncL3Tam().equals("80pt")) { out.print(" selected"); } %>>80</option>
                <option value="88pt"<% if(rec.getEncL3Tam().equals("88pt")) { out.print(" selected"); } %>>88</option>
                <option value="96pt"<% if(rec.getEncL3Tam().equals("96pt")) { out.print(" selected"); } %>>96</option>
              </select> &nbsp; <select name="encl3_grosor" class="cpoBco">
                <option value="normal"<% if(rec.getEncL3Grosor().equals("normal")) { out.print(" selected"); } %>>normal</option>
                <option value="bold"<% if(rec.getEncL3Grosor().equals("bold")) { out.print(" selected"); } %>>negrita</option>
              </select> &nbsp; <select name="encl3_estilo" class="cpoBco">
                <option value="normal"<% if(rec.getEncL3Estilo().equals("normal")) { out.print(" selected"); } %>>normal</option>
                <option value="italic"<% if(rec.getEncL3Estilo().equals("italic")) { out.print(" selected"); } %>>cursiva</option>
              </select> </td>
          </tr>
          <tr> 
            <td width="20%">Detalles Primer Nivel:</td>
            <td colspan="3"> <select name="l1_fuente" class="cpoBco">
                <option value="Arial, Helvetica, sans-serif"<% if(rec.getL1Fuente().equals("Arial, Helvetica, sans-serif")) { out.print(" selected"); } %>>Arial, 
                Helvetica, sans-serif</option>
                <option value="Times New Roman, Times, serif"<% if(rec.getL1Fuente().equals("Times New Roman, Times, serif")) { out.print(" selected"); } %>>Times 
                New Roman, Times, serif</option>
                <option value="Courier New, Courier, mono"<% if(rec.getL1Fuente().equals("Courier New, Courier, mono")) { out.print(" selected"); } %>>Courier 
                New, Courier, mono</option>
                <option value="Georgia, Times New Roman, Times, serif"<% if(rec.getL1Fuente().equals("Georgia, Times New Roman, Times, serif")) { out.print(" selected"); } %>>Georgia, 
                Times New Roman, Times, serif</option>
                <option value="Verdana, Arial, Helvetica, sans-serif"<% if(rec.getL1Fuente().equals("Verdana, Arial, Helvetica, sans-serif")) { out.print(" selected"); } %>>Verdana, 
                Arial, Helvetica, sans-serif</option>
                <option value="Geneva, Arial, Helvetica, sans-serif"<% if(rec.getL1Fuente().equals("Geneva, Arial, Helvetica, sans-serif")) { out.print(" selected"); } %>>Geneva, 
                Arial, Helvetica, sans-serif</option>
              </select> &nbsp; <select name="l1_tam" class="cpoBco">
                <option value="4pt"<% if(rec.getL1Tam().equals("4pt")) { out.print(" selected"); } %>>4</option>
                <option value="5pt"<% if(rec.getL1Tam().equals("5pt")) { out.print(" selected"); } %>>5</option>
                <option value="6pt"<% if(rec.getL1Tam().equals("6pt")) { out.print(" selected"); } %>>6</option>
                <option value="7pt"<% if(rec.getL1Tam().equals("7pt")) { out.print(" selected"); } %>>7</option>
                <option value="8pt"<% if(rec.getL1Tam().equals("8pt")) { out.print(" selected"); } %>>8</option>
                <option value="9pt"<% if(rec.getL1Tam().equals("9pt")) { out.print(" selected"); } %>>9</option>
                <option value="10pt"<% if(rec.getL1Tam().equals("10pt")) { out.print(" selected"); } %>>10</option>
                <option value="11pt"<% if(rec.getL1Tam().equals("11pt")) { out.print(" selected"); } %>>11</option>
                <option value="12pt"<% if(rec.getL1Tam().equals("12pt")) { out.print(" selected"); } %>>12</option>
                <option value="13pt"<% if(rec.getL1Tam().equals("13pt")) { out.print(" selected"); } %>>13</option>
                <option value="14pt"<% if(rec.getL1Tam().equals("14pt")) { out.print(" selected"); } %>>14</option>
                <option value="15pt"<% if(rec.getL1Tam().equals("15pt")) { out.print(" selected"); } %>>15</option>
                <option value="16pt"<% if(rec.getL1Tam().equals("16pt")) { out.print(" selected"); } %>>16</option>
                <option value="18pt"<% if(rec.getL1Tam().equals("18pt")) { out.print(" selected"); } %>>18</option>
                <option value="20pt"<% if(rec.getL1Tam().equals("20pt")) { out.print(" selected"); } %>>20</option>
                <option value="22pt"<% if(rec.getL1Tam().equals("22pt")) { out.print(" selected"); } %>>22</option>
                <option value="24pt"<% if(rec.getL1Tam().equals("24pt")) { out.print(" selected"); } %>>24</option>
                <option value="26pt"<% if(rec.getL1Tam().equals("26pt")) { out.print(" selected"); } %>>26</option>
                <option value="28pt"<% if(rec.getL1Tam().equals("28pt")) { out.print(" selected"); } %>>28</option>
                <option value="32pt"<% if(rec.getL1Tam().equals("32pt")) { out.print(" selected"); } %>>32</option>
                <option value="36pt"<% if(rec.getL1Tam().equals("36pt")) { out.print(" selected"); } %>>36</option>
                <option value="40pt"<% if(rec.getL1Tam().equals("40pt")) { out.print(" selected"); } %>>40</option>
                <option value="44pt"<% if(rec.getL1Tam().equals("44pt")) { out.print(" selected"); } %>>44</option>
                <option value="48pt"<% if(rec.getL1Tam().equals("48pt")) { out.print(" selected"); } %>>48</option>
                <option value="54pt"<% if(rec.getL1Tam().equals("54pt")) { out.print(" selected"); } %>>54</option>
                <option value="60pt"<% if(rec.getL1Tam().equals("60pt")) { out.print(" selected"); } %>>60</option>
                <option value="66pt"<% if(rec.getL1Tam().equals("66pt")) { out.print(" selected"); } %>>66</option>
                <option value="72pt"<% if(rec.getL1Tam().equals("72pt")) { out.print(" selected"); } %>>72</option>
                <option value="80pt"<% if(rec.getL1Tam().equals("80pt")) { out.print(" selected"); } %>>80</option>
                <option value="88pt"<% if(rec.getL1Tam().equals("88pt")) { out.print(" selected"); } %>>88</option>
                <option value="96pt"<% if(rec.getL1Tam().equals("96pt")) { out.print(" selected"); } %>>96</option>
              </select> &nbsp; <select name="l1_grosor" class="cpoBco">
                <option value="normal"<% if(rec.getL1Grosor().equals("normal")) { out.print(" selected"); } %>>normal</option>
                <option value="bold"<% if(rec.getL1Grosor().equals("bold")) { out.print(" selected"); } %>>negrita</option>
              </select> &nbsp; <select name="l1_estilo" class="cpoBco">
                <option value="normal"<% if(rec.getL1Estilo().equals("normal")) { out.print(" selected"); } %>>normal</option>
                <option value="italic"<% if(rec.getL1Estilo().equals("italic")) { out.print(" selected"); } %>>cursiva</option>
              </select> </td>
          </tr>
          <tr> 
            <td width="20%">Detalles Segundo Nivel:</td>
            <td colspan="3"> <select name="l2_fuente" class="cpoBco">
                <option value="Arial, Helvetica, sans-serif"<% if(rec.getL2Fuente().equals("Arial, Helvetica, sans-serif")) { out.print(" selected"); } %>>Arial, 
                Helvetica, sans-serif</option>
                <option value="Times New Roman, Times, serif"<% if(rec.getL2Fuente().equals("Times New Roman, Times, serif")) { out.print(" selected"); } %>>Times 
                New Roman, Times, serif</option>
                <option value="Courier New, Courier, mono"<% if(rec.getL2Fuente().equals("Courier New, Courier, mono")) { out.print(" selected"); } %>>Courier 
                New, Courier, mono</option>
                <option value="Georgia, Times New Roman, Times, serif"<% if(rec.getL2Fuente().equals("Georgia, Times New Roman, Times, serif")) { out.print(" selected"); } %>>Georgia, 
                Times New Roman, Times, serif</option>
                <option value="Verdana, Arial, Helvetica, sans-serif"<% if(rec.getL2Fuente().equals("Verdana, Arial, Helvetica, sans-serif")) { out.print(" selected"); } %>>Verdana, 
                Arial, Helvetica, sans-serif</option>
                <option value="Geneva, Arial, Helvetica, sans-serif"<% if(rec.getL2Fuente().equals("Geneva, Arial, Helvetica, sans-serif")) { out.print(" selected"); } %>>Geneva, 
                Arial, Helvetica, sans-serif</option>
              </select> &nbsp; <select name="l2_tam" class="cpoBco">
                <option value="4pt"<% if(rec.getL2Tam().equals("4pt")) { out.print(" selected"); } %>>4</option>
                <option value="5pt"<% if(rec.getL2Tam().equals("5pt")) { out.print(" selected"); } %>>5</option>
                <option value="6pt"<% if(rec.getL2Tam().equals("6pt")) { out.print(" selected"); } %>>6</option>
                <option value="7pt"<% if(rec.getL2Tam().equals("7pt")) { out.print(" selected"); } %>>7</option>
                <option value="8pt"<% if(rec.getL2Tam().equals("8pt")) { out.print(" selected"); } %>>8</option>
                <option value="9pt"<% if(rec.getL2Tam().equals("9pt")) { out.print(" selected"); } %>>9</option>
                <option value="10pt"<% if(rec.getL2Tam().equals("10pt")) { out.print(" selected"); } %>>10</option>
                <option value="11pt"<% if(rec.getL2Tam().equals("11pt")) { out.print(" selected"); } %>>11</option>
                <option value="12pt"<% if(rec.getL2Tam().equals("12pt")) { out.print(" selected"); } %>>12</option>
                <option value="13pt"<% if(rec.getL2Tam().equals("13pt")) { out.print(" selected"); } %>>13</option>
                <option value="14pt"<% if(rec.getL2Tam().equals("14pt")) { out.print(" selected"); } %>>14</option>
                <option value="15pt"<% if(rec.getL2Tam().equals("15pt")) { out.print(" selected"); } %>>15</option>
                <option value="16pt"<% if(rec.getL2Tam().equals("16pt")) { out.print(" selected"); } %>>16</option>
                <option value="18pt"<% if(rec.getL2Tam().equals("18pt")) { out.print(" selected"); } %>>18</option>
                <option value="20pt"<% if(rec.getL2Tam().equals("20pt")) { out.print(" selected"); } %>>20</option>
                <option value="22pt"<% if(rec.getL2Tam().equals("22pt")) { out.print(" selected"); } %>>22</option>
                <option value="24pt"<% if(rec.getL2Tam().equals("24pt")) { out.print(" selected"); } %>>24</option>
                <option value="26pt"<% if(rec.getL2Tam().equals("26pt")) { out.print(" selected"); } %>>26</option>
                <option value="28pt"<% if(rec.getL2Tam().equals("28pt")) { out.print(" selected"); } %>>28</option>
                <option value="32pt"<% if(rec.getL2Tam().equals("32pt")) { out.print(" selected"); } %>>32</option>
                <option value="36pt"<% if(rec.getL2Tam().equals("36pt")) { out.print(" selected"); } %>>36</option>
                <option value="40pt"<% if(rec.getL2Tam().equals("40pt")) { out.print(" selected"); } %>>40</option>
                <option value="44pt"<% if(rec.getL2Tam().equals("44pt")) { out.print(" selected"); } %>>44</option>
                <option value="48pt"<% if(rec.getL2Tam().equals("48pt")) { out.print(" selected"); } %>>48</option>
                <option value="54pt"<% if(rec.getL2Tam().equals("54pt")) { out.print(" selected"); } %>>54</option>
                <option value="60pt"<% if(rec.getL2Tam().equals("60pt")) { out.print(" selected"); } %>>60</option>
                <option value="66pt"<% if(rec.getL2Tam().equals("66pt")) { out.print(" selected"); } %>>66</option>
                <option value="72pt"<% if(rec.getL2Tam().equals("72pt")) { out.print(" selected"); } %>>72</option>
                <option value="80pt"<% if(rec.getL2Tam().equals("80pt")) { out.print(" selected"); } %>>80</option>
                <option value="88pt"<% if(rec.getL2Tam().equals("88pt")) { out.print(" selected"); } %>>88</option>
                <option value="96pt"<% if(rec.getL2Tam().equals("96pt")) { out.print(" selected"); } %>>96</option>
              </select> &nbsp; <select name="l2_grosor" class="cpoBco">
                <option value="normal"<% if(rec.getL2Grosor().equals("normal")) { out.print(" selected"); } %>>normal</option>
                <option value="bold"<% if(rec.getL2Grosor().equals("bold")) { out.print(" selected"); } %>>negrita</option>
              </select> &nbsp; <select name="l2_estilo" class="cpoBco">
                <option value="normal"<% if(rec.getL2Estilo().equals("normal")) { out.print(" selected"); } %>>normal</option>
                <option value="italic"<% if(rec.getL2Estilo().equals("italic")) { out.print(" selected"); } %>>cursiva</option>
              </select> </td>
          </tr>
          <tr> 
            <td width="20%">Detalles Tercer Nivel:</td>
            <td colspan="3"> <select name="l3_fuente" class="cpoBco">
                <option value="Arial, Helvetica, sans-serif"<% if(rec.getL3Fuente().equals("Arial, Helvetica, sans-serif")) { out.print(" selected"); } %>>Arial, 
                Helvetica, sans-serif</option>
                <option value="Times New Roman, Times, serif"<% if(rec.getL3Fuente().equals("Times New Roman, Times, serif")) { out.print(" selected"); } %>>Times 
                New Roman, Times, serif</option>
                <option value="Courier New, Courier, mono"<% if(rec.getL3Fuente().equals("Courier New, Courier, mono")) { out.print(" selected"); } %>>Courier 
                New, Courier, mono</option>
                <option value="Georgia, Times New Roman, Times, serif"<% if(rec.getL3Fuente().equals("Georgia, Times New Roman, Times, serif")) { out.print(" selected"); } %>>Georgia, 
                Times New Roman, Times, serif</option>
                <option value="Verdana, Arial, Helvetica, sans-serif"<% if(rec.getL3Fuente().equals("Verdana, Arial, Helvetica, sans-serif")) { out.print(" selected"); } %>>Verdana, 
                Arial, Helvetica, sans-serif</option>
                <option value="Geneva, Arial, Helvetica, sans-serif"<% if(rec.getL3Fuente().equals("Geneva, Arial, Helvetica, sans-serif")) { out.print(" selected"); } %>>Geneva, 
                Arial, Helvetica, sans-serif</option>
              </select> &nbsp; <select name="l3_tam" class="cpoBco">
                <option value="4pt"<% if(rec.getL3Tam().equals("4pt")) { out.print(" selected"); } %>>4</option>
                <option value="5pt"<% if(rec.getL3Tam().equals("5pt")) { out.print(" selected"); } %>>5</option>
                <option value="6pt"<% if(rec.getL3Tam().equals("6pt")) { out.print(" selected"); } %>>6</option>
                <option value="7pt"<% if(rec.getL3Tam().equals("7pt")) { out.print(" selected"); } %>>7</option>
                <option value="8pt"<% if(rec.getL3Tam().equals("8pt")) { out.print(" selected"); } %>>8</option>
                <option value="9pt"<% if(rec.getL3Tam().equals("9pt")) { out.print(" selected"); } %>>9</option>
                <option value="10pt"<% if(rec.getL3Tam().equals("10pt")) { out.print(" selected"); } %>>10</option>
                <option value="11pt"<% if(rec.getL3Tam().equals("11pt")) { out.print(" selected"); } %>>11</option>
                <option value="12pt"<% if(rec.getL3Tam().equals("12pt")) { out.print(" selected"); } %>>12</option>
                <option value="13pt"<% if(rec.getL3Tam().equals("13pt")) { out.print(" selected"); } %>>13</option>
                <option value="14pt"<% if(rec.getL3Tam().equals("14pt")) { out.print(" selected"); } %>>14</option>
                <option value="15pt"<% if(rec.getL3Tam().equals("15pt")) { out.print(" selected"); } %>>15</option>
                <option value="16pt"<% if(rec.getL3Tam().equals("16pt")) { out.print(" selected"); } %>>16</option>
                <option value="18pt"<% if(rec.getL3Tam().equals("18pt")) { out.print(" selected"); } %>>18</option>
                <option value="20pt"<% if(rec.getL3Tam().equals("20pt")) { out.print(" selected"); } %>>20</option>
                <option value="22pt"<% if(rec.getL3Tam().equals("22pt")) { out.print(" selected"); } %>>22</option>
                <option value="24pt"<% if(rec.getL3Tam().equals("24pt")) { out.print(" selected"); } %>>24</option>
                <option value="26pt"<% if(rec.getL3Tam().equals("26pt")) { out.print(" selected"); } %>>26</option>
                <option value="28pt"<% if(rec.getL3Tam().equals("28pt")) { out.print(" selected"); } %>>28</option>
                <option value="32pt"<% if(rec.getL3Tam().equals("32pt")) { out.print(" selected"); } %>>32</option>
                <option value="36pt"<% if(rec.getL3Tam().equals("36pt")) { out.print(" selected"); } %>>36</option>
                <option value="40pt"<% if(rec.getL3Tam().equals("40pt")) { out.print(" selected"); } %>>40</option>
                <option value="44pt"<% if(rec.getL3Tam().equals("44pt")) { out.print(" selected"); } %>>44</option>
                <option value="48pt"<% if(rec.getL3Tam().equals("48pt")) { out.print(" selected"); } %>>48</option>
                <option value="54pt"<% if(rec.getL3Tam().equals("54pt")) { out.print(" selected"); } %>>54</option>
                <option value="60pt"<% if(rec.getL3Tam().equals("60pt")) { out.print(" selected"); } %>>60</option>
                <option value="66pt"<% if(rec.getL3Tam().equals("66pt")) { out.print(" selected"); } %>>66</option>
                <option value="72pt"<% if(rec.getL3Tam().equals("72pt")) { out.print(" selected"); } %>>72</option>
                <option value="80pt"<% if(rec.getL3Tam().equals("80pt")) { out.print(" selected"); } %>>80</option>
                <option value="88pt"<% if(rec.getL3Tam().equals("88pt")) { out.print(" selected"); } %>>88</option>
                <option value="96pt"<% if(rec.getL3Tam().equals("96pt")) { out.print(" selected"); } %>>96</option>
              </select> &nbsp; <select name="l3_grosor" class="cpoBco">
                <option value="normal"<% if(rec.getL3Grosor().equals("normal")) { out.print(" selected"); } %>>normal</option>
                <option value="bold"<% if(rec.getL3Grosor().equals("bold")) { out.print(" selected"); } %>>negrita</option>
              </select> &nbsp; <select name="l3_estilo" class="cpoBco">
                <option value="normal"<% if(rec.getL3Estilo().equals("normal")) { out.print(" selected"); } %>>normal</option>
                <option value="italic"<% if(rec.getL3Estilo().equals("italic")) { out.print(" selected"); } %>>cursiva</option>
              </select> </td>
          </tr>
          <tr> 
            <td width="20%">Acumulado Primer Nivel:</td>
            <td colspan="3"> <select name="cl1_fuente" class="cpoBco">
                <option value="Arial, Helvetica, sans-serif"<% if(rec.getCL1Fuente().equals("Arial, Helvetica, sans-serif")) { out.print(" selected"); } %>>Arial, 
                Helvetica, sans-serif</option>
                <option value="Times New Roman, Times, serif"<% if(rec.getCL1Fuente().equals("Times New Roman, Times, serif")) { out.print(" selected"); } %>>Times 
                New Roman, Times, serif</option>
                <option value="Courier New, Courier, mono"<% if(rec.getCL1Fuente().equals("Courier New, Courier, mono")) { out.print(" selected"); } %>>Courier 
                New, Courier, mono</option>
                <option value="Georgia, Times New Roman, Times, serif"<% if(rec.getCL1Fuente().equals("Georgia, Times New Roman, Times, serif")) { out.print(" selected"); } %>>Georgia, 
                Times New Roman, Times, serif</option>
                <option value="Verdana, Arial, Helvetica, sans-serif"<% if(rec.getCL1Fuente().equals("Verdana, Arial, Helvetica, sans-serif")) { out.print(" selected"); } %>>Verdana, 
                Arial, Helvetica, sans-serif</option>
                <option value="Geneva, Arial, Helvetica, sans-serif"<% if(rec.getCL1Fuente().equals("Geneva, Arial, Helvetica, sans-serif")) { out.print(" selected"); } %>>Geneva, 
                Arial, Helvetica, sans-serif</option>
              </select> &nbsp; <select name="cl1_tam" class="cpoBco">
                <option value="4pt"<% if(rec.getCL1Tam().equals("4pt")) { out.print(" selected"); } %>>4</option>
                <option value="5pt"<% if(rec.getCL1Tam().equals("5pt")) { out.print(" selected"); } %>>5</option>
                <option value="6pt"<% if(rec.getCL1Tam().equals("6pt")) { out.print(" selected"); } %>>6</option>
                <option value="7pt"<% if(rec.getCL1Tam().equals("7pt")) { out.print(" selected"); } %>>7</option>
                <option value="8pt"<% if(rec.getCL1Tam().equals("8pt")) { out.print(" selected"); } %>>8</option>
                <option value="9pt"<% if(rec.getCL1Tam().equals("9pt")) { out.print(" selected"); } %>>9</option>
                <option value="10pt"<% if(rec.getCL1Tam().equals("10pt")) { out.print(" selected"); } %>>10</option>
                <option value="11pt"<% if(rec.getCL1Tam().equals("11pt")) { out.print(" selected"); } %>>11</option>
                <option value="12pt"<% if(rec.getCL1Tam().equals("12pt")) { out.print(" selected"); } %>>12</option>
                <option value="13pt"<% if(rec.getCL1Tam().equals("13pt")) { out.print(" selected"); } %>>13</option>
                <option value="14pt"<% if(rec.getCL1Tam().equals("14pt")) { out.print(" selected"); } %>>14</option>
                <option value="15pt"<% if(rec.getCL1Tam().equals("15pt")) { out.print(" selected"); } %>>15</option>
                <option value="16pt"<% if(rec.getCL1Tam().equals("16pt")) { out.print(" selected"); } %>>16</option>
                <option value="18pt"<% if(rec.getCL1Tam().equals("18pt")) { out.print(" selected"); } %>>18</option>
                <option value="20pt"<% if(rec.getCL1Tam().equals("20pt")) { out.print(" selected"); } %>>20</option>
                <option value="22pt"<% if(rec.getCL1Tam().equals("22pt")) { out.print(" selected"); } %>>22</option>
                <option value="24pt"<% if(rec.getCL1Tam().equals("24pt")) { out.print(" selected"); } %>>24</option>
                <option value="26pt"<% if(rec.getCL1Tam().equals("26pt")) { out.print(" selected"); } %>>26</option>
                <option value="28pt"<% if(rec.getCL1Tam().equals("28pt")) { out.print(" selected"); } %>>28</option>
                <option value="32pt"<% if(rec.getCL1Tam().equals("32pt")) { out.print(" selected"); } %>>32</option>
                <option value="36pt"<% if(rec.getCL1Tam().equals("36pt")) { out.print(" selected"); } %>>36</option>
                <option value="40pt"<% if(rec.getCL1Tam().equals("40pt")) { out.print(" selected"); } %>>40</option>
                <option value="44pt"<% if(rec.getCL1Tam().equals("44pt")) { out.print(" selected"); } %>>44</option>
                <option value="48pt"<% if(rec.getCL1Tam().equals("48pt")) { out.print(" selected"); } %>>48</option>
                <option value="54pt"<% if(rec.getCL1Tam().equals("54pt")) { out.print(" selected"); } %>>54</option>
                <option value="60pt"<% if(rec.getCL1Tam().equals("60pt")) { out.print(" selected"); } %>>60</option>
                <option value="66pt"<% if(rec.getCL1Tam().equals("66pt")) { out.print(" selected"); } %>>66</option>
                <option value="72pt"<% if(rec.getCL1Tam().equals("72pt")) { out.print(" selected"); } %>>72</option>
                <option value="80pt"<% if(rec.getCL1Tam().equals("80pt")) { out.print(" selected"); } %>>80</option>
                <option value="88pt"<% if(rec.getCL1Tam().equals("88pt")) { out.print(" selected"); } %>>88</option>
                <option value="96pt"<% if(rec.getCL1Tam().equals("96pt")) { out.print(" selected"); } %>>96</option>
              </select> &nbsp; <select name="cl1_grosor" class="cpoBco">
                <option value="normal"<% if(rec.getCL1Grosor().equals("normal")) { out.print(" selected"); } %>>normal</option>
                <option value="bold"<% if(rec.getCL1Grosor().equals("bold")) { out.print(" selected"); } %>>negrita</option>
              </select> &nbsp; <select name="cl1_estilo" class="cpoBco">
                <option value="normal"<% if(rec.getCL1Estilo().equals("normal")) { out.print(" selected"); } %>>normal</option>
                <option value="italic"<% if(rec.getCL1Estilo().equals("italic")) { out.print(" selected"); } %>>cursiva</option>
              </select> </td>
          </tr>
          <tr> 
            <td width="20%">Acumulado Segundo Nivel:</td>
            <td colspan="3"> <select name="cl2_fuente" class="cpoBco">
                <option value="Arial, Helvetica, sans-serif"<% if(rec.getCL2Fuente().equals("Arial, Helvetica, sans-serif")) { out.print(" selected"); } %>>Arial, 
                Helvetica, sans-serif</option>
                <option value="Times New Roman, Times, serif"<% if(rec.getCL2Fuente().equals("Times New Roman, Times, serif")) { out.print(" selected"); } %>>Times 
                New Roman, Times, serif</option>
                <option value="Courier New, Courier, mono"<% if(rec.getCL2Fuente().equals("Courier New, Courier, mono")) { out.print(" selected"); } %>>Courier 
                New, Courier, mono</option>
                <option value="Georgia, Times New Roman, Times, serif"<% if(rec.getCL2Fuente().equals("Georgia, Times New Roman, Times, serif")) { out.print(" selected"); } %>>Georgia, 
                Times New Roman, Times, serif</option>
                <option value="Verdana, Arial, Helvetica, sans-serif"<% if(rec.getCL2Fuente().equals("Verdana, Arial, Helvetica, sans-serif")) { out.print(" selected"); } %>>Verdana, 
                Arial, Helvetica, sans-serif</option>
                <option value="Geneva, Arial, Helvetica, sans-serif"<% if(rec.getCL2Fuente().equals("Geneva, Arial, Helvetica, sans-serif")) { out.print(" selected"); } %>>Geneva, 
                Arial, Helvetica, sans-serif</option>
              </select> &nbsp; <select name="cl2_tam" class="cpoBco">
                <option value="4pt"<% if(rec.getCL2Tam().equals("4pt")) { out.print(" selected"); } %>>4</option>
                <option value="5pt"<% if(rec.getCL2Tam().equals("5pt")) { out.print(" selected"); } %>>5</option>
                <option value="6pt"<% if(rec.getCL2Tam().equals("6pt")) { out.print(" selected"); } %>>6</option>
                <option value="7pt"<% if(rec.getCL2Tam().equals("7pt")) { out.print(" selected"); } %>>7</option>
                <option value="8pt"<% if(rec.getCL2Tam().equals("8pt")) { out.print(" selected"); } %>>8</option>
                <option value="9pt"<% if(rec.getCL2Tam().equals("9pt")) { out.print(" selected"); } %>>9</option>
                <option value="10pt"<% if(rec.getCL2Tam().equals("10pt")) { out.print(" selected"); } %>>10</option>
                <option value="11pt"<% if(rec.getCL2Tam().equals("11pt")) { out.print(" selected"); } %>>11</option>
                <option value="12pt"<% if(rec.getCL2Tam().equals("12pt")) { out.print(" selected"); } %>>12</option>
                <option value="13pt"<% if(rec.getCL2Tam().equals("13pt")) { out.print(" selected"); } %>>13</option>
                <option value="14pt"<% if(rec.getCL2Tam().equals("14pt")) { out.print(" selected"); } %>>14</option>
                <option value="15pt"<% if(rec.getCL2Tam().equals("15pt")) { out.print(" selected"); } %>>15</option>
                <option value="16pt"<% if(rec.getCL2Tam().equals("16pt")) { out.print(" selected"); } %>>16</option>
                <option value="18pt"<% if(rec.getCL2Tam().equals("18pt")) { out.print(" selected"); } %>>18</option>
                <option value="20pt"<% if(rec.getCL2Tam().equals("20pt")) { out.print(" selected"); } %>>20</option>
                <option value="22pt"<% if(rec.getCL2Tam().equals("22pt")) { out.print(" selected"); } %>>22</option>
                <option value="24pt"<% if(rec.getCL2Tam().equals("24pt")) { out.print(" selected"); } %>>24</option>
                <option value="26pt"<% if(rec.getCL2Tam().equals("26pt")) { out.print(" selected"); } %>>26</option>
                <option value="28pt"<% if(rec.getCL2Tam().equals("28pt")) { out.print(" selected"); } %>>28</option>
                <option value="32pt"<% if(rec.getCL2Tam().equals("32pt")) { out.print(" selected"); } %>>32</option>
                <option value="36pt"<% if(rec.getCL2Tam().equals("36pt")) { out.print(" selected"); } %>>36</option>
                <option value="40pt"<% if(rec.getCL2Tam().equals("40pt")) { out.print(" selected"); } %>>40</option>
                <option value="44pt"<% if(rec.getCL2Tam().equals("44pt")) { out.print(" selected"); } %>>44</option>
                <option value="48pt"<% if(rec.getCL2Tam().equals("48pt")) { out.print(" selected"); } %>>48</option>
                <option value="54pt"<% if(rec.getCL2Tam().equals("54pt")) { out.print(" selected"); } %>>54</option>
                <option value="60pt"<% if(rec.getCL2Tam().equals("60pt")) { out.print(" selected"); } %>>60</option>
                <option value="66pt"<% if(rec.getCL2Tam().equals("66pt")) { out.print(" selected"); } %>>66</option>
                <option value="72pt"<% if(rec.getCL2Tam().equals("72pt")) { out.print(" selected"); } %>>72</option>
                <option value="80pt"<% if(rec.getCL2Tam().equals("80pt")) { out.print(" selected"); } %>>80</option>
                <option value="88pt"<% if(rec.getCL2Tam().equals("88pt")) { out.print(" selected"); } %>>88</option>
                <option value="96pt"<% if(rec.getCL2Tam().equals("96pt")) { out.print(" selected"); } %>>96</option>
              </select> &nbsp; <select name="cl2_grosor" class="cpoBco">
                <option value="normal"<% if(rec.getCL2Grosor().equals("normal")) { out.print(" selected"); } %>>normal</option>
                <option value="bold"<% if(rec.getCL2Grosor().equals("bold")) { out.print(" selected"); } %>>negrita</option>
              </select> &nbsp; <select name="cl2_estilo" class="cpoBco">
                <option value="normal"<% if(rec.getCL2Estilo().equals("normal")) { out.print(" selected"); } %>>normal</option>
                <option value="italic"<% if(rec.getCL2Estilo().equals("italic")) { out.print(" selected"); } %>>cursiva</option>
              </select> </td>
          </tr>
          <tr> 
            <td width="20%">Acumulado Tercer Nivel:</td>
            <td colspan="3"> <select name="cl3_fuente" class="cpoBco">
                <option value="Arial, Helvetica, sans-serif"<% if(rec.getCL3Fuente().equals("Arial, Helvetica, sans-serif")) { out.print(" selected"); } %>>Arial, 
                Helvetica, sans-serif</option>
                <option value="Times New Roman, Times, serif"<% if(rec.getCL3Fuente().equals("Times New Roman, Times, serif")) { out.print(" selected"); } %>>Times 
                New Roman, Times, serif</option>
                <option value="Courier New, Courier, mono"<% if(rec.getCL3Fuente().equals("Courier New, Courier, mono")) { out.print(" selected"); } %>>Courier 
                New, Courier, mono</option>
                <option value="Georgia, Times New Roman, Times, serif"<% if(rec.getCL3Fuente().equals("Georgia, Times New Roman, Times, serif")) { out.print(" selected"); } %>>Georgia, 
                Times New Roman, Times, serif</option>
                <option value="Verdana, Arial, Helvetica, sans-serif"<% if(rec.getCL3Fuente().equals("Verdana, Arial, Helvetica, sans-serif")) { out.print(" selected"); } %>>Verdana, 
                Arial, Helvetica, sans-serif</option>
                <option value="Geneva, Arial, Helvetica, sans-serif"<% if(rec.getCL3Fuente().equals("Geneva, Arial, Helvetica, sans-serif")) { out.print(" selected"); } %>>Geneva, 
                Arial, Helvetica, sans-serif</option>
              </select> &nbsp; <select name="cl3_tam" class="cpoBco">
                <option value="4pt"<% if(rec.getCL3Tam().equals("4pt")) { out.print(" selected"); } %>>4</option>
                <option value="5pt"<% if(rec.getCL3Tam().equals("5pt")) { out.print(" selected"); } %>>5</option>
                <option value="6pt"<% if(rec.getCL3Tam().equals("6pt")) { out.print(" selected"); } %>>6</option>
                <option value="7pt"<% if(rec.getCL3Tam().equals("7pt")) { out.print(" selected"); } %>>7</option>
                <option value="8pt"<% if(rec.getCL3Tam().equals("8pt")) { out.print(" selected"); } %>>8</option>
                <option value="9pt"<% if(rec.getCL3Tam().equals("9pt")) { out.print(" selected"); } %>>9</option>
                <option value="10pt"<% if(rec.getCL3Tam().equals("10pt")) { out.print(" selected"); } %>>10</option>
                <option value="11pt"<% if(rec.getCL3Tam().equals("11pt")) { out.print(" selected"); } %>>11</option>
                <option value="12pt"<% if(rec.getCL3Tam().equals("12pt")) { out.print(" selected"); } %>>12</option>
                <option value="13pt"<% if(rec.getCL3Tam().equals("13pt")) { out.print(" selected"); } %>>13</option>
                <option value="14pt"<% if(rec.getCL3Tam().equals("14pt")) { out.print(" selected"); } %>>14</option>
                <option value="15pt"<% if(rec.getCL3Tam().equals("15pt")) { out.print(" selected"); } %>>15</option>
                <option value="16pt"<% if(rec.getCL3Tam().equals("16pt")) { out.print(" selected"); } %>>16</option>
                <option value="18pt"<% if(rec.getCL3Tam().equals("18pt")) { out.print(" selected"); } %>>18</option>
                <option value="20pt"<% if(rec.getCL3Tam().equals("20pt")) { out.print(" selected"); } %>>20</option>
                <option value="22pt"<% if(rec.getCL3Tam().equals("22pt")) { out.print(" selected"); } %>>22</option>
                <option value="24pt"<% if(rec.getCL3Tam().equals("24pt")) { out.print(" selected"); } %>>24</option>
                <option value="26pt"<% if(rec.getCL3Tam().equals("26pt")) { out.print(" selected"); } %>>26</option>
                <option value="28pt"<% if(rec.getCL3Tam().equals("28pt")) { out.print(" selected"); } %>>28</option>
                <option value="32pt"<% if(rec.getCL3Tam().equals("32pt")) { out.print(" selected"); } %>>32</option>
                <option value="36pt"<% if(rec.getCL3Tam().equals("36pt")) { out.print(" selected"); } %>>36</option>
                <option value="40pt"<% if(rec.getCL3Tam().equals("40pt")) { out.print(" selected"); } %>>40</option>
                <option value="44pt"<% if(rec.getCL3Tam().equals("44pt")) { out.print(" selected"); } %>>44</option>
                <option value="48pt"<% if(rec.getCL3Tam().equals("48pt")) { out.print(" selected"); } %>>48</option>
                <option value="54pt"<% if(rec.getCL3Tam().equals("54pt")) { out.print(" selected"); } %>>54</option>
                <option value="60pt"<% if(rec.getCL3Tam().equals("60pt")) { out.print(" selected"); } %>>60</option>
                <option value="66pt"<% if(rec.getCL3Tam().equals("66pt")) { out.print(" selected"); } %>>66</option>
                <option value="72pt"<% if(rec.getCL3Tam().equals("72pt")) { out.print(" selected"); } %>>72</option>
                <option value="80pt"<% if(rec.getCL3Tam().equals("80pt")) { out.print(" selected"); } %>>80</option>
                <option value="88pt"<% if(rec.getCL3Tam().equals("88pt")) { out.print(" selected"); } %>>88</option>
                <option value="96pt"<% if(rec.getCL3Tam().equals("96pt")) { out.print(" selected"); } %>>96</option>
              </select> &nbsp; <select name="cl3_grosor" class="cpoBco">
                <option value="normal"<% if(rec.getCL3Grosor().equals("normal")) { out.print(" selected"); } %>>normal</option>
                <option value="bold"<% if(rec.getCL3Grosor().equals("bold")) { out.print(" selected"); } %>>negrita</option>
              </select> &nbsp; <select name="cl3_estilo" class="cpoBco">
                <option value="normal"<% if(rec.getCL3Estilo().equals("normal")) { out.print(" selected"); } %>>normal</option>
                <option value="italic"<% if(rec.getCL3Estilo().equals("italic")) { out.print(" selected"); } %>>cursiva</option>
              </select> </td>
          </tr>
        </table>
	</td>
  </tr>
  <tr>
  	  <td align="right">&nbsp;</td>
  </tr>
  <tr>
  	  <td>&nbsp;</td>
  </tr>
  <tr>
  	<td>
		<table width="100%" border="0" cellspacing="2" cellpadding="0">
          <tr>
			<td width="20%" valign="top">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
                  <th nowrap><%= rec.getNumTablas() %> Tablas:</th>
  </tr>
  <tr>
    <td>
	<select onChange="javascript:establecerProcesoSVE(this.form.subproceso, 'CAMBIO_TABLA'); this.form.submit();" name="tablas" size="20" class="cpoCol" style="width: 100%;">
<%
	for(int i = 0; i < rec.getNumTablas(); i++)
	{
%>
              <option value="<%= rec.getNombreTabla(i) %>"<%  if(rec.getTabla().equals(rec.getNombreTabla(i))) { out.print(" selected"); } %>><%= rec.getNombreTabla(i) %></option>
<%	
	}
%>    
	</select></td>
  </tr>
  <tr>
                  <th nowrap><%= rec.getNumColumnas() %> Columnas: </th>
  </tr>
  <tr>
    <td>
	<select name="columnas" size="20" class="cpoCol" style="width: 100%;">
<%
	for(int i = 0; i < rec.getNumColumnas(); i++)
	{
%>
              <option value="<%= rec.getNombreColumna(i) %>"<%  if(rec.getColumna().equals(rec.getNombreColumna(i))) { out.print(" selected"); } %>><%= rec.getDescColumna(i) %></option>
<%	
	}
%>                        
	</select></td>
  </tr>
</table>

				
            </td>
			<td width="80%" valign="top"> 
              <!--div id="cyclelinks"></div-->
		<p>
              <table width="100%" border="0" cellspacing="2" cellpadding="0">
                <tr> 
                  <td width="20%">Tabulador</td>
                  <td width="40%"><input name="tabprintpntL1" type="text" size="12" maxlength="10" value="<%=  rec.getTabPrintPntL1() %>"> 
                    &nbsp;pixeles</td>
                  <td width="40%" align="right"> 
                    <input onClick="javascript:establecerProcesoSVE(this.form.subproceso, 'PROBARL1')" name="PROBARL1" type="submit" value="Probar">
                  </td>
                </tr>
                <tr> 
                  <td>Codigo principal</td>
                  <td class="titChicoNar"><%= rec.getDescStatusL1() %></td>
                  <td>&nbsp;</td>
                </tr>
                <tr> 
                  <td colspan="3">
				   <table width="100%" border="0" cellspacing="2" cellpadding="0">
                      <tr>
						<td width="85%">
							<textarea name="select_clauseL1" rows="10" style="width: 100%;"><%=  rec.getSCL1() %></textarea></td>
						<td valign="top" class="titChicoNar">
<%
	for(int i = 0; i < rec.getNumPrep("L1"); i++)
	{
%>
		<%= rec.getPrepPart("L1", i).getNombre() %><br>
                          <input style="width: 85%;" type="text" name="FSI_L1_<%= rec.getPrepPart("L1", i).getNombre() %>" value="<%= rec.getPrepPart("L1", i).getValor() %>">
<%
	}
%>
    					  </td>
					  </tr>
					</table>
				  </td>
                </tr>
				<tr> 
                  <td colspan="3" class="titChicoRj">
<%
	String SCL1 = (String)request.getAttribute("SCL1");
	if(SCL1 == null)
		out.println("&nbsp;");
	else
		out.println(SCL1);
%>				  
				  </td>
                </tr>
				<tr> 
                  <td width="20%">Tabulador</td>
                  <td width="40%"><input name="tabprintpntCL1" type="text" size="12" maxlength="10" value="<%=  rec.getTabPrintPntCL1() %>"> 
                    &nbsp;pixeles</td>
                  <td width="40%">&nbsp;</td>
                </tr>
                <tr> 
                  <td>Codigo principal</td>
                  <td class="titChicoNar"><%= rec.getDescStatusCL1() %></td>
                  <td>&nbsp;</td>
                </tr>
                <tr> 
                  <td colspan="3"><textarea name="select_clauseCL1" rows="5" style="width: 100%;"><%=  rec.getCSCL1() %></textarea></td>
                </tr>
				<tr> 
                  <td colspan="3">
<%
	String CSCL1 = (String)request.getAttribute("CSCL1");
	if(CSCL1 == null)
		out.println("&nbsp;");
	else
		out.println(CSCL1);
%>				  
				  </td>
                </tr>				
              </table>
 		</p>
		<p>
              <table width="100%" border="0" cellspacing="2" cellpadding="0">
                <tr> 
                  <td width="20%">Tabulador</td>
                  <td width="40%"><input name="tabprintpntL2" type="text" size="12" maxlength="10" value="<%=  rec.getTabPrintPntL2() %>"> 
                    &nbsp;pixeles</td>
                  <td width="40%" align="right"> 
                    <input onClick="javascript:establecerProcesoSVE(this.form.subproceso, 'PROBARL2')" name="PROBARL2" type="submit" value="Probar"></td>
                </tr>
                <tr> 
                  <td>Codigo principal</td>
                  <td class="titChicoNar"><%= rec.getDescStatusL2() %></td>
                  <td>&nbsp;</td>
                </tr>
                <tr> 
                  <td colspan="3">
				  <table width="100%" border="0" cellspacing="2" cellpadding="0">
                      <tr>
						<td width="85%">
							<textarea name="select_clauseL2" rows="10" style="width: 100%;"><%=  rec.getSCL2() %></textarea></td>
						<td valign="top" class="titChicoNar">
<%
	for(int i = 0; i < rec.getNumPrep("L2"); i++)
	{
%>
		<%= rec.getPrepPart("L2", i).getNombre() %><br>
                          <input style="width: 85%;" type="text" name="FSI_L2_<%= rec.getPrepPart("L2", i).getNombre() %>" value="<%= rec.getPrepPart("L2", i).getValor() %>">
<%
	}
%>
    					  </td>
					  </tr>
					</table>
				  </td>
                </tr>
				<tr> 
                  <td colspan="3">
<%
	String SCL2 = (String)request.getAttribute("SCL2");
	if(SCL2 == null)
		out.println("&nbsp;");
	else
		out.println(SCL2);
%>				  
				  </td>
                </tr>
				<tr> 
                  <td width="20%">Tabulador</td>
                  <td width="40%"><input name="tabprintpntCL2" type="text" size="12" maxlength="10" value="<%=  rec.getTabPrintPntCL2() %>"> 
                    &nbsp;pixeles</td>
                  <td width="40%">&nbsp;</td>
                </tr>
                <tr> 
                  <td>Codigo principal</td>
                  <td class="titChicoNar"><%= rec.getDescStatusCL2() %></td>
                  <td>&nbsp;</td>
                </tr>
                <tr> 
                  <td colspan="3"><textarea name="select_clauseCL2" rows="5" style="width: 100%;"><%=  rec.getCSCL2() %></textarea></td>
                </tr>
				<tr> 
                  <td colspan="3">
<%
	String CSCL2 = (String)request.getAttribute("CSCL2");
	if(CSCL2 == null)
		out.println("&nbsp;");
	else
		out.println(CSCL2);
%>				  
				  </td>
                </tr>				
              </table>
		</p>
		<p class="dropcontent">
              <table width="100%" border="0" cellspacing="2" cellpadding="0">
                <tr> 
                  <td width="20%">Tabulador</td>
                  <td width="40%"><input name="tabprintpntL3" type="text" size="12" maxlength="10" value="<%=  rec.getTabPrintPntL3() %>"> 
                    &nbsp;pixeles</td>
                  <td width="40%" align="right"> 
                    <input onClick="javascript:establecerProcesoSVE(this.form.subproceso, 'PROBARL3')" name="PROBARL3" type="submit" value="Probar"> 
                  </td>
                </tr>
                <tr> 
                  <td>Codigo principal</td>
                  <td class="titChicoNar"><%= rec.getDescStatusL3() %></td>
                  <td>&nbsp;</td>
                </tr>
                <tr> 
                  <td colspan="3">
				  <table width="100%" border="0" cellspacing="2" cellpadding="0">
                      <tr>
						<td width="85%">
							<textarea name="select_clauseL3" rows="10" style="width: 100%;"><%=  rec.getSCL3() %></textarea></td>
						<td valign="top" class="titChicoNar">
<%
	for(int i = 0; i < rec.getNumPrep("L3"); i++)
	{
%>
		<%= rec.getPrepPart("L3", i).getNombre() %><br>
                          <input style="width: 85%;" type="text" name="FSI_L3_<%= rec.getPrepPart("L3", i).getNombre() %>" value="<%= rec.getPrepPart("L3", i).getValor() %>">
<%
	}
%>
    					  </td>
					  </tr>
					</table>
				  </td>
                </tr>
				<tr> 
                  <td colspan="3">
<%
	String SCL3 = (String)request.getAttribute("SCL3");
	if(SCL3 == null)
		out.println("&nbsp;");
	else
		out.println(SCL3);
%>				  
				  </td>
                </tr>
				<tr> 
                  <td width="20%">Tabulador</td>
                  <td width="40%"><input name="tabprintpntCL3" type="text" size="12" maxlength="10" value="<%=  rec.getTabPrintPntCL3() %>"> 
                    &nbsp;pixeles</td>
                  <td width="40%">&nbsp;</td>
                </tr>
                <tr> 
                  <td>Codigo principal</td>
                  <td class="titChicoNar"><%= rec.getDescStatusCL3() %></td>
                  <td>&nbsp;</td>
                </tr>
                <tr> 
                  <td colspan="3"><textarea name="select_clauseCL3" rows="5" style="width: 100%;"><%=  rec.getCSCL3() %></textarea></td>
                </tr>
                <tr> 
                  <td colspan="3">
<%
	String CSCL3 = (String)request.getAttribute("CSCL3");
	if(CSCL3 == null)
		out.println("&nbsp;");
	else
		out.println(CSCL3);
%>
                  </td>
                </tr>
              </table>
		</p>
		<p>
		      <table width="100%" border="0" cellspacing="2" cellpadding="0">
<%	
	for(int j = 1; j <=6; j++)
	{
		String L = "", Enc = "";
		switch(j)
		{
			case 1:		L = "L1";
				Enc = "Detalles del primer nivel";
				break;
			case 2:		L = "CL1";
				Enc = "Acumulado del primer nivel";
				break;
			case 3:		L = "L2";
				Enc = "Detalles del segundo nivel";
				break;				
			case 4:		L = "CL2";
				Enc = "Acumulado del segundo nivel";
				break;				
			case 5:		L = "L3";
				Enc = "Detalles del tercer nivel";
				break;
			case 6:		L = "CL3";
				Enc = "Acumulado del tercer nivel";
				break;
			default:
				Enc = "";
				break;				
		}	
	
		if(rec.getNumCols(L) > 0)
		{
%>
                <tr> 
                  <td colspan="6" class="titChicoNar"><%= Enc + ": " + rec.getNumCols(L) + " Columnas" %></td>
                </tr>
                <tr> 
                  <td class="cpoColNg">Nombre</td>
                  <td class="cpoColNg">Tipo</td>
                  <td class="cpoColNg">Invisible</td>
                  <td class="cpoColNg">Formato</td>
                  <td class="cpoColNg">Ancho (%)</td>
                  <td class="cpoColNg">Alineaci&oacute;n</td>
                </tr>
<%
			for(int i=0; i < rec.getNumCols(L); i++)
			{
%>
				<tr> 
                  <td><%= rec.getColsPart(L,i).getColName() %></td>
                  <td><input name="FSI_BDT_<%= L + "_" +  rec.getColsPart(L,i).getColName() %>" type="hidden" value="<%= rec.getColsPart(L,i).getBindDataType() %>"><%= rec.getColsPart(L,i).getBindDataType() %></td>
                  <td><input name="FSI_WS_<%= L + "_" +  rec.getColsPart(L,i).getColName() %>" type="checkbox"<%= ((rec.getColsPart(L,i).getWillShow()) ? " checked" : "") %>></td>
                  <td>
				  	<select name="FSI_FMT_<%= L + "_" +  rec.getColsPart(L,i).getColName() %>" class="cpoBco">
<%
					if(rec.getColsPart(L,i).getBindDataType().equals("BYTE") || rec.getColsPart(L,i).getBindDataType().equals("INT"))
					{
%>
					  <option value=" |0"<% if(rec.getColsPart(L,i).getFormat().equals(" |0")) { out.print(" selected"); } %>>#### - 0</option>
                      <option value=" |1"<% if(rec.getColsPart(L,i).getFormat().equals(" |1")) { out.print(" selected"); } %>>#### - _</option>
					  <option value=",|0"<% if(rec.getColsPart(L,i).getFormat().equals(",|0")) { out.print(" selected"); } %>>#,### - 0</option>
                      <option value=",|1"<% if(rec.getColsPart(L,i).getFormat().equals(",|1")) { out.print(" selected"); } %>>#,### - _</option>
<%		
					}
					else if(rec.getColsPart(L,i).getBindDataType().equals("STRING"))
					{
%>
					  <option value="general"<% if(rec.getColsPart(L,i).getFormat().equals("general")) { out.print(" selected"); } %>>Toda la cadena</option>
                      <option value="cuenta"<% if(rec.getColsPart(L,i).getFormat().equals("cuenta")) { out.print(" selected"); } %>>Cuenta contable</option>
 <%				
					}
					else if(rec.getColsPart(L,i).getBindDataType().equals("DECIMAL") || rec.getColsPart(L,i).getBindDataType().equals("MONEY"))
					{
%>
					  <option value=",|.|0|0"<% if(rec.getColsPart(L,i).getFormat().equals(",|.|0|0")) { out.print(" selected"); } %>>#,### - 0</option>
                      <option value=",|.|1|0"<% if(rec.getColsPart(L,i).getFormat().equals(",|.|1|0")) { out.print(" selected"); } %>>#,###.# -  0</option>
 					  <option value=",|.|2|0"<% if(rec.getColsPart(L,i).getFormat().equals(",|.|2|0")) { out.print(" selected"); } %>>#,###.## - 0</option>
                      <option value=",|.|3|0"<% if(rec.getColsPart(L,i).getFormat().equals(",|.|3|0")) { out.print(" selected"); } %>>#,###.### - 0</option>
					  <option value=",|.|4|0"<% if(rec.getColsPart(L,i).getFormat().equals(",|.|4|0")) { out.print(" selected"); } %>>#,###.#### -  0</option>
 					  <option value=",|.|5|0"<% if(rec.getColsPart(L,i).getFormat().equals(",|.|5|0")) { out.print(" selected"); } %>>#,###.##### - 0</option>
                      <option value=",|.|6|0"<% if(rec.getColsPart(L,i).getFormat().equals(",|.|6|0")) { out.print(" selected"); } %>>#,###.###### - 0</option>
					  <option value=",|.|0|1"<% if(rec.getColsPart(L,i).getFormat().equals(",|.|0|1")) { out.print(" selected"); } %>>#,### - _</option>
                      <option value=",|.|1|1"<% if(rec.getColsPart(L,i).getFormat().equals(",|.|1|1")) { out.print(" selected"); } %>>#,###.# -  _</option>
 					  <option value=",|.|2|1"<% if(rec.getColsPart(L,i).getFormat().equals(",|.|2|1")) { out.print(" selected"); } %>>#,###.## - _</option>
                      <option value=",|.|3|1"<% if(rec.getColsPart(L,i).getFormat().equals(",|.|3|1")) { out.print(" selected"); } %>>#,###.### - _</option>
					  <option value=",|.|4|1"<% if(rec.getColsPart(L,i).getFormat().equals(",|.|4|1")) { out.print(" selected"); } %>>#,###.#### - _</option>
 					  <option value=",|.|5|1"<% if(rec.getColsPart(L,i).getFormat().equals(",|.|5|1")) { out.print(" selected"); } %>>#,###.##### - _</option>
                      <option value=",|.|6|1"<% if(rec.getColsPart(L,i).getFormat().equals(",|.|6|1")) { out.print(" selected"); } %>>#,###.###### - _</option>
 <%				
					}
					else if(rec.getColsPart(L,i).getBindDataType().equals("TIME"))
					{
%>
					  <option value="dd/MM/yy"<% if(rec.getColsPart(L,i).getFormat().equals("dd/MM/yy")) { out.print(" selected"); } %>>dd/mm/aa</option>
                      <option value="dd/MM/yyyy"<% if(rec.getColsPart(L,i).getFormat().equals("dd/MM/yyyy")) { out.print(" selected"); } %>>dd/mm/aaaa</option>
                      <option value="dd/MMM/yy"<% if(rec.getColsPart(L,i).getFormat().equals("dd/MMM/yy")) { out.print(" selected"); } %>>dd/mmm/aa</option>
                      <option value="dd/MMM/yyyy"<% if(rec.getColsPart(L,i).getFormat().equals("dd/MMM/yyyy")) { out.print(" selected"); } %>>dd/mmm/aaaa</option>
<%				
					}
					else if(rec.getColsPart(L,i).getBindDataType().equals("BOOL"))
					{
%>
					  <option value="VER_FAL"<% if(rec.getColsPart(L,i).getFormat().equals("VER_FAL")) { out.print(" selected"); } %>>Verdadero / Falso</option>
                      <option value="V_F"<% if(rec.getColsPart(L,i).getFormat().equals("V_F")) { out.print(" selected"); } %>>V / F</option>
                      <option value="SI_NO"<% if(rec.getColsPart(L,i).getFormat().equals("SI_NO")) { out.print(" selected"); } %>>SI / NO</option>
                      <option value="1_0"<% if(rec.getColsPart(L,i).getFormat().equals("1_0")) { out.print(" selected"); } %>>1 / 0</option>
<%
					}
					else
					{
%>
	                   <option value=""<% if(rec.getColsPart(L,i).getFormat().equals("")) { out.print(" selected"); } %>>INDEFINIDO</option>
<%					
					}
%>
				  	</select>	
				  	</td>
                  <td><input name="FSI_ANC_<%= L + "_" +  rec.getColsPart(L,i).getColName() %>" type="text" value="<%= rec.getColsPart(L,i).getAncho() %>" size="7" maxlength="5"></td>
                  <td>
				    <select name="FSI_ALH_<%= L + "_" +  rec.getColsPart(L,i).getColName() %>" class="cpoBco">
                      <option value="left"<% if(rec.getColsPart(L,i).getAlinHor().equals("left")) { out.print(" selected"); } %>>Izquerda</option>
                      <option value="center"<% if(rec.getColsPart(L,i).getAlinHor().equals("center")) { out.print(" selected"); } %>>Centrado</option>
                      <option value="right"<% if(rec.getColsPart(L,i).getAlinHor().equals("right")) { out.print(" selected"); } %>>Derecha</option>
                    </select>
					</td>
                </tr>
 <%      
 			}
%> 
	            <tr> 
                  <td>&nbsp;</td>
                  <td>&nbsp;</td>
                  <td>&nbsp;</td>
                  <td>&nbsp;</td>
                  <td class="titChicoNeg"><%= rec.getAnchoCols(L) %></td>
                  <td align="right"><input onClick="javascript:establecerProcesoSVE(this.form.subproceso, 'ACTUALIZAR<%=L%>')" name="ACTUALIZAR<%=L%>" type="submit" value="Actualizar"></td>
                </tr>
 <%             
		}
	}
%>
	        </table>
		</p>
		<p>
		      <table width="100%" border="0" cellspacing="2" cellpadding="0">
<%
	if(rec.getNumFiltros() > 0)
	{	
		JListasCatalogosSet cats = new  JListasCatalogosSet(request);
		cats.ConCat(true);
		cats.m_Where = "AplRep = '1'";
		cats.m_OrderBy = "ID_Catalogo ASC";
		cats.Open();
%>
				<tr> 
                  <td colspan="7" class="titChicoNar">Filtro para este reporte</td>
                </tr>
                <tr class="cpoColNg"> 
                  <td>Instrucciones</td>
                  <td>Tipo</td>
                  <td>Nombre Uno</td>
                  <td>Por defecto Uno</td>
                  <td>Nombre Dos</td>
                  <td>Por defecto Dos</td>
                  <td>Catalogo</td>
                </tr>
<%
		for(int i=0; i < rec.getNumFiltros(); i++)
		{
%>
                <tr> 
                  <td><input name="FIL_INS_<%= rec.getFiltro(i).getPriDataName() %>" type="text" value="<%= rec.getFiltro(i).getInstructions() %>" size="25" maxlength="254"></td>
                  <td>
				    <select name="FIL_BDT_<%= rec.getFiltro(i).getPriDataName() %>" class="cpoBco">
					  <option value="BYTE"<% if(rec.getFiltro(i).getBindDataType().equals("BYTE")) { out.print(" selected"); } %>>BYTE</option>
                      <option value="INT"<% if(rec.getFiltro(i).getBindDataType().equals("INT")) { out.print(" selected"); } %>>INT</option>
                	  <option value="STRING"<% if(rec.getFiltro(i).getBindDataType().equals("STRING")) { out.print(" selected"); } %>>STRING</option>
					   <option value="DECIMAL"<% if(rec.getFiltro(i).getBindDataType().equals("DECIMAL")) { out.print(" selected"); } %>>DECIMAL</option>
                       <option value="MONEY"<% if(rec.getFiltro(i).getBindDataType().equals("MONEY")) { out.print(" selected"); } %>>MONEY</option>
                       <option value="TIME"<% if(rec.getFiltro(i).getBindDataType().equals("TIME")) { out.print(" selected"); } %>>TIME</option>
                       <option value="BOOL"<% if(rec.getFiltro(i).getBindDataType().equals("BOOL")) { out.print(" selected"); } %>>BOOL</option>
					</select></td>
                  <td><%= rec.getFiltro(i).getPriDataName() %></td>
                  <td><input name="FIL_PDF_<%= rec.getFiltro(i).getPriDataName() %>" type="text" size="25" maxlength="1000" value="<%= rec.getFiltro(i).getPriDefault() %>"<% if(rec.getFiltro(i).getFromCatalog()) { out.print(" readonly='true'"); } %>></td>
                  <td><%= rec.getFiltro(i).getSecDataName() %></td>
                  <td><input name="FIL_SDF_<%= rec.getFiltro(i).getPriDataName() %>" type="text" size="25" maxlength="1000" value="<%= rec.getFiltro(i).getSecDefault() %>"<% if(rec.getFiltro(i).getFromCatalog()) { out.print(" readonly='true'"); } %>></td>
                  <td><select name="FIL_IDC_<%= rec.getFiltro(i).getPriDataName() %>" class="cpoBco">
                      <option value="0"<% if(!rec.getFiltro(i).getFromCatalog()) { out.print(" selected"); } %>>------------ 
                      NINGUNO ----------------</option>
<%
			for(int c = 0; c< cats.getNumRows(); c++)
			{
%>
				      <option value="<%= cats.getAbsRow(c).getID_Catalogo() %>"<% if(rec.getFiltro(i).getID_Catalogo()  == cats.getAbsRow(c).getID_Catalogo()) { out.print(" selected"); } %>><%= cats.getAbsRow(c).getNombre() %></option>
<%
			}
%>
                   </select></td>
                </tr>
<%
		}
%>
				<tr> 
                  <td colspan="7" align="right"><input onClick="javascript:establecerProcesoSVE(this.form.subproceso, 'ACTUALIZARFIL')" name="ACTUALIZARFIL" type="submit" value="Actualizar"></td>
                </tr>
<%
	}
%>
              </table>
		</p>
		<!--div id="cyclelinks2" align="right"></div>
		<br-->
		
			</td>
		  </tr>
		</table>
		
	</td>
  </tr>
  <tr> 
    <td>&nbsp;</td>
  </tr>
</table>
</form>
</body>
</html>
