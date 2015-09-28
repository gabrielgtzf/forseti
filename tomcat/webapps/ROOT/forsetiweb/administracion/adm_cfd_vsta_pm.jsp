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
	else //EXPEDICION / RECEPCION
	{
		colvsta = JUtil.Msj("CEF","ADM_CFDI","VISTA","COLUMNAS",3);
		coletq = JUtil.Msj("CEF","ADM_CFDI","VISTA","COLUMNAS2",3);
	}
	int etq = 1, col = 1;
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
<link href="../compfsi/estilos.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#333333" text="#000000" link="#0099FF" vlink="#FF0000" alink="#000099" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form action="/servlet/CEFAdmCFDDlg" method="post" enctype="application/x-www-form-urlencoded" name="adm_cfd">
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
			  <a href="../forsetiweb/menu_pm.jsp"><img src="../imgfsi/menu_principal.png" title="<%= JUtil.Msj("GLB","GLB","GLB","HERRAMIENTAS",4) %>" width="24" height="24" border="0"/></a><img src="../imgfsi/pixel_333333.gif" width="5" height="24"/>
			  <a href="../forsetiweb/administracion/adm_cfd_ent_pm.jsp"><img src="../imgfsi/p_izq_on.png" title="<%= JUtil.Msj("GLB","GLB","GLB","PANELES_PM",1) %>" width="24" height="24" border="0"/></a><img src="../imgfsi/pixel_333333.gif" width="5" height="24"/>
			  <img src="../imgfsi/p_inf_off.png" title="<%= JUtil.Msj("GLB","GLB","GLB","PANELES_PM",2) %>" width="24" height="24" border="0"/><img src="../imgfsi/pixel_333333.gif" width="5" height="24"/>
			  <img src="../imgfsi/p_der_off.png" title="<%= JUtil.Msj("GLB","GLB","GLB","PANELES_PM",3) %>" width="24" height="24" border="0"/></td>
  			<td width="50%" align="right" valign="middle">
				<a href="/servlet/CEFAdmCFDCtrl"><img src="../imgfsi/actualizar24.png" title="<%= JUtil.Msj("GLB","VISTA","GLB","HERRAMIENTAS",1) %>" width="24" height="24" border="0"/></a><img src="../imgfsi/pixel_333333.gif" width="5" height="24"/> 
				<a href="/servlet/CEFRegistro"><img src="../imgfsi/inicio.png" title="<%= JUtil.Msj("GLB","GLB","GLB","HERRAMIENTAS",1) %>" width="24" height="24" border="0" /></a><img src="../imgfsi/pixel_333333.gif" width="5" height="24"/>
				<a href="/servlet/CEFSalir"><img src="../imgfsi/cerrar_sesion.png" title="<%= JUtil.Msj("GLB","GLB","GLB","HERRAMIENTAS",2) + " " + JUtil.getSesion(request).getNombreUsuario() %>" width="24" height="24" border="0"/></a><img src="../imgfsi/pixel_333333.gif" width="5" height="24"/>
				<a href="../../forsetidoc/040105.html"><img src="../imgfsi/ayudacef.png" title="<%= JUtil.Msj("GLB","GLB","GLB","HERRAMIENTAS",3) %>" width="24" height="24" border="0"/></a></td>
  		  </tr>
		</table>
	</td>
  </tr>
  <tr> 
    <td align="center" class="titCuerpoAzc"><%= titulo %></td>
  </tr>
<%	
	String mensaje = JUtil.getMensaje(request, response);	
	out.println(mensaje);
%>  
  <tr> 
    <td align="right"> 
			  <input name="proceso" type="hidden" value="ACTUALIZAR">
<%
	if(ent.equals("EMISOR"))
	{
%>
			  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'CAMBIAR_EMISOR',<%= JUtil.Msj("CEF","ADM_CFDI","VISTA","CAMBIAR_EMISOR",4) %>,<%= JUtil.Msj("CEF","ADM_CFDI","VISTA","CAMBIAR_EMISOR",5) %>)" src="../imgfsi/pm_cambiar_bd.png" title="<%= JUtil.Msj("CEF","ADM_CFDI","VISTA","CAMBIAR_EMISOR",2) %>" width="24" height="24" border="0"/><img src="../imgfsi/pixel_333333.gif" width="5" height="24"/>
