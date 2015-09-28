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
<%@ page import="forseti.*, forseti.sets.*, forseti.almacen.*, java.util.*, java.io.*"%>
<%
	String alm_traspasos_dlg = (String)request.getAttribute("alm_traspasos_dlg");
	if(alm_traspasos_dlg == null)
	{
		RequestDispatcher despachador = getServletContext().getRequestDispatcher("/forsetiweb/errorAtributos.jsp");
      	despachador.forward(request,response);
		return;
	}
	
	String titulo =  ( request.getParameter("tipomov").equals("TRASPASOS") ? 
						JUtil.getSesion(request).getSesion("ALM_TRASPASOS").generarTitulo(JUtil.Msj("CEF","ALM_TRASPASOS","VISTA",request.getParameter("proceso"),3)) :
						JUtil.getSesion(request).getSesion("ALM_REQUERIMIENTOS").generarTitulo(JUtil.Msj("CEF","ALM_REQUERIMIENTOS","VISTA",request.getParameter("proceso"),3)) );
	String coletq = JUtil.Msj("CEF","ALM_TRASPASOS","DLG","COLUMNAS",1);
	int etq = 1;
	
	session = request.getSession(true);
    JAlmTraspasosSes rec = ( request.getParameter("tipomov").equals("TRASPASOS") ? 
								(JAlmTraspasosSes)session.getAttribute("alm_traspasos_dlg") :
								(JAlmTraspasosSes)session.getAttribute("alm_requerimientos_dlg") );

   
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
<script language="JavaScript" type="text/javascript" src="../../compfsi/cefdatetimepicker_pm.js" >
</script>
<script language="JavaScript" type="text/javascript">
<!--
function limpiarFormulario()
{
	document.alm_traspasos_dlg.cantidad.value = "";
	document.alm_traspasos_dlg.idprod.value = "";
	document.alm_traspasos_dlg.idprod_nombre.value = "";
}

function editarPartida(idpartida, cantidad, idprod, idprod_nombre)
{
	document.alm_traspasos_dlg.idpartida.value = idpartida;
	document.alm_traspasos_dlg.subproceso.value = "EDIT_PART";

	document.alm_traspasos_dlg.cantidad.value = cantidad;
	document.alm_traspasos_dlg.idprod.value = idprod;
	document.alm_traspasos_dlg.idprod_nombre.value = idprod_nombre;
}

function enviarlo(formAct)
{
	if(formAct.subproceso.value == "AGR_PART" || formAct.subproceso.value == "EDIT_PART")
	{
		if(	!esNumeroDecimal("<%= JUtil.Msj("GLB","GLB","GLB","CANTIDAD") %>:", formAct.cantidad.value, 0, 9999999999, 2) ||
			!esCadena("<%= JUtil.Msj("GLB","GLB","GLB","CLAVE") %>:", formAct.idprod.value, 1, 20) )
			return false;
		
	}
	else
	{	
		if(!esNumeroEntero('<%= JUtil.Msj("GLB","GLB","GLB","CLAVE") %>:', formAct.clave.value, 0, 255) )
			return false;
		
		if(formAct.subproceso.value == "ENVIAR")
		{
			if(confirm("<%= JUtil.Msj("GLB","GLB","GLB","CONFIRMACION") %>"))
			{
				formAct.aceptar.disabled = true;
				return true;
			}
			else
				return false;
		}
		else
			return true;
	}
	
}
-->
</script>
<link href="../compfsi/estilos.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#333333" leftmargin="0" topmargin="0" rightmargin="0" bottommargin="0" marginwidth="0" marginheight="0">
<form onSubmit="return enviarlo(this)" action="/servlet/CEFAlmTraspasosDlg" method="post" enctype="application/x-www-form-urlencoded" name="alm_traspasos_dlg" target="_self">
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
              <%  if(JUtil.getSesion(request).getID_Mensaje() == 0 || request.getParameter("proceso").equals("CONSULTAR_TRASPASO")) { %>
        			<input type="submit" name="aceptar" disabled="true" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>"/>
        			<%  } else { %>
        			<input type="submit" name="aceptar" value="<%= JUtil.Msj("GLB","GLB","GLB","ACEPTAR") %>"/>
       				<%  } %>
        			<input type="button" name="cancelar" onClick="javascript:document.location.href='/servlet/<%= ( request.getParameter("tipomov").equals("TRASPASOS") ? "CEFAlmTraspasosCtrl" : "CEFAlmRequerimientosCtrl" ) %>'" value="<%= JUtil.Msj("GLB","GLB","GLB","CANCELAR") %>"/></td>
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
          <td width="20%">
		  	<input name="tipomov" type="hidden" value="<%= request.getParameter("tipomov") %>"/>
			<input name="proceso" type="hidden" value="<%= request.getParameter("proceso")%>"/>
			<input name="subproceso" type="hidden" id="subproceso" value="ENVIAR"/>
			<input name="idbodegases" type="hidden" id="idbodegases" value="<%= rec.getID_BodegaDest() %>"/>
			<input type="hidden" name="idpartida" value="<%= request.getParameter("idpartida") %>"/>
                    <%= JUtil.Msj("GLB","GLB","GLB","NUMERO",2) %>
                    <% if(request.getParameter("tipomov").equals("TRASPASOS")) { out.print(JUtil.Msj("GLB","GLB","GLB","TRASPASO")); } else if(request.getParameter("tipomov").equals("REQUERIMIENTOS")) { out.print(JUtil.Msj("GLB","GLB","GLB","REQUERIMIENTO") ); }%>
           </td>
		   <td><%= JUtil.Msj("GLB","GLB","GLB","FECHA") %></td>
           <td width="25%"><%= JUtil.Msj("GLB","GLB","GLB","REFERENCIA") %></td>  
		 </tr>
		 <tr>          
		   <td><input name="traspaso" type="text" id="traspaso" style="width:100%" maxlength="10" readonly="true"/></td>
           <td><table width="100%"><tr><td><input name="fecha" type="text" id="fecha" style="width:100%" maxlength="15" readonly="true"/></td><td width="24">
                          <a href="javascript:NewCal('fecha','ddmmmyyyy',false)"><img src="../../imgfsi/calendario.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CALENDARIO") %>" border="0" align="absmiddle" width="24" height="24"/></a></td></tr></table></td>
           <td><input name="referencia" type="text" id="referencia" style="width:100%" maxlength="25"></td>
         </tr>
       </table>
	   <table width="100%" border="0" cellspacing="5" cellpadding="5">
         <tr> 
           <td width="20%"><%= JUtil.Msj("GLB","GLB","GLB","BODEGA") %></td>
           <td><table width="100%"><tr><td width="30%"> 
