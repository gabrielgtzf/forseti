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
	String adm_cfdi = (String)request.getAttribute("adm_cfdi");
	if(adm_cfdi == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}
	
	// Parametros de entidad cuando se generan por primera vez
	String titulo =  JUtil.getSesion(request).getSesion("ADM_CFDI").generarTitulo();
	String donde = JUtil.getSesion(request).getSesion("ADM_CFDI").generarWhere();
	String orden = JUtil.getSesion(request).getSesion("ADM_CFDI").generarOrderBy();
			
	String ent = JUtil.getSesion(request).getSesion("ADM_CFDI").getEspecial();
	
	String colvsta;
	String coletq;
	if(ent.equals("EMISOR"))
	{
		colvsta = JUtil.Msj("CEF","ADM_CFDI","VISTA","COLUMNAS",1);
		coletq = JUtil.Msj("CEF","ADM_CFDI","VISTA","COLUMNAS2",1);
	}
	else if(ent.equals("CERTIFICADOS"))
	{
		colvsta = JUtil.Msj("CEF","ADM_CFDI","VISTA","COLUMNAS",2);
		coletq = JUtil.Msj("CEF","ADM_CFDI","VISTA","COLUMNAS2",2);
	}
	else if(ent.equals("EXPEDICION"))
	{
		colvsta = JUtil.Msj("CEF","ADM_CFDI","VISTA","COLUMNAS",3);
		coletq = JUtil.Msj("CEF","ADM_CFDI","VISTA","COLUMNAS2",3);
	}
	else if(ent.equals("CFDI"))
	{
		if(!JUtil.getSesion(request).getSesion("ADM_CFDI").getStatus().equals("OTROS"))
		{
			colvsta = JUtil.Msj("CEF","ADM_CFDI","VISTA","COLUMNAS",4);
			coletq = JUtil.Msj("CEF","ADM_CFDI","VISTA","COLUMNAS2",4);
		}
		else
		{
			colvsta = JUtil.Msj("CEF","ADM_CFDI","VISTA","COLUMNAS3");
			coletq = JUtil.Msj("CEF","ADM_CFDI","VISTA","COLUMNAS4");
		}
	}
	else // if(ent.equals("CECAT") || ent.equals("CEBAL") || ent.equals("CEPOL"))
	{
		colvsta = JUtil.Msj("CEF","ADM_CFDI","VISTA","COLUMNAS",5);
		coletq = JUtil.Msj("CEF","ADM_CFDI","VISTA","COLUMNAS2",5);
	}
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
parent.tiempo.document.location.href = "../forsetiweb/administracion/adm_cfd_tmp.jsp"
if(parent.entidad.document.URL.indexOf('adm_cfd_ent.jsp') == -1) {
	parent.entidad.document.location.href = "../forsetiweb/administracion/adm_cfd_ent.jsp"
}
parent.ztatuz.document.location.href = "../forsetiweb/administracion/adm_cfd_sts.jsp"
-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="../../compfsi/estilos.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form action="/servlet/CEFAdmCFDDlg" method="post" enctype="application/x-www-form-urlencoded" name="adm_cfd" target="_self">
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
<%
	if(ent.equals("EMISOR"))
	{
%>
			  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'CAMBIAR_EMISOR',<%= JUtil.Msj("CEF","ADM_CFDI","VISTA","CAMBIAR_EMISOR",4) %>,<%= JUtil.Msj("CEF","ADM_CFDI","VISTA","CAMBIAR_EMISOR",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","ADM_CFDI","VISTA","CAMBIAR_EMISOR") %>" alt="" title="<%= JUtil.Msj("CEF","ADM_CFDI","VISTA","CAMBIAR_EMISOR",2) %>" border="0">
<%
	}
	else if(ent.equals("CERTIFICADOS"))
	{
%>
			  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'AGREGAR_CERTIFICADO',<%= JUtil.Msj("CEF","ADM_CFDI","VISTA","AGREGAR_CERTIFICADO",4) %>,<%= JUtil.Msj("CEF","ADM_CFDI","VISTA","AGREGAR_CERTIFICADO",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","ADM_CFDI","VISTA","AGREGAR_CERTIFICADO") %>" alt="" title="<%= JUtil.Msj("CEF","ADM_CFDI","VISTA","AGREGAR_CERTIFICADO",2) %>" border="0">
			  <!--img src="../imgfsi/<%= JUtil.Msj("CEF","ADM_CFDI","VISTA","AGREGAR_CERTIFICADO") %>" onClick="javascript: abrirCatalogo('../forsetiweb/subir_archivos.jsp?verif=/servlet/CEFAdmCFDDlg&archivo_1=key&archivo_2=cer&proceso=AGREGAR_CERTIFICADO',200,450)" alt="" title="<%= JUtil.Msj("CEF","ADM_CFDI","VISTA","AGREGAR_CERTIFICADO",2) %>" border="0"--> 
<%
	}
	else if(ent.equals("EXPEDICION"))
	{
%>
			  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'AGREGAR_EXPEDITOR',<%= JUtil.Msj("CEF","ADM_CFDI","VISTA","AGREGAR_EXPEDITOR",4) %>,<%= JUtil.Msj("CEF","ADM_CFDI","VISTA","AGREGAR_EXPEDITOR",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","ADM_CFDI","VISTA","AGREGAR_EXPEDITOR") %>" alt="" title="<%= JUtil.Msj("CEF","ADM_CFDI","VISTA","AGREGAR_EXPEDITOR",2) %>" border="0">
			  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'CAMBIAR_EXPEDITOR',<%= JUtil.Msj("CEF","ADM_CFDI","VISTA","CAMBIAR_EXPEDITOR",4) %>,<%= JUtil.Msj("CEF","ADM_CFDI","VISTA","CAMBIAR_EXPEDITOR",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","ADM_CFDI","VISTA","CAMBIAR_EXPEDITOR") %>" alt="" title="<%= JUtil.Msj("CEF","ADM_CFDI","VISTA","CAMBIAR_EXPEDITOR",2) %>" border="0">
<%
	}
	else if(ent.equals("CFDI"))
	{
%>
			  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'DESENLAZAR_DOCUMENTO',<%= JUtil.Msj("CEF","ADM_CFDI","VISTA","DESENLAZAR_DOCUMENTO",4) %>,<%= JUtil.Msj("CEF","ADM_CFDI","VISTA","DESENLAZAR_DOCUMENTO",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","ADM_CFDI","VISTA","DESENLAZAR_DOCUMENTO") %>" alt="" title="<%= JUtil.Msj("CEF","ADM_CFDI","VISTA","DESENLAZAR_DOCUMENTO",2) %>" border="0">
			  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'ELIMINAR_DOCUMENTO',<%= JUtil.Msj("CEF","ADM_CFDI","VISTA","ELIMINAR_DOCUMENTO",4) %>,<%= JUtil.Msj("CEF","ADM_CFDI","VISTA","ELIMINAR_DOCUMENTO",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","ADM_CFDI","VISTA","ELIMINAR_DOCUMENTO") %>" alt="" title="<%= JUtil.Msj("CEF","ADM_CFDI","VISTA","ELIMINAR_DOCUMENTO",2) %>" border="0">
<%
	} 
	else // if(ent.equals("CECAT") || ent.equals("CEBAL") || ent.equals("CEPOL"))
	{
%>
			  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'GENERAR_CE',<%= JUtil.Msj("CEF","ADM_CFDI","VISTA","GENERAR_CE",4) %>,<%= JUtil.Msj("CEF","ADM_CFDI","VISTA","GENERAR_CE",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","ADM_CFDI","VISTA","GENERAR_CE") %>" alt="" title="<%= JUtil.Msj("CEF","ADM_CFDI","VISTA","GENERAR_CE",2) %>" border="0">
			  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'CONSULTAR_ST',<%= JUtil.Msj("CEF","ADM_CFDI","VISTA","CONSULTAR_ST",4) %>,<%= JUtil.Msj("CEF","ADM_CFDI","VISTA","CONSULTAR_ST",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","ADM_CFDI","VISTA","CONSULTAR_ST") %>" alt="" title="<%= JUtil.Msj("CEF","ADM_CFDI","VISTA","CONSULTAR_ST",2) %>" border="0">
			  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'CONSULTAR_ARCHIVOS',<%= JUtil.Msj("CEF","ADM_CFDI","VISTA","CONSULTAR_ARCHIVOS",4) %>,<%= JUtil.Msj("CEF","ADM_CFDI","VISTA","CONSULTAR_ARCHIVOS",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","ADM_CFDI","VISTA","CONSULTAR_ARCHIVOS") %>" alt="" title="<%= JUtil.Msj("CEF","ADM_CFDI","VISTA","CONSULTAR_ARCHIVOS",2) %>" border="0">
			  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'VALIDAR_ARCHIVOS',<%= JUtil.Msj("CEF","ADM_CFDI","VISTA","VALIDAR_ARCHIVOS",4) %>,<%= JUtil.Msj("CEF","ADM_CFDI","VISTA","VALIDAR_ARCHIVOS",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","ADM_CFDI","VISTA","VALIDAR_ARCHIVOS") %>" alt="" title="<%= JUtil.Msj("CEF","ADM_CFDI","VISTA","VALIDAR_ARCHIVOS",2) %>" border="0">
			  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'DESCARGAR_ARCHIVOS',<%= JUtil.Msj("CEF","ADM_CFDI","VISTA","DESCARGAR_ARCHIVOS",4) %>,<%= JUtil.Msj("CEF","ADM_CFDI","VISTA","DESCARGAR_ARCHIVOS",5) %>)" src="../imgfsi/<%= JUtil.Msj("CEF","ADM_CFDI","VISTA","DESCARGAR_ARCHIVOS") %>" alt="" title="<%= JUtil.Msj("CEF","ADM_CFDI","VISTA","DESCARGAR_ARCHIVOS",2) %>" border="0">
<%
	}
%>
			  <a href="javascript:try { gestionarArchivos2('ADM_CFDI', '<%= ent %>', document.adm_cfd.id.value, ''); } catch(err) { gestionarArchivos2('ADM_CFDI', '<%= ent %>', '', ''); }" target="_self"><img src="../imgfsi/es_gestionar_archivos.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",5) %>" border="0"></a> 
			  <a href="/servlet/CEFAdmCFDCtrl" target="_self"><img src="../imgfsi/actualizar.png" alt="" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",1) %>" border="0"></a> 
 			 </div></td>
        </tr>
      </table></td>
  </tr> 
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td bgcolor="#0099FF">
	  <table width="100%" border="0" cellpadding="1" cellspacing="0">
<%
	if(ent.equals("EMISOR"))
	{
%>
          <tr>
			<td width="20%" align="left" class="titChico"><%= JUtil.Elm(coletq,etq++) %></a></td>
			<td align="left" class="titChico"><%= JUtil.Elm(coletq,etq++) %></td>
	      </tr>
<%
	}
	else if(ent.equals("CERTIFICADOS"))
	{
%>
          <tr>
			<td width="20%" align="left"><a class="titChico" href="/servlet/CEFAdmCFDCtrl?orden=CFD_NoCertificado&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="40%" align="left"><a class="titChico" href="/servlet/CEFAdmCFDCtrl?orden=CFD_ArchivoCertificado&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="20%" align="center"><a class="titChico" href="/servlet/CEFAdmCFDCtrl?orden=CFD_CaducidadCertificado&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td align="left"><a class="titChico" href="/servlet/CEFAdmCFDCtrl?orden=CFD_CaducidadCertificado&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
	      </tr>
<%
	}
	else if(ent.equals("EXPEDICION") || ent.equals("RECEPTOR"))
	{
%>
          <tr>
			<td width="3%" align="left" class="titChico">&nbsp;</td>
			<td width="5%" align="left"><a class="titChico" href="/servlet/CEFAdmCFDCtrl?orden=<%= ( ent.equals("EXPEDICION") ? "CFD_ID_Expedicion" : "CFD_ID_Receptor") %>&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td align="left"><a class="titChico" href="/servlet/CEFAdmCFDCtrl?orden=CFD_Nombre&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="10%" align="left"><a class="titChico" href="/servlet/CEFAdmCFDCtrl?orden=CFD_Calle&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="5%" align="left"><a class="titChico" href="/servlet/CEFAdmCFDCtrl?orden=CFD_NoExt&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="5%" align="left"><a class="titChico" href="/servlet/CEFAdmCFDCtrl?orden=CFD_NoInt&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="10%" align="left"><a class="titChico" href="/servlet/CEFAdmCFDCtrl?orden=CFD_Colonia&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="10%" align="left"><a class="titChico" href="/servlet/CEFAdmCFDCtrl?orden=CFD_Localidad&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="10%" align="left"><a class="titChico" href="/servlet/CEFAdmCFDCtrl?orden=CFD_Municipio&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="10%" align="left"><a class="titChico" href="/servlet/CEFAdmCFDCtrl?orden=CFD_Estado&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="10%" align="left"><a class="titChico" href="/servlet/CEFAdmCFDCtrl?orden=CFD_Pais&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="7%" align="left"><a class="titChico" href="/servlet/CEFAdmCFDCtrl?orden=CFD_CP&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
		  </tr>
<%
	}
	else if(ent.equals("CFDI"))
	{
		if(!JUtil.getSesion(request).getSesion("ADM_CFDI").getStatus().equals("OTROS"))
		{
%>
		  <tr>
			<td width="3%" align="left" class="titChico">&nbsp;</td>
			<td width="17%" class="titChico"><%= JUtil.Elm(colvsta,col++) %></td>
			<td width="10%" class="titChico"><%= JUtil.Elm(colvsta,col++) %></td>
			<td width="10%" class="titChico"><%= JUtil.Elm(colvsta,col++) %></td>
			<td width="5%" class="titChico"><%= JUtil.Elm(colvsta,col++) %></td>
			<td width="20%" class="titChico" align="center"><%= JUtil.Elm(colvsta,col++) %></td>
			<td width="15%" class="titChico" align="center"><%= JUtil.Elm(colvsta,col++) %></td>
			<td class="titChico"><%= JUtil.Elm(colvsta,col++) %></td>
		</tr>
<%
		}
		else
		{
%>
		  <tr> 
            <td width="3%" class="titChico">&nbsp;</td>
			<td width="20%" class="titChico"><%= JUtil.Elm(colvsta,col++) %></td>
			<td width="10%" class="titChico"><%= JUtil.Elm(colvsta,col++) %></td>
			<td width="15%" class="titChico" align="center"><%= JUtil.Elm(colvsta,col++) %></td>
			<td width="27%" class="titChico"><%= JUtil.Elm(colvsta,col++) %></td>
			<td width="5%" class="titChico" align="center"><%= JUtil.Elm(colvsta,col++) %></td>
			<td width="10%" class="titChico" align="right"><%= JUtil.Elm(colvsta,col++) %></td>
			<td width="10%" class="titChico" align="right"><%= JUtil.Elm(colvsta,col++) %></td>
          </tr>		
<%
		}
	}
	else // if(ent.equals("CECAT") || ent.equals("CEBAL") || ent.equals("CEPOL"))
	{
%>
          <tr>
			<td width="3%" align="left" class="titChico">&nbsp;</td>
			<td width="20%" class="titChico" align="left"><%= JUtil.Elm(colvsta,col++) %></td>
			<td width="15%" align="center" class="titChico"><%= JUtil.Elm(colvsta,col++) %></td>
			<td width="10%" align="center" class="titChico"><%= JUtil.Elm(colvsta,col++) %></td>
			<td width="10%" align="center" class="titChico"><%= JUtil.Elm(colvsta,col++) %></td>
			<td width="10%" align="center" class="titChico"><%= JUtil.Elm(colvsta,col++) %></td>
			<td class="titChico">&nbsp;</td>
		  </tr>
<%
	}
%>
	 </table>
	</td>
  </tr>
</table>	
</div>
  <table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
 	  <td height="125" bgcolor="#333333">&nbsp;</td>
	</tr>
  	<tr>
      <td> 
        <table width="100%" border="0" cellpadding="1" cellspacing="0">
<%
	if(ent.equals("EMISOR"))
	{
		JBDSSet set = new JBDSSet(request);
		set.ConCat(true);
    	set.m_Where = "Nombre = 'FSIBD_" + JUtil.getSesion(request).getBDCompania() + "'";
    	set.Open();
%>
          <tr>
	   		<td width="20%" align="left"><%= JUtil.Msj("GLB","GLB","GLB","NOMBRE") %></td>
			<td align="left"><%= set.getAbsRow(0).getCompania() %></td>
          </tr>
		  <tr>
	   		<td width="20%" align="left"><%= JUtil.Msj("GLB","GLB","GLB","RFC") %></td>
			<td align="left"><%= set.getAbsRow(0).getRFC() %></td>
          </tr>
		  <tr>
	   		<td width="20%" align="left"><%= JUtil.Msj("GLB","GLB","GLB","CFD",2) %></td>
			<td class="titChicoNeg" align="left">
<%		switch(set.getAbsRow(0).getCFD())
		{
			case 0:
				out.print("----------");
				break;
			default:
				out.print(JUtil.Msj("GLB","GLB","GLB","CFD",4));
				break;
		}
%>
			</td>
          </tr>
		  <tr>
	   		<td width="20%" align="left"><%= JUtil.Msj("GLB","GLB","GLB","CALLE") %></td>
			<td align="left"><%= set.getAbsRow(0).getCFD_Calle() %></td>
          </tr>
		  <tr>
	   		<td width="20%" align="left"><%= JUtil.Msj("GLB","GLB","GLB","NUMERO",3) %></td>
			<td align="left"><%= set.getAbsRow(0).getCFD_NoExt() %></td>
          </tr>
		  <tr>
	   		<td width="20%" align="left"><%= JUtil.Msj("GLB","GLB","GLB","NUMERO",4) %></td>
			<td align="left"><%= set.getAbsRow(0).getCFD_NoInt() %></td>
          </tr>
		   <tr>
	   		<td width="20%" align="left"><%= JUtil.Msj("GLB","GLB","GLB","COLONIA") %></td>
			<td align="left"><%= set.getAbsRow(0).getCFD_Colonia() %></td>
          </tr>
		  <tr>
	   		<td width="20%" align="left"><%= JUtil.Msj("GLB","GLB","GLB","LOCALIDAD") %></td>
			<td align="left"><%= set.getAbsRow(0).getCFD_Localidad() %></td>
          </tr>
		   <tr>
	   		<td width="20%" align="left"><%= JUtil.Msj("GLB","GLB","GLB","MUNICIPIO") %></td>
			<td align="left"><%= set.getAbsRow(0).getCFD_Municipio() %></td>
          </tr>
		  <tr>
	   		<td width="20%" align="left"><%= JUtil.Msj("GLB","GLB","GLB","ESTADO") %></td>
			<td align="left"><%= set.getAbsRow(0).getCFD_Estado() %></td>
          </tr>
		   <tr>
	   		<td width="20%" align="left"><%= JUtil.Msj("GLB","GLB","GLB","PAIS") %></td>
			<td align="left"><%= set.getAbsRow(0).getCFD_Pais() %></td>
          </tr>
		  <tr>
	   		<td width="20%" align="left"><%= JUtil.Msj("GLB","GLB","GLB","CP") %></td>
			<td align="left"><%= set.getAbsRow(0).getCFD_CP() %></td>
          </tr>
		  <tr>
	   		<td width="20%" align="left"><%= JUtil.Msj("GLB","GLB","GLB","REGIMEN_FISCAL") %></td>
			<td align="left"><%= set.getAbsRow(0).getCFD_RegimenFiscal() %></td>
          </tr>
<%
	}
	else if(ent.equals("CERTIFICADOS"))
	{
		JCFDCertificadosSet set = new JCFDCertificadosSet(request);
		set.Open();
		for(int i=0; i < set.getNumRows(); i++)
		{
			
%>
          <tr>
	      	<td width="20%" align="left"><%= set.getAbsRow(i).getCFD_NoCertificado() %></td>
			<td width="40%" align="left"><%= set.getAbsRow(i).getCFD_ArchivoCertificado() %></td>
		 	<td width="20%" align="center"><%= JUtil.obtFechaTxt(set.getAbsRow(i).getCFD_CaducidadCertificado(),"dd/MMM/yyyy" ) %></td>
		 	<td align="left"><%= JUtil.obtHoraTxt(set.getAbsRow(i).getCFD_HoraCaducidadCertificado(),"HH:mm:ss" ) %></td>
		 </tr>
<%
		}
	}
	else if(ent.equals("EXPEDICION") || ent.equals("RECEPTOR"))
	{
		JCFDExpRecSet set;
		if(ent.equals("EXPEDICION"))
			set = new JCFDExpRecSet(request,"EXP");
		else
			set = new JCFDExpRecSet(request,"REC");
		set.Open();
		
		for(int i=0; i < set.getNumRows(); i++)
		{
			
%>
		  <tr>
			<td width="3%" align="left"><input type="radio" name="id" value="<%= set.getAbsRow(i).getCFD_ID_ExpRec() %>"></td>
			<td width="5%" align="left"><%= set.getAbsRow(i).getCFD_ID_ExpRec() %></td>
			<td align="left"><%= set.getAbsRow(i).getCFD_Nombre() %></td>
			<td width="10%" align="left"><%= set.getAbsRow(i).getCFD_Calle() %></td>
			<td width="5%" align="left"><%= set.getAbsRow(i).getCFD_NoExt() %></td>
			<td width="5%" align="left"><%= set.getAbsRow(i).getCFD_NoInt() %></td>
			<td width="10%" align="left"><%= set.getAbsRow(i).getCFD_Colonia() %></td>
			<td width="10%" align="left"><%= set.getAbsRow(i).getCFD_Localidad() %></td>
			<td width="10%" align="left"><%= set.getAbsRow(i).getCFD_Municipio() %></td>
			<td width="10%" align="left"><%= set.getAbsRow(i).getCFD_Estado() %></td>
			<td width="10%" align="left"><%= set.getAbsRow(i).getCFD_Pais() %></td>
			<td width="7%" align="left"><%= set.getAbsRow(i).getCFD_CP() %></td>
		  </tr>
<%
		}
	}
	else if(ent.equals("CFDI"))
	{
		String status = JUtil.getSesion(request).getSesion("ADM_CFDI").getStatus();
		String statusTit = JUtil.getSesion(request).getSesion("ADM_CFDI").getStatusTit();
		JUtil.getSesion(request).getSesion("ADM_CFDI").setStatus("","");
		if(!status.equals("OTROS"))
		{
			JCFDCompViewSet set = new JCFDCompViewSet(request,status);
			set.m_Where = JUtil.getSesion(request).getSesion("ADM_CFDI").generarWhere();
			set.m_OrderBy = orden;
			set.Open();
			//out.println(set.getSQL());
			for(int i=0; i < set.getNumRows(); i++)
			{
%>
		  <tr>
			<td width="3%"><input name="id" type="radio" value="<%= set.getAbsRow(i).getID_CFD() %>">&nbsp;</td>
			<td width="17%"><%= set.getAbsRow(i).getEnlace() %></td>
			<td width="10%"><%= set.getAbsRow(i).getFecha() %></td>
			<td width="10%"><%= set.getAbsRow(i).getTotal() %></td>
			<td width="5%" align="center"><%= set.getAbsRow(i).getEfectoStr() %></td>
			<td width="20%" align="center"><%= set.getAbsRow(i).getUUID() %></td>
			<td width="15%" align="center"><%= set.getAbsRow(i).getRFC() %></td>
			<td><%= set.getAbsRow(i).getFechaTimbre() + " " + set.getAbsRow(i).getHoraTimbre() %></td>
	      </tr>
<%
			}
		}
		else
		{
			JCFDCompOtrSet sotr = new JCFDCompOtrSet(request);
			sotr.m_Where = JUtil.getSesion(request).getSesion("ADM_CFDI").generarWhere();
			sotr.m_OrderBy = orden;
			sotr.Open();
			//out.println(sotr.getSQL());
			for(int i = 0; i < sotr.getNumRows(); i++)
			{
%>
		  <tr> 
			<td width="3%"><input name="id" type="radio" value="<%= sotr.getAbsRow(i).getID_CFD() %>">&nbsp;</td>
			<td width="20%"><%= sotr.getAbsRow(i).getTipo() + ": " + sotr.getAbsRow(i).getEnlace() %> </td>
			<td width="10%"><%= sotr.getAbsRow(i).getFactura() %></td>
			<td width="15%" align="center"><%= sotr.getAbsRow(i).getFecha() %></td>
			<td width="27%"><%= sotr.getAbsRow(i).getNombre_Original() %></td>
			<td width="5%" align="center"><%= sotr.getAbsRow(i).getID_Moneda() %></td>
			<td width="10%" align="right"><%= sotr.getAbsRow(i).getTC() %></td>
			<td width="10%" align="right"><%= sotr.getAbsRow(i).getTotal() %></td>
		  </tr>	
<%
			}
		}
		JUtil.getSesion(request).getSesion("ADM_CFDI").setStatus(status,statusTit);
	}
	else // if(ent.equals("CECAT") || ent.equals("CEBAL") || ent.equals("CEPOL"))
	{
		JAdmPeriodosSet perIni = new JAdmPeriodosSet(request);
		perIni.setSQL("select * from TBL_CONT_CATALOGO_PERIODOS order by Ano Asc, Mes Asc limit 1");
		perIni.Open();
		
		JContCEModuloSet set = new JContCEModuloSet(request, ent);
		set.m_OrderBy = "Ano DESC, Mes DESC"; 
		set.Open();
	
		for(int i=0; i < set.getNumRows(); i++)
		{
			String meslargo = "", generado, error = "", alerta = "";
			if((set.getAbsRow(i).getMes() == perIni.getAbsRow(0).getMes() && set.getAbsRow(i).getAno() == perIni.getAbsRow(0).getAno())
					|| set.getAbsRow(i).getMes() == 13)
				continue;
			else
				meslargo = JUtil.convertirMesLargo(set.getAbsRow(i).getMes());
			generado = (set.getAbsRow(i).getGenerado().equals("G") ? "X" : "---");
			if(set.getAbsRow(i).getGenerado().equals("G"))
				error = Integer.toString(set.getAbsRow(i).getErrores());
			if(set.getAbsRow(i).getGenerado().equals("G"))
				alerta = Integer.toString(set.getAbsRow(i).getAlertas());
%>			
          <tr>
	      	<td width="3%" align="left"><input type="radio" name="id" value="<%= set.getAbsRow(i).getAno() + "|" + set.getAbsRow(i).getMes() %>"></td>
			<td width="20%" align="left"><%= meslargo %></td>
			<td width="15%" align="center"><%= set.getAbsRow(i).getAno() %></td>
 			<td width="10%" align="center"><%= generado %></td>
 			<td width="10%" align="center"><%= error %></td>
 			<td width="10%" align="center"><%= alerta %></td>
			<td>&nbsp;</td>
          </tr>		
<%
		}	
	}
%>
     </table>
	 </td>
  </tr>
</table>
</form>
</body>
</html>
