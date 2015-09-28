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
<%@ page import="forseti.*, forseti.admon.*, forseti.sets.*, java.util.*, java.io.*"%>
<%
	String adm_formatos_dlg = (String)request.getAttribute("adm_formatos_dlg");
	if(adm_formatos_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}
	
	String titulo = JUtil.getSesion(request).getSesion("ADM_FORMATOS").generarTitulo(JUtil.Msj("CEF","ADM_FORMATOS","VISTA",request.getParameter("proceso"),3),"../../forsetidoc/040202.html");
	
	session = request.getSession(true);
    JAdmFormatosSes rec = (JAdmFormatosSes)session.getAttribute("adm_formatos_dlg");
	
	JFormatosTiposSet set = new JFormatosTiposSet(request);      
	set.ConCat(true);
	set.Open();
	
	String cols = JUtil.Msj("CEF","ADM_FORMATOS","DLG","COLUMNAS");
	String etq = JUtil.Msj("CEF","ADM_FORMATOS","DLG","ETQ3",5);
%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.1//EN" "http://www.wapforum.org/DTD/xhtml-mobile11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"> 
<head> 
<meta http-equiv="Content-Type" content="application/xhtml+xml; charset=UTF-8"/> 
<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=5.0"/>
<script language="JavaScript" type="text/javascript" src="../../compfsi/comps.js">
</script>
<script language="JavaScript" type="text/javascript" src="../../compfsi/staticbar.js">
</script>
<script language="JavaScript" type="text/javascript">
<!--
function limpiarFormulario()
{
	document.adm_formatos_dlg.pos_x.value = "";
	document.adm_formatos_dlg.pos_y.value = "";
	document.adm_formatos_dlg.alt.value = "";
	document.adm_formatos_dlg.anc.value = "";
	document.adm_formatos_dlg.hor.selectedIndex = 0;
	document.adm_formatos_dlg.ver.selectedIndex = 0;
}

function limpiarFormularioDET()
{
	document.adm_formatos_dlg.pos_x_det.value = "";
	document.adm_formatos_dlg.pos_y_det.value = "";
	document.adm_formatos_dlg.alt_det.value = "";
	document.adm_formatos_dlg.anc_det.value = "";
	document.adm_formatos_dlg.hor_det.selectedIndex = 0;
	document.adm_formatos_dlg.ver_det.selectedIndex = 0;
}

function enviarlo(formAct)
{
	if(formAct.subproceso.value == "AGR_PART" || formAct.subproceso.value == "EDIT_PART")
	{
		if(	!esNumeroDecimal("<%= JUtil.Elm(cols,3) %>", formAct.pos_x.value, 0, 999.99, 2) ||
			!esNumeroDecimal("<%= JUtil.Elm(cols,4) %>", formAct.pos_y.value, 0, 999.99, 2) ||
			!esNumeroDecimal("<%= JUtil.Elm(cols,5) %>", formAct.alt.value, 0, 999.99, 2) ||
		   	!esNumeroDecimal("<%= JUtil.Elm(cols,6) %>", formAct.anc.value, 0, 999.99, 2))
			return false;
		else
			return true;
	}
	else if(formAct.subproceso.value == "AGR_PART_DET" || formAct.subproceso.value == "EDIT_PART_DET")
	{
		if(	!esNumeroDecimal("<%= JUtil.Elm(cols,3) %>", formAct.pos_x_det.value, 0, 999.99, 2) ||
			!esNumeroDecimal("<%= JUtil.Elm(cols,4) %>", formAct.pos_y_det.value, 0, 999.99, 2) ||
			!esNumeroDecimal("<%= JUtil.Elm(cols,5) %>", formAct.alt_det.value, 0, 999.99, 2) ||
		   	!esNumeroDecimal("<%= JUtil.Elm(cols,6) %>", formAct.anc_det.value, 0, 999.99, 2))
			return false;
		else
			return true;
	}
	else if(formAct.subproceso.value == "ENVIAR")
	{	
		if(confirm("<%= JUtil.Msj("GLB","GLB","GLB","CONFIRMACION") %>"))
		{
			formAct.aceptar.disabled = true;
			return true;
		}
		else
		{
			return false;
		}
	} 
	else
	{
		return true;
	}

}
-->
</script>
<link href="../compfsi/estilos.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#333333" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form onSubmit="return enviarlo(this)" action="/servlet/CEFAdmFormatosDlg" method="post" enctype="application/x-www-form-urlencoded" name="adm_formatos_dlg" target="_self">
<div id="topbar"> 
<table width="100%" border="0" cellspacing="0" cellpadding="5" bgcolor="#333333">
  <tr>
    <td class="titCuerpoBco" align="center" valign="middle" bgcolor="#0099FF"><%= JUtil.Msj("GLB","GLB","GLB","CEF") %></td>
  </tr>
  <tr>
    <td>
		<table width="100%" border="0" cellspacing="10" cellpadding="0">
		  <tr>
			<td width="50%" align="left" valign="middle">
              <%  if(JUtil.getSesion(request).getID_Mensaje() == 0 || request.getParameter("proceso").equals("CONSULTAR_FORMATO")) { %>
        			<input type="submit" name="aceptar" disabled="true" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>"/>
        			<%  } else { %>
        			<input type="submit" name="aceptar" onClick="javascript:establecerProcesoSVE(this.form.subproceso, 'ENVIAR')" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>"/>
       				<%  } %>
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/CEFAdmFormatosCtrl'" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>"/></td>
  		  	<td width="50%" align="right" valign="middle">
				<a href="/servlet/CEFRegistro"><img src="../imgfsi/inicio.png" title="<%= JUtil.Msj("GLB","GLB","GLB","HERRAMIENTAS",1) %>" width="24" height="24" border="0" /></a><img src="../imgfsi/pixel_333333.gif" width="5" height="24"/>
				<a href="/servlet/CEFSalir"><img src="../imgfsi/cerrar_sesion.png" title="<%= JUtil.Msj("GLB","GLB","GLB","HERRAMIENTAS",2) + " " + JUtil.getSesion(request).getNombreUsuario() %>" width="24" height="24" border="0"/></a><img src="../imgfsi/pixel_333333.gif" width="5" height="24"/>
				<a href="../../forsetidoc/040205.html"><img src="../imgfsi/ayudacef.png" title="<%= JUtil.Msj("GLB","GLB","GLB","HERRAMIENTAS",3) %>" width="24" height="24" border="0"/></a></td>
  		  </tr>
		</table>
	</td>
  </tr>
  <tr> 
    <td align="center" class="titCuerpoAzc"><%= titulo %></td>
  </tr>
</table>
</div>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
 	  <td height="150" bgcolor="#333333">&nbsp;</td>
	</tr>
<%	
	String mensaje = JUtil.getMensaje(request, response);	
	out.println(mensaje);
	//out.print(JUtil.depurarParametros(request));
%>
  <tr> 
    <td  bgcolor="#FFFFFF"> 
	    <table width="100%" border="0" cellspacing="5" cellpadding="5">
          <tr> 
            <td width="50%"> 
			<input name="proceso" type="hidden" value="<%= request.getParameter("proceso") %>"/> 
              <input name="FMTID" type="hidden" value="<%= request.getParameter("FMTID") %>"/> 
              <input name="subproceso" type="hidden" value="<%= request.getParameter("subproceso") %>"/>
			   <input type="hidden" name="idpartida" value="<%= request.getParameter("idpartida") %>"/>
              <%= JUtil.Msj("GLB","GLB","GLB","CLAVE") %></td>
            <td width="50%"><%= JUtil.Msj("GLB","GLB","GLB","TIPO") %></td>
          </tr>
		  <tr>
		  	<td><input class="cpoColAzc" name="idformato" type="text" style="width:50%" maxlength="7" value="<%=  rec.getID_Formato() %>"<% if(request.getParameter("proceso").equals("CAMBIAR_FORMATO")) { out.print(" readonly='true'"); } %>/></td>
            <td> 
			  <select style="width:100%" onChange="javascript:establecerProcesoSVE(this.form.subproceso, 'CAMBIO_TIPO'); this.form.submit();" name="tipo">
<%
	for(int i = 0; i < set.getNumRows(); i++)
	{
%>
                <option value="<%= set.getAbsRow(i).getID_Tipo() %>"<%  if(rec.getTipo().equals(set.getAbsRow(i).getID_Tipo())) { out.print(" selected"); } %>><%= set.getAbsRow(i).getDescripcion() %></option>
<%	
	}
