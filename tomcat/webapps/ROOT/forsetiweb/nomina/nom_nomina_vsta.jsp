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
<%@ page import="forseti.*, forseti.sets.*"%>
<%
	String nom_nomina = (String)request.getAttribute("nom_nomina");
	if(nom_nomina == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}
	
	String titulo =  JUtil.getSesion(request).getSesion("NOM_NOMINA").generarTitulo();
	String donde = JUtil.getSesion(request).getSesion("NOM_NOMINA").generarWhere();
	String orden = JUtil.getSesion(request).getSesion("NOM_NOMINA").generarOrderBy();
	String colvsta = JUtil.Msj("CEF","NOM_NOMINA","VISTA","COLUMNAS",1);
	String coletq = JUtil.Msj("CEF","NOM_NOMINA","VISTA","COLUMNAS",2);
	int etq = 1, col = 1;
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<script language="JavaScript" type="text/javascript" src="../../compfsi/comps.js">
</script>
<script language="JavaScript" type="text/javascript" src="../../compfsi/staticbar.js">
</script>
<script language="JavaScript" type="text/javascript">
<!-- 
if(top.location == self.location) {
	top.location.href = "../forsetiweb/forseti.html"
}
if(parent.tiempo.document.URL.indexOf('nom_nomina_tmp.jsp') == -1) {
	parent.tiempo.document.location.href = "../forsetiweb/nomina/nom_nomina_tmp.jsp"
}
if(parent.entidad.document.URL.indexOf('nom_nomina_ent.jsp') == -1) {
	parent.entidad.document.location.href = "../forsetiweb/nomina/nom_nomina_ent.jsp"
}
if(parent.ztatuz.document.URL.indexOf('status.html') == -1) {
	parent.ztatuz.document.location.href = "../forsetiweb/status.html"
}
-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form action="/servlet/CEFNomMovDirDlg" method="post" enctype="application/x-www-form-urlencoded" name="nom_nomina" target="_self">
<div id="topbar">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="titCuerpoBco" align="center" valign="middle" bgcolor="#333333"><%= titulo %></td>
  </tr>
<%	
	String mensaje = JUtil.getMensaje(request, response);	
	out.println(mensaje);
