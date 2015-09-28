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
	String adm_cfdi_dlg = (String)request.getAttribute("adm_cfdi_dlg");
	if(adm_cfdi_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}

	String titulo = JUtil.getSesion(request).getSesion("ADM_CFDI").generarTitulo(JUtil.Msj("CEF","ADM_CFDI","VISTA",request.getParameter("proceso"),3),"../../forsetidoc/040202.html");
	
	JBDSSet set = new JBDSSet(request);
	set.ConCat(true);
    set.m_Where = "Nombre = 'FSIBD_" + JUtil.getSesion(request).getBDCompania() + "'";
    set.Open();

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
function enviarlo(formAct)
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
-->
</script>
<link href="../compfsi/estilos.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#333333" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form onSubmit="return enviarlo(this)" action="/servlet/CEFAdmCFDDlg" method="post" enctype="application/x-www-form-urlencoded" name="adm_cfd_dlg" target="_self">
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
              <%  if(JUtil.getSesion(request).getID_Mensaje() == 0 || request.getParameter("proceso").equals("CONSULTAR_EMISOR")) { %>
        			<input type="submit" name="aceptar" disabled="true" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>"/>
        			<%  } else { %>
        			<input type="submit" name="aceptar" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>"/>
       				<%  } %>
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/CEFAdmCFDCtrl'" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>"/></td>
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
            <td colspan="2"> 
                <input name="proceso" type="hidden" value="<%= request.getParameter("proceso")%>"/>
                <input name="subproceso" type="hidden" value="ENVIAR"/>
                <%= JUtil.Msj("GLB","GLB","GLB","NOMBRE") %></td>
          </tr>
          <tr> 
            <td colspan="2"><input class="cpoColAzc" name="nombre" type="text" id="nombre" style="width:100%" maxlength="255"<% if(request.getParameter("proceso").equals("CAMBIAR_EMISOR")) { out.print(" readonly=\"true\""); } %>/></td>
          </tr>
          <tr> 
          	<td width="50%"><%= JUtil.Msj("GLB","GLB","GLB","RFC") %></td>
            <td><%= JUtil.Msj("GLB","GLB","GLB","CFD",2) %></td>
          </tr>
          <tr> 
            <td><input name="rfc" type="text" id="rfc" style="width:50%" maxlength="15"/></td>
            <td>
<%
	if(!request.getParameter("proceso").equals("CONSULTAR_EMISOR"))
	{
%>
          		<input type="radio" name="cfd" value="0"<% if(request.getParameter("cfd") != null && request.getParameter("cfd").equals("0")) { out.print(" checked"); } else if(!request.getParameter("proceso").equals("AGREGAR_EMISOR") && set.getAbsRow(0).getCFD() == 0) { out.print(" checked"); } else { out.print(" checked"); } %>/>
                    <%= JUtil.Msj("GLB","GLB","GLB","NO") %>
                    <input type="radio" name="cfd" value="1"<% if(request.getParameter("cfd") != null && request.getParameter("cfd").equals("1")) { out.print(" checked"); } else if(!request.getParameter("proceso").equals("AGREGAR_EMISOR") && set.getAbsRow(0).getCFD() == 1) { out.print(" checked"); } else { out.print(""); } %>/>
                    <%= JUtil.Msj("GLB","GLB","GLB","CFD",4) %>
<%
	}
	else
	{

		switch(set.getAbsRow(0).getCFD())
		{
			case 0:
				out.print(JUtil.Msj("GLB","GLB","GLB","NO"));
				break;
			default:
				out.print(JUtil.Msj("GLB","GLB","GLB","CFD",4));
				break;
		}
	}