%>
              </select>
			  </td>
          </tr>
          <tr> 
            <td colspan="2"><%= JUtil.Msj("GLB","GLB","GLB","DESCRIPCION") %></td>
          </tr>
          <tr> 
            <td colspan="2"><input name="descripcion" type="text" style="width:100%" maxlength="80" value="<%=  rec.getDescripcion() %>"/></td>
          </tr>
          <tr> 
            <td colspan="2"><%= JUtil.Msj("CEF","ADM_FORMATOS","DLG","ETQ",1) %></td>
          </tr>
          <tr> 
            <td> <input name="ventana_hw" type="text" id="ventana_hw" style="width:50%" maxlength="10" value="<%=  rec.getVentanaHW() %>"/>
              &nbsp;<%= JUtil.Msj("CEF","ADM_FORMATOS","DLG","ETQ",2) %></td>
            <td> <input name="ventana_vw" type="text" id="ventana_vw" style="width:50%" maxlength="10" value="<%=  rec.getVentanaVW() %>"/> 
              &nbsp;<%= JUtil.Msj("CEF","ADM_FORMATOS","DLG","ETQ",3) %></td>
          </tr>
          <tr> 
            <td><%= JUtil.Msj("CEF","ADM_FORMATOS","DLG","ETQ",4) %></td>
            <td><input name="cab" type="text" id="cab" style="width:50%" maxlength="10" value="<%=  rec.getCab() %>"/>
              &nbsp;<%= JUtil.Msj("CEF","ADM_FORMATOS","DLG","ETQ",5) %></td>
          </tr>
          <tr> 
            <td colspan="2"><%= JUtil.Msj("CEF","ADM_FORMATOS","DLG","ETQ2",1) %></td>
          </tr>
          <tr> 
            <td><input name="det_ini" type="text" id="det_ini" style="width:50%" maxlength="10" value="<%=  rec.getDetIni() %>"/>
              &nbsp;<%= JUtil.Msj("CEF","ADM_FORMATOS","DLG","ETQ2",2) %></td> 
            <td><input name="det_alt" type="text" id="det_alt" style="width:50%" maxlength="10" value="<%=  rec.getDetAlt() %>"/>
              &nbsp;<%= JUtil.Msj("CEF","ADM_FORMATOS","DLG","ETQ2",3) %></td>
          </tr>
          <tr> 
            <td valign="top"><%= JUtil.Msj("CEF","ADM_FORMATOS","DLG","ETQ2",4) %></td>
            <td><input name="det_num" type="text" id="det_num" style="width:50%" maxlength="10" value="<%=  rec.getDetNum() %>">
              &nbsp;<%= JUtil.Msj("CEF","ADM_FORMATOS","DLG","ETQ2",5) %></td>
          </tr>
          <tr> 
            <td colspan="2"><%= JUtil.Msj("CEF","ADM_FORMATOS","DLG","ETQ3",1) %></td>
          </tr>
          <tr> 
            <td colspan="2"> <select style="width:90%" name="titulo_fuente" class="cpoBco">
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
              </select></td>
	      </tr>
          <tr> 
            <td colspan="2">
			  <table width="100%"><tr><td width="30%"><select style="width:90%" name="titulo_tam" class="cpoBco">
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
              </select></td>
                  <td width="35%"><select style="width:90%" name="titulo_grosor" class="cpoBco">
                      <option value="normal"<% if(rec.getTituloGrosor().equals("normal")) { out.print(" selected"); } %>>
                      <%= JUtil.Msj("CEF","ADM_FORMATOS","DLG","ETQ4",1) %>
                      </option>
                      <option value="bold"<% if(rec.getTituloGrosor().equals("bold")) { out.print(" selected"); } %>>
                      <%= JUtil.Msj("CEF","ADM_FORMATOS","DLG","ETQ4",2) %>
                      </option>
                    </select></td>
                  <td><select style="width:90%" name="titulo_estilo" class="cpoBco">
                      <option value="normal"<% if(rec.getTituloEstilo().equals("normal")) { out.print(" selected"); } %>>
                      <%= JUtil.Msj("CEF","ADM_FORMATOS","DLG","ETQ4",1) %>
                      </option>
                      <option value="italic"<% if(rec.getTituloEstilo().equals("italic")) { out.print(" selected"); } %>>
                      <%= JUtil.Msj("CEF","ADM_FORMATOS","DLG","ETQ4",3) %>
                      </option>
                    </select> </td>
                </tr></table></td>
          </tr>
          <tr> 
            <td colspan="2"><%= JUtil.Msj("CEF","ADM_FORMATOS","DLG","ETQ3",2) %></td>
          </tr>
          <tr> 
            <td colspan="2"> <select style="width:90%" name="etiqueta_fuente" class="cpoBco">
                <option value="Arial, Helvetica, sans-serif"<% if(rec.getEtiquetaFuente().equals("Arial, Helvetica, sans-serif")) { out.print(" selected"); } %>>Arial, 
                Helvetica, sans-serif</option>
                <option value="Times New Roman, Times, serif"<% if(rec.getEtiquetaFuente().equals("Times New Roman, Times, serif")) { out.print(" selected"); } %>>Times 
                New Roman, Times, serif</option>
                <option value="Courier New, Courier, mono"<% if(rec.getEtiquetaFuente().equals("Courier New, Courier, mono")) { out.print(" selected"); } %>>Courier 
                New, Courier, mono</option>
                <option value="Georgia, Times New Roman, Times, serif"<% if(rec.getEtiquetaFuente().equals("Georgia, Times New Roman, Times, serif")) { out.print(" selected"); } %>>Georgia, 
                Times New Roman, Times, serif</option>
                <option value="Verdana, Arial, Helvetica, sans-serif"<% if(rec.getEtiquetaFuente().equals("Verdana, Arial, Helvetica, sans-serif")) { out.print(" selected"); } %>>Verdana, 
                Arial, Helvetica, sans-serif</option>
                <option value="Geneva, Arial, Helvetica, sans-serif"<% if(rec.getEtiquetaFuente().equals("Geneva, Arial, Helvetica, sans-serif")) { out.print(" selected"); } %>>Geneva, 
                Arial, Helvetica, sans-serif</option>
              </select></td>
		  </tr>
          <tr> 
            <td colspan="2">
			  <table width="100%"><tr><td width="30%"><select style="width:90%" name="etiqueta_tam" class="cpoBco">
                <option value="4pt"<% if(rec.getEtiquetaTam().equals("4pt")) { out.print(" selected"); } %>>4</option>
                <option value="5pt"<% if(rec.getEtiquetaTam().equals("5pt")) { out.print(" selected"); } %>>5</option>
                <option value="6pt"<% if(rec.getEtiquetaTam().equals("6pt")) { out.print(" selected"); } %>>6</option>
                <option value="7pt"<% if(rec.getEtiquetaTam().equals("7pt")) { out.print(" selected"); } %>>7</option>
                <option value="8pt"<% if(rec.getEtiquetaTam().equals("8pt")) { out.print(" selected"); } %>>8</option>
                <option value="9pt"<% if(rec.getEtiquetaTam().equals("9pt")) { out.print(" selected"); } %>>9</option>
                <option value="10pt"<% if(rec.getEtiquetaTam().equals("10pt")) { out.print(" selected"); } %>>10</option>
                <option value="11pt"<% if(rec.getEtiquetaTam().equals("11pt")) { out.print(" selected"); } %>>11</option>
                <option value="12pt"<% if(rec.getEtiquetaTam().equals("12pt")) { out.print(" selected"); } %>>12</option>
                <option value="13pt"<% if(rec.getEtiquetaTam().equals("13pt")) { out.print(" selected"); } %>>13</option>
                <option value="14pt"<% if(rec.getEtiquetaTam().equals("14pt")) { out.print(" selected"); } %>>14</option>
                <option value="15pt"<% if(rec.getEtiquetaTam().equals("15pt")) { out.print(" selected"); } %>>15</option>
                <option value="16pt"<% if(rec.getEtiquetaTam().equals("16pt")) { out.print(" selected"); } %>>16</option>
                <option value="18pt"<% if(rec.getEtiquetaTam().equals("18pt")) { out.print(" selected"); } %>>18</option>
                <option value="20pt"<% if(rec.getEtiquetaTam().equals("20pt")) { out.print(" selected"); } %>>20</option>
                <option value="22pt"<% if(rec.getEtiquetaTam().equals("22pt")) { out.print(" selected"); } %>>22</option>
                <option value="24pt"<% if(rec.getEtiquetaTam().equals("24pt")) { out.print(" selected"); } %>>24</option>
                <option value="26pt"<% if(rec.getEtiquetaTam().equals("26pt")) { out.print(" selected"); } %>>26</option>
                <option value="28pt"<% if(rec.getEtiquetaTam().equals("28pt")) { out.print(" selected"); } %>>28</option>
                <option value="32pt"<% if(rec.getEtiquetaTam().equals("32pt")) { out.print(" selected"); } %>>32</option>
                <option value="36pt"<% if(rec.getEtiquetaTam().equals("36pt")) { out.print(" selected"); } %>>36</option>
                <option value="40pt"<% if(rec.getEtiquetaTam().equals("40pt")) { out.print(" selected"); } %>>40</option>
                <option value="44pt"<% if(rec.getEtiquetaTam().equals("44pt")) { out.print(" selected"); } %>>44</option>
                <option value="48pt"<% if(rec.getEtiquetaTam().equals("48pt")) { out.print(" selected"); } %>>48</option>
                <option value="54pt"<% if(rec.getEtiquetaTam().equals("54pt")) { out.print(" selected"); } %>>54</option>
                <option value="60pt"<% if(rec.getEtiquetaTam().equals("60pt")) { out.print(" selected"); } %>>60</option>
                <option value="66pt"<% if(rec.getEtiquetaTam().equals("66pt")) { out.print(" selected"); } %>>66</option>
                <option value="72pt"<% if(rec.getEtiquetaTam().equals("72pt")) { out.print(" selected"); } %>>72</option>
                <option value="80pt"<% if(rec.getEtiquetaTam().equals("80pt")) { out.print(" selected"); } %>>80</option>
                <option value="88pt"<% if(rec.getEtiquetaTam().equals("88pt")) { out.print(" selected"); } %>>88</option>
                <option value="96pt"<% if(rec.getEtiquetaTam().equals("96pt")) { out.print(" selected"); } %>>96</option>
              </select></td><td width="35%"><select style="width:90%" name="etiqueta_grosor" class="cpoBco">
                <option value="normal"<% if(rec.getEtiquetaGrosor().equals("normal")) { out.print(" selected"); } %>><%= JUtil.Msj("CEF","ADM_FORMATOS","DLG","ETQ4",1) %></option>
                <option value="bold"<% if(rec.getEtiquetaGrosor().equals("bold")) { out.print(" selected"); } %>><%= JUtil.Msj("CEF","ADM_FORMATOS","DLG","ETQ4",2) %></option>
              </select></td><td><select style="width:90%" name="etiqueta_estilo" class="cpoBco">
                <option value="normal"<% if(rec.getEtiquetaEstilo().equals("normal")) { out.print(" selected"); } %>><%= JUtil.Msj("CEF","ADM_FORMATOS","DLG","ETQ4",1) %></option>
                <option value="italic"<% if(rec.getEtiquetaEstilo().equals("italic")) { out.print(" selected"); } %>><%= JUtil.Msj("CEF","ADM_FORMATOS","DLG","ETQ4",3) %></option>
              </select> </td></tr></table></td>
          </tr>
          <tr> 
            <td colspan="2"><%= JUtil.Msj("CEF","ADM_FORMATOS","DLG","ETQ3",3) %></td>
          </tr>
          <tr> 
            <td colspan="2"> <select style="width:90%" name="cabecero_fuente" class="cpoBco">
                <option value="Arial, Helvetica, sans-serif"<% if(rec.getCabeceroFuente().equals("Arial, Helvetica, sans-serif")) { out.print(" selected"); } %>>Arial, 
                Helvetica, sans-serif</option>
                <option value="Times New Roman, Times, serif"<% if(rec.getCabeceroFuente().equals("Times New Roman, Times, serif")) { out.print(" selected"); } %>>Times 
                New Roman, Times, serif</option>
                <option value="Courier New, Courier, mono"<% if(rec.getCabeceroFuente().equals("Courier New, Courier, mono")) { out.print(" selected"); } %>>Courier 
                New, Courier, mono</option>
                <option value="Georgia, Times New Roman, Times, serif"<% if(rec.getCabeceroFuente().equals("Georgia, Times New Roman, Times, serif")) { out.print(" selected"); } %>>Georgia, 
                Times New Roman, Times, serif</option>
                <option value="Verdana, Arial, Helvetica, sans-serif"<% if(rec.getCabeceroFuente().equals("Verdana, Arial, Helvetica, sans-serif")) { out.print(" selected"); } %>>Verdana, 
                Arial, Helvetica, sans-serif</option>
                <option value="Geneva, Arial, Helvetica, sans-serif"<% if(rec.getCabeceroFuente().equals("Geneva, Arial, Helvetica, sans-serif")) { out.print(" selected"); } %>>Geneva, 
                Arial, Helvetica, sans-serif</option>				
              </select></td>
			</tr>
			<tr>
			  <td colspan="2">
			    <table width="100%"><tr><td width="30%"><select style="width:90%" name="cabecero_tam" class="cpoBco">
                <option value="4pt"<% if(rec.getCabeceroTam().equals("4pt")) { out.print(" selected"); } %>>4</option>
                <option value="5pt"<% if(rec.getCabeceroTam().equals("5pt")) { out.print(" selected"); } %>>5</option>
                <option value="6pt"<% if(rec.getCabeceroTam().equals("6pt")) { out.print(" selected"); } %>>6</option>
                <option value="7pt"<% if(rec.getCabeceroTam().equals("7pt")) { out.print(" selected"); } %>>7</option>
                <option value="8pt"<% if(rec.getCabeceroTam().equals("8pt")) { out.print(" selected"); } %>>8</option>
                <option value="9pt"<% if(rec.getCabeceroTam().equals("9pt")) { out.print(" selected"); } %>>9</option>
                <option value="10pt"<% if(rec.getCabeceroTam().equals("10pt")) { out.print(" selected"); } %>>10</option>
                <option value="11pt"<% if(rec.getCabeceroTam().equals("11pt")) { out.print(" selected"); } %>>11</option>
                <option value="12pt"<% if(rec.getCabeceroTam().equals("12pt")) { out.print(" selected"); } %>>12</option>
                <option value="13pt"<% if(rec.getCabeceroTam().equals("13pt")) { out.print(" selected"); } %>>13</option>
                <option value="14pt"<% if(rec.getCabeceroTam().equals("14pt")) { out.print(" selected"); } %>>14</option>
                <option value="15pt"<% if(rec.getCabeceroTam().equals("15pt")) { out.print(" selected"); } %>>15</option>
                <option value="16pt"<% if(rec.getCabeceroTam().equals("16pt")) { out.print(" selected"); } %>>16</option>
                <option value="18pt"<% if(rec.getCabeceroTam().equals("18pt")) { out.print(" selected"); } %>>18</option>
                <option value="20pt"<% if(rec.getCabeceroTam().equals("20pt")) { out.print(" selected"); } %>>20</option>
                <option value="22pt"<% if(rec.getCabeceroTam().equals("22pt")) { out.print(" selected"); } %>>22</option>
                <option value="24pt"<% if(rec.getCabeceroTam().equals("24pt")) { out.print(" selected"); } %>>24</option>
                <option value="26pt"<% if(rec.getCabeceroTam().equals("26pt")) { out.print(" selected"); } %>>26</option>
                <option value="28pt"<% if(rec.getCabeceroTam().equals("28pt")) { out.print(" selected"); } %>>28</option>
                <option value="32pt"<% if(rec.getCabeceroTam().equals("32pt")) { out.print(" selected"); } %>>32</option>
                <option value="36pt"<% if(rec.getCabeceroTam().equals("36pt")) { out.print(" selected"); } %>>36</option>
                <option value="40pt"<% if(rec.getCabeceroTam().equals("40pt")) { out.print(" selected"); } %>>40</option>
                <option value="44pt"<% if(rec.getCabeceroTam().equals("44pt")) { out.print(" selected"); } %>>44</option>
                <option value="48pt"<% if(rec.getCabeceroTam().equals("48pt")) { out.print(" selected"); } %>>48</option>
                <option value="54pt"<% if(rec.getCabeceroTam().equals("54pt")) { out.print(" selected"); } %>>54</option>
                <option value="60pt"<% if(rec.getCabeceroTam().equals("60pt")) { out.print(" selected"); } %>>60</option>
                <option value="66pt"<% if(rec.getCabeceroTam().equals("66pt")) { out.print(" selected"); } %>>66</option>
                <option value="72pt"<% if(rec.getCabeceroTam().equals("72pt")) { out.print(" selected"); } %>>72</option>
                <option value="80pt"<% if(rec.getCabeceroTam().equals("80pt")) { out.print(" selected"); } %>>80</option>
                <option value="88pt"<% if(rec.getCabeceroTam().equals("88pt")) { out.print(" selected"); } %>>88</option>
                <option value="96pt"<% if(rec.getCabeceroTam().equals("96pt")) { out.print(" selected"); } %>>96</option>
              </select></td><td width="35%"><select style="width:90%" name="cabecero_grosor" class="cpoBco">
                <option value="normal"<% if(rec.getCabeceroGrosor().equals("normal")) { out.print(" selected"); } %>><%= JUtil.Msj("CEF","ADM_FORMATOS","DLG","ETQ4",1) %></option>
                <option value="bold"<% if(rec.getCabeceroGrosor().equals("bold")) { out.print(" selected"); } %>><%= JUtil.Msj("CEF","ADM_FORMATOS","DLG","ETQ4",2) %></option>
              </select></td><td><select style="width:90%" name="cabecero_estilo" class="cpoBco">
                <option value="normal"<% if(rec.getCabeceroEstilo().equals("normal")) { out.print(" selected"); } %>><%= JUtil.Msj("CEF","ADM_FORMATOS","DLG","ETQ4",1) %></option>
                <option value="italic"<% if(rec.getCabeceroEstilo().equals("italic")) { out.print(" selected"); } %>><%= JUtil.Msj("CEF","ADM_FORMATOS","DLG","ETQ4",3) %></option>
              </select></td></tr></table></td>
          </tr>
          <tr> 
            <td colspan="2"><%= JUtil.Msj("CEF","ADM_FORMATOS","DLG","ETQ3",4) %></td>
          </tr>
          <tr> 
            <td colspan="2"> <select style="width:90%" name="detalle_fuente" class="cpoBco">
                <option value="Arial, Helvetica, sans-serif"<% if(rec.getDetalleFuente().equals("Arial, Helvetica, sans-serif")) { out.print(" selected"); } %>>Arial, 
                Helvetica, sans-serif</option>
                <option value="Times New Roman, Times, serif"<% if(rec.getDetalleFuente().equals("Times New Roman, Times, serif")) { out.print(" selected"); } %>>Times 
                New Roman, Times, serif</option>
                <option value="Courier New, Courier, mono"<% if(rec.getDetalleFuente().equals("Courier New, Courier, mono")) { out.print(" selected"); } %>>Courier 
                New, Courier, mono</option>
                <option value="Georgia, Times New Roman, Times, serif"<% if(rec.getDetalleFuente().equals("Georgia, Times New Roman, Times, serif")) { out.print(" selected"); } %>>Georgia, 
                Times New Roman, Times, serif</option>
                <option value="Verdana, Arial, Helvetica, sans-serif"<% if(rec.getDetalleFuente().equals("Verdana, Arial, Helvetica, sans-serif")) { out.print(" selected"); } %>>Verdana, 
                Arial, Helvetica, sans-serif</option>
                <option value="Geneva, Arial, Helvetica, sans-serif"<% if(rec.getDetalleFuente().equals("Geneva, Arial, Helvetica, sans-serif")) { out.print(" selected"); } %>>Geneva, 
                Arial, Helvetica, sans-serif</option>
              </select></td>
		  </tr>
          <tr> 
          	<td colspan="2">
			  <table width="100%"><tr><td width="30%"><select style="width:90%" name="detalle_tam" class="cpoBco">
                <option value="4pt"<% if(rec.getDetalleTam().equals("4pt")) { out.print(" selected"); } %>>4</option>
                <option value="5pt"<% if(rec.getDetalleTam().equals("5pt")) { out.print(" selected"); } %>>5</option>
                <option value="6pt"<% if(rec.getDetalleTam().equals("6pt")) { out.print(" selected"); } %>>6</option>
                <option value="7pt"<% if(rec.getDetalleTam().equals("7pt")) { out.print(" selected"); } %>>7</option>
                <option value="8pt"<% if(rec.getDetalleTam().equals("8pt")) { out.print(" selected"); } %>>8</option>
                <option value="9pt"<% if(rec.getDetalleTam().equals("9pt")) { out.print(" selected"); } %>>9</option>
                <option value="10pt"<% if(rec.getDetalleTam().equals("10pt")) { out.print(" selected"); } %>>10</option>
                <option value="11pt"<% if(rec.getDetalleTam().equals("11pt")) { out.print(" selected"); } %>>11</option>
                <option value="12pt"<% if(rec.getDetalleTam().equals("12pt")) { out.print(" selected"); } %>>12</option>
                <option value="13pt"<% if(rec.getDetalleTam().equals("13pt")) { out.print(" selected"); } %>>13</option>
                <option value="14pt"<% if(rec.getDetalleTam().equals("14pt")) { out.print(" selected"); } %>>14</option>
                <option value="15pt"<% if(rec.getDetalleTam().equals("15pt")) { out.print(" selected"); } %>>15</option>
                <option value="16pt"<% if(rec.getDetalleTam().equals("16pt")) { out.print(" selected"); } %>>16</option>
                <option value="18pt"<% if(rec.getDetalleTam().equals("18pt")) { out.print(" selected"); } %>>18</option>
                <option value="20pt"<% if(rec.getDetalleTam().equals("20pt")) { out.print(" selected"); } %>>20</option>
                <option value="22pt"<% if(rec.getDetalleTam().equals("22pt")) { out.print(" selected"); } %>>22</option>
                <option value="24pt"<% if(rec.getDetalleTam().equals("24pt")) { out.print(" selected"); } %>>24</option>
                <option value="26pt"<% if(rec.getDetalleTam().equals("26pt")) { out.print(" selected"); } %>>26</option>
                <option value="28pt"<% if(rec.getDetalleTam().equals("28pt")) { out.print(" selected"); } %>>28</option>
                <option value="32pt"<% if(rec.getDetalleTam().equals("32pt")) { out.print(" selected"); } %>>32</option>
                <option value="36pt"<% if(rec.getDetalleTam().equals("36pt")) { out.print(" selected"); } %>>36</option>
                <option value="40pt"<% if(rec.getDetalleTam().equals("40pt")) { out.print(" selected"); } %>>40</option>
                <option value="44pt"<% if(rec.getDetalleTam().equals("44pt")) { out.print(" selected"); } %>>44</option>
                <option value="48pt"<% if(rec.getDetalleTam().equals("48pt")) { out.print(" selected"); } %>>48</option>
                <option value="54pt"<% if(rec.getDetalleTam().equals("54pt")) { out.print(" selected"); } %>>54</option>
                <option value="60pt"<% if(rec.getDetalleTam().equals("60pt")) { out.print(" selected"); } %>>60</option>
                <option value="66pt"<% if(rec.getDetalleTam().equals("66pt")) { out.print(" selected"); } %>>66</option>
                <option value="72pt"<% if(rec.getDetalleTam().equals("72pt")) { out.print(" selected"); } %>>72</option>
                <option value="80pt"<% if(rec.getDetalleTam().equals("80pt")) { out.print(" selected"); } %>>80</option>
                <option value="88pt"<% if(rec.getDetalleTam().equals("88pt")) { out.print(" selected"); } %>>88</option>
                <option value="96pt"<% if(rec.getDetalleTam().equals("96pt")) { out.print(" selected"); } %>>96</option>
              </select></td><td width="35%"><select style="width:90%" name="detalle_grosor" class="cpoBco">
                <option value="normal"<% if(rec.getDetalleGrosor().equals("normal")) { out.print(" selected"); } %>><%= JUtil.Msj("CEF","ADM_FORMATOS","DLG","ETQ4",1) %></option>
                <option value="bold"<% if(rec.getDetalleGrosor().equals("bold")) { out.print(" selected"); } %>><%= JUtil.Msj("CEF","ADM_FORMATOS","DLG","ETQ4",2) %></option>
              </select></td><td><select style="width:90%" name="detalle_estilo" class="cpoBco">
                <option value="normal"<% if(rec.getDetalleEstilo().equals("normal")) { out.print(" selected"); } %>><%= JUtil.Msj("CEF","ADM_FORMATOS","DLG","ETQ4",1) %></option>
                <option value="italic"<% if(rec.getDetalleEstilo().equals("italic")) { out.print(" selected"); } %>><%= JUtil.Msj("CEF","ADM_FORMATOS","DLG","ETQ4",3) %></option>
              </select></td></tr></table></td>
          </tr>
          <tr>
		    <td colspan="2">
			
			
			  <table width="100%">
				<tr>	 
            	  <td colspan="2" class="titChicoAzc"><%= JUtil.Elm(cols,1) %></td>
                  <td colspan="2" class="titChicoAzc"><%= JUtil.Elm(cols,2) %></td>
            	</tr>
				<tr>
				  <td width="25%" align="right" class="titChicoAzc"><%= JUtil.Elm(cols,3) %></td>
            	  <td width="25%" align="right" class="titChicoAzc"><%= JUtil.Elm(cols,4) %></td>
                  <td width="25%" align="right" class="titChicoAzc"><%= JUtil.Elm(cols,5) %></td>
            	  <td align="right" class="titChicoAzc"><%= JUtil.Elm(cols,6) %></td>
            	</tr>
				<tr>
				  <td align="right" class="titChicoAzc"><%= JUtil.Elm(cols,7) %></td>
				  <td align="right" class="titChicoAzc"><%= JUtil.Elm(cols,8) %></td>
				  <td colspan="2" class="titChicoAzc"><%= JUtil.Elm(cols,9) %></td>
	           </tr>