<%
	}
	else if(ent.equals("CERTIFICADOS"))
	{
%>
			  <img src="../imgfsi/pm_agregar_bd.png" onClick="javascript: abrirCatalogo('../forsetiweb/subir_archivos_pm.jsp?verif=/servlet/CEFAdmCFDDlg&archivo_1=key&archivo_2=cer&proceso=AGREGAR_CERTIFICADO',200,450)" title="<%= JUtil.Msj("CEF","ADM_CFDI","VISTA","AGREGAR_CERTIFICADO",2) %>" width="24" height="24" border="0"/><img src="../imgfsi/pixel_333333.gif" width="5" height="24"/>
<%
	}
	else if(ent.equals("EXPEDICION"))
	{
%>
			  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'AGREGAR_EXPEDITOR',<%= JUtil.Msj("CEF","ADM_CFDI","VISTA","AGREGAR_EXPEDITOR",4) %>,<%= JUtil.Msj("CEF","ADM_CFDI","VISTA","AGREGAR_EXPEDITOR",5) %>)" src="../imgfsi/pm_agregar_bd.png" title="<%= JUtil.Msj("CEF","ADM_CFDI","VISTA","AGREGAR_EXPEDITOR",2) %>" width="24" height="24" border="0"/><img src="../imgfsi/pixel_333333.gif" width="5" height="24"/>
			  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'CAMBIAR_EXPEDITOR',<%= JUtil.Msj("CEF","ADM_CFDI","VISTA","CAMBIAR_EXPEDITOR",4) %>,<%= JUtil.Msj("CEF","ADM_CFDI","VISTA","CAMBIAR_EXPEDITOR",5) %>)" src="../imgfsi/pm_cambiar_bd.png" title="<%= JUtil.Msj("CEF","ADM_CFDI","VISTA","CAMBIAR_EXPEDITOR",2) %>" width="24" height="24" border="0"/><img src="../imgfsi/pixel_333333.gif" width="5" height="24"/>
<%
	}
	else if(ent.equals("RECEPTOR"))
	{
%>
			  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'AGREGAR_RECEPTOR',<%= JUtil.Msj("CEF","ADM_CFDI","VISTA","AGREGAR_RECEPTOR",4) %>,<%= JUtil.Msj("CEF","ADM_CFDI","VISTA","AGREGAR_RECEPTOR",5) %>)" src="../imgfsi/pm_agregar_bd.png" title="<%= JUtil.Msj("CEF","ADM_CFDI","VISTA","AGREGAR_RECEPTOR",2) %>" width="24" height="24" border="0"/><img src="../imgfsi/pixel_333333.gif" width="5" height="24"/>
			  <input name="submit" type="image" onClick="javascript:establecerProcesoSVE(this.form.proceso, 'CAMBIAR_RECEPTOR',<%= JUtil.Msj("CEF","ADM_CFDI","VISTA","CAMBIAR_RECEPTOR",4) %>,<%= JUtil.Msj("CEF","ADM_CFDI","VISTA","CAMBIAR_RECEPTOR",5) %>)" src="../imgfsi/pm_cambiar_bd.png" title="<%= JUtil.Msj("CEF","ADM_CFDI","VISTA","CAMBIAR_RECEPTOR",2) %>" width="24" height="24" border="0"/><img src="../imgfsi/pixel_333333.gif" width="5" height="24"/>
<%
	}
