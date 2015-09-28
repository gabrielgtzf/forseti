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
	String nom_imss_dlg = (String)request.getAttribute("nom_imss_dlg");
	if(nom_imss_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}

	String titulo =  JUtil.getSesion(request).getSesion("NOM_IMSS").generarTitulo(JUtil.Msj("CEF","NOM_IMSS","VISTA",request.getParameter("proceso"),3),"../../forsetidoc/040202.html");

	JImssSet set = new JImssSet(request);
	if( request.getParameter("proceso").equals("CAMBIAR_IMSS") )
	{
		set.m_Where = "ID_Imss = '" + JUtil.p(request.getParameter("id")) + "'";
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
	if(formAct.proceso.value == "AGREGAR_IMSS" || formAct.proceso.value == "CAMBIAR_IMSS")
	{
		if(	!esNumeroDecimal('Cuota Patron:', formAct.cuota_patron.value, 0, 999.999999, 6) ||
			!esNumeroDecimal('Cuota Trabajador:', formAct.cuota_trabajador.value, 0, 999.999999, 6) ||
			!esNumeroDecimal('Total:', formAct.total.value, 0, 9999.999999, 6) ||
			!esNumeroEntero('Clave:', formAct.id_imss.value, 0, 254)  )
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
	else
		return false;
}
-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css"></head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form onSubmit="return enviarlo(this)" action="/servlet/CEFNomImssDlg" method="post" enctype="application/x-www-form-urlencoded" name="nom_imss_dlg" target="_self">
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
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/CEFNomImssCtrl'" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
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
            <td> <input name="id_imss" type="text" id="id_imss" size="8" maxlength="3"<%= (request.getParameter("proceso").equals("CAMBIAR_IMSS")) ? " readonly=\"true\"" : "" %>> 
            </td>
          </tr>
          <tr> 
            <td> <div align="right">Concepto:</div></td>
            <td> <input name="concepto" type="text" id="concepto" size="40" maxlength="40"></td>
          </tr>
          <tr> 
            <td> <div align="right">Cuota trabajador:</div></td>
            <td> <input name="cuota_trabajador" type="text" id="cuota_trabajador" size="11" maxlength="11"> 
            </td>
          </tr>
          <tr> 
            <td> <div align="right">Cuota patr&oacute;n:</div></td>
            <td> <input name="cuota_patron" type="text" id="cuota_patron" size="11" maxlength="11"> 
            </td>
          </tr>
          <tr> 
            <td> <div align="right">Total:</div></td>
            <td> <input name="total" type="text" id="total" size="11" maxlength="11"> 
            </td>
          </tr>
          
        </table>
      </td>
  </tr>
 </table>
</form>
<script language="JavaScript1.2">
document.nom_imss_dlg.id_imss.value = '<% if(request.getParameter("id_imss") != null) { out.print( request.getParameter("id_imss") ); } else if(!request.getParameter("proceso").equals("AGREGAR_IMSS")) { out.print( set.getAbsRow(0).getID_Imss() ); } else { out.print(""); } %>'  
document.nom_imss_dlg.concepto.value = '<% if(request.getParameter("concepto") != null) { out.print( request.getParameter("concepto") ); } else if(!request.getParameter("proceso").equals("AGREGAR_IMSS")) { out.print( set.getAbsRow(0).getConcepto() ); } else { out.print(""); } %>' 
document.nom_imss_dlg.cuota_trabajador.value = '<% if(request.getParameter("cuota_trabajador") != null) { out.print( request.getParameter("cuota_trabajador") ); } else if(!request.getParameter("proceso").equals("AGREGAR_IMSS")) { out.print( set.getAbsRow(0).getCuota_Trabajador() ); } else { out.print("0"); } %>' 
document.nom_imss_dlg.cuota_patron.value = '<% if(request.getParameter("cuota_patron") != null) { out.print( request.getParameter("cuota_patron") ); } else if(!request.getParameter("proceso").equals("AGREGAR_IMSS")) { out.print( set.getAbsRow(0).getCuota_Patron() ); } else { out.print("0"); } %>' 
document.nom_imss_dlg.total.value = '<% if(request.getParameter("total") != null) { out.print( request.getParameter("total") ); } else if(!request.getParameter("proceso").equals("AGREGAR_IMSS")) { out.print( set.getAbsRow(0).getTotal() ); } else { out.print("0"); } %>' 

</script>
</body>
</html>