<%
		if( !request.getParameter("proceso").equals("CONSULTAR_FORMATO") )
		{
%>
          <tr> 
            <td colspan="2"> <select style="width:90%" onChange="javascript:establecerProcesoSVE(this.form.subproceso, 'CAMBIO_ETQCAB'); this.form.submit();" name="etiqueta" class="cpoBco">
                <option value="FSI_ETIQUETA"<% if(rec.getEtiquetaCab().equals("FSI_ETIQUETA")) { out.print(" selected"); } %>><%= JUtil.Elm(etq,1) %></option>
                <option value="FSI_TITULO"<% if(rec.getEtiquetaCab().equals("FSI_TITULO")) { out.print(" selected"); } %>><%= JUtil.Elm(etq,2) %></option>
                <option value="FSI_LH"<% if(rec.getEtiquetaCab().equals("FSI_LH")) { out.print(" selected"); } %>><%= JUtil.Elm(etq,3) %></option>
                <% 
			for(int i = 0; i < rec.getNumColumnasCab(); i++)
			{
%>
                <option value="<%= (rec.getColumnaCab(i)).getNombreCol() %>"<% if(rec.getEtiquetaCab().equals((rec.getColumnaCab(i)).getNombreCol() )) { out.print(" selected"); } %>>
                <%= (rec.getColumnaCab(i)).getNombreCol()  %>
                </option>
                <%	
			}       
%>
              </select></td>
            <td colspan="2">
				
<%
					if(rec.getTipoColCab().equals("ESP"))
					{
%>
					<input name="valor" type="text" id="valor" class="cpoBco" style="width:100%" maxlength="254" value="<%= rec.getValorValCab() %>"/>
<%					
					}
					else if(rec.getTipoColCab().equals("BYTE") || rec.getTipoColCab().equals("INT"))
					{
%>
					<select style="width:90%" name="valor" class="cpoBco">
					  <option value=" |0"<% if(rec.getValorValCab().equals(" |0")) { out.print(" selected"); } %>>#### - 0</option>
                      <option value=" |1"<% if(rec.getValorValCab().equals(" |1")) { out.print(" selected"); } %>>#### - _</option>
					  <option value=",|0"<% if(rec.getValorValCab().equals(",|0")) { out.print(" selected"); } %>>#,### - 0</option>
                      <option value=",|1"<% if(rec.getValorValCab().equals(",|1")) { out.print(" selected"); } %>>#,### - _</option>
					</select>
<%		
					}
					else if(rec.getTipoColCab().equals("STRING"))
					{
%>
					<select style="width:90%" name="valor" class="cpoBco">
					  <option value="general"<% if(rec.getValorValCab().equals("general")) { out.print(" selected"); } %>><%= JUtil.Elm(etq,4) %></option>
                      <option value="cuenta"<% if(rec.getValorValCab().equals("cuenta")) { out.print(" selected"); } %>><%= JUtil.Elm(etq,5) %></option>
 					</select>
 <%				
					}
					else if(rec.getTipoColCab().equals("DECIMAL") || rec.getTipoColCab().equals("MONEY"))
					{
%>
					<select style="width:90%" name="valor" class="cpoBco">
					  <option value=",|.|0|0"<% if(rec.getValorValCab().equals(",|.|0|0")) { out.print(" selected"); } %>>#,### - 0</option>
                      <option value=",|.|1|0"<% if(rec.getValorValCab().equals(",|.|1|0")) { out.print(" selected"); } %>>#,###.# -  0</option>
 					  <option value=",|.|2|0"<% if(rec.getValorValCab().equals(",|.|2|0")) { out.print(" selected"); } %>>#,###.## - 0</option>
                      <option value=",|.|3|0"<% if(rec.getValorValCab().equals(",|.|3|0")) { out.print(" selected"); } %>>#,###.### - 0</option>
					  <option value=",|.|4|0"<% if(rec.getValorValCab().equals(",|.|4|0")) { out.print(" selected"); } %>>#,###.#### -  0</option>
 					  <option value=",|.|5|0"<% if(rec.getValorValCab().equals(",|.|5|0")) { out.print(" selected"); } %>>#,###.##### - 0</option>
                      <option value=",|.|6|0"<% if(rec.getValorValCab().equals(",|.|6|0")) { out.print(" selected"); } %>>#,###.###### - 0</option>
					  <option value=",|.|0|1"<% if(rec.getValorValCab().equals(",|.|0|1")) { out.print(" selected"); } %>>#,### - _</option>
                      <option value=",|.|1|1"<% if(rec.getValorValCab().equals(",|.|1|1")) { out.print(" selected"); } %>>#,###.# -  _</option>
 					  <option value=",|.|2|1"<% if(rec.getValorValCab().equals(",|.|2|1")) { out.print(" selected"); } %>>#,###.## - _</option>
                      <option value=",|.|3|1"<% if(rec.getValorValCab().equals(",|.|3|1")) { out.print(" selected"); } %>>#,###.### - _</option>
					  <option value=",|.|4|1"<% if(rec.getValorValCab().equals(",|.|4|1")) { out.print(" selected"); } %>>#,###.#### - _</option>
 					  <option value=",|.|5|1"<% if(rec.getValorValCab().equals(",|.|5|1")) { out.print(" selected"); } %>>#,###.##### - _</option>
                      <option value=",|.|6|1"<% if(rec.getValorValCab().equals(",|.|6|1")) { out.print(" selected"); } %>>#,###.###### - _</option>
 					</select>
 <%				
					}
					else if(rec.getTipoColCab().equals("TIME"))
					{
%>
					<select style="width:90%" name="valor" class="cpoBco">
					  <option value="dd/MM/yy"<% if(rec.getValorValCab().equals("dd/MM/yy")) { out.print(" selected"); } %>>dd/mm/aa</option>
                      <option value="dd/MM/yyyy"<% if(rec.getValorValCab().equals("dd/MM/yyyy")) { out.print(" selected"); } %>>dd/mm/aaaa</option>
                      <option value="dd/MMM/yy"<% if(rec.getValorValCab().equals("dd/MMM/yy")) { out.print(" selected"); } %>>dd/mmm/aa</option>
                      <option value="dd/MMM/yyyy"<% if(rec.getValorValCab().equals("dd/MMM/yyyy")) { out.print(" selected"); } %>>dd/mmm/aaaa</option>
					  <option value="dd/MM/yy hh:mm:ss"<% if(rec.getValorValCab().equals("dd/MM/yy hh:mm:ss")) { out.print(" selected"); } %>>dd/mm/aa hh:mm:ss</option>
                      <option value="dd/MM/yyyy hh:mm:ss"<% if(rec.getValorValCab().equals("dd/MM/yyyy hh:mm:ss")) { out.print(" selected"); } %>>dd/mm/aaaa hh:mm:ss</option>
                      <option value="dd/MMM/yy hh:mm:ss"<% if(rec.getValorValCab().equals("dd/MMM/yy hh:mm:ss")) { out.print(" selected"); } %>>dd/mmm/aa hh:mm:ss</option>
                      <option value="dd/MMM/yyyy hh:mm:ss"<% if(rec.getValorValCab().equals("dd/MMM/yyyy hh:mm:ss")) { out.print(" selected"); } %>>dd/mmm/aaaa hh:mm:ss</option>
  					  <option value="hh:mm"<% if(rec.getValorValCab().equals("hh:mm")) { out.print(" selected"); } %>>hh:mm</option>
                      <option value="hh:mm:ss"<% if(rec.getValorValCab().equals("hh:mm:ss")) { out.print(" selected"); } %>>hh:mm:ss</option>
					</select>
<%				
					}
					else if(rec.getTipoColCab().equals("BOOL"))
					{
%>
					<select style="width:90%" name="valor" class="cpoBco">
					  <option value="VER_FAL"<% if(rec.getValorValCab().equals("VER_FAL")) { out.print(" selected"); } %>><%= JUtil.Elm(etq,6) %></option>
                      <option value="V_F"<% if(rec.getValorValCab().equals("V_F")) { out.print(" selected"); } %>><%= JUtil.Elm(etq,7) %></option>
                      <option value="SI_NO"<% if(rec.getValorValCab().equals("SI_NO")) { out.print(" selected"); } %>><%= JUtil.Elm(etq,8) %></option>
                      <option value="1_0"<% if(rec.getValorValCab().equals("1_0")) { out.print(" selected"); } %>><%= JUtil.Elm(etq,9) %></option>
					</select>
<%
					}
					else if(rec.getTipoColCab().equals("LH"))
					{
%>
					<select style="width:90%" name="valor" class="cpoBco">
	                   <option value="t_negra"<% if(rec.getValorValCab().equals("t_negra")) { out.print(" selected"); } %>>---</option>
					</select>
<%					
					}
%>
				  
			</td>
          </tr>
		  <tr>  
		    <td align="right"> <input name="pos_x" type="text" id="pos_x" class="cpoBco" style="width:80%" maxlength="15" value="<%= rec.getPos_X_Cab() %>"/></td>
            <td align="right"> <input name="pos_y" type="text" id="pos_y" class="cpoBco" style="width:80%" maxlength="15" value="<%= rec.getPos_Y_Cab() %>"/> </td>
            <td align="right">  <input name="anc" type="text" id="anc" class="cpoBco" style="width:80%" maxlength="15" value="<%= rec.getAnc_Cab() %>"/></td>
            <td align="right"> <input name="alt" type="text" id="alt" class="cpoBco" style="width:80%" maxlength="15" value="<%= rec.getAlt_Cab() %>"/></td>
          </tr>
		  <tr>
		    <td align="right"> 
				<select style="width:100%" name="hor" class="cpoBco">
                      <option value="left"<% if(rec.getHor_Cab().equals("left")) { out.print(" selected"); } %>><%= JUtil.Elm(etq,10) %></option>
                      <option value="center"<% if(rec.getHor_Cab().equals("center")) { out.print(" selected"); } %>><%= JUtil.Elm(etq,11) %></option>
                      <option value="right"<% if(rec.getHor_Cab().equals("right")) { out.print(" selected"); } %>><%= JUtil.Elm(etq,12) %></option>
                 </select></td>
            <td align="right"> 
				<select style="width:100%" name="ver" class="cpoBco">
                      <option value="top"<% if(rec.getVer_Cab().equals("top")) { out.print(" selected"); } %>><%= JUtil.Elm(etq,13) %></option>
                      <option value="middle"<% if(rec.getVer_Cab().equals("middle")) { out.print(" selected"); } %>><%= JUtil.Elm(etq,14) %></option>
                      <option value="bottom"<% if(rec.getVer_Cab().equals("bottom")) { out.print(" selected"); } %>><%= JUtil.Elm(etq,15) %></option>
                 </select> </td>
		    <td colspan="2" align="right"><div align="left"><input name="fin" type="checkbox" class="cpoBco"<% if(rec.getFin()) { out.print(" checked"); } %>/></div>
        	&nbsp;<input name="submit_agr" type="image" id="submit_agr" onClick="javascript:if(this.form.subproceso.value == 'PRE_EDIT_PART') { establecerProcesoSVE(this.form.subproceso, 'EDIT_PART'); } else  { establecerProcesoSVE(this.form.subproceso, 'AGR_PART'); }" src="../../imgfsi/lista_ok.gif" border="0" width="24" height="24"/>
             &nbsp; <a href="javascript:limpiarFormulario();"><img src="../../imgfsi/lista_x.gif" border="0" width="24" height="24"/></a></td>
          </tr>
<%
		}
		
		if(rec.numPartidas() == 0)
		{
			out.println("<tr><td align=\"center\" class=\"titCuerpoAzc\" colspan=\"4\">" + JUtil.Msj("CEF","ADM_FORMATOS","DLG","ETQ4",4) + "</td></tr>");
		}
		else
		{						
			for(int i = 0; i < rec.numPartidas(); i++)
			{
%>
          <tr> 
            <td colspan="2">
				<% if( rec.getPartida(i).getEtiqueta() == null || rec.getPartida(i).getEtiqueta().equals("FSI_ETIQUETA")) {
					out.print(JUtil.Elm(etq,1));
				} else if( rec.getPartida(i).getEtiqueta().equals("FSI_TITULO")){
					out.print(JUtil.Elm(etq,2));
				} else if( rec.getPartida(i).getEtiqueta().equals("FSI_LH")) {
					out.print(JUtil.Elm(etq,3));
				} else { 
					out.print(rec.getPartida(i).getEtiqueta());
				} %></td>
            <td colspan="2">
				<%
				if(rec.getPartida(i).getValor() == null || rec.getPartida(i).getValor().equals(" |0")) { out.print("#### - 0");
                } else if(rec.getPartida(i).getValor().equals(" |1")) { out.print("#### - _");
				} else if(rec.getPartida(i).getValor().equals(",|0")) { out.print("#,### - 0");
                } else if(rec.getPartida(i).getValor().equals(",|1")) { out.print("#,### - _");

				} else if(rec.getPartida(i).getValor().equals("general")) { out.print(JUtil.Elm(etq,4));
                } else if(rec.getPartida(i).getValor().equals("cuenta")) { out.print(JUtil.Elm(etq,5));
 
				} else if(rec.getPartida(i).getValor().equals(",|.|0|0")) { out.print("#,### - 0");
                } else if(rec.getPartida(i).getValor().equals(",|.|1|0")) { out.print("#,###.# -  0");
 				} else if(rec.getPartida(i).getValor().equals(",|.|2|0")) { out.print("#,###.## - 0");
                } else if(rec.getPartida(i).getValor().equals(",|.|3|0")) { out.print("#,###.### - 0");
				} else if(rec.getPartida(i).getValor().equals(",|.|4|0")) { out.print("#,###.#### -  0");
 				} else if(rec.getPartida(i).getValor().equals(",|.|5|0")) { out.print("#,###.##### - 0");
                } else if(rec.getPartida(i).getValor().equals(",|.|6|0")) { out.print("#,###.###### - 0");
				} else if(rec.getPartida(i).getValor().equals(",|.|0|1")) { out.print("#,### - _");
                } else if(rec.getPartida(i).getValor().equals(",|.|1|1")) { out.print("#,###.# -  _");
 				} else if(rec.getPartida(i).getValor().equals(",|.|2|1")) { out.print("#,###.## - _");
                } else if(rec.getPartida(i).getValor().equals(",|.|3|1")) { out.print("#,###.### - _");
				} else if(rec.getPartida(i).getValor().equals(",|.|4|1")) { out.print("#,###.#### - _");
 				} else if(rec.getPartida(i).getValor().equals(",|.|5|1")) { out.print("#,###.##### - _");
                } else if(rec.getPartida(i).getValor().equals(",|.|6|1")) { out.print("#,###.###### - _");

				} else if(rec.getPartida(i).getValor().equals("dd/MM/yy")) { out.print("dd/mm/aa");
                } else if(rec.getPartida(i).getValor().equals("dd/MM/yyyy")) { out.print("dd/mm/aaaa");
                } else if(rec.getPartida(i).getValor().equals("dd/MMM/yy")) { out.print("dd/mmm/aa");
                } else if(rec.getPartida(i).getValor().equals("dd/MMM/yyyy")) { out.print("dd/mmm/aaaa");

				} else if(rec.getPartida(i).getValor().equals("VER_FAL")) { out.print(JUtil.Elm(etq,6));
                } else if(rec.getPartida(i).getValor().equals("V_F")) { out.print(JUtil.Elm(etq,7));
                } else if(rec.getPartida(i).getValor().equals("SI_NO")) { out.print(JUtil.Elm(etq,8));
                } else if(rec.getPartida(i).getValor().equals("1_0")) { out.print(JUtil.Elm(etq,9));

	            } else if(rec.getPartida(i).getValor().equals("t_negra")) { out.print("---");
				} else { out.print(rec.getPartida(i).getValor()); } %></td>
           </tr>
		   <tr>
		    <td align="right"><%= rec.getPartida(i).getX() %></td>
            <td align="right"><%= rec.getPartida(i).getY() %></td>
            <td align="right"><%= rec.getPartida(i).getAnc() %></td>
            <td align="right"><%= rec.getPartida(i).getAlt() %></td>
           </tr>
		   <tr>
		     <td align="right">
			<% if( rec.getPartida(i).getHor() == null || rec.getPartida(i).getHor().equals("left")) {
					out.print(JUtil.Elm(etq,10));
				} else if( rec.getPartida(i).getHor().equals("center")){
					out.print(JUtil.Elm(etq,11));
				} else if( rec.getPartida(i).getHor().equals("right")) {
					out.print(JUtil.Elm(etq,12));
				} else {
					out.print(JUtil.Elm(etq,10));
				} %></td>
            <td align="right">
			<% if( rec.getPartida(i).getVer() == null || rec.getPartida(i).getVer().equals("top")) {
					out.print(JUtil.Elm(etq,13));
				} else if( rec.getPartida(i).getVer().equals("middle")) {
					out.print(JUtil.Elm(etq,14));
				} else if( rec.getPartida(i).getVer().equals("bottom")) {
					out.print(JUtil.Elm(etq,15));
				} else {
					out.print(JUtil.Elm(etq,13));
				} %></td>
           	<td colspan="2" align="right"><%= rec.getPartida(i).getFin() %>
				<% if(!request.getParameter("proceso").equals("CONSULTAR_FORMATO")) { %>
		     &nbsp;<input name="submit_edt" type="image" id="submit_edt" onClick="javascript: establecerProcesoSVE(this.form.subproceso, 'PRE_EDIT_PART'); establecerProcesoSVE(this.form.FMTID, '<%= i %>');" src="../../imgfsi/lista_ed.gif" border="0" width="24" height="24"/>	
			 &nbsp;<input name="submit" type="image" onClick="javascript:this.form.idpartida.value = '<%= i %>'; establecerProcesoSVE(this.form.subproceso, 'BORR_PART');" src="../../imgfsi/lista_el.gif" border="0" width="24" height="24"/><% } else { out.print("&nbsp;"); } %></td>
          </tr>
<%
			}
		}
				
