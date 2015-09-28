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
	String nom_credsal_dlg = (String)request.getAttribute("nom_credsal_dlg");
	if(nom_credsal_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}

	String titulo =  JUtil.getSesion(request).getSesion("NOM_CREDSAL").generarTitulo(JUtil.Msj("CEF","NOM_CREDSAL","VISTA",request.getParameter("proceso"),3),"../../forsetidoc/040202.html");

	JCreditoSalarioSet set = new JCreditoSalarioSet(request);
	if( request.getParameter("proceso").equals("CAMBIAR_CREDSAL") )
	{
		set.m_Where = "ID_CS = '" + JUtil.p(request.getParameter("id")) + "'";
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
	if(formAct.proceso.value == "AGREGAR_CREDSAL" || formAct.proceso.value == "CAMBIAR_CREDSAL")
	{
		if(	!esNumeroDecimal('Desde Ingresos:', formAct.ingresos_desde.value, 0, 99999999.99, 2) ||
			!esNumeroDecimal('Hasta Ingresos:', formAct.ingresos_hasta.value, 0, 99999999.99, 2) ||
			!esNumeroDecimal('Credito al Salario Mensual:', formAct.csm.value, 0, 99999999.99, 2) ||
			!esNumeroEntero('Clave:', formAct.id_cs.value, 0, 254)  )
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
<form onSubmit="return enviarlo(this)" action="/servlet/CEFNomCredSalDlg" method="post" enctype="application/x-www-form-urlencoded" name="nom_credsal_dlg" target="_self">
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
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/CEFNomCredSalCtrl'" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
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
            <td> <input name="id_cs" type="text" id="id_cs" size="8" maxlength="3"<%= (request.getParameter("proceso").equals("CAMBIAR_CREDSAL")) ? " readonly=\"true\"" : "" %>> 
            </td>
          </tr>
          <tr> 
            <td> <div align="right">Desde Ingresos:</div></td>
            <td> <input name="ingresos_desde" type="text" id="ingresos_desde" size="11" maxlength="11"> 
            </td>
          </tr>
          <tr> 
            <td> <div align="right">Hasta Ingresos:</div></td>
            <td> <input name="ingresos_hasta" type="text" id="ingresos_hasta" size="11" maxlength="11"> 
            </td>
          </tr>
          <tr> 
            <td> <div align="right">C.S.M.:</div></td>
            <td> <input name="csm" type="text" id="csm" size="11" maxlength="11"> 
            </td>
          </tr>
          
        </table>
      </td>
  </tr>
 </table>
</form>
<script language="JavaScript1.2">
document.nom_credsal_dlg.id_cs.value = '<% if(request.getParameter("id_cs") != null) { out.print( request.getParameter("id_cs") ); } else if(!request.getParameter("proceso").equals("AGREGAR_CREDSAL")) { out.print( set.getAbsRow(0).getID_CS() ); } else { out.print(""); } %>' 
document.nom_credsal_dlg.ingresos_desde.value = '<% if(request.getParameter("ingresos_desde") != null) { out.print( request.getParameter("ingresos_desde") ); } else if(!request.getParameter("proceso").equals("AGREGAR_CREDSAL")) { out.print( set.getAbsRow(0).getIngresos_Desde() ); } else { out.print("0"); } %>' 
document.nom_credsal_dlg.ingresos_hasta.value = '<% if(request.getParameter("ingresos_hasta") != null) { out.print( request.getParameter("ingresos_hasta") ); } else if(!request.getParameter("proceso").equals("AGREGAR_CREDSAL")) { out.print( set.getAbsRow(0).getIngresos_Hasta() ); } else { out.print("0"); } %>' 
document.nom_credsal_dlg.csm.value = '<% if(request.getParameter("csm") != null) { out.print( request.getParameter("csm") ); } else if(!request.getParameter("proceso").equals("AGREGAR_CREDSAL")) { out.print( set.getAbsRow(0).getCSM() ); } else { out.print("0"); } %>' 
</script>
</body>
</html>