%>
                </td>
				<tr> 
                  <td colspan="2" align="center" class="titChicoAzc"><%= JUtil.Msj("CEF","ADM_CFDI","DLG","ETQ",1) %></td>
                </tr>
                <tr> 
                  <td colspan="2"><%= JUtil.Msj("GLB","GLB","GLB","CALLE") %></td>
                </tr>
                <tr> 
                  <td colspan="2"><input name="cfd_calle" type="text" id="cfd_calle" style="width:90%" maxlength="80"/></td>
                </tr>
                <tr> 
                  <td><%= JUtil.Msj("GLB","GLB","GLB","NUMERO",3) %></td>
                  <td><%= JUtil.Msj("GLB","GLB","GLB","NUMERO",4) %></td>
                </tr>
                <tr> 
                  <td><input name="cfd_noext" type="text" id="cfd_noext" style="width:50%" maxlength="10"/></td>
                  <td><input name="cfd_noint" type="text" id="cfd_noint" style="width:50%" maxlength="10"/></td>
                </tr>
                <tr> 
                  <td colspan="2"><%= JUtil.Msj("GLB","GLB","GLB","COLONIA") %></td>
                </tr>
                <tr> 
                  <td colspan="2"><input name="cfd_colonia" type="text" id="cfd_colonia" style="width:90%" maxlength="40"/></td>
                </tr>
                <tr> 
                  <td><%= JUtil.Msj("GLB","GLB","GLB","LOCALIDAD") %></td>
                  <td><%= JUtil.Msj("GLB","GLB","GLB","CP") %></td>
                </tr>
                <tr> 
                  <td><input name="cfd_localidad" type="text" id="cfd_localidad" style="width:100%" maxlength="80"/></td>
                  <td><input name="cfd_cp" type="text" id="cfd_cp" style="width:50%" maxlength="5"/></td>
                </tr>
                <tr> 
                  <td><%= JUtil.Msj("GLB","GLB","GLB","MUNICIPIO") %></td>
                  <td><%= JUtil.Msj("GLB","GLB","GLB","ESTADO") %></td>
                </tr>
                <tr> 
                  <td><input name="cfd_municipio" type="text" id="cfd_municipio" style="width:100%" maxlength="40"/></td>
                  <td><input name="cfd_estado" type="text" id="cfd_estado" style="width:100%" maxlength="40"/></td>
                </tr>
                <tr> 
                  <td colspan="2"><%= JUtil.Msj("GLB","GLB","GLB","PAIS") %></td>
                </tr>
                <tr> 
                  <td colspan="2"><input name="cfd_pais" type="text" id="cfd_pais" style="width:50%" maxlength="20"/></td>
                </tr>
				<tr> 
                  <td colspan="2"><%= JUtil.Msj("GLB","GLB","GLB","REGIMEN_FISCAL") %></td>
                </tr>
                <tr> 
                  <td colspan="2"><input name="cfd_regimenfiscal" type="text" id="cfd_regimenfiscal" style="width:100%" maxlength="254"/></td>
                </tr>
         		<tr> 
            		<td colspan="2">&nbsp; </td>
          		</tr>
        </table>
      </td>
  </tr>
</table>
</form>
<script language="JavaScript1.2">
document.adm_cfd_dlg.nombre.value = '<% if(request.getParameter("nombre") != null) { out.print( request.getParameter("nombre") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EMISOR")) { out.print( set.getAbsRow(0).getCompania() ); } else { out.print(""); } %>'
document.adm_cfd_dlg.rfc.value = '<% if(request.getParameter("rfc") != null) { out.print( request.getParameter("rfc") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EMISOR")) { out.print( set.getAbsRow(0).getRFC() ); } else { out.print(""); } %>' 
document.adm_cfd_dlg.cfd_calle.value = '<% if(request.getParameter("cfd_calle") != null) { out.print( request.getParameter("cfd_calle") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EMISOR")) { out.print( set.getAbsRow(0).getCFD_Calle() ); } else { out.print(""); } %>'
document.adm_cfd_dlg.cfd_noext.value = '<% if(request.getParameter("cfd_noext") != null) { out.print( request.getParameter("cfd_noext") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EMISOR")) { out.print( set.getAbsRow(0).getCFD_NoExt() ); } else { out.print(""); } %>'
document.adm_cfd_dlg.cfd_noint.value = '<% if(request.getParameter("cfd_noint") != null) { out.print( request.getParameter("cfd_noint") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EMISOR")) { out.print( set.getAbsRow(0).getCFD_NoInt() ); } else { out.print(""); } %>'
document.adm_cfd_dlg.cfd_colonia.value = '<% if(request.getParameter("cfd_colonia") != null) { out.print( request.getParameter("cfd_colonia") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EMISOR")) { out.print( set.getAbsRow(0).getCFD_Colonia() ); } else { out.print(""); } %>'
document.adm_cfd_dlg.cfd_localidad.value = '<% if(request.getParameter("cfd_localidad") != null) { out.print( request.getParameter("cfd_localidad") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EMISOR")) { out.print( set.getAbsRow(0).getCFD_Localidad() ); } else { out.print(""); } %>'
document.adm_cfd_dlg.cfd_cp.value = '<% if(request.getParameter("cfd_cp") != null) { out.print( request.getParameter("cfd_cp") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EMISOR")) { out.print( set.getAbsRow(0).getCFD_CP() ); } else { out.print(""); } %>'
document.adm_cfd_dlg.cfd_municipio.value = '<% if(request.getParameter("cfd_municipio") != null) { out.print( request.getParameter("cfd_municipio") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EMISOR")) { out.print( set.getAbsRow(0).getCFD_Municipio() ); } else { out.print(""); } %>'
document.adm_cfd_dlg.cfd_estado.value = '<% if(request.getParameter("cfd_estado") != null) { out.print( request.getParameter("cfd_estado") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EMISOR")) { out.print( set.getAbsRow(0).getCFD_Estado() ); } else { out.print(""); } %>'
document.adm_cfd_dlg.cfd_pais.value = '<% if(request.getParameter("cfd_pais") != null) { out.print( request.getParameter("cfd_pais") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EMISOR")) { out.print( set.getAbsRow(0).getCFD_Pais() ); } else { out.print("Mexico"); } %>'
document.adm_cfd_dlg.cfd_regimenfiscal.value = '<% if(request.getParameter("cfd_regimenfiscal") != null) { out.print( request.getParameter("cfd_regimenfiscal") ); } else if(!request.getParameter("proceso").equals("AGREGAR_EMISOR")) { out.print( set.getAbsRow(0).getCFD_RegimenFiscal() ); } else { out.print(""); } %>'
</script>
</body>
</html>