%>
		
		  <tr>
			  <td colspan="4">&nbsp;</td>
		  </tr>
		  <tr> 
			<td colspan="2" class="titChicoAzc"><%= JUtil.Elm(cols,1) %></td>
            <td colspan="2" class="titChicoAzc"><%= JUtil.Elm(cols,2) %></td>
          </tr>
		  <tr> 
			<td align="right" class="titChicoAzc"><%= JUtil.Elm(cols,3) %></td>
            <td align="right" class="titChicoAzc"><%= JUtil.Elm(cols,4) %></td>
            <td align="right" class="titChicoAzc"><%= JUtil.Elm(cols,5) %></td>
            <td align="right" class="titChicoAzc"><%= JUtil.Elm(cols,6) %></td>
          </tr>
		  <tr> 
			<td align="right" class="titChicoAzc"><%= JUtil.Elm(cols,7) %></td>
			<td align="right" class="titChicoAzc"><%= JUtil.Elm(cols,8) %></td>
			<td colspan="2" class="titChicoAzc">&nbsp;</td>
          </tr>
<%
		if( !request.getParameter("proceso").equals("CONSULTAR_FORMATO") )
		{
%>
          <tr> 
            <td colspan="2"> 
              <select style="width:90%" onChange="javascript:establecerProcesoSVE(this.form.subproceso, 'CAMBIO_ETQDET'); this.form.submit();" name="etiqueta_det" class="cpoBco">
			    <option value="FSI_ETIQUETA"<% if(rec.getEtiquetaDet().equals("FSI_ETIQUETA")) { out.print(" selected"); } %>><%= JUtil.Elm(etq,1) %></option>
				<option value="FSI_TITULO"<% if(rec.getEtiquetaDet().equals("FSI_TITULO")) { out.print(" selected"); } %>><%= JUtil.Elm(etq,2) %></option>
				<option value="FSI_LH"<% if(rec.getEtiquetaDet().equals("FSI_LH")) { out.print(" selected"); } %>><%= JUtil.Elm(etq,3) %></option>
<% 
			for(int i = 0; i < rec.getNumColumnasDet(); i++)
			{
%>
              <option value="<%= (rec.getColumnaDet(i)).getNombreCol() %>"<% if(rec.getEtiquetaDet().equals((rec.getColumnaDet(i)).getNombreCol() )) { out.print(" selected"); } %>><%= (rec.getColumnaDet(i)).getNombreCol()  %></option>
<%	
			}       
%>
              </select></td>
            <td colspan="2">
				
<%
					if(rec.getTipoColDet().equals("ESP"))
					{
%>
					<input name="valor_det" type="text" id="valor_det" class="cpoBco" style="width:90%" maxlength="254" value="<%= rec.getValorValDet() %>"/>
<%					
					}
					else if(rec.getTipoColDet().equals("BYTE") || rec.getTipoColDet().equals("INT"))
					{
%>
					<select style="width:90%" name="valor_det" class="cpoBco">
					  <option value=" |0"<% if(rec.getValorValDet().equals(" |0")) { out.print(" selected"); } %>>#### - 0</option>
                      <option value=" |1"<% if(rec.getValorValDet().equals(" |1")) { out.print(" selected"); } %>>#### - _</option>
					  <option value=",|0"<% if(rec.getValorValDet().equals(",|0")) { out.print(" selected"); } %>>#,### - 0</option>
                      <option value=",|1"<% if(rec.getValorValDet().equals(",|1")) { out.print(" selected"); } %>>#,### - _</option>
					</select>
<%		
					}
					else if(rec.getTipoColDet().equals("STRING"))
					{
%>
					<select style="width:90%" name="valor_det" class="cpoBco">
					  <option value="general"<% if(rec.getValorValDet().equals("general")) { out.print(" selected"); } %>><%= JUtil.Elm(etq,4) %></option>
                      <option value="cuenta"<% if(rec.getValorValDet().equals("cuenta")) { out.print(" selected"); } %>><%= JUtil.Elm(etq,5) %></option>
 					</select>
 <%				
					}
					else if(rec.getTipoColDet().equals("DECIMAL") || rec.getTipoColDet().equals("MONEY"))
					{
%>
					<select style="width:90%" name="valor_det" class="cpoBco">
					  <option value=",|.|0|0"<% if(rec.getValorValDet().equals(",|.|0|0")) { out.print(" selected"); } %>>#,### - 0</option>
                      <option value=",|.|1|0"<% if(rec.getValorValDet().equals(",|.|1|0")) { out.print(" selected"); } %>>#,###.# -  0</option>
 					  <option value=",|.|2|0"<% if(rec.getValorValDet().equals(",|.|2|0")) { out.print(" selected"); } %>>#,###.## - 0</option>
                      <option value=",|.|3|0"<% if(rec.getValorValDet().equals(",|.|3|0")) { out.print(" selected"); } %>>#,###.### - 0</option>
					  <option value=",|.|4|0"<% if(rec.getValorValDet().equals(",|.|4|0")) { out.print(" selected"); } %>>#,###.#### -  0</option>
 					  <option value=",|.|5|0"<% if(rec.getValorValDet().equals(",|.|5|0")) { out.print(" selected"); } %>>#,###.##### - 0</option>
                      <option value=",|.|6|0"<% if(rec.getValorValDet().equals(",|.|6|0")) { out.print(" selected"); } %>>#,###.###### - 0</option>
					  <option value=",|.|0|1"<% if(rec.getValorValDet().equals(",|.|0|1")) { out.print(" selected"); } %>>#,### - _</option>
                      <option value=",|.|1|1"<% if(rec.getValorValDet().equals(",|.|1|1")) { out.print(" selected"); } %>>#,###.# -  _</option>
 					  <option value=",|.|2|1"<% if(rec.getValorValDet().equals(",|.|2|1")) { out.print(" selected"); } %>>#,###.## - _</option>
                      <option value=",|.|3|1"<% if(rec.getValorValDet().equals(",|.|3|1")) { out.print(" selected"); } %>>#,###.### - _</option>
					  <option value=",|.|4|1"<% if(rec.getValorValDet().equals(",|.|4|1")) { out.print(" selected"); } %>>#,###.#### - _</option>
 					  <option value=",|.|5|1"<% if(rec.getValorValDet().equals(",|.|5|1")) { out.print(" selected"); } %>>#,###.##### - _</option>
                      <option value=",|.|6|1"<% if(rec.getValorValDet().equals(",|.|6|1")) { out.print(" selected"); } %>>#,###.###### - _</option>
 					</select>
 <%				
					}
					else if(rec.getTipoColDet().equals("TIME"))
					{
%>
					<select style="width:90%" name="valor_det" class="cpoBco">
					  <option value="dd/MM/yy"<% if(rec.getValorValDet().equals("dd/MM/yy")) { out.print(" selected"); } %>>dd/mm/aa</option>
                      <option value="dd/MM/yyyy"<% if(rec.getValorValDet().equals("dd/MM/yyyy")) { out.print(" selected"); } %>>dd/mm/aaaa</option>
                      <option value="dd/MMM/yy"<% if(rec.getValorValDet().equals("dd/MMM/yy")) { out.print(" selected"); } %>>dd/mmm/aa</option>
                      <option value="dd/MMM/yyyy"<% if(rec.getValorValDet().equals("dd/MMM/yyyy")) { out.print(" selected"); } %>>dd/mmm/aaaa</option>
					  <option value="dd/MM/yy hh:mm:ss"<% if(rec.getValorValDet().equals("dd/MM/yy hh:mm:ss")) { out.print(" selected"); } %>>dd/mm/aa hh:mm:ss</option>
                      <option value="dd/MM/yyyy hh:mm:ss"<% if(rec.getValorValDet().equals("dd/MM/yyyy hh:mm:ss")) { out.print(" selected"); } %>>dd/mm/aaaa hh:mm:ss</option>
                      <option value="dd/MMM/yy hh:mm:ss"<% if(rec.getValorValDet().equals("dd/MMM/yy hh:mm:ss")) { out.print(" selected"); } %>>dd/mmm/aa hh:mm:ss</option>
                      <option value="dd/MMM/yyyy hh:mm:ss"<% if(rec.getValorValDet().equals("dd/MMM/yyyy hh:mm:ss")) { out.print(" selected"); } %>>dd/mmm/aaaa hh:mm:ss</option>
  					  <option value="hh:mm"<% if(rec.getValorValDet().equals("hh:mm")) { out.print(" selected"); } %>>hh:mm</option>
                      <option value="hh:mm:ss"<% if(rec.getValorValDet().equals("hh:mm:ss")) { out.print(" selected"); } %>>hh:mm:ss</option>
					</select>
<%				
					}
					else if(rec.getTipoColDet().equals("BOOL"))
					{
%>
					<select style="width:90%" name="valor_det" class="cpoBco">
					  <option value="VER_FAL"<% if(rec.getValorValDet().equals("VER_FAL")) { out.print(" selected"); } %>><%= JUtil.Elm(etq,6) %></option>
                      <option value="V_F"<% if(rec.getValorValDet().equals("V_F")) { out.print(" selected"); } %>><%= JUtil.Elm(etq,7) %></option>
                      <option value="SI_NO"<% if(rec.getValorValDet().equals("SI_NO")) { out.print(" selected"); } %>><%= JUtil.Elm(etq,8) %></option>
                      <option value="1_0"<% if(rec.getValorValDet().equals("1_0")) { out.print(" selected"); } %>><%= JUtil.Elm(etq,9) %></option>
					</select>
<%
					}
					else if(rec.getTipoColDet().equals("LH"))
					{
%>
					<select style="width:90%" name="valor_det" class="cpoBco">
	                   <option value="t_negra"<% if(rec.getValorValDet().equals("t_negra")) { out.print(" selected"); } %>>---</option>
					</select>
<%					
					}
%>
				  	
				</td>
              </tr>
			  <tr>
			    <td align="right"> <input name="pos_x_det" type="text" id="pos_x_det" class="cpoBco" style="width:80%" maxlength="15" value="<%= rec.getPos_X_Det() %>"/></td>
            	<td align="right"> <input name="pos_y_det" type="text" id="pos_y_det" class="cpoBco" style="width:80%" maxlength="15" value="<%= rec.getPos_Y_Det() %>"/> </td>
           		<td align="right">  <input name="anc_det" type="text" id="anc_det" class="cpoBco" style="width:80%" maxlength="15" value="<%= rec.getAnc_Det() %>"/></td>
            	<td align="right"> <input name="alt_det" type="text" id="alt_det" class="cpoBco" style="width:80%" maxlength="15" value="<%= rec.getAlt_Det() %>"/></td>
              </tr>
			  <tr>
			    <td align="right"> 
				<select style="width:100%" name="hor_det" class="cpoBco">
                      <option value="left"<% if(rec.getHor_Det().equals("left")) { out.print(" selected"); } %>><%= JUtil.Elm(etq,10) %></option>
                      <option value="center"<% if(rec.getHor_Det().equals("center")) { out.print(" selected"); } %>><%= JUtil.Elm(etq,11) %></option>
                      <option value="right"<% if(rec.getHor_Det().equals("right")) { out.print(" selected"); } %>><%= JUtil.Elm(etq,12) %></option>
              	</select></td>
            	<td align="right">
				<select style="width:100%" name="ver_det" class="cpoBco">
                      <option value="top"<% if(rec.getVer_Det().equals("top")) { out.print(" selected"); } %>><%= JUtil.Elm(etq,13) %></option>
                      <option value="middle"<% if(rec.getVer_Det().equals("middle")) { out.print(" selected"); } %>><%= JUtil.Elm(etq,14) %></option>
                      <option value="bottom"<% if(rec.getVer_Det().equals("bottom")) { out.print(" selected"); } %>><%= JUtil.Elm(etq,15) %></option>
                    </select> </td>
		       	<td colspan="2" align="right"> <input name="submit_agr" type="image" id="submit_agr" onClick="javascript:if(this.form.subproceso.value == 'PRE_EDIT_PART_DET') { establecerProcesoSVE(this.form.subproceso, 'EDIT_PART_DET'); } else  { establecerProcesoSVE(this.form.subproceso, 'AGR_PART_DET'); }" src="../../imgfsi/lista_ok.gif" border="0" width="24" height="24"/>
                &nbsp;<a href="javascript:limpiarFormularioDET();"><img src="../../imgfsi/lista_x.gif" width="24" height="24" border="0"/></a></td>
          	  </tr>
<%
		}
		
		if(rec.numObjetos() == 0)
		{
			out.println("<tr><td align=\"center\" class=\"titCuerpoAzc\" colspan=\"4\">" + JUtil.Msj("CEF","ADM_FORMATOS","DLG","ETQ4",5) + "</td></tr>");
		}
		else
		{						
			for(int i = 0; i < rec.numObjetos(); i++)
	 		{
%>
          <tr> 
            <td colspan="2">
				<% if( rec.getObjeto(i).getEtiqueta().equals("FSI_ETIQUETA")) {
					out.print(JUtil.Elm(etq,1));
				} else if( rec.getObjeto(i).getEtiqueta().equals("FSI_TITULO")){
					out.print(JUtil.Elm(etq,2));
				} else if( rec.getObjeto(i).getEtiqueta().equals("FSI_LH")) {
					out.print(JUtil.Elm(etq,3));
				} else { 
					out.print(rec.getObjeto(i).getEtiqueta());
				} %></td>
            <td colspan="2">
				<%
				if(rec.getObjeto(i).getValor().equals(" |0")) { out.print("#### - 0");
                } else if(rec.getObjeto(i).getValor().equals(" |1")) { out.print("#### - _");
				} else if(rec.getObjeto(i).getValor().equals(",|0")) { out.print("#,### - 0");
                } else if(rec.getObjeto(i).getValor().equals(",|1")) { out.print("#,### - _");

				} else if(rec.getObjeto(i).getValor().equals("general")) { out.print(JUtil.Elm(etq,4));
                } else if(rec.getObjeto(i).getValor().equals("cuenta")) { out.print(JUtil.Elm(etq,5));
 
				} else if(rec.getObjeto(i).getValor().equals(",|.|0|0")) { out.print("#,### - 0");
                } else if(rec.getObjeto(i).getValor().equals(",|.|1|0")) { out.print("#,###.# -  0");
 				} else if(rec.getObjeto(i).getValor().equals(",|.|2|0")) { out.print("#,###.## - 0");
                } else if(rec.getObjeto(i).getValor().equals(",|.|3|0")) { out.print("#,###.### - 0");
				} else if(rec.getObjeto(i).getValor().equals(",|.|4|0")) { out.print("#,###.#### -  0");
 				} else if(rec.getObjeto(i).getValor().equals(",|.|5|0")) { out.print("#,###.##### - 0");
                } else if(rec.getObjeto(i).getValor().equals(",|.|6|0")) { out.print("#,###.###### - 0");
				} else if(rec.getObjeto(i).getValor().equals(",|.|0|1")) { out.print("#,### - _");
                } else if(rec.getObjeto(i).getValor().equals(",|.|1|1")) { out.print("#,###.# - _");
 				} else if(rec.getObjeto(i).getValor().equals(",|.|2|1")) { out.print("#,###.## - _");
                } else if(rec.getObjeto(i).getValor().equals(",|.|3|1")) { out.print("#,###.### - _");
				} else if(rec.getObjeto(i).getValor().equals(",|.|4|1")) { out.print("#,###.#### - _");
 				} else if(rec.getObjeto(i).getValor().equals(",|.|5|1")) { out.print("#,###.##### - _");
                } else if(rec.getObjeto(i).getValor().equals(",|.|6|1")) { out.print("#,###.###### - _");

				} else if(rec.getObjeto(i).getValor().equals("dd/MM/yy")) { out.print("dd/mm/aa");
                } else if(rec.getObjeto(i).getValor().equals("dd/MM/yyyy")) { out.print("dd/mm/aaaa");
                } else if(rec.getObjeto(i).getValor().equals("dd/MMM/yy")) { out.print("dd/mmm/aa");
                } else if(rec.getObjeto(i).getValor().equals("dd/MMM/yyyy")) { out.print("dd/mmm/aaaa");

				} else if(rec.getObjeto(i).getValor().equals("VER_FAL")) { out.print(JUtil.Elm(etq,6));
                } else if(rec.getObjeto(i).getValor().equals("V_F")) { out.print(JUtil.Elm(etq,7));
                } else if(rec.getObjeto(i).getValor().equals("SI_NO")) { out.print(JUtil.Elm(etq,8));
                } else if(rec.getObjeto(i).getValor().equals("1_0")) { out.print(JUtil.Elm(etq,9));

	            } else if(rec.getObjeto(i).getValor().equals("t_negra")) { out.print("---");
				} else { out.print(rec.getObjeto(i).getValor()); } %></td>
           </tr>
		   <tr>
		    <td align="right"><%= rec.getObjeto(i).getX() %></td>
            <td align="right"><%= rec.getObjeto(i).getY() %></td>
            <td align="right"><%= rec.getObjeto(i).getAnc() %></td>
            <td align="right"><%= rec.getObjeto(i).getAlt() %></td>
           </tr>
		   <tr>
		    <td align="right">
			<% if( rec.getObjeto(i).getHor().equals("left")) {
					out.print(JUtil.Elm(etq,10));
				} else if( rec.getObjeto(i).getHor().equals("center")){
					out.print(JUtil.Elm(etq,11));
				} else if( rec.getObjeto(i).getHor().equals("right")) {
					out.print(JUtil.Elm(etq,12));
				} %></td>
            <td align="right">
			<% if( rec.getObjeto(i).getVer().equals("top")) {
					out.print(JUtil.Elm(etq,13));
				} else if( rec.getObjeto(i).getVer().equals("middle")) {
					out.print(JUtil.Elm(etq,14));
				} else if( rec.getObjeto(i).getVer().equals("bottom")) {
					out.print(JUtil.Elm(etq,15));
				} %></td>
           	<td colspan="2" align="right"><% if(!request.getParameter("proceso").equals("CONSULTAR_FORMATO")) { %>
			  <input name="submit_edt" type="image" id="submit_edt" onClick="javascript: establecerProcesoSVE(this.form.subproceso, 'PRE_EDIT_PART_DET'); establecerProcesoSVE(this.form.FMTID, '<%= i %>');" src="../../imgfsi/lista_ed.gif" border="0" width="24" height="24"/>
              &nbsp;<input name="submit" type="image" onClick="javascript:this.form.idpartida.value = '<%= i %>'; establecerProcesoSVE(this.form.subproceso, 'BORR_PART_DET');" src="../../imgfsi/lista_el.gif" border="0" width="24" height="24"/><% } else { out.print("&nbsp;"); } %></td>
          </tr>
<%
			}
		}
				
%>
			</table>
		  </td>
	  </tr>
	  <tr> 
		<td colspan="2">&nbsp;</td>
	  </tr>
	</table>
	</td>
  </tr>
</table>
</form>
</body>
</html>