%>
	</td>
  </tr> 
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td bgcolor="#0099FF">
<%
	if(ent.equals("EMISOR"))
	{
%>
		 <table width="100%" border="0" cellpadding="5" cellspacing="0">
          <tr>
			<td width="40%" class="titChico"><%= JUtil.Elm(coletq,etq++) %></a></td>
			<td class="titChico"><%= JUtil.Elm(coletq,etq++) %></td>
	      </tr>
		</table>
<%
	}
	else if(ent.equals("CERTIFICADOS"))
	{
%>
		 <table width="100%" border="0" cellpadding="5" cellspacing="0">
          <tr>
			<td width="30%"><a class="titChico" href="/servlet/CEFAdmCFDCtrl?orden=CFD_NoCertificado&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="30%"><a class="titChico" href="/servlet/CEFAdmCFDCtrl?orden=CFD_ArchivoCertificado&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="20%" align="center"><a class="titChico" href="/servlet/CEFAdmCFDCtrl?orden=CFD_CaducidadCertificado&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td align="center"><a class="titChico" href="/servlet/CEFAdmCFDCtrl?orden=CFD_CaducidadCertificado&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
	      </tr>
		 </table>
<%
	}
	else if(ent.equals("EXPEDICION") || ent.equals("RECEPTOR"))
	{
%>
		 <table width="100%" border="0" cellpadding="5" cellspacing="0">
          <tr>
			<td width="10%" align="center" class="titChico">&nbsp;</td>
			<td width="10%"><a class="titChico" href="/servlet/CEFAdmCFDCtrl?orden=<%= ( ent.equals("EXPEDICION") ? "CFD_ID_Expedicion" : "CFD_ID_Receptor") %>&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td><a class="titChico" href="/servlet/CEFAdmCFDCtrl?orden=CFD_Nombre&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="25%"><a class="titChico" href="/servlet/CEFAdmCFDCtrl?orden=CFD_Calle&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="10%"><a class="titChico" href="/servlet/CEFAdmCFDCtrl?orden=CFD_NoExt&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="10%"><a class="titChico" href="/servlet/CEFAdmCFDCtrl?orden=CFD_NoInt&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
		   </tr>
		  </table>
		  <table width="100%" border="0" cellpadding="5" cellspacing="0">
           <tr>	
			<td width="20%"><a class="titChico" href="/servlet/CEFAdmCFDCtrl?orden=CFD_Colonia&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="20%"><a class="titChico" href="/servlet/CEFAdmCFDCtrl?orden=CFD_Localidad&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="20%"><a class="titChico" href="/servlet/CEFAdmCFDCtrl?orden=CFD_Municipio&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="15%"><a class="titChico" href="/servlet/CEFAdmCFDCtrl?orden=CFD_Estado&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td width="15%"><a class="titChico" href="/servlet/CEFAdmCFDCtrl?orden=CFD_Pais&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
			<td><a class="titChico" href="/servlet/CEFAdmCFDCtrl?orden=CFD_CP&etq=<%= JUtil.Elm(coletq,etq++) %>"><%= JUtil.Elm(colvsta,col++) %></a></td>
		  </tr>
		 </table>
<%
	}
%>
	</td>
  </tr>
</table>	
</div>
<table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF">
    <tr>
 	  <td height="265">&nbsp;</td>
	</tr>
  	<tr>
      <td> 