<% 
		if( request.getParameter("proceso").equals("AGREGAR_TRASPASO")  || request.getParameter("proceso").equals("CAMBIAR_TRASPASO") ) 
		{ 
%>
					<input name="idbodega" type="text" id="idbodega" onBlur="javascript: if(this.form.idbodega.value != this.form.idbodegases.value) { establecerProcesoSVE(this.form.subproceso, 'AGR_BODEGA'); this.form.submit(); }" style="width:100%" maxlength="10"/></td><td width="24">
                    <a href="javascript:abrirCatalogo('../../forsetiweb/listas_dlg_pm.jsp?formul=alm_traspasos_dlg&lista=idbodega&idcatalogo=20&nombre=BODEGAS&destino=idbodega_nombre',250,350)"><img src="../../imgfsi/catalogo.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CATALOGO") %>" align="absmiddle" width="24" height="24" border="0"/></a></td> 
<% 	
		} 
		else
		{
				out.print(rec.getID_BodegaDest() + "</td>");
		}
%>
                    <td><input name="idbodega_nombre" type="text" id="idbodega_nombre"  value="<%= rec.getBodegaDest_Descripcion() %>" style="width:100%" maxlength="250" readonly="true"/></td></tr></table>
                </td>
             </tr>
            </table>
			<table width="100%" border="0" cellspacing="5" cellpadding="5">
             <tr> 
               <td><%= JUtil.Msj("GLB","GLB","GLB","CONCEPTO") %></td>
             </tr>
			 <tr>
			   <td><input name="concepto" type="text" id="concepto" style="width:100%" maxlength="80"/></td>
             </tr>
            </table>
			<table bgcolor="#0099FF" width="100%" border="0" cellspacing="5" cellpadding="5">
                <tr> 
                  <td width="20%" align="right" class="titChico"><%= JUtil.Elm(coletq,etq++) %></td>
                  <td width="15%" class="titChico"><%= JUtil.Elm(coletq,etq++) %></td>
                  <td width="30%" class="titChico"><%= JUtil.Elm(coletq,etq++) %></td>
                  <td class="titChico"><%= JUtil.Elm(coletq,etq++) %></td>
				</tr>
				<tr>
				  <td colspan="4">&nbsp;</td>
                </tr>
			</table>
