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

	JNominasModuloSet set = new JNominasModuloSet(request);
	set.m_Where = "ID_Nomina = '" + JUtil.p(request.getParameter("id")) + "'";
	set.Open();
	
	JCalculoNominaEspSet cset = new JCalculoNominaEspSet(request);
	cset.m_Where = "ID_Nomina = '" + JUtil.p(request.getParameter("id")) + "'";
	cset.m_OrderBy = "Recibo asc";
	cset.Open();
	
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
function ventanaDos(ancho, alto)
{
	parametrs = "toolbar=0,location=0,directories=0,status=1,menubar=0,scrollbars=1,resizable=1,width=" + ancho + ",height=" + alto;
	ventana = window.open('', 'ventEm2', parametrs);
	ventana.focus();
}
-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css"></head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form action="/servlet/CEFNomMovDirDlg" method="post" enctype="application/x-www-form-urlencoded" name="nom_nomina_dlg" target="_self">
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
             		<input type="submit" name="aceptar" disabled="true" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>">
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/CEFNomMovDirCtrl'" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>">
            </td>
          </tr>
        </table> 
      </td>
    </tr>
<%
	if(request.getParameter("proceso").equals("MOVER_NOMINA"))
	{
%> 
   <tr> 
     <td bgcolor="#333333"> 	
	   <table width="100%" border="0" cellpadding="0" cellspacing="5">
		 <tr> 
          <td> 
		    <div align="right">
			 <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'CARGAR_RECIBO',<%= JUtil.Msj("CEF","NOM_NOMINA","VISTA","CARGAR_RECIBO",4) %>,<%= JUtil.Msj("CEF","NOM_NOMINA","VISTA","CARGAR_RECIBO",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","NOM_NOMINA","VISTA","CARGAR_RECIBO") %>" alt="" title="<%= JUtil.Msj("CEF","NOM_NOMINA","VISTA","CARGAR_RECIBO",2) %>" border="0">
   			 <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'ENLAZAR_RECIBO',<%= JUtil.Msj("CEF","NOM_NOMINA","VISTA","ENLAZAR_RECIBO",4) %>,<%= JUtil.Msj("CEF","NOM_NOMINA","VISTA","ENLAZAR_RECIBO",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","NOM_NOMINA","VISTA","ENLAZAR_RECIBO") %>" alt="" title="<%= JUtil.Msj("CEF","NOM_NOMINA","VISTA","ENLAZAR_RECIBO",2) %>" border="0">
   			 <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'AGR_EMP',<%= JUtil.Msj("CEF","NOM_NOMINA","VISTA","AGR_EMP",4) %>,<%= JUtil.Msj("CEF","NOM_NOMINA","VISTA","AGR_EMP",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","NOM_NOMINA","VISTA","AGR_EMP") %>" alt="" title="<%= JUtil.Msj("CEF","NOM_NOMINA","VISTA","AGR_EMP",2) %>" border="0">
             <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'CAM_EMP',<%= JUtil.Msj("CEF","NOM_NOMINA","VISTA","CAM_EMP",4) %>,<%= JUtil.Msj("CEF","NOM_NOMINA","VISTA","CAM_EMP",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","NOM_NOMINA","VISTA","CAM_EMP") %>" alt="" title="<%= JUtil.Msj("CEF","NOM_NOMINA","VISTA","CAM_EMP",2) %>" border="0">
      		 <input type="image" onClick="javascript: if(confirm('<%= JUtil.Msj("GLB","GLB","GLB","CONFIRMACION",2) %>')) { establecerProcesoSVE(this.form.proceso, 'BORR_EMP',<%= JUtil.Msj("CEF","NOM_NOMINA","VISTA","BORR_EMP",4) %>,<%= JUtil.Msj("CEF","NOM_NOMINA","VISTA","BORR_EMP",5) %>); } else { return false; } " src="../imgfsi/<%= JUtil.Msj("CEF","NOM_NOMINA","VISTA","BORR_EMP") %>" alt="" title="<%= JUtil.Msj("CEF","NOM_NOMINA","VISTA","BORR_EMP",2) %>" border="0">
		     <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'XML_RECIBO',<%= JUtil.Msj("CEF","NOM_NOMINA","VISTA","XML_RECIBO",4) %>,<%= JUtil.Msj("CEF","NOM_NOMINA","VISTA","XML_RECIBO",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","NOM_NOMINA","VISTA","XML_RECIBO") %>" alt="" title="<%= JUtil.Msj("CEF","NOM_NOMINA","VISTA","XML_RECIBO",2) %>" border="0">
             <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'PDF_RECIBO',<%= JUtil.Msj("CEF","NOM_NOMINA","VISTA","PDF_RECIBO",4) %>,<%= JUtil.Msj("CEF","NOM_NOMINA","VISTA","PDF_RECIBO",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","NOM_NOMINA","VISTA","PDF_RECIBO") %>" alt="" title="<%= JUtil.Msj("CEF","NOM_NOMINA","VISTA","PDF_RECIBO",2) %>" border="0">
             <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'ENVIAR_RECIBO',<%= JUtil.Msj("CEF","NOM_NOMINA","VISTA","ENVIAR_RECIBO",4) %>,<%= JUtil.Msj("CEF","NOM_NOMINA","VISTA","ENVIAR_RECIBO",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","NOM_NOMINA","VISTA","ENVIAR_RECIBO") %>" alt="" title="<%= JUtil.Msj("CEF","NOM_NOMINA","VISTA","ENVIAR_RECIBO",2) %>" border="0">
             <a href="/servlet/CEFNomMovDirDlg?proceso=MOVER_NOMINA&id=<%= request.getParameter("id") %>" target="_self"><img src="../imgfsi/actualizar.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",1) %>" border="0"></a> 
			 <input name="submit" type="image" onClick="javascript:establecerProceso(this.form.proceso, 'IMPRIMIR',400,250)" src="../imgfsi/imprimir.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",2) %>" border="0">
              <a href="../../forsetidoc/040203.html" target="_blank"><img src="../imgfsi/ayuda32Cef.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",3) %>" border="0"></a> 
    		 </div>
			  </td>
  			</tr>
		</table>
	</td>
  </tr>
<%
 	}
%>
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
            <td width="15%"> <input name="proceso" type="hidden" value="<%= request.getParameter("proceso") %>"> 
              								<input name="id" type="hidden" value="<%= request.getParameter("id") %>">
              N&oacute;mina:</td>
            <td width="15%" class="titChicoAzc"><%= set.getAbsRow(0).getID_Nomina() %></td>
            <td width="15%">N&uacute;mero</td>
            <td width="15%" class="titChicoAzc"><%= set.getAbsRow(0).getNumero_Nomina() %></td>
            <td width="15%">A&ntilde;o:</td>
            <td width="15%" class="titChicoAzc"><%= set.getAbsRow(0).getAno() %></td>
          </tr>
          <tr> 
            <td width="15%">Desde:</td>
            <td width="15%" class="titChicoAzc"><%= JUtil.obtFechaTxt(set.getAbsRow(0).getFecha_Desde(), "dd/MMM/yyyy") %></td>
            <td width="15%">Hasta:</td>
            <td width="15%" class="titChicoAzc"><%= JUtil.obtFechaTxt(set.getAbsRow(0).getFecha_Hasta(), "dd/MMM/yyyy") %></td>
            <td width="15%">Dias:</td>
            <td width="15%" class="titChicoAzc"><%= set.getAbsRow(0).getDias() %></td>
          </tr>
          <tr> 
            <td colspan="6">
			 <table width="100%" border="0" cellspacing="0" cellpadding="2">
               <tr bgcolor="#0099FF">
				<td width="5%" class="titChico">&nbsp;</td>
                <td width="10%" class="titChico">Clave</td>
                <td class="titChico">Nombre</td>
                <td width="5%" align="center" class="titChico">Rbo</td>
				<td width="5%" align="center" class="titChico">Flt</td>
				<td width="5%" align="center" class="titChico">IXA</td>
				<td width="5%" align="center" class="titChico">IXE</td>
				<td width="5%" align="center" class="titChico">IXM</td>
				<td width="5%" align="right" class="titChico">HE</td>
				<td width="5%" align="right" class="titChico">HT</td>
	           	<td width="5%" align="right" class="titChico">HD</td>
				<td width="3%" align="center" class="titChico">CFDI</td>
	           </tr>
			   <tr>
					<td colspan="12">
						<table width="100%" border="0" cellspacing="0" cellpadding="2">
						  <tr bgcolor="#999999">
							<td width="5%" align="right">&nbsp;</td>
							<td width="5%" align="center" class="titChico">Mov</td>
							<td width="60%" align="left" class="titChico">Descripcion</td>
							<td width="10%" align="right" class="titChico">Gravado</td>
							<td width="10%" align="right" class="titChico">Exento</td>
							<td width="10%" align="right" class="titChico">Deduccion</td>
						  </tr>
						</table>
					</td>
				</tr>
<%
		float totgravado = 0.0f, totexento = 0.0f, totdeduccion = 0.0f;
		for(int i = 0; i < cset.getNumRows(); i++)
		{
%>
                <tr> 
				  <td width="5%">
<%
			if(request.getParameter("proceso").equals("MOVER_NOMINA"))
			{
%>
					  <input type="radio" name="idempleado" value="<%= cset.getAbsRow(i).getID_Empleado() %>">
<%
			}
			else
			{
					out.print("&nbsp;");
			}
%>					  
				</td>
                  	<td width="10%" class="titChicoNeg"><%= cset.getAbsRow(i).getID_Empleado() %></td>
                  	<td class="titChicoNeg"><%= cset.getAbsRow(i).getNombre() %></td>
                  	<td width="5%" align="center" class="titChicoNeg"><%= cset.getAbsRow(i).getRecibo() %></td>
					<td width="5%" align="center" class="titChicoNeg"><%= cset.getAbsRow(i).getFaltas() %></td>
				 	<td width="5%" align="center" class="titChicoNeg"><%= cset.getAbsRow(i).getIXA() %></td>
				 	<td width="5%" align="center" class="titChicoNeg"><%= cset.getAbsRow(i).getIXE() %></td>
				 	<td width="5%" align="center" class="titChicoNeg"><%= cset.getAbsRow(i).getIXM() %></td>
				 	<td width="5%" align="right" class="titChicoNeg"><%= cset.getAbsRow(i).getHE() %></td>
	           		<td width="5%" align="right" class="titChicoNeg"><%= cset.getAbsRow(i).getHT() %></td>
		        	<td width="5%" align="right" class="titChicoNeg"><%= cset.getAbsRow(i).getHD() %></td>
					<td width="3%" align="center" class="titChicoNeg"><% if(cset.getAbsRow(i).getTFD() == 3) { out.print("PDF"); } else if(cset.getAbsRow(i).getTFD() == 2) { out.print("TFD"); } else { out.print("---"); } %></td>
		        </tr>
				<tr>
					<td colspan="12">
<% 
			JCalculoNominaDetSet dset = new JCalculoNominaDetSet(request);
			dset.m_Where = "ID_Nomina = '" + cset.getAbsRow(i).getID_Nomina()  + "' and ID_Empleado = '" +  JUtil.p(cset.getAbsRow(i).getID_Empleado()) + "'";
			dset.m_OrderBy = "EsDeduccion asc, ID_Movimiento asc";
			dset.Open();
			float sumgravado = 0.0f, sumexento = 0.0f, sumdeduccion = 0.0f;
			for(int j = 0; j < dset.getNumRows(); j++)
			{
					sumgravado += dset.getAbsRow(j).getGravado();
					sumexento += dset.getAbsRow(j).getExento();
					sumdeduccion += dset.getAbsRow(j).getDeduccion();
%>					
				  		<table width="100%" border="0" cellspacing="0" cellpadding="2">
						  <tr>
							<td width="5%" align="right">&nbsp;</td>
							<td width="5%" align="center"><%= dset.getAbsRow(j).getID_Movimiento() %></td>
							<td width="60%" align="left"><%= dset.getAbsRow(j).getDescripcion() %></td>
							<td width="10%" align="right"><%= JUtil.Converts(dset.getAbsRow(j).getGravado(),",",".",2,true) %></td>
							<td width="10%" align="right"><%=  JUtil.Converts(dset.getAbsRow(j).getExento(),",",".",2,true) %></td>
							<td width="10%" align="right"><%=  JUtil.Converts(dset.getAbsRow(j).getDeduccion(),",",".",2,true) %></td>
						  </tr>
						</table>
<%
			}
%>
						<table width="100%" border="0" cellspacing="0" cellpadding="2">
						  <tr>
							<td width="5%" align="right">&nbsp;</td>
							<td width="5%" align="center">&nbsp;</td>
							<td width="60%" align="left">&nbsp;</td>
							<td width="10%" align="right" class="titChicoNeg"><%= JUtil.Converts(sumgravado,",",".",2,true) %></td>
							<td width="10%" align="right" class="titChicoNeg"><%=  JUtil.Converts(sumexento,",",".",2,true) %></td>
							<td width="10%" align="right" class="titChicoNeg"><%=  JUtil.Converts(sumdeduccion,",",".",2,true) %></td>
						  </tr>
						</table>
						<table width="100%" border="0" cellspacing="0" cellpadding="2">
						  <tr>
							<td width="5%" align="right">&nbsp;</td>
							<td width="5%" align="center">&nbsp;</td>
							<td width="60%" align="right" class="titChicoNeg">Neto:</td>
							<td width="10%" align="right">&nbsp;</td>
							<td width="10%" align="right">&nbsp;</td>
							<td width="10%" align="right" class="titChicoNeg"><%=  JUtil.Converts((sumgravado + sumexento + sumdeduccion),",",".",2,true) %></td>
						  </tr>
						</table>
					</td>
				</tr>
<%
			totgravado += sumgravado;
			totexento += sumexento;
			totdeduccion += sumdeduccion;
		}
%>
            	<tr>
					<td colspan="11">
					   <table width="100%" border="0" cellspacing="0" cellpadding="2">
                      	<tr>
							<td width="5%" align="right">&nbsp;</td>
							<td width="5%" align="center">&nbsp;</td>
							<td width="60%" align="right">&nbsp;</td>
							<td width="10%" align="right" class="titChicoAzc"><%=  JUtil.Converts(totgravado,",",".",2,true) %></td>
							<td width="10%" align="right" class="titChicoAzc"><%=  JUtil.Converts(totexento,",",".",2,true) %></td>
							<td width="10%" align="right" class="titChicoAzc"><%=  JUtil.Converts(totdeduccion,",",".",2,true) %></td>
						  </tr>
						</table>
					</td>
				</tr>
				<tr>
					<td colspan="11">
					   <table width="100%" border="0" cellspacing="0" cellpadding="2">
                      <tr>
							<td width="5%" align="right">&nbsp;</td>
							<td width="5%" align="center">&nbsp;</td>
							<td width="60%" align="right" class="titChicoAzc">Total 
                          N&oacute;mina </td>
							<td width="10%" align="right">&nbsp;</td>
							<td width="10%" align="right">&nbsp;</td>
							<td width="10%" align="right" class="titChicoAzc"><%=  JUtil.Converts((totgravado + totexento + totdeduccion),",",".",2,true) %></td>
						  </tr>
						</table>
					</td>
				</tr>
              </table></td>
          </tr>
         </table>
      </td>
  </tr>
 </table>
</form>
</body>
</html>
