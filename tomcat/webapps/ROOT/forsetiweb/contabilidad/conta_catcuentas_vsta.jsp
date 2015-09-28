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
	String conta_catcuentas = (String)request.getAttribute("conta_catcuentas");
	if(conta_catcuentas == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}
	
	// Parametros de entidad cuando se generan por primera vez
	String titulo =  JUtil.getSesion(request).getSesion("CONT_CATCUENTAS").generarTitulo();
	String donde = JUtil.getSesion(request).getSesion("CONT_CATCUENTAS").generarWhere();
	String orden = JUtil.getSesion(request).getSesion("CONT_CATCUENTAS").generarOrderBy();
	String colvsta = JUtil.Msj("CEF","CONT_CATCUENTAS","VISTA","COLUMNAS",2);
	String coletq = JUtil.Msj("CEF","CONT_CATCUENTAS","VISTA","COLUMNAS",1);
	int etq = 1, col = 1;
	String sts = JUtil.Msj("CEF", "CONT_CATCUENTAS", "VISTA", "STATUS",2);
	String nat = JUtil.Msj("CEF", "CONT_CATCUENTAS", "DLG", "NATURALEZA");
		
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
if(parent.tiempo.document.URL.indexOf('tiempo.html') == -1) {
	parent.tiempo.document.location.href = "../forsetiweb/tiempo.html"
}
if(parent.entidad.document.URL.indexOf('conta_catcuentas_ent.jsp') == -1) {
	parent.entidad.document.location.href = "../forsetiweb/contabilidad/conta_catcuentas_ent.jsp"
}
if(parent.ztatuz.document.URL.indexOf('conta_catcuentas_sts.jsp') == -1) {
	parent.ztatuz.document.location.href = "../forsetiweb/contabilidad/conta_catcuentas_sts.jsp"
}
-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form action="/servlet/CEFContaCatcuentasDlg" method="post" enctype="application/x-www-form-urlencoded" name="conta_catcuentas">
<div id="topbar">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="titCuerpoBco" align="center" valign="middle" bgcolor="#333333"><%= titulo  %></td>
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
           	  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'AGREGAR_CUENTA')" src="../imgfsi/<%= JUtil.Msj("CEF","CONT_CATCUENTAS","VISTA","AGREGAR_CUENTA") %>" alt="" title="<%= JUtil.Msj("CEF","CONT_CATCUENTAS","VISTA","AGREGAR_CUENTA",2) %>" border="0">
              <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'CAMBIAR_CUENTA')" src="../imgfsi/<%= JUtil.Msj("CEF","CONT_CATCUENTAS","VISTA","CAMBIAR_CUENTA") %>" alt="" title="<%= JUtil.Msj("CEF","CONT_CATCUENTAS","VISTA","CAMBIAR_CUENTA",2) %>" border="0">
			  <input type="image" onClick="javascript: if(confirm('<%= JUtil.Msj("GLB","GLB","GLB","CONFIRMACION",2) %>')) { establecerProcesoSVE(this.form.proceso, 'ELIMINAR_CUENTA'); } else { return false; } " src="../imgfsi/<%= JUtil.Msj("CEF","CONT_CATCUENTAS","VISTA","ELIMINAR_CUENTA") %>" alt="" title="<%= JUtil.Msj("CEF","CONT_CATCUENTAS","VISTA","ELIMINAR_CUENTA",2) %>" border="0">
              <a href="javascript:try { gestionarArchivosCta('CONT_CATCUENTAS', document.conta_catcuentas.cuenta.value, ''); } catch(err) { gestionarArchivosCta('CONT_CATCUENTAS', '', ''); }" target="_self"><img src="../imgfsi/es_gestionar_archivos.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",5) %>" border="0"></a> 
			  <a href="/servlet/CEFContaCatCuentasCtrl" target="_self"><img src="../imgfsi/actualizar.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",1) %>" border="0"></a> 
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
			<td width="5%" align="center">&nbsp;</td>
		  	<td width="15%" align="left"><a class="titChico" href="/servlet/CEFContaCatCuentasCtrl?orden=Cuenta&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
            <td align="left"><a class="titChico" href="/servlet/CEFContaCatCuentasCtrl?orden=Nombre&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
            <td width="10%" align="center"><a class="titChico" href="/servlet/CEFContaCatCuentasCtrl?orden=Estatus&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
		    <td width="10%" align="right"><a class="titChico" href="/servlet/CEFContaCatCuentasCtrl?orden=Saldo&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
		    <td width="10%" align="center"><a class="titChico" href="/servlet/CEFContaCatCuentasCtrl?orden=CE_CodAgrup&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
		    <td width="10%" align="center"><a class="titChico" href="/servlet/CEFContaCatCuentasCtrl?orden=CE_Natur&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
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
	JContaCatalogSetV2 set = new JContaCatalogSetV2(request);
	set.m_Where = donde;
	set.m_OrderBy = orden; 
	set.Open();
	for(int i=0; i < set.getNumRows(); i++)
	{
		String status, clase, naturaleza;
		
	   	if(set.getAbsRow(i).getEstatus().equals("A"))
		{
			status = JUtil.Elm(sts,2); 
			clase = (!set.getAbsRow(i).getAC()) ? " class=\"txtChicoNg\"" : " class=\"titChicoAzc\"";
		}
		else if(set.getAbsRow(i).getEstatus().equals("D"))
		{
			status = JUtil.Elm(sts,3);
			clase = (!set.getAbsRow(i).getAC()) ? " class=\"txtChicoRj\"" : " class=\"titChicoRj\"";
		}
		else
		{
			status = "";
			clase = "";
		} 	
		if(set.getAbsRow(i).getCE_Natur().equals("A"))
			naturaleza = JUtil.Elm(nat,3);
		else if(set.getAbsRow(i).getCE_Natur().equals("D"))
			naturaleza = JUtil.Elm(nat,2);
		else
			naturaleza = JUtil.Elm(nat,1);
%>
       <tr<%= clase %>>
		  <td width="5%" align="center"><input type="radio" name="cuenta" value="<%= JUtil.obtCuentaFormato(new StringBuffer(set.getAbsRow(i).getCuenta()), request) %>"></td>
		  <td width="15%" align="left"><%= JUtil.obtCuentaFormato(new StringBuffer(set.getAbsRow(i).getCuenta()), request) %></td>
		  <td align="left"><%= set.getAbsRow(i).getNombre() %></td>
          <td width="10%" align="center"><%= status %></td>
          <td width="10%" align="right"><%= set.getAbsRow(i).getSaldo() %></td>
		  <td width="10%" align="center"><%= set.getAbsRow(i).getCE_CodAgrup() %></td>
		  <td width="10%" align="center"><%= naturaleza %></td>
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