<%
	if( !request.getParameter("proceso").equals("CONSULTAR_TRASPASO") )
	{
%>				
             <table width="100%" border="0" cellspacing="5" cellpadding="5">
               <tr> 
                  <td width="20%" align="right"> <input name="cantidad" type="text" class="cpoBco" id="cantidad" style="width:100%" maxlength="12"/></td>
                  <td width="15%">&nbsp;</td>
                  <td width="30%"><table width="100%"><tr><td><input name="idprod" type="text" class="cpoBco" id="idprod" style="width:100%" maxlength="20"/></td><td width="24"> 
                    <a href="javascript:abrirCatalogo('../../forsetiweb/listas_dlg.jsp?formul=alm_traspasos_dlg&lista=idprod&idcatalogo=19&nombre=PRODUCTOS&destino=idprod_nombre',250,350)"><img src="../../imgfsi/catalogo.gif" title="<%= JUtil.Msj("GLB","GLB","DLG","CATALOGO") %>" align="absmiddle" width="24" height="24" border="0"/></a></td></tr></table></td>
                  <td> <input name="idprod_nombre" type="text" class="cpoBco" id="idprod_nombre" style="width:100%" maxlength="120" readonly="true"></td>
                </tr>
				<tr>
				  <td colspan="4" align="right">
					<input name="submit_agr" type="image" id="submit_agr" onClick="javascript:if(this.form.subproceso.value != 'EDIT_PART') { establecerProcesoSVE(this.form.subproceso, 'AGR_PART'); }" src="../../imgfsi/lista_ok.gif" border="0" width="24" height="24"/>
                    <a href="javascript:limpiarFormulario();"><img src="../../imgfsi/lista_x.gif" width="24" height="24" border="0"/></a></td>
                </tr>
			  </table>
<%
	}
	
	if(rec.numPartidas() == 0)
	{
			out.println("<table width=\"100%\"><tr><td align=\"center\" class=\"titChicoAzc\">" + JUtil.Msj("GLB","GLB","DLG","CERO-PART") + "</td></tr></table>");
	}
	else
	{						
		for(int i = 0; i < rec.numPartidas(); i++)
		{
%>
            <table width="100%" border="0" cellspacing="5" cellpadding="5">
               <tr>  
                  <td width="20%" align="right"><%= rec.getPartida(i).getCantidad() %></td>
                  <td width="15%"><%= rec.getPartida(i).getUnidad() %></td>
                  <td width="30%"><%= rec.getPartida(i).getID_Prod() %></td>
                  <td><%= rec.getPartida(i).getID_ProdNombre() %></td>
			   </tr>
			   <tr>
			      <td colspan="3"><%= rec.getPartida(i).getObs() %></td>
			   	  <td align="right"><% if(!request.getParameter("proceso").equals("CONSULTAR_TRASPASO")) { %><a href="javascript:editarPartida('<%= i %>','<%= rec.getPartida(i).getCantidad() %>','<%= rec.getPartida(i).getID_Prod() %>','<%= rec.getPartida(i).getID_ProdNombre() %>');"><img src="../../imgfsi/lista_ed.gif" border="0" width="24" height="24"/></a>
             			 <input name="submit" type="image" onClick="javascript:this.form.idpartida.value = '<%= i %>'; establecerProcesoSVE(this.form.subproceso, 'BORR_PART');" src="../../imgfsi/lista_x.gif" border="0" width="24" height="24"/><% } else { out.print("&nbsp;"); } %></td>
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
<script language="JavaScript1.2">
<% 
		if( request.getParameter("proceso").equals("AGREGAR_TRASPASO")  ||  request.getParameter("proceso").equals("CAMBIAR_TRASPASO") ) 
		{ 
%>
document.alm_traspasos_dlg.idbodega.value = '<%= rec.getID_BodegaDest() %>'
<% 
		}
%>
document.alm_traspasos_dlg.fecha.value = '<%=  JUtil.obtFechaTxt(rec.getFecha(),"dd/MMM/yyyy") %>'
document.alm_traspasos_dlg.traspaso.value = '<%= rec.getNumero() %>'
document.alm_traspasos_dlg.referencia.value = '<%= rec.getRef() %>'
document.alm_traspasos_dlg.concepto.value = '<%= rec.getConcepto() %>'
<%
	if( !request.getParameter("proceso").equals("CONSULTAR_TRASPASO") )
	{
%>	
document.alm_traspasos_dlg.cantidad.value = '<% if(request.getParameter("cantidad") != null) { out.print( request.getParameter("cantidad") ); } else { out.print("1"); } %>'
document.alm_traspasos_dlg.idprod.value = '<% if(request.getParameter("idprod") != null) { out.print( request.getParameter("idprod") ); } else { out.print(""); } %>'
document.alm_traspasos_dlg.idprod_nombre.value = '<% if(request.getParameter("idprod_nombre") != null) { out.print( request.getParameter("idprod_nombre") ); } else { out.print(""); } %>'
<%
	}
%>	
</script>
</body>
</html>
