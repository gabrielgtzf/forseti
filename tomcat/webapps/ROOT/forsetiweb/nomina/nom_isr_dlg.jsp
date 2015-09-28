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
	String nom_isr_dlg = (String)request.getAttribute("nom_isr_dlg");
	if(nom_isr_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}

	String titulo =  JUtil.getSesion(request).getSesion("NOM_ISR").generarTitulo(JUtil.Msj("CEF","NOM_ISR","VISTA",request.getParameter("proceso"),3),"../../forsetidoc/040202.html");

	JIsrSet set = new JIsrSet(request);
	if( request.getParameter("proceso").equals("CAMBIAR_ISR") || request.getParameter("proceso").equals("CONSULTAR_ISR") )
	{
		set.m_Where = "ID_Isr = '" + JUtil.p(request.getParameter("id")) + "'";
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
	if(formAct.proceso.value == "AGREGAR_ISR" || formAct.proceso.value == "CAMBIAR_ISR")
	{
		if( !esNumeroEntero('Clave:', formAct.id_isr.value, 1, 254)  ||
			!esNumeroDecimal('Limite Inferior: ', formAct.limite_inferior.value, 0, 99999999.99, 2)  ||
			!esNumeroDecimal('Limite Superior: ', formAct.limite_superior.value, 0, 99999999.99, 2)  ||
			!esNumeroDecimal('Cuota Fija: ', formAct.cuota_fija.value, 0, 99999999.99, 2)  ||
			!esNumeroDecimal('% Exedente: ', formAct.porcentaje_exd.value, 0, 99999999.99, 2)  ||
			!esNumeroDecimal('Sibsidio: ', formAct.subsidio.value, 0, 99999999.99, 2)  ||
			!esNumeroDecimal('S.I.M.: ', formAct.subsidio_sim.value, 0, 99999999.99, 2)  ||
			 !esNumeroDecimal('Limite Inferior Anual: ', formAct.limite_inferior_anual.value, 0, 99999999.99, 2)  ||
			!esNumeroDecimal('Limite Superior Anual: ', formAct.limite_superior_anual.value, 0, 99999999.99, 2)  ||
			!esNumeroDecimal('Cuota Fija Anual: ', formAct.cuota_fija_anual.value, 0, 99999999.99, 2)  ||
			!esNumeroDecimal('% Exedente Anual: ', formAct.porcentaje_exd_anual.value, 0, 99999999.99, 2)  ||
			!esNumeroDecimal('Sibsidio Anual: ', formAct.subsidio_anual.value, 0, 99999999.99, 2)  ||
			!esNumeroDecimal('S.I.M. Anual: ', formAct.subsidio_sim_anual.value, 0, 99999999.99, 2)  )
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
<form onSubmit="return enviarlo(this)" action="/servlet/CEFNomIsrDlg" method="post" enctype="application/x-www-form-urlencoded" name="nom_isr_dlg" target="_self">
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
              <%  if(JUtil.getSesion(request).getID_Mensaje() == 0 || request.getParameter("proceso").equals("CONSULTAR_ISR")) { %>
        			<input type="submit" name="aceptar" disabled="true" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
        			<%  } else { %>
        			<input type="submit" name="aceptar" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
       				<%  } %>
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/CEFNomIsrCtrl'" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
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
            <td colspan="2"> 
              <div align="right"> 
                <input name="proceso" type="hidden" value="<%= request.getParameter("proceso")%>">
                <input name="subproceso" type="hidden" value="ENVIAR">
                <input name="id" type="hidden" value="<%= request.getParameter("id")%>">
                Clave:</div></td>
            <td colspan="5"> <input name="id_isr" type="text" id="id_isr" size="8" maxlength="3"<%= (request.getParameter("proceso").equals("CAMBIAR_ISR")) ? " readonly=\"true\"" : "" %>> 
            </td>
          </tr>
          <tr> 
            <td align="left">&nbsp; </td>
            <td width="14%" align="center">L&iacute;m. inferior</td>
            <td width="14%" align="center">L&iacute;m. superior</td>
            <td width="14%" align="center">Cuota Fija</td>
            <td width="14%" align="center">% Exedente</td>
            <td width="14%" align="center">Subsidio</td>
            <td width="14%" align="center">S.I.M.</td>
          </tr>
          <tr> 
            <td align="left" class="titChicoAzc">Normal:</td>
            <td align="center"><input name="limite_inferior" type="text" id="limite_inferior" size="7" maxlength="11"></td>
            <td align="center"><input name="limite_superior" type="text" id="limite_superior" size="7" maxlength="11"></td>
             <td align="center"><input name="cuota_fija" type="text" id="cuota_fija" size="7" maxlength="11"></td>
            <td align="center"><input name="porcentaje_exd" type="text" id="porcentaje_exd" size="7" maxlength="11"></td>
             <td align="center"><input name="subsidio" type="text" id="subsidio" size="7" maxlength="11"></td>
            <td align="center"><input name="subsidio_sim" type="text" id="subsidio_sim" size="7" maxlength="11"></td>
          </tr>
		  <tr> 
            <td align="left" class="titChicoAzc">Anual:</td>
            <td align="center"><input name="limite_inferior_anual" type="text" id="limite_inferior_anual" size="7" maxlength="11"></td>
            <td align="center"><input name="limite_superior_anual" type="text" id="limite_superior_anual" size="7" maxlength="11"></td>
             <td align="center"><input name="cuota_fija_anual" type="text" id="cuota_fija_anual" size="7" maxlength="11"></td>
            <td align="center"><input name="porcentaje_exd_anual" type="text" id="porcentaje_exd_anual" size="7" maxlength="11"></td>
             <td align="center"><input name="subsidio_anual" type="text" id="subsidio_anual" size="7" maxlength="11"></td>
            <td align="center"><input name="subsidio_sim_anual" type="text" id="subsidio_sim_anual" size="7" maxlength="11"></td>
          </tr>
         
        </table>
      </td>
  </tr>
 </table>
</form>
<script language="JavaScript1.2">
document.nom_isr_dlg.id_isr.value = '<% if(request.getParameter("id_isr") != null) { out.print( request.getParameter("id_isr") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ISR")) { out.print( set.getAbsRow(0).getID_Isr() ); } else { out.print(""); } %>'  
document.nom_isr_dlg.limite_superior.value = '<% if(request.getParameter("limite_superior") != null) { out.print( request.getParameter("limite_superior") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ISR")) { out.print( set.getAbsRow(0).getLimite_Superior() ); } else { out.print("0"); } %>' 
document.nom_isr_dlg.limite_superior_anual.value = '<% if(request.getParameter("limite_superior_anual") != null) { out.print( request.getParameter("limite_superior_anual") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ISR")) { out.print( set.getAbsRow(0).getLimite_Superior_Anual() ); } else { out.print("0"); } %>' 
document.nom_isr_dlg.limite_inferior_anual.value = '<% if(request.getParameter("limite_inferior_anual") != null) { out.print( request.getParameter("limite_inferior_anual") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ISR")) { out.print( set.getAbsRow(0).getLimite_Inferior_Anual() ); } else { out.print("0"); } %>' 
document.nom_isr_dlg.limite_inferior.value = '<% if(request.getParameter("limite_inferior") != null) { out.print( request.getParameter("limite_inferior") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ISR")) { out.print( set.getAbsRow(0).getLimite_Inferior() ); } else { out.print("0"); } %>' 
document.nom_isr_dlg.subsidio.value = '<% if(request.getParameter("subsidio") != null) { out.print( request.getParameter("subsidio") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ISR")) { out.print( set.getAbsRow(0).getSubsidio() ); } else { out.print("0"); } %>' 
document.nom_isr_dlg.subsidio_anual.value = '<% if(request.getParameter("subsidio_anual") != null) { out.print( request.getParameter("subsidio_anual") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ISR")) { out.print( set.getAbsRow(0).getSubsidio_Anual() ); } else { out.print("0"); } %>' 
document.nom_isr_dlg.subsidio_sim_anual.value = '<% if(request.getParameter("subsidio_sim_anual") != null) { out.print( request.getParameter("subsidio_sim_anual") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ISR")) { out.print( set.getAbsRow(0).getSubsidio_SIM_Anual() ); } else { out.print("0"); } %>' 
document.nom_isr_dlg.subsidio_sim.value = '<% if(request.getParameter("subsidio_sim") != null) { out.print( request.getParameter("subsidio_sim") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ISR")) { out.print( set.getAbsRow(0).getSubsidio_SIM() ); } else { out.print("0"); } %>' 
document.nom_isr_dlg.cuota_fija.value = '<% if(request.getParameter("cuota_fija") != null) { out.print( request.getParameter("cuota_fija") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ISR")) { out.print( set.getAbsRow(0).getCuota_Fija() ); } else { out.print("0"); } %>' 
document.nom_isr_dlg.cuota_fija_anual.value = '<% if(request.getParameter("cuota_fija_anual") != null) { out.print( request.getParameter("cuota_fija_anual") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ISR")) { out.print( set.getAbsRow(0).getCuota_Fija_Anual() ); } else { out.print("0"); } %>' 
document.nom_isr_dlg.porcentaje_exd.value = '<% if(request.getParameter("porcentaje_exd") != null) { out.print( request.getParameter("porcentaje_exd") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ISR")) { out.print( set.getAbsRow(0).getPorcentaje_Exd() ); } else { out.print("0"); } %>' 
document.nom_isr_dlg.porcentaje_exd_anual.value = '<% if(request.getParameter("porcentaje_exd_anual") != null) { out.print( request.getParameter("porcentaje_exd_anual") ); } else if(!request.getParameter("proceso").equals("AGREGAR_ISR")) { out.print( set.getAbsRow(0).getPorcentaje_Exd_Anual() ); } else { out.print("0"); } %>' 
</script>
</body>
</html>