%>  
  <tr>
    <td bgcolor="#333333">
		<table width="100%" border="0" cellpadding="0" cellspacing="5">
		 <tr> 
          <td> 
		    <div align="right"> 
			  <input name="proceso" type="hidden" value="ACTUALIZAR">
			  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'AGREGAR_NOMINA',<%= JUtil.Msj("CEF","NOM_NOMINA","VISTA","AGREGAR_NOMINA",4) %>,<%= JUtil.Msj("CEF","NOM_NOMINA","VISTA","AGREGAR_NOMINA",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","NOM_NOMINA","VISTA","AGREGAR_NOMINA") %>" alt="" title="<%= JUtil.Msj("CEF","NOM_NOMINA","VISTA","AGREGAR_NOMINA",2) %>" border="0">
              <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'CAMBIAR_NOMINA',<%= JUtil.Msj("CEF","NOM_NOMINA","VISTA","CAMBIAR_NOMINA",4) %>,<%= JUtil.Msj("CEF","NOM_NOMINA","VISTA","CAMBIAR_NOMINA",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","NOM_NOMINA","VISTA","CAMBIAR_NOMINA") %>" alt="" title="<%= JUtil.Msj("CEF","NOM_NOMINA","VISTA","CAMBIAR_NOMINA",2) %>" border="0">
              <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'CONSULTAR_NOMINA',<%= JUtil.Msj("CEF","NOM_NOMINA","VISTA","CONSULTAR_NOMINA",4) %>,<%= JUtil.Msj("CEF","NOM_NOMINA","VISTA","CONSULTAR_NOMINA",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","NOM_NOMINA","VISTA","CONSULTAR_NOMINA") %>" alt="" title="<%= JUtil.Msj("CEF","NOM_NOMINA","VISTA","CONSULTAR_NOMINA",2) %>" border="0">
			  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'CALCULAR_NOMINA',<%= JUtil.Msj("CEF","NOM_NOMINA","VISTA","CALCULAR_NOMINA",4) %>,<%= JUtil.Msj("CEF","NOM_NOMINA","VISTA","CALCULAR_NOMINA",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","NOM_NOMINA","VISTA","CALCULAR_NOMINA") %>" alt="" title="<%= JUtil.Msj("CEF","NOM_NOMINA","VISTA","CALCULAR_NOMINA",2) %>" border="0">
			  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'MOVER_NOMINA',<%= JUtil.Msj("CEF","NOM_NOMINA","VISTA","MOVER_NOMINA",4) %>,<%= JUtil.Msj("CEF","NOM_NOMINA","VISTA","MOVER_NOMINA",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","NOM_NOMINA","VISTA","MOVER_NOMINA") %>" alt="" title="<%= JUtil.Msj("CEF","NOM_NOMINA","VISTA","MOVER_NOMINA",2) %>" border="0">
			  <input type="image" onClick="javascript: if(confirm('<%= JUtil.Msj("GLB","GLB","GLB","CONFIRMACION",4) %>')) { establecerProcesoSVE(this.form.proceso, 'PROTEGER_NOMINA',<%= JUtil.Msj("CEF","NOM_NOMINA","VISTA","PROTEGER_NOMINA",4) %>,<%= JUtil.Msj("CEF","NOM_NOMINA","VISTA","PROTEGER_NOMINA",5) %>); } else { return false; } " src="../imgfsi/<%= JUtil.Msj("CEF","NOM_NOMINA","VISTA","PROTEGER_NOMINA") %>" alt="" title="<%= JUtil.Msj("CEF","NOM_NOMINA","VISTA","PROTEGER_NOMINA",2) %>" border="0">
			  <input type="image" onClick="javascript: if(confirm('<%= JUtil.Msj("GLB","GLB","GLB","CONFIRMACION",4) %>')) { establecerProcesoSVE(this.form.proceso, 'GENERAR_NOMINA',<%= JUtil.Msj("CEF","NOM_NOMINA","VISTA","GENERAR_NOMINA",4) %>,<%= JUtil.Msj("CEF","NOM_NOMINA","VISTA","GENERAR_NOMINA",5) %>); } else { return false; } " src="../imgfsi/<%= JUtil.Msj("CEF","NOM_NOMINA","VISTA","GENERAR_NOMINA") %>" alt="" title="<%= JUtil.Msj("CEF","NOM_NOMINA","VISTA","GENERAR_NOMINA",2) %>" border="0">
			  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'SELLAR_NOMINA',<%= JUtil.Msj("CEF","NOM_NOMINA","VISTA","SELLAR_NOMINA",4) %>,<%= JUtil.Msj("CEF","NOM_NOMINA","VISTA","SELLAR_NOMINA",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","NOM_NOMINA","VISTA","SELLAR_NOMINA") %>" alt="" title="<%= JUtil.Msj("CEF","NOM_NOMINA","VISTA","SELLAR_NOMINA",2) %>" border="0">
			  <input type="image" onClick="javascript: if(confirm('<%= JUtil.Msj("GLB","GLB","GLB","CONFIRMACION",2) %>')) { establecerProcesoSVE(this.form.proceso, 'ELIMINAR_NOMINA',<%= JUtil.Msj("CEF","NOM_NOMINA","VISTA","ELIMINAR_NOMINA",4) %>,<%= JUtil.Msj("CEF","NOM_NOMINA","VISTA","ELIMINAR_NOMINA",5) %>); } else { return false; } " src="../imgfsi/<%= JUtil.Msj("CEF","NOM_NOMINA","VISTA","ELIMINAR_NOMINA") %>" alt="" title="<%= JUtil.Msj("CEF","NOM_NOMINA","VISTA","ELIMINAR_NOMINA",2) %>" border="0">
			  <a href="/servlet/CEFReportesCtrl?tipo=NOM_NOMINA" target="_self"><img src="../imgfsi/rep_nomina.png" alt="" title="<%= JUtil.Msj("GLB","GLB","GLB","REPORTES") %>" width="30" height="30" border="0"></a> 
              <a href="javascript:try { gestionarArchivos('NOM_NOMINA', document.nom_nomina.id.value, ''); } catch(err) { gestionarArchivos('NOM_NOMINA', '', ''); }" target="_self"><img src="../imgfsi/es_gestionar_archivos.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",5) %>" border="0"></a> 
			  <a href="/servlet/CEFNomMovDirCtrl" target="_self"><img src="../imgfsi/actualizar.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",1) %>" border="0"></a> 
            </div></td>
        </tr>
      </table></td>
  </tr> 
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td bgcolor="#0099FF">
	  <table width="100%" border="0" cellpadding="1" cellspacing="0">
        <tr>
			<td width="3%" align="center">&nbsp;</td>
			<td width="15%" align="left"><a class="titChico" href="/servlet/CEFNomMovDirCtrl?orden=Tipo_Nomina&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
  		  	<td width="5%" align="center"><a class="titChico" href="/servlet/CEFNomMovDirCtrl?orden=Numero_Nomina&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
    		<td width="5%" align="center"><a class="titChico" href="/servlet/CEFNomMovDirCtrl?orden=Ano&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="10%" align="center"><a class="titChico" href="/servlet/CEFNomMovDirCtrl?orden=Fecha_Desde&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
    		<td width="10%" align="center"><a class="titChico" href="/servlet/CEFNomMovDirCtrl?orden=Fecha_Hasta&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
    		<td width="8%" align="left"><a class="titChico" href="/servlet/CEFNomMovDirCtrl?orden=Proteccion&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="11%" align="left"><a class="titChico" href="/servlet/CEFNomMovDirCtrl?orden=Status&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<!--td width="8%" align="left"><a class="titChico" href="/servlet/CEFNomMovDirCtrl?orden=Poliza&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td-->
			<td align="left"><a class="titChico" href="/servlet/CEFNomMovDirCtrl?orden=Pago&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
 		</tr>
	 </table>
	</td>
  </tr>
</table>	
</div>
  <table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
 	  <td height="120" bgcolor="#333333">&nbsp;</td>
 	</tr>
  	<tr>
      <td> 
        <table width="100%" border="0" cellpadding="1" cellspacing="0">
<%
	JNominasModuloSet set = new JNominasModuloSet(request);
	set.m_Where = donde;
	set.m_OrderBy = orden; 
	set.Open();
	for(int i=0; i < set.getNumRows(); i++)
	{
		String status;
		if(set.getAbsRow(i).getStatus().equals("G"))
			status = "Guardada";
		else if(set.getAbsRow(i).getStatus().equals("P"))
			status = "Pagada";
		else
			status = "No identificado";
%>
    	 <tr>
		  <td width="3%" align="center"><input type="radio" name="id" value="<%= set.getAbsRow(i).getID_Nomina() %>"></td>
		  <td width="15%" align="left"><%= set.getAbsRow(i).getTipo_Nomina()  %></td>
		  <td width="5%" align="center"><%= set.getAbsRow(i).getNumero_Nomina()  %></td>
		  <td width="5%" align="center"><%= set.getAbsRow(i).getAno()  %></td>
		  <td width="10%" align="center"><%= JUtil.obtFechaTxt(set.getAbsRow(i).getFecha_Desde(), "dd/MMM/yyyy")  %></td>
		  <td width="10%" align="center"><%= JUtil.obtFechaTxt(set.getAbsRow(i).getFecha_Hasta(), "dd/MMM/yyyy")  %></td>
		  <td width="8%" align="left"><%= set.getAbsRow(i).getProteccion()  %></td>
		  <td width="11%" align="left"><%= status %></td>
		  <!--td width="8%" align="left"><%= set.getAbsRow(i).getPol()  %></td-->
		  <td align="left"><%= set.getAbsRow(i).getPago()  %></td>
  	 	 </tr>		
<%
	}
%>		
     </table>
	 </td>
  </tr>
</table>
</form>
</body>
</html>