<%
	if(ent.equals("EMISOR"))
	{
		JBDSSet set = new JBDSSet(request);
		set.ConCat(true);
    	set.m_Where = "Nombre = 'FSIBD_" + JUtil.getSesion(request).getBDCompania() + "'";
    	set.Open();
%>
		<table width="100%" border="0" cellpadding="5" cellspacing="0">
          <tr>
	   		<td width="40%"><%= JUtil.Msj("GLB","GLB","GLB","NOMBRE") %></td>
			<td><%= set.getAbsRow(0).getCompania() %></td>
          </tr>
		  <tr>
	   		<td><%= JUtil.Msj("GLB","GLB","GLB","RFC") %></td>
			<td><%= set.getAbsRow(0).getRFC() %></td>
          </tr>
		  <tr>
	   		<td><%= JUtil.Msj("GLB","GLB","GLB","CFD",2) %></td>
			<td class="titChicoNeg">
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
	   		<td><%= JUtil.Msj("GLB","GLB","GLB","CALLE") %></td>
			<td><%= set.getAbsRow(0).getCFD_Calle() %></td>
          </tr>
		  <tr>
	   		<td><%= JUtil.Msj("GLB","GLB","GLB","NUMERO",3) %></td>
			<td><%= set.getAbsRow(0).getCFD_NoExt() %></td>
          </tr>
		  <tr>
	   		<td><%= JUtil.Msj("GLB","GLB","GLB","NUMERO",4) %></td>
			<td><%= set.getAbsRow(0).getCFD_NoInt() %></td>
          </tr>
		  <tr>
	   		<td><%= JUtil.Msj("GLB","GLB","GLB","COLONIA") %></td>
			<td><%= set.getAbsRow(0).getCFD_Colonia() %></td>
          </tr>
		  <tr>
	   		<td><%= JUtil.Msj("GLB","GLB","GLB","LOCALIDAD") %></td>
			<td><%= set.getAbsRow(0).getCFD_Localidad() %></td>
          </tr>
		  <tr>
	   		<td><%= JUtil.Msj("GLB","GLB","GLB","MUNICIPIO") %></td>
			<td><%= set.getAbsRow(0).getCFD_Municipio() %></td>
          </tr>
		  <tr>
	   		<td><%= JUtil.Msj("GLB","GLB","GLB","ESTADO") %></td>
			<td><%= set.getAbsRow(0).getCFD_Estado() %></td>
          </tr>
		  <tr>
	   		<td><%= JUtil.Msj("GLB","GLB","GLB","PAIS") %></td>
			<td><%= set.getAbsRow(0).getCFD_Pais() %></td>
          </tr>
		  <tr>
	   		<td><%= JUtil.Msj("GLB","GLB","GLB","CP") %></td>
			<td><%= set.getAbsRow(0).getCFD_CP() %></td>
          </tr>
		  <tr>
	   		<td><%= JUtil.Msj("GLB","GLB","GLB","REGIMEN_FISCAL") %></td>
			<td><%= set.getAbsRow(0).getCFD_RegimenFiscal() %></td>
          </tr>
		 </table>
<%
	}
	else if(ent.equals("CERTIFICADOS"))
	{
		JCFDCertificadosSet set = new JCFDCertificadosSet(request);
		set.Open();
		for(int i=0; i < set.getNumRows(); i++)
		{
			
%>
        <table width="100%" border="0" cellpadding="5" cellspacing="0">
  		 <tr>
	      	<td width="30%"><%= set.getAbsRow(i).getCFD_NoCertificado() %></td>
			<td width="30%"><%= set.getAbsRow(i).getCFD_ArchivoCertificado() %></td>
		 	<td width="20%" align="center"><%= JUtil.obtFechaTxt(set.getAbsRow(i).getCFD_CaducidadCertificado(),"dd/MMM/yyyy" ) %></td>
		 	<td align="center"><%= JUtil.obtHoraTxt(set.getAbsRow(i).getCFD_HoraCaducidadCertificado(),"HH:mm:ss" ) %></td>
		 </tr>
		</table>
<%
		}
	}
	else
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
		 <table width="100%" border="0" cellpadding="5" cellspacing="0">
 		  <tr>
			<td width="10%" align="center"><input type="radio" name="id" value="<%= set.getAbsRow(i).getCFD_ID_ExpRec() %>"/></td>
			<td width="10%"><%= set.getAbsRow(i).getCFD_ID_ExpRec() %></td>
			<td><%= set.getAbsRow(i).getCFD_Nombre() %></td>
			<td width="25%"><%= set.getAbsRow(i).getCFD_Calle() %></td>
			<td width="10%"><%= set.getAbsRow(i).getCFD_NoExt() %></td>
			<td width="10%"><%= set.getAbsRow(i).getCFD_NoInt() %></td>
		  </tr>
	     </table>	
		 <table width="100%" border="0" cellpadding="5" cellspacing="0">
 		  <tr>
		    <td width="20%"><%= set.getAbsRow(i).getCFD_Colonia() %></td>
			<td width="20%"><%= set.getAbsRow(i).getCFD_Localidad() %></td>
			<td width="20%"><%= set.getAbsRow(i).getCFD_Municipio() %></td>
			<td width="15%"><%= set.getAbsRow(i).getCFD_Estado() %></td>
			<td width="15%"><%= set.getAbsRow(i).getCFD_Pais() %></td>
			<td><%= set.getAbsRow(i).getCFD_CP() %></td>
		  </tr>
		 </table>
<%
		}
	}
%>
	 </td>
  </tr>
</table>
</form>
</body>
</html>
