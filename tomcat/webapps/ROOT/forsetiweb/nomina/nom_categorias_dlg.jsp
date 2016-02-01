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
	String nom_categorias_dlg = (String)request.getAttribute("nom_categorias_dlg");
	if(nom_categorias_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}

	String titulo =  JUtil.getSesion(request).getSesion("NOM_CATEGORIAS").generarTitulo(JUtil.Msj("CEF","NOM_CATEGORIAS","VISTA",request.getParameter("proceso"),3),"../../forsetidoc/040205.html");
	
	JCategoriasSet set = new JCategoriasSet(request);
	if( request.getParameter("proceso").equals("CAMBIAR_CATEGORIA") )
	{
		set.m_Where = "ID_Categoria = '" + JUtil.p(request.getParameter("id")) + "'";
		set.Open();
	}
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
	if(formAct.proceso.value == "AGREGAR_CATEGORIA" || formAct.proceso.value == "CAMBIAR_CATEGORIA")
	{
		if(	!esNumeroDecimal('Sueldo:', formAct.sueldo.value, 0, 999999.99, 2) ||
			!esNumeroDecimal('Vales:', formAct.vales.value, 0, 999999.99, 2) ||
			!esNumeroDecimal('% sueldo:', formAct.sueldoam.value, -0.999999, 0.999999, 6) ||
			!esNumeroDecimal('% integrado:', formAct.integradoam.value, -0.999999, 0.999999, 6) ||
			!esNumeroDecimal('% vales:', formAct.valesam.value, -0.999999, 0.999999, 6) ||
			!esNumeroEntero('Clave:', formAct.id_categoria.value, 0, 254)  )
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
}
-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css"></head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form onSubmit="return enviarlo(this)" action="/servlet/CEFNomCategoriasDlg" method="post" enctype="application/x-www-form-urlencoded" name="nom_categorias_dlg" target="_self">
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
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/CEFNomCategoriasCtrl'" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
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
            <td width="20%"> <div align="right"> 
                <input name="proceso" type="hidden" value="<%= request.getParameter("proceso")%>">
                <input name="subproceso" type="hidden" value="ENVIAR">
                <input name="id" type="hidden" value="<%= request.getParameter("id")%>">
                Clave:</div></td>
            <td colspan="2"> <input name="id_categoria" type="text" id="id_categoria" size="8" maxlength="3"<%= (request.getParameter("proceso").equals("CAMBIAR_CATEGORIA")) ? " readonly=\"true\"" : "" %>> 
            </td>
          </tr>
          <tr> 
            <td> <div align="right">Descripcion:</div></td>
            <td colspan="2"> <input name="descripcion" type="text" id="descripcion" size="35" maxlength="20"></td>
          </tr>
          <tr> 
            <td> <div align="right">Sueldo:</div></td>
            <td width="60%"><input name="sueldo" type="text" id="sueldo" size="12" maxlength="12"></td>
            <td width="20%"><input name="sueldoam" type="text" id="sueldoam" size="9" maxlength="9">
              ej: 12% = 0.12</td>
          </tr>
          <tr> 
            <td> <div align="right">Integrado:</div></td>
            <td width="60%">&nbsp;</td>
            <td width="20%"><input name="integradoam" type="text" id="integradoam" size="9" maxlength="9">
              ej: 12% = 0.12</td>
          </tr>
          <tr> 
            <td> <div align="right">Vales:</div></td>
            <td width="60%"><input name="vales" type="text" id="vales" size="12" maxlength="12"></td>
            <td width="20%"><input name="valesam" type="text" id="valesam" size="9" maxlength="9">
              ej: 12% = 0.12 </td>
          </tr>
        </table>
      </td>
  </tr>
 </table>
</form>
<script language="JavaScript1.2">
document.nom_categorias_dlg.id_categoria.value = '<% if(request.getParameter("id_categoria") != null) { out.print( request.getParameter("id_categoria") ); } else if(!request.getParameter("proceso").equals("AGREGAR_CATEGORIA")) { out.print( set.getAbsRow(0).getID_Categoria() ); } else { out.print(""); } %>'  
document.nom_categorias_dlg.descripcion.value = '<% if(request.getParameter("descripcion") != null) { out.print( request.getParameter("descripcion") ); } else if(!request.getParameter("proceso").equals("AGREGAR_CATEGORIA")) { out.print( set.getAbsRow(0).getDescripcion() ); } else { out.print(""); } %>' 
document.nom_categorias_dlg.sueldo.value = '<% if(request.getParameter("sueldo") != null) { out.print( request.getParameter("sueldo") ); } else if(!request.getParameter("proceso").equals("AGREGAR_CATEGORIA")) { out.print( set.getAbsRow(0).getSueldo() ); } else { out.print("0.00"); } %>' 
document.nom_categorias_dlg.sueldoam.value = '<% if(request.getParameter("sueldoam") != null) { out.print( request.getParameter("sueldoam") ); } else if(!request.getParameter("proceso").equals("AGREGAR_CATEGORIA")) { out.print( set.getAbsRow(0).getSueldoAM() ); } else { out.print("0"); } %>' 
document.nom_categorias_dlg.integradoam.value = '<% if(request.getParameter("integradoam") != null) { out.print( request.getParameter("integradoam") ); } else if(!request.getParameter("proceso").equals("AGREGAR_CATEGORIA")) { out.print( set.getAbsRow(0).getIntegradoAM() ); } else { out.print("0"); } %>' 
document.nom_categorias_dlg.vales.value = '<% if(request.getParameter("vales") != null) { out.print( request.getParameter("vales") ); } else if(!request.getParameter("proceso").equals("AGREGAR_CATEGORIA")) { out.print( set.getAbsRow(0).getVales() ); } else { out.print("0.00"); } %>' 
document.nom_categorias_dlg.valesam.value = '<% if(request.getParameter("valesam") != null) { out.print( request.getParameter("valesam") ); } else if(!request.getParameter("proceso").equals("AGREGAR_CATEGORIA")) { out.print( set.getAbsRow(0).getValesAM() ); } else { out.print("0"); } %>' 
</script>
</body>
</html>